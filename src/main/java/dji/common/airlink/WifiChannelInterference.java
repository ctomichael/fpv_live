package dji.common.airlink;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class WifiChannelInterference {
    private WiFiFrequencyBand band;
    private int channel;
    private int power;

    public WifiChannelInterference(@NonNull WiFiFrequencyBand band2, int power2, int channel2) {
        this.band = band2;
        this.power = power2;
        this.channel = channel2;
    }

    public WiFiFrequencyBand getBand() {
        return this.band;
    }

    public int getPower() {
        return this.power;
    }

    public int getChannel() {
        return this.channel;
    }

    public int hashCode() {
        return ((((this.band != null ? this.band.hashCode() : 0) * 31) + this.power) * 31) + this.channel;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof WifiChannelInterference)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.band == ((WifiChannelInterference) o).band && this.power == ((WifiChannelInterference) o).power && this.channel == ((WifiChannelInterference) o).channel;
    }
}
