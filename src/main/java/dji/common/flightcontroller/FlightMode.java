package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum FlightMode {
    MANUAL(0),
    ATTI(1),
    ATTI_COURSE_LOCK(2),
    ATTI_HOVER(3),
    HOVER(4),
    GPS_BLAKE(5),
    GPS_ATTI(6),
    GPS_COURSE_LOCK(7),
    GPS_HOME_LOCK(8),
    GPS_HOT_POINT(9),
    ASSISTED_TAKEOFF(10),
    AUTO_TAKEOFF(11),
    AUTO_LANDING(12),
    ATTI_LANDING(13),
    GPS_WAYPOINT(14),
    GO_HOME(15),
    CLICK_GO(16),
    JOYSTICK(17),
    GPS_ATTI_WRISTBAND(18),
    CINEMATIC(19),
    ATTI_LIMITED(23),
    DRAW(24),
    GPS_FOLLOW_ME(25),
    ACTIVE_TRACK(26),
    TAP_FLY(27),
    PANO(28),
    FARMING(29),
    FPV(30),
    GPS_SPORT(31),
    GPS_NOVICE(32),
    CONFIRM_LANDING(33),
    TERRAIN_FOLLOW(35),
    PALM_CONTROL(36),
    QUICK_SHOT(37),
    TRIPOD(38),
    TRACK_SPOTLIGHT(39),
    MOTORS_JUST_STARTED(41),
    DETOUR(43),
    TIME_LAPSE(46),
    POI2(50),
    OMNI_MOVING(49),
    ADSB_AVOIDING(49),
    UNKNOWN(255);
    
    private static volatile FlightMode[] flightModes;
    private int data;

    private FlightMode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static FlightMode find(int value) {
        FlightMode result = UNKNOWN;
        if (flightModes == null) {
            flightModes = values();
        }
        for (int i = 0; i < flightModes.length; i++) {
            if (flightModes[i]._equals(value)) {
                return flightModes[i];
            }
        }
        return result;
    }
}
