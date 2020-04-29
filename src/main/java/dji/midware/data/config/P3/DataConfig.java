package dji.midware.data.config.P3;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DataConfig {
    public static final boolean DEBUG_ON = true;
    public static final int PACK_HEAD_LENGTH = 4;
    public static final int PACK_MIN_LENGTH = 13;

    public enum CMDTYPE {
        REQUEST(0),
        ACK(1);
        
        private int data;

        private CMDTYPE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }
    }

    public enum NEEDACK {
        YES(2),
        NO(0),
        YES_BY_PUSH(1);
        
        private int data;

        private NEEDACK(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }
    }

    public enum EncryptType {
        NO(0),
        DIC(1),
        OTHER(2),
        SIMPLE(3);
        
        private int data;

        private EncryptType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }
    }
}
