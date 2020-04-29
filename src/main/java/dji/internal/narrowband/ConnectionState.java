package dji.internal.narrowband;

import dji.midware.data.model.P3.DataNarrowBandGetPushStateInfo;

public class ConnectionState {
    private boolean[] isConnected = new boolean[8];

    public ConnectionState(int... connectedIndex) {
        for (int index : connectedIndex) {
            this.isConnected[index] = true;
        }
    }

    public ConnectionState(DataNarrowBandGetPushStateInfo info) {
        for (int i = 0; i < 8; i++) {
            this.isConnected[i] = info.getConnectState(i);
        }
    }

    public boolean isConnected(int slaveIndex) {
        return this.isConnected[slaveIndex];
    }

    public boolean isGimbalControllerConnected() {
        return isConnected(1);
    }

    public boolean isAssistant1Connected() {
        return isConnected(2);
    }

    public boolean isAssistant2Connected() {
        return isConnected(3);
    }

    public boolean isFull() {
        return isGimbalControllerConnected() && isAssistant1Connected() && isAssistant2Connected();
    }

    public String toString() {
        StringBuilder s1 = new StringBuilder("Slave Connected: ");
        for (int i = 0; i < 8; i++) {
            s1.append(this.isConnected[i] ? "1" : "0");
        }
        return s1.toString();
    }
}
