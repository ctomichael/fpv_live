package lecho.lib.hellocharts.model;

import dji.component.accountcenter.IMemberProtocol;
import java.util.Arrays;
import lecho.lib.hellocharts.util.ChartUtils;

public class SubcolumnValue {
    private int color = ChartUtils.DEFAULT_COLOR;
    private int darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
    private float diff;
    private char[] label;
    private float originValue;
    private float value;

    public SubcolumnValue() {
        setValue(0.0f);
    }

    public SubcolumnValue(float value2) {
        setValue(value2);
    }

    public SubcolumnValue(float value2, int color2) {
        setValue(value2);
        setColor(color2);
    }

    public SubcolumnValue(SubcolumnValue columnValue) {
        setValue(columnValue.value);
        setColor(columnValue.color);
        this.label = columnValue.label;
    }

    public void update(float scale) {
        this.value = this.originValue + (this.diff * scale);
    }

    public void finish() {
        setValue(this.originValue + this.diff);
    }

    public float getValue() {
        return this.value;
    }

    public SubcolumnValue setValue(float value2) {
        this.value = value2;
        this.originValue = value2;
        this.diff = 0.0f;
        return this;
    }

    public SubcolumnValue setTarget(float target) {
        setValue(this.value);
        this.diff = target - this.originValue;
        return this;
    }

    public int getColor() {
        return this.color;
    }

    public SubcolumnValue setColor(int color2) {
        this.color = color2;
        this.darkenColor = ChartUtils.darkenColor(color2);
        return this;
    }

    public int getDarkenColor() {
        return this.darkenColor;
    }

    @Deprecated
    public char[] getLabel() {
        return this.label;
    }

    public SubcolumnValue setLabel(String label2) {
        this.label = label2.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return this.label;
    }

    @Deprecated
    public SubcolumnValue setLabel(char[] label2) {
        this.label = label2;
        return this;
    }

    public String toString() {
        return "ColumnValue [value=" + this.value + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubcolumnValue that = (SubcolumnValue) o;
        if (this.color != that.color) {
            return false;
        }
        if (this.darkenColor != that.darkenColor) {
            return false;
        }
        if (Float.compare(that.diff, this.diff) != 0) {
            return false;
        }
        if (Float.compare(that.originValue, this.originValue) != 0) {
            return false;
        }
        if (Float.compare(that.value, this.value) != 0) {
            return false;
        }
        if (!Arrays.equals(this.label, that.label)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3 = 0;
        if (this.value != 0.0f) {
            result = Float.floatToIntBits(this.value);
        } else {
            result = 0;
        }
        int i4 = result * 31;
        if (this.originValue != 0.0f) {
            i = Float.floatToIntBits(this.originValue);
        } else {
            i = 0;
        }
        int i5 = (i4 + i) * 31;
        if (this.diff != 0.0f) {
            i2 = Float.floatToIntBits(this.diff);
        } else {
            i2 = 0;
        }
        int i6 = (((((i5 + i2) * 31) + this.color) * 31) + this.darkenColor) * 31;
        if (this.label != null) {
            i3 = Arrays.hashCode(this.label);
        }
        return i6 + i3;
    }
}
