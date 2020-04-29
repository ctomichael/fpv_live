package dji.component.areacode;

public class DJIAreaCodeStrategyEvent {
    public String ac;
    public AreaCodeStrategy strategy;

    public DJIAreaCodeStrategyEvent(String ac2, AreaCodeStrategy strategy2) {
        this.ac = ac2;
        this.strategy = strategy2;
    }
}
