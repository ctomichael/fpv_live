package dji.component.analysis;

import java.util.Map;

public interface IDJIAnalytics {
    public static final String COMPONENT_NAME = "DJIAnalytics";

    void configAppStateInfo();

    void report(String str, Map<String, ?> map);
}
