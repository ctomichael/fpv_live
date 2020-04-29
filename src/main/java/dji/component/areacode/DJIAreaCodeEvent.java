package dji.component.areacode;

public class DJIAreaCodeEvent {
    public String areaCode;
    public AreaCodeStrategy strategy;

    public DJIAreaCodeEvent(String ac, AreaCodeStrategy strategy2) {
        this.areaCode = ac;
        this.strategy = strategy2;
    }

    public String toString() {
        return "DJIAreaCodeEvent{areaCode='" + this.areaCode + '\'' + ", strategy=" + this.strategy + '}';
    }
}
