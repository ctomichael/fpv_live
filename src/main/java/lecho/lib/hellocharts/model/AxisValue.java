package lecho.lib.hellocharts.model;

import java.util.Arrays;

public class AxisValue {
    private char[] label;
    private float value;

    public AxisValue(float value2) {
        setValue(value2);
    }

    @Deprecated
    public AxisValue(float value2, char[] label2) {
        this.value = value2;
        this.label = label2;
    }

    public AxisValue(AxisValue axisValue) {
        this.value = axisValue.value;
        this.label = axisValue.label;
    }

    public float getValue() {
        return this.value;
    }

    public AxisValue setValue(float value2) {
        this.value = value2;
        return this;
    }

    @Deprecated
    public char[] getLabel() {
        return this.label;
    }

    public AxisValue setLabel(String label2) {
        this.label = label2.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return this.label;
    }

    @Deprecated
    public AxisValue setLabel(char[] label2) {
        this.label = label2;
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AxisValue axisValue = (AxisValue) o;
        if (Float.compare(axisValue.value, this.value) != 0) {
            return false;
        }
        if (!Arrays.equals(this.label, axisValue.label)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.value != 0.0f) {
            result = Float.floatToIntBits(this.value);
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.label != null) {
            i = Arrays.hashCode(this.label);
        }
        return i2 + i;
    }
}
