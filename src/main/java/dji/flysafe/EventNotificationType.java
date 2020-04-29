package dji.flysafe;

import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

public enum EventNotificationType implements WireEnum {
    ShowWarning(0),
    ShowTips(1),
    ShowCDLWarning(2),
    ShowRTHTips(3),
    ShowAreas(4),
    ShowSpecialUnlockArea(5),
    UpdateLicenseUnlockVersion(6),
    ChinaAirportWarningArea(7),
    SupervisorRecord(8),
    FlySafeLimitInfoRecord(9),
    V1WhiteListInfoUpdated(10),
    AircraftLoadingDatabaseProgress(11);
    
    public static final ProtoAdapter<EventNotificationType> ADAPTER = ProtoAdapter.newEnumAdapter(EventNotificationType.class);
    private final int value;

    private EventNotificationType(int value2) {
        this.value = value2;
    }

    public static EventNotificationType fromValue(int value2) {
        switch (value2) {
            case 0:
                return ShowWarning;
            case 1:
                return ShowTips;
            case 2:
                return ShowCDLWarning;
            case 3:
                return ShowRTHTips;
            case 4:
                return ShowAreas;
            case 5:
                return ShowSpecialUnlockArea;
            case 6:
                return UpdateLicenseUnlockVersion;
            case 7:
                return ChinaAirportWarningArea;
            case 8:
                return SupervisorRecord;
            case 9:
                return FlySafeLimitInfoRecord;
            case 10:
                return V1WhiteListInfoUpdated;
            case 11:
                return AircraftLoadingDatabaseProgress;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }
}
