package lecho.lib.hellocharts.formatter;

import lecho.lib.hellocharts.model.SubcolumnValue;

public interface ColumnChartValueFormatter {
    int formatChartValue(char[] cArr, SubcolumnValue subcolumnValue);
}
