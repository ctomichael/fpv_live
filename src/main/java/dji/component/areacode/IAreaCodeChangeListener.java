package dji.component.areacode;

public interface IAreaCodeChangeListener {
    void onAreaCodeChanged(DJIAreaCodeEvent dJIAreaCodeEvent);

    void onAreaCodeStrategyChanged(DJIAreaCodeStrategyEvent dJIAreaCodeStrategyEvent);
}
