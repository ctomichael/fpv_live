package com.google.zxing.multi.qrcode.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.detector.FinderPattern;
import com.google.zxing.qrcode.detector.FinderPatternFinder;
import com.google.zxing.qrcode.detector.FinderPatternInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

final class MultiFinderPatternFinder extends FinderPatternFinder {
    private static final float DIFF_MODSIZE_CUTOFF = 0.5f;
    private static final float DIFF_MODSIZE_CUTOFF_PERCENT = 0.05f;
    private static final FinderPatternInfo[] EMPTY_RESULT_ARRAY = new FinderPatternInfo[0];
    private static final float MAX_MODULE_COUNT_PER_EDGE = 180.0f;
    private static final float MIN_MODULE_COUNT_PER_EDGE = 9.0f;

    private static final class ModuleSizeComparator implements Serializable, Comparator<FinderPattern> {
        private ModuleSizeComparator() {
        }

        public int compare(FinderPattern center1, FinderPattern center2) {
            float value = center2.getEstimatedModuleSize() - center1.getEstimatedModuleSize();
            if (((double) value) < 0.0d) {
                return -1;
            }
            return ((double) value) > 0.0d ? 1 : 0;
        }
    }

    MultiFinderPatternFinder(BitMatrix image) {
        super(image);
    }

    MultiFinderPatternFinder(BitMatrix image, ResultPointCallback resultPointCallback) {
        super(image, resultPointCallback);
    }

    private FinderPattern[][] selectMutipleBestPatterns() throws NotFoundException {
        List<FinderPattern> possibleCenters = getPossibleCenters();
        int size = possibleCenters.size();
        if (size < 3) {
            throw NotFoundException.getNotFoundInstance();
        } else if (size == 3) {
            return new FinderPattern[][]{new FinderPattern[]{possibleCenters.get(0), possibleCenters.get(1), possibleCenters.get(2)}};
        } else {
            Collections.sort(possibleCenters, new ModuleSizeComparator());
            List<FinderPattern[]> results = new ArrayList<>();
            for (int i1 = 0; i1 < size - 2; i1++) {
                FinderPattern p1 = possibleCenters.get(i1);
                if (p1 != null) {
                    for (int i2 = i1 + 1; i2 < size - 1; i2++) {
                        FinderPattern p2 = possibleCenters.get(i2);
                        if (p2 != null) {
                            float vModSize12 = (p1.getEstimatedModuleSize() - p2.getEstimatedModuleSize()) / Math.min(p1.getEstimatedModuleSize(), p2.getEstimatedModuleSize());
                            if (Math.abs(p1.getEstimatedModuleSize() - p2.getEstimatedModuleSize()) > DIFF_MODSIZE_CUTOFF && vModSize12 >= DIFF_MODSIZE_CUTOFF_PERCENT) {
                                break;
                            }
                            for (int i3 = i2 + 1; i3 < size; i3++) {
                                FinderPattern p3 = possibleCenters.get(i3);
                                if (p3 != null) {
                                    float vModSize23 = (p2.getEstimatedModuleSize() - p3.getEstimatedModuleSize()) / Math.min(p2.getEstimatedModuleSize(), p3.getEstimatedModuleSize());
                                    if (Math.abs(p2.getEstimatedModuleSize() - p3.getEstimatedModuleSize()) > DIFF_MODSIZE_CUTOFF && vModSize23 >= DIFF_MODSIZE_CUTOFF_PERCENT) {
                                        break;
                                    }
                                    FinderPattern[] test = {p1, p2, p3};
                                    ResultPoint.orderBestPatterns(test);
                                    FinderPatternInfo info = new FinderPatternInfo(test);
                                    float dA = ResultPoint.distance(info.getTopLeft(), info.getBottomLeft());
                                    float dC = ResultPoint.distance(info.getTopRight(), info.getBottomLeft());
                                    float dB = ResultPoint.distance(info.getTopLeft(), info.getTopRight());
                                    float estimatedModuleCount = (dA + dB) / (p1.getEstimatedModuleSize() * 2.0f);
                                    if (estimatedModuleCount <= 180.0f && estimatedModuleCount >= MIN_MODULE_COUNT_PER_EDGE && Math.abs((dA - dB) / Math.min(dA, dB)) < 0.1f) {
                                        float dCpy = (float) Math.sqrt((double) ((dA * dA) + (dB * dB)));
                                        if (Math.abs((dC - dCpy) / Math.min(dC, dCpy)) < 0.1f) {
                                            results.add(test);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!results.isEmpty()) {
                return (FinderPattern[][]) results.toArray(new FinderPattern[results.size()][]);
            }
            throw NotFoundException.getNotFoundInstance();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00df A[LOOP:2: B:42:0x00dd->B:43:0x00df, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00f9  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00fc  */
    /* JADX WARNING: Removed duplicated region for block: B:5:0x000f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.zxing.qrcode.detector.FinderPatternInfo[] findMulti(java.util.Map<com.google.zxing.DecodeHintType, ?> r18) throws com.google.zxing.NotFoundException {
        /*
            r17 = this;
            if (r18 == 0) goto L_0x0069
            com.google.zxing.DecodeHintType r14 = com.google.zxing.DecodeHintType.TRY_HARDER
            r0 = r18
            boolean r14 = r0.containsKey(r14)
            if (r14 == 0) goto L_0x0069
            r13 = 1
        L_0x000d:
            if (r18 == 0) goto L_0x006b
            com.google.zxing.DecodeHintType r14 = com.google.zxing.DecodeHintType.PURE_BARCODE
            r0 = r18
            boolean r14 = r0.containsKey(r14)
            if (r14 == 0) goto L_0x006b
            r10 = 1
        L_0x001a:
            com.google.zxing.common.BitMatrix r4 = r17.getImage()
            int r6 = r4.getHeight()
            int r7 = r4.getWidth()
            float r14 = (float) r6
            r15 = 1130627072(0x43640000, float:228.0)
            float r14 = r14 / r15
            r15 = 1077936128(0x40400000, float:3.0)
            float r14 = r14 * r15
            int r3 = (int) r14
            r14 = 3
            if (r3 < r14) goto L_0x0033
            if (r13 == 0) goto L_0x0034
        L_0x0033:
            r3 = 3
        L_0x0034:
            r14 = 5
            int[] r12 = new int[r14]
            int r2 = r3 + -1
        L_0x0039:
            if (r2 >= r6) goto L_0x00d2
            r14 = 0
            r15 = 0
            r12[r14] = r15
            r14 = 1
            r15 = 0
            r12[r14] = r15
            r14 = 2
            r15 = 0
            r12[r14] = r15
            r14 = 3
            r15 = 0
            r12[r14] = r15
            r14 = 4
            r15 = 0
            r12[r14] = r15
            r1 = 0
            r5 = 0
        L_0x0051:
            if (r5 >= r7) goto L_0x00c4
            boolean r14 = r4.get(r5, r2)
            if (r14 == 0) goto L_0x006d
            r14 = r1 & 1
            r15 = 1
            if (r14 != r15) goto L_0x0060
            int r1 = r1 + 1
        L_0x0060:
            r14 = r12[r1]
            int r14 = r14 + 1
            r12[r1] = r14
        L_0x0066:
            int r5 = r5 + 1
            goto L_0x0051
        L_0x0069:
            r13 = 0
            goto L_0x000d
        L_0x006b:
            r10 = 0
            goto L_0x001a
        L_0x006d:
            r14 = r1 & 1
            if (r14 != 0) goto L_0x00bd
            r14 = 4
            if (r1 != r14) goto L_0x00b4
            boolean r14 = foundPatternCross(r12)
            if (r14 == 0) goto L_0x0098
            r0 = r17
            boolean r14 = r0.handlePossibleCenter(r12, r2, r5, r10)
            if (r14 == 0) goto L_0x0098
            r1 = 0
            r14 = 0
            r15 = 0
            r12[r14] = r15
            r14 = 1
            r15 = 0
            r12[r14] = r15
            r14 = 2
            r15 = 0
            r12[r14] = r15
            r14 = 3
            r15 = 0
            r12[r14] = r15
            r14 = 4
            r15 = 0
            r12[r14] = r15
            goto L_0x0066
        L_0x0098:
            r14 = 0
            r15 = 2
            r15 = r12[r15]
            r12[r14] = r15
            r14 = 1
            r15 = 3
            r15 = r12[r15]
            r12[r14] = r15
            r14 = 2
            r15 = 4
            r15 = r12[r15]
            r12[r14] = r15
            r14 = 3
            r15 = 1
            r12[r14] = r15
            r14 = 4
            r15 = 0
            r12[r14] = r15
            r1 = 3
            goto L_0x0066
        L_0x00b4:
            int r1 = r1 + 1
            r14 = r12[r1]
            int r14 = r14 + 1
            r12[r1] = r14
            goto L_0x0066
        L_0x00bd:
            r14 = r12[r1]
            int r14 = r14 + 1
            r12[r1] = r14
            goto L_0x0066
        L_0x00c4:
            boolean r14 = foundPatternCross(r12)
            if (r14 == 0) goto L_0x00cf
            r0 = r17
            r0.handlePossibleCenter(r12, r2, r7, r10)
        L_0x00cf:
            int r2 = r2 + r3
            goto L_0x0039
        L_0x00d2:
            com.google.zxing.qrcode.detector.FinderPattern[][] r9 = r17.selectMutipleBestPatterns()
            java.util.ArrayList r11 = new java.util.ArrayList
            r11.<init>()
            int r15 = r9.length
            r14 = 0
        L_0x00dd:
            if (r14 >= r15) goto L_0x00f3
            r8 = r9[r14]
            com.google.zxing.ResultPoint.orderBestPatterns(r8)
            com.google.zxing.qrcode.detector.FinderPatternInfo r16 = new com.google.zxing.qrcode.detector.FinderPatternInfo
            r0 = r16
            r0.<init>(r8)
            r0 = r16
            r11.add(r0)
            int r14 = r14 + 1
            goto L_0x00dd
        L_0x00f3:
            boolean r14 = r11.isEmpty()
            if (r14 == 0) goto L_0x00fc
            com.google.zxing.qrcode.detector.FinderPatternInfo[] r14 = com.google.zxing.multi.qrcode.detector.MultiFinderPatternFinder.EMPTY_RESULT_ARRAY
        L_0x00fb:
            return r14
        L_0x00fc:
            int r14 = r11.size()
            com.google.zxing.qrcode.detector.FinderPatternInfo[] r14 = new com.google.zxing.qrcode.detector.FinderPatternInfo[r14]
            java.lang.Object[] r14 = r11.toArray(r14)
            com.google.zxing.qrcode.detector.FinderPatternInfo[] r14 = (com.google.zxing.qrcode.detector.FinderPatternInfo[]) r14
            goto L_0x00fb
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.multi.qrcode.detector.MultiFinderPatternFinder.findMulti(java.util.Map):com.google.zxing.qrcode.detector.FinderPatternInfo[]");
    }
}
