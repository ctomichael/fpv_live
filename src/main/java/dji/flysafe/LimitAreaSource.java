package dji.flysafe;

import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

public enum LimitAreaSource implements WireEnum {
    DJISourceOne(0),
    DJISourceTwo(1),
    Airmap(2);
    
    public static final ProtoAdapter<LimitAreaSource> ADAPTER = ProtoAdapter.newEnumAdapter(LimitAreaSource.class);
    private final int value;

    private LimitAreaSource(int value2) {
        this.value = value2;
    }

    public static LimitAreaSource fromValue(int value2) {
        switch (value2) {
            case 0:
                return DJISourceOne;
            case 1:
                return DJISourceTwo;
            case 2:
                return Airmap;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }
}
