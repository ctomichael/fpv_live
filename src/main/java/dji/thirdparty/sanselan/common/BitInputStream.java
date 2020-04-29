package dji.thirdparty.sanselan.common;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.eac.CertificateBody;

public class BitInputStream extends InputStream implements BinaryConstants {
    private long bytes_read = 0;
    private int cache;
    private int cacheBitsRemaining = 0;
    private final InputStream is;

    public BitInputStream(InputStream is2) {
        this.is = is2;
    }

    public int read() throws IOException {
        if (this.cacheBitsRemaining <= 0) {
            return this.is.read();
        }
        throw new IOException("BitInputStream: incomplete bit read");
    }

    public final int readBits(int count) throws IOException {
        if (count < 8) {
            if (this.cacheBitsRemaining == 0) {
                this.cache = this.is.read();
                this.cacheBitsRemaining = 8;
                this.bytes_read++;
            }
            if (count > this.cacheBitsRemaining) {
                throw new IOException("BitInputStream: can't read bit fields across bytes");
            }
            this.cacheBitsRemaining -= count;
            int bits = this.cache >> this.cacheBitsRemaining;
            switch (count) {
                case 1:
                    return bits & 1;
                case 2:
                    return bits & 3;
                case 3:
                    return bits & 7;
                case 4:
                    return bits & 15;
                case 5:
                    return bits & 31;
                case 6:
                    return bits & 63;
                case 7:
                    return bits & CertificateBody.profileType;
            }
        }
        if (this.cacheBitsRemaining > 0) {
            throw new IOException("BitInputStream: incomplete bit read");
        } else if (count == 8) {
            this.bytes_read++;
            return this.is.read();
        } else if (count == 16) {
            this.bytes_read += 2;
            return (this.is.read() << 8) | (this.is.read() << 0);
        } else if (count == 24) {
            this.bytes_read += 3;
            return (this.is.read() << 16) | (this.is.read() << 8) | (this.is.read() << 0);
        } else if (count == 32) {
            this.bytes_read += 4;
            return (this.is.read() << 24) | (this.is.read() << 16) | (this.is.read() << 8) | (this.is.read() << 0);
        } else {
            throw new IOException("BitInputStream: unknown error");
        }
    }

    public void flushCache() {
        this.cacheBitsRemaining = 0;
    }

    public long getBytesRead() {
        return this.bytes_read;
    }
}
