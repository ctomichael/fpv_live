package dji.common.handheld;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class LEDCommand {
    public LEDColorPattern blue;
    public LEDColorPattern green;
    public LEDColorPattern red;

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.red != null) {
            result = this.red.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.green != null) {
            i = this.green.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.blue != null) {
            i2 = this.blue.hashCode();
        }
        return i4 + i2;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof LEDCommand)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.red == null || ((LEDCommand) o).red == null) {
            if (this.red == null || ((LEDCommand) o).red == null) {
                return false;
            }
        } else if (!this.red.equals(((LEDCommand) o).red)) {
            return false;
        }
        if (this.green == null || ((LEDCommand) o).green == null) {
            if (this.green == null || ((LEDCommand) o).green == null) {
                return false;
            }
        } else if (!this.green.equals(((LEDCommand) o).green)) {
            return false;
        }
        if (this.blue == null || ((LEDCommand) o).blue == null) {
            if (this.blue == null || ((LEDCommand) o).blue == null) {
                return false;
            }
        } else if (!this.blue.equals(((LEDCommand) o).blue)) {
            return false;
        }
        return true;
    }
}
