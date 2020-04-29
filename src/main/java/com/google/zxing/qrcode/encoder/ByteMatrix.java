package com.google.zxing.qrcode.encoder;

import java.lang.reflect.Array;
import java.util.Arrays;

public final class ByteMatrix {
    private final byte[][] bytes;
    private final int height;
    private final int width;

    public ByteMatrix(int width2, int height2) {
        this.bytes = (byte[][]) Array.newInstance(Byte.TYPE, height2, width2);
        this.width = width2;
        this.height = height2;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public byte get(int x, int y) {
        return this.bytes[y][x];
    }

    public byte[][] getArray() {
        return this.bytes;
    }

    public void set(int x, int y, byte value) {
        this.bytes[y][x] = value;
    }

    public void set(int x, int y, int value) {
        this.bytes[y][x] = (byte) value;
    }

    public void set(int x, int y, boolean value) {
        this.bytes[y][x] = (byte) (value ? 1 : 0);
    }

    public void clear(byte value) {
        for (byte[] bArr : this.bytes) {
            Arrays.fill(bArr, value);
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder((this.width * 2 * this.height) + 2);
        for (int y = 0; y < this.height; y++) {
            byte[] bytesY = this.bytes[y];
            for (int x = 0; x < this.width; x++) {
                switch (bytesY[x]) {
                    case 0:
                        result.append(" 0");
                        break;
                    case 1:
                        result.append(" 1");
                        break;
                    default:
                        result.append("  ");
                        break;
                }
            }
            result.append(10);
        }
        return result.toString();
    }
}
