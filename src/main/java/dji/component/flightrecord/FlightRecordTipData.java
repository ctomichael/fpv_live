package dji.component.flightrecord;

public class FlightRecordTipData {
    public Double continueTime;
    public String identifier;
    public Integer level;
    public Double startTime;
    public String tips;

    public FlightRecordTipData(Integer level2, String diagnostics, String tips2, Double continueTime2) {
        this.level = level2;
        this.identifier = diagnostics;
        this.tips = tips2;
        this.continueTime = continueTime2;
    }

    public String toString() {
        return "FlightRecordTipData{level=" + this.level + ", identifier='" + this.identifier + '\'' + ", tips='" + this.tips + '\'' + ", startTime=" + this.startTime + ", continueTime=" + this.continueTime + '}';
    }
}
