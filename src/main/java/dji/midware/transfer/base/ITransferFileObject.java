package dji.midware.transfer.base;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface ITransferFileObject {

    public enum TransferCmdType {
        REQUEST(1),
        DATA(2),
        VERIFY(3),
        DATA_EXTENDED(4),
        REAL_TIME_DATA(5);
        
        private final int mValue;

        private TransferCmdType(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }
    }

    public enum TransferErrorCode {
        SDCARD_NON(232),
        SDCARD_NOT_ENOUGH(233),
        FILE_VERIFY_ERROR(242),
        FILE_WRITE_ERROR(244),
        SUCCESS(0),
        OTHER(255);
        
        private final int mValue;

        private TransferErrorCode(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        public boolean _equals(int value) {
            return this.mValue == value;
        }

        public static TransferErrorCode find(int b) {
            TransferErrorCode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum CommonTransferFileType {
        FIRMWARE(1),
        PANORAMA(2),
        QUICK_SHOT(3),
        AUDIO(4),
        OTHER(255);
        
        private final int mValue;

        private CommonTransferFileType(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }
    }

    public enum CommonTransferVerifyType {
        MD5(1),
        CRC16(2),
        CRC32(3),
        OTHER(255);
        
        private final int mValue;

        private CommonTransferVerifyType(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        public boolean _equals(int value) {
            return this.mValue == value;
        }

        public static CommonTransferVerifyType find(int b) {
            CommonTransferVerifyType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum CommonTransferProtocolType {
        PROTOCOL_1(0),
        PROTOCOL_2(1),
        OTHER(255);
        
        private final int mValue;

        private CommonTransferProtocolType(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        public boolean _equals(int value) {
            return this.mValue == value;
        }

        public static CommonTransferProtocolType find(int b) {
            CommonTransferProtocolType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
