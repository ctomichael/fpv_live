package dji.midware.media.metadata;

import com.dji.video.framing.DJIVideoDecoderInterface;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraSetExposureMode;
import dji.midware.media.record.DroneVideoSegment;
import dji.pilot.fpv.util.DJIFlurryReportPublic;
import dji.sdksharedlib.keycatalog.CameraKeys;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

@EXClassNullAway
public class VideoRecordInfo implements DJIVideoDecoderInterface.IVideoRecordInfo {
    private static final boolean DEBUG = false;
    private static final String SupportedVersion = "1.0";
    private static final String TAG = "VideoRecordInfo";
    private static final double UNDEFINED_DOUBLE = -100.0d;
    public static final int UNDEFINED_INT = -100;
    private static final long UNDEFINED_LONG = -100;
    private static final String UNDEFINED_STR = "__UNDEFINED__";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
    @Mapper(key = "ApertureSize")
    public Integer apertureSize = -100;
    @Mapper(key = CameraKeys.CAMERA_TYPE)
    public DataCameraGetPushStateInfo.CameraType cameraType;
    @Mapper(key = "CaptureDate")
    public Date captureDate = new Date(0);
    @Mapper(key = "DeviceMaker")
    public String deviceMaker = UNDEFINED_STR;
    @Mapper(key = "EndTimeMsec")
    public Integer endTimeMsec = -100;
    @Mapper(key = CameraKeys.EXPOSURE_MODE)
    public DataCameraSetExposureMode.ExposureMode exposureMode;
    @Mapper(key = "FileID_Drone")
    public Integer fileId_drone = -100;
    @Mapper(key = "File_Source_Type")
    public Integer fileSourceType = -100;
    @Mapper(key = "FolderID_Drone")
    public Integer folderID_drone = -100;
    @Mapper(key = "FPS_Drone")
    public Integer fps_drone = -100;
    @Mapper(key = "FPS_local")
    public Integer fps_local = -100;
    @Mapper(key = "FrameJumpped")
    public Integer frameJumpped = -100;
    @Mapper(key = CameraKeys.ISO)
    public Integer iSO = -100;
    @Mapper(key = "ImageDescription")
    public String imageDescription = UNDEFINED_STR;
    @Mapper(key = "Is_HD")
    public Boolean isHD = false;
    @Mapper(key = "LocalFileName")
    public String localFileName = UNDEFINED_STR;
    @Mapper(key = "LocationString")
    public String locationString = UNDEFINED_STR;
    @Mapper(key = "PixelXDimension_Local")
    public Integer pixelXDimension_app = -100;
    @Mapper(key = "PixelXDimension_Drone")
    public Integer pixelXDimension_drone = -100;
    @Mapper(key = "PixelYDimension_Local")
    public Integer pixelYDimension_app = -100;
    @Mapper(key = "PixelYDimension_Drone")
    public Integer pixelYDimension_drone = -100;
    @Mapper(key = "PositionGPSAlt")
    public Double positionGPSAlt = Double.valueOf((double) UNDEFINED_DOUBLE);
    @Mapper(key = "PositionGPSLat")
    public Double positionGPSLat = Double.valueOf((double) UNDEFINED_DOUBLE);
    @Mapper(key = "PositionGPSLng")
    public Double positionGPSLng = Double.valueOf((double) UNDEFINED_DOUBLE);
    @Mapper(key = "PositionRelativeAlt")
    public Double positionRelativeAlt = Double.valueOf((double) UNDEFINED_DOUBLE);
    @Mapper(key = DJIFlurryReportPublic.V2_PRODUCT_TYPE_KEY)
    public ProductType productType;
    @Mapper(key = CameraKeys.SHUTTER_SPEED)
    public String shutterSpeed = UNDEFINED_STR;
    @Mapper(key = "Source_File_Path")
    public String sourceFilePath = "";
    @Mapper(key = "StartTimeMsec")
    public Integer startTimeMsec = -100;
    @Mapper(key = "Sync_Drone_Time")
    @Type(elementType = Integer.class)
    public Vector<Integer> syncDroneTime = null;
    @Mapper(key = "Sync_Local_Time")
    @Type(elementType = Integer.class)
    public Vector<Integer> syncLocalTime = null;
    @Mapper(key = "UUID_Drone")
    public Long uuid_drone = Long.valueOf((long) UNDEFINED_LONG);
    @Mapper(key = "Version")
    public final String version = "1.0";
    @Mapper(key = "Video_Type")
    public Integer videoType = 0;
    @Mapper(key = "Video_Resolution_Enum_Drone")
    public Integer video_Resolution_Enum_Drone = -100;
    @Mapper(key = CameraKeys.WHITE_BALANCE)
    public Integer whiteBalance = -100;

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Mapper {
        String key();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Type {
        Class<?> elementType();
    }

    public ProductType getProductType() {
        return this.productType;
    }

    public void setProductType(ProductType productType2) {
        this.productType = productType2;
    }

    public DataCameraGetPushStateInfo.CameraType getCameraType() {
        return this.cameraType;
    }

    public void setCameraType(DataCameraGetPushStateInfo.CameraType cameraType2) {
        this.cameraType = cameraType2;
    }

    public int getApertureSize() {
        return this.apertureSize.intValue();
    }

    public void setApertureSize(int apertureSize2) {
        this.apertureSize = Integer.valueOf(apertureSize2);
    }

    public String getShutterSpeed() {
        return this.shutterSpeed;
    }

    public void setShutterSpeed(String shutterSpeed2) {
        this.shutterSpeed = shutterSpeed2;
    }

    public int getWhiteBalance() {
        return this.whiteBalance.intValue();
    }

    public void setWhiteBalance(int whiteBalance2) {
        this.whiteBalance = Integer.valueOf(whiteBalance2);
    }

    public DataCameraSetExposureMode.ExposureMode getExposureMode() {
        return this.exposureMode;
    }

    public void setExposureMode(DataCameraSetExposureMode.ExposureMode exposureMode2) {
        this.exposureMode = exposureMode2;
    }

    public int getiSO() {
        return this.iSO.intValue();
    }

    public void setiSO(int iSO2) {
        this.iSO = Integer.valueOf(iSO2);
    }

    public String getImageDescription() {
        return this.imageDescription;
    }

    public void setImageDescription(String imageDescription2) {
        this.imageDescription = imageDescription2;
    }

    public int getPixelXDimension_Drone() {
        return this.pixelXDimension_drone.intValue();
    }

    public void setPixelXDimension_Drone(int pixelXDimension_drone2) {
        this.pixelXDimension_drone = Integer.valueOf(pixelXDimension_drone2);
    }

    public int getPixelYDimension_Drone() {
        return this.pixelYDimension_drone.intValue();
    }

    public void setPixelYDimension_Drone(int pixelYDimension_drone2) {
        this.pixelYDimension_drone = Integer.valueOf(pixelYDimension_drone2);
    }

    public int getPixelXDimension_app() {
        return this.pixelXDimension_app.intValue();
    }

    public void setPixelXDimension_app(int pixelXDimension_app2) {
        this.pixelXDimension_app = Integer.valueOf(pixelXDimension_app2);
    }

    public int getPixelYDimension_app() {
        return this.pixelYDimension_app.intValue();
    }

    public void setPixelYDimension_app(int pixelYDimension_app2) {
        this.pixelYDimension_app = Integer.valueOf(pixelYDimension_app2);
    }

    public Date getCaptureDate() {
        return this.captureDate;
    }

    public void setCaptureDate(Date captureDate2) {
        this.captureDate = captureDate2;
    }

    public int getFPS_local() {
        return this.fps_local.intValue();
    }

    public void setFPS_local(int fps_local2) {
        this.fps_local = Integer.valueOf(fps_local2);
    }

    public String getDeviceMaker() {
        return this.deviceMaker;
    }

    public void setDeviceMaker(String deviceMaker2) {
        this.deviceMaker = deviceMaker2;
    }

    public double getPositonGPSLng() {
        return this.positionGPSLng.doubleValue();
    }

    public void setPositionGPSLng(double positonGPSLng) {
        this.positionGPSLng = Double.valueOf(positonGPSLng);
    }

    public double getPositionGPSLat() {
        return this.positionGPSLat.doubleValue();
    }

    public void setPositionGPSLat(double positionGPSLat2) {
        this.positionGPSLat = Double.valueOf(positionGPSLat2);
    }

    public double getPositionRelativeAlt() {
        return this.positionRelativeAlt.doubleValue();
    }

    public void setPositionRelativeAlt(double positionRelativeAlt2) {
        this.positionRelativeAlt = Double.valueOf(positionRelativeAlt2);
    }

    public double getPositionGPSAlt() {
        return this.positionGPSAlt.doubleValue();
    }

    public void setPositionGPSAlt(double positionGPSAlt2) {
        this.positionGPSAlt = Double.valueOf(positionGPSAlt2);
    }

    public String getLocationString() {
        return this.locationString;
    }

    public void setLocationString(String locationString2) {
        this.locationString = locationString2;
    }

    public String getLocalFileName() {
        return this.localFileName;
    }

    public void setLocalFileName(String localFileName2) {
        this.localFileName = localFileName2;
    }

    public int getFps_drone() {
        return this.fps_drone.intValue();
    }

    public void setFps_drone(int fps_drone2) {
        this.fps_drone = Integer.valueOf(fps_drone2);
    }

    public long getUuid_drone() {
        return this.uuid_drone.longValue();
    }

    public void setUuid_drone(long uuid_drone2) {
        this.uuid_drone = Long.valueOf(uuid_drone2);
    }

    public int getFileId_drone() {
        return this.fileId_drone.intValue();
    }

    public void setFileId_drone(int fileId_drone2) {
        this.fileId_drone = Integer.valueOf(fileId_drone2);
    }

    public int getFolderID_drone() {
        return this.folderID_drone.intValue();
    }

    public void setFolderID_drone(int folderID_drone2) {
        this.folderID_drone = Integer.valueOf(folderID_drone2);
    }

    public int getVideoType() {
        return this.videoType.intValue();
    }

    public void setVideoType(int type) {
        this.videoType = Integer.valueOf(type);
    }

    public String getVersion() {
        return "1.0";
    }

    /* JADX WARNING: Removed duplicated region for block: B:100:0x01e1 A[SYNTHETIC, Splitter:B:100:0x01e1] */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x01ed A[SYNTHETIC, Splitter:B:107:0x01ed] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void store(java.lang.String r21) {
        /*
            r20 = this;
            monitor-enter(r20)
            java.util.Properties r7 = new java.util.Properties     // Catch:{ all -> 0x00a1 }
            r7.<init>()     // Catch:{ all -> 0x00a1 }
            java.lang.Class<dji.midware.media.metadata.VideoRecordInfo> r14 = dji.midware.media.metadata.VideoRecordInfo.class
            java.lang.reflect.Field[] r6 = r14.getFields()     // Catch:{ all -> 0x00a1 }
            int r15 = r6.length     // Catch:{ all -> 0x00a1 }
            r14 = 0
        L_0x000e:
            if (r14 >= r15) goto L_0x01be
            r1 = r6[r14]     // Catch:{ all -> 0x00a1 }
            java.lang.Class<dji.midware.media.metadata.VideoRecordInfo$Mapper> r16 = dji.midware.media.metadata.VideoRecordInfo.Mapper.class
            r0 = r16
            java.lang.annotation.Annotation r2 = r1.getAnnotation(r0)     // Catch:{ all -> 0x00a1 }
            dji.midware.media.metadata.VideoRecordInfo$Mapper r2 = (dji.midware.media.metadata.VideoRecordInfo.Mapper) r2     // Catch:{ all -> 0x00a1 }
            if (r2 != 0) goto L_0x0021
        L_0x001e:
            int r14 = r14 + 1
            goto L_0x000e
        L_0x0021:
            java.lang.String r12 = ""
            r0 = r20
            java.lang.Object r5 = r1.get(r0)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.Class r4 = r1.getType()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r16 = 0
            java.lang.String r17 = "VideoRecordInfo"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r18.<init>()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.String r19 = "storing: "
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.String r19 = r4.getName()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.String r18 = r18.toString()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            dji.midware.media.MediaLogger.i(r16, r17, r18)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.Class<dji.midware.data.model.P3.DataCameraGetPushStateInfo$CameraType> r16 = dji.midware.data.model.P3.DataCameraGetPushStateInfo.CameraType.class
            r0 = r16
            boolean r16 = r4.equals(r0)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            if (r16 == 0) goto L_0x00a4
            r0 = r5
            dji.midware.data.model.P3.DataCameraGetPushStateInfo$CameraType r0 = (dji.midware.data.model.P3.DataCameraGetPushStateInfo.CameraType) r0     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r11 = r0
            if (r11 == 0) goto L_0x0066
            int r16 = r11.value()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.String r12 = java.lang.String.valueOf(r16)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
        L_0x0066:
            r16 = 0
            java.lang.String r17 = "VideoRecordInfo"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r18.<init>()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.String r19 = r2.key()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.String r19 = "="
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r12)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.String r18 = r18.toString()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            dji.midware.media.MediaLogger.i(r16, r17, r18)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.String r16 = r2.key()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r0 = r16
            r7.setProperty(r0, r12)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            goto L_0x001e
        L_0x0096:
            r3 = move-exception
            java.lang.String r16 = "VideoRecordInfo"
            r0 = r16
            dji.midware.media.MediaLogger.e(r0, r3)     // Catch:{ all -> 0x00a1 }
            goto L_0x001e
        L_0x00a1:
            r14 = move-exception
            monitor-exit(r20)
            throw r14
        L_0x00a4:
            java.lang.Class<dji.midware.data.model.P3.DataCameraSetExposureMode$ExposureMode> r16 = dji.midware.data.model.P3.DataCameraSetExposureMode.ExposureMode.class
            r0 = r16
            boolean r16 = r4.equals(r0)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            if (r16 == 0) goto L_0x00bd
            r0 = r5
            dji.midware.data.model.P3.DataCameraSetExposureMode$ExposureMode r0 = (dji.midware.data.model.P3.DataCameraSetExposureMode.ExposureMode) r0     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r11 = r0
            if (r11 == 0) goto L_0x0066
            int r16 = r11.value()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.String r12 = java.lang.String.valueOf(r16)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            goto L_0x0066
        L_0x00bd:
            java.lang.Class<dji.midware.data.config.P3.ProductType> r16 = dji.midware.data.config.P3.ProductType.class
            r0 = r16
            boolean r16 = r4.equals(r0)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            if (r16 == 0) goto L_0x00d6
            r0 = r5
            dji.midware.data.config.P3.ProductType r0 = (dji.midware.data.config.P3.ProductType) r0     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r11 = r0
            if (r11 == 0) goto L_0x0066
            int r16 = r11.value()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.String r12 = java.lang.String.valueOf(r16)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            goto L_0x0066
        L_0x00d6:
            java.lang.Class<java.util.Date> r16 = java.util.Date.class
            r0 = r16
            boolean r16 = r4.equals(r0)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            if (r16 == 0) goto L_0x00f0
            r0 = r5
            java.util.Date r0 = (java.util.Date) r0     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r11 = r0
            if (r11 == 0) goto L_0x0066
            java.text.SimpleDateFormat r16 = dji.midware.media.metadata.VideoRecordInfo.dateFormat     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r0 = r16
            java.lang.String r12 = r0.format(r11)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            goto L_0x0066
        L_0x00f0:
            java.lang.String r16 = r4.getName()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.String r17 = "[Ljava.lang.Integer;"
            boolean r16 = r16.equals(r17)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            if (r16 == 0) goto L_0x012c
            java.lang.Integer[] r5 = (java.lang.Integer[]) r5     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r0 = r5
            java.lang.Integer[] r0 = (java.lang.Integer[]) r0     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r11 = r0
            if (r11 == 0) goto L_0x0066
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r13.<init>()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r10 = 0
        L_0x010b:
            int r0 = r11.length     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r16 = r0
            r0 = r16
            if (r10 >= r0) goto L_0x0126
            if (r10 == 0) goto L_0x011c
            java.lang.String r16 = ","
            r0 = r16
            r13.append(r0)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
        L_0x011c:
            r16 = r11[r10]     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r0 = r16
            r13.append(r0)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            int r10 = r10 + 1
            goto L_0x010b
        L_0x0126:
            java.lang.String r12 = r13.toString()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            goto L_0x0066
        L_0x012c:
            java.lang.String r16 = r4.getName()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.String r17 = "[Ljava.lang.Long;"
            boolean r16 = r16.equals(r17)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            if (r16 == 0) goto L_0x0168
            java.lang.Long[] r5 = (java.lang.Long[]) r5     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r0 = r5
            java.lang.Long[] r0 = (java.lang.Long[]) r0     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r11 = r0
            if (r11 == 0) goto L_0x0066
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r13.<init>()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r10 = 0
        L_0x0147:
            int r0 = r11.length     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r16 = r0
            r0 = r16
            if (r10 >= r0) goto L_0x0162
            if (r10 == 0) goto L_0x0158
            java.lang.String r16 = ","
            r0 = r16
            r13.append(r0)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
        L_0x0158:
            r16 = r11[r10]     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r0 = r16
            r13.append(r0)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            int r10 = r10 + 1
            goto L_0x0147
        L_0x0162:
            java.lang.String r12 = r13.toString()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            goto L_0x0066
        L_0x0168:
            java.lang.Class<java.util.Vector> r16 = java.util.Vector.class
            r0 = r16
            boolean r16 = r4.equals(r0)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            if (r16 == 0) goto L_0x01a6
            r0 = r5
            java.util.Vector r0 = (java.util.Vector) r0     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r11 = r0
            if (r11 == 0) goto L_0x0066
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r13.<init>()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r10 = 0
        L_0x017e:
            int r16 = r11.size()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r0 = r16
            if (r10 >= r0) goto L_0x01a0
            if (r10 == 0) goto L_0x0190
            java.lang.String r16 = ","
            r0 = r16
            r13.append(r0)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
        L_0x0190:
            java.lang.Object r16 = r11.get(r10)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            java.lang.String r16 = r16.toString()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            r0 = r16
            r13.append(r0)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            int r10 = r10 + 1
            goto L_0x017e
        L_0x01a0:
            java.lang.String r12 = r13.toString()     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            goto L_0x0066
        L_0x01a6:
            if (r5 != 0) goto L_0x01ad
            java.lang.String r12 = "__UNDEFINED__"
            goto L_0x0066
        L_0x01ad:
            java.lang.String r12 = java.lang.String.valueOf(r5)     // Catch:{ IllegalAccessException -> 0x0096, IllegalArgumentException -> 0x01b3 }
            goto L_0x0066
        L_0x01b3:
            r3 = move-exception
            java.lang.String r16 = "VideoRecordInfo"
            r0 = r16
            dji.midware.media.MediaLogger.e(r0, r3)     // Catch:{ all -> 0x00a1 }
            goto L_0x001e
        L_0x01be:
            r8 = 0
            java.io.FileWriter r9 = new java.io.FileWriter     // Catch:{ IOException -> 0x01d8 }
            r0 = r21
            r9.<init>(r0)     // Catch:{ IOException -> 0x01d8 }
            r14 = 0
            r7.store(r9, r14)     // Catch:{ IOException -> 0x01f9, all -> 0x01f6 }
            if (r9 == 0) goto L_0x01fc
            r9.close()     // Catch:{ IOException -> 0x01d2 }
            r8 = r9
        L_0x01d0:
            monitor-exit(r20)
            return
        L_0x01d2:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x00a1 }
            r8 = r9
            goto L_0x01d0
        L_0x01d8:
            r3 = move-exception
        L_0x01d9:
            java.lang.String r14 = "VideoRecordInfo"
            dji.midware.media.MediaLogger.e(r14, r3)     // Catch:{ all -> 0x01ea }
            if (r8 == 0) goto L_0x01d0
            r8.close()     // Catch:{ IOException -> 0x01e5 }
            goto L_0x01d0
        L_0x01e5:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x00a1 }
            goto L_0x01d0
        L_0x01ea:
            r14 = move-exception
        L_0x01eb:
            if (r8 == 0) goto L_0x01f0
            r8.close()     // Catch:{ IOException -> 0x01f1 }
        L_0x01f0:
            throw r14     // Catch:{ all -> 0x00a1 }
        L_0x01f1:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x00a1 }
            goto L_0x01f0
        L_0x01f6:
            r14 = move-exception
            r8 = r9
            goto L_0x01eb
        L_0x01f9:
            r3 = move-exception
            r8 = r9
            goto L_0x01d9
        L_0x01fc:
            r8 = r9
            goto L_0x01d0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.media.metadata.VideoRecordInfo.store(java.lang.String):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:93:?, code lost:
        java.util.Collections.sort(r14);
        r11 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x0318, code lost:
        if (r11 >= r14.size()) goto L_0x0331;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x031a, code lost:
        dji.midware.media.MediaLogger.i(false, "xx", (java.lang.String) r14.get(r11));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x032e, code lost:
        r11 = r11 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void load(java.lang.String r25) throws java.lang.Exception {
        /*
            r24 = this;
            monitor-enter(r24)
            java.util.Properties r9 = new java.util.Properties     // Catch:{ all -> 0x0129 }
            r9.<init>()     // Catch:{ all -> 0x0129 }
            java.io.FileReader r10 = new java.io.FileReader     // Catch:{ all -> 0x0129 }
            r0 = r25
            r10.<init>(r0)     // Catch:{ all -> 0x0129 }
            r9.load(r10)     // Catch:{ all -> 0x0129 }
            r10.close()     // Catch:{ all -> 0x0129 }
            java.util.Vector r14 = new java.util.Vector     // Catch:{ all -> 0x0129 }
            r14.<init>()     // Catch:{ all -> 0x0129 }
            java.lang.Class<dji.midware.media.metadata.VideoRecordInfo> r18 = dji.midware.media.metadata.VideoRecordInfo.class
            java.lang.reflect.Field[] r20 = r18.getFields()     // Catch:{ all -> 0x0129 }
            r0 = r20
            int r0 = r0.length     // Catch:{ all -> 0x0129 }
            r21 = r0
            r18 = 0
            r19 = r18
        L_0x0027:
            r0 = r19
            r1 = r21
            if (r0 >= r1) goto L_0x030e
            r4 = r20[r19]     // Catch:{ all -> 0x0129 }
            java.lang.Class<dji.midware.media.metadata.VideoRecordInfo$Mapper> r18 = dji.midware.media.metadata.VideoRecordInfo.Mapper.class
            r0 = r18
            java.lang.annotation.Annotation r13 = r4.getAnnotation(r0)     // Catch:{ all -> 0x0129 }
            dji.midware.media.metadata.VideoRecordInfo$Mapper r13 = (dji.midware.media.metadata.VideoRecordInfo.Mapper) r13     // Catch:{ all -> 0x0129 }
            if (r13 == 0) goto L_0x009b
            java.lang.String r18 = r13.key()     // Catch:{ all -> 0x0129 }
            java.lang.String r12 = r18.trim()     // Catch:{ all -> 0x0129 }
            java.lang.String r15 = r9.getProperty(r12)     // Catch:{ Exception -> 0x0072 }
            java.lang.String r18 = "Version"
            r0 = r18
            boolean r18 = r12.equals(r0)     // Catch:{ Exception -> 0x0072 }
            if (r18 == 0) goto L_0x00a0
            if (r15 == 0) goto L_0x0065
            java.lang.String r18 = r15.trim()     // Catch:{ Exception -> 0x0072 }
            java.lang.String r22 = "1.0"
            r0 = r18
            r1 = r22
            boolean r18 = r0.equals(r1)     // Catch:{ Exception -> 0x0072 }
            if (r18 != 0) goto L_0x009b
        L_0x0065:
            java.lang.Exception r18 = new java.lang.Exception     // Catch:{ Exception -> 0x0072 }
            java.lang.String r22 = "This method only support for loading Version 1.0"
            r0 = r18
            r1 = r22
            r0.<init>(r1)     // Catch:{ Exception -> 0x0072 }
            throw r18     // Catch:{ Exception -> 0x0072 }
        L_0x0072:
            r6 = move-exception
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ all -> 0x0129 }
            r18.<init>()     // Catch:{ all -> 0x0129 }
            java.lang.String r22 = "loading key "
            r0 = r18
            r1 = r22
            java.lang.StringBuilder r18 = r0.append(r1)     // Catch:{ all -> 0x0129 }
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r12)     // Catch:{ all -> 0x0129 }
            java.lang.String r22 = " fails"
            r0 = r18
            r1 = r22
            java.lang.StringBuilder r18 = r0.append(r1)     // Catch:{ all -> 0x0129 }
            java.lang.String r18 = r18.toString()     // Catch:{ all -> 0x0129 }
            dji.midware.media.MediaLogger.show(r18)     // Catch:{ all -> 0x0129 }
        L_0x009b:
            int r18 = r19 + 1
            r19 = r18
            goto L_0x0027
        L_0x00a0:
            java.lang.Class r8 = r4.getType()     // Catch:{ Exception -> 0x0072 }
            java.lang.String r18 = r8.toString()     // Catch:{ Exception -> 0x0072 }
            r0 = r18
            r14.add(r0)     // Catch:{ Exception -> 0x0072 }
            if (r15 == 0) goto L_0x009b
            java.lang.String r15 = r15.trim()     // Catch:{ Exception -> 0x0072 }
            java.lang.String r18 = ""
            r0 = r18
            boolean r18 = r15.equals(r0)     // Catch:{ Exception -> 0x0072 }
            if (r18 != 0) goto L_0x009b
            java.lang.String r18 = "__UNDEFINED__"
            r0 = r18
            boolean r18 = r15.equals(r0)     // Catch:{ Exception -> 0x0072 }
            if (r18 != 0) goto L_0x009b
            java.lang.Class<java.lang.Integer> r18 = java.lang.Integer.class
            r0 = r18
            boolean r18 = r8.equals(r0)     // Catch:{ Exception -> 0x00e3 }
            if (r18 == 0) goto L_0x012c
            int r18 = java.lang.Integer.parseInt(r15)     // Catch:{ Exception -> 0x00e3 }
            java.lang.Integer r18 = java.lang.Integer.valueOf(r18)     // Catch:{ Exception -> 0x00e3 }
            r0 = r24
            r1 = r18
            r4.set(r0, r1)     // Catch:{ Exception -> 0x00e3 }
            goto L_0x009b
        L_0x00e3:
            r6 = move-exception
            java.lang.String r18 = "VideoRecordInfo"
            r0 = r18
            dji.midware.media.MediaLogger.e(r0, r6)     // Catch:{ Exception -> 0x0072 }
            java.lang.String r18 = "VideoRecordInfo"
            java.lang.String r22 = r4.toString()     // Catch:{ Exception -> 0x0072 }
            r0 = r18
            r1 = r22
            dji.midware.media.MediaLogger.i(r0, r1)     // Catch:{ Exception -> 0x0072 }
            java.lang.String r18 = "VideoRecordInfo"
            java.lang.StringBuilder r22 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0072 }
            r22.<init>()     // Catch:{ Exception -> 0x0072 }
            java.lang.String r23 = "key="
            java.lang.StringBuilder r22 = r22.append(r23)     // Catch:{ Exception -> 0x0072 }
            r0 = r22
            java.lang.StringBuilder r22 = r0.append(r12)     // Catch:{ Exception -> 0x0072 }
            java.lang.String r23 = " ; value="
            java.lang.StringBuilder r22 = r22.append(r23)     // Catch:{ Exception -> 0x0072 }
            r0 = r22
            java.lang.StringBuilder r22 = r0.append(r15)     // Catch:{ Exception -> 0x0072 }
            java.lang.String r22 = r22.toString()     // Catch:{ Exception -> 0x0072 }
            r0 = r18
            r1 = r22
            dji.midware.media.MediaLogger.i(r0, r1)     // Catch:{ Exception -> 0x0072 }
            goto L_0x009b
        L_0x0129:
            r18 = move-exception
            monitor-exit(r24)
            throw r18
        L_0x012c:
            java.lang.Class<java.lang.Long> r18 = java.lang.Long.class
            r0 = r18
            boolean r18 = r8.equals(r0)     // Catch:{ Exception -> 0x00e3 }
            if (r18 == 0) goto L_0x0147
            long r22 = java.lang.Long.parseLong(r15)     // Catch:{ Exception -> 0x00e3 }
            java.lang.Long r18 = java.lang.Long.valueOf(r22)     // Catch:{ Exception -> 0x00e3 }
            r0 = r24
            r1 = r18
            r4.set(r0, r1)     // Catch:{ Exception -> 0x00e3 }
            goto L_0x009b
        L_0x0147:
            java.lang.Class<java.lang.Boolean> r18 = java.lang.Boolean.class
            r0 = r18
            boolean r18 = r8.equals(r0)     // Catch:{ Exception -> 0x00e3 }
            if (r18 == 0) goto L_0x0162
            boolean r18 = java.lang.Boolean.parseBoolean(r15)     // Catch:{ Exception -> 0x00e3 }
            java.lang.Boolean r18 = java.lang.Boolean.valueOf(r18)     // Catch:{ Exception -> 0x00e3 }
            r0 = r24
            r1 = r18
            r4.set(r0, r1)     // Catch:{ Exception -> 0x00e3 }
            goto L_0x009b
        L_0x0162:
            java.lang.Class<java.lang.String> r18 = java.lang.String.class
            r0 = r18
            boolean r18 = r8.equals(r0)     // Catch:{ Exception -> 0x00e3 }
            if (r18 == 0) goto L_0x0173
            r0 = r24
            r4.set(r0, r15)     // Catch:{ Exception -> 0x00e3 }
            goto L_0x009b
        L_0x0173:
            java.lang.Class<java.util.Date> r18 = java.util.Date.class
            r0 = r18
            boolean r18 = r8.equals(r0)     // Catch:{ Exception -> 0x00e3 }
            if (r18 == 0) goto L_0x018d
            java.util.Date r18 = new java.util.Date     // Catch:{ Exception -> 0x00e3 }
            r0 = r18
            r0.<init>(r15)     // Catch:{ Exception -> 0x00e3 }
            r0 = r24
            r1 = r18
            r4.set(r0, r1)     // Catch:{ Exception -> 0x00e3 }
            goto L_0x009b
        L_0x018d:
            java.lang.Class<java.lang.Double> r18 = java.lang.Double.class
            r0 = r18
            boolean r18 = r8.equals(r0)     // Catch:{ Exception -> 0x00e3 }
            if (r18 == 0) goto L_0x01a8
            double r22 = java.lang.Double.parseDouble(r15)     // Catch:{ Exception -> 0x00e3 }
            java.lang.Double r18 = java.lang.Double.valueOf(r22)     // Catch:{ Exception -> 0x00e3 }
            r0 = r24
            r1 = r18
            r4.set(r0, r1)     // Catch:{ Exception -> 0x00e3 }
            goto L_0x009b
        L_0x01a8:
            java.lang.Class<dji.midware.data.model.P3.DataCameraGetPushStateInfo$CameraType> r18 = dji.midware.data.model.P3.DataCameraGetPushStateInfo.CameraType.class
            r0 = r18
            boolean r18 = r8.equals(r0)     // Catch:{ Exception -> 0x00e3 }
            if (r18 == 0) goto L_0x01c3
            int r18 = java.lang.Integer.parseInt(r15)     // Catch:{ Exception -> 0x00e3 }
            dji.midware.data.model.P3.DataCameraGetPushStateInfo$CameraType r18 = dji.midware.data.model.P3.DataCameraGetPushStateInfo.CameraType.find(r18)     // Catch:{ Exception -> 0x00e3 }
            r0 = r24
            r1 = r18
            r4.set(r0, r1)     // Catch:{ Exception -> 0x00e3 }
            goto L_0x009b
        L_0x01c3:
            java.lang.Class<dji.midware.data.model.P3.DataCameraSetExposureMode$ExposureMode> r18 = dji.midware.data.model.P3.DataCameraSetExposureMode.ExposureMode.class
            r0 = r18
            boolean r18 = r8.equals(r0)     // Catch:{ Exception -> 0x00e3 }
            if (r18 == 0) goto L_0x01de
            int r18 = java.lang.Integer.parseInt(r15)     // Catch:{ Exception -> 0x00e3 }
            dji.midware.data.model.P3.DataCameraSetExposureMode$ExposureMode r18 = dji.midware.data.model.P3.DataCameraSetExposureMode.ExposureMode.find(r18)     // Catch:{ Exception -> 0x00e3 }
            r0 = r24
            r1 = r18
            r4.set(r0, r1)     // Catch:{ Exception -> 0x00e3 }
            goto L_0x009b
        L_0x01de:
            java.lang.Class<dji.midware.data.config.P3.ProductType> r18 = dji.midware.data.config.P3.ProductType.class
            r0 = r18
            boolean r18 = r8.equals(r0)     // Catch:{ Exception -> 0x00e3 }
            if (r18 == 0) goto L_0x01f9
            int r18 = java.lang.Integer.parseInt(r15)     // Catch:{ Exception -> 0x00e3 }
            dji.midware.data.config.P3.ProductType r18 = dji.midware.data.config.P3.ProductType.find(r18)     // Catch:{ Exception -> 0x00e3 }
            r0 = r24
            r1 = r18
            r4.set(r0, r1)     // Catch:{ Exception -> 0x00e3 }
            goto L_0x009b
        L_0x01f9:
            java.lang.String r18 = r8.getName()     // Catch:{ Exception -> 0x00e3 }
            java.lang.String r22 = "[Ljava.lang.Integer;"
            r0 = r18
            r1 = r22
            boolean r18 = r0.equals(r1)     // Catch:{ Exception -> 0x00e3 }
            if (r18 == 0) goto L_0x023e
            java.lang.String r18 = ","
            r0 = r18
            java.lang.String[] r16 = r15.split(r0)     // Catch:{ Exception -> 0x00e3 }
            r0 = r16
            int r0 = r0.length     // Catch:{ Exception -> 0x00e3 }
            r18 = r0
            r0 = r18
            java.lang.Integer[] r5 = new java.lang.Integer[r0]     // Catch:{ Exception -> 0x00e3 }
            r11 = 0
        L_0x021d:
            int r0 = r5.length     // Catch:{ Exception -> 0x00e3 }
            r18 = r0
            r0 = r18
            if (r11 >= r0) goto L_0x0237
            r18 = r16[r11]     // Catch:{ Exception -> 0x00e3 }
            java.lang.String r18 = r18.trim()     // Catch:{ Exception -> 0x00e3 }
            int r18 = java.lang.Integer.parseInt(r18)     // Catch:{ Exception -> 0x00e3 }
            java.lang.Integer r18 = java.lang.Integer.valueOf(r18)     // Catch:{ Exception -> 0x00e3 }
            r5[r11] = r18     // Catch:{ Exception -> 0x00e3 }
            int r11 = r11 + 1
            goto L_0x021d
        L_0x0237:
            r0 = r24
            r4.set(r0, r5)     // Catch:{ Exception -> 0x00e3 }
            goto L_0x009b
        L_0x023e:
            java.lang.String r18 = r8.getName()     // Catch:{ Exception -> 0x00e3 }
            java.lang.String r22 = "[Ljava.lang.Long;"
            r0 = r18
            r1 = r22
            boolean r18 = r0.equals(r1)     // Catch:{ Exception -> 0x00e3 }
            if (r18 == 0) goto L_0x0283
            java.lang.String r18 = ","
            r0 = r18
            java.lang.String[] r16 = r15.split(r0)     // Catch:{ Exception -> 0x00e3 }
            r0 = r16
            int r0 = r0.length     // Catch:{ Exception -> 0x00e3 }
            r18 = r0
            r0 = r18
            java.lang.Long[] r5 = new java.lang.Long[r0]     // Catch:{ Exception -> 0x00e3 }
            r11 = 0
        L_0x0262:
            int r0 = r5.length     // Catch:{ Exception -> 0x00e3 }
            r18 = r0
            r0 = r18
            if (r11 >= r0) goto L_0x027c
            r18 = r16[r11]     // Catch:{ Exception -> 0x00e3 }
            java.lang.String r18 = r18.trim()     // Catch:{ Exception -> 0x00e3 }
            long r22 = java.lang.Long.parseLong(r18)     // Catch:{ Exception -> 0x00e3 }
            java.lang.Long r18 = java.lang.Long.valueOf(r22)     // Catch:{ Exception -> 0x00e3 }
            r5[r11] = r18     // Catch:{ Exception -> 0x00e3 }
            int r11 = r11 + 1
            goto L_0x0262
        L_0x027c:
            r0 = r24
            r4.set(r0, r5)     // Catch:{ Exception -> 0x00e3 }
            goto L_0x009b
        L_0x0283:
            java.lang.Class<java.util.Vector> r18 = java.util.Vector.class
            r0 = r18
            boolean r18 = r8.equals(r0)     // Catch:{ Exception -> 0x00e3 }
            if (r18 == 0) goto L_0x02f4
            java.lang.Class<dji.midware.media.metadata.VideoRecordInfo$Type> r18 = dji.midware.media.metadata.VideoRecordInfo.Type.class
            r0 = r18
            java.lang.annotation.Annotation r18 = r4.getAnnotation(r0)     // Catch:{ Exception -> 0x00e3 }
            dji.midware.media.metadata.VideoRecordInfo$Type r18 = (dji.midware.media.metadata.VideoRecordInfo.Type) r18     // Catch:{ Exception -> 0x00e3 }
            java.lang.Class r7 = r18.elementType()     // Catch:{ Exception -> 0x00e3 }
            java.lang.Class<java.lang.Integer> r18 = java.lang.Integer.class
            r0 = r18
            boolean r18 = r7.equals(r0)     // Catch:{ Exception -> 0x00e3 }
            if (r18 == 0) goto L_0x02da
            java.util.Vector r17 = new java.util.Vector     // Catch:{ Exception -> 0x00e3 }
            r17.<init>()     // Catch:{ Exception -> 0x00e3 }
            java.lang.String r18 = ","
            r0 = r18
            java.lang.String[] r16 = r15.split(r0)     // Catch:{ Exception -> 0x00e3 }
            r11 = 0
        L_0x02b4:
            r0 = r16
            int r0 = r0.length     // Catch:{ Exception -> 0x00e3 }
            r18 = r0
            r0 = r18
            if (r11 >= r0) goto L_0x02d1
            r18 = r16[r11]     // Catch:{ Exception -> 0x00e3 }
            java.lang.String r18 = r18.trim()     // Catch:{ Exception -> 0x00e3 }
            int r18 = java.lang.Integer.parseInt(r18)     // Catch:{ Exception -> 0x00e3 }
            java.lang.Integer r18 = java.lang.Integer.valueOf(r18)     // Catch:{ Exception -> 0x00e3 }
            r17.add(r18)     // Catch:{ Exception -> 0x00e3 }
            int r11 = r11 + 1
            goto L_0x02b4
        L_0x02d1:
            r0 = r24
            r1 = r17
            r4.set(r0, r1)     // Catch:{ Exception -> 0x00e3 }
            goto L_0x009b
        L_0x02da:
            java.lang.String r18 = "VideoRecordInfo"
            java.lang.String r22 = "unsupported type when loading"
            r0 = r18
            r1 = r22
            dji.midware.media.MediaLogger.e(r0, r1)     // Catch:{ Exception -> 0x00e3 }
            java.lang.Exception r18 = new java.lang.Exception     // Catch:{ Exception -> 0x00e3 }
            java.lang.String r22 = "Unsupported type"
            r0 = r18
            r1 = r22
            r0.<init>(r1)     // Catch:{ Exception -> 0x00e3 }
            throw r18     // Catch:{ Exception -> 0x00e3 }
        L_0x02f4:
            java.lang.String r18 = "VideoRecordInfo"
            java.lang.String r22 = "unsupported type when loading"
            r0 = r18
            r1 = r22
            dji.midware.media.MediaLogger.e(r0, r1)     // Catch:{ Exception -> 0x00e3 }
            java.lang.Exception r18 = new java.lang.Exception     // Catch:{ Exception -> 0x00e3 }
            java.lang.String r22 = "Unsupported type"
            r0 = r18
            r1 = r22
            r0.<init>(r1)     // Catch:{ Exception -> 0x00e3 }
            throw r18     // Catch:{ Exception -> 0x00e3 }
        L_0x030e:
            java.util.Collections.sort(r14)     // Catch:{ all -> 0x0129 }
            r11 = 0
        L_0x0312:
            int r18 = r14.size()     // Catch:{ all -> 0x0129 }
            r0 = r18
            if (r11 >= r0) goto L_0x0331
            r19 = 0
            java.lang.String r20 = "xx"
            java.lang.Object r18 = r14.get(r11)     // Catch:{ all -> 0x0129 }
            java.lang.String r18 = (java.lang.String) r18     // Catch:{ all -> 0x0129 }
            r0 = r19
            r1 = r20
            r2 = r18
            dji.midware.media.MediaLogger.i(r0, r1, r2)     // Catch:{ all -> 0x0129 }
            int r11 = r11 + 1
            goto L_0x0312
        L_0x0331:
            monitor-exit(r24)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.media.metadata.VideoRecordInfo.load(java.lang.String):void");
    }

    public int getEndTimeMsec() {
        return this.endTimeMsec.intValue();
    }

    public void setEndTimeMsec(int endTimeMsec2) {
        this.endTimeMsec = Integer.valueOf(endTimeMsec2);
    }

    public int getStartTimeMsec() {
        return this.startTimeMsec.intValue();
    }

    public void setStartTimeMsec(int startTimeMsec2) {
        this.startTimeMsec = Integer.valueOf(startTimeMsec2);
    }

    public int getVideo_Resolution_Enum_Drone() {
        return this.video_Resolution_Enum_Drone.intValue();
    }

    public void setVideo_Resolution_Enum_Drone(int video_Resolution_Enum_Drone2) {
        this.video_Resolution_Enum_Drone = Integer.valueOf(video_Resolution_Enum_Drone2);
    }

    public int getFrameJumpped() {
        return this.frameJumpped.intValue();
    }

    public void setFrameJumpped(int frameJumpped2) {
        this.frameJumpped = Integer.valueOf(frameJumpped2);
    }

    public Vector<Integer> getSyncLocalTime() {
        return this.syncLocalTime;
    }

    public void setSyncLocalTime(Vector<Integer> syncLocalTime2) {
        this.syncLocalTime = syncLocalTime2;
    }

    public void addSyncLocalTime(Integer element) {
        if (this.syncLocalTime == null) {
            this.syncLocalTime = new Vector<>();
        }
        this.syncLocalTime.add(element);
    }

    public Vector<Integer> getSyncDroneTime() {
        return this.syncDroneTime;
    }

    public void setSyncDroneTime(Vector<Integer> syncDroneTime2) {
        this.syncDroneTime = syncDroneTime2;
    }

    public void addSyncDroneTime(Integer element) {
        if (this.syncDroneTime == null) {
            this.syncDroneTime = new Vector<>();
        }
        this.syncDroneTime.add(element);
    }

    public DroneVideoSegment getDroneDuration(int startLocalMSec, int endLocalMSec) {
        return new DroneVideoSegment(mapLocalToDroneWithoutRectification(startLocalMSec), mapLocalToDroneWithoutRectification(endLocalMSec));
    }

    public Vector<DroneVideoSegment> getDroneSegments(int startLocalMSec, int endLocalMSec) {
        Vector<DroneVideoSegment> re = new Vector<>();
        if (this.syncLocalTime == null) {
            re.add(new DroneVideoSegment(startLocalMSec, endLocalMSec));
        } else {
            int previous = 0;
            int index = 0;
            int current = this.syncLocalTime.get(0).intValue();
            while (startLocalMSec >= current) {
                previous = current;
                index++;
                current = index < this.syncLocalTime.size() ? this.syncLocalTime.get(index).intValue() : Integer.MAX_VALUE;
            }
            int startLocal = startLocalMSec;
            int startDrone = (index == 0 ? 0 : this.syncDroneTime.get(index - 1).intValue()) + (startLocalMSec - previous);
            while (current <= endLocalMSec) {
                re.add(new DroneVideoSegment(startDrone, (current - startLocal) + startDrone));
                startDrone = this.syncDroneTime.get(index).intValue();
                index++;
                startLocal = current;
                current = index < this.syncLocalTime.size() ? this.syncLocalTime.get(index).intValue() : Integer.MAX_VALUE;
            }
            re.add(new DroneVideoSegment(startDrone, (endLocalMSec - startLocal) + startDrone));
        }
        return re;
    }

    public int mapLocalToDroneWithoutRectification(int targetLocal) {
        if (this.syncLocalTime == null) {
            return targetLocal;
        }
        int previous = 0;
        int index = 0;
        int current = this.syncLocalTime.get(0).intValue();
        while (targetLocal >= current) {
            previous = current;
            index++;
            current = index < this.syncLocalTime.size() ? this.syncLocalTime.get(index).intValue() : Integer.MAX_VALUE;
        }
        return (index == 0 ? 0 : this.syncDroneTime.get(index - 1).intValue()) + (targetLocal - previous);
    }

    public String toString() {
        return this.localFileName + " at folderID=" + this.folderID_drone + " fileID=" + this.fileId_drone + " uuid=" + this.uuid_drone;
    }

    public Boolean getIsHD() {
        return this.isHD;
    }

    public void setIsHD(Boolean isHD2) {
        this.isHD = isHD2;
    }

    public String getSourceFilePath() {
        return this.sourceFilePath;
    }

    public void setSourceFilePath(String sourceFilePath2) {
        this.sourceFilePath = sourceFilePath2;
    }

    public Integer getFileSourceType() {
        return this.fileSourceType;
    }

    public void setFileSourceType(Integer fileSourceType2) {
        this.fileSourceType = fileSourceType2;
    }
}
