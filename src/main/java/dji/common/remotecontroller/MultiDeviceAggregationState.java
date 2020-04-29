package dji.common.remotecontroller;

public class MultiDeviceAggregationState {
    private MultiDeviceState aircraftState;
    private MultiDeviceState rtkBaseStationState;

    public interface Callback {
        void onMultiDeviceAggregationStateChanged(MultiDeviceAggregationState multiDeviceAggregationState);
    }

    private MultiDeviceAggregationState(Builder builder) {
        this.aircraftState = builder.aircraftState;
        this.rtkBaseStationState = builder.rtkBaseStationState;
    }

    public MultiDeviceState getAircraftState() {
        return this.aircraftState;
    }

    public MultiDeviceState getRtkBaseStationState() {
        return this.rtkBaseStationState;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public MultiDeviceState aircraftState;
        /* access modifiers changed from: private */
        public MultiDeviceState rtkBaseStationState;

        public Builder aircraftMultiDeviceState(MultiDeviceState state) {
            this.aircraftState = state;
            return this;
        }

        public Builder baseStationSDRMultiDeviceState(MultiDeviceState state) {
            this.rtkBaseStationState = state;
            return this;
        }

        public MultiDeviceAggregationState build() {
            return new MultiDeviceAggregationState(this);
        }
    }
}
