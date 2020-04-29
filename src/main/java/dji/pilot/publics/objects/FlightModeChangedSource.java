package dji.pilot.publics.objects;

public enum FlightModeChangedSource {
    User(0),
    AppResume(1),
    AppEnterSmart(2),
    AppForbid(3),
    AppExitSelf(4),
    Rc(5),
    Navigation(6),
    Flyc(7),
    CameraLostConnect(8),
    LostConnect(9),
    GroundStation(10),
    Unknown(255);
    
    int _value;

    private FlightModeChangedSource(int value) {
        this._value = value;
    }
}
