package dji.common.camera;

public class ColorWaveformSettings {

    public enum ColorWaveformDisplayMode {
        MIX(0),
        SEPARATE(1),
        UNKNOWN(255);
        
        private final int value;

        private ColorWaveformDisplayMode(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static ColorWaveformDisplayMode find(int value2) {
            ColorWaveformDisplayMode result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public enum ColorWaveformDisplayState {
        SWITCH_TO_EXP(0),
        SWITCH_TO_COLOR(1),
        UNKNOWN(255);
        
        private final int value;

        private ColorWaveformDisplayState(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static ColorWaveformDisplayState find(int value2) {
            ColorWaveformDisplayState result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
