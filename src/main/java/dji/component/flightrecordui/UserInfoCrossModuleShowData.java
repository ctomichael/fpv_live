package dji.component.flightrecordui;

public class UserInfoCrossModuleShowData {
    public double duration;
    public boolean isSyncMode;
    public double mileage;
    public int totallyFlight;

    public UserInfoCrossModuleShowData(double mileage2, double duration2, int totallyFlight2, boolean isSyncMode2) {
        this.mileage = mileage2;
        this.duration = duration2;
        this.totallyFlight = totallyFlight2;
        this.isSyncMode = isSyncMode2;
    }
}
