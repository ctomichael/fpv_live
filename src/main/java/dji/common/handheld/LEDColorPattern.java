package dji.common.handheld;

import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;

@EXClassNullAway
public class LEDColorPattern {
    public ArrayList<Boolean> pattern;
    public int repeatTimes = 255;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LEDColorPattern that = (LEDColorPattern) o;
        if (this.repeatTimes != that.repeatTimes) {
            return false;
        }
        if (this.pattern != null) {
            return this.pattern.equals(that.pattern);
        }
        if (that.pattern != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((this.pattern != null ? this.pattern.hashCode() : 0) * 31) + this.repeatTimes;
    }
}
