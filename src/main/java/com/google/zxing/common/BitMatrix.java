package com.google.zxing.common;

import java.util.Arrays;

public final class BitMatrix implements Cloneable {
    private final int[] bits;
    private final int height;
    private final int rowSize;
    private final int width;

    public BitMatrix(int dimension) {
        this(dimension, dimension);
    }

    public BitMatrix(int width2, int height2) {
        if (width2 <= 0 || height2 <= 0) {
            throw new IllegalArgumentException("Both dimensions must be greater than 0");
        }
        this.width = width2;
        this.height = height2;
        this.rowSize = (width2 + 31) / 32;
        this.bits = new int[(this.rowSize * height2)];
    }

    private BitMatrix(int width2, int height2, int rowSize2, int[] bits2) {
        this.width = width2;
        this.height = height2;
        this.rowSize = rowSize2;
        this.bits = bits2;
    }

    public static BitMatrix parse(boolean[][] image) {
        int height2 = image.length;
        int width2 = image[0].length;
        BitMatrix bits2 = new BitMatrix(width2, height2);
        for (int i = 0; i < height2; i++) {
            boolean[] imageI = image[i];
            for (int j = 0; j < width2; j++) {
                if (imageI[j]) {
                    bits2.set(j, i);
                }
            }
        }
        return bits2;
    }

    public static BitMatrix parse(String stringRepresentation, String setString, String unsetString) {
        if (stringRepresentation == null) {
            throw new IllegalArgumentException();
        }
        boolean[] bits2 = new boolean[stringRepresentation.length()];
        int bitsPos = 0;
        int rowStartPos = 0;
        int rowLength = -1;
        int nRows = 0;
        int pos = 0;
        while (pos < stringRepresentation.length()) {
            if (stringRepresentation.charAt(pos) == 10 || stringRepresentation.charAt(pos) == 13) {
                if (bitsPos > rowStartPos) {
                    if (rowLength == -1) {
                        rowLength = bitsPos - rowStartPos;
                    } else if (bitsPos - rowStartPos != rowLength) {
                        throw new IllegalArgumentException("row lengths do not match");
                    }
                    rowStartPos = bitsPos;
                    nRows++;
                }
                pos++;
            } else if (stringRepresentation.substring(pos, setString.length() + pos).equals(setString)) {
                pos += setString.length();
                bits2[bitsPos] = true;
                bitsPos++;
            } else if (stringRepresentation.substring(pos, unsetString.length() + pos).equals(unsetString)) {
                pos += unsetString.length();
                bits2[bitsPos] = false;
                bitsPos++;
            } else {
                throw new IllegalArgumentException("illegal character encountered: " + stringRepresentation.substring(pos));
            }
        }
        if (bitsPos > rowStartPos) {
            if (rowLength == -1) {
                rowLength = bitsPos - rowStartPos;
            } else if (bitsPos - rowStartPos != rowLength) {
                throw new IllegalArgumentException("row lengths do not match");
            }
            nRows++;
        }
        BitMatrix matrix = new BitMatrix(rowLength, nRows);
        for (int i = 0; i < bitsPos; i++) {
            if (bits2[i]) {
                matrix.set(i % rowLength, i / rowLength);
            }
        }
        return matrix;
    }

    public boolean get(int x, int y) {
        return ((this.bits[(this.rowSize * y) + (x / 32)] >>> (x & 31)) & 1) != 0;
    }

    public void set(int x, int y) {
        int offset = (this.rowSize * y) + (x / 32);
        int[] iArr = this.bits;
        iArr[offset] = iArr[offset] | (1 << (x & 31));
    }

    public void unset(int x, int y) {
        int offset = (this.rowSize * y) + (x / 32);
        int[] iArr = this.bits;
        iArr[offset] = iArr[offset] & ((1 << (x & 31)) ^ -1);
    }

    public void flip(int x, int y) {
        int offset = (this.rowSize * y) + (x / 32);
        int[] iArr = this.bits;
        iArr[offset] = iArr[offset] ^ (1 << (x & 31));
    }

    public void xor(BitMatrix mask) {
        if (this.width == mask.getWidth() && this.height == mask.getHeight() && this.rowSize == mask.getRowSize()) {
            BitArray rowArray = new BitArray((this.width / 32) + 1);
            for (int y = 0; y < this.height; y++) {
                int offset = y * this.rowSize;
                int[] row = mask.getRow(y, rowArray).getBitArray();
                for (int x = 0; x < this.rowSize; x++) {
                    int[] iArr = this.bits;
                    int i = offset + x;
                    iArr[i] = iArr[i] ^ row[x];
                }
            }
            return;
        }
        throw new IllegalArgumentException("input matrix dimensions do not match");
    }

    public void clear() {
        int max = this.bits.length;
        for (int i = 0; i < max; i++) {
            this.bits[i] = 0;
        }
    }

    public void setRegion(int left, int top, int width2, int height2) {
        if (top < 0 || left < 0) {
            throw new IllegalArgumentException("Left and top must be nonnegative");
        } else if (height2 <= 0 || width2 <= 0) {
            throw new IllegalArgumentException("Height and width must be at least 1");
        } else {
            int right = left + width2;
            int bottom = top + height2;
            if (bottom > this.height || right > this.width) {
                throw new IllegalArgumentException("The region must fit inside the matrix");
            }
            for (int y = top; y < bottom; y++) {
                int offset = y * this.rowSize;
                for (int x = left; x < right; x++) {
                    int[] iArr = this.bits;
                    int i = (x / 32) + offset;
                    iArr[i] = iArr[i] | (1 << (x & 31));
                }
            }
        }
    }

    public BitArray getRow(int y, BitArray row) {
        if (row == null || row.getSize() < this.width) {
            row = new BitArray(this.width);
        } else {
            row.clear();
        }
        int offset = y * this.rowSize;
        for (int x = 0; x < this.rowSize; x++) {
            row.setBulk(x << 5, this.bits[offset + x]);
        }
        return row;
    }

    public void setRow(int y, BitArray row) {
        System.arraycopy(row.getBitArray(), 0, this.bits, this.rowSize * y, this.rowSize);
    }

    public void rotate180() {
        int width2 = getWidth();
        int height2 = getHeight();
        BitArray topRow = new BitArray(width2);
        BitArray bottomRow = new BitArray(width2);
        for (int i = 0; i < (height2 + 1) / 2; i++) {
            topRow = getRow(i, topRow);
            bottomRow = getRow((height2 - 1) - i, bottomRow);
            topRow.reverse();
            bottomRow.reverse();
            setRow(i, bottomRow);
            setRow((height2 - 1) - i, topRow);
        }
    }

    public int[] getEnclosingRectangle() {
        int left = this.width;
        int top = this.height;
        int right = -1;
        int bottom = -1;
        for (int y = 0; y < this.height; y++) {
            for (int x32 = 0; x32 < this.rowSize; x32++) {
                int theBits = this.bits[(this.rowSize * y) + x32];
                if (theBits != 0) {
                    if (y < top) {
                        top = y;
                    }
                    if (y > bottom) {
                        bottom = y;
                    }
                    if ((x32 << 5) < left) {
                        int bit = 0;
                        while ((theBits << (31 - bit)) == 0) {
                            bit++;
                        }
                        if ((x32 << 5) + bit < left) {
                            left = (x32 << 5) + bit;
                        }
                    }
                    if ((x32 << 5) + 31 > right) {
                        int bit2 = 31;
                        while ((theBits >>> bit2) == 0) {
                            bit2--;
                        }
                        if ((x32 << 5) + bit2 > right) {
                            right = (x32 << 5) + bit2;
                        }
                    }
                }
            }
        }
        if (right < left || bottom < top) {
            return null;
        }
        return new int[]{left, top, (right - left) + 1, (bottom - top) + 1};
    }

    public int[] getTopLeftOnBit() {
        int bitsOffset = 0;
        while (bitsOffset < this.bits.length && this.bits[bitsOffset] == 0) {
            bitsOffset++;
        }
        if (bitsOffset == this.bits.length) {
            return null;
        }
        int y = bitsOffset / this.rowSize;
        int x = (bitsOffset % this.rowSize) << 5;
        int bit = 0;
        while ((this.bits[bitsOffset] << (31 - bit)) == 0) {
            bit++;
        }
        return new int[]{x + bit, y};
    }

    public int[] getBottomRightOnBit() {
        int bitsOffset = this.bits.length - 1;
        while (bitsOffset >= 0 && this.bits[bitsOffset] == 0) {
            bitsOffset--;
        }
        if (bitsOffset < 0) {
            return null;
        }
        int y = bitsOffset / this.rowSize;
        int x = (bitsOffset % this.rowSize) << 5;
        int bit = 31;
        while ((this.bits[bitsOffset] >>> bit) == 0) {
            bit--;
        }
        return new int[]{x + bit, y};
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getRowSize() {
        return this.rowSize;
    }

    public boolean equals(Object o) {
        if (!(o instanceof BitMatrix)) {
            return false;
        }
        BitMatrix other = (BitMatrix) o;
        if (this.width == other.width && this.height == other.height && this.rowSize == other.rowSize && Arrays.equals(this.bits, other.bits)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((((this.width * 31) + this.width) * 31) + this.height) * 31) + this.rowSize) * 31) + Arrays.hashCode(this.bits);
    }

    public String toString() {
        return toString("X ", "  ");
    }

    public String toString(String setString, String unsetString) {
        return buildToString(setString, unsetString, "\n");
    }

    @Deprecated
    public String toString(String setString, String unsetString, String lineSeparator) {
        return buildToString(setString, unsetString, lineSeparator);
    }

    private String buildToString(String setString, String unsetString, String lineSeparator) {
        String str;
        StringBuilder result = new StringBuilder(this.height * (this.width + 1));
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (get(x, y)) {
                    str = setString;
                } else {
                    str = unsetString;
                }
                result.append(str);
            }
            result.append(lineSeparator);
        }
        return result.toString();
    }

    public BitMatrix clone() {
        return new BitMatrix(this.width, this.height, this.rowSize, (int[]) this.bits.clone());
    }
}
