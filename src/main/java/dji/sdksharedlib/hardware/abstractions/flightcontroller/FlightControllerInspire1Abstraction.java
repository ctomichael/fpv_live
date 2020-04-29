package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.error.DJIError;
import dji.common.error.DJIFlightControllerError;
import dji.common.flightcontroller.LandingGearMode;
import dji.common.flightcontroller.LandingGearState;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataFlycFunctionControl;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataFlycGetPushDeformStatus;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class FlightControllerInspire1Abstraction extends FlightControllerAbstraction {
    private static final String PARAM_NAME_AUTO_LANDING_GEAR = "g_config.gear_cfg.auto_control_enable_0";

    /* access modifiers changed from: protected */
    public boolean isLandingGearMovable() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        onEvent3BackgroundThread(DataFlycGetPushDeformStatus.getInstance());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushDeformStatus params) {
        if (DataFlycGetPushDeformStatus.getInstance().isGetted()) {
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isDeformProtected()), convertKeyToPath(FlightControllerKeys.LANDING_GEAR_AUTOMATIC_MOVEMENT_ENABLED));
            notifyValueChangeForKeyPath(LandingGearMode.find(params.getDeformMode().value()), convertKeyToPath(FlightControllerKeys.LANDING_GEAR_MODE));
            notifyValueChangeForKeyPath(params.getDeformMode(), convertKeyToPath(FlightControllerKeys.DEFORM_MODE));
            notifyValueChangeForKeyPath(LandingGearState.find(params.getDeformStatus().value()), convertKeyToPath(FlightControllerKeys.LANDING_GEAR_STATUS));
        }
    }

    @Action(FlightControllerKeys.ENTER_TRANSPORT_MODE)
    public void enterTransportMode(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataOsdGetPushCommon.getInstance().isMotorUp()) {
            CallbackUtils.onFailure(callback, DJIFlightControllerError.FAIL_TO_ENTER_TRANSPORT_MODE_WHEN_MOTORS_ON);
        } else if (DataCameraGetPushStateInfo.getInstance().getConnectState()) {
            CallbackUtils.onFailure(callback, DJIFlightControllerError.COULD_NOT_ENTER_TRANSPORT_MODE);
        } else if (!DJIComponentManager.getInstance().getCameraComponentType(0).equals(DJIComponentManager.CameraComponentType.None) || !DJIComponentManager.getInstance().getCameraComponentType(1).equals(DJIComponentManager.CameraComponentType.None)) {
            CallbackUtils.onFailure(callback, DJIFlightControllerError.COULD_NOT_ENTER_TRANSPORT_MODE);
        } else {
            sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND.PackMode, callback);
        }
    }

    @Action(FlightControllerKeys.EXIT_TRANSPORT_MODE)
    public void exitTransportMode(DJISDKCacheHWAbstraction.InnerCallback callback) {
        sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND.UnPackMode, callback);
    }

    @Action(FlightControllerKeys.DEPLOY_LANDING_GEAR)
    public void deployLandingGear(DJISDKCacheHWAbstraction.InnerCallback callback) {
        sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND.DownDeform, callback);
    }

    @Action(FlightControllerKeys.RETRACT_LANDING_GEAR)
    public void retractLandingGear(DJISDKCacheHWAbstraction.InnerCallback callback) {
        sendFlightControllerCommand(DataFlycFunctionControl.FLYC_COMMAND.UpDeform, callback);
    }

    @Setter(FlightControllerKeys.LANDING_GEAR_AUTOMATIC_MOVEMENT_ENABLED)
    public void setAutomaticMovementEnabled(boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ParamInfo read = DJIFlycParamInfoManager.read(PARAM_NAME_AUTO_LANDING_GEAR);
        new DataFlycSetParams().setInfo(PARAM_NAME_AUTO_LANDING_GEAR, Integer.valueOf(enable ? 1 : 0)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerInspire1Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(FlightControllerKeys.LANDING_GEAR_AUTOMATIC_MOVEMENT_ENABLED)
    public void getAutomaticMovementEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataFlycGetParams().setInfos(new String[]{PARAM_NAME_AUTO_LANDING_GEAR}).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerInspire1Abstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(DJIFlycParamInfoManager.read(FlightControllerInspire1Abstraction.PARAM_NAME_AUTO_LANDING_GEAR).value.intValue() > 0));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean isNewProgressOfActivation() {
        return false;
    }
}
