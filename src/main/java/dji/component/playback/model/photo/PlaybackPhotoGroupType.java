package dji.component.playback.model.photo;

public enum PlaybackPhotoGroupType {
    RESERVE(0),
    PANORAMA_360(1),
    PANORAMA_SPHERE(2),
    PANORAMA_1X3(3),
    PANORAMA_180(4),
    PANORAMA_3X3(5),
    PANORAMA_180_VERTICAL(6),
    PANORAMA_180_WIDE(7),
    PANORAMA_SUPER_RESOLUTION(8),
    OTHER(15);
    
    private int data;

    private PlaybackPhotoGroupType(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static PlaybackPhotoGroupType find(int b) {
        PlaybackPhotoGroupType result = OTHER;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}
