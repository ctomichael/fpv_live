package dji.pilot.playback;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import com.dji.frame.util.MD5;
import dji.component.accountcenter.IMemberProtocol;
import dji.component.mediaprovider.DJIMediaProviderConstant;
import dji.component.playback.model.PlaybackFileInfo;
import dji.component.playback.model.PlaybackFileType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import java.text.DecimalFormat;
import java.util.Locale;

public class PlaybackPathManager {
    public static String CACHE_DIRECTORY;
    private static String DOWNLOAD_DIRECTORY_INTELLIGENT;
    private static String DOWNLOAD_DIRECTORY_PHOTO;
    private static String DOWNLOAD_DIRECTORY_VIDEO;
    private static String LTM_DIRECTORY_PHOTO;

    public static void setup(@NonNull Context applicationContext) {
        CACHE_DIRECTORY = Environment.getExternalStorageDirectory() + "/DJI/" + applicationContext.getPackageName() + "/CACHE_IMAGE";
        DOWNLOAD_DIRECTORY_PHOTO = Environment.getExternalStorageDirectory() + "/DJI/" + applicationContext.getPackageName() + DJIMediaProviderConstant.PHOTO_ORG;
        DOWNLOAD_DIRECTORY_VIDEO = Environment.getExternalStorageDirectory() + "/DJI/" + applicationContext.getPackageName() + DJIMediaProviderConstant.VIDEO_ORG;
        DOWNLOAD_DIRECTORY_INTELLIGENT = Environment.getExternalStorageDirectory() + "/DJI/" + applicationContext.getPackageName() + DJIMediaProviderConstant.INTELLIGENT_ORG;
        LTM_DIRECTORY_PHOTO = Environment.getExternalStorageDirectory() + "/DJI/" + applicationContext.getPackageName() + DJIMediaProviderConstant.LTM_CACHE;
    }

    @NonNull
    public static String getDownloadPhotoDir(@NonNull String fileName) {
        return DOWNLOAD_DIRECTORY_PHOTO + IMemberProtocol.PARAM_SEPERATOR + fileName;
    }

    @NonNull
    public static String getDownloadVideoDir(@NonNull String fileName) {
        return DOWNLOAD_DIRECTORY_VIDEO + IMemberProtocol.PARAM_SEPERATOR + fileName;
    }

    @NonNull
    public static String getDownloadIntelligentDir(@NonNull String fileName) {
        return DOWNLOAD_DIRECTORY_INTELLIGENT + IMemberProtocol.PARAM_SEPERATOR + fileName;
    }

    @NonNull
    public static String getLTMPhotoDir(@NonNull String fileName) {
        String path = LTM_DIRECTORY_PHOTO + IMemberProtocol.PARAM_SEPERATOR + fileName;
        return path.substring(0, path.lastIndexOf(".")) + ".jpg";
    }

    @NonNull
    public static String getCacheFileDir(@NonNull String fileName) {
        return CACHE_DIRECTORY + IMemberProtocol.PARAM_SEPERATOR + fileName;
    }

    @NonNull
    private static String getExt(@NonNull PlaybackFileInfo info) {
        return "." + info.fileType.toString().toLowerCase(Locale.ENGLISH);
    }

    @NonNull
    public static String getMd5(@NonNull PlaybackFileInfo info) {
        return MD5.getMD5For16(info.index + "_" + info.createTimeOrg + "_" + info.length);
    }

    @NonNull
    public static String getNameKey(@NonNull PlaybackFileInfo info) {
        if (info.fileType == PlaybackFileType.TIF || info.fileType == PlaybackFileType.BOKEH || info.fileType == PlaybackFileType.PANO) {
            return "org_" + MD5.getMD5For16(info.index + "_" + info.createTimeOrg + "_" + info.length) + "_" + info.createTime + ".jpg";
        }
        return "org_" + MD5.getMD5For16(info.index + "_" + info.createTimeOrg + "_" + info.length) + "_" + info.createTime + getExt(info);
    }

    @NonNull
    public static String getThumbNameKey(@NonNull PlaybackFileInfo info) {
        return "thumb_" + MD5.getMD5For16(info.index + "_" + info.createTimeOrg + "_" + info.length) + "_" + info.createTime + ".jpg";
    }

    @NonNull
    public static String getScreenNameKey(@NonNull PlaybackFileInfo info) {
        return "screen_" + MD5.getMD5For16(info.index + "_" + info.createTimeOrg + "_" + info.length) + "_" + info.createTime + ".jpg";
    }

    @NonNull
    public static String getExifNameKey(@NonNull PlaybackFileInfo info) {
        return "exif_" + MD5.getMD5For16(info.index + "_" + info.createTimeOrg) + "_" + new DecimalFormat("00").format((long) info.subIndex) + ".exif";
    }

    @NonNull
    public static String getBokehGroupNameKey(@NonNull PlaybackFileInfo info) {
        return "bokeh_" + MD5.getMD5For16(info.index + "_" + info.createTimeOrg);
    }

    @NonNull
    public static String getBokehNameKey(@NonNull PlaybackFileInfo info) {
        return "bokeh_" + MD5.getMD5For16(info.index + "_" + info.createTimeOrg) + "_" + new DecimalFormat("00").format((long) info.subIndex) + ".jpg";
    }

    @NonNull
    public static String getPanoramaGroupNameKey(@NonNull PlaybackFileInfo info) {
        return "panorama_" + MD5.getMD5(info.index + "_" + info.createTimeOrg);
    }

    @NonNull
    public static String getPanoramaNameKey(@NonNull PlaybackFileInfo info) {
        return "panorama_" + MD5.getMD5For16(info.index + "_" + info.createTimeOrg) + "_" + new DecimalFormat("00").format((long) info.subIndex) + ".jpg";
    }

    @NonNull
    public static String getPanoNameKey(@NonNull PlaybackFileInfo info) {
        return "pano_" + MD5.getMD5For16(info.subIndex + "_" + info.createTimeOrg + "_" + info.length) + "_" + info.createTime + ".jpg";
    }

    @NonNull
    public static String getFullPanoNameKey(@NonNull PlaybackFileInfo info) {
        return "pano_" + MD5.getMD5For16(info.subIndex + "_" + info.createTimeOrg + "_" + info.length) + "_" + info.createTime + getExt(info);
    }

    @NonNull
    public static String getPanoThumbNameKey(@NonNull PlaybackFileInfo info) {
        return "panothumb_" + MD5.getMD5For16(info.subIndex + "_" + info.createTimeOrg + "_" + info.length) + "_" + info.createTime + ".jpg";
    }

    @NonNull
    private static String getStreamKey(@NonNull PlaybackFileInfo info) {
        return "screen_" + MD5.getMD5For16(info.index + "_" + info.createTimeOrg + "_" + info.duration) + "_" + info.duration + ".h264";
    }

    @NonNull
    private static String getMP4StreamKey(@NonNull PlaybackFileInfo info) {
        return "screen_" + MD5.getMD5For16(info.index + "_" + info.createTimeOrg + "_" + info.duration) + "_" + info.duration + ".mp4";
    }

    @NonNull
    public static String getVideoNameKey(@NonNull PlaybackFileInfo info) {
        return getVideoNameKey(info, 0);
    }

    @NonNull
    public static String getVideoNameKey(@NonNull PlaybackFileInfo info, int cameraId) {
        switch (DataCameraGetPushStateInfo.getInstance().getCameraType(cameraId)) {
            case DJICameraTypeTau336:
            case DJICameraTypeTau640:
            case DJICameraTypeGD600:
            case DJICameraTypeFC1705:
                return getStreamKey(info);
            case DJICameraTypeFC6510:
            case DJICameraTypeFC6520:
            case DJICameraTypeFC6540:
                return getMP4StreamKey(info);
            default:
                switch (DJIProductManager.getInstance().getType()) {
                    case Orange:
                    case litchiX:
                    case Longan:
                    case N1:
                    case BigBanana:
                    case OrangeRAW:
                    case OrangeCV600:
                    case Tomato:
                    case litchiC:
                    case litchiS:
                    case P34K:
                        return getStreamKey(info);
                    case Pomato:
                    case PomatoSDR:
                    case Orange2:
                    case M200:
                    case M210:
                    case M210RTK:
                    case PM420:
                    case PM420PRO:
                    case PM420PRO_RTK:
                    case Potato:
                    case PomatoRTK:
                        return getMP4StreamKey(info);
                    default:
                        return getStreamKey(info);
                }
        }
    }
}
