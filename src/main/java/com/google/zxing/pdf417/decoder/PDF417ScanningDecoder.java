package com.google.zxing.pdf417.decoder;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.detector.MathUtils;
import com.google.zxing.pdf417.PDF417Common;
import com.google.zxing.pdf417.decoder.ec.ErrorCorrection;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class PDF417ScanningDecoder {
    private static final int CODEWORD_SKEW_SIZE = 2;
    private static final int MAX_EC_CODEWORDS = 512;
    private static final int MAX_ERRORS = 3;
    private static final ErrorCorrection errorCorrection = new ErrorCorrection();

    private PDF417ScanningDecoder() {
    }

    public static DecoderResult decode(BitMatrix image, ResultPoint imageTopLeft, ResultPoint imageBottomLeft, ResultPoint imageTopRight, ResultPoint imageBottomRight, int minCodewordWidth, int maxCodewordWidth) throws NotFoundException, FormatException, ChecksumException {
        DetectionResultColumn detectionResultColumn;
        BoundingBox boundingBox = new BoundingBox(image, imageTopLeft, imageBottomLeft, imageTopRight, imageBottomRight);
        DetectionResultRowIndicatorColumn leftRowIndicatorColumn = null;
        DetectionResultRowIndicatorColumn rightRowIndicatorColumn = null;
        DetectionResult detectionResult = null;
        int i = 0;
        while (true) {
            if (i >= 2) {
                break;
            }
            if (imageTopLeft != null) {
                leftRowIndicatorColumn = getRowIndicatorColumn(image, boundingBox, imageTopLeft, true, minCodewordWidth, maxCodewordWidth);
            }
            if (imageTopRight != null) {
                rightRowIndicatorColumn = getRowIndicatorColumn(image, boundingBox, imageTopRight, false, minCodewordWidth, maxCodewordWidth);
            }
            detectionResult = merge(leftRowIndicatorColumn, rightRowIndicatorColumn);
            if (detectionResult == null) {
                throw NotFoundException.getNotFoundInstance();
            } else if (i != 0 || detectionResult.getBoundingBox() == null || (detectionResult.getBoundingBox().getMinY() >= boundingBox.getMinY() && detectionResult.getBoundingBox().getMaxY() <= boundingBox.getMaxY())) {
                detectionResult.setBoundingBox(boundingBox);
            } else {
                boundingBox = detectionResult.getBoundingBox();
                i++;
            }
        }
        int maxBarcodeColumn = detectionResult.getBarcodeColumnCount() + 1;
        detectionResult.setDetectionResultColumn(0, leftRowIndicatorColumn);
        detectionResult.setDetectionResultColumn(maxBarcodeColumn, rightRowIndicatorColumn);
        boolean leftToRight = leftRowIndicatorColumn != null;
        for (int barcodeColumnCount = 1; barcodeColumnCount <= maxBarcodeColumn; barcodeColumnCount++) {
            int barcodeColumn = leftToRight ? barcodeColumnCount : maxBarcodeColumn - barcodeColumnCount;
            if (detectionResult.getDetectionResultColumn(barcodeColumn) == null) {
                if (barcodeColumn == 0 || barcodeColumn == maxBarcodeColumn) {
                    detectionResultColumn = new DetectionResultRowIndicatorColumn(boundingBox, barcodeColumn == 0);
                } else {
                    detectionResultColumn = new DetectionResultColumn(boundingBox);
                }
                detectionResult.setDetectionResultColumn(barcodeColumn, detectionResultColumn);
                int previousStartColumn = -1;
                for (int imageRow = boundingBox.getMinY(); imageRow <= boundingBox.getMaxY(); imageRow++) {
                    int startColumn = getStartColumn(detectionResult, barcodeColumn, imageRow, leftToRight);
                    if (startColumn < 0 || startColumn > boundingBox.getMaxX()) {
                        if (previousStartColumn != -1) {
                            startColumn = previousStartColumn;
                        }
                    }
                    Codeword codeword = detectCodeword(image, boundingBox.getMinX(), boundingBox.getMaxX(), leftToRight, startColumn, imageRow, minCodewordWidth, maxCodewordWidth);
                    if (codeword != null) {
                        detectionResultColumn.setCodeword(imageRow, codeword);
                        previousStartColumn = startColumn;
                        minCodewordWidth = Math.min(minCodewordWidth, codeword.getWidth());
                        maxCodewordWidth = Math.max(maxCodewordWidth, codeword.getWidth());
                    }
                }
            }
        }
        return createDecoderResult(detectionResult);
    }

    private static DetectionResult merge(DetectionResultRowIndicatorColumn leftRowIndicatorColumn, DetectionResultRowIndicatorColumn rightRowIndicatorColumn) throws NotFoundException {
        BarcodeMetadata barcodeMetadata;
        if ((leftRowIndicatorColumn == null && rightRowIndicatorColumn == null) || (barcodeMetadata = getBarcodeMetadata(leftRowIndicatorColumn, rightRowIndicatorColumn)) == null) {
            return null;
        }
        return new DetectionResult(barcodeMetadata, BoundingBox.merge(adjustBoundingBox(leftRowIndicatorColumn), adjustBoundingBox(rightRowIndicatorColumn)));
    }

    private static BoundingBox adjustBoundingBox(DetectionResultRowIndicatorColumn rowIndicatorColumn) throws NotFoundException {
        int[] rowHeights;
        if (rowIndicatorColumn == null || (rowHeights = rowIndicatorColumn.getRowHeights()) == null) {
            return null;
        }
        int maxRowHeight = getMax(rowHeights);
        int missingStartRows = 0;
        for (int rowHeight : rowHeights) {
            missingStartRows += maxRowHeight - rowHeight;
            if (rowHeight > 0) {
                break;
            }
        }
        Codeword[] codewords = rowIndicatorColumn.getCodewords();
        int row = 0;
        while (missingStartRows > 0 && codewords[row] == null) {
            missingStartRows--;
            row++;
        }
        int missingEndRows = 0;
        for (int row2 = rowHeights.length - 1; row2 >= 0; row2--) {
            missingEndRows += maxRowHeight - rowHeights[row2];
            if (rowHeights[row2] > 0) {
                break;
            }
        }
        int row3 = codewords.length - 1;
        while (missingEndRows > 0 && codewords[row3] == null) {
            missingEndRows--;
            row3--;
        }
        return rowIndicatorColumn.getBoundingBox().addMissingRows(missingStartRows, missingEndRows, rowIndicatorColumn.isLeft());
    }

    private static int getMax(int[] values) {
        int maxValue = -1;
        for (int value : values) {
            maxValue = Math.max(maxValue, value);
        }
        return maxValue;
    }

    private static BarcodeMetadata getBarcodeMetadata(DetectionResultRowIndicatorColumn leftRowIndicatorColumn, DetectionResultRowIndicatorColumn rightRowIndicatorColumn) {
        BarcodeMetadata leftBarcodeMetadata;
        BarcodeMetadata rightBarcodeMetadata;
        if (leftRowIndicatorColumn == null || (leftBarcodeMetadata = leftRowIndicatorColumn.getBarcodeMetadata()) == null) {
            if (rightRowIndicatorColumn == null) {
                return null;
            }
            return rightRowIndicatorColumn.getBarcodeMetadata();
        } else if (rightRowIndicatorColumn == null || (rightBarcodeMetadata = rightRowIndicatorColumn.getBarcodeMetadata()) == null || leftBarcodeMetadata.getColumnCount() == rightBarcodeMetadata.getColumnCount() || leftBarcodeMetadata.getErrorCorrectionLevel() == rightBarcodeMetadata.getErrorCorrectionLevel() || leftBarcodeMetadata.getRowCount() == rightBarcodeMetadata.getRowCount()) {
            return leftBarcodeMetadata;
        } else {
            return null;
        }
    }

    private static DetectionResultRowIndicatorColumn getRowIndicatorColumn(BitMatrix image, BoundingBox boundingBox, ResultPoint startPoint, boolean leftToRight, int minCodewordWidth, int maxCodewordWidth) {
        DetectionResultRowIndicatorColumn rowIndicatorColumn = new DetectionResultRowIndicatorColumn(boundingBox, leftToRight);
        int i = 0;
        while (i < 2) {
            int increment = i == 0 ? 1 : -1;
            int startColumn = (int) startPoint.getX();
            int imageRow = (int) startPoint.getY();
            while (imageRow <= boundingBox.getMaxY() && imageRow >= boundingBox.getMinY()) {
                Codeword codeword = detectCodeword(image, 0, image.getWidth(), leftToRight, startColumn, imageRow, minCodewordWidth, maxCodewordWidth);
                if (codeword != null) {
                    rowIndicatorColumn.setCodeword(imageRow, codeword);
                    if (leftToRight) {
                        startColumn = codeword.getStartX();
                    } else {
                        startColumn = codeword.getEndX();
                    }
                }
                imageRow += increment;
            }
            i++;
        }
        return rowIndicatorColumn;
    }

    private static void adjustCodewordCount(DetectionResult detectionResult, BarcodeValue[][] barcodeMatrix) throws NotFoundException {
        BarcodeValue barcodeMatrix01 = barcodeMatrix[0][1];
        int[] numberOfCodewords = barcodeMatrix01.getValue();
        int calculatedNumberOfCodewords = (detectionResult.getBarcodeColumnCount() * detectionResult.getBarcodeRowCount()) - getNumberOfECCodeWords(detectionResult.getBarcodeECLevel());
        if (numberOfCodewords.length == 0) {
            if (calculatedNumberOfCodewords <= 0 || calculatedNumberOfCodewords > 928) {
                throw NotFoundException.getNotFoundInstance();
            }
            barcodeMatrix01.setValue(calculatedNumberOfCodewords);
        } else if (numberOfCodewords[0] != calculatedNumberOfCodewords) {
            barcodeMatrix01.setValue(calculatedNumberOfCodewords);
        }
    }

    private static DecoderResult createDecoderResult(DetectionResult detectionResult) throws FormatException, ChecksumException, NotFoundException {
        BarcodeValue[][] barcodeMatrix = createBarcodeMatrix(detectionResult);
        adjustCodewordCount(detectionResult, barcodeMatrix);
        Collection<Integer> erasures = new ArrayList<>();
        int[] codewords = new int[(detectionResult.getBarcodeRowCount() * detectionResult.getBarcodeColumnCount())];
        List<int[]> ambiguousIndexValuesList = new ArrayList<>();
        List<Integer> ambiguousIndexesList = new ArrayList<>();
        for (int row = 0; row < detectionResult.getBarcodeRowCount(); row++) {
            for (int column = 0; column < detectionResult.getBarcodeColumnCount(); column++) {
                int[] values = barcodeMatrix[row][column + 1].getValue();
                int codewordIndex = (detectionResult.getBarcodeColumnCount() * row) + column;
                if (values.length == 0) {
                    erasures.add(Integer.valueOf(codewordIndex));
                } else if (values.length == 1) {
                    codewords[codewordIndex] = values[0];
                } else {
                    ambiguousIndexesList.add(Integer.valueOf(codewordIndex));
                    ambiguousIndexValuesList.add(values);
                }
            }
        }
        int[][] ambiguousIndexValues = new int[ambiguousIndexValuesList.size()][];
        for (int i = 0; i < ambiguousIndexValues.length; i++) {
            ambiguousIndexValues[i] = (int[]) ambiguousIndexValuesList.get(i);
        }
        return createDecoderResultFromAmbiguousValues(detectionResult.getBarcodeECLevel(), codewords, PDF417Common.toIntArray(erasures), PDF417Common.toIntArray(ambiguousIndexesList), ambiguousIndexValues);
    }

    private static DecoderResult createDecoderResultFromAmbiguousValues(int ecLevel, int[] codewords, int[] erasureArray, int[] ambiguousIndexes, int[][] ambiguousIndexValues) throws FormatException, ChecksumException {
        int[] ambiguousIndexCount = new int[ambiguousIndexes.length];
        int tries = 100;
        while (true) {
            int tries2 = tries;
            tries = tries2 - 1;
            if (tries2 > 0) {
                for (int i = 0; i < ambiguousIndexCount.length; i++) {
                    codewords[ambiguousIndexes[i]] = ambiguousIndexValues[i][ambiguousIndexCount[i]];
                }
                try {
                    return decodeCodewords(codewords, ecLevel, erasureArray);
                } catch (ChecksumException e) {
                    if (ambiguousIndexCount.length != 0) {
                        int i2 = 0;
                        while (true) {
                            if (i2 >= ambiguousIndexCount.length) {
                                break;
                            } else if (ambiguousIndexCount[i2] < ambiguousIndexValues[i2].length - 1) {
                                ambiguousIndexCount[i2] = ambiguousIndexCount[i2] + 1;
                                break;
                            } else {
                                ambiguousIndexCount[i2] = 0;
                                if (i2 == ambiguousIndexCount.length - 1) {
                                    throw ChecksumException.getChecksumInstance();
                                }
                                i2++;
                            }
                        }
                    } else {
                        throw ChecksumException.getChecksumInstance();
                    }
                }
            } else {
                throw ChecksumException.getChecksumInstance();
            }
        }
    }

    private static BarcodeValue[][] createBarcodeMatrix(DetectionResult detectionResult) {
        int rowNumber;
        BarcodeValue[][] barcodeMatrix = (BarcodeValue[][]) Array.newInstance(BarcodeValue.class, detectionResult.getBarcodeRowCount(), detectionResult.getBarcodeColumnCount() + 2);
        for (int row = 0; row < barcodeMatrix.length; row++) {
            for (int column = 0; column < barcodeMatrix[row].length; column++) {
                barcodeMatrix[row][column] = new BarcodeValue();
            }
        }
        int column2 = 0;
        DetectionResultColumn[] detectionResultColumns = detectionResult.getDetectionResultColumns();
        for (DetectionResultColumn detectionResultColumn : detectionResultColumns) {
            if (detectionResultColumn != null) {
                Codeword[] codewords = detectionResultColumn.getCodewords();
                for (Codeword codeword : codewords) {
                    if (codeword != null && (rowNumber = codeword.getRowNumber()) >= 0 && rowNumber < barcodeMatrix.length) {
                        barcodeMatrix[rowNumber][column2].setValue(codeword.getValue());
                    }
                }
            }
            column2++;
        }
        return barcodeMatrix;
    }

    private static boolean isValidBarcodeColumn(DetectionResult detectionResult, int barcodeColumn) {
        return barcodeColumn >= 0 && barcodeColumn <= detectionResult.getBarcodeColumnCount() + 1;
    }

    private static int getStartColumn(DetectionResult detectionResult, int barcodeColumn, int imageRow, boolean leftToRight) {
        int offset = leftToRight ? 1 : -1;
        Codeword codeword = null;
        if (isValidBarcodeColumn(detectionResult, barcodeColumn - offset)) {
            codeword = detectionResult.getDetectionResultColumn(barcodeColumn - offset).getCodeword(imageRow);
        }
        if (codeword == null) {
            Codeword codeword2 = detectionResult.getDetectionResultColumn(barcodeColumn).getCodewordNearby(imageRow);
            if (codeword2 != null) {
                return leftToRight ? codeword2.getStartX() : codeword2.getEndX();
            }
            if (isValidBarcodeColumn(detectionResult, barcodeColumn - offset)) {
                codeword2 = detectionResult.getDetectionResultColumn(barcodeColumn - offset).getCodewordNearby(imageRow);
            }
            if (codeword2 != null) {
                return leftToRight ? codeword2.getEndX() : codeword2.getStartX();
            }
            int skippedColumns = 0;
            while (isValidBarcodeColumn(detectionResult, barcodeColumn - offset)) {
                barcodeColumn -= offset;
                Codeword[] codewords = detectionResult.getDetectionResultColumn(barcodeColumn).getCodewords();
                for (Codeword previousRowCodeword : codewords) {
                    if (previousRowCodeword != null) {
                        return (leftToRight ? previousRowCodeword.getEndX() : previousRowCodeword.getStartX()) + (offset * skippedColumns * (previousRowCodeword.getEndX() - previousRowCodeword.getStartX()));
                    }
                }
                skippedColumns++;
            }
            return leftToRight ? detectionResult.getBoundingBox().getMinX() : detectionResult.getBoundingBox().getMaxX();
        } else if (leftToRight) {
            return codeword.getEndX();
        } else {
            return codeword.getStartX();
        }
    }

    private static Codeword detectCodeword(BitMatrix image, int minColumn, int maxColumn, boolean leftToRight, int startColumn, int imageRow, int minCodewordWidth, int maxCodewordWidth) {
        int endColumn;
        int decodedValue;
        int codeword;
        int startColumn2 = adjustCodewordStartColumn(image, minColumn, maxColumn, leftToRight, startColumn, imageRow);
        int[] moduleBitCount = getModuleBitCount(image, minColumn, maxColumn, leftToRight, startColumn2, imageRow);
        if (moduleBitCount == null) {
            return null;
        }
        int codewordBitCount = MathUtils.sum(moduleBitCount);
        if (leftToRight) {
            endColumn = startColumn2 + codewordBitCount;
        } else {
            for (int i = 0; i < moduleBitCount.length / 2; i++) {
                int tmpCount = moduleBitCount[i];
                moduleBitCount[i] = moduleBitCount[(moduleBitCount.length - 1) - i];
                moduleBitCount[(moduleBitCount.length - 1) - i] = tmpCount;
            }
            endColumn = startColumn2;
            startColumn2 -= codewordBitCount;
        }
        if (checkCodewordSkew(codewordBitCount, minCodewordWidth, maxCodewordWidth) && (codeword = PDF417Common.getCodeword((decodedValue = PDF417CodewordDecoder.getDecodedValue(moduleBitCount)))) != -1) {
            return new Codeword(startColumn2, endColumn, getCodewordBucketNumber(decodedValue), codeword);
        }
        return null;
    }

    private static int[] getModuleBitCount(BitMatrix image, int minColumn, int maxColumn, boolean leftToRight, int startColumn, int imageRow) {
        int imageColumn = startColumn;
        int[] moduleBitCount = new int[8];
        int moduleNumber = 0;
        int increment = leftToRight ? 1 : -1;
        boolean previousPixelValue = leftToRight;
        while (true) {
            if (!leftToRight) {
                if (imageColumn < minColumn) {
                    break;
                }
            } else if (imageColumn >= maxColumn) {
                break;
            }
            if (moduleNumber >= 8) {
                break;
            } else if (image.get(imageColumn, imageRow) == previousPixelValue) {
                moduleBitCount[moduleNumber] = moduleBitCount[moduleNumber] + 1;
                imageColumn += increment;
            } else {
                moduleNumber++;
                previousPixelValue = !previousPixelValue;
            }
        }
        if (moduleNumber == 8) {
            return moduleBitCount;
        }
        if (!leftToRight) {
            maxColumn = minColumn;
        }
        if (imageColumn == maxColumn && moduleNumber == 7) {
            return moduleBitCount;
        }
        return null;
    }

    private static int getNumberOfECCodeWords(int barcodeECLevel) {
        return 2 << barcodeECLevel;
    }

    private static int adjustCodewordStartColumn(BitMatrix image, int minColumn, int maxColumn, boolean leftToRight, int codewordStartColumn, int imageRow) {
        int increment;
        int correctedStartColumn = codewordStartColumn;
        if (leftToRight) {
            increment = -1;
        } else {
            increment = 1;
        }
        int i = 0;
        int correctedStartColumn2 = correctedStartColumn;
        while (i < 2) {
            int correctedStartColumn3 = correctedStartColumn2;
            while (true) {
                if (!leftToRight) {
                    if (correctedStartColumn3 >= maxColumn) {
                        break;
                    }
                } else if (correctedStartColumn3 < minColumn) {
                    break;
                }
                if (leftToRight != image.get(correctedStartColumn3, imageRow)) {
                    break;
                } else if (Math.abs(codewordStartColumn - correctedStartColumn3) > 2) {
                    return codewordStartColumn;
                } else {
                    correctedStartColumn3 += increment;
                }
            }
            increment = -increment;
            if (!leftToRight) {
                leftToRight = true;
            } else {
                leftToRight = false;
            }
            i++;
            correctedStartColumn2 = correctedStartColumn3;
        }
        return correctedStartColumn2;
    }

    private static boolean checkCodewordSkew(int codewordSize, int minCodewordWidth, int maxCodewordWidth) {
        return minCodewordWidth + -2 <= codewordSize && codewordSize <= maxCodewordWidth + 2;
    }

    private static DecoderResult decodeCodewords(int[] codewords, int ecLevel, int[] erasures) throws FormatException, ChecksumException {
        if (codewords.length == 0) {
            throw FormatException.getFormatInstance();
        }
        int numECCodewords = 1 << (ecLevel + 1);
        int correctedErrorsCount = correctErrors(codewords, erasures, numECCodewords);
        verifyCodewordCount(codewords, numECCodewords);
        DecoderResult decoderResult = DecodedBitStreamParser.decode(codewords, String.valueOf(ecLevel));
        decoderResult.setErrorsCorrected(Integer.valueOf(correctedErrorsCount));
        decoderResult.setErasures(Integer.valueOf(erasures.length));
        return decoderResult;
    }

    private static int correctErrors(int[] codewords, int[] erasures, int numECCodewords) throws ChecksumException {
        if ((erasures == null || erasures.length <= (numECCodewords / 2) + 3) && numECCodewords >= 0 && numECCodewords <= 512) {
            return errorCorrection.decode(codewords, numECCodewords, erasures);
        }
        throw ChecksumException.getChecksumInstance();
    }

    private static void verifyCodewordCount(int[] codewords, int numECCodewords) throws FormatException {
        if (codewords.length < 4) {
            throw FormatException.getFormatInstance();
        }
        int numberOfCodewords = codewords[0];
        if (numberOfCodewords > codewords.length) {
            throw FormatException.getFormatInstance();
        } else if (numberOfCodewords != 0) {
        } else {
            if (numECCodewords < codewords.length) {
                codewords[0] = codewords.length - numECCodewords;
                return;
            }
            throw FormatException.getFormatInstance();
        }
    }

    private static int[] getBitCountForCodeword(int codeword) {
        int[] result = new int[8];
        int previousValue = 0;
        int i = 7;
        while (true) {
            if ((codeword & 1) != previousValue) {
                previousValue = codeword & 1;
                i--;
                if (i < 0) {
                    return result;
                }
            }
            result[i] = result[i] + 1;
            codeword >>= 1;
        }
    }

    private static int getCodewordBucketNumber(int codeword) {
        return getCodewordBucketNumber(getBitCountForCodeword(codeword));
    }

    private static int getCodewordBucketNumber(int[] moduleBitCount) {
        return ((((moduleBitCount[0] - moduleBitCount[2]) + moduleBitCount[4]) - moduleBitCount[6]) + 9) % 9;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x005d, code lost:
        r5 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x005e, code lost:
        r6 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0082, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0083, code lost:
        r5 = r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String toString(com.google.zxing.pdf417.decoder.BarcodeValue[][] r10) {
        /*
            r6 = 0
            java.util.Formatter r2 = new java.util.Formatter
            r2.<init>()
            r3 = 0
        L_0x0007:
            int r4 = r10.length     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            if (r3 >= r4) goto L_0x0071
            java.lang.String r4 = "Row %2d: "
            r5 = 1
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            r7 = 0
            java.lang.Integer r8 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            r5[r7] = r8     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            r2.format(r4, r5)     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            r1 = 0
        L_0x001b:
            r4 = r10[r3]     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            int r4 = r4.length     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            if (r1 >= r4) goto L_0x0065
            r4 = r10[r3]     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            r0 = r4[r1]     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            int[] r4 = r0.getValue()     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            int r4 = r4.length     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            if (r4 != 0) goto L_0x0035
            java.lang.String r4 = "        "
            r5 = 0
            r2.format(r4, r5)     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
        L_0x0032:
            int r1 = r1 + 1
            goto L_0x001b
        L_0x0035:
            java.lang.String r4 = "%4d(%2d)"
            r5 = 2
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            r7 = 0
            int[] r8 = r0.getValue()     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            r9 = 0
            r8 = r8[r9]     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            r5[r7] = r8     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            r7 = 1
            int[] r8 = r0.getValue()     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            r9 = 0
            r8 = r8[r9]     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            java.lang.Integer r8 = r0.getConfidence(r8)     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            r5[r7] = r8     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            r2.format(r4, r5)     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            goto L_0x0032
        L_0x005b:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x005d }
        L_0x005d:
            r5 = move-exception
            r6 = r4
        L_0x005f:
            if (r6 == 0) goto L_0x007e
            r2.close()     // Catch:{ Throwable -> 0x0079 }
        L_0x0064:
            throw r5
        L_0x0065:
            java.lang.String r4 = "%n"
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            r2.format(r4, r5)     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            int r3 = r3 + 1
            goto L_0x0007
        L_0x0071:
            java.lang.String r4 = r2.toString()     // Catch:{ Throwable -> 0x005b, all -> 0x0082 }
            r2.close()
            return r4
        L_0x0079:
            r4 = move-exception
            r6.addSuppressed(r4)
            goto L_0x0064
        L_0x007e:
            r2.close()
            goto L_0x0064
        L_0x0082:
            r4 = move-exception
            r5 = r4
            goto L_0x005f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.PDF417ScanningDecoder.toString(com.google.zxing.pdf417.decoder.BarcodeValue[][]):java.lang.String");
    }
}
