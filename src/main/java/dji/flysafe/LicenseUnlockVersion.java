package dji.flysafe;

import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

public enum LicenseUnlockVersion implements WireEnum {
    Version2(0),
    Version3(1),
    VersionUnknown(255);
    
    public static final ProtoAdapter<LicenseUnlockVersion> ADAPTER = ProtoAdapter.newEnumAdapter(LicenseUnlockVersion.class);
    private final int value;

    private LicenseUnlockVersion(int value2) {
        this.value = value2;
    }

    public static LicenseUnlockVersion fromValue(int value2) {
        switch (value2) {
            case 0:
                return Version2;
            case 1:
                return Version3;
            case 255:
                return VersionUnknown;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }
}
