package dji.common.flightcontroller.rtk;

import dji.common.error.DJIRTKNetworkServiceError;

public class NetworkServiceState {
    private final NetworkServiceChannelState channelState;
    private final DJIRTKNetworkServiceError error;

    public interface Callback {
        void onNetworkServiceStateUpdate(NetworkServiceState networkServiceState);
    }

    public NetworkServiceChannelState getChannelState() {
        return this.channelState;
    }

    public DJIRTKNetworkServiceError getError() {
        return this.error;
    }

    public NetworkServiceState(NetworkServiceChannelState channelState2, DJIRTKNetworkServiceError error2) {
        this.channelState = channelState2;
        this.error = error2;
    }
}
