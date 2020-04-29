package com.google.zxing.common.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

@Deprecated
public final class MonochromeRectangleDetector {
    private static final int MAX_MODULES = 32;
    private final BitMatrix image;

    public MonochromeRectangleDetector(BitMatrix image2) {
        this.image = image2;
    }

    public ResultPoint[] detect() throws NotFoundException {
        int height = this.image.getHeight();
        int width = this.image.getWidth();
        int halfHeight = height / 2;
        int halfWidth = width / 2;
        int deltaY = Math.max(1, height / 256);
        int deltaX = Math.max(1, width / 256);
        int top = ((int) findCornerFromCenter(halfWidth, 0, 0, width, halfHeight, -deltaY, 0, height, halfWidth / 2).getY()) - 1;
        ResultPoint pointB = findCornerFromCenter(halfWidth, -deltaX, 0, width, halfHeight, 0, top, height, halfHeight / 2);
        int left = ((int) pointB.getX()) - 1;
        ResultPoint pointC = findCornerFromCenter(halfWidth, deltaX, left, width, halfHeight, 0, top, height, halfHeight / 2);
        int right = ((int) pointC.getX()) + 1;
        ResultPoint pointD = findCornerFromCenter(halfWidth, 0, left, right, halfHeight, deltaY, top, height, halfWidth / 2);
        return new ResultPoint[]{findCornerFromCenter(halfWidth, 0, left, right, halfHeight, -deltaY, top, ((int) pointD.getY()) + 1, halfWidth / 4), pointB, pointC, pointD};
    }

    private ResultPoint findCornerFromCenter(int centerX, int deltaX, int left, int right, int centerY, int deltaY, int top, int bottom, int maxWhiteRun) throws NotFoundException {
        int[] range;
        char c;
        int[] lastRange = null;
        int y = centerY;
        int x = centerX;
        while (y < bottom && y >= top && x < right && x >= left) {
            if (deltaX == 0) {
                range = blackWhiteRange(y, maxWhiteRun, left, right, true);
            } else {
                range = blackWhiteRange(x, maxWhiteRun, top, bottom, false);
            }
            if (range != null) {
                lastRange = range;
                y += deltaY;
                x += deltaX;
            } else if (lastRange == null) {
                throw NotFoundException.getNotFoundInstance();
            } else if (deltaX == 0) {
                int lastY = y - deltaY;
                if (lastRange[0] >= centerX) {
                    return new ResultPoint((float) lastRange[1], (float) lastY);
                }
                if (lastRange[1] <= centerX) {
                    return new ResultPoint((float) lastRange[0], (float) lastY);
                }
                if (deltaY > 0) {
                    c = 0;
                } else {
                    c = 1;
                }
                return new ResultPoint((float) lastRange[c], (float) lastY);
            } else {
                int lastX = x - deltaX;
                if (lastRange[0] >= centerY) {
                    return new ResultPoint((float) lastX, (float) lastRange[1]);
                }
                if (lastRange[1] <= centerY) {
                    return new ResultPoint((float) lastX, (float) lastRange[0]);
                }
                return new ResultPoint((float) lastX, (float) lastRange[deltaX < 0 ? (char) 0 : 1]);
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001f, code lost:
        if (r2 < r10) goto L_0x002b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0021, code lost:
        if (r12 == false) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0029, code lost:
        if (r7.image.get(r2, r8) == false) goto L_0x001d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002b, code lost:
        r3 = r4 - r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002d, code lost:
        if (r2 < r10) goto L_0x0031;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
        if (r3 <= r9) goto L_0x0005;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004a, code lost:
        if (r7.image.get(r8, r2) == false) goto L_0x001d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0055, code lost:
        r4 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0056, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0058, code lost:
        if (r1 >= r11) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x005a, code lost:
        if (r12 == false) goto L_0x0079;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0062, code lost:
        if (r7.image.get(r1, r8) == false) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0064, code lost:
        r3 = r1 - r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0066, code lost:
        if (r1 >= r11) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0068, code lost:
        if (r3 <= r9) goto L_0x0035;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x006a, code lost:
        r1 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x007f, code lost:
        if (r7.image.get(r8, r1) == false) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001c, code lost:
        r4 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001d, code lost:
        r2 = r2 - 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int[] blackWhiteRange(int r8, int r9, int r10, int r11, boolean r12) {
        /*
            r7 = this;
            int r5 = r10 + r11
            int r0 = r5 / 2
            r2 = r0
        L_0x0005:
            if (r2 < r10) goto L_0x0032
            if (r12 == 0) goto L_0x0014
            com.google.zxing.common.BitMatrix r5 = r7.image
            boolean r5 = r5.get(r2, r8)
            if (r5 == 0) goto L_0x001c
        L_0x0011:
            int r2 = r2 + -1
            goto L_0x0005
        L_0x0014:
            com.google.zxing.common.BitMatrix r5 = r7.image
            boolean r5 = r5.get(r8, r2)
            if (r5 != 0) goto L_0x0011
        L_0x001c:
            r4 = r2
        L_0x001d:
            int r2 = r2 + -1
            if (r2 < r10) goto L_0x002b
            if (r12 == 0) goto L_0x0044
            com.google.zxing.common.BitMatrix r5 = r7.image
            boolean r5 = r5.get(r2, r8)
            if (r5 == 0) goto L_0x001d
        L_0x002b:
            int r3 = r4 - r2
            if (r2 < r10) goto L_0x0031
            if (r3 <= r9) goto L_0x0005
        L_0x0031:
            r2 = r4
        L_0x0032:
            int r2 = r2 + 1
            r1 = r0
        L_0x0035:
            if (r1 >= r11) goto L_0x006b
            if (r12 == 0) goto L_0x004d
            com.google.zxing.common.BitMatrix r5 = r7.image
            boolean r5 = r5.get(r1, r8)
            if (r5 == 0) goto L_0x0055
        L_0x0041:
            int r1 = r1 + 1
            goto L_0x0035
        L_0x0044:
            com.google.zxing.common.BitMatrix r5 = r7.image
            boolean r5 = r5.get(r8, r2)
            if (r5 == 0) goto L_0x001d
            goto L_0x002b
        L_0x004d:
            com.google.zxing.common.BitMatrix r5 = r7.image
            boolean r5 = r5.get(r8, r1)
            if (r5 != 0) goto L_0x0041
        L_0x0055:
            r4 = r1
        L_0x0056:
            int r1 = r1 + 1
            if (r1 >= r11) goto L_0x0064
            if (r12 == 0) goto L_0x0079
            com.google.zxing.common.BitMatrix r5 = r7.image
            boolean r5 = r5.get(r1, r8)
            if (r5 == 0) goto L_0x0056
        L_0x0064:
            int r3 = r1 - r4
            if (r1 >= r11) goto L_0x006a
            if (r3 <= r9) goto L_0x0035
        L_0x006a:
            r1 = r4
        L_0x006b:
            int r1 = r1 + -1
            if (r1 <= r2) goto L_0x0082
            r5 = 2
            int[] r5 = new int[r5]
            r6 = 0
            r5[r6] = r2
            r6 = 1
            r5[r6] = r1
        L_0x0078:
            return r5
        L_0x0079:
            com.google.zxing.common.BitMatrix r5 = r7.image
            boolean r5 = r5.get(r8, r1)
            if (r5 == 0) goto L_0x0056
            goto L_0x0064
        L_0x0082:
            r5 = 0
            goto L_0x0078
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.common.detector.MonochromeRectangleDetector.blackWhiteRange(int, int, int, int, boolean):int[]");
    }
}
