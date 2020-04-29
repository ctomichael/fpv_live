package dji.flysafe;

import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

public enum RTHTipsEventType implements WireEnum {
    NearBy(0),
    Cross(1);
    
    public static final ProtoAdapter<RTHTipsEventType> ADAPTER = ProtoAdapter.newEnumAdapter(RTHTipsEventType.class);
    private final int value;

    private RTHTipsEventType(int value2) {
        this.value = value2;
    }

    public static RTHTipsEventType fromValue(int value2) {
        switch (value2) {
            case 0:
                return NearBy;
            case 1:
                return Cross;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }
}
