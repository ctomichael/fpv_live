package dji.midware.media.record;

import android.graphics.Bitmap;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIAudioRecordWrapper;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.media.metadata.VideoMetadataManager;
import dji.midware.media.record.RecorderBase;
import dji.midware.natives.FPVController;
import dji.midware.util.DjiSharedPreferencesManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import kotlin.jvm.internal.LongCompanionObject;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class RecorderManager {
    private static boolean DEBUG = false;
    public static long GB = 1073741824;
    public static final String KEY_NEED_CACHE_AUDIO = "video_cache_need_audio";
    public static long MB = 1048576;
    private static String TAG = "RecorderManager";
    public static BufferMode bufferMode = null;
    public static int keyFrameInterfal = 5;
    private static boolean mVideoCacheAutoClean = true;
    private static long maxBufferSpace = (2 * GB);
    private static RecorderInterface recorder = null;
    public static int storageSpaceCheckDuration;
    public static final long targetReleaseSpace = (50 * MB);

    public enum Service_Action {
        START_RECORD,
        END_RECORD
    }

    public enum BufferMode {
        GDR_OFFLINE,
        GDR_ONLINE,
        GOP,
        FULL_FRAME_ENCODE,
        QUICK_MOVIE,
        SPEED_ADJUST
    }

    public enum VIDEO_CACHE_EVENT {
        RELEASE_CACHE_DONE
    }

    public enum Video_Buffer_Notify {
        NO_SPACE
    }

    static {
        storageSpaceCheckDuration = 10000;
        if (DJIVideoUtil.isDebug(DEBUG)) {
            storageSpaceCheckDuration = 5000;
            MediaLogger.show("Buffer space test is started");
        }
    }

    public static class SurfaceSaveEvent {
        private String filePath;

        public SurfaceSaveEvent(String filePath2) {
            this.filePath = filePath2;
        }

        public String getFilePath() {
            return this.filePath;
        }
    }

    public static void setMaxBufferSpace(long maxBufferSpace2) {
        maxBufferSpace = maxBufferSpace2;
    }

    public static long getMaxBufferSpace() {
        return maxBufferSpace;
    }

    public static void setBufferAutoClean(boolean isAuto) {
        mVideoCacheAutoClean = isAuto;
    }

    public static boolean getBufferAutoClean() {
        return mVideoCacheAutoClean;
    }

    public static boolean isRecording() {
        if (recorder == null || !(recorder instanceof RecorderBase)) {
            return false;
        }
        if (((RecorderBase) recorder).getCurrentStatus() == RecorderBase.RecorderStatus.RECORDING) {
            return true;
        }
        return false;
    }

    public static void setNeedCacheAudio(boolean need) {
        DjiSharedPreferencesManager.putBoolean(ServiceManager.getContext(), "video_cache_need_audio", need);
    }

    public static boolean needCacheAudio() {
        return DjiSharedPreferencesManager.getBoolean(ServiceManager.getContext(), "video_cache_need_audio", false);
    }

    public static boolean isRecordingToExternalSD() {
        if (recorder != null) {
            return recorder.isRecordingToExternalSd();
        }
        return false;
    }

    public static boolean needTranscoding() {
        return !DJIVideoUtil.isCurrentStreamGop();
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    public static void start(BufferMode bufferMode2) {
        destroy();
        bufferMode = bufferMode2;
        switch (bufferMode2) {
            case GDR_ONLINE:
                if (FPVController.native_isX264Enabled()) {
                    if (!needCacheAudio() || !DJIAudioRecordWrapper.getInstance().isRecorderPermissionGet()) {
                        recorder = RecorderMp4.getInstance();
                        return;
                    } else {
                        recorder = RecorderAudioMp4.getInstance();
                        return;
                    }
                }
                break;
            case FULL_FRAME_ENCODE:
                break;
            case GDR_OFFLINE:
                recorder = RecorderH264.getInstance();
                return;
            case GOP:
                recorder = RecorderGop.getInstance();
                return;
            case QUICK_MOVIE:
                recorder = RecorderQuickMovie.getInstance();
                return;
            case SPEED_ADJUST:
                recorder = RecorderSpeedAdjust.getInstance();
                return;
            default:
                return;
        }
        if (needTranscoding()) {
            DJILogHelper.getInstance().LOGD(TAG, "start: need transcode");
            if (!needCacheAudio() || !DJIAudioRecordWrapper.getInstance().isRecorderPermissionGet()) {
                recorder = RecorderFullFrame.getInstance();
            } else {
                recorder = RecorderAudioFullFrame.getInstance();
            }
        } else {
            DJILogHelper.getInstance().LOGD(TAG, "start: not need transcode");
            if (!needCacheAudio() || !DJIAudioRecordWrapper.getInstance().isRecorderPermissionGet()) {
                recorder = RecorderMp4.getInstance();
            } else {
                recorder = RecorderAudioMp4.getInstance();
            }
        }
    }

    public static RecorderInterface getCurrentRecorder() {
        return recorder;
    }

    public static void destroy() {
        RecorderMp4.destroy();
        RecorderAudioMp4.destroy();
        RecorderH264.destroy();
        RecorderGop.destroy();
        RecorderFullFrame.destroy();
        RecorderAudioFullFrame.destroy();
        RecorderQuickMovie.destroy();
        RecorderSpeedAdjust.destroy();
        recorder = null;
    }

    public static boolean checkAndReleaseBuffer() {
        long start = System.currentTimeMillis();
        boolean re = _checkAndReleaseBuffer();
        if (DEBUG) {
            MediaLogger.show("checkAndReleaseBuffer consumes time (ms): " + (System.currentTimeMillis() - start));
        }
        return re;
    }

    private static boolean _checkAndReleaseBuffer() {
        try {
            long avail = getAvailableSpace();
            if (avail >= targetReleaseSpace) {
                return true;
            }
            if (!mVideoCacheAutoClean) {
                return false;
            }
            String dir = VideoMetadataManager.getSourceVideoDirectory();
            if (dir == null) {
                return false;
            }
            File[] infoList = new File(dir).listFiles(new FilenameFilter() {
                /* class dji.midware.media.record.RecorderManager.AnonymousClass1 */

                public boolean accept(File dir, String filename) {
                    if (filename.endsWith(".mp4")) {
                        return true;
                    }
                    return false;
                }
            });
            if (infoList == null || infoList.length == 0) {
                return false;
            }
            Arrays.sort(infoList, new Comparator<File>() {
                /* class dji.midware.media.record.RecorderManager.AnonymousClass2 */

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
                    EventBus.getDefault().post(VIDEO_CACHE_EVENT.RELEASE_CACHE_DONE);
                    return true;
                }
            }
            if (getAvailableSpace() < targetReleaseSpace) {
                return false;
            }
            return true;
        } catch (Exception e) {
            MediaLogger.show(e);
            return true;
        }
    }

    private static long deleteFile(String fullPath, HashSet<String> filter) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> it2 = filter.iterator();
        while (it2.hasNext()) {
            builder.append(it2.next() + ";");
        }
        if (DEBUG) {
            MediaLogger.show("try to delete: " + fullPath + " filter: " + builder.toString());
        }
        if (filter == null || !filter.contains(fullPath)) {
            File file = new File(fullPath);
            long length = file.length();
            if (!file.delete()) {
                if (DEBUG) {
                    MediaLogger.show("NOT EXIST");
                }
                return 0;
            } else if (!DEBUG) {
                return length;
            } else {
                MediaLogger.show("SUCCESS");
                return length;
            }
        } else {
            if (DEBUG) {
                MediaLogger.show("REJECT");
            }
            return 0;
        }
    }

    public static long getAvailableSpace() {
        long min;
        long re;
        File recordDir = new File(VideoMetadataManager.getSourceVideoDirectory());
        if (!recordDir.exists() && !recordDir.mkdirs()) {
            return 0;
        }
        long avail_globle = recordDir.getUsableSpace();
        long avail_self_dir = (isRecordingToExternalSD() || (ExternalSdRecordingHelper.getVideoCacheExternalStorageEnable() && ExternalSdRecordingHelper.isExteranSDGranted())) ? LongCompanionObject.MAX_VALUE : maxBufferSpace - getDirectorySize(new File(VideoMetadataManager.getSourceVideoDirectory()));
        if (avail_globle < avail_self_dir) {
            min = avail_globle;
        } else {
            min = avail_self_dir;
        }
        if (min < 0) {
            re = 0;
        } else {
            re = min;
        }
        if (DEBUG) {
            MediaLogger.show("Available space: " + re);
        }
        return re;
    }

    private static long getDirectorySize(File folder) {
        long directorySize;
        if (folder == null || !folder.exists()) {
            return 0;
        }
        if (!folder.isDirectory()) {
            return folder.length();
        }
        long length = 0;
        File[] listFiles = folder.listFiles();
        for (File file : listFiles) {
            if (file.isFile()) {
                directorySize = file.length();
            } else {
                directorySize = getDirectorySize(file);
            }
            length += directorySize;
        }
        return length;
    }

    private static HashSet<String> makeProtectorForOpenedFile() {
        String dir = VideoMetadataManager.getSourceVideoDirectory();
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

    public static void clearBuffer() {
        if (DEBUG) {
            MediaLogger.show("clearBuffer()");
        }
        String dir = VideoMetadataManager.getSourceVideoDirectory();
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
                                MediaLogger.show("delete directory:" + fileEntry.getAbsolutePath());
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
                            MediaLogger.show("delete a file:" + fileEntry.getAbsolutePath());
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

    public static String getRecordingFileName() {
        if (recorder == null) {
            return null;
        }
        return recorder.getRecordingFileName();
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
