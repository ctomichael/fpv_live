package lecho.lib.hellocharts.provider;

import lecho.lib.hellocharts.model.LineChartData;

public interface LineChartDataProvider {
    LineChartData getLineChartData();

    void setLineChartData(LineChartData lineChartData);
}
