package dji.midware.data.packages.P3;

import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;

@EXClassNullAway
public class PackBufferObject {
    private static ArrayList<PackBufferObject> list = new ArrayList<>();
    private byte[] buffer;
    private boolean isRepeat;
    private volatile boolean isUsing = true;

    private PackBufferObject(int length) {
        int mLength;
        if (length < 100) {
            mLength = 100;
        } else {
            mLength = length;
        }
        this.buffer = new byte[mLength];
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public void willRepeat(boolean isRepeat2) {
        this.isRepeat = isRepeat2;
    }

    public void noUsed() {
        if (!this.isRepeat) {
            this.isUsing = false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r0 = new dji.midware.data.packages.P3.PackBufferObject(r4);
        dji.midware.data.packages.P3.PackBufferObject.list.add(r0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static synchronized dji.midware.data.packages.P3.PackBufferObject getPackBufferObject(int r4) {
        /*
            java.lang.Class<dji.midware.data.packages.P3.PackBufferObject> r2 = dji.midware.data.packages.P3.PackBufferObject.class
            monitor-enter(r2)
            java.util.ArrayList<dji.midware.data.packages.P3.PackBufferObject> r1 = dji.midware.data.packages.P3.PackBufferObject.list     // Catch:{ all -> 0x0030 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x0030 }
        L_0x0009:
            boolean r3 = r1.hasNext()     // Catch:{ all -> 0x0030 }
            if (r3 == 0) goto L_0x0025
            java.lang.Object r0 = r1.next()     // Catch:{ all -> 0x0030 }
            dji.midware.data.packages.P3.PackBufferObject r0 = (dji.midware.data.packages.P3.PackBufferObject) r0     // Catch:{ all -> 0x0030 }
            boolean r3 = r0.isUsing     // Catch:{ all -> 0x0030 }
            if (r3 != 0) goto L_0x0009
            byte[] r3 = r0.getBuffer()     // Catch:{ all -> 0x0030 }
            int r3 = r3.length     // Catch:{ all -> 0x0030 }
            if (r3 < r4) goto L_0x0009
            r1 = 1
            r0.isUsing = r1     // Catch:{ all -> 0x0030 }
        L_0x0023:
            monitor-exit(r2)
            return r0
        L_0x0025:
            dji.midware.data.packages.P3.PackBufferObject r0 = new dji.midware.data.packages.P3.PackBufferObject     // Catch:{ all -> 0x0030 }
            r0.<init>(r4)     // Catch:{ all -> 0x0030 }
            java.util.ArrayList<dji.midware.data.packages.P3.PackBufferObject> r1 = dji.midware.data.packages.P3.PackBufferObject.list     // Catch:{ all -> 0x0030 }
            r1.add(r0)     // Catch:{ all -> 0x0030 }
            goto L_0x0023
        L_0x0030:
            r1 = move-exception
            monitor-exit(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.data.packages.P3.PackBufferObject.getPackBufferObject(int):dji.midware.data.packages.P3.PackBufferObject");
    }
}
