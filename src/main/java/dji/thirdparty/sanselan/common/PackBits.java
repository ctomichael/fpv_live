package dji.thirdparty.sanselan.common;

import dji.thirdparty.sanselan.ImageReadException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PackBits {
    public byte[] decompress(byte[] bytes, int expected) throws ImageReadException, IOException {
        int total = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = 0;
        while (total < expected) {
            if (i >= bytes.length) {
                throw new ImageReadException("Tiff: Unpack bits source exhausted: " + i + ", done + " + total + ", expected + " + expected);
            }
            int i2 = i + 1;
            byte b = bytes[i];
            if (b >= 0 && b <= Byte.MAX_VALUE) {
                int count = b + 1;
                total += count;
                int j = 0;
                int i3 = i2;
                while (j < count) {
                    baos.write(bytes[i3]);
                    j++;
                    i3++;
                }
                i2 = i3;
            } else if (b >= -127 && b <= -1) {
                int i4 = i2 + 1;
                byte b2 = bytes[i2];
                int count2 = (-b) + 1;
                total += count2;
                for (int j2 = 0; j2 < count2; j2++) {
                    baos.write(b2);
                }
                i2 = i4;
            } else if (b == Byte.MIN_VALUE) {
                throw new ImageReadException("Packbits: " + ((int) b));
            }
            i = i2;
        }
        return baos.toByteArray();
    }

    private int findNextDuplicate(byte[] bytes, int start) {
        if (start >= bytes.length) {
            return -1;
        }
        byte prev = bytes[start];
        for (int i = start + 1; i < bytes.length; i++) {
            byte b = bytes[i];
            if (b == prev) {
                return i - 1;
            }
            prev = b;
        }
        return -1;
    }

    private int findRunLength(byte[] bytes, int start) {
        byte b = bytes[start];
        int i = start + 1;
        while (i < bytes.length && bytes[i] == b) {
            i++;
        }
        return i - start;
    }

    public byte[] compress(byte[] bytes) throws IOException {
        int runlen;
        int nextptr;
        int nextdup;
        MyByteArrayOutputStream baos = new MyByteArrayOutputStream(bytes.length * 2);
        int ptr = 0;
        int count = 0;
        while (ptr < bytes.length) {
            count++;
            int dup = findNextDuplicate(bytes, ptr);
            if (dup == ptr) {
                int actual_len = Math.min(findRunLength(bytes, dup), 128);
                baos.write(-(actual_len - 1));
                baos.write(bytes[ptr]);
                ptr += actual_len;
            } else {
                int len = dup - ptr;
                if (dup > 0 && (runlen = findRunLength(bytes, dup)) < 3 && (nextdup = findNextDuplicate(bytes, (nextptr = ptr + len + runlen))) != nextptr) {
                    dup = nextdup;
                    len = dup - ptr;
                }
                if (dup < 0) {
                    len = bytes.length - ptr;
                }
                int actual_len2 = Math.min(len, 128);
                baos.write(actual_len2 - 1);
                for (int i = 0; i < actual_len2; i++) {
                    baos.write(bytes[ptr]);
                    ptr++;
                }
            }
        }
        return baos.toByteArray();
    }
}
