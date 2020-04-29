package dji.sdksharedlib.hardware.abstractions.airlink.lb.merge;

import dji.common.airlink.ChannelSelectionMode;
import dji.common.airlink.OcuSyncBandwidth;
import dji.common.error.DJIError;
import dji.common.util.SDRLinkHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataOsdGetSdrConfig;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.hardware.extension.DJISDKMergeGet;
import dji.sdksharedlib.keycatalog.airlink.OcuSyncLinkKeys;
import java.util.List;

@EXClassNullAway
public class MergeGetOcuSyncInfo extends DJISDKMergeGet {
    public void getInfo(String key, DJISDKCacheCommonMergeCallback callback) {
        addCommand(new OcuSyncConfigCommand(key, callback));
    }

    /* access modifiers changed from: protected */
    public void execute(List<Object> list) {
        DataOsdGetSdrConfig.getInstance().start(new OcuSyncCallbackContainerClass((OcuSyncConfigCommand[]) list.toArray(new OcuSyncConfigCommand[list.size()])));
    }

    public static class OcuSyncConfigCommand {
        public DJISDKCacheCommonMergeCallback callback;
        public String index;

        public OcuSyncConfigCommand(String index2, DJISDKCacheCommonMergeCallback callBack) {
            this.index = index2;
            this.callback = callBack;
        }
    }

    private class OcuSyncCallbackContainerClass implements DJIDataCallBack {
        private OcuSyncConfigCommand[] ocuSyncConfigCommands;

        public OcuSyncCallbackContainerClass(OcuSyncConfigCommand[] ocuSyncConfigCommands2) {
            this.ocuSyncConfigCommands = ocuSyncConfigCommands2;
        }

        public void onSuccess(Object model) {
            int i;
            OcuSyncConfigCommand[] ocuSyncConfigCommandArr = this.ocuSyncConfigCommands;
            for (OcuSyncConfigCommand command : ocuSyncConfigCommandArr) {
                if (command.index.compareTo(OcuSyncLinkKeys.BANDWIDTH) == 0) {
                    command.callback.onSuccess(OcuSyncBandwidth.find(DataOsdGetSdrConfig.getInstance().getBandwidthType()));
                } else if (command.index.compareTo(OcuSyncLinkKeys.FREQUENCY_POINT_INDEX) == 0) {
                    command.callback.onSuccess(Integer.valueOf(SDRLinkHelper.convertFrequencyFormFrequencyPointIndex(DataOsdGetSdrConfig.getInstance().getSdrNf())));
                } else if (command.index.compareTo("ChannelSelectionMode") == 0) {
                    DJISDKCacheCommonMergeCallback dJISDKCacheCommonMergeCallback = command.callback;
                    if (DataOsdGetSdrConfig.getInstance().getSelectionMode() == 0) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    dJISDKCacheCommonMergeCallback.onSuccess(ChannelSelectionMode.find(i));
                } else if (command.index.compareTo(OcuSyncLinkKeys.IS_DUAL_BAND_SUPPORTED) == 0) {
                    command.callback.onSuccess(Boolean.valueOf(DataOsdGetSdrConfig.getInstance().getIsSupportDualBand()));
                }
            }
        }

        public void onFailure(Ccode ccode) {
            for (OcuSyncConfigCommand command : this.ocuSyncConfigCommands) {
                command.callback.onFailure(DJIError.getDJIError(ccode));
            }
        }
    }
}
