package com.google.zxing.pdf417.decoder;

class DetectionResultColumn {
    private static final int MAX_NEARBY_DISTANCE = 5;
    private final BoundingBox boundingBox;
    private final Codeword[] codewords;

    DetectionResultColumn(BoundingBox boundingBox2) {
        this.boundingBox = new BoundingBox(boundingBox2);
        this.codewords = new Codeword[((boundingBox2.getMaxY() - boundingBox2.getMinY()) + 1)];
    }

    /* access modifiers changed from: package-private */
    public final Codeword getCodewordNearby(int imageRow) {
        Codeword codeword = getCodeword(imageRow);
        if (codeword != null) {
            return codeword;
        }
        for (int i = 1; i < 5; i++) {
            int nearImageRow = imageRowToCodewordIndex(imageRow) - i;
            if (nearImageRow >= 0 && (codeword = this.codewords[nearImageRow]) != null) {
                return codeword;
            }
            int nearImageRow2 = imageRowToCodewordIndex(imageRow) + i;
            if (nearImageRow2 < this.codewords.length && (codeword = this.codewords[nearImageRow2]) != null) {
                return codeword;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public final int imageRowToCodewordIndex(int imageRow) {
        return imageRow - this.boundingBox.getMinY();
    }

    /* access modifiers changed from: package-private */
    public final void setCodeword(int imageRow, Codeword codeword) {
        this.codewords[imageRowToCodewordIndex(imageRow)] = codeword;
    }

    /* access modifiers changed from: package-private */
    public final Codeword getCodeword(int imageRow) {
        return this.codewords[imageRowToCodewordIndex(imageRow)];
    }

    /* access modifiers changed from: package-private */
    public final BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    /* access modifiers changed from: package-private */
    public final Codeword[] getCodewords() {
        return this.codewords;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0063, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0064, code lost:
        r6.addSuppressed(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0068, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006c, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x006d, code lost:
        r5 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x006f, code lost:
        r4 = th;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0057 A[SYNTHETIC, Splitter:B:25:0x0057] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x006c A[ExcHandler: all (r4v1 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:1:0x0008] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String toString() {
        /*
            r12 = this;
            r4 = 0
            java.util.Formatter r1 = new java.util.Formatter
            r1.<init>()
            r6 = 0
            r2 = 0
            com.google.zxing.pdf417.decoder.Codeword[] r5 = r12.codewords     // Catch:{ Throwable -> 0x0051, all -> 0x006c }
            int r7 = r5.length     // Catch:{ Throwable -> 0x0051, all -> 0x006c }
            r3 = r2
        L_0x000c:
            if (r4 >= r7) goto L_0x005b
            r0 = r5[r4]     // Catch:{ Throwable -> 0x006f, all -> 0x006c }
            if (r0 != 0) goto L_0x0028
            java.lang.String r8 = "%3d:    |   %n"
            r9 = 1
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x006f, all -> 0x006c }
            r10 = 0
            int r2 = r3 + 1
            java.lang.Integer r11 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x0051, all -> 0x006c }
            r9[r10] = r11     // Catch:{ Throwable -> 0x0051, all -> 0x006c }
            r1.format(r8, r9)     // Catch:{ Throwable -> 0x0051, all -> 0x006c }
        L_0x0024:
            int r4 = r4 + 1
            r3 = r2
            goto L_0x000c
        L_0x0028:
            java.lang.String r8 = "%3d: %3d|%3d%n"
            r9 = 3
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x006f, all -> 0x006c }
            r10 = 0
            int r2 = r3 + 1
            java.lang.Integer r11 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x0051, all -> 0x006c }
            r9[r10] = r11     // Catch:{ Throwable -> 0x0051, all -> 0x006c }
            r10 = 1
            int r11 = r0.getRowNumber()     // Catch:{ Throwable -> 0x0051, all -> 0x006c }
            java.lang.Integer r11 = java.lang.Integer.valueOf(r11)     // Catch:{ Throwable -> 0x0051, all -> 0x006c }
            r9[r10] = r11     // Catch:{ Throwable -> 0x0051, all -> 0x006c }
            r10 = 2
            int r11 = r0.getValue()     // Catch:{ Throwable -> 0x0051, all -> 0x006c }
            java.lang.Integer r11 = java.lang.Integer.valueOf(r11)     // Catch:{ Throwable -> 0x0051, all -> 0x006c }
            r9[r10] = r11     // Catch:{ Throwable -> 0x0051, all -> 0x006c }
            r1.format(r8, r9)     // Catch:{ Throwable -> 0x0051, all -> 0x006c }
            goto L_0x0024
        L_0x0051:
            r4 = move-exception
        L_0x0052:
            throw r4     // Catch:{ all -> 0x0053 }
        L_0x0053:
            r5 = move-exception
            r6 = r4
        L_0x0055:
            if (r6 == 0) goto L_0x0068
            r1.close()     // Catch:{ Throwable -> 0x0063 }
        L_0x005a:
            throw r5
        L_0x005b:
            java.lang.String r4 = r1.toString()     // Catch:{ Throwable -> 0x006f, all -> 0x006c }
            r1.close()
            return r4
        L_0x0063:
            r4 = move-exception
            r6.addSuppressed(r4)
            goto L_0x005a
        L_0x0068:
            r1.close()
            goto L_0x005a
        L_0x006c:
            r4 = move-exception
            r5 = r4
            goto L_0x0055
        L_0x006f:
            r4 = move-exception
            r2 = r3
            goto L_0x0052
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DetectionResultColumn.toString():java.lang.String");
    }
}
