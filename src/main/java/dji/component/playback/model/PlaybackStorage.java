package dji.component.playback.model;

public enum PlaybackStorage {
    SDCARD(0),
    INNER_STORAGE(1),
    OUTER_STORAGE(2),
    OTHER(255);
    
    private int data;

    private PlaybackStorage(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static PlaybackStorage find(int b) {
        PlaybackStorage result = SDCARD;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
