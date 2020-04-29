package dji.common.flightcontroller.rtk;

public enum ReferenceStationSource {
    NONE(0),
    BASE_STATION(2),
    CUSTOM_NETWORK_SERVICE(4),
    UNKNOWN(100);
    
    public int value;

    public interface Callback {
        void onReferenceStationSourceUpdate(ReferenceStationSource referenceStationSource);
    }

    private ReferenceStationSource(int value2) {
        this.value = value2;
    }

    public static ReferenceStationSource find(int value2) {
        ReferenceStationSource[] values = values();
        for (ReferenceStationSource item : values) {
            if (item.value == value2) {
                return item;
            }
        }
        return UNKNOWN;
    }
}
