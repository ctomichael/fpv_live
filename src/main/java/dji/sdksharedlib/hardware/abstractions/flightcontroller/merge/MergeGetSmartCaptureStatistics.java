package dji.sdksharedlib.hardware.abstractions.flightcontroller.merge;

import dji.common.error.DJIError;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataEyeGetSmartCaptureStatisticsData;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeGetCommand;
import dji.sdksharedlib.hardware.extension.DJISDKMergeGet;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import java.util.List;

public class MergeGetSmartCaptureStatistics extends DJISDKMergeGet {
    private static MergeGetSmartCaptureStatistics instance = null;

    public static synchronized MergeGetSmartCaptureStatistics getInstance() {
        MergeGetSmartCaptureStatistics mergeGetSmartCaptureStatistics;
        synchronized (MergeGetSmartCaptureStatistics.class) {
            if (instance == null) {
                instance = new MergeGetSmartCaptureStatistics();
            }
            mergeGetSmartCaptureStatistics = instance;
        }
        return mergeGetSmartCaptureStatistics;
    }

    public void getInfo(String key, DJISDKCacheCommonMergeCallback callback) {
        addCommand(new DJISDKCacheCommonMergeGetCommand(key, callback));
    }

    /* access modifiers changed from: protected */
    public void execute(List<Object> list) {
        final DJISDKCacheCommonMergeGetCommand[] finalCommands = (DJISDKCacheCommonMergeGetCommand[]) list.toArray(new DJISDKCacheCommonMergeGetCommand[list.size()]);
        final DataEyeGetSmartCaptureStatisticsData getter = new DataEyeGetSmartCaptureStatisticsData();
        getter.setCmdTyp(0).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.merge.MergeGetSmartCaptureStatistics.AnonymousClass1 */

            public void onSuccess(Object model) {
                DataEyeGetSmartCaptureStatisticsData.StatisticsData[] data;
                int count = getter.getNumberOfStatisticsData();
                DJISDKCacheCommonMergeGetCommand[] dJISDKCacheCommonMergeGetCommandArr = finalCommands;
                for (DJISDKCacheCommonMergeGetCommand cmd : dJISDKCacheCommonMergeGetCommandArr) {
                    if (cmd.index.equals(IntelligentFlightAssistantKeys.SMART_CAPTURE_STATISTICS_DATA_COUNT)) {
                        cmd.callback.onSuccess(Integer.valueOf(count));
                    } else if (cmd.index.equals(IntelligentFlightAssistantKeys.SMART_CAPTURE_STATISTICS_DATA)) {
                        if (count == 0) {
                            data = new DataEyeGetSmartCaptureStatisticsData.StatisticsData[0];
                        } else {
                            data = getter.getStatisticsData();
                        }
                        cmd.callback.onSuccess(data);
                    } else if (cmd.index.equals(IntelligentFlightAssistantKeys.SMART_CAPTURE_STATISTICS_DATA_TIME_STAMP)) {
                        cmd.callback.onSuccess(Long.valueOf(getter.getTimeStamp()));
                    }
                }
            }

            public void onFailure(Ccode ccode) {
                DJISDKCacheCommonMergeGetCommand[] dJISDKCacheCommonMergeGetCommandArr = finalCommands;
                for (DJISDKCacheCommonMergeGetCommand cmd : dJISDKCacheCommonMergeGetCommandArr) {
                    if (cmd.callback != null) {
                        cmd.callback.onFailure(DJIError.getDJIError(ccode));
                    }
                }
            }
        });
    }
}
