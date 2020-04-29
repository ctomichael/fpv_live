package dji.thirdparty.sanselan.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CachingOutputStream extends OutputStream {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final OutputStream os;

    public CachingOutputStream(OutputStream os2) {
        this.os = os2;
    }

    public void write(int b) throws IOException {
        this.os.write(b);
        this.baos.write(b);
    }

    public byte[] getCache() {
        return this.baos.toByteArray();
    }

    public void close() throws IOException {
        this.os.close();
    }

    public void flush() throws IOException {
        this.os.flush();
    }
}
