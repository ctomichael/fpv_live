package dji.midware.tlv;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.tlv.dataParser.DJISTLVPackDataParserFactory;
import dji.midware.tlv.dataParser.IDJISTLVPackDataParser;

@EXClassNullAway
public class DJISTLVPackFactory {

    public enum Type {
        NONE(0),
        VIDEO_POOL_AIRCRAFT_STATUS(1),
        VIDEO_POOL_FLIGHT_ANALYTICS(2),
        VIDEO_POOL_REMOTE_TIME_TAG(3);
        
        private int type;

        private Type(int _type) {
            this.type = _type;
        }

        public int _value() {
            return this.type;
        }

        public boolean _equals(int _type) {
            return this.type == _type;
        }

        public static Type find(int _type) {
            Type result = NONE;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(_type)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public static DJISTLVBasePack getPack(Type type) {
        switch (type) {
            case VIDEO_POOL_AIRCRAFT_STATUS:
                return new DJIVideoPoolAircraftStatusPack();
            case VIDEO_POOL_FLIGHT_ANALYTICS:
                return new DJIVideoPoolFlightAnalyticsPack();
            case VIDEO_POOL_REMOTE_TIME_TAG:
                return new DJIVideoPoolRemoteTimeTagPack();
            default:
                return new DJISTLVBasePack();
        }
    }

    public static IDJISTLVPackDataParser getDataParser(Type type) {
        return DJISTLVPackDataParserFactory.getParser(type);
    }
}
