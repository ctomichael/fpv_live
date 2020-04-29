package lecho.lib.hellocharts.model;

import android.graphics.Typeface;

public interface ChartData {
    void finish();

    Axis getAxisXBottom();

    Axis getAxisXTop();

    Axis getAxisYLeft();

    Axis getAxisYRight();

    int getValueLabelBackgroundColor();

    int getValueLabelTextColor();

    int getValueLabelTextSize();

    Typeface getValueLabelTypeface();

    boolean isValueLabelBackgroundAuto();

    boolean isValueLabelBackgroundEnabled();

    void setAxisXBottom(Axis axis);

    void setAxisXTop(Axis axis);

    void setAxisYLeft(Axis axis);

    void setAxisYRight(Axis axis);

    void setValueLabelBackgroundAuto(boolean z);

    void setValueLabelBackgroundColor(int i);

    void setValueLabelBackgroundEnabled(boolean z);

    void setValueLabelTextSize(int i);

    void setValueLabelTypeface(Typeface typeface);

    void setValueLabelsTextColor(int i);

    void update(float f);
}
