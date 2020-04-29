package dji.sdksharedlib.hardware.abstractions.airlink.lb;

import dji.common.airlink.LightbridgeFrequencyBand;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.airlink.LightbridgeLinkKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class Lightbridge2Phantom4AAbstraction extends Lightbridge2Phantom4PAbstraction {
    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        this.supportBand = new LightbridgeFrequencyBand[]{LightbridgeFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ};
        this.currentFrequencyBand = LightbridgeFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ;
        this.currentRange = this.DEFAULT_RANGE;
        notifyValueChangeForKeyPath(this.supportBand, convertKeyToPath("SupportedFrequencyBands"));
        notifyValueChangeForKeyPath(this.currentFrequencyBand, convertKeyToPath("FrequencyBand"));
        notifyValueChangeForKeyPath(this.currentRange, convertKeyToPath(LightbridgeLinkKeys.CHANNEL_RANGE));
    }
}
