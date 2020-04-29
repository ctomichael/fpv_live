package dji.midware.ble;

public enum DJIBLEDeviceType {
    HG300(0),
    DJIWATCH(1),
    RONIN2(2),
    OTHER(100);
    
    private int mID;

    private DJIBLEDeviceType(int id) {
        this.mID = id;
    }

    public int getID() {
        return this.mID;
    }

    public int value() {
        return this.mID;
    }

    public boolean _equals(int b) {
        return this.mID == b;
    }

    public static DJIBLEDeviceType find(int b) {
        DJIBLEDeviceType result = OTHER;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
