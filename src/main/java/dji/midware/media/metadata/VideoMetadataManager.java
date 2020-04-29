package dji.midware.media.metadata;

import android.support.annotation.Keep;
import android.util.Log;
import com.dji.frame.util.V_DiskUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.utils.DJIProductSupportUtil;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.reflect.MidwareInjectManager;
import java.io.File;
import java.util.Locale;

@Keep
@EXClassNullAway
public class VideoMetadataManager {
    private static String TAG = "BufferedVideoDatabase";

    public static VideoRecordInfo getSourceInfo(String sourceName) {
        VideoRecordInfo info = new VideoRecordInfo();
        String infoPath = getSourceVideoDirectory() + sourceName + ".info";
        if (!new File(infoPath).exists()) {
            return null;
        }
        try {
            info.load(infoPath);
            return info;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getSourceVideoDirectory() {
        return MidwareInjectManager.getMidwareToVideoLibInjectable().getVideoCacheDirectoryPath();
    }

    public static String getMomentInfoDirectory() {
        ServiceManager.getInstance();
        String path = V_DiskUtil.getExternalCacheDirPath(ServiceManager.getContext(), "VideoDatabase/MomentInfo/");
        DJIVideoUtil.checkAndCreateDirectory(path);
        return path;
    }

    private static String getInfoPathFromVideoPath(String videoPath) {
        if (videoPath == null || videoPath.length() < 4) {
            MediaLogger.e(TAG, "unrecognised video file path");
            return videoPath;
        }
        String tmp = videoPath.toLowerCase(Locale.US);
        if (tmp.endsWith(".mp4") || tmp.endsWith(".mov")) {
            return tmp.substring(0, tmp.length() - 4) + ".info";
        }
        MediaLogger.e(TAG, "unrecognised video file path");
        return videoPath;
    }

    public static VideoRecordInfo createMomentInfo(String momentName, String sourceFileFullPath, int startTime, int endTime) {
        Log.i(TAG, "create a moment info: momentName=" + momentName + "; sourceFile=" + sourceFileFullPath + " startTime=" + startTime + " endTime=" + endTime);
        VideoRecordInfo vri = new VideoRecordInfo();
        if (sourceFileFullPath != null) {
            String infoPath = getInfoPathFromVideoPath(sourceFileFullPath);
            if (new File(infoPath).exists()) {
                try {
                    vri.load(infoPath);
                } catch (Exception e) {
                }
            } else if (loadMetadataFromOriginalVideo(vri, sourceFileFullPath)) {
                vri.setFileSourceType(2);
            }
            vri.setStartTimeMsec(startTime);
            vri.setEndTimeMsec(endTime);
            vri.setLocalFileName(momentName);
            vri.setSourceFilePath(sourceFileFullPath);
            vri.store(getMomentInfoPathFromMomentName(momentName));
        }
        return vri;
    }

    public static void deleteMoment(String momentName) {
        new File(getMomentInfoPathFromMomentName(momentName)).delete();
    }

    public static boolean existsMoment(String momentName) {
        return new File(getMomentInfoPathFromMomentName(momentName)).exists();
    }

    public static boolean existsSource(String sourceName) {
        return new File(getSourceVideoDirectory() + sourceName + ".info").exists();
    }

    private static String getMomentInfoPathFromMomentName(String momentName) {
        return getMomentInfoDirectory() + "moment_" + momentName + ".info";
    }

    private static String getMomentInfoPathFromMomentPath(String momentPath) {
        String name = new File(momentPath).getName();
        if (name == null || name.length() < 4 || !name.toLowerCase(Locale.US).endsWith(".mp4")) {
            return null;
        }
        return getMomentInfoPathFromMomentName(name.substring(0, name.length() - 4));
    }

    public static void deleteMomentInfo(String momentPath) {
        String infoPath = getMomentInfoPathFromMomentPath(momentPath);
        if (infoPath != null) {
            File infoFile = new File(infoPath);
            MediaLogger.i(TAG, "Delete " + infoFile.getAbsolutePath());
            infoFile.delete();
        }
    }

    public static VideoRecordInfo getMomentInfo(String momentName) {
        Log.i(TAG, "Request a moment info with name " + momentName);
        VideoRecordInfo vri = new VideoRecordInfo();
        String infoPath = getMomentInfoPathFromMomentName(momentName);
        if (new File(infoPath).exists()) {
            try {
                vri.load(infoPath);
                if (vri.getProductType() == null && loadMetadataFromOriginalVideo(vri, vri.getSourceFilePath())) {
                    vri.store(infoPath);
                }
            } catch (Exception e) {
            }
        } else if (loadMetadataFromOriginalVideo(vri, vri.getSourceFilePath())) {
            vri.store(infoPath);
        }
        return vri;
    }

    private static boolean loadMetadataFromOriginalVideo(VideoRecordInfo vri, String sourceFilePath) {
        if (!new File(sourceFilePath).exists()) {
            return false;
        }
        OriginalVideoMetadataRetriever mr = new OriginalVideoMetadataRetriever();
        try {
            mr.setDataSource(sourceFilePath);
            mr.extract();
            double[] gps = mr.getGPS();
            if (!(gps[0] == 0.0d || gps[1] == 0.0d)) {
                vri.setPositionGPSLng(gps[0]);
                vri.setPositionGPSLat(gps[1]);
            }
            vri.setProductType(DJIProductSupportUtil.getProductTypeFormExfMap(mr.getMap()));
            vri.setCaptureDate(mr.getCaptureDate());
            MediaLogger.i(TAG, "From drone. CaptureDate = " + vri.getCaptureDate());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
