package dji.thirdparty.sanselan.common.mylzw;

import dji.thirdparty.sanselan.common.BinaryConstants;
import java.io.IOException;
import java.io.OutputStream;

public class MyBitOutputStream extends OutputStream implements BinaryConstants {
    private int bitCache = 0;
    private int bitsInCache = 0;
    private final int byteOrder;
    private int bytesWritten = 0;
    private final OutputStream os;

    public MyBitOutputStream(OutputStream os2, int byteOrder2) {
        this.byteOrder = byteOrder2;
        this.os = os2;
    }

    public void write(int value) throws IOException {
        writeBits(value, 8);
    }

    public void writeBits(int value, int SampleBits) throws IOException {
        int value2 = value & ((1 << SampleBits) - 1);
        if (this.byteOrder == 77) {
            this.bitCache = (this.bitCache << SampleBits) | value2;
        } else if (this.byteOrder == 73) {
            this.bitCache |= value2 << this.bitsInCache;
        } else {
            throw new IOException("Unknown byte order: " + this.byteOrder);
        }
        this.bitsInCache += SampleBits;
        while (this.bitsInCache >= 8) {
            if (this.byteOrder == 77) {
                actualWrite((this.bitCache >> (this.bitsInCache - 8)) & 255);
                this.bitsInCache -= 8;
            } else if (this.byteOrder == 73) {
                actualWrite(this.bitCache & 255);
                this.bitCache >>= 8;
                this.bitsInCache -= 8;
            }
            this.bitCache &= (1 << this.bitsInCache) - 1;
        }
    }

    private void actualWrite(int value) throws IOException {
        this.os.write(value);
        this.bytesWritten++;
    }

    public void flushCache() throws IOException {
        if (this.bitsInCache > 0) {
            int b = ((1 << this.bitsInCache) - 1) & this.bitCache;
            if (this.byteOrder == 77) {
                this.os.write(b << (8 - this.bitsInCache));
            } else if (this.byteOrder == 73) {
                this.os.write(b);
            }
        }
        this.bitsInCache = 0;
        this.bitCache = 0;
    }

    public int getBytesWritten() {
        return (this.bitsInCache > 0 ? 1 : 0) + this.bytesWritten;
    }
}
