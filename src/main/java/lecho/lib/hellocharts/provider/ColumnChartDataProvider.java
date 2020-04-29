package lecho.lib.hellocharts.provider;

import lecho.lib.hellocharts.model.ColumnChartData;

public interface ColumnChartDataProvider {
    ColumnChartData getColumnChartData();

    void setColumnChartData(ColumnChartData columnChartData);
}
