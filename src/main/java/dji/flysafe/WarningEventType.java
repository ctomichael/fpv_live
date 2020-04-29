package dji.flysafe;

import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

public enum WarningEventType implements WireEnum {
    TakeOffFailedUnderLimitArea(0),
    TakeOffFailedUnderLimitAreaWithoutGPS(1),
    TakeOffFailedWithWhiteList(2),
    TakeOffFailedUnderAuthAreaWithoutGPSAndLicense(3),
    TakeOffFailedUnderAuthAreaWithoutLicense(4),
    TakeOffFailedUnderAuthArea(5),
    TakeOffUnderSpecialWarningArea(6),
    TakeOffWithLimitAreaNearby(7),
    TakeOffWithLimitHeightAreaNearby(8),
    TakeOffWithAuthAreaNearbyWithoutLicense(9),
    TakeOffWithAuthAreaNearby(10),
    TakeOffWithFlySafeAreaNearby(11),
    CollisionWithAuthArea(12),
    TakeOffFailedUnderLimitAreaWithGPSOnce(13),
    HaveOneHourWillApplyTFRs(14);
    
    public static final ProtoAdapter<WarningEventType> ADAPTER = ProtoAdapter.newEnumAdapter(WarningEventType.class);
    private final int value;

    private WarningEventType(int value2) {
        this.value = value2;
    }

    public static WarningEventType fromValue(int value2) {
        switch (value2) {
            case 0:
                return TakeOffFailedUnderLimitArea;
            case 1:
                return TakeOffFailedUnderLimitAreaWithoutGPS;
            case 2:
                return TakeOffFailedWithWhiteList;
            case 3:
                return TakeOffFailedUnderAuthAreaWithoutGPSAndLicense;
            case 4:
                return TakeOffFailedUnderAuthAreaWithoutLicense;
            case 5:
                return TakeOffFailedUnderAuthArea;
            case 6:
                return TakeOffUnderSpecialWarningArea;
            case 7:
                return TakeOffWithLimitAreaNearby;
            case 8:
                return TakeOffWithLimitHeightAreaNearby;
            case 9:
                return TakeOffWithAuthAreaNearbyWithoutLicense;
            case 10:
                return TakeOffWithAuthAreaNearby;
            case 11:
                return TakeOffWithFlySafeAreaNearby;
            case 12:
                return CollisionWithAuthArea;
            case 13:
                return TakeOffFailedUnderLimitAreaWithGPSOnce;
            case 14:
                return HaveOneHourWillApplyTFRs;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }
}
