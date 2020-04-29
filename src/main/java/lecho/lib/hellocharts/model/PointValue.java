package lecho.lib.hellocharts.model;

import dji.component.accountcenter.IMemberProtocol;
import java.util.Arrays;

public class PointValue {
    private float diffX;
    private float diffY;
    private char[] label;
    private float originX;
    private float originY;
    private float x;
    private float y;

    public PointValue() {
        set(0.0f, 0.0f);
    }

    public PointValue(float x2, float y2) {
        set(x2, y2);
    }

    public PointValue(PointValue pointValue) {
        set(pointValue.x, pointValue.y);
        this.label = pointValue.label;
    }

    public void update(float scale) {
        this.x = this.originX + (this.diffX * scale);
        this.y = this.originY + (this.diffY * scale);
    }

    public void finish() {
        set(this.originX + this.diffX, this.originY + this.diffY);
    }

    public PointValue set(float x2, float y2) {
        this.x = x2;
        this.y = y2;
        this.originX = x2;
        this.originY = y2;
        this.diffX = 0.0f;
        this.diffY = 0.0f;
        return this;
    }

    public PointValue setTarget(float targetX, float targetY) {
        set(this.x, this.y);
        this.diffX = targetX - this.originX;
        this.diffY = targetY - this.originY;
        return this;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    @Deprecated
    public char[] getLabel() {
        return this.label;
    }

    public PointValue setLabel(String label2) {
        this.label = label2.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return this.label;
    }

    @Deprecated
    public PointValue setLabel(char[] label2) {
        this.label = label2;
        return this;
    }

    public String toString() {
        return "PointValue [x=" + this.x + ", y=" + this.y + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PointValue that = (PointValue) o;
        if (Float.compare(that.diffX, this.diffX) != 0) {
            return false;
        }
        if (Float.compare(that.diffY, this.diffY) != 0) {
            return false;
        }
        if (Float.compare(that.originX, this.originX) != 0) {
            return false;
        }
        if (Float.compare(that.originY, this.originY) != 0) {
            return false;
        }
        if (Float.compare(that.x, this.x) != 0) {
            return false;
        }
        if (Float.compare(that.y, this.y) != 0) {
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
        int i3;
        int i4;
        int i5;
        int i6 = 0;
        if (this.x != 0.0f) {
            result = Float.floatToIntBits(this.x);
        } else {
            result = 0;
        }
        int i7 = result * 31;
        if (this.y != 0.0f) {
            i = Float.floatToIntBits(this.y);
        } else {
            i = 0;
        }
        int i8 = (i7 + i) * 31;
        if (this.originX != 0.0f) {
            i2 = Float.floatToIntBits(this.originX);
        } else {
            i2 = 0;
        }
        int i9 = (i8 + i2) * 31;
        if (this.originY != 0.0f) {
            i3 = Float.floatToIntBits(this.originY);
        } else {
            i3 = 0;
        }
        int i10 = (i9 + i3) * 31;
        if (this.diffX != 0.0f) {
            i4 = Float.floatToIntBits(this.diffX);
        } else {
            i4 = 0;
        }
        int i11 = (i10 + i4) * 31;
        if (this.diffY != 0.0f) {
            i5 = Float.floatToIntBits(this.diffY);
        } else {
            i5 = 0;
        }
        int i12 = (i11 + i5) * 31;
        if (this.label != null) {
            i6 = Arrays.hashCode(this.label);
        }
        return i12 + i6;
    }
}
