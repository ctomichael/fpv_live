package lecho.lib.hellocharts.provider;

import lecho.lib.hellocharts.model.PieChartData;

public interface PieChartDataProvider {
    PieChartData getPieChartData();

    void setPieChartData(PieChartData pieChartData);
}
