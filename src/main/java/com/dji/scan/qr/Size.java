package com.dji.scan.qr;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Size implements Comparable<Size> {
    public final int height;
    public final int width;

    public Size(int width2, int height2) {
        this.width = width2;
        this.height = height2;
    }

    public Size rotate() {
        return new Size(this.height, this.width);
    }

    public Size scale(int n, int d) {
        return new Size((this.width * n) / d, (this.height * n) / d);
    }

    public Size scaleFit(Size into) {
        if (this.width * into.height >= into.width * this.height) {
            return new Size(into.width, (this.height * into.width) / this.width);
        }
        return new Size((this.width * into.height) / this.height, into.height);
    }

    public Size scaleCrop(Size into) {
        if (this.width * into.height <= into.width * this.height) {
            return new Size(into.width, (this.height * into.width) / this.width);
        }
        return new Size((this.width * into.height) / this.height, into.height);
    }

    public boolean fitsIn(Size other) {
        return this.width <= other.width && this.height <= other.height;
    }

    public int compareTo(@NonNull Size other) {
        int aPixels = this.height * this.width;
        int bPixels = other.height * other.width;
        if (bPixels < aPixels) {
            return 1;
        }
        if (bPixels > aPixels) {
            return -1;
        }
        return 0;
    }

    public String toString() {
        return this.width + "x" + this.height;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Size size = (Size) o;
        if (this.width == size.width && this.height == size.height) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (this.width * 31) + this.height;
    }
}
