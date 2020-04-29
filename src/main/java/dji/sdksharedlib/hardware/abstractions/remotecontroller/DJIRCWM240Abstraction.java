package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import dji.common.error.DJIRemoteControllerError;
import dji.common.remotecontroller.HardwareState;
import dji.common.util.CallbackUtils;
import dji.midware.component.rc.DJIRcDetectHelper;
import dji.midware.component.rc.RcHardWareType;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataOsdGetSdrUplinkBandwidth;
import dji.midware.data.model.P3.DataOsdSetSdrAssitantRead;
import dji.midware.data.model.P3.DataRcGetPushParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIRCWM240Abstraction extends DJIRCFoldingDroneAbstraction {
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushParams params) {
        super.onEvent3BackgroundThread(params);
    }

    /* access modifiers changed from: protected */
    public HardwareState.RightWheel getRightWheel(DataRcGetPushParams params) {
        int realValue = params.getRightGyroValueV1() - 1024;
        return new HardwareState.RightWheel(this.hardwareState.getRightWheel().isPresent(), realValue != 0, false, realValue);
    }

    @Getter(RemoteControllerKeys.REMAIN_BUFFER_SIZE)
    public void getRemainBufferSize(DJISDKCacheHWAbstraction.InnerCallback callback) {
        RcHardWareType type = DJIRcDetectHelper.getInstance().getRcHardWareType();
        if (type == RcHardWareType.WM245_PIGEON) {
            getRemainBufferSizeByCmd(callback);
        } else if (type == RcHardWareType.WM240_PIGEON) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        } else {
            getRemainBufferSizeByReadAddress(callback);
        }
    }

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return DJIRCAbstraction.DisplayNameMavic2;
    }

    /* access modifiers changed from: protected */
    public void getRemainBufferSizeByReadAddress(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataOsdSetSdrAssitantRead getter = new DataOsdSetSdrAssitantRead();
        getter.setSdrDeviceType(DataOsdSetSdrAssitantRead.SdrDeviceType.Ground);
        getter.setSdrCpuType(DataOsdSetSdrAssitantRead.SdrCpuType.CP_A7);
        getter.setSdrDataType(DataOsdSetSdrAssitantRead.SdrDataType.Short_Data);
        getter.setAddress(-65434);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCWM240Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                int value = getter.getShortValue();
                CallbackUtils.onSuccess(callback, new Integer[]{Integer.valueOf(value & 255), Integer.valueOf((value >> 8) & 255)});
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void getRemainBufferSizeByCmd(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataOsdGetSdrUplinkBandwidth getter = new DataOsdGetSdrUplinkBandwidth();
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCWM240Abstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                int tbSize = getter.getTbSize();
                int remainListCnt = getter.getRemainListCnt();
                CallbackUtils.onSuccess(callback, new Integer[]{Integer.valueOf(tbSize), Integer.valueOf(remainListCnt)});
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }
}
