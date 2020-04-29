package dji.thirdparty.sanselan.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class ZLibUtils extends BinaryFileFunctions {
    public final byte[] inflate(byte[] bytes) throws IOException {
        return getStreamBytes(new InflaterInputStream(new ByteArrayInputStream(bytes)));
    }

    public final byte[] deflate(byte[] bytes) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeflaterOutputStream dos = new DeflaterOutputStream(baos);
        dos.write(bytes);
        dos.close();
        return baos.toByteArray();
    }
}
