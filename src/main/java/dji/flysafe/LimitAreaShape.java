package dji.flysafe;

import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

public enum LimitAreaShape implements WireEnum {
    Circle(0),
    Polygon(1),
    Virtual(12);
    
    public static final ProtoAdapter<LimitAreaShape> ADAPTER = ProtoAdapter.newEnumAdapter(LimitAreaShape.class);
    private final int value;

    private LimitAreaShape(int value2) {
        this.value = value2;
    }

    public static LimitAreaShape fromValue(int value2) {
        switch (value2) {
            case 0:
                return Circle;
            case 1:
                return Polygon;
            case 12:
                return Virtual;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }
}
