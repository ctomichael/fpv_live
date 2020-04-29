package dji.common.flightcontroller;

public enum FlightAction {
    NONE(0),
    WARNING_POWER_GOHOME(1),
    WARNING_POWER_LANDING(2),
    SMART_POWER_GOHOME(3),
    SMART_POWER_LANDING(4),
    LOW_VOLTAGE_LANDING(5),
    LOW_VOLTAGE_GOHOME(6),
    SERIOUS_LOW_VOLTAGE_LANDING(7),
    RC_ONEKEY_GOHOME(8),
    RC_ASSISTANT_TAKEOFF(9),
    RC_AUTO_TAKEOFF(10),
    RC_AUTO_LANDING(11),
    APP_AUTO_GOHOME(12),
    APP_AUTO_LANDING(13),
    APP_AUTO_TAKEOFF(14),
    OUTOF_CONTROL_GOHOME(15),
    API_AUTO_TAKEOFF(16),
    API_AUTO_LANDING(17),
    API_AUTO_GOHOME(18),
    AVOID_GROUND_LANDING(19),
    AIRPORT_AVOID_LANDING(20),
    TOO_CLOSE_GOHOME_LANDING(21),
    TOO_FAR_GOHOME_LANDING(22),
    APP_WP_MISSION(23),
    WP_AUTO_TAKEOFF(24),
    GOHOME_AVOID(25),
    GOHOME_FINISH(26),
    VERT_LOW_LIMIT_LANDING(27),
    BATTERY_FORCE_LANDING(28),
    MC_PROTECT_GOHOME(29),
    MOTORBLOCK_LANDING(30),
    APP_REQUEST_FORCE_LANDING(31),
    FAKEBATTERY_LANDING(32),
    RTH_COMING_OBSTACLE_LANDING(33),
    IMUERROR_RTH(34),
    LANDING_FOR_PLANE(38);
    
    private static volatile FlightAction[] sValues = null;
    private int _value = 0;

    private FlightAction(int value) {
        this._value = value;
    }

    public int value() {
        return this._value;
    }

    public boolean belongsTo(int value) {
        return this._value == value;
    }

    public static FlightAction find(int value) {
        if (sValues == null) {
            sValues = values();
        }
        FlightAction result = NONE;
        FlightAction[] flightActionArr = sValues;
        for (FlightAction action : flightActionArr) {
            if (action.belongsTo(value)) {
                return action;
            }
        }
        return result;
    }
}
