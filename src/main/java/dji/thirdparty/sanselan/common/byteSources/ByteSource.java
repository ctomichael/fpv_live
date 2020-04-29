package dji.thirdparty.sanselan.common.byteSources;

import dji.thirdparty.sanselan.common.BinaryFileFunctions;
import java.io.IOException;
import java.io.InputStream;

public abstract class ByteSource extends BinaryFileFunctions {
    protected final String filename;

    public abstract byte[] getAll() throws IOException;

    public abstract byte[] getBlock(int i, int i2) throws IOException;

    public abstract String getDescription();

    public abstract InputStream getInputStream() throws IOException;

    public abstract long getLength() throws IOException;

    public ByteSource(String filename2) {
        this.filename = filename2;
    }

    public final InputStream getInputStream(int start) throws IOException {
        InputStream is = getInputStream();
        skipBytes(is, start);
        return is;
    }

    public final String getFilename() {
        return this.filename;
    }
}
