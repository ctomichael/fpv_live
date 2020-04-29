package dji.midware.media.record;

import android.annotation.TargetApi;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.external.DJIBaseExternalStorageHelper;
import dji.midware.media.external.DJIExternalStorageController;
import dji.midware.media.record.RecorderManager;
import dji.midware.usb.P3.DJIUsbAccessoryReceiver;
import dji.midware.util.BytesUtil;
import dji.midware.util.DjiSharedPreferencesManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class ExternalSdRecordingHelper extends DJIBaseExternalStorageHelper {
    private static final boolean DEBUG = false;
    private static final String KEY_EXTERNAL_STORAGE_ENABLE = "video_cache_external_storage_enable";
    private static final String KEY_RECORDING_SRC_FILE_PATH = "external_sd_recording_source_file_path";
    private static final String TAG = "ExSdRecordingHelper";
    private String testOutputFilePath;

    public enum ExternalStorageUiEvent {
        CreateFileError,
        SdcardMounted,
        SdcardUnMounted
    }

    private static void logd(String tag, String log) {
    }

    public ExternalSdRecordingHelper(String filePath, String outputFile) {
        super(filePath);
        this.testOutputFilePath = outputFile;
        try {
            this.externalFileStream = new FileOutputStream(new File(this.testOutputFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ExternalSdRecordingHelper(String filePath, DocumentFile documentFile) {
        super(filePath, documentFile);
    }

    public void start() {
        if (checkDirSizeAndRelease()) {
            setSrcFilePathToSp(this.filePath);
            super.start();
            new Thread(new Runnable() {
                /* class dji.midware.media.record.ExternalSdRecordingHelper.AnonymousClass1 */

                public void run() {
                    do {
                        try {
                            if (RecorderManager.getRecordingFileName() != null) {
                                Thread.sleep((long) RecorderManager.storageSpaceCheckDuration);
                            } else {
                                return;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    } while (ExternalSdRecordingHelper.this.checkDirSizeAndRelease());
                    EventBus.getDefault().post(RecorderManager.Service_Action.END_RECORD);
                    EventBus.getDefault().post(RecorderManager.Video_Buffer_Notify.NO_SPACE);
                }
            }, "externalStart").start();
            return;
        }
        EventBus.getDefault().post(RecorderManager.Service_Action.END_RECORD);
        EventBus.getDefault().post(RecorderManager.Video_Buffer_Notify.NO_SPACE);
    }

    @TargetApi(19)
    public static boolean isSupportExternalSD() {
        return DJIExternalStorageController.isSupportExternalSD(DJIExternalStorageController.getContext());
    }

    public static boolean isExteranSDGranted() {
        return DJIExternalStorageController.getInstance().isExteranSDGranted();
    }

    public static void setVideoCacheExternalStorageEnable(boolean enable) {
        DjiSharedPreferencesManager.putBoolean(DJIExternalStorageController.getContext(), KEY_EXTERNAL_STORAGE_ENABLE, enable);
        EventBus.getDefault().post(new DJIExternalStorageController.ExternalStorageEvent(enable ? 6 : 7));
    }

    public static boolean getVideoCacheExternalStorageEnable() {
        return DjiSharedPreferencesManager.getBoolean(DJIExternalStorageController.getContext(), KEY_EXTERNAL_STORAGE_ENABLE, false);
    }

    public static DocumentFile checkAndCreateFile(DocumentFile parent, String mime, String name) {
        return DJIExternalStorageController.checkAndCreateFile(parent, mime, name);
    }

    public static DocumentFile getExternalSdRecordingDirDf() {
        DocumentFile rootDirDf;
        Uri rootUri = DJIExternalStorageController.getInstance().getGlobalTreeUriSAF();
        if (rootUri == null || (rootDirDf = DocumentFile.fromTreeUri(DJIExternalStorageController.getContext(), rootUri)) == null) {
            return null;
        }
        return DJIExternalStorageController.checkAndCreateDir(DJIExternalStorageController.checkAndCreateDir(DJIExternalStorageController.checkAndCreateDir(rootDirDf, DJIUsbAccessoryReceiver.myFacturer), DJIExternalStorageController.getContext().getPackageName()), "DJI_RECORD");
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
        } else if (!RecorderManager.getBufferAutoClean()) {
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
            List<DocumentFile> videoList = DJIExternalStorageController.getDocumentFileList(dir, "mp4");
            if (videoList == null || videoList.size() == 0) {
                logd(TAG, "checkDirSizeAndRelease: no mp4 file");
                return false;
            }
            Collections.sort(videoList, new Comparator<DocumentFile>() {
                /* class dji.midware.media.record.ExternalSdRecordingHelper.AnonymousClass2 */

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
                    String simpleName = DJIExternalStorageController.getSimpleName(df);
                    if (simpleName != null && !"".equals(simpleName) && (infoDf = dir.findFile(simpleName + ".info")) != null && infoDf.isFile()) {
                        logd(TAG, "checkDirSizeAndRelease: delete: " + infoDf.getName());
                        availableSize -= DJIExternalStorageController.deleteFile(infoDf);
                    }
                    logd(TAG, "checkDirSizeAndRelease: delete: " + df.getName());
                    availableSize -= DJIExternalStorageController.deleteFile(df);
                    if (availableSize > RecorderManager.targetReleaseSpace) {
                        logd(TAG, "checkDirSizeAndRelease: release done!");
                        EventBus.getDefault().post(RecorderManager.VIDEO_CACHE_EVENT.RELEASE_CACHE_DONE);
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

    public static long getAvailableSpace(DocumentFile srcDf) {
        return DJIExternalStorageController.getAvailableSpace(srcDf);
    }

    public static void copyToExternalFile(File srcFile, DocumentFile dstDf, boolean needRemoveSrc) throws IOException {
        DJIExternalStorageController.copyToExternalFile(srcFile, dstDf, needRemoveSrc);
    }

    public static void checkUnfinishedFile() {
        logd(TAG, "checkUnfinishedFile: 0");
        String filePath = getSrcFilePathFromSp();
        logd(TAG, "checkUnfinishedFile: filepath: " + filePath);
        if (isExteranSDGranted() && !"".equals(filePath)) {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                logd(TAG, "checkUnfinishedFile: path exist");
                file.delete();
            }
            String infoPath = filePath.substring(0, filePath.lastIndexOf(".")) + ".info";
            logd(TAG, "checkUnfinishedFile: info path: " + infoPath);
            File infoFile = new File(infoPath);
            if (infoFile.exists() && infoFile.isFile()) {
                logd(TAG, "checkUnfinishedFile: info path exist");
                DocumentFile infoDf = checkAndCreateFile(getExternalSdRecordingDirDf(), "", infoFile.getName());
                if (infoDf != null) {
                    logd(TAG, "checkUnfinishedFile: info df uri: " + infoDf.getUri());
                    try {
                        copyToExternalFile(infoFile, infoDf, true);
                    } catch (IOException e) {
                        Log.e(TAG, "checkUnfinishedFile: ", e);
                    }
                }
            }
            setSrcFilePathToSp("");
        }
    }

    private static void setSrcFilePathToSp(String path) {
        logd(TAG, "setSrcFilePathToSp: path: " + path);
        DjiSharedPreferencesManager.putString(DJIExternalStorageController.getContext(), KEY_RECORDING_SRC_FILE_PATH, path);
    }

    private static String getSrcFilePathFromSp() {
        logd(TAG, "getSrcFilePathFromSp: ");
        return DjiSharedPreferencesManager.getString(DJIExternalStorageController.getContext(), KEY_RECORDING_SRC_FILE_PATH, "");
    }

    private RandomAccessFile getStopCheckRaf() throws FileNotFoundException {
        return new RandomAccessFile(this.testOutputFilePath, "rw");
    }

    public void stop(boolean needDeleteSrcFile) {
        setSrcFilePathToSp("");
        if (this.testOutputFilePath != null) {
            try {
                this.copyController.stop(getStopCheckRaf(), getCheckLen());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.stop(needDeleteSrcFile);
    }

    /* access modifiers changed from: protected */
    public int getCheckLen() throws IOException {
        return locateMdatStartOffset() + 4;
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
        return DJIExternalStorageController.getInstance().getSecondaryStorage() + "/DJI/" + DJIExternalStorageController.getContext().getPackageName() + "/DJI_RECORD/";
    }
}
