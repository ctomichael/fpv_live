package dji.midware.data.model.P3;

public enum DataModule4GCardType {
    ZTE(1),
    NOTIO(2),
    UNKNOWN(3),
    NODATA(4),
    HUAWEI(5),
    VERIZON(6);
    
    private int mValue;

    private DataModule4GCardType(int value) {
        this.mValue = value;
    }

    public static DataModule4GCardType find(int value) {
        DataModule4GCardType target = UNKNOWN;
        DataModule4GCardType[] values = values();
        for (DataModule4GCardType type : values) {
            if (type.mValue == value) {
                return type;
            }
        }
        return target;
    }
}
