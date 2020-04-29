package lecho.lib.hellocharts.listener;

import lecho.lib.hellocharts.model.BubbleValue;

public interface BubbleChartOnValueSelectListener extends OnValueDeselectListener {
    void onValueSelected(int i, BubbleValue bubbleValue);
}
