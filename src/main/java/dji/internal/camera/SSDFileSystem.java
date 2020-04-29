package dji.internal.camera;

public enum SSDFileSystem {
    FAT32(0),
    EXFAT(1),
    UNKNOWN(255);
    
    private int data;

    private SSDFileSystem(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static SSDFileSystem find(int b) {
        SSDFileSystem result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
