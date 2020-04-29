package dji.midware.media.metadata;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraGetVideoParams;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.MediaLogger;
import dji.midware.usb.P3.DJIUsbAccessoryReceiver;
import dji.midware.util.DJIEventBusUtil;
import dji.pilot.fpv.util.DJIFlurryReportPublic;
import java.util.Date;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class VideoRecordInfoBuild {
    private static final String TAG = "VideoRecordInfoBuild";
    private VideoRecordInfo flightInfo;
    private boolean hasCameraGetPushShotParams;
    private boolean hasCameraGetPushStateInfo;
    private boolean hasCameraGetVideoParams;
    private boolean hasOsdGetPushCommon;
    private boolean hasProductType;
    private boolean hasRcGetPushGpsInfo;

    public VideoRecordInfoBuild(Date captureTime, int pixelXDimension_local, int pixelYDimension_local, String fileMainName, String fullSourceFilePath) {
        this.flightInfo = null;
        this.hasCameraGetPushShotParams = false;
        this.hasCameraGetPushStateInfo = false;
        this.hasCameraGetVideoParams = false;
        this.hasOsdGetPushCommon = false;
        this.hasRcGetPushGpsInfo = false;
        this.hasProductType = false;
        this.flightInfo = new VideoRecordInfo();
        this.flightInfo.setCaptureDate(captureTime);
        this.flightInfo.setImageDescription("");
        this.flightInfo.setDeviceMaker(DJIUsbAccessoryReceiver.myFacturer);
        this.flightInfo.setLocationString("");
        this.flightInfo.setStartTimeMsec(0);
        this.flightInfo.setEndTimeMsec(-1);
        this.flightInfo.setPixelXDimension_app(pixelXDimension_local);
        this.flightInfo.setPixelYDimension_app(pixelYDimension_local);
        this.flightInfo.setFPS_local(DJIVideoUtil.getFPS());
        this.flightInfo.setFrameJumpped(0);
        this.flightInfo.setSourceFilePath(fullSourceFilePath);
        this.flightInfo.setFileSourceType(1);
        this.flightInfo.setLocalFileName(fileMainName);
        if (DataCameraGetPushStateInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataCameraGetPushStateInfo.getInstance());
        }
        if (DataOsdGetPushCommon.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushCommon.getInstance());
        }
        if (DataCameraGetPushShotParams.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataCameraGetPushShotParams.getInstance());
        }
        onEvent3BackgroundThread(DJIProductManager.getInstance().getType());
        DataCameraGetVideoParams.getInstance().start(new DJIDataCallBack() {
            /* class dji.midware.media.metadata.VideoRecordInfoBuild.AnonymousClass1 */

            public void onSuccess(Object model) {
                VideoRecordInfoBuild.this.onEvent3BackgroundThread((DataCameraGetVideoParams) model);
            }

            public void onFailure(Ccode ccode) {
            }
        });
        DJIEventBusUtil.register(this);
        MediaLogger.i(TAG, "initilized");
    }

    public void onDestroy() {
        DJIEventBusUtil.unRegister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(ProductType productType) {
        if (!this.hasProductType) {
            this.hasProductType = true;
            MediaLogger.i(TAG, DJIFlurryReportPublic.V2_PRODUCT_TYPE_KEY);
            this.flightInfo.setProductType(productType);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo stateInfo) {
        if (!this.hasCameraGetPushStateInfo) {
            this.hasCameraGetPushStateInfo = true;
            MediaLogger.i(TAG, "DataCameraGetPushStateInfo");
            this.flightInfo.setCameraType(stateInfo.getCameraType());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushShotParams params) {
        if (!this.hasCameraGetPushShotParams) {
            this.hasCameraGetPushShotParams = true;
            MediaLogger.i(TAG, "DataCameraGetPushShotParams");
            this.flightInfo.setApertureSize(params.getApertureSize());
            this.flightInfo.setShutterSpeed(params.getShutterString());
            this.flightInfo.setWhiteBalance(params.getWhiteBalance());
            this.flightInfo.setExposureMode(params.getExposureMode());
            this.flightInfo.setiSO(params.getISO());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon osdPush) {
        if (!this.hasOsdGetPushCommon && osdPush.getGpsNum() > 0) {
            this.hasOsdGetPushCommon = true;
            MediaLogger.i(TAG, "DataOsdGetPushCommon");
            this.flightInfo.setPositionRelativeAlt((double) osdPush.getHeight());
            this.flightInfo.setPositionGPSAlt((double) osdPush.getHeight());
            this.flightInfo.setPositionGPSLat(osdPush.getLatitude());
            this.flightInfo.setPositionGPSLng(osdPush.getLongitude());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetVideoParams videoParams) {
        if (!this.hasCameraGetVideoParams) {
            this.hasCameraGetVideoParams = true;
            MediaLogger.i(TAG, "DataCameraGetVideoParams");
            this.flightInfo.setFolderID_drone(videoParams.getFolderId());
            this.flightInfo.setFileId_drone(videoParams.getFileId());
            this.flightInfo.setUuid_drone(videoParams.getUuid());
            this.flightInfo.setFps_drone(DataCameraGetVideoParams.FPS_Drone.find(videoParams.getFps()).fps());
            int ratio = videoParams.getRatio();
            this.flightInfo.setVideo_Resolution_Enum_Drone(ratio);
            MediaLogger.i(TAG, "ratio=" + ratio);
            DataCameraGetVideoParams.Resolution_Drone rs = DataCameraGetVideoParams.Resolution_Drone.find(ratio);
            if (!rs.equals(DataCameraGetVideoParams.Resolution_Drone.OTHER)) {
                this.flightInfo.setPixelXDimension_Drone(rs.width());
                this.flightInfo.setPixelYDimension_Drone(rs.height());
            }
        }
    }

    public synchronized void setQuickMovieType(int type) {
        this.flightInfo.videoType = Integer.valueOf(type);
    }

    public void addSyncPoint(int localTime, int droneTime) {
        DJILogHelper.getInstance().LOGD("", "add sync point");
        this.flightInfo.addSyncLocalTime(Integer.valueOf(localTime));
        this.flightInfo.addSyncDroneTime(Integer.valueOf(droneTime));
    }

    public void setEndTimeMsec(int endTime) {
        this.flightInfo.setEndTimeMsec(endTime);
    }

    public VideoRecordInfo getVideoInfo() {
        return this.flightInfo;
    }

    public void setFilePath(String filePath) {
        this.flightInfo.sourceFilePath = filePath;
    }
}
