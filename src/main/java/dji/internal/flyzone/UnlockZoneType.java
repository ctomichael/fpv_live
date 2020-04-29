package dji.internal.flyzone;

public enum UnlockZoneType {
    GEO_UNLOCK(0),
    CIRCLE_UNLOCK_AREA(1),
    COUNTRY_UNLOCK(2),
    HEIGHT_UNLOCK(3),
    PENTAGON_UNLOCK(4),
    UNKNOWN(255);
    
    private int value;

    private UnlockZoneType(int value2) {
        this.value = value2;
    }

    public static UnlockZoneType find(int value2) {
        UnlockZoneType unlockZoneType = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i].value == value2) {
                unlockZoneType = values()[i];
            }
        }
        return unlockZoneType;
    }
}
