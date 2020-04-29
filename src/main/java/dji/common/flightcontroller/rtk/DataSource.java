package dji.common.flightcontroller.rtk;

public enum DataSource {
    UNKNOWN(0),
    GPS(1),
    RTK(2);
    
    private static volatile DataSource[] sValues = null;
    private final int data;

    private DataSource(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static DataSource find(int b) {
        if (sValues == null) {
            sValues = values();
        }
        DataSource result = UNKNOWN;
        for (int i = 0; i < sValues.length; i++) {
            if (sValues[i]._equals(b)) {
                return sValues[i];
            }
        }
        return result;
    }
}
