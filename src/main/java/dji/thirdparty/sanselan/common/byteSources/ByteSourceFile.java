package dji.thirdparty.sanselan.common.byteSources;

import dji.thirdparty.sanselan.util.Debug;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class ByteSourceFile extends ByteSource {
    private final File file;

    public ByteSourceFile(File file2) {
        super(file2.getName());
        this.file = file2;
    }

    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(new FileInputStream(this.file));
    }

    public byte[] getBlock(int start, int length) throws IOException {
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(this.file, "r");
            try {
                byte[] rAFBytes = getRAFBytes(raf, (long) start, length, "Could not read value from file");
                try {
                    raf.close();
                } catch (Exception e) {
                    Debug.debug((Throwable) e);
                }
                return rAFBytes;
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
            raf = null;
            try {
                raf.close();
            } catch (Exception e2) {
                Debug.debug((Throwable) e2);
            }
            throw th;
        }
    }

    public long getLength() {
        return this.file.length();
    }

    public byte[] getAll() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            InputStream is2 = new FileInputStream(this.file);
            try {
                is = new BufferedInputStream(is2);
                byte[] buffer = new byte[1024];
                while (true) {
                    int read = is.read(buffer);
                    if (read <= 0) {
                        break;
                    }
                    baos.write(buffer, 0, read);
                }
                byte[] byteArray = baos.toByteArray();
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
                return byteArray;
            } catch (Throwable th) {
                th = th;
                is = is2;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e2) {
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public String getDescription() {
        return "File: '" + this.file.getAbsolutePath() + "'";
    }
}
