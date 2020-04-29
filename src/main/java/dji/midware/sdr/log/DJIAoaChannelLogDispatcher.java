package dji.midware.sdr.log;

import dji.midware.data.model.P3.DataWifi_gGetPushCheckStatus;

public class DJIAoaChannelLogDispatcher {
    private DJISdrLogDataReciever mSdrLogDataReciever = DJISdrLogDataReciever.getInstance();
    private DJIWifiRcLogDataReceiver mWifiRcLogDataReceiver = DJIWifiRcLogDataReceiver.getInstance();

    public void onPortLogRawDataReceive(int portIndex, byte[] dataBuffer, int offset, int size) {
        if (isWifiGround()) {
            this.mWifiRcLogDataReceiver.onPortLogRawDataReceive(1, dataBuffer, offset, size);
        } else if (portIndex == 1) {
            this.mSdrLogDataReciever.onAoaRecvLogPort1(dataBuffer, offset, size);
        } else if (portIndex == 2) {
            this.mSdrLogDataReciever.onAoaRecvLogPort2(dataBuffer, offset, size);
        } else {
            this.mSdrLogDataReciever.onAoaRecvLogPort3(dataBuffer, offset, size);
        }
    }

    private boolean isWifiGround() {
        return DataWifi_gGetPushCheckStatus.getInstance().isGetted();
    }
}
