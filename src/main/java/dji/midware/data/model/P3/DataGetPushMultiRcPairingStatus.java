package dji.midware.data.model.P3;

import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcMultiPairing;

public class DataGetPushMultiRcPairingStatus extends DataBase {
    private static DataGetPushMultiRcPairingStatus instance = null;

    public static synchronized DataGetPushMultiRcPairingStatus getInstance() {
        DataGetPushMultiRcPairingStatus dataGetPushMultiRcPairingStatus;
        synchronized (DataGetPushMultiRcPairingStatus.class) {
            if (instance == null) {
                instance = new DataGetPushMultiRcPairingStatus();
            }
            dataGetPushMultiRcPairingStatus = instance;
        }
        return dataGetPushMultiRcPairingStatus;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        super.setPushRecData(data);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public DataRcMultiPairing.PairState getAircraftPairingStatus() {
        return DataRcMultiPairing.PairState.find(((Integer) get(5, 1, Integer.class)).intValue() & 15);
    }

    public boolean isUsingGroundSystem() {
        return ((Integer) get(1, 1, Integer.class)).intValue() == 1;
    }

    public BaseStationPairingState getBaseStationSDRPairingState() {
        return BaseStationPairingState.find(((Integer) get(2, 1, Integer.class)).intValue() & 15);
    }

    public BaseStationPairingState getBaseStationLTEPairingState() {
        return BaseStationPairingState.find(((Integer) get(2, 1, Integer.class)).intValue() & 15);
    }

    public int getBaseStationSignalQuality() {
        return ((Integer) get(41, 1, Integer.class)).intValue();
    }

    public enum BaseStationPairingState {
        UNPAIRED(0),
        PAIRED_WITHOUT_CONNECTION(1),
        CONNECTED(2),
        IN_USE(3),
        UNKNOWN(255);
        
        private int value;

        private BaseStationPairingState(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean equals(int value2) {
            return this.value == value2;
        }

        public static BaseStationPairingState find(int value2) {
            BaseStationPairingState[] values = values();
            for (BaseStationPairingState arg : values) {
                if (arg.equals(value2)) {
                    return arg;
                }
            }
            return UNKNOWN;
        }
    }
}
