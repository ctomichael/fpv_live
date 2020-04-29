package dji.thirdparty.sanselan.util;

import java.io.IOException;
import java.io.OutputStream;

public class DebugOutputStream extends OutputStream {
    private long count = 0;
    private final OutputStream os;

    public DebugOutputStream(OutputStream os2) {
        this.os = os2;
    }

    public void write(int b) throws IOException {
        this.os.write(b);
        this.count++;
    }

    public void write(byte[] b) throws IOException {
        this.os.write(b);
        this.count += (long) b.length;
    }

    public void write(byte[] b, int off, int len) throws IOException {
        this.os.write(b, off, len);
        this.count += (long) len;
    }

    public void flush() throws IOException {
        this.os.flush();
    }

    public void close() throws IOException {
        this.os.close();
    }

    public long count() {
        return this.count;
    }
}
