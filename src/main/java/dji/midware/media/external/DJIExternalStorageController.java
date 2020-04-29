package dji.midware.media.external;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.v4.provider.DocumentFile;
import android.text.TextUtils;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.media.record.RecorderManager;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.publics.DJIExecutor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

@Keep
public class DJIExternalStorageController {
    public static final boolean DEBUG = false;
    private static final String DPAD_EXTERNAL_PATH_0 = "/mnt/external_sd";
    private static final String DPAD_EXTERNAL_PATH_1 = "/mnt/external_sd1";
    private static final String KEY_GLOBAL_TREE_URI_SAF = "global_tree_uri_saf";
    private static final String TAG = "DJIExternalStorageHelper";
    /* access modifiers changed from: private */
    public static boolean isInited = false;
    private Context mContext;
    private String mSecondaryStorage = null;

    @Keep
    private static final class Holder {
        /* access modifiers changed from: private */
        public static final DJIExternalStorageController INSTANCE = new DJIExternalStorageController();

        private Holder() {
        }
    }

    public static DJIExternalStorageController getInstance() {
        return Holder.INSTANCE;
    }

    @Keep
    public static class ExternalStorageEvent {
        public static final int CREATE_FILE_ERROR = 3;
        public static final int DOWNLOAD_TO_SDCARD_DISABLE = 5;
        public static final int DOWNLOAD_TO_SDCARD_ENABLE = 4;
        public static final int RECORDING_TO_SDCARD_DISABLE = 7;
        public static final int RECORDING_TO_SDCARD_ENABLE = 6;
        public static final int SDCARD_MOUNTED = 1;
        public static final int SDCARD_UNMOUNTED = 2;
        private final int mEventAction;

        public @interface EventAction {
        }

        public ExternalStorageEvent(@EventAction int eventAction) {
            this.mEventAction = eventAction;
        }

        @EventAction
        public int getEventAction() {
            return this.mEventAction;
        }
    }

    @Keep
    public static class ExternalStorageInfo {
        public final int identity;
        public final Uri uri;

        public ExternalStorageInfo(Uri uri2, int identity2) {
            this.uri = uri2;
            this.identity = identity2;
        }
    }

    @Keep
    public static class SdCardBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.MEDIA_MOUNTED".equals(action)) {
                DJIExternalStorageController.logd("sd卡已挂载");
                DJIExternalStorageController.getInstance().init(context);
                EventBus.getDefault().post(new ExternalStorageEvent(1));
            } else if ("android.intent.action.MEDIA_UNMOUNTED".equals(action)) {
                boolean unused = DJIExternalStorageController.isInited = false;
                EventBus.getDefault().post(new ExternalStorageEvent(2));
                DJIExternalStorageController.logd("sd卡已卸载");
            }
        }
    }

    public String getSecondaryStorage() {
        return this.mSecondaryStorage;
    }

    public void init(Context context) {
        if (!isInited) {
            this.mContext = context;
            isInited = true;
            if (isExteranSDGranted()) {
                DocumentFile rootdf = DocumentFile.fromTreeUri(context, getGlobalTreeUriSAF());
                if (rootdf == null || !rootdf.exists()) {
                    unGrantExternalSd();
                } else {
                    DJIExecutor.getExecutorFor(DJIExecutor.Purpose.IO).execute(new Runnable() {
                        /* class dji.midware.media.external.DJIExternalStorageController.AnonymousClass1 */

                        public void run() {
                            DJIExternalStorageActionDispatcher.checkUnfinishedFile();
                        }
                    });
                }
            }
        }
    }

    @TargetApi(19)
    public boolean isSupportExternalSD() {
        return isSupportExternalSD(this.mContext);
    }

    @TargetApi(19)
    public static boolean isSupportExternalSD(Context ctx) {
        boolean z = true;
        if (Build.VERSION.SDK_INT < 21) {
            return false;
        }
        if (!DpadProductManager.getInstance().isDpad() || DpadProductManager.getInstance().isRM500()) {
            File[] files = ctx.getExternalCacheDirs();
            if (files.length >= 2 && files[1] != null) {
                String path = files[1].getAbsolutePath();
                logd("isSupportExternalSD: " + path);
                path.indexOf("/Android");
                getInstance().mSecondaryStorage = path.substring(0, path.indexOf("/Android"));
            }
        } else {
            File external0 = new File(DPAD_EXTERNAL_PATH_0);
            File external1 = new File(DPAD_EXTERNAL_PATH_1);
            if (external0.exists() && external0.getUsableSpace() > 0) {
                getInstance().mSecondaryStorage = DPAD_EXTERNAL_PATH_0;
            } else if (external1.exists() && external1.getUsableSpace() > 0) {
                getInstance().mSecondaryStorage = DPAD_EXTERNAL_PATH_1;
            }
        }
        if (Build.VERSION.SDK_INT < 21 || TextUtils.isEmpty(getInstance().getSecondaryStorage())) {
            z = false;
        }
        return z;
    }

    public boolean isExteranSDGranted() {
        Uri uri;
        if (!isSupportExternalSD() || (uri = getGlobalTreeUriSAF()) == null || TextUtils.isEmpty(getInstance().getSecondaryStorage()) || TextUtils.isEmpty(uri.toString())) {
            return false;
        }
        return true;
    }

    private void unGrantExternalSd() {
        getInstance().mSecondaryStorage = null;
        putGlobalTreeUriSAF(null);
        DJIExternalStorageActionDispatcher.unGrantExternalSd();
    }

    public void putGlobalTreeUriSAF(Uri treeUri) {
        DjiSharedPreferencesManager.putString(this.mContext, KEY_GLOBAL_TREE_URI_SAF, treeUri == null ? "" : treeUri.toString());
    }

    public Uri getGlobalTreeUriSAF() {
        String loc = DjiSharedPreferencesManager.getString(this.mContext, KEY_GLOBAL_TREE_URI_SAF, "");
        if (TextUtils.isEmpty(loc)) {
            return null;
        }
        return Uri.parse(loc);
    }

    public static DocumentFile checkAndCreateDir(DocumentFile parent, String dirName) {
        if (parent == null || !parent.exists() || !parent.isDirectory()) {
            return null;
        }
        DocumentFile df = parent.findFile(dirName);
        if (df != null && df.exists()) {
            return df;
        }
        try {
            return parent.createDirectory(dirName);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static DocumentFile checkAndCreateFile(DocumentFile parent, String mime, String name) {
        if (parent == null || !parent.exists() || !parent.isDirectory()) {
            return null;
        }
        DocumentFile df = parent.findFile(name);
        if (df == null || !df.exists()) {
            return parent.createFile(mime, name);
        }
        return df;
    }

    public static void copyToExternalFile(File srcFile, DocumentFile dstDf, boolean needRemoveSrc) throws IOException {
        if (srcFile != null && srcFile.exists() && srcFile.isFile() && dstDf != null && dstDf.exists() && dstDf.isFile()) {
            FileInputStream fis = new FileInputStream(srcFile);
            OutputStream os = getContext().getContentResolver().openOutputStream(dstDf.getUri());
            byte[] cache = new byte[1024];
            while (true) {
                int rst = fis.read(cache);
                if (rst <= 0) {
                    break;
                }
                os.write(cache, 0, rst);
            }
            fis.close();
            os.flush();
            os.close();
            if (needRemoveSrc) {
                srcFile.delete();
            }
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.Math.max(long, long):long}
     arg types: [int, long]
     candidates:
      ClspMth{java.lang.Math.max(double, double):double}
      ClspMth{java.lang.Math.max(int, int):int}
      ClspMth{java.lang.Math.max(float, float):float}
      ClspMth{java.lang.Math.max(long, long):long} */
    public static long getAvailableSpace(DocumentFile srcDf) {
        if (!getInstance().isExteranSDGranted() || srcDf == null) {
            return 0;
        }
        long availGlobal = new File(getInstance().getSecondaryStorage()).getUsableSpace();
        logd("getAvailableSpace: availGlobal: " + (availGlobal / RecorderManager.MB) + "MB");
        long sourceDirSize = getDocumentFileSize(srcDf);
        logd("getAvailableSpace: source dir size: " + (sourceDirSize / RecorderManager.MB) + "MB");
        long availSelfDir = RecorderManager.getMaxBufferSpace() - sourceDirSize;
        logd("getAvailableSpace: available self dir: " + (availSelfDir / RecorderManager.MB) + "MB");
        long rst = Math.max(0L, Math.min(availGlobal, availSelfDir));
        logd("getAvailableSpace: rst: " + rst);
        return rst;
    }

    private static long getDocumentFileSize(DocumentFile inputDf) {
        if (!inputDf.exists()) {
            return 0;
        }
        if (inputDf.isFile()) {
            return inputDf.length();
        }
        if (!inputDf.isDirectory()) {
            return 0;
        }
        long rst = 0;
        for (DocumentFile df : inputDf.listFiles()) {
            rst += getDocumentFileSize(df);
        }
        return rst;
    }

    public static List<DocumentFile> getDocumentFileList(DocumentFile parent, String suffix) {
        if (parent == null) {
            return null;
        }
        LinkedList<DocumentFile> rst = new LinkedList<>();
        DocumentFile[] listFiles = parent.listFiles();
        for (DocumentFile df : listFiles) {
            if (df != null && df.isFile() && df.getName().endsWith(suffix)) {
                rst.add(df);
            }
        }
        return rst;
    }

    public static long deleteFile(DocumentFile df) {
        if (df == null) {
            return -1;
        }
        if (df.isDirectory()) {
            long totalLength = 0;
            for (DocumentFile subDf : df.listFiles()) {
                long delRst = deleteFile(subDf);
                if (delRst > 0) {
                    totalLength += delRst;
                }
            }
            df.delete();
            return totalLength;
        }
        long length = df.length();
        df.delete();
        return length;
    }

    public static String getSimpleName(DocumentFile df) {
        if (df == null) {
            return null;
        }
        if (df.isDirectory()) {
            return df.getName();
        }
        String[] splitRst = df.getName().split("\\.");
        if (splitRst.length <= 2) {
            return splitRst[0];
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < splitRst.length - 1) {
            sb.append(splitRst[i]).append(i == splitRst.length + -2 ? "" : ".");
            i++;
        }
        return sb.toString();
    }

    @Nullable
    public static String getExtension(File file) {
        String fileFullName;
        int dotIndex;
        if (file == null || file.isDirectory() || (dotIndex = (fileFullName = file.getName()).lastIndexOf(".")) == -1) {
            return null;
        }
        return fileFullName.substring(dotIndex + 1);
    }

    /* access modifiers changed from: private */
    public static void logd(String log) {
    }

    public static Context getContext() {
        return getInstance().mContext;
    }
}
