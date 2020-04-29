package dji.thirdparty.sanselan.common.mylzw;

import dji.thirdparty.sanselan.common.BinaryConstants;
import java.io.IOException;
import java.io.InputStream;

public class MyBitInputStream extends InputStream implements BinaryConstants {
    private int bitCache = 0;
    private int bitsInCache = 0;
    private final int byteOrder;
    private long bytesRead = 0;
    private final InputStream is;
    private boolean tiffLZWMode = false;

    public MyBitInputStream(InputStream is2, int byteOrder2) {
        this.byteOrder = byteOrder2;
        this.is = is2;
    }

    public int read() throws IOException {
        return readBits(8);
    }

    public void setTiffLZWMode() {
        this.tiffLZWMode = true;
    }

    public int readBits(int SampleBits) throws IOException {
        int sample;
        while (this.bitsInCache < SampleBits) {
            int next = this.is.read();
            if (next >= 0) {
                int newByte = next & 255;
                if (this.byteOrder == 77) {
                    this.bitCache = (this.bitCache << 8) | newByte;
                } else if (this.byteOrder == 73) {
                    this.bitCache = (newByte << this.bitsInCache) | this.bitCache;
                } else {
                    throw new IOException("Unknown byte order: " + this.byteOrder);
                }
                this.bytesRead++;
                this.bitsInCache += 8;
            } else if (this.tiffLZWMode) {
                return 257;
            } else {
                return -1;
            }
        }
        int sampleMask = (1 << SampleBits) - 1;
        if (this.byteOrder == 77) {
            sample = sampleMask & (this.bitCache >> (this.bitsInCache - SampleBits));
        } else if (this.byteOrder == 73) {
            sample = sampleMask & this.bitCache;
            this.bitCache >>= SampleBits;
        } else {
            throw new IOException("Unknown byte order: " + this.byteOrder);
        }
        int i = sample;
        this.bitsInCache -= SampleBits;
        this.bitCache &= (1 << this.bitsInCache) - 1;
        return i;
    }

    public void flushCache() {
        this.bitsInCache = 0;
        this.bitCache = 0;
    }

    public long getBytesRead() {
        return this.bytesRead;
    }
}
