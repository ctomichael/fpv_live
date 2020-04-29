package dji.common.flightcontroller;

public enum HomePointState {
    RECORD(0),
    UPDATE(1),
    NONE(2);
    
    private static volatile HomePointState[] sValues = null;
    private int _value = 0;

    private HomePointState(int value) {
        this._value = value;
    }

    public int value() {
        return this._value;
    }

    public boolean belongsTo(int value) {
        return this._value == value;
    }

    public static HomePointState find(int value) {
        if (sValues == null) {
            sValues = values();
        }
        HomePointState result = NONE;
        HomePointState[] homePointStateArr = sValues;
        for (HomePointState action : homePointStateArr) {
            if (action.belongsTo(value)) {
                return action;
            }
        }
        return result;
    }
}
