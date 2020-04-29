package org.xeustechnologies.jtar;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TarInputStream extends FilterInputStream {
    private static final int SKIP_BUFFER_SIZE = 2048;
    private long bytesRead = 0;
    private TarEntry currentEntry;
    private long currentFileSize = 0;
    private boolean defaultSkip = false;

    public TarInputStream(InputStream in2) {
        super(in2);
    }

    public boolean markSupported() {
        return false;
    }

    public synchronized void mark(int readlimit) {
    }

    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    public int read() throws IOException {
        byte[] buf = new byte[1];
        int res = read(buf, 0, 1);
        if (res != -1) {
            return buf[0];
        }
        return res;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (this.currentEntry != null) {
            if (this.currentFileSize == this.currentEntry.getSize()) {
                return -1;
            }
            if (this.currentEntry.getSize() - this.currentFileSize < ((long) len)) {
                len = (int) (this.currentEntry.getSize() - this.currentFileSize);
            }
        }
        int br = super.read(b, off, len);
        if (br == -1) {
            return br;
        }
        if (this.currentEntry != null) {
            this.currentFileSize += (long) br;
        }
        this.bytesRead += (long) br;
        return br;
    }

    public TarEntry getNextEntry() throws IOException {
        closeCurrentEntry();
        byte[] header = new byte[512];
        byte[] theader = new byte[512];
        int tr = 0;
        while (tr < 512) {
            int res = read(theader, 0, 512 - tr);
            if (res < 0) {
                break;
            }
            System.arraycopy(theader, 0, header, tr, res);
            tr += res;
        }
        boolean eof = true;
        byte[] arr$ = header;
        int len$ = arr$.length;
        int i$ = 0;
        while (true) {
            if (i$ >= len$) {
                break;
            } else if (arr$[i$] != 0) {
                eof = false;
                break;
            } else {
                i$++;
            }
        }
        if (!eof) {
            this.bytesRead += (long) header.length;
            this.currentEntry = new TarEntry(header);
        }
        return this.currentEntry;
    }

    /* access modifiers changed from: protected */
    public void closeCurrentEntry() throws IOException {
        if (this.currentEntry != null) {
            if (this.currentEntry.getSize() > this.currentFileSize) {
                long bs = 0;
                while (bs < this.currentEntry.getSize() - this.currentFileSize) {
                    long res = skip((this.currentEntry.getSize() - this.currentFileSize) - bs);
                    if (res != 0 || this.currentEntry.getSize() - this.currentFileSize <= 0) {
                        bs += res;
                    } else {
                        throw new IOException("Possible tar file corruption");
                    }
                }
            }
            this.currentEntry = null;
            this.currentFileSize = 0;
            skipPad();
        }
    }

    /* access modifiers changed from: protected */
    public void skipPad() throws IOException {
        int extra;
        if (this.bytesRead > 0 && (extra = (int) (this.bytesRead % 512)) > 0) {
            long bs = 0;
            while (bs < ((long) (512 - extra))) {
                bs += skip(((long) (512 - extra)) - bs);
            }
        }
    }

    public long skip(long n) throws IOException {
        if (this.defaultSkip) {
            return super.skip(n);
        }
        if (n <= 0) {
            return 0;
        }
        long left = n;
        byte[] sBuff = new byte[2048];
        while (left > 0) {
            int res = read(sBuff, 0, (int) (left < PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH ? left : 2048));
            if (res < 0) {
                break;
            }
            left -= (long) res;
        }
        return n - left;
    }

    public boolean isDefaultSkip() {
        return this.defaultSkip;
    }

    public void setDefaultSkip(boolean defaultSkip2) {
        this.defaultSkip = defaultSkip2;
    }
}
