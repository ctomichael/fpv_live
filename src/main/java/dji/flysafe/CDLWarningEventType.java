package dji.flysafe;

import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

public enum CDLWarningEventType implements WireEnum {
    InLimitArea(0),
    InLimitHeightArea(1),
    InAuthAreaWithoutLicense(2),
    InAuthAreaWithLicense(3),
    HaveThreeMinWillApplyTFRs(4);
    
    public static final ProtoAdapter<CDLWarningEventType> ADAPTER = ProtoAdapter.newEnumAdapter(CDLWarningEventType.class);
    private final int value;

    private CDLWarningEventType(int value2) {
        this.value = value2;
    }

    public static CDLWarningEventType fromValue(int value2) {
        switch (value2) {
            case 0:
                return InLimitArea;
            case 1:
                return InLimitHeightArea;
            case 2:
                return InAuthAreaWithoutLicense;
            case 3:
                return InAuthAreaWithLicense;
            case 4:
                return HaveThreeMinWillApplyTFRs;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }
}
