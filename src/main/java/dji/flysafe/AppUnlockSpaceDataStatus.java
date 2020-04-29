package dji.flysafe;

import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

public enum AppUnlockSpaceDataStatus implements WireEnum {
    Inactivate(0),
    Valid(1),
    Expired(2);
    
    public static final ProtoAdapter<AppUnlockSpaceDataStatus> ADAPTER = ProtoAdapter.newEnumAdapter(AppUnlockSpaceDataStatus.class);
    private final int value;

    private AppUnlockSpaceDataStatus(int value2) {
        this.value = value2;
    }

    public static AppUnlockSpaceDataStatus fromValue(int value2) {
        switch (value2) {
            case 0:
                return Inactivate;
            case 1:
                return Valid;
            case 2:
                return Expired;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }
}
