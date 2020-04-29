package dji.thirdparty.ciphersql;

import android.database.CharArrayBuffer;

public abstract class AbstractWindowedCursor extends AbstractCursor {
    protected CursorWindow mWindow;

    public byte[] getBlob(int columnIndex) {
        checkPosition();
        synchronized (this.mUpdatedRows) {
            if (!isFieldUpdated(columnIndex)) {
                return this.mWindow.getBlob(this.mPos, columnIndex);
            }
            byte[] bArr = (byte[]) getUpdatedField(columnIndex);
            return bArr;
        }
    }

    public String getString(int columnIndex) {
        checkPosition();
        synchronized (this.mUpdatedRows) {
            if (!isFieldUpdated(columnIndex)) {
                return this.mWindow.getString(this.mPos, columnIndex);
            }
            String str = (String) getUpdatedField(columnIndex);
            return str;
        }
    }

    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        checkPosition();
        synchronized (this.mUpdatedRows) {
            if (isFieldUpdated(columnIndex)) {
                super.copyStringToBuffer(columnIndex, buffer);
            }
        }
        this.mWindow.copyStringToBuffer(this.mPos, columnIndex, buffer);
    }

    public short getShort(int columnIndex) {
        checkPosition();
        synchronized (this.mUpdatedRows) {
            if (!isFieldUpdated(columnIndex)) {
                return this.mWindow.getShort(this.mPos, columnIndex);
            }
            short shortValue = ((Number) getUpdatedField(columnIndex)).shortValue();
            return shortValue;
        }
    }

    public int getInt(int columnIndex) {
        checkPosition();
        synchronized (this.mUpdatedRows) {
            if (!isFieldUpdated(columnIndex)) {
                return this.mWindow.getInt(this.mPos, columnIndex);
            }
            int intValue = ((Number) getUpdatedField(columnIndex)).intValue();
            return intValue;
        }
    }

    public long getLong(int columnIndex) {
        checkPosition();
        synchronized (this.mUpdatedRows) {
            if (!isFieldUpdated(columnIndex)) {
                return this.mWindow.getLong(this.mPos, columnIndex);
            }
            long longValue = ((Number) getUpdatedField(columnIndex)).longValue();
            return longValue;
        }
    }

    public float getFloat(int columnIndex) {
        checkPosition();
        synchronized (this.mUpdatedRows) {
            if (!isFieldUpdated(columnIndex)) {
                return this.mWindow.getFloat(this.mPos, columnIndex);
            }
            float floatValue = ((Number) getUpdatedField(columnIndex)).floatValue();
            return floatValue;
        }
    }

    public double getDouble(int columnIndex) {
        checkPosition();
        synchronized (this.mUpdatedRows) {
            if (!isFieldUpdated(columnIndex)) {
                return this.mWindow.getDouble(this.mPos, columnIndex);
            }
            double doubleValue = ((Number) getUpdatedField(columnIndex)).doubleValue();
            return doubleValue;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isNull(int r3) {
        /*
            r2 = this;
            r2.checkPosition()
            java.util.HashMap r1 = r2.mUpdatedRows
            monitor-enter(r1)
            boolean r0 = r2.isFieldUpdated(r3)     // Catch:{ all -> 0x0021 }
            if (r0 == 0) goto L_0x0017
            java.lang.Object r0 = r2.getUpdatedField(r3)     // Catch:{ all -> 0x0021 }
            if (r0 != 0) goto L_0x0015
            r0 = 1
        L_0x0013:
            monitor-exit(r1)     // Catch:{ all -> 0x0021 }
        L_0x0014:
            return r0
        L_0x0015:
            r0 = 0
            goto L_0x0013
        L_0x0017:
            monitor-exit(r1)     // Catch:{ all -> 0x0021 }
            dji.thirdparty.ciphersql.CursorWindow r0 = r2.mWindow
            int r1 = r2.mPos
            boolean r0 = r0.isNull(r1, r3)
            goto L_0x0014
        L_0x0021:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0021 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.ciphersql.AbstractWindowedCursor.isNull(int):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isBlob(int r4) {
        /*
            r3 = this;
            r3.checkPosition()
            java.util.HashMap r2 = r3.mUpdatedRows
            monitor-enter(r2)
            boolean r1 = r3.isFieldUpdated(r4)     // Catch:{ all -> 0x0025 }
            if (r1 == 0) goto L_0x001b
            java.lang.Object r0 = r3.getUpdatedField(r4)     // Catch:{ all -> 0x0025 }
            if (r0 == 0) goto L_0x0016
            boolean r1 = r0 instanceof byte[]     // Catch:{ all -> 0x0025 }
            if (r1 == 0) goto L_0x0019
        L_0x0016:
            r1 = 1
        L_0x0017:
            monitor-exit(r2)     // Catch:{ all -> 0x0025 }
        L_0x0018:
            return r1
        L_0x0019:
            r1 = 0
            goto L_0x0017
        L_0x001b:
            monitor-exit(r2)     // Catch:{ all -> 0x0025 }
            dji.thirdparty.ciphersql.CursorWindow r1 = r3.mWindow
            int r2 = r3.mPos
            boolean r1 = r1.isBlob(r2, r4)
            goto L_0x0018
        L_0x0025:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0025 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.ciphersql.AbstractWindowedCursor.isBlob(int):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isString(int r4) {
        /*
            r3 = this;
            r3.checkPosition()
            java.util.HashMap r2 = r3.mUpdatedRows
            monitor-enter(r2)
            boolean r1 = r3.isFieldUpdated(r4)     // Catch:{ all -> 0x0025 }
            if (r1 == 0) goto L_0x001b
            java.lang.Object r0 = r3.getUpdatedField(r4)     // Catch:{ all -> 0x0025 }
            if (r0 == 0) goto L_0x0016
            boolean r1 = r0 instanceof java.lang.String     // Catch:{ all -> 0x0025 }
            if (r1 == 0) goto L_0x0019
        L_0x0016:
            r1 = 1
        L_0x0017:
            monitor-exit(r2)     // Catch:{ all -> 0x0025 }
        L_0x0018:
            return r1
        L_0x0019:
            r1 = 0
            goto L_0x0017
        L_0x001b:
            monitor-exit(r2)     // Catch:{ all -> 0x0025 }
            dji.thirdparty.ciphersql.CursorWindow r1 = r3.mWindow
            int r2 = r3.mPos
            boolean r1 = r1.isString(r2, r4)
            goto L_0x0018
        L_0x0025:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0025 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.ciphersql.AbstractWindowedCursor.isString(int):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isLong(int r4) {
        /*
            r3 = this;
            r3.checkPosition()
            java.util.HashMap r2 = r3.mUpdatedRows
            monitor-enter(r2)
            boolean r1 = r3.isFieldUpdated(r4)     // Catch:{ all -> 0x0029 }
            if (r1 == 0) goto L_0x001f
            java.lang.Object r0 = r3.getUpdatedField(r4)     // Catch:{ all -> 0x0029 }
            if (r0 == 0) goto L_0x001d
            boolean r1 = r0 instanceof java.lang.Integer     // Catch:{ all -> 0x0029 }
            if (r1 != 0) goto L_0x001a
            boolean r1 = r0 instanceof java.lang.Long     // Catch:{ all -> 0x0029 }
            if (r1 == 0) goto L_0x001d
        L_0x001a:
            r1 = 1
        L_0x001b:
            monitor-exit(r2)     // Catch:{ all -> 0x0029 }
        L_0x001c:
            return r1
        L_0x001d:
            r1 = 0
            goto L_0x001b
        L_0x001f:
            monitor-exit(r2)     // Catch:{ all -> 0x0029 }
            dji.thirdparty.ciphersql.CursorWindow r1 = r3.mWindow
            int r2 = r3.mPos
            boolean r1 = r1.isLong(r2, r4)
            goto L_0x001c
        L_0x0029:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0029 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.ciphersql.AbstractWindowedCursor.isLong(int):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isFloat(int r4) {
        /*
            r3 = this;
            r3.checkPosition()
            java.util.HashMap r2 = r3.mUpdatedRows
            monitor-enter(r2)
            boolean r1 = r3.isFieldUpdated(r4)     // Catch:{ all -> 0x0029 }
            if (r1 == 0) goto L_0x001f
            java.lang.Object r0 = r3.getUpdatedField(r4)     // Catch:{ all -> 0x0029 }
            if (r0 == 0) goto L_0x001d
            boolean r1 = r0 instanceof java.lang.Float     // Catch:{ all -> 0x0029 }
            if (r1 != 0) goto L_0x001a
            boolean r1 = r0 instanceof java.lang.Double     // Catch:{ all -> 0x0029 }
            if (r1 == 0) goto L_0x001d
        L_0x001a:
            r1 = 1
        L_0x001b:
            monitor-exit(r2)     // Catch:{ all -> 0x0029 }
        L_0x001c:
            return r1
        L_0x001d:
            r1 = 0
            goto L_0x001b
        L_0x001f:
            monitor-exit(r2)     // Catch:{ all -> 0x0029 }
            dji.thirdparty.ciphersql.CursorWindow r1 = r3.mWindow
            int r2 = r3.mPos
            boolean r1 = r1.isFloat(r2, r4)
            goto L_0x001c
        L_0x0029:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0029 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.ciphersql.AbstractWindowedCursor.isFloat(int):boolean");
    }

    public int getType(int columnIndex) {
        checkPosition();
        return this.mWindow.getType(this.mPos, columnIndex);
    }

    /* access modifiers changed from: protected */
    public void checkPosition() {
        super.checkPosition();
        if (this.mWindow == null) {
            throw new StaleDataException("Access closed cursor");
        }
    }

    public CursorWindow getWindow() {
        return this.mWindow;
    }

    public void setWindow(CursorWindow window) {
        if (this.mWindow != null) {
            this.mWindow.close();
        }
        this.mWindow = window;
    }

    public boolean hasWindow() {
        return this.mWindow != null;
    }
}
