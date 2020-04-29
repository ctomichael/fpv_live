package dji.thirdparty.sanselan.common;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStreamFlexible extends InputStream implements BinaryConstants {
    private long bytesRead = 0;
    private int cache;
    private int cacheBitsRemaining = 0;
    private final InputStream is;

    public BitInputStreamFlexible(InputStream is2) {
        this.is = is2;
    }

    public int read() throws IOException {
        if (this.cacheBitsRemaining <= 0) {
            return this.is.read();
        }
        throw new IOException("BitInputStream: incomplete bit read");
    }

    public final int readBits(int count) throws IOException {
        if (count <= 32) {
            int result = 0;
            if (this.cacheBitsRemaining > 0) {
                if (count >= this.cacheBitsRemaining) {
                    result = ((1 << this.cacheBitsRemaining) - 1) & this.cache;
                    count -= this.cacheBitsRemaining;
                    this.cacheBitsRemaining = 0;
                } else {
                    this.cacheBitsRemaining -= count;
                    result = ((1 << count) - 1) & (this.cache >> this.cacheBitsRemaining);
                    count = 0;
                }
            }
            while (count >= 8) {
                this.cache = this.is.read();
                if (this.cache < 0) {
                    throw new IOException("couldn't read bits");
                }
                System.out.println("cache 1: " + this.cache + " (" + Integer.toHexString(this.cache) + ", " + Integer.toBinaryString(this.cache) + ")");
                this.bytesRead++;
                result = (result << 8) | (this.cache & 255);
                count -= 8;
            }
            if (count <= 0) {
                return result;
            }
            this.cache = this.is.read();
            if (this.cache < 0) {
                throw new IOException("couldn't read bits");
            }
            System.out.println("cache 2: " + this.cache + " (" + Integer.toHexString(this.cache) + ", " + Integer.toBinaryString(this.cache) + ")");
            this.bytesRead++;
            this.cacheBitsRemaining = 8 - count;
            return (result << count) | (((1 << count) - 1) & (this.cache >> this.cacheBitsRemaining));
        }
        throw new IOException("BitInputStream: unknown error");
    }

    public void flushCache() {
        this.cacheBitsRemaining = 0;
    }

    public long getBytesRead() {
        return this.bytesRead;
    }
}
