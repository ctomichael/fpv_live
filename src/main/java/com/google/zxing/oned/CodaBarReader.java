package com.google.zxing.oned;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

public final class CodaBarReader extends OneDReader {
    static final char[] ALPHABET = ALPHABET_STRING.toCharArray();
    private static final String ALPHABET_STRING = "0123456789-$:/.+ABCD";
    static final int[] CHARACTER_ENCODINGS = {3, 6, 9, 96, 18, 66, 33, 36, 48, 72, 12, 24, 69, 81, 84, 21, 26, 41, 11, 14};
    private static final float MAX_ACCEPTABLE = 2.0f;
    private static final int MIN_CHARACTER_LENGTH = 3;
    private static final float PADDING = 1.5f;
    private static final char[] STARTEND_ENCODING = {'A', 'B', 'C', 'D'};
    private int counterLength = 0;
    private int[] counters = new int[80];
    private final StringBuilder decodeRowResult = new StringBuilder(20);

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00ee, code lost:
        if (r23.containsKey(com.google.zxing.DecodeHintType.RETURN_CODABAR_START_END) == false) goto L_0x00f0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.zxing.Result decodeRow(int r21, com.google.zxing.common.BitArray r22, java.util.Map<com.google.zxing.DecodeHintType, ?> r23) throws com.google.zxing.NotFoundException {
        /*
            r20 = this;
            r0 = r20
            int[] r13 = r0.counters
            r14 = 0
            java.util.Arrays.fill(r13, r14)
            r0 = r20
            r1 = r22
            r0.setCounters(r1)
            int r10 = r20.findStartPattern()
            r7 = r10
            r0 = r20
            java.lang.StringBuilder r13 = r0.decodeRowResult
            r14 = 0
            r13.setLength(r14)
        L_0x001c:
            r0 = r20
            int r2 = r0.toNarrowWidePattern(r7)
            r13 = -1
            if (r2 != r13) goto L_0x002a
            com.google.zxing.NotFoundException r13 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r13
        L_0x002a:
            r0 = r20
            java.lang.StringBuilder r13 = r0.decodeRowResult
            char r14 = (char) r2
            r13.append(r14)
            int r7 = r7 + 8
            r0 = r20
            java.lang.StringBuilder r13 = r0.decodeRowResult
            int r13 = r13.length()
            r14 = 1
            if (r13 <= r14) goto L_0x004b
            char[] r13 = com.google.zxing.oned.CodaBarReader.STARTEND_ENCODING
            char[] r14 = com.google.zxing.oned.CodaBarReader.ALPHABET
            char r14 = r14[r2]
            boolean r13 = arrayContains(r13, r14)
            if (r13 != 0) goto L_0x0051
        L_0x004b:
            r0 = r20
            int r13 = r0.counterLength
            if (r7 < r13) goto L_0x001c
        L_0x0051:
            r0 = r20
            int[] r13 = r0.counters
            int r14 = r7 + -1
            r12 = r13[r14]
            r5 = 0
            r4 = -8
        L_0x005b:
            r13 = -1
            if (r4 >= r13) goto L_0x006a
            r0 = r20
            int[] r13 = r0.counters
            int r14 = r7 + r4
            r13 = r13[r14]
            int r5 = r5 + r13
            int r4 = r4 + 1
            goto L_0x005b
        L_0x006a:
            r0 = r20
            int r13 = r0.counterLength
            if (r7 >= r13) goto L_0x0079
            int r13 = r5 / 2
            if (r12 >= r13) goto L_0x0079
            com.google.zxing.NotFoundException r13 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r13
        L_0x0079:
            r0 = r20
            r0.validatePattern(r10)
            r4 = 0
        L_0x007f:
            r0 = r20
            java.lang.StringBuilder r13 = r0.decodeRowResult
            int r13 = r13.length()
            if (r4 >= r13) goto L_0x009f
            r0 = r20
            java.lang.StringBuilder r13 = r0.decodeRowResult
            char[] r14 = com.google.zxing.oned.CodaBarReader.ALPHABET
            r0 = r20
            java.lang.StringBuilder r15 = r0.decodeRowResult
            char r15 = r15.charAt(r4)
            char r14 = r14[r15]
            r13.setCharAt(r4, r14)
            int r4 = r4 + 1
            goto L_0x007f
        L_0x009f:
            r0 = r20
            java.lang.StringBuilder r13 = r0.decodeRowResult
            r14 = 0
            char r11 = r13.charAt(r14)
            char[] r13 = com.google.zxing.oned.CodaBarReader.STARTEND_ENCODING
            boolean r13 = arrayContains(r13, r11)
            if (r13 != 0) goto L_0x00b5
            com.google.zxing.NotFoundException r13 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r13
        L_0x00b5:
            r0 = r20
            java.lang.StringBuilder r13 = r0.decodeRowResult
            r0 = r20
            java.lang.StringBuilder r14 = r0.decodeRowResult
            int r14 = r14.length()
            int r14 = r14 + -1
            char r3 = r13.charAt(r14)
            char[] r13 = com.google.zxing.oned.CodaBarReader.STARTEND_ENCODING
            boolean r13 = arrayContains(r13, r3)
            if (r13 != 0) goto L_0x00d4
            com.google.zxing.NotFoundException r13 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r13
        L_0x00d4:
            r0 = r20
            java.lang.StringBuilder r13 = r0.decodeRowResult
            int r13 = r13.length()
            r14 = 3
            if (r13 > r14) goto L_0x00e4
            com.google.zxing.NotFoundException r13 = com.google.zxing.NotFoundException.getNotFoundInstance()
            throw r13
        L_0x00e4:
            if (r23 == 0) goto L_0x00f0
            com.google.zxing.DecodeHintType r13 = com.google.zxing.DecodeHintType.RETURN_CODABAR_START_END
            r0 = r23
            boolean r13 = r0.containsKey(r13)
            if (r13 != 0) goto L_0x0109
        L_0x00f0:
            r0 = r20
            java.lang.StringBuilder r13 = r0.decodeRowResult
            r0 = r20
            java.lang.StringBuilder r14 = r0.decodeRowResult
            int r14 = r14.length()
            int r14 = r14 + -1
            r13.deleteCharAt(r14)
            r0 = r20
            java.lang.StringBuilder r13 = r0.decodeRowResult
            r14 = 0
            r13.deleteCharAt(r14)
        L_0x0109:
            r9 = 0
            r4 = 0
        L_0x010b:
            if (r4 >= r10) goto L_0x0117
            r0 = r20
            int[] r13 = r0.counters
            r13 = r13[r4]
            int r9 = r9 + r13
            int r4 = r4 + 1
            goto L_0x010b
        L_0x0117:
            float r6 = (float) r9
            r4 = r10
        L_0x0119:
            int r13 = r7 + -1
            if (r4 >= r13) goto L_0x0127
            r0 = r20
            int[] r13 = r0.counters
            r13 = r13[r4]
            int r9 = r9 + r13
            int r4 = r4 + 1
            goto L_0x0119
        L_0x0127:
            float r8 = (float) r9
            com.google.zxing.Result r13 = new com.google.zxing.Result
            r0 = r20
            java.lang.StringBuilder r14 = r0.decodeRowResult
            java.lang.String r14 = r14.toString()
            r15 = 0
            r16 = 2
            r0 = r16
            com.google.zxing.ResultPoint[] r0 = new com.google.zxing.ResultPoint[r0]
            r16 = r0
            r17 = 0
            com.google.zxing.ResultPoint r18 = new com.google.zxing.ResultPoint
            r0 = r21
            float r0 = (float) r0
            r19 = r0
            r0 = r18
            r1 = r19
            r0.<init>(r6, r1)
            r16[r17] = r18
            r17 = 1
            com.google.zxing.ResultPoint r18 = new com.google.zxing.ResultPoint
            r0 = r21
            float r0 = (float) r0
            r19 = r0
            r0 = r18
            r1 = r19
            r0.<init>(r8, r1)
            r16[r17] = r18
            com.google.zxing.BarcodeFormat r17 = com.google.zxing.BarcodeFormat.CODABAR
            r13.<init>(r14, r15, r16, r17)
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.oned.CodaBarReader.decodeRow(int, com.google.zxing.common.BitArray, java.util.Map):com.google.zxing.Result");
    }

    private void validatePattern(int start) throws NotFoundException {
        int[] sizes = {0, 0, 0, 0};
        int[] counts = {0, 0, 0, 0};
        int end = this.decodeRowResult.length() - 1;
        int pos = start;
        int i = 0;
        while (true) {
            int pattern = CHARACTER_ENCODINGS[this.decodeRowResult.charAt(i)];
            for (int j = 6; j >= 0; j--) {
                int category = (j & 1) + ((pattern & 1) << 1);
                sizes[category] = sizes[category] + this.counters[pos + j];
                counts[category] = counts[category] + 1;
                pattern >>= 1;
            }
            if (i >= end) {
                break;
            }
            pos += 8;
            i++;
        }
        float[] maxes = new float[4];
        float[] mins = new float[4];
        for (int i2 = 0; i2 < 2; i2++) {
            mins[i2] = 0.0f;
            mins[i2 + 2] = ((((float) sizes[i2]) / ((float) counts[i2])) + (((float) sizes[i2 + 2]) / ((float) counts[i2 + 2]))) / 2.0f;
            maxes[i2] = mins[i2 + 2];
            maxes[i2 + 2] = ((((float) sizes[i2 + 2]) * 2.0f) + 1.5f) / ((float) counts[i2 + 2]);
        }
        int pos2 = start;
        int i3 = 0;
        loop3:
        while (true) {
            int pattern2 = CHARACTER_ENCODINGS[this.decodeRowResult.charAt(i3)];
            int j2 = 6;
            while (j2 >= 0) {
                int category2 = (j2 & 1) + ((pattern2 & 1) << 1);
                int size = this.counters[pos2 + j2];
                if (((float) size) >= mins[category2] && ((float) size) <= maxes[category2]) {
                    pattern2 >>= 1;
                    j2--;
                }
            }
            if (i3 < end) {
                pos2 += 8;
                i3++;
            } else {
                return;
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private void setCounters(BitArray row) throws NotFoundException {
        this.counterLength = 0;
        int i = row.getNextUnset(0);
        int end = row.getSize();
        if (i >= end) {
            throw NotFoundException.getNotFoundInstance();
        }
        boolean isWhite = true;
        int count = 0;
        while (i < end) {
            if (row.get(i) != isWhite) {
                count++;
            } else {
                counterAppend(count);
                count = 1;
                isWhite = !isWhite;
            }
            i++;
        }
        counterAppend(count);
    }

    private void counterAppend(int e) {
        this.counters[this.counterLength] = e;
        this.counterLength++;
        if (this.counterLength >= this.counters.length) {
            int[] temp = new int[(this.counterLength << 1)];
            System.arraycopy(this.counters, 0, temp, 0, this.counterLength);
            this.counters = temp;
        }
    }

    private int findStartPattern() throws NotFoundException {
        for (int i = 1; i < this.counterLength; i += 2) {
            int charOffset = toNarrowWidePattern(i);
            if (charOffset != -1 && arrayContains(STARTEND_ENCODING, ALPHABET[charOffset])) {
                int patternSize = 0;
                for (int j = i; j < i + 7; j++) {
                    patternSize += this.counters[j];
                }
                if (i == 1 || this.counters[i - 1] >= patternSize / 2) {
                    return i;
                }
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    static boolean arrayContains(char[] array, char key) {
        if (array == null) {
            return false;
        }
        for (char c : array) {
            if (c == key) {
                return true;
            }
        }
        return false;
    }

    private int toNarrowWidePattern(int position) {
        int threshold;
        int end = position + 7;
        if (end >= this.counterLength) {
            return -1;
        }
        int[] theCounters = this.counters;
        int maxBar = 0;
        int minBar = Integer.MAX_VALUE;
        for (int j = position; j < end; j += 2) {
            int currentCounter = theCounters[j];
            if (currentCounter < minBar) {
                minBar = currentCounter;
            }
            if (currentCounter > maxBar) {
                maxBar = currentCounter;
            }
        }
        int thresholdBar = (minBar + maxBar) / 2;
        int maxSpace = 0;
        int minSpace = Integer.MAX_VALUE;
        for (int j2 = position + 1; j2 < end; j2 += 2) {
            int currentCounter2 = theCounters[j2];
            if (currentCounter2 < minSpace) {
                minSpace = currentCounter2;
            }
            if (currentCounter2 > maxSpace) {
                maxSpace = currentCounter2;
            }
        }
        int thresholdSpace = (minSpace + maxSpace) / 2;
        int bitmask = 128;
        int pattern = 0;
        for (int i = 0; i < 7; i++) {
            if ((i & 1) == 0) {
                threshold = thresholdBar;
            } else {
                threshold = thresholdSpace;
            }
            bitmask >>= 1;
            if (theCounters[position + i] > threshold) {
                pattern |= bitmask;
            }
        }
        for (int i2 = 0; i2 < CHARACTER_ENCODINGS.length; i2++) {
            if (CHARACTER_ENCODINGS[i2] == pattern) {
                return i2;
            }
        }
        return -1;
    }
}
