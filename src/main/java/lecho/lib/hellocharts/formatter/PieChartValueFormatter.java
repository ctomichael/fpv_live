package lecho.lib.hellocharts.formatter;

import lecho.lib.hellocharts.model.SliceValue;

public interface PieChartValueFormatter {
    int formatChartValue(char[] cArr, SliceValue sliceValue);
}
