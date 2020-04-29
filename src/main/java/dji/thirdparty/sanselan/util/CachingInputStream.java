package dji.thirdparty.sanselan.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachingInputStream extends InputStream {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final InputStream is;

    public CachingInputStream(InputStream is2) {
        this.is = is2;
    }

    public byte[] getCache() {
        return this.baos.toByteArray();
    }

    public int read() throws IOException {
        int result = this.is.read();
        this.baos.write(result);
        return result;
    }

    public int available() throws IOException {
        return this.is.available();
    }

    public void close() throws IOException {
        this.is.close();
    }
}
