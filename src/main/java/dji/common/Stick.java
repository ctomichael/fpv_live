package dji.common;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Stick {
    private int horizontalPosition;
    private int verticalPosition;

    public Stick(int horizontalPosition2, int verticalPosition2) {
        this.horizontalPosition = horizontalPosition2;
        this.verticalPosition = verticalPosition2;
    }

    public int getHorizontalPosition() {
        return this.horizontalPosition;
    }

    public int getVerticalPosition() {
        return this.verticalPosition;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stick stick = (Stick) o;
        if (this.horizontalPosition != stick.horizontalPosition) {
            return false;
        }
        if (this.verticalPosition != stick.verticalPosition) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.horizontalPosition * 31) + this.verticalPosition;
    }

    public String toString() {
        return "Stick{horizontalPosition=" + this.horizontalPosition + ", verticalPosition=" + this.verticalPosition + '}';
    }
}
