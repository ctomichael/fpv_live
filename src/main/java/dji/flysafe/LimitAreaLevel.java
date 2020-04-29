package dji.flysafe;

import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

public enum LimitAreaLevel implements WireEnum {
    Warning(0),
    CanUnlock(1),
    NoUnlock(2),
    SpecialWarning(3),
    InVisible(4);
    
    public static final ProtoAdapter<LimitAreaLevel> ADAPTER = ProtoAdapter.newEnumAdapter(LimitAreaLevel.class);
    private final int value;

    private LimitAreaLevel(int value2) {
        this.value = value2;
    }

    public static LimitAreaLevel fromValue(int value2) {
        switch (value2) {
            case 0:
                return Warning;
            case 1:
                return CanUnlock;
            case 2:
                return NoUnlock;
            case 3:
                return SpecialWarning;
            case 4:
                return InVisible;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }
}
