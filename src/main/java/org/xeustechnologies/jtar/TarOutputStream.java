package org.xeustechnologies.jtar;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarOutputStream extends FilterOutputStream {
    private long bytesWritten = 0;
    private TarEntry currentEntry;
    private long currentFileSize = 0;

    public TarOutputStream(OutputStream out) {
        super(out);
    }

    public void close() throws IOException {
        closeCurrentEntry();
        write(new byte[1024]);
        super.close();
    }

    public void write(int b) throws IOException {
        super.write(b);
        this.bytesWritten++;
        if (this.currentEntry != null) {
            this.currentFileSize++;
        }
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (this.currentEntry == null || this.currentEntry.isDirectory() || this.currentEntry.getSize() >= this.currentFileSize + ((long) len)) {
            super.write(b, off, len);
            return;
        }
        throw new IOException("The current entry[" + this.currentEntry.getName() + "] size[" + this.currentEntry.getSize() + "] is smaller than the bytes[" + (this.currentFileSize + ((long) len)) + "] being written.");
    }

    public void putNextEntry(TarEntry entry) throws IOException {
        closeCurrentEntry();
        byte[] header = new byte[512];
        entry.writeEntryHeader(header);
        write(header);
        this.currentEntry = entry;
    }

    /* access modifiers changed from: protected */
    public void closeCurrentEntry() throws IOException {
        if (this.currentEntry == null) {
            return;
        }
        if (this.currentEntry.getSize() > this.currentFileSize) {
            throw new IOException("The current entry[" + this.currentEntry.getName() + "] of size[" + this.currentEntry.getSize() + "] has not been fully written.");
        }
        this.currentEntry = null;
        this.currentFileSize = 0;
        pad();
    }

    /* access modifiers changed from: protected */
    public void pad() throws IOException {
        int extra;
        if (this.bytesWritten > 0 && (extra = (int) (this.bytesWritten % 512)) > 0) {
            write(new byte[(512 - extra)]);
        }
    }
}
