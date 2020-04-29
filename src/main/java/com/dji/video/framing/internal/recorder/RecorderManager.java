package com.dji.video.framing.internal.recorder;

import android.graphics.Bitmap;
import android.util.Log;
import com.dji.video.framing.VideoLog;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import com.dji.video.framing.internal.recorder.RecorderBase;
import com.dji.video.framing.internal.recorder.externalsd.ExternalSdRecordingHelper;
import com.dji.video.framing.utils.DJIVideoUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import kotlin.jvm.internal.LongCompanionObject;

public class RecorderManager {
    private static boolean DEBUG = false;
    public static final int ERROR_FILE_CREATE_FAILED = 1;
    public static final int ERROR_NOT_ENOUGH_SPACE = 0;
    public static long GB = 1073741824;
    public static final String KEY_NEED_CACHE_AUDIO = "video_cache_need_audio";
    public static long MB = 1048576;
    private static String TAG = "RecorderManager";
    private static long maxBufferSpace = (2 * GB);
    public static int storageSpaceCheckDuration;
    public static final long targetReleaseSpace = (50 * MB);
    public BufferMode bufferMode = null;
    private RecorderManagerCallback callback;
    private boolean mVideoCacheAutoClean = true;
    private String recordDir;
    private RecorderBase recorder = null;

    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RecordeErrorCode {
    }

    public interface RecorderManagerCallback {
        void onError(int i);

        void onFileCompleted(String str, double d);

        void onFileCreated();

        void onMuxerStarted();

        void onMuxerStoped();

        void onSpaceReleaseFinish();
    }

    public enum VIDEO_CACHE_EVENT {
        RELEASE_CACHE_DONE
    }

    public enum BufferMode {
        Direct,
        AudioDirect,
        Transcode,
        AudioTranscode
    }

    static {
        storageSpaceCheckDuration = 10000;
        if (DJIVideoUtil.isDebug(DEBUG)) {
            storageSpaceCheckDuration = 5000;
            VideoLog.d("Buffer space test is started", new Object[0]);
        }
    }

    public String getRecordDir() {
        return this.recordDir;
    }

    public void setRecordDir(String recordDir2) {
        this.recordDir = recordDir2;
    }

    public RecorderManagerCallback getCallback() {
        return this.callback;
    }

    public void setCallback(RecorderManagerCallback callback2) {
        this.callback = callback2;
    }

    /* access modifiers changed from: package-private */
    public boolean isCurrentRecorder(RecorderBase recorder2) {
        return this.recorder == recorder2;
    }

    public static void setMaxBufferSpace(long maxBufferSpace2) {
        maxBufferSpace = maxBufferSpace2;
    }

    public static long getMaxBufferSpace() {
        return maxBufferSpace;
    }

    public void setBufferAutoClean(boolean isAuto) {
        this.mVideoCacheAutoClean = isAuto;
    }

    public boolean getBufferAutoClean() {
        return this.mVideoCacheAutoClean;
    }

    public boolean isRecording() {
        if (this.recorder == null || this.recorder.getCurrentStatus() != RecorderBase.RecorderStatus.Recording) {
            return false;
        }
        return true;
    }

    public boolean isRecordingToExternalSD() {
        if (this.recorder != null) {
            return this.recorder.isRecordingToExternalSd();
        }
        return false;
    }

    public void start(BufferMode bufferMode2, DJIVideoDecoder decoder) {
        destroy();
        this.bufferMode = bufferMode2;
        String fileName = DJIVideoUtil.getOutputFileNameWithoutSuffix();
        switch (bufferMode2) {
            case Direct:
                this.recorder = new RecorderMp4(this, decoder, fileName);
                break;
            case AudioDirect:
                this.recorder = new RecorderAudioMp4(this, decoder, fileName);
                break;
            case Transcode:
                this.recorder = new RecorderFullFrame(this, decoder, fileName);
                break;
            case AudioTranscode:
                this.recorder = new RecorderAudioFullFrame(this, decoder, fileName);
                break;
        }
        this.recorder.startRecord();
    }

    public void stop() {
        if (this.recorder != null) {
            this.recorder.stopRecord();
        }
    }

    public void destroy() {
        if (this.recorder != null) {
            this.recorder.destroy();
            this.recorder = null;
        }
    }

    public String getRecordingFilePath() {
        if (this.recorder != null) {
            return this.recorder.getRecordingFilePath();
        }
        return null;
    }

    public boolean checkAndReleaseBuffer() {
        long start = System.currentTimeMillis();
        boolean re = _checkAndReleaseBuffer();
        if (DEBUG) {
            VideoLog.d("checkAndReleaseBuffer consumes time (ms): " + (System.currentTimeMillis() - start), new Object[0]);
        }
        return re;
    }

    private boolean _checkAndReleaseBuffer() {
        try {
            long avail = getAvailableSpace();
            if (avail >= targetReleaseSpace) {
                return true;
            }
            if (!this.mVideoCacheAutoClean) {
                return false;
            }
            String dir = this.recordDir;
            if (dir == null) {
                return false;
            }
            File[] infoList = new File(dir).listFiles(new FilenameFilter() {
                /* class com.dji.video.framing.internal.recorder.RecorderManager.AnonymousClass1 */

                public boolean accept(File dir, String filename) {
                    if (RecorderManager.this.isCacheVideo(filename)) {
                        return true;
                    }
                    return false;
                }
            });
            if (infoList == null || infoList.length == 0) {
                return false;
            }
            Arrays.sort(infoList, new Comparator<File>() {
                /* class com.dji.video.framing.internal.recorder.RecorderManager.AnonymousClass2 */

                public int compare(File a, File b) {
                    long cmp = a.lastModified() - b.lastModified();
                    if (cmp < 0) {
                        return -1;
                    }
                    if (cmp > 0) {
                        return 1;
                    }
                    return 0;
                }
            });
            HashSet<String> filter = makeProtectorForOpenedFile();
            for (File aFile : infoList) {
                String fileNameDel = aFile.getAbsolutePath();
                String removeSuffix = fileNameDel.substring(0, fileNameDel.length() - (fileNameDel.endsWith(".mp4") ? ".mp4" : ".h264").length());
                avail = avail + deleteFile(removeSuffix + ".mp4", filter) + deleteFile(removeSuffix + ".h264", filter) + deleteFile(removeSuffix + ".jpg", filter) + deleteFile(removeSuffix + ".info", filter) + deleteFile(removeSuffix + ".m4a", filter);
                if (avail > targetReleaseSpace) {
                    if (this.callback != null) {
                        this.callback.onSpaceReleaseFinish();
                    }
                    return true;
                }
            }
            if (getAvailableSpace() < targetReleaseSpace) {
                return false;
            }
            return true;
        } catch (Exception e) {
            VideoLog.e(TAG, e);
            return true;
        }
    }

    /* access modifiers changed from: private */
    public boolean isCacheVideo(String fileName) {
        return fileName.endsWith("_Cache.mp4");
    }

    private static long deleteFile(String fullPath, HashSet<String> filter) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> it2 = filter.iterator();
        while (it2.hasNext()) {
            builder.append(it2.next() + ";");
        }
        if (DEBUG) {
            VideoLog.d("try to delete: " + fullPath + " filter: " + builder.toString(), new Object[0]);
        }
        if (filter == null || !filter.contains(fullPath)) {
            File file = new File(fullPath);
            long length = file.length();
            if (!file.delete()) {
                if (DEBUG) {
                    VideoLog.d("NOT EXIST", new Object[0]);
                }
                return 0;
            } else if (!DEBUG) {
                return length;
            } else {
                VideoLog.d("SUCCESS", new Object[0]);
                return length;
            }
        } else {
            if (DEBUG) {
                VideoLog.d("REJECT", new Object[0]);
            }
            return 0;
        }
    }

    public long getAvailableSpace() {
        long min;
        long re = 0;
        long avail_globle = new File(this.recordDir).getUsableSpace();
        long avail_self_dir = (isRecordingToExternalSD() || (ExternalSdRecordingHelper.getVideoCacheExternalStorageEnable() && ExternalSdRecordingHelper.isExteranSDGranted())) ? LongCompanionObject.MAX_VALUE : maxBufferSpace - getCacheVideoSize(new File(this.recordDir));
        if (avail_globle < avail_self_dir) {
            min = avail_globle;
        } else {
            min = avail_self_dir;
        }
        if (min >= 0) {
            re = min;
        }
        if (DEBUG) {
            VideoLog.d("Available space: " + re, new Object[0]);
        }
        return re;
    }

    private long getCacheVideoSize(File folder) {
        if (folder == null || !folder.exists()) {
            return 0;
        }
        long length = 0;
        File[] listFiles = folder.listFiles();
        for (File file : listFiles) {
            if (file.isFile() && isCacheVideo(file.getName())) {
                length += file.length();
            }
        }
        return length;
    }

    private HashSet<String> makeProtectorForOpenedFile() {
        String dir = this.recordDir;
        if (dir == null) {
            return null;
        }
        HashSet<String> filter = new HashSet<>();
        String recordingFile = getRecordingFileName();
        if (recordingFile == null) {
            return filter;
        }
        filter.add(dir + recordingFile + ".h264");
        Log.i(TAG, "filter: " + dir + recordingFile + ".h264");
        filter.add(dir + recordingFile + ".mp4");
        Log.i(TAG, "filter: " + dir + recordingFile + ".mp4");
        filter.add(dir + recordingFile + ".info");
        Log.i(TAG, "filter: " + dir + recordingFile + ".info");
        filter.add(dir + recordingFile + ".jpg");
        Log.i(TAG, "filter: " + dir + recordingFile + ".jpg");
        filter.add(dir + recordingFile + ".aac");
        Log.i(TAG, "filter: " + dir + recordingFile + ".aac");
        filter.add(dir + recordingFile + ".m4a");
        Log.i(TAG, "filter: " + dir + recordingFile + ".m4a");
        return filter;
    }

    public void clearBuffer() {
        if (DEBUG) {
            VideoLog.d("clearBuffer()", new Object[0]);
        }
        String dir = this.recordDir;
        if (dir != null) {
            deleteFolder(dir, makeProtectorForOpenedFile());
        }
    }

    private static void deleteFolder(String dir, HashSet<String> filter) {
        File folder = new File(dir);
        if (folder.exists()) {
            File[] listFiles = folder.listFiles();
            for (File fileEntry : listFiles) {
                if (fileEntry.isDirectory()) {
                    if (!filter.contains(fileEntry.getAbsolutePath())) {
                        deleteFolder(fileEntry.getAbsolutePath(), filter);
                        try {
                            if (DEBUG) {
                                VideoLog.d("delete directory:" + fileEntry.getAbsolutePath(), new Object[0]);
                            }
                            fileEntry.delete();
                            Log.i(TAG, "deleted " + fileEntry.getAbsolutePath());
                        } catch (Exception e) {
                            Log.i(TAG, "failed to delete " + fileEntry.getAbsolutePath());
                        }
                    }
                } else if (!filter.contains(fileEntry.getAbsolutePath())) {
                    try {
                        if (DEBUG) {
                            VideoLog.d("delete a file:" + fileEntry.getAbsolutePath(), new Object[0]);
                        }
                        fileEntry.delete();
                        Log.i(TAG, "deleted " + fileEntry.getAbsolutePath());
                    } catch (Exception e2) {
                        Log.i(TAG, "failed to delete " + fileEntry.getAbsolutePath());
                    }
                }
            }
        }
    }

    public String getRecordingFileName() {
        if (this.recorder == null) {
            return null;
        }
        return this.recorder.getRecordingFileName();
    }

    public static void setBitmap(Bitmap bitmap, String filePath) {
        if (bitmap != null) {
            Log.i(TAG, "is now saving a bitmap to a file");
            try {
                FileOutputStream thumbFileOS = new FileOutputStream(new File(filePath));
                if (thumbFileOS != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, thumbFileOS);
                    thumbFileOS.flush();
                    thumbFileOS.close();
                }
            } catch (FileNotFoundException e) {
                Log.e(TAG, "error in saving thumb 1");
                e.printStackTrace();
            } catch (IOException e2) {
                Log.e(TAG, "error in saving thumb 2");
                e2.printStackTrace();
            }
        }
    }
}
