package lecho.lib.hellocharts.model;

import dji.component.accountcenter.IMemberProtocol;
import java.util.Arrays;
import lecho.lib.hellocharts.util.ChartUtils;

public class BubbleValue {
    private int color = ChartUtils.DEFAULT_COLOR;
    private int darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
    private float diffX;
    private float diffY;
    private float diffZ;
    private char[] label;
    private float originX;
    private float originY;
    private float originZ;
    private ValueShape shape = ValueShape.CIRCLE;
    private float x;
    private float y;
    private float z;

    public BubbleValue() {
        set(0.0f, 0.0f, 0.0f);
    }

    public BubbleValue(float x2, float y2, float z2) {
        set(x2, y2, z2);
    }

    public BubbleValue(float x2, float y2, float z2, int color2) {
        set(x2, y2, z2);
        setColor(color2);
    }

    public BubbleValue(BubbleValue bubbleValue) {
        set(bubbleValue.x, bubbleValue.y, bubbleValue.z);
        setColor(bubbleValue.color);
        this.label = bubbleValue.label;
    }

    public void update(float scale) {
        this.x = this.originX + (this.diffX * scale);
        this.y = this.originY + (this.diffY * scale);
        this.z = this.originZ + (this.diffZ * scale);
    }

    public void finish() {
        set(this.originX + this.diffX, this.originY + this.diffY, this.originZ + this.diffZ);
    }

    public BubbleValue set(float x2, float y2, float z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
        this.originX = x2;
        this.originY = y2;
        this.originZ = z2;
        this.diffX = 0.0f;
        this.diffY = 0.0f;
        this.diffZ = 0.0f;
        return this;
    }

    public BubbleValue setTarget(float targetX, float targetY, float targetZ) {
        set(this.x, this.y, this.z);
        this.diffX = targetX - this.originX;
        this.diffY = targetY - this.originY;
        this.diffZ = targetZ - this.originZ;
        return this;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public int getColor() {
        return this.color;
    }

    public BubbleValue setColor(int color2) {
        this.color = color2;
        this.darkenColor = ChartUtils.darkenColor(color2);
        return this;
    }

    public int getDarkenColor() {
        return this.darkenColor;
    }

    public ValueShape getShape() {
        return this.shape;
    }

    public BubbleValue setShape(ValueShape shape2) {
        this.shape = shape2;
        return this;
    }

    @Deprecated
    public char[] getLabel() {
        return this.label;
    }

    public BubbleValue setLabel(String label2) {
        this.label = label2.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return this.label;
    }

    @Deprecated
    public BubbleValue setLabel(char[] label2) {
        this.label = label2;
        return this;
    }

    public String toString() {
        return "BubbleValue [x=" + this.x + ", y=" + this.y + ", z=" + this.z + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BubbleValue that = (BubbleValue) o;
        if (this.color != that.color) {
            return false;
        }
        if (this.darkenColor != that.darkenColor) {
            return false;
        }
        if (Float.compare(that.diffX, this.diffX) != 0) {
            return false;
        }
        if (Float.compare(that.diffY, this.diffY) != 0) {
            return false;
        }
        if (Float.compare(that.diffZ, this.diffZ) != 0) {
            return false;
        }
        if (Float.compare(that.originX, this.originX) != 0) {
            return false;
        }
        if (Float.compare(that.originY, this.originY) != 0) {
            return false;
        }
        if (Float.compare(that.originZ, this.originZ) != 0) {
            return false;
        }
        if (Float.compare(that.x, this.x) != 0) {
            return false;
        }
        if (Float.compare(that.y, this.y) != 0) {
            return false;
        }
        if (Float.compare(that.z, this.z) != 0) {
            return false;
        }
        if (!Arrays.equals(this.label, that.label)) {
            return false;
        }
        if (this.shape != that.shape) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10 = 0;
        if (this.x != 0.0f) {
            result = Float.floatToIntBits(this.x);
        } else {
            result = 0;
        }
        int i11 = result * 31;
        if (this.y != 0.0f) {
            i = Float.floatToIntBits(this.y);
        } else {
            i = 0;
        }
        int i12 = (i11 + i) * 31;
        if (this.z != 0.0f) {
            i2 = Float.floatToIntBits(this.z);
        } else {
            i2 = 0;
        }
        int i13 = (i12 + i2) * 31;
        if (this.originX != 0.0f) {
            i3 = Float.floatToIntBits(this.originX);
        } else {
            i3 = 0;
        }
        int i14 = (i13 + i3) * 31;
        if (this.originY != 0.0f) {
            i4 = Float.floatToIntBits(this.originY);
        } else {
            i4 = 0;
        }
        int i15 = (i14 + i4) * 31;
        if (this.originZ != 0.0f) {
            i5 = Float.floatToIntBits(this.originZ);
        } else {
            i5 = 0;
        }
        int i16 = (i15 + i5) * 31;
        if (this.diffX != 0.0f) {
            i6 = Float.floatToIntBits(this.diffX);
        } else {
            i6 = 0;
        }
        int i17 = (i16 + i6) * 31;
        if (this.diffY != 0.0f) {
            i7 = Float.floatToIntBits(this.diffY);
        } else {
            i7 = 0;
        }
        int i18 = (i17 + i7) * 31;
        if (this.diffZ != 0.0f) {
            i8 = Float.floatToIntBits(this.diffZ);
        } else {
            i8 = 0;
        }
        int i19 = (((((i18 + i8) * 31) + this.color) * 31) + this.darkenColor) * 31;
        if (this.shape != null) {
            i9 = this.shape.hashCode();
        } else {
            i9 = 0;
        }
        int i20 = (i19 + i9) * 31;
        if (this.label != null) {
            i10 = Arrays.hashCode(this.label);
        }
        return i20 + i10;
    }
}
