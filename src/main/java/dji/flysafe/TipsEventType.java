package dji.flysafe;

import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

public enum TipsEventType implements WireEnum {
    TakeOffWithLimitHeightWithoutGPS(0),
    TakeOffUnderLimitHeightArea(1),
    TakeOffUnderWarningArea(2),
    CollisionWithLimitArea(3),
    CollisionWithAuthAreaWithoutLicense(4),
    CollisionWithLimitHeightArea(5),
    CollisionWithGoHomeMode(6),
    CollisionWithSmartMode(7),
    CollisionWithSpecialUnlockArea(8);
    
    public static final ProtoAdapter<TipsEventType> ADAPTER = ProtoAdapter.newEnumAdapter(TipsEventType.class);
    private final int value;

    private TipsEventType(int value2) {
        this.value = value2;
    }

    public static TipsEventType fromValue(int value2) {
        switch (value2) {
            case 0:
                return TakeOffWithLimitHeightWithoutGPS;
            case 1:
                return TakeOffUnderLimitHeightArea;
            case 2:
                return TakeOffUnderWarningArea;
            case 3:
                return CollisionWithLimitArea;
            case 4:
                return CollisionWithAuthAreaWithoutLicense;
            case 5:
                return CollisionWithLimitHeightArea;
            case 6:
                return CollisionWithGoHomeMode;
            case 7:
                return CollisionWithSmartMode;
            case 8:
                return CollisionWithSpecialUnlockArea;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }
}
