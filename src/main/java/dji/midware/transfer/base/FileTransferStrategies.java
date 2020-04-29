package dji.midware.transfer.base;

import com.dji.mapkit.lbs.configuration.Defaults;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.network.DJIFeatureFlags;
import dji.midware.media.DJIVideoDecoder;

@EXClassNullAway
public enum FileTransferStrategies {
    APP_AOA_RC_SDR_UAV(Defaults.SECOND_IN_NANOS, 250000, 30, 1000, 3),
    APP_TCP_3399_TCP_RC_SDR_UAV(Defaults.SECOND_IN_NANOS, Defaults.SECOND_IN_NANOS, 30, DJIVideoDecoder.connectLosedelay, 1),
    APP_TCP_3399_TCP_RC(500000, 500000, 20, 1000, 1),
    APP_AOA_RC_WIFI_UAV(DJIFeatureFlags.DEFAULT_MAXIMUM_CACHE_SIZE, 500000, 10, 5000, 1),
    APP_AOA_RC(Defaults.SECOND_IN_NANOS, Defaults.SECOND_IN_NANOS, 30, 3000, 1);
    
    public final int packSendPeriodMaxNanosecond;
    public final int packSendPeriodMinNanosecond;
    public final int packSendRetryMaxTimes;
    public final int packTimeoutMillisecond;
    public final int transferVerifyMaxTimes;

    private FileTransferStrategies(int packSendPeriodMaxNanosecond2, int packSendPeriodMinNanosecond2, int packSendRetryMaxTimes2, int packTimeoutMillisecond2, int transferVerifyMaxTimes2) {
        this.packSendPeriodMaxNanosecond = packSendPeriodMaxNanosecond2;
        this.packSendPeriodMinNanosecond = packSendPeriodMinNanosecond2;
        this.packSendRetryMaxTimes = packSendRetryMaxTimes2;
        this.packTimeoutMillisecond = packTimeoutMillisecond2;
        this.transferVerifyMaxTimes = transferVerifyMaxTimes2;
    }
}
