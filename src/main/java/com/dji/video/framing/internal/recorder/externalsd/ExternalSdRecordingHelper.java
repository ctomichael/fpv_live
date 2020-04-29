package com.dji.video.framing.internal.recorder.externalsd;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.support.v4.provider.DocumentFile;
import android.text.TextUtils;
import android.util.Log;
import com.dji.video.framing.internal.recorder.RecorderManager;
import com.dji.video.framing.utils.BytesUtil;
import com.dji.video.framing.utils.DJIVideoUtil;
import com.dji.video.framing.utils.DjiSharedPreferencesManager;
import com.dji.video.framing.utils.FileStreamCopyController;
import dji.midware.usb.P3.DJIUsbAccessoryReceiver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ExternalSdRecordingHelper {
    private static final boolean DEBUG = false;
    private static final String DPAD_EXTERNAL_PATH_0 = "/mnt/external_sd";
    private static final String DPAD_EXTERNAL_PATH_1 = "/mnt/external_sd1";
    private static final String KEY_EXTERNAL_STORAGE_ENABLE = "video_cache_external_storage_enable";
    private static final String KEY_GLOBAL_TREE_URI_SAF = "global_tree_uri_saf";
    private static final String KEY_RECORDING_SRC_FILE_PATH = "external_sd_recording_source_file_path";
    public static String SECONDARY_STORAGE = null;
    private static final String TAG = "ExSdRecordingHelper";
    private static boolean isInited = false;
    private FileStreamCopyController copyController;
    private DocumentFile externalFileDf;
    private OutputStream externalFileStream;
    private String filePath;
    /* access modifiers changed from: private */
    public RecorderManager manager;
    private String testOutputFilePath;

    public enum ExternalStorageUiEvent {
        Open,
        Close,
        CreateFileError,
        SdcardMounted,
        SdcardUnMounted
    }

    private static void logd(String tag, String log) {
    }

    public static class ExternalStorageInfo {
        public Uri uri;

        public ExternalStorageInfo(Uri uri2) {
            this.uri = uri2;
        }
    }

    private ExternalSdRecordingHelper(RecorderManager manager2, String filePath2) {
        this.manager = manager2;
        this.filePath = filePath2;
        this.copyController = new FileStreamCopyController(filePath2);
    }

    public ExternalSdRecordingHelper(RecorderManager manager2, String filePath2, String outputFile) {
        this(manager2, filePath2);
        this.testOutputFilePath = outputFile;
        try {
            this.externalFileStream = new FileOutputStream(new File(this.testOutputFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ExternalSdRecordingHelper(RecorderManager manager2, String filePath2, DocumentFile documentFile) {
        this(manager2, filePath2);
        this.externalFileDf = documentFile;
        try {
            this.externalFileStream = DJIVideoUtil.getContext().getContentResolver().openOutputStream(documentFile.getUri());
        } catch (FileNotFoundException e) {
            Log.e(TAG, "ExternalSdRecordingHelper: open document file output stream failed", e);
        }
    }

    public void start() {
        if (checkDirSizeAndRelease()) {
            setSrcFilePathToSp(this.filePath);
            this.copyController.start(this.externalFileStream);
            new Thread(new Runnable() {
                /* class com.dji.video.framing.internal.recorder.externalsd.ExternalSdRecordingHelper.AnonymousClass1 */

                public void run() {
                    do {
                        try {
                            if (ExternalSdRecordingHelper.this.manager.getRecordingFileName() != null) {
                                Thread.sleep((long) RecorderManager.storageSpaceCheckDuration);
                            } else {
                                return;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    } while (ExternalSdRecordingHelper.this.checkDirSizeAndRelease());
                    ExternalSdRecordingHelper.this.manager.stop();
                    if (ExternalSdRecordingHelper.this.manager.getCallback() != null) {
                        ExternalSdRecordingHelper.this.manager.getCallback().onError(0);
                    }
                }
            }, "externalStart").start();
            return;
        }
        this.manager.stop();
        if (this.manager.getCallback() != null) {
            this.manager.getCallback().onError(0);
        }
    }

    public void pause() {
        if (this.copyController != null) {
            this.copyController.pause();
        }
    }

    public void resume() {
        if (this.copyController != null) {
            this.copyController.resume();
        }
    }

    @TargetApi(19)
    public static boolean isSupportExternalSD() {
        boolean z = true;
        if (Build.VERSION.SDK_INT < 21) {
            return false;
        }
        File[] files = DJIVideoUtil.getContext().getExternalCacheDirs();
        if (files.length >= 2 && files[1] != null) {
            String path = files[1].getAbsolutePath();
            logd(TAG, "isSupportExternalSD: " + path);
            path.indexOf("/Android");
            SECONDARY_STORAGE = path.substring(0, path.indexOf("/Android"));
        }
        if (Build.VERSION.SDK_INT < 21 || TextUtils.isEmpty(SECONDARY_STORAGE)) {
            z = false;
        }
        return z;
    }

    public static boolean isExteranSDGranted() {
        Uri uri;
        if (!isSupportExternalSD() || (uri = getGlobalTreeUriSAF()) == null || TextUtils.isEmpty(SECONDARY_STORAGE) || TextUtils.isEmpty(uri.toString())) {
            return false;
        }
        return true;
    }

    public static void unGrantExternalSd() {
        SECONDARY_STORAGE = null;
        putGlobalTreeUriSAF(null);
        setVideoCacheExternalStorageEnable(false);
    }

    public static void putGlobalTreeUriSAF(Uri treeUri) {
        DjiSharedPreferencesManager.putString(DJIVideoUtil.getContext(), KEY_GLOBAL_TREE_URI_SAF, treeUri == null ? "" : treeUri.toString());
    }

    public static Uri getGlobalTreeUriSAF() {
        String loc = DjiSharedPreferencesManager.getString(DJIVideoUtil.getContext(), KEY_GLOBAL_TREE_URI_SAF, "");
        if (loc == null || "".equals(loc)) {
            return null;
        }
        return Uri.parse(loc);
    }

    public static void setVideoCacheExternalStorageEnable(boolean enable) {
        DjiSharedPreferencesManager.putBoolean(DJIVideoUtil.getContext(), KEY_EXTERNAL_STORAGE_ENABLE, enable);
    }

    public static boolean getVideoCacheExternalStorageEnable() {
        return DjiSharedPreferencesManager.getBoolean(DJIVideoUtil.getContext(), KEY_EXTERNAL_STORAGE_ENABLE, false);
    }

    private static void setSrcFilePathToSp(String path) {
        logd(TAG, "setSrcFilePathToSp: path: " + path);
        DjiSharedPreferencesManager.putString(DJIVideoUtil.getContext(), KEY_RECORDING_SRC_FILE_PATH, path);
    }

    private static String getSrcFilePathFromSp() {
        logd(TAG, "getSrcFilePathFromSp: ");
        return DjiSharedPreferencesManager.getString(DJIVideoUtil.getContext(), KEY_RECORDING_SRC_FILE_PATH, "");
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

    public static DocumentFile getExternalSdRecordingDirDf() {
        DocumentFile rootDirDf;
        Uri rootUri = getGlobalTreeUriSAF();
        if (rootUri == null || (rootDirDf = DocumentFile.fromTreeUri(DJIVideoUtil.getContext(), rootUri)) == null) {
            return null;
        }
        return checkAndCreateDir(checkAndCreateDir(checkAndCreateDir(rootDirDf, DJIUsbAccessoryReceiver.myFacturer), DJIVideoUtil.getContext().getPackageName()), "DJI_RECORD");
    }

    private List<DocumentFile> getDocumentFileList(DocumentFile parent, String suffix) {
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

    private static String getSimpleName(DocumentFile df) {
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

    private static long deleteFile(DocumentFile df) {
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

    /* access modifiers changed from: private */
    public boolean checkDirSizeAndRelease() {
        DocumentFile infoDf;
        if (this.externalFileDf == null) {
            return false;
        }
        long availableSize = getAvailableSpace(this.externalFileDf.getParentFile());
        logd(TAG, "checkDirSizeAndRelease: avail size: " + (availableSize / RecorderManager.MB) + "MB");
        if (availableSize > RecorderManager.targetReleaseSpace) {
            logd(TAG, "checkDirSizeAndRelease: size OK");
            return true;
        } else if (!this.manager.getBufferAutoClean()) {
            logd(TAG, "checkDirSizeAndRelease: not allow auto clean");
            return false;
        } else if (this.externalFileDf == null) {
            logd(TAG, "checkDirSizeAndRelease: no external df");
            return false;
        } else {
            DocumentFile dir = this.externalFileDf.getParentFile();
            if (dir == null) {
                logd(TAG, "checkDirSizeAndRelease: no parent dir");
                return false;
            }
            List<DocumentFile> videoList = getDocumentFileList(dir, "mp4");
            if (videoList == null || videoList.size() == 0) {
                logd(TAG, "checkDirSizeAndRelease: no mp4 file");
                return false;
            }
            Collections.sort(videoList, new Comparator<DocumentFile>() {
                /* class com.dji.video.framing.internal.recorder.externalsd.ExternalSdRecordingHelper.AnonymousClass2 */

                public int compare(DocumentFile t0, DocumentFile t1) {
                    long cmp = t0.lastModified() - t1.lastModified();
                    if (cmp < 0) {
                        return -1;
                    }
                    if (cmp > 0) {
                        return 1;
                    }
                    return 0;
                }
            });
            for (DocumentFile df : videoList) {
                logd(TAG, "checkDirSizeAndRelease: cur df name: " + df.getName() + ", is file: " + df.isFile() + ", length: " + (df.length() / RecorderManager.MB) + "MB, external df name: " + this.externalFileDf.getName());
                if (!df.isFile() || df.getUri().equals(this.externalFileDf.getUri())) {
                    logd(TAG, "checkDirSizeAndRelease: is current file.");
                } else {
                    String simpleName = getSimpleName(df);
                    if (simpleName != null && !"".equals(simpleName) && (infoDf = dir.findFile(simpleName + ".info")) != null && infoDf.isFile()) {
                        logd(TAG, "checkDirSizeAndRelease: delete: " + infoDf.getName());
                        availableSize -= deleteFile(infoDf);
                    }
                    logd(TAG, "checkDirSizeAndRelease: delete: " + df.getName());
                    availableSize -= deleteFile(df);
                    if (availableSize > RecorderManager.targetReleaseSpace) {
                        logd(TAG, "checkDirSizeAndRelease: release done!");
                        if (this.manager.getCallback() != null) {
                            this.manager.getCallback().onSpaceReleaseFinish();
                        }
                        return true;
                    }
                }
            }
            if (getAvailableSpace(this.externalFileDf.getParentFile()) < RecorderManager.targetReleaseSpace) {
                logd(TAG, "checkDirSizeAndRelease: check end failed");
                return false;
            }
            logd(TAG, "checkDirSizeAndRelease: check end succeed");
            return true;
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
        if (!isExteranSDGranted() || srcDf == null) {
            return 0;
        }
        long availGlobal = new File(SECONDARY_STORAGE).getUsableSpace();
        logd(TAG, "getAvailableSpace: availGlobal: " + (availGlobal / RecorderManager.MB) + "MB");
        long sourceDirSize = getDocumentFileSize(srcDf);
        logd(TAG, "getAvailableSpace: source dir size: " + (sourceDirSize / RecorderManager.MB) + "MB");
        long availSelfDir = RecorderManager.getMaxBufferSpace() - sourceDirSize;
        logd(TAG, "getAvailableSpace: available self dir: " + (availSelfDir / RecorderManager.MB) + "MB");
        long rst = Math.max(0L, Math.min(availGlobal, availSelfDir));
        logd(TAG, "getAvailableSpace: rst: " + rst);
        return rst;
    }

    public static void copyToExternalFile(File srcFile, DocumentFile dstDf, boolean needRemoveSrc) throws IOException {
        if (srcFile != null && srcFile.exists() && srcFile.isFile() && dstDf != null && dstDf.exists() && dstDf.isFile()) {
            FileInputStream fis = new FileInputStream(srcFile);
            OutputStream os = DJIVideoUtil.getContext().getContentResolver().openOutputStream(dstDf.getUri());
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

    public static void init() {
        if (!isInited) {
            isInited = true;
            if (isExteranSDGranted()) {
                DocumentFile rootdf = DocumentFile.fromTreeUri(DJIVideoUtil.getContext(), getGlobalTreeUriSAF());
                if (rootdf == null || !rootdf.exists()) {
                    unGrantExternalSd();
                } else {
                    checkUnfinishedFile();
                }
            }
        }
    }

    public static void checkUnfinishedFile() {
        logd(TAG, "checkUnfinishedFile: 0");
        String filePath2 = getSrcFilePathFromSp();
        logd(TAG, "checkUnfinishedFile: filepath: " + filePath2);
        if (isExteranSDGranted() && !"".equals(filePath2)) {
            File file = new File(filePath2);
            if (file.exists() && file.isFile()) {
                logd(TAG, "checkUnfinishedFile: path exist");
                file.delete();
            }
            String infoPath = filePath2.substring(0, filePath2.lastIndexOf(".")) + ".info";
            logd(TAG, "checkUnfinishedFile: info path: " + infoPath);
            File infoFile = new File(infoPath);
            if (infoFile.exists() && infoFile.isFile()) {
                logd(TAG, "checkUnfinishedFile: info path exist");
                DocumentFile infoDf = checkAndCreateFile(getExternalSdRecordingDirDf(), "", infoFile.getName());
                logd(TAG, "checkUnfinishedFile: info df uri: " + infoDf.getUri());
                try {
                    copyToExternalFile(infoFile, infoDf, true);
                } catch (IOException e) {
                    Log.e(TAG, "checkUnfinishedFile: ", e);
                }
            }
            setSrcFilePathToSp("");
        }
    }

    private RandomAccessFile getStopCheckRaf() throws FileNotFoundException {
        return new RandomAccessFile(this.testOutputFilePath, "rw");
    }

    public void forceStop() {
        this.copyController.forceStop();
    }

    public void stop(boolean needDeleteSrcFile) {
        setSrcFilePathToSp("");
        if (this.testOutputFilePath != null) {
            try {
                this.copyController.stop(getStopCheckRaf(), locateMdatStartOffset() + 4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.externalFileDf != null) {
            try {
                this.copyController.stop(this.externalFileDf, locateMdatStartOffset() + 4, needDeleteSrcFile);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void streamRead(byte[] cache, RandomAccessFile raf) throws IOException {
        int readLen = 0;
        while (readLen < cache.length) {
            readLen += raf.read(cache, readLen, cache.length - readLen);
        }
    }

    private int locateMdatStartOffset() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(this.filePath, "r");
        int curOffset = 0;
        byte[] cache = new byte[8];
        byte[] sizeCache = new byte[4];
        byte[] nameCache = new byte[4];
        while (((long) curOffset) < raf.length()) {
            raf.seek((long) curOffset);
            streamRead(cache, raf);
            System.arraycopy(cache, 0, sizeCache, 0, 4);
            System.arraycopy(cache, 4, nameCache, 0, 4);
            long blockSize = BytesUtil.getUnsignedLong(sizeCache, ByteOrder.BIG_ENDIAN);
            String blockName = new String(nameCache);
            logd(TAG, "locateMdatStartOffset: size: " + blockSize + ", name: " + blockName + ", cur offset: " + curOffset);
            if ("mdat".equals(blockName)) {
                logd(TAG, "locateMdatStartOffset: rst=" + curOffset);
                raf.close();
                return curOffset;
            }
            curOffset = (int) (((long) curOffset) + blockSize);
        }
        raf.close();
        logd(TAG, "locateMdatStartOffset: failed");
        return -1;
    }

    public static String getExternalCachePath() {
        if (!isExteranSDGranted()) {
            return "";
        }
        return SECONDARY_STORAGE + "/DJI/" + DJIVideoUtil.getContext().getPackageName() + "/DJI_RECORD/";
    }
}
