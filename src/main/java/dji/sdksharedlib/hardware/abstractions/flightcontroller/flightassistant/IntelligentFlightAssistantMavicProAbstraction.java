package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import dji.common.error.DJIError;
import dji.common.mission.activetrack.ActiveTrackMode;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraSetTrackingParms;
import dji.midware.data.model.P3.DataSingleVisualParam;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;

@EXClassNullAway
public class IntelligentFlightAssistantMavicProAbstraction extends IntelligentFlightAssistant1860Abstraction {
    public void setActiveTrackGestureModeEnabled(final Boolean isEnabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_INTELLIGENT).setTrackIntelligent(isEnabled.booleanValue()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantMavicProAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (isEnabled.booleanValue()) {
                    DataCameraSetTrackingParms.getInstance().setIsTrackingEnable(true).start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantMavicProAbstraction.AnonymousClass1.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            CallbackUtils.onSuccess(callback, (Object) null);
                        }

                        public void onFailure(Ccode ccode) {
                            CallbackUtils.onFailure(callback, ccode);
                        }
                    });
                } else {
                    DataCameraSetTrackingParms.getInstance().setIsTrackingEnable(false).start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantMavicProAbstraction.AnonymousClass1.AnonymousClass2 */

                        public void onSuccess(Object model) {
                            CallbackUtils.onSuccess(callback, (Object) null);
                        }

                        public void onFailure(Ccode ccode) {
                            CallbackUtils.onFailure(callback, ccode);
                        }
                    });
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public void setActiveTrackMode(ActiveTrackMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (mode == ActiveTrackMode.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_INVALID);
        } else {
            new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_MODE).setTrackMode(convertModeToTrackingMode(mode)).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantMavicProAbstraction.AnonymousClass2 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }
}
