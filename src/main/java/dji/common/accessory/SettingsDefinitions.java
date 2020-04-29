package dji.common.accessory;

public class SettingsDefinitions {

    public enum SpeakerDataTransmissionState {
        IDLE,
        READY_TO_TRANSMIT,
        TRANSMITING,
        UNKNOWN
    }

    public enum SpeakerPlayingState {
        PLAYING,
        STOPPED,
        UNKNOWN
    }

    public enum PlayMode {
        SINGLE_ONCE(0),
        REPEAT_SINGLE(1),
        LIST_ORDER(2),
        LIST_CYCLE(3),
        UNKNOWN(255);
        
        private final int mValue;

        private PlayMode(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        private boolean _equals(int b) {
            return this.mValue == b;
        }

        public static PlayMode find(int b) {
            PlayMode result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum AudioStorageLocation {
        TEMPORARY(1),
        PERSISTENT(2),
        UNKNOWN(255);
        
        private final int mValue;

        private AudioStorageLocation(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        private boolean _equals(int b) {
            return this.mValue == b;
        }

        public static AudioStorageLocation find(int b) {
            AudioStorageLocation result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum PCMEncodingBitFormat {
        BIT_8(0),
        BIT_16(1),
        UNKNOWN(255);
        
        private final int mValue;

        private PCMEncodingBitFormat(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        private boolean _equals(int b) {
            return this.mValue == b;
        }

        public static PCMEncodingBitFormat find(int b) {
            PCMEncodingBitFormat result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum AudioSampleRateInHz {
        HZ_8000(0),
        HZ_16000(1),
        HZ_22050(2),
        HZ_32K(3),
        HZ_44100(4),
        HZ_48000(5),
        UNKNOWN(255);
        
        private final int mValue;

        private AudioSampleRateInHz(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        private boolean _equals(int b) {
            return this.mValue == b;
        }

        public static AudioSampleRateInHz find(int b) {
            AudioSampleRateInHz result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
