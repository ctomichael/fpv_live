package dji.common.flightcontroller;

public class NavigationSystemError {
    private Component component;
    private String error;

    public interface Callback {
        void onNavigationSystemErrorUpdate(NavigationSystemError navigationSystemError);
    }

    public NavigationSystemError(Component component2, String error2) {
        this.component = component2;
        this.error = error2;
    }

    public Component getComponent() {
        return this.component;
    }

    public String getError() {
        return this.error;
    }

    public enum Component {
        NONE(0),
        ESC(1),
        PMU(2),
        SYSTEM(3),
        FMU(4),
        MC(5),
        IMU(6),
        GPS(7),
        GYROSCOPE(8),
        ACCELEROMETER(9),
        GYRO_ACC(10),
        BAROMETER(11),
        COMPASS(12),
        ULTRASONIC(13),
        VISION_SENSOR(14),
        RADAR(15),
        CAMERA(16),
        GIMBAL(17),
        TRANSFORMERS(18),
        LED(19),
        PC(20),
        REMOTE_CONTROLLER(21),
        APP(22),
        BASE_STATION(23),
        SDK(24),
        RTK(25),
        DROPSAFE(26),
        BATTERY(27),
        SPRAY(28),
        PITOT(29),
        TOF(30),
        UNKNOWN(255);
        
        private int value;

        private Component(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }

        private boolean _equals(int b) {
            return this.value == b;
        }

        public static Component find(int value2) {
            Component result = UNKNOWN;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(value2)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
