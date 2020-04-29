package dji.flysafe;

import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

public enum SpecialUnlockType implements WireEnum {
    InvalidUnlock(0),
    CircleUnlock(1),
    PolygonUnlock(2);
    
    public static final ProtoAdapter<SpecialUnlockType> ADAPTER = ProtoAdapter.newEnumAdapter(SpecialUnlockType.class);
    private final int value;

    private SpecialUnlockType(int value2) {
        this.value = value2;
    }

    public static SpecialUnlockType fromValue(int value2) {
        switch (value2) {
            case 0:
                return InvalidUnlock;
            case 1:
                return CircleUnlock;
            case 2:
                return PolygonUnlock;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }
}
