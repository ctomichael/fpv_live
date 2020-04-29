package dji.sdksharedlib.hardware.abstractions.remotecontroller.merge;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataRcGetFDRcCalibrationState;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeGetCommand;
import dji.sdksharedlib.hardware.extension.DJISDKMergeGet;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import dji.sdksharedlib.keycatalog.airlink.LightbridgeLinkKeys;
import java.util.List;

@EXClassNullAway
public class CalibrationStatusMergeGet extends DJISDKMergeGet {
    public CalibrationStatusMergeGet() {
        this.DELAY_TIME = 100;
    }

    /* access modifiers changed from: protected */
    public void execute(List<Object> list) {
        DataRcGetFDRcCalibrationState.getInstance().start(new CalibrationStatusMergeGetContainClass((DJISDKCacheCommonMergeGetCommand[]) list.toArray(new DJISDKCacheCommonMergeGetCommand[list.size()])));
    }

    public void getInfo(String key, DJISDKCacheCommonMergeCallback callback) {
        addCommand(new DJISDKCacheCommonMergeGetCommand(key, callback));
    }

    private class CalibrationStatusMergeGetContainClass implements DJIDataCallBack {
        private DJISDKCacheCommonMergeGetCommand[] rcCalibrationStatusCommands;

        public CalibrationStatusMergeGetContainClass(DJISDKCacheCommonMergeGetCommand[] sdrConfigCommands) {
            this.rcCalibrationStatusCommands = sdrConfigCommands;
        }

        public void onSuccess(Object model) {
            new DJISDKCacheKey.Builder().component(RemoteControllerKeys.COMPONENT_KEY).index(0).subComponent(LightbridgeLinkKeys.COMPONENT_KEY).subComponentIndex(0);
            DJISDKCacheCommonMergeGetCommand[] dJISDKCacheCommonMergeGetCommandArr = this.rcCalibrationStatusCommands;
            for (DJISDKCacheCommonMergeGetCommand command : dJISDKCacheCommonMergeGetCommandArr) {
                if (command.index.compareTo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_A_AXIS_STATUS) == 0) {
                    command.callback.onSuccess(Integer.valueOf(DataRcGetFDRcCalibrationState.getInstance().getASegmentFilledUpState()));
                } else if (command.index.compareTo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_B_AXIS_STATUS) == 0) {
                    command.callback.onSuccess(Integer.valueOf(DataRcGetFDRcCalibrationState.getInstance().getBSegmentFilledUpState()));
                } else if (command.index.compareTo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_C_AXIS_STATUS) == 0) {
                    command.callback.onSuccess(Integer.valueOf(DataRcGetFDRcCalibrationState.getInstance().getCSegmentFilledUpState()));
                } else if (command.index.compareTo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_D_AXIS_STATUS) == 0) {
                    command.callback.onSuccess(Integer.valueOf(DataRcGetFDRcCalibrationState.getInstance().getDSegmentFilledUpState()));
                } else if (command.index.compareTo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_E_AXIS_STATUS) == 0) {
                    command.callback.onSuccess(Integer.valueOf(DataRcGetFDRcCalibrationState.getInstance().getESegmentFilledUpState()));
                } else if (command.index.compareTo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_F_AXIS_STATUS) == 0) {
                    command.callback.onSuccess(Integer.valueOf(DataRcGetFDRcCalibrationState.getInstance().getFSegmentFilledUpState()));
                } else if (command.index.compareTo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_G_AXIS_STATUS) == 0) {
                    command.callback.onSuccess(Integer.valueOf(DataRcGetFDRcCalibrationState.getInstance().getGSegmentFilledUpState()));
                } else if (command.index.compareTo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_H_AXIS_STATUS) == 0) {
                    command.callback.onSuccess(Integer.valueOf(DataRcGetFDRcCalibrationState.getInstance().getHSegmentFilledUpState()));
                } else if (command.index.compareTo(RemoteControllerKeys.REMOTE_CONTROLLER_CALIBRATION_NUMBER_OF_SEGMENT) == 0) {
                    command.callback.onSuccess(Integer.valueOf(DataRcGetFDRcCalibrationState.getInstance().getSegmentNumber()));
                }
            }
        }

        public void onFailure(Ccode ccode) {
        }
    }
}
