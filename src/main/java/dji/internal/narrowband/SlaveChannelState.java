package dji.internal.narrowband;

import dji.midware.data.model.P3.DataNarrowBandGetPushStateInfo;

public class SlaveChannelState {
    private int character;
    private ConnectionState connectionState = new ConnectionState(new int[0]);
    private int[] deviceBandInfo;
    private boolean isMaster;
    private WorkBand workBand;

    public SlaveChannelState(DataNarrowBandGetPushStateInfo info) {
        boolean z = false;
        this.isMaster = info.getMasterMode() == 0 ? true : z;
        this.character = info.getMasterMode();
        this.workBand = new WorkBand(info);
        this.connectionState = new ConnectionState(info);
        this.deviceBandInfo = new int[info.getAvailableDevicesNum()];
        int index = -1;
        int i = 0;
        while (true) {
            if (i >= info.getAvailableBandNum()) {
                break;
            } else if (info.getCurrentWorkBand() == info.getBand(i)) {
                index = i;
                break;
            } else {
                i++;
            }
        }
        for (int j = 0; j < info.getAvailableDevicesNum(); j++) {
            if (index == -1) {
                this.deviceBandInfo[j] = -1;
            } else {
                this.deviceBandInfo[j] = info.getInfo(index, j);
            }
        }
        StringBuilder s1 = new StringBuilder("");
        for (int i2 = 0; i2 < info.getAvailableDevicesNum(); i2++) {
            s1.append(this.deviceBandInfo[i2]);
            s1.append(" ");
        }
    }

    public boolean getIsMaster() {
        return this.isMaster;
    }

    public int getDeviceSignal(int deviceIndex) {
        if (this.deviceBandInfo == null || deviceIndex >= this.deviceBandInfo.length) {
            return -1;
        }
        if (!getIsMaster() || this.connectionState.isConnected(deviceIndex + 1)) {
            return this.deviceBandInfo[deviceIndex];
        }
        return -1;
    }

    public ConnectionState getConnectionState() {
        return this.connectionState;
    }

    public NarrowBandSlaveMode getCharacter() {
        return NarrowBandSlaveMode.find(this.character);
    }
}
