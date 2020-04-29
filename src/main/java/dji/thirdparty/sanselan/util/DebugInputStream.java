package dji.thirdparty.sanselan.util;

import java.io.IOException;
import java.io.InputStream;

public class DebugInputStream extends InputStream {
    private long bytes_read = 0;
    private final InputStream is;

    public DebugInputStream(InputStream is2) {
        this.is = is2;
    }

    public int read() throws IOException {
        int result = this.is.read();
        this.bytes_read++;
        return result;
    }

    public long skip(long n) throws IOException {
        long result = this.is.skip(n);
        this.bytes_read += n;
        return result;
    }

    public int available() throws IOException {
        return this.is.available();
    }

    public void close() throws IOException {
        this.is.close();
    }

    public long getBytesRead() {
        return this.bytes_read;
    }
}
