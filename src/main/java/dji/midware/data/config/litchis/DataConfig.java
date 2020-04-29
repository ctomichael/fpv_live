package dji.midware.data.config.litchis;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DataConfig {
    public static final int PACK_MIN_LENGTH = 10;

    public enum CmdType {
        REQUEST(0),
        DATA(1),
        ACK(2),
        PUSH(3),
        ABORT(4),
        DEL(5),
        PAUSE(6),
        RESUME(7),
        UNDEFINED(100);
        
        private int data;

        private CmdType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CmdType find(int b) {
            CmdType result = UNDEFINED;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum CmdId {
        List(0),
        File(1),
        Stream(2),
        Num(3),
        UNDEFINED(100);
        
        private int data;

        private CmdId(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CmdId find(int b) {
            CmdId result = UNDEFINED;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum SubType {
        ORG(0),
        THM(1),
        SCR(2),
        CLIP(3),
        Stream(4),
        Pano(5),
        Pano_SCR(6),
        Pano_THM(7),
        TIMELAPSE(8),
        MP4(9),
        EXIF(10),
        EXIF_NEW(11),
        BOKEH(97),
        BOKEH_THM(98),
        BOKEH_SCR(99),
        UNDEFINED(100);
        
        private int data;

        private SubType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SubType find(int b) {
            SubType result = UNDEFINED;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
