package dji.component.voice;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface IDJIVoice {

    @Retention(RetentionPolicy.SOURCE)
    public @interface Voice {
        public static final int CAMERA_CAPTURE = 1;
        public static final int CAMERA_METERING = 22;
        public static final int CAMERA_RECORD_START = 2;
        public static final int CAMERA_RECORD_STOP = 3;
        public static final int COLLISION_AUTH_AREA = 14;
        public static final int COLLISION_LIMIT_AREA = 12;
        public static final int COLLISION_LIMIT_HEIGHT = 13;
        public static final int COUNTDOWN_1 = 9;
        public static final int COUNTDOWN_2 = 10;
        public static final int COUNTDOWN_3 = 11;
        public static final int EXAMPLE = 0;
        public static final int GO_HOME = 7;
        public static final int GO_HOME_SMART_LOW_BATTERY = 8;
        public static final int HOME_POINT_IS_UPDATE = 18;
        public static final int IN_ATTI_MODE = 19;
        public static final int LANDING = 6;
        public static final int NEWBIE_GUIDE_CHECK_BEFORE_FLIGHT = 100;
        public static final int NEWBIE_GUIDE_FINISH = 111;
        public static final int NEWBIE_GUIDE_LANDING = 110;
        public static final int NEWBIE_GUIDE_STICK_BACKWARD = 107;
        public static final int NEWBIE_GUIDE_STICK_DOWN = 103;
        public static final int NEWBIE_GUIDE_STICK_FORWARD = 106;
        public static final int NEWBIE_GUIDE_STICK_LEFT = 108;
        public static final int NEWBIE_GUIDE_STICK_RIGHT = 109;
        public static final int NEWBIE_GUIDE_STICK_TURN_LEFT = 104;
        public static final int NEWBIE_GUIDE_STICK_TURN_RIGHT = 105;
        public static final int NEWBIE_GUIDE_STICK_UP = 102;
        public static final int NEWBIE_GUIDE_TAKE_OFF = 101;
        public static final int QUICK_SHOT_ENTER_WAITING_TO_CONFIRM = 4;
        public static final int REACH_DISTANCE_LIMIT = 20;
        public static final int REACH_HEIGHT_LIMIT = 21;
        public static final int SERIOUS_LOW_POWER = 16;
        public static final int SERIOUS_LOW_VOLTAGE = 17;
        public static final int START_AUTO_LAND = 15;
        public static final int TAKE_OFF = 5;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Priority {
        public static final int HIGH = 2;
        public static final int LOW = 0;
        public static final int NORMAL = 1;
    }
}
