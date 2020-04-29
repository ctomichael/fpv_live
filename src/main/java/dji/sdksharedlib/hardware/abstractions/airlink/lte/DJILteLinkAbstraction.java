package dji.sdksharedlib.hardware.abstractions.airlink.lte;

import dji.common.airlink.LteLinkState;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.common.remotecontroller.RCMode;
import dji.common.util.CallbackUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataDm368CameraVideoStreams;
import dji.midware.data.model.P3.DataModule4GVideoCamera;
import dji.midware.data.model.P3.DataOsdGetPushSdrLteStatus;
import dji.midware.data.model.P3.DataRcGetMaster;
import dji.midware.data.model.P3.DataRcSetMaster;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.usb.P3.SdrLteVideoController;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISubComponentHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.airlink.LteLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.OcuSyncLinkKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJILteLinkAbstraction extends DJISubComponentHWAbstraction {
    private static final String TAG = DJILteLinkAbstraction.class.getSimpleName();

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(LteLinkKeys.class, getClass());
    }

    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
    }

    public void destroy() {
        super.destroy();
        DJIEventBusUtil.unRegister(this);
    }

    @Action(LteLinkKeys.SWITCH_LTE_VIDEO_CAMERA)
    public void switch4GVideoCamera(final DJISDKCacheHWAbstraction.InnerCallback callback, SettingsDefinitions.CameraType cameraType, boolean isOpen) {
        if (cameraType == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        LteLinkState state = (LteLinkState) CacheHelper.getValue(convertKeyToPath(LteLinkKeys.LTE_LINK_STATE));
        if (state == LteLinkState.CONNECTED || state == LteLinkState.USING || !isOpen) {
            if (isOpen) {
                SdrLteVideoController.getInstance().closeLteVideo();
            }
            DataModule4GVideoCamera.getInstance().setCamera(DataCameraGetPushStateInfo.CameraType.find(cameraType.value())).open4GStream(isOpen).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lte.DJILteLinkAbstraction.AnonymousClass1 */

                public void onSuccess(Object model) {
                    SdrLteVideoController.getInstance().updateCameraStreams(DataModule4GVideoCamera.getInstance());
                    SettingsDefinitions.CameraType mainVideoCamera = DJILteLinkAbstraction.this.getSettingsCameraType(SdrLteVideoController.getInstance().getMainVideoCamera());
                    SettingsDefinitions.CameraType secondaryCamera = DJILteLinkAbstraction.this.getSettingsCameraType(SdrLteVideoController.getInstance().getSecondaryVideoCamera());
                    DJILteLinkAbstraction.this.notifyValueChangeForKeyPath(new SettingsDefinitions.CameraType[]{mainVideoCamera, secondaryCamera}, DJILteLinkAbstraction.this.convertKeyToPath(OcuSyncLinkKeys.VIDEO_STREAM_CAMERAS));
                    DJILteLinkAbstraction.this.notifyValueChangeForKeyPath(DJILteLinkAbstraction.this.getSettingsCameraType(SdrLteVideoController.getInstance().getLteVideoCamera()), DJILteLinkAbstraction.this.convertKeyToPath(LteLinkKeys.MODULE_LTE_VIDEO_CAMERA));
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
            return;
        }
        CallbackUtils.onFailure(callback, DJIError.COMMON_DISCONNECTED);
    }

    @Getter(LteLinkKeys.MODULE_LTE_VIDEO_CAMERA)
    public void get4GVideoCamera(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcGetMaster.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.lte.DJILteLinkAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                DataRcSetMaster.MODE mode = DataRcGetMaster.getInstance().getMode();
                if (mode == DataRcSetMaster.MODE.OTHER) {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
                    return;
                }
                DataCameraGetPushStateInfo.CameraType cam1Type = SdrLteVideoController.getInstance().getMainVideoCamera();
                DataDm368CameraVideoStreams.getInstance().setRcMode(mode).isGet(true).setMainCamera(cam1Type).setSecondaryCamera(SdrLteVideoController.getInstance().getSecondaryVideoCamera()).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.airlink.lte.DJILteLinkAbstraction.AnonymousClass2.AnonymousClass1 */

                    public void onSuccess(Object model) {
                        SdrLteVideoController.getInstance().updateCameraStreams(DataModule4GVideoCamera.getInstance());
                        SettingsDefinitions.CameraType mainVideoCamera = DJILteLinkAbstraction.this.getSettingsCameraType(SdrLteVideoController.getInstance().getMainVideoCamera());
                        SettingsDefinitions.CameraType secondaryCamera = DJILteLinkAbstraction.this.getSettingsCameraType(SdrLteVideoController.getInstance().getSecondaryVideoCamera());
                        DJILteLinkAbstraction.this.notifyValueChangeForKeyPath(new SettingsDefinitions.CameraType[]{mainVideoCamera, secondaryCamera}, DJILteLinkAbstraction.this.convertKeyToPath(OcuSyncLinkKeys.VIDEO_STREAM_CAMERAS));
                        CallbackUtils.onSuccess(callback, DJILteLinkAbstraction.this.getSettingsCameraType(SdrLteVideoController.getInstance().getLteVideoCamera()));
                    }

                    public void onFailure(Ccode ccode) {
                        CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                    }
                });
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: private */
    public SettingsDefinitions.CameraType getSettingsCameraType(DataCameraGetPushStateInfo.CameraType cameraType) {
        return cameraType == null ? SettingsDefinitions.CameraType.OTHER : SettingsDefinitions.CameraType.find(cameraType.value());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushSdrLteStatus netLink) {
        DataOsdGetPushSdrLteStatus.P2PNetLinkStatus status;
        LteLinkState state;
        DJISDKCacheKey key = KeyHelper.getRemoteControllerKey("Mode");
        RCMode rcMode = (RCMode) CacheHelper.getValue(key);
        if (rcMode == null) {
            CacheHelper.get(key, null);
        }
        if (rcMode == null || rcMode == RCMode.MASTER) {
            status = netLink.getMasterRc2DroneLink();
        } else {
            status = netLink.getSlaveRc2DroneLink();
        }
        switch (status.getLteState()) {
            case UNPAIRED:
                state = LteLinkState.UNPAIRED;
                break;
            case PAIRED_NOT_CONNECT:
                state = LteLinkState.PAIRED_NOT_CONNECT;
                break;
            case CONNECTED:
                state = LteLinkState.CONNECTED;
                break;
            case USING:
                state = LteLinkState.USING;
                break;
            default:
                state = LteLinkState.UNKNOWN;
                break;
        }
        notifyValueChangeForKeyPath(state, convertKeyToPath(LteLinkKeys.LTE_LINK_STATE));
    }
}
