package dji.thirdparty.sanselan.common.byteSources;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteSourceInputStream extends ByteSource {
    private static final int BLOCK_SIZE = 1024;
    private CacheBlock cacheHead = null;
    private final InputStream is;
    private Long length = null;
    private byte[] readBuffer = null;

    public ByteSourceInputStream(InputStream is2, String filename) {
        super(filename);
        this.is = new BufferedInputStream(is2);
    }

    private class CacheBlock {
        public final byte[] bytes;
        private CacheBlock next = null;
        private boolean triedNext = false;

        public CacheBlock(byte[] bytes2) {
            this.bytes = bytes2;
        }

        public CacheBlock getNext() throws IOException {
            if (this.next != null) {
                return this.next;
            }
            if (this.triedNext) {
                return null;
            }
            this.triedNext = true;
            this.next = ByteSourceInputStream.this.readBlock();
            return this.next;
        }
    }

    /* access modifiers changed from: private */
    public CacheBlock readBlock() throws IOException {
        if (this.readBuffer == null) {
            this.readBuffer = new byte[1024];
        }
        int read = this.is.read(this.readBuffer);
        if (read < 1) {
            return null;
        }
        if (read < 1024) {
            byte[] result = new byte[read];
            System.arraycopy(this.readBuffer, 0, result, 0, read);
            return new CacheBlock(result);
        }
        byte[] result2 = this.readBuffer;
        this.readBuffer = null;
        return new CacheBlock(result2);
    }

    /* access modifiers changed from: private */
    public CacheBlock getFirstBlock() throws IOException {
        if (this.cacheHead == null) {
            this.cacheHead = readBlock();
        }
        return this.cacheHead;
    }

    private class CacheReadingInputStream extends InputStream {
        private CacheBlock block;
        private int blockIndex;
        private boolean readFirst;

        private CacheReadingInputStream() {
            this.block = null;
            this.readFirst = false;
            this.blockIndex = 0;
        }

        public int read() throws IOException {
            if (this.block == null) {
                if (this.readFirst) {
                    return -1;
                }
                this.block = ByteSourceInputStream.this.getFirstBlock();
                this.readFirst = true;
            }
            if (this.block != null && this.blockIndex >= this.block.bytes.length) {
                this.block = this.block.getNext();
                this.blockIndex = 0;
            }
            if (this.block == null || this.blockIndex >= this.block.bytes.length) {
                return -1;
            }
            byte[] bArr = this.block.bytes;
            int i = this.blockIndex;
            this.blockIndex = i + 1;
            return bArr[i] & 255;
        }

        public int read(byte[] b, int off, int len) throws IOException {
            if (b == null) {
                throw new NullPointerException();
            } else if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            } else {
                if (this.block == null) {
                    if (this.readFirst) {
                        return -1;
                    }
                    this.block = ByteSourceInputStream.this.getFirstBlock();
                    this.readFirst = true;
                }
                if (this.block != null && this.blockIndex >= this.block.bytes.length) {
                    this.block = this.block.getNext();
                    this.blockIndex = 0;
                }
                if (this.block == null) {
                    return -1;
                }
                if (this.blockIndex >= this.block.bytes.length) {
                    return -1;
                }
                int readSize = Math.min(len, this.block.bytes.length - this.blockIndex);
                System.arraycopy(this.block.bytes, this.blockIndex, b, off, readSize);
                this.blockIndex += readSize;
                return readSize;
            }
        }
    }

    public InputStream getInputStream() throws IOException {
        return new CacheReadingInputStream();
    }

    public byte[] getBlock(int start, int length2) throws IOException {
        InputStream is2 = getInputStream();
        is2.skip((long) start);
        byte[] bytes = new byte[length2];
        int total = 0;
        do {
            int read = is2.read(bytes, total, bytes.length - total);
            if (read < 1) {
                throw new IOException("Could not read block.");
            }
            total += read;
        } while (total < length2);
        return bytes;
    }

    public long getLength() throws IOException {
        if (this.length != null) {
            return this.length.longValue();
        }
        InputStream is2 = getInputStream();
        long result = 0;
        while (true) {
            long skipped = is2.skip(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
            if (skipped > 0) {
                result += skipped;
            } else {
                this.length = new Long(result);
                return result;
            }
        }
    }

    public byte[] getAll() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (CacheBlock block = getFirstBlock(); block != null; block = block.getNext()) {
            baos.write(block.bytes);
        }
        return baos.toByteArray();
    }

    public String getDescription() {
        return "Inputstream: '" + this.filename + "'";
    }
}
