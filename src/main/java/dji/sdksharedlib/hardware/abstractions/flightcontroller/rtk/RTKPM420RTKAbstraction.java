package dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk;

import android.support.annotation.Nullable;
import dji.common.error.DJIFlightControllerError;
import dji.common.flightcontroller.rtk.RTKConnectType;
import dji.common.util.CallbackUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataOsdGetPushSdrLteStatus;
import dji.midware.data.model.P3.NRTKActivateData;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.RTKKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RTKPM420RTKAbstraction extends RTKPhantom4RTKAbstraction {
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushSdrLteStatus netLink) {
        DataOsdGetPushSdrLteStatus.P2PNetLinkStatus status = netLink.getDrone2RTKLink();
        RTKConnectType connectType = RTKConnectType.None;
        if (status.getSdrState() == DataOsdGetPushSdrLteStatus.NetLinkState.CONNECTED || status.getSdrState() == DataOsdGetPushSdrLteStatus.NetLinkState.USING) {
            connectType = RTKConnectType.SDR;
        } else if (status.getLteState() == DataOsdGetPushSdrLteStatus.NetLinkState.CONNECTED || status.getLteState() == DataOsdGetPushSdrLteStatus.NetLinkState.USING) {
            connectType = RTKConnectType.LTE;
        }
        notifyValueChangeForKeyPath(connectType, convertKeyToPath(RTKKeys.RTK_CONNECT_TYPE));
    }

    @Action(RTKKeys.RTK_ACTIVATE)
    public void setRTKActivate(@Nullable final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new NRTKActivateData().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKPM420RTKAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }
}
