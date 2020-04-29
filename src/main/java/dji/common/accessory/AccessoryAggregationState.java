package dji.common.accessory;

public class AccessoryAggregationState {
    private final boolean beaconConnected;
    private final boolean speakerConnected;
    private final boolean spotlightConnected;

    public interface Callback {
        void onUpdate(AccessoryAggregationState accessoryAggregationState);
    }

    private AccessoryAggregationState(Builder builder) {
        this.beaconConnected = builder.beaconConnected;
        this.spotlightConnected = builder.spotlightConnected;
        this.speakerConnected = builder.speakerConnected;
    }

    public boolean isBeaconConnected() {
        return this.beaconConnected;
    }

    public boolean isSpotlightConnected() {
        return this.spotlightConnected;
    }

    public boolean isSpeakerConnected() {
        return this.speakerConnected;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccessoryAggregationState that = (AccessoryAggregationState) o;
        if (isBeaconConnected() == that.isBeaconConnected() && isSpotlightConnected() == that.isSpotlightConnected() && isSpeakerConnected() == that.isSpeakerConnected()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 1;
        if (isBeaconConnected()) {
            result = 1;
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (isSpotlightConnected()) {
            i = 1;
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (!isSpeakerConnected()) {
            i2 = 0;
        }
        return i4 + i2;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public boolean beaconConnected;
        /* access modifiers changed from: private */
        public boolean speakerConnected;
        /* access modifiers changed from: private */
        public boolean spotlightConnected;

        public Builder beaconConnected(boolean beaconConnected2) {
            this.beaconConnected = beaconConnected2;
            return this;
        }

        public Builder spotlightConnected(boolean spotlightConnected2) {
            this.spotlightConnected = spotlightConnected2;
            return this;
        }

        public Builder speakerConnected(boolean speakerConnected2) {
            this.speakerConnected = speakerConnected2;
            return this;
        }

        public AccessoryAggregationState build() {
            return new AccessoryAggregationState(this);
        }
    }
}
