package dji.sdksharedlib.hardware.abstractions.flightcontroller;

import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCommonGetDeviceSerialNumber;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantWM230Abstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class FlightControllerWM230Abstraction extends FlightControllerFoldingDroneAbstraction {
    /* access modifiers changed from: protected */
    public IntelligentFlightAssistantAbstraction newIntelligentFlightAssistantIfSupport() {
        return new IntelligentFlightAssistantWM230Abstraction();
    }

    /* access modifiers changed from: protected */
    public void initFlightControllerSupportParameter() {
        super.initFlightControllerSupportParameter();
        this.compassCount = 1;
        notifyValueChangeForKeyPath(Integer.valueOf(this.compassCount), convertKeyToPath(FlightControllerKeys.COMPASS_COUNT));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushHome information) {
        super.onEvent3BackgroundThread(information);
        notifyValueChangeForKeyPath(Boolean.valueOf(information.isExtraLoadDetected()), KeyHelper.getFlightControllerKey(FlightControllerKeys.IS_EXTRA_LOAD_DETECTED));
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetDeviceSerialNumber deviceSerialNumber = new DataCommonGetDeviceSerialNumber();
        deviceSerialNumber.setReceiveId(DataCommonGetDeviceSerialNumber.DeviceIndex.DeviceNum);
        deviceSerialNumber.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.FlightControllerWM230Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, deviceSerialNumber.getSerialNum());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }
}
