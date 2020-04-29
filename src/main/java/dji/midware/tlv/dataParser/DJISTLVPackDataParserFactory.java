package dji.midware.tlv.dataParser;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.tlv.DJISTLVPackFactory;

@EXClassNullAway
public class DJISTLVPackDataParserFactory {
    public static IDJISTLVPackDataParser getParser(DJISTLVPackFactory.Type type) {
        switch (type) {
            case VIDEO_POOL_AIRCRAFT_STATUS:
                return new DJIVideoPoolAircraftStatusPackDataParser();
            case VIDEO_POOL_FLIGHT_ANALYTICS:
                return new DJIVideoPoolFlightAnalyticsPackDataParser();
            case VIDEO_POOL_REMOTE_TIME_TAG:
                return new DJIVideoPoolRemoteTimeTagPackDataParser();
            default:
                return new DJISTLVBasePackDataParser();
        }
    }
}
