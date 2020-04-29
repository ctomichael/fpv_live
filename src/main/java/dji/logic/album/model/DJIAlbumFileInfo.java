package dji.logic.album.model;

import android.support.annotation.Keep;
import android.text.format.DateFormat;
import com.dji.frame.util.MD5;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogUtils;
import dji.logic.album.manager.accessory.DJIAudioFileInfo;
import dji.logic.album.manager.litchis.DJIFileResolution;
import dji.logic.album.manager.litchis.DJIFileType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Keep
@EXClassNullAway
public class DJIAlbumFileInfo implements Serializable {
    public static final int PANO_RESULT_ABORT = 3;
    public static final int PANO_RESULT_FAIL = 2;
    public static final int PANO_RESULT_RESERVE = 0;
    public static final int PANO_RESULT_SUCCESS = 1;
    private static final long serialVersionUID = 8999516028984791868L;
    public DJIAudioFileInfo.AudioType audioType;
    public DataCameraSetPhoto.TYPE captureType;
    public long createTime;
    public long createTimeOrg;
    public int dataSource;
    public int duration;
    public EncodeType encodeType = EncodeType.H264;
    public ExtExif extExif;
    public long fileGuid;
    public String fileName;
    public DJIFileType fileType;
    public int frameRate;
    public int frameRateScale = 0;
    public int groupNum = 3;
    public int groupResult = 0;
    public GroupType groupType;
    public boolean hasEXT;
    public boolean hasOrigPhoto = false;
    public int index;
    public boolean isSync = false;
    public long length;
    public EncodeType mEncodeType = EncodeType.H264;
    public int pathLength;
    public String pathStr;
    public int photoGroupId;
    public DJIFileResolution resolution;
    public int rotation;
    public DJIAudioFileInfo.SamplingBit samplingBit;
    public DJIAudioFileInfo.SamplingFrequency samplingFrequency;
    public boolean starTag;
    public int subIndex;
    public int subVideoType = 0;
    public int videoTpye = 0;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof DJIAlbumFileInfo)) {
            return false;
        }
        return this.index == ((DJIAlbumFileInfo) obj).index && this.subIndex == ((DJIAlbumFileInfo) obj).subIndex;
    }

    public int hashCode() {
        return (this.index * 29) + this.subIndex;
    }

    public boolean isQuickMoive() {
        return (this.fileType == DJIFileType.MP4 || this.fileType == DJIFileType.MOV) && this.subVideoType != 0 && this.videoTpye == 3;
    }

    public boolean isHLGVideo() {
        return this.videoTpye == 4;
    }

    public boolean isHyperLapse() {
        return this.videoTpye == 5;
    }

    public boolean canDownload() {
        return this.fileType.canDownload() && (videoCanDownload() || pictureCanDownload());
    }

    public boolean isValidFile() {
        return ((double) this.fileGuid) != 0.0d || !this.fileType.isVideo() || !hasFileGuid();
    }

    private boolean hasFileGuid() {
        ProductType type = DJIProductManager.getInstance().getType();
        return type == ProductType.KumquatX || type == ProductType.Mammoth || type == ProductType.WM230 || type == ProductType.WM240;
    }

    private boolean videoCanDownload() {
        return this.fileType.isVideo() && (DpadProductManager.getInstance().isRM500() || this.resolution.isLowerThan(DJIFileResolution.Size_1920_1440p)) && this.encodeType != EncodeType.H265;
    }

    private boolean pictureCanDownload() {
        return this.fileType.isPicture();
    }

    public boolean canSync() {
        return this.fileType == DJIFileType.PANO;
    }

    private String getExt() {
        if (this.fileType == null) {
            return "";
        }
        return "." + this.fileType.toString().toLowerCase(Locale.ENGLISH);
    }

    public String getMd5() {
        return MD5.getMD5For16(this.index + "_" + this.createTimeOrg + "_" + this.length);
    }

    public String getNameKey() {
        if (this.fileType == DJIFileType.TIF || this.fileType == DJIFileType.BOKEH || this.fileType == DJIFileType.PANO) {
            return "org_" + MD5.getMD5For16(this.index + "_" + this.createTimeOrg + "_" + this.length) + "_" + this.createTime + ".jpg";
        }
        return "org_" + MD5.getMD5For16(this.index + "_" + this.createTimeOrg + "_" + this.length) + "_" + this.createTime + getExt();
    }

    public String getThumbNameKey() {
        return "thumb_" + MD5.getMD5For16(this.index + "_" + this.createTimeOrg + "_" + this.length) + "_" + this.createTime + ".jpg";
    }

    public String getScreenNameKey() {
        return "screen_" + MD5.getMD5For16(this.index + "_" + this.createTimeOrg + "_" + this.length) + "_" + this.createTime + ".jpg";
    }

    public String getExifNameKey() {
        return "exif_" + MD5.getMD5For16(this.index + "_" + this.createTimeOrg) + "_" + new DecimalFormat("00").format((long) this.subIndex) + ".exif";
    }

    public String getQuickMovieNameKey() {
        return "quickmovie_" + MD5.getMD5For16(this.index + "_" + this.createTimeOrg + "_" + this.length) + "_" + this.createTime + getExt();
    }

    public String getBokehGroupNameKey() {
        return "bokeh_" + MD5.getMD5For16(this.index + "_" + this.createTimeOrg);
    }

    public String getBokehNameKey() {
        return "bokeh_" + MD5.getMD5For16(this.index + "_" + this.createTimeOrg) + "_" + new DecimalFormat("00").format((long) this.subIndex) + ".jpg";
    }

    public String getPanoramaGroupNameKey() {
        return "panorama_" + MD5.getMD5(this.index + "_" + this.createTimeOrg);
    }

    public String getPanoramaNameKey() {
        return "panorama_" + MD5.getMD5For16(this.index + "_" + this.createTimeOrg) + "_" + new DecimalFormat("00").format((long) this.subIndex) + ".jpg";
    }

    public String getPanoNameKey() {
        return "pano_" + MD5.getMD5For16(this.subIndex + "_" + this.createTimeOrg + "_" + this.length) + "_" + this.createTime + ".jpg";
    }

    public String getFullPanoNameKey() {
        return "pano_" + MD5.getMD5For16(this.subIndex + "_" + this.createTimeOrg + "_" + this.length) + "_" + this.createTime + getExt();
    }

    public String getPanoThumbNameKey() {
        return "panothumb_" + MD5.getMD5For16(this.subIndex + "_" + this.createTimeOrg + "_" + this.length) + "_" + this.createTime + ".jpg";
    }

    private String getStreamKey() {
        return "screen_" + MD5.getMD5For16(this.index + "_" + this.createTimeOrg + "_" + this.duration) + "_" + this.duration + ".h264";
    }

    private String getMP4StreamKey() {
        return "screen_" + MD5.getMD5For16(this.index + "_" + this.createTimeOrg + "_" + this.duration) + "_" + this.duration + ".mp4";
    }

    public String getVideoNameKey() {
        return getVideoNameKey(0);
    }

    public String getVideoNameKey(int cameraId) {
        switch (DataCameraGetPushStateInfo.getInstance().getCameraType(cameraId)) {
            case DJICameraTypeTau336:
            case DJICameraTypeTau640:
            case DJICameraTypeGD600:
            case DJICameraTypeFC1705:
                return getStreamKey();
            case DJICameraTypeFC6510:
            case DJICameraTypeFC6520:
            case DJICameraTypeFC6540:
                return getMP4StreamKey();
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
                        return getStreamKey();
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
                        return getMP4StreamKey();
                    default:
                        return getStreamKey();
                }
        }
    }

    public boolean is4KResolution() {
        return this.resolution.is4K();
    }

    public String toString() {
        return this.index + getExt() + " (len=" + this.length + "b " + getTime() + " pLen=" + this.pathLength + " nameKey=" + getNameKey() + " guid=" + this.fileGuid + " fileTye=" + this.fileType + " resolution=" + this.resolution + " encodeType=" + this.encodeType + " starTag=" + this.starTag + ")";
    }

    private String getTime() {
        return DateFormat.format("yyyy-MM-dd kk:mm:ss", this.createTime).toString();
    }

    public void parseTime(long time) {
        this.createTimeOrg = time;
        this.createTime = getParsedTime(time);
    }

    public static long getParsedTime(long time) {
        int month = (int) ((time >> 21) & 15);
        int day = (int) ((time >> 16) & 31);
        int hour = (int) ((time >> 11) & 31);
        int min = (int) ((time >> 5) & 63);
        int sec = ((int) (31 & time)) * 2;
        try {
            return new SimpleDateFormat(DJILogUtils.FORMAT_2, Locale.ENGLISH).parse(((int) (1980 + (time >> 25))) + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static DJIAlbumFileInfo copy(DJIAlbumFileInfo fileInfo) {
        DJIAlbumFileInfo fileInfo1 = new DJIAlbumFileInfo();
        fileInfo1.length = fileInfo.length;
        fileInfo1.createTime = fileInfo.createTime;
        fileInfo1.createTimeOrg = fileInfo.createTimeOrg;
        fileInfo1.fileType = fileInfo.fileType;
        fileInfo1.index = fileInfo.index;
        fileInfo1.pathLength = fileInfo.pathLength;
        fileInfo1.pathStr = fileInfo.pathStr;
        fileInfo1.groupType = fileInfo.groupType;
        fileInfo1.groupNum = fileInfo.groupNum;
        fileInfo1.frameRate = fileInfo.frameRate;
        fileInfo1.duration = fileInfo.duration;
        return fileInfo1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:76:0x0258 A[SYNTHETIC, Splitter:B:76:0x0258] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0261 A[SYNTHETIC, Splitter:B:81:0x0261] */
    /* JADX WARNING: Removed duplicated region for block: B:95:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readFromProperty(java.io.File r31) {
        /*
            r30 = this;
            r20 = 0
            java.util.Properties r17 = new java.util.Properties     // Catch:{ IOException -> 0x0250, IllegalArgumentException -> 0x0265 }
            r17.<init>()     // Catch:{ IOException -> 0x0250, IllegalArgumentException -> 0x0265 }
            java.io.FileReader r21 = new java.io.FileReader     // Catch:{ IOException -> 0x0250, IllegalArgumentException -> 0x0265 }
            r0 = r21
            r1 = r31
            r0.<init>(r1)     // Catch:{ IOException -> 0x0250, IllegalArgumentException -> 0x0265 }
            r0 = r17
            r1 = r21
            r0.load(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            java.lang.String r28 = "length"
            r0 = r17
            r1 = r28
            java.lang.String r16 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r16)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x0032
            long r28 = java.lang.Long.parseLong(r16)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r2 = r30
            r2.length = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x0032:
            java.lang.String r28 = "createTime"
            r0 = r17
            r1 = r28
            java.lang.String r6 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r6)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x004d
            long r28 = java.lang.Long.parseLong(r6)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r2 = r30
            r2.createTime = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x004d:
            java.lang.String r28 = "createTimeOrg"
            r0 = r17
            r1 = r28
            java.lang.String r5 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r5)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x0068
            long r28 = java.lang.Long.parseLong(r5)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r2 = r30
            r2.createTimeOrg = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x0068:
            java.lang.String r28 = "index"
            r0 = r17
            r1 = r28
            java.lang.String r15 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r15)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x0083
            int r28 = java.lang.Integer.parseInt(r15)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.index = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x0083:
            java.lang.String r28 = "subIndex"
            r0 = r17
            r1 = r28
            java.lang.String r25 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r25)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x009e
            int r28 = java.lang.Integer.parseInt(r25)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.subIndex = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x009e:
            java.lang.String r28 = "duration"
            r0 = r17
            r1 = r28
            java.lang.String r7 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r7)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x00b9
            int r28 = java.lang.Integer.parseInt(r7)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.duration = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x00b9:
            java.lang.String r28 = "rotation"
            r0 = r17
            r1 = r28
            java.lang.String r23 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r23)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x00d4
            int r28 = java.lang.Integer.parseInt(r23)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.rotation = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x00d4:
            java.lang.String r28 = "frameRate"
            r0 = r17
            r1 = r28
            java.lang.String r11 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r11)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x00ef
            int r28 = java.lang.Integer.parseInt(r11)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.frameRate = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x00ef:
            java.lang.String r28 = "resolution"
            r0 = r17
            r1 = r28
            java.lang.String r22 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r22)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x010a
            dji.logic.album.manager.litchis.DJIFileResolution r28 = dji.logic.album.manager.litchis.DJIFileResolution.valueOf(r22)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.resolution = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x010a:
            java.lang.String r28 = "fileType"
            r0 = r17
            r1 = r28
            java.lang.String r10 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r10)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x0125
            dji.logic.album.manager.litchis.DJIFileType r28 = dji.logic.album.manager.litchis.DJIFileType.valueOf(r10)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.fileType = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x0125:
            java.lang.String r28 = "pathLength"
            r0 = r17
            r1 = r28
            java.lang.String r18 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r18)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x0140
            int r28 = java.lang.Integer.parseInt(r18)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.pathLength = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x0140:
            java.lang.String r28 = "pathStr"
            r0 = r17
            r1 = r28
            java.lang.String r28 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.pathStr = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            java.lang.String r28 = "hasEXT"
            r0 = r17
            r1 = r28
            java.lang.String r14 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r14)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x016c
            boolean r28 = java.lang.Boolean.parseBoolean(r14)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.hasEXT = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x016c:
            java.lang.String r28 = "fileGuid"
            r0 = r17
            r1 = r28
            java.lang.String r9 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r9)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x0187
            long r28 = java.lang.Long.parseLong(r9)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r2 = r30
            r2.fileGuid = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x0187:
            java.lang.String r28 = "captureType"
            r0 = r17
            r1 = r28
            java.lang.String r4 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r4)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x01a2
            dji.midware.data.model.P3.DataCameraSetPhoto$TYPE r28 = dji.midware.data.model.P3.DataCameraSetPhoto.TYPE.valueOf(r4)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.captureType = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x01a2:
            java.lang.String r28 = "photoGroupId"
            r0 = r17
            r1 = r28
            java.lang.String r19 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r19)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x01bd
            int r28 = java.lang.Integer.parseInt(r19)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.photoGroupId = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x01bd:
            java.lang.String r28 = "starTag"
            r0 = r17
            r1 = r28
            java.lang.String r24 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r24)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x01d8
            boolean r28 = java.lang.Boolean.parseBoolean(r24)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.starTag = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x01d8:
            java.lang.String r28 = "groupType"
            r0 = r17
            r1 = r28
            java.lang.String r13 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r13)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x01f3
            dji.logic.album.model.DJIAlbumFileInfo$GroupType r28 = dji.logic.album.model.DJIAlbumFileInfo.GroupType.valueOf(r13)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.groupType = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x01f3:
            java.lang.String r28 = "groupNum"
            r0 = r17
            r1 = r28
            java.lang.String r12 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r12)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x020e
            int r28 = java.lang.Integer.parseInt(r12)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.groupNum = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x020e:
            java.lang.String r28 = "subVideoType"
            r0 = r17
            r1 = r28
            java.lang.String r26 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r26)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x0229
            int r28 = java.lang.Integer.parseInt(r26)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.subVideoType = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x0229:
            java.lang.String r28 = "videoType"
            r0 = r17
            r1 = r28
            java.lang.String r27 = r0.getProperty(r1)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            boolean r28 = android.text.TextUtils.isEmpty(r27)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            if (r28 != 0) goto L_0x0244
            int r28 = java.lang.Integer.parseInt(r27)     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
            r0 = r28
            r1 = r30
            r1.videoTpye = r0     // Catch:{ IOException -> 0x026f, IllegalArgumentException -> 0x0273, all -> 0x026b }
        L_0x0244:
            if (r21 == 0) goto L_0x0249
            r21.close()     // Catch:{ Exception -> 0x024c }
        L_0x0249:
            r20 = r21
        L_0x024b:
            return
        L_0x024c:
            r28 = move-exception
            r20 = r21
            goto L_0x024b
        L_0x0250:
            r28 = move-exception
        L_0x0251:
            r8 = r28
        L_0x0253:
            r8.printStackTrace()     // Catch:{ all -> 0x025e }
            if (r20 == 0) goto L_0x024b
            r20.close()     // Catch:{ Exception -> 0x025c }
            goto L_0x024b
        L_0x025c:
            r28 = move-exception
            goto L_0x024b
        L_0x025e:
            r28 = move-exception
        L_0x025f:
            if (r20 == 0) goto L_0x0264
            r20.close()     // Catch:{ Exception -> 0x0269 }
        L_0x0264:
            throw r28
        L_0x0265:
            r28 = move-exception
        L_0x0266:
            r8 = r28
            goto L_0x0253
        L_0x0269:
            r29 = move-exception
            goto L_0x0264
        L_0x026b:
            r28 = move-exception
            r20 = r21
            goto L_0x025f
        L_0x026f:
            r28 = move-exception
            r20 = r21
            goto L_0x0251
        L_0x0273:
            r28 = move-exception
            r20 = r21
            goto L_0x0266
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.logic.album.model.DJIAlbumFileInfo.readFromProperty(java.io.File):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x012e A[SYNTHETIC, Splitter:B:32:0x012e] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0137 A[SYNTHETIC, Splitter:B:37:0x0137] */
    /* JADX WARNING: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeToProperty(java.io.File r9) {
        /*
            r8 = this;
            r2 = 0
            java.util.Properties r1 = new java.util.Properties     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.<init>()     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r4 = "length"
            long r6 = r8.length     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Long.toString(r6)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r4 = "createTime"
            long r6 = r8.createTime     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Long.toString(r6)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r4 = "createTimeOrg"
            long r6 = r8.createTimeOrg     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Long.toString(r6)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r4 = "index"
            int r5 = r8.index     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r4 = "subIndex"
            int r5 = r8.subIndex     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r4 = "duration"
            int r5 = r8.duration     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r4 = "rotation"
            int r5 = r8.rotation     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r4 = "frameRate"
            int r5 = r8.frameRate     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            dji.logic.album.manager.litchis.DJIFileResolution r4 = r8.resolution     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            if (r4 == 0) goto L_0x0076
            java.lang.String r4 = "resolution"
            dji.logic.album.manager.litchis.DJIFileResolution r5 = r8.resolution     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = r5.name()     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
        L_0x0076:
            dji.logic.album.manager.litchis.DJIFileType r4 = r8.fileType     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            if (r4 == 0) goto L_0x0086
            java.lang.String r4 = "fileType"
            dji.logic.album.manager.litchis.DJIFileType r5 = r8.fileType     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = r5.name()     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
        L_0x0086:
            java.lang.String r4 = "pathLength"
            int r5 = r8.pathLength     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r4 = r8.pathStr     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            if (r4 == 0) goto L_0x009e
            java.lang.String r4 = "pathStr"
            java.lang.String r5 = r8.pathStr     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
        L_0x009e:
            java.lang.String r4 = "hasEXT"
            boolean r5 = r8.hasEXT     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Boolean.toString(r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r4 = "fileGuid"
            long r6 = r8.fileGuid     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Long.toString(r6)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            dji.midware.data.model.P3.DataCameraSetPhoto$TYPE r4 = r8.captureType     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            if (r4 == 0) goto L_0x00c6
            java.lang.String r4 = "captureType"
            dji.midware.data.model.P3.DataCameraSetPhoto$TYPE r5 = r8.captureType     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = r5.name()     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
        L_0x00c6:
            java.lang.String r4 = "photoGroupId"
            int r5 = r8.photoGroupId     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r4 = "starTag"
            boolean r5 = r8.starTag     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Boolean.toString(r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            dji.logic.album.model.DJIAlbumFileInfo$GroupType r4 = r8.groupType     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            if (r4 == 0) goto L_0x00ee
            java.lang.String r4 = "groupType"
            dji.logic.album.model.DJIAlbumFileInfo$GroupType r5 = r8.groupType     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = r5.name()     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
        L_0x00ee:
            java.lang.String r4 = "groupNum"
            int r5 = r8.groupNum     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r4 = "subVideoType"
            int r5 = r8.subVideoType     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r4 = "videoType"
            int r5 = r8.videoTpye     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.io.FileWriter r3 = new java.io.FileWriter     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            r3.<init>(r9)     // Catch:{ IOException -> 0x0127, NumberFormatException -> 0x013b }
            java.lang.String r4 = "Org Video UUID info"
            r1.store(r3, r4)     // Catch:{ IOException -> 0x0146, NumberFormatException -> 0x0143, all -> 0x0140 }
            if (r3 == 0) goto L_0x0122
            r3.close()     // Catch:{ Exception -> 0x0124 }
        L_0x0122:
            r2 = r3
        L_0x0123:
            return
        L_0x0124:
            r4 = move-exception
            r2 = r3
            goto L_0x0123
        L_0x0127:
            r4 = move-exception
        L_0x0128:
            r0 = r4
        L_0x0129:
            r0.printStackTrace()     // Catch:{ all -> 0x0134 }
            if (r2 == 0) goto L_0x0123
            r2.close()     // Catch:{ Exception -> 0x0132 }
            goto L_0x0123
        L_0x0132:
            r4 = move-exception
            goto L_0x0123
        L_0x0134:
            r4 = move-exception
        L_0x0135:
            if (r2 == 0) goto L_0x013a
            r2.close()     // Catch:{ Exception -> 0x013e }
        L_0x013a:
            throw r4
        L_0x013b:
            r4 = move-exception
        L_0x013c:
            r0 = r4
            goto L_0x0129
        L_0x013e:
            r5 = move-exception
            goto L_0x013a
        L_0x0140:
            r4 = move-exception
            r2 = r3
            goto L_0x0135
        L_0x0143:
            r4 = move-exception
            r2 = r3
            goto L_0x013c
        L_0x0146:
            r4 = move-exception
            r2 = r3
            goto L_0x0128
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.logic.album.model.DJIAlbumFileInfo.writeToProperty(java.io.File):void");
    }

    public int getRealFrameRate() {
        switch (this.frameRate) {
            case 1:
            case 13:
                return 24;
            case 2:
                return 25;
            case 3:
                return 30;
            case 4:
                return 48;
            case 5:
                return 50;
            case 6:
                return 60;
            case 7:
                return 120;
            case 8:
                return 240;
            case 9:
            case 12:
            case 18:
            case 19:
            case 20:
            case 22:
            default:
                return 0;
            case 10:
                return 100;
            case 11:
                return 96;
            case 14:
                return 30;
            case 15:
                return 48;
            case 16:
                return 60;
            case 17:
                return 90;
            case 21:
                return 8;
            case 23:
                return 9;
        }
    }

    @Keep
    public enum EXT_TYPE {
        VideoGUID(1),
        PhotoInfo(2),
        FileTag(3),
        VideoInfo(4),
        GroupParam(5),
        SyncTag(6),
        origInfo(7),
        exifInfo(8),
        AudioInfo(9),
        OTHER(0);
        
        private int data;

        private EXT_TYPE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static EXT_TYPE find(int b) {
            EXT_TYPE result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum GroupType {
        Reservied(0),
        Pano_360(1),
        Pano_Sphere(2),
        Pano_1x3(3),
        Pano_180(4),
        Pano_3x3(5),
        Pano_180_vertical(6),
        Pano_wide_180(7),
        Pano_Super_Resolution(8),
        OTHER(15);
        
        private int data;

        private GroupType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static GroupType find(int b) {
            GroupType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum EncodeType {
        H264(0),
        H265(1),
        MJPEG(2),
        NUM(3),
        Unknown(255);
        
        private static volatile EncodeType[] sValues = null;
        private int data;
        public String names;

        private EncodeType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static EncodeType find(int b) {
            if (sValues == null) {
                sValues = values();
            }
            EncodeType result = Unknown;
            for (int i = 0; i < sValues.length; i++) {
                if (sValues[i]._equals(b)) {
                    return sValues[i];
                }
            }
            return result;
        }
    }
}
