package com.google.zxing.pdf417.decoder;

import com.google.zxing.pdf417.PDF417Common;

final class DetectionResult {
    private static final int ADJUST_ROW_NUMBER_SKIP = 2;
    private final int barcodeColumnCount;
    private final BarcodeMetadata barcodeMetadata;
    private BoundingBox boundingBox;
    private final DetectionResultColumn[] detectionResultColumns = new DetectionResultColumn[(this.barcodeColumnCount + 2)];

    DetectionResult(BarcodeMetadata barcodeMetadata2, BoundingBox boundingBox2) {
        this.barcodeMetadata = barcodeMetadata2;
        this.barcodeColumnCount = barcodeMetadata2.getColumnCount();
        this.boundingBox = boundingBox2;
    }

    /* access modifiers changed from: package-private */
    public DetectionResultColumn[] getDetectionResultColumns() {
        int previousUnadjustedCount;
        adjustIndicatorColumnRowNumbers(this.detectionResultColumns[0]);
        adjustIndicatorColumnRowNumbers(this.detectionResultColumns[this.barcodeColumnCount + 1]);
        int unadjustedCodewordCount = PDF417Common.MAX_CODEWORDS_IN_BARCODE;
        do {
            previousUnadjustedCount = unadjustedCodewordCount;
            unadjustedCodewordCount = adjustRowNumbers();
            if (unadjustedCodewordCount <= 0) {
                break;
            }
        } while (unadjustedCodewordCount < previousUnadjustedCount);
        return this.detectionResultColumns;
    }

    private void adjustIndicatorColumnRowNumbers(DetectionResultColumn detectionResultColumn) {
        if (detectionResultColumn != null) {
            ((DetectionResultRowIndicatorColumn) detectionResultColumn).adjustCompleteIndicatorColumnRowNumbers(this.barcodeMetadata);
        }
    }

    private int adjustRowNumbers() {
        int unadjustedCount = adjustRowNumbersByRow();
        if (unadjustedCount == 0) {
            return 0;
        }
        for (int barcodeColumn = 1; barcodeColumn < this.barcodeColumnCount + 1; barcodeColumn++) {
            Codeword[] codewords = this.detectionResultColumns[barcodeColumn].getCodewords();
            for (int codewordsRow = 0; codewordsRow < codewords.length; codewordsRow++) {
                if (codewords[codewordsRow] != null && !codewords[codewordsRow].hasValidRowNumber()) {
                    adjustRowNumbers(barcodeColumn, codewordsRow, codewords);
                }
            }
        }
        return unadjustedCount;
    }

    private int adjustRowNumbersByRow() {
        adjustRowNumbersFromBothRI();
        return adjustRowNumbersFromLRI() + adjustRowNumbersFromRRI();
    }

    private void adjustRowNumbersFromBothRI() {
        if (this.detectionResultColumns[0] != null && this.detectionResultColumns[this.barcodeColumnCount + 1] != null) {
            Codeword[] LRIcodewords = this.detectionResultColumns[0].getCodewords();
            Codeword[] RRIcodewords = this.detectionResultColumns[this.barcodeColumnCount + 1].getCodewords();
            for (int codewordsRow = 0; codewordsRow < LRIcodewords.length; codewordsRow++) {
                if (!(LRIcodewords[codewordsRow] == null || RRIcodewords[codewordsRow] == null || LRIcodewords[codewordsRow].getRowNumber() != RRIcodewords[codewordsRow].getRowNumber())) {
                    for (int barcodeColumn = 1; barcodeColumn <= this.barcodeColumnCount; barcodeColumn++) {
                        Codeword codeword = this.detectionResultColumns[barcodeColumn].getCodewords()[codewordsRow];
                        if (codeword != null) {
                            codeword.setRowNumber(LRIcodewords[codewordsRow].getRowNumber());
                            if (!codeword.hasValidRowNumber()) {
                                this.detectionResultColumns[barcodeColumn].getCodewords()[codewordsRow] = null;
                            }
                        }
                    }
                }
            }
        }
    }

    private int adjustRowNumbersFromRRI() {
        if (this.detectionResultColumns[this.barcodeColumnCount + 1] == null) {
            return 0;
        }
        int unadjustedCount = 0;
        Codeword[] codewords = this.detectionResultColumns[this.barcodeColumnCount + 1].getCodewords();
        for (int codewordsRow = 0; codewordsRow < codewords.length; codewordsRow++) {
            if (codewords[codewordsRow] != null) {
                int rowIndicatorRowNumber = codewords[codewordsRow].getRowNumber();
                int invalidRowCounts = 0;
                for (int barcodeColumn = this.barcodeColumnCount + 1; barcodeColumn > 0 && invalidRowCounts < 2; barcodeColumn--) {
                    Codeword codeword = this.detectionResultColumns[barcodeColumn].getCodewords()[codewordsRow];
                    if (codeword != null) {
                        invalidRowCounts = adjustRowNumberIfValid(rowIndicatorRowNumber, invalidRowCounts, codeword);
                        if (!codeword.hasValidRowNumber()) {
                            unadjustedCount++;
                        }
                    }
                }
            }
        }
        return unadjustedCount;
    }

    private int adjustRowNumbersFromLRI() {
        if (this.detectionResultColumns[0] == null) {
            return 0;
        }
        int unadjustedCount = 0;
        Codeword[] codewords = this.detectionResultColumns[0].getCodewords();
        for (int codewordsRow = 0; codewordsRow < codewords.length; codewordsRow++) {
            if (codewords[codewordsRow] != null) {
                int rowIndicatorRowNumber = codewords[codewordsRow].getRowNumber();
                int invalidRowCounts = 0;
                for (int barcodeColumn = 1; barcodeColumn < this.barcodeColumnCount + 1 && invalidRowCounts < 2; barcodeColumn++) {
                    Codeword codeword = this.detectionResultColumns[barcodeColumn].getCodewords()[codewordsRow];
                    if (codeword != null) {
                        invalidRowCounts = adjustRowNumberIfValid(rowIndicatorRowNumber, invalidRowCounts, codeword);
                        if (!codeword.hasValidRowNumber()) {
                            unadjustedCount++;
                        }
                    }
                }
            }
        }
        return unadjustedCount;
    }

    private static int adjustRowNumberIfValid(int rowIndicatorRowNumber, int invalidRowCounts, Codeword codeword) {
        if (codeword == null) {
            return invalidRowCounts;
        }
        if (!codeword.hasValidRowNumber()) {
            if (codeword.isValidRowNumber(rowIndicatorRowNumber)) {
                codeword.setRowNumber(rowIndicatorRowNumber);
                invalidRowCounts = 0;
            } else {
                invalidRowCounts++;
            }
        }
        return invalidRowCounts;
    }

    private void adjustRowNumbers(int barcodeColumn, int codewordsRow, Codeword[] codewords) {
        int i = 0;
        Codeword codeword = codewords[codewordsRow];
        Codeword[] previousColumnCodewords = this.detectionResultColumns[barcodeColumn - 1].getCodewords();
        Codeword[] nextColumnCodewords = previousColumnCodewords;
        if (this.detectionResultColumns[barcodeColumn + 1] != null) {
            nextColumnCodewords = this.detectionResultColumns[barcodeColumn + 1].getCodewords();
        }
        Codeword[] otherCodewords = new Codeword[14];
        otherCodewords[2] = previousColumnCodewords[codewordsRow];
        otherCodewords[3] = nextColumnCodewords[codewordsRow];
        if (codewordsRow > 0) {
            otherCodewords[0] = codewords[codewordsRow - 1];
            otherCodewords[4] = previousColumnCodewords[codewordsRow - 1];
            otherCodewords[5] = nextColumnCodewords[codewordsRow - 1];
        }
        if (codewordsRow > 1) {
            otherCodewords[8] = codewords[codewordsRow - 2];
            otherCodewords[10] = previousColumnCodewords[codewordsRow - 2];
            otherCodewords[11] = nextColumnCodewords[codewordsRow - 2];
        }
        if (codewordsRow < codewords.length - 1) {
            otherCodewords[1] = codewords[codewordsRow + 1];
            otherCodewords[6] = previousColumnCodewords[codewordsRow + 1];
            otherCodewords[7] = nextColumnCodewords[codewordsRow + 1];
        }
        if (codewordsRow < codewords.length - 2) {
            otherCodewords[9] = codewords[codewordsRow + 2];
            otherCodewords[12] = previousColumnCodewords[codewordsRow + 2];
            otherCodewords[13] = nextColumnCodewords[codewordsRow + 2];
        }
        while (i < 14 && !adjustRowNumber(codeword, otherCodewords[i])) {
            i++;
        }
    }

    private static boolean adjustRowNumber(Codeword codeword, Codeword otherCodeword) {
        if (otherCodeword == null || !otherCodeword.hasValidRowNumber() || otherCodeword.getBucket() != codeword.getBucket()) {
            return false;
        }
        codeword.setRowNumber(otherCodeword.getRowNumber());
        return true;
    }

    /* access modifiers changed from: package-private */
    public int getBarcodeColumnCount() {
        return this.barcodeColumnCount;
    }

    /* access modifiers changed from: package-private */
    public int getBarcodeRowCount() {
        return this.barcodeMetadata.getRowCount();
    }

    /* access modifiers changed from: package-private */
    public int getBarcodeECLevel() {
        return this.barcodeMetadata.getErrorCorrectionLevel();
    }

    /* access modifiers changed from: package-private */
    public void setBoundingBox(BoundingBox boundingBox2) {
        this.boundingBox = boundingBox2;
    }

    /* access modifiers changed from: package-private */
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    /* access modifiers changed from: package-private */
    public void setDetectionResultColumn(int barcodeColumn, DetectionResultColumn detectionResultColumn) {
        this.detectionResultColumns[barcodeColumn] = detectionResultColumn;
    }

    /* access modifiers changed from: package-private */
    public DetectionResultColumn getDetectionResultColumn(int barcodeColumn) {
        return this.detectionResultColumns[barcodeColumn];
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005e, code lost:
        r6 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005f, code lost:
        r7 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0086, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0087, code lost:
        r6 = r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String toString() {
        /*
            r10 = this;
            r6 = 0
            com.google.zxing.pdf417.decoder.DetectionResultColumn[] r5 = r10.detectionResultColumns
            r4 = r5[r6]
            if (r4 != 0) goto L_0x000f
            com.google.zxing.pdf417.decoder.DetectionResultColumn[] r5 = r10.detectionResultColumns
            int r6 = r10.barcodeColumnCount
            int r6 = r6 + 1
            r4 = r5[r6]
        L_0x000f:
            java.util.Formatter r3 = new java.util.Formatter
            r3.<init>()
            r7 = 0
            r2 = 0
        L_0x0016:
            com.google.zxing.pdf417.decoder.Codeword[] r5 = r4.getCodewords()     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            int r5 = r5.length     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            if (r2 >= r5) goto L_0x0095
            java.lang.String r5 = "CW %3d:"
            r6 = 1
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r8 = 0
            java.lang.Integer r9 = java.lang.Integer.valueOf(r2)     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r6[r8] = r9     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r3.format(r5, r6)     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r0 = 0
        L_0x002e:
            int r5 = r10.barcodeColumnCount     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            int r5 = r5 + 2
            if (r0 >= r5) goto L_0x0089
            com.google.zxing.pdf417.decoder.DetectionResultColumn[] r5 = r10.detectionResultColumns     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r5 = r5[r0]     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            if (r5 != 0) goto L_0x0046
            java.lang.String r5 = "    |   "
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r3.format(r5, r6)     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
        L_0x0043:
            int r0 = r0 + 1
            goto L_0x002e
        L_0x0046:
            com.google.zxing.pdf417.decoder.DetectionResultColumn[] r5 = r10.detectionResultColumns     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r5 = r5[r0]     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            com.google.zxing.pdf417.decoder.Codeword[] r5 = r5.getCodewords()     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r1 = r5[r2]     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            if (r1 != 0) goto L_0x0066
            java.lang.String r5 = "    |   "
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r3.format(r5, r6)     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            goto L_0x0043
        L_0x005c:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x005e }
        L_0x005e:
            r6 = move-exception
            r7 = r5
        L_0x0060:
            if (r7 == 0) goto L_0x00a2
            r3.close()     // Catch:{ Throwable -> 0x009d }
        L_0x0065:
            throw r6
        L_0x0066:
            java.lang.String r5 = " %3d|%3d"
            r6 = 2
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r8 = 0
            int r9 = r1.getRowNumber()     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r6[r8] = r9     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r8 = 1
            int r9 = r1.getValue()     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r6[r8] = r9     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r3.format(r5, r6)     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            goto L_0x0043
        L_0x0086:
            r5 = move-exception
            r6 = r5
            goto L_0x0060
        L_0x0089:
            java.lang.String r5 = "%n"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r3.format(r5, r6)     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            int r2 = r2 + 1
            goto L_0x0016
        L_0x0095:
            java.lang.String r5 = r3.toString()     // Catch:{ Throwable -> 0x005c, all -> 0x0086 }
            r3.close()
            return r5
        L_0x009d:
            r5 = move-exception
            r7.addSuppressed(r5)
            goto L_0x0065
        L_0x00a2:
            r3.close()
            goto L_0x0065
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DetectionResult.toString():java.lang.String");
    }
}
