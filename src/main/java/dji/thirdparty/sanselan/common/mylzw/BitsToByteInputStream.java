package dji.thirdparty.sanselan.common.mylzw;

import java.io.IOException;
import java.io.InputStream;

public class BitsToByteInputStream extends InputStream {
    private final int desiredDepth;
    private final MyBitInputStream is;

    public BitsToByteInputStream(MyBitInputStream is2, int desiredDepth2) {
        this.is = is2;
        this.desiredDepth = desiredDepth2;
    }

    public int read() throws IOException {
        return readBits(8);
    }

    public int readBits(int bitCount) throws IOException {
        int i = this.is.readBits(bitCount);
        if (bitCount < this.desiredDepth) {
            return i << (this.desiredDepth - bitCount);
        }
        if (bitCount > this.desiredDepth) {
            return i >> (bitCount - this.desiredDepth);
        }
        return i;
    }

    public int[] readBitsArray(int sampleBits, int length) throws IOException {
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = readBits(sampleBits);
        }
        return result;
    }
}
