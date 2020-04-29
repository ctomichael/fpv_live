package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.common.mission.activetrack.ActiveTrackMode;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.CommonUtil;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataDm368SetActiveTrackCamera;
import dji.midware.data.model.P3.DataSingleVisualParam;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DoubleCameraSupportUtil;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;

@EXClassNullAway
public class IntelligentFlightAssistantInspire2Abstraction extends IntelligentFlightAssistant1860Abstraction {
    public void setActiveTrackMode(ActiveTrackMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (mode == ActiveTrackMode.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_INVALID);
        } else {
            new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_MODE).setTrackMode(convertModeToTrackingMode(mode)).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantInspire2Abstraction.AnonymousClass1 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public DataSingleVisualParam.TrackingMode convertModeToTrackingMode(ActiveTrackMode mode) {
        switch (mode) {
            case TRACE:
                return DataSingleVisualParam.TrackingMode.HEADLESS_FOLLOW;
            case SPOTLIGHT:
            case SPOTLIGHT_PRO:
            case SPOTLIGHT_HEAT_POINT:
                return DataSingleVisualParam.TrackingMode.WATCH_TARGET;
            case PROFILE:
                return DataSingleVisualParam.TrackingMode.FIXED_ANGLE;
            default:
                return DataSingleVisualParam.TrackingMode.HEADLESS_FOLLOW;
        }
    }

    @Setter(IntelligentFlightAssistantKeys.SET_ACTIVE_TRACK_CAMERA)
    public void setActiveTrackCamera(final SettingsDefinitions.CameraType type, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataDm368SetActiveTrackCamera) new DataDm368SetActiveTrackCamera().setReceiverId(CommonUtil.isPM420Platform() ? (char) 6 : 1, DataDm368SetActiveTrackCamera.class)).setCameraType(DataCameraGetPushStateInfo.CameraType.find(type.value())).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantInspire2Abstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
                if (type.value() == DataCameraGetPushStateInfo.getInstance().getCameraType(2).value()) {
                    DoubleCameraSupportUtil.setTrackMissionCameraID(2);
                } else {
                    DoubleCameraSupportUtil.setTrackMissionCameraID(0);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }
}
