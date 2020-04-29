package dji.logic.album.manager.accessory;

public class DJIAudioFileInfo {

    public enum AudioType {
        MAV(0),
        MP3(1),
        ACC(2),
        UNKNOWN(255);
        
        private final int mValue;

        private AudioType(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        private boolean _equals(int b) {
            return this.mValue == b;
        }

        public static AudioType find(int b) {
            AudioType result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum SamplingBit {
        BIT_8(0),
        BIT_16(1),
        UNKNOWN(255);
        
        private final int mValue;

        private SamplingBit(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        private boolean _equals(int b) {
            return this.mValue == b;
        }

        public static SamplingBit find(int b) {
            SamplingBit result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum SamplingFrequency {
        FREQUENCY_8K(0),
        FREQUENCY_16K(1),
        FREQUENCY_24K(2),
        FREQUENCY_32K(3),
        FREQUENCY_44_DOT_1K(4),
        FREQUENCY_64K(5),
        UNKNOWN(255);
        
        private final int mValue;

        private SamplingFrequency(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        private boolean _equals(int b) {
            return this.mValue == b;
        }

        public static SamplingFrequency find(int b) {
            SamplingFrequency result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
