package com.google.zxing.pdf417.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.pdf417.PDF417ResultMetadata;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

final class DecodedBitStreamParser {
    private static final int AL = 28;
    private static final int AS = 27;
    private static final int BEGIN_MACRO_PDF417_CONTROL_BLOCK = 928;
    private static final int BEGIN_MACRO_PDF417_OPTIONAL_FIELD = 923;
    private static final int BYTE_COMPACTION_MODE_LATCH = 901;
    private static final int BYTE_COMPACTION_MODE_LATCH_6 = 924;
    private static final int ECI_CHARSET = 927;
    private static final int ECI_GENERAL_PURPOSE = 926;
    private static final int ECI_USER_DEFINED = 925;
    private static final BigInteger[] EXP900;
    private static final int LL = 27;
    private static final int MACRO_PDF417_TERMINATOR = 922;
    private static final int MAX_NUMERIC_CODEWORDS = 15;
    private static final char[] MIXED_CHARS = "0123456789&\r\t,:#-.$/+%*=^".toCharArray();
    private static final int ML = 28;
    private static final int MODE_SHIFT_TO_BYTE_COMPACTION_MODE = 913;
    private static final int NUMBER_OF_SEQUENCE_CODEWORDS = 2;
    private static final int NUMERIC_COMPACTION_MODE_LATCH = 902;
    private static final int PAL = 29;
    private static final int PL = 25;
    private static final int PS = 29;
    private static final char[] PUNCT_CHARS = ";<>@[\\]_`~!\r\t,:\n-.$/\"|*()?{}'".toCharArray();
    private static final int TEXT_COMPACTION_MODE_LATCH = 900;

    private enum Mode {
        ALPHA,
        LOWER,
        MIXED,
        PUNCT,
        ALPHA_SHIFT,
        PUNCT_SHIFT
    }

    static {
        BigInteger[] bigIntegerArr = new BigInteger[16];
        EXP900 = bigIntegerArr;
        bigIntegerArr[0] = BigInteger.ONE;
        BigInteger nineHundred = BigInteger.valueOf(900);
        EXP900[1] = nineHundred;
        for (int i = 2; i < EXP900.length; i++) {
            EXP900[i] = EXP900[i - 1].multiply(nineHundred);
        }
    }

    private DecodedBitStreamParser() {
    }

    static DecoderResult decode(int[] codewords, String ecLevel) throws FormatException {
        int codeIndex;
        StringBuilder result = new StringBuilder(codewords.length << 1);
        Charset encoding = StandardCharsets.ISO_8859_1;
        int codeIndex2 = 1 + 1;
        int code = codewords[1];
        PDF417ResultMetadata resultMetadata = new PDF417ResultMetadata();
        while (codeIndex2 < codewords[0]) {
            switch (code) {
                case TEXT_COMPACTION_MODE_LATCH /*900*/:
                    codeIndex = textCompaction(codewords, codeIndex2, result);
                    break;
                case BYTE_COMPACTION_MODE_LATCH /*901*/:
                case BYTE_COMPACTION_MODE_LATCH_6 /*924*/:
                    codeIndex = byteCompaction(code, codewords, encoding, codeIndex2, result);
                    break;
                case NUMERIC_COMPACTION_MODE_LATCH /*902*/:
                    codeIndex = numericCompaction(codewords, codeIndex2, result);
                    break;
                case MODE_SHIFT_TO_BYTE_COMPACTION_MODE /*913*/:
                    result.append((char) codewords[codeIndex2]);
                    codeIndex = codeIndex2 + 1;
                    break;
                case MACRO_PDF417_TERMINATOR /*922*/:
                case BEGIN_MACRO_PDF417_OPTIONAL_FIELD /*923*/:
                    throw FormatException.getFormatInstance();
                case ECI_USER_DEFINED /*925*/:
                    codeIndex = codeIndex2 + 1;
                    break;
                case ECI_GENERAL_PURPOSE /*926*/:
                    codeIndex = codeIndex2 + 2;
                    break;
                case ECI_CHARSET /*927*/:
                    encoding = Charset.forName(CharacterSetECI.getCharacterSetECIByValue(codewords[codeIndex2]).name());
                    codeIndex = codeIndex2 + 1;
                    break;
                case 928:
                    codeIndex = decodeMacroBlock(codewords, codeIndex2, resultMetadata);
                    break;
                default:
                    codeIndex = textCompaction(codewords, codeIndex2 - 1, result);
                    break;
            }
            if (codeIndex < codewords.length) {
                code = codewords[codeIndex];
                codeIndex2 = codeIndex + 1;
            } else {
                throw FormatException.getFormatInstance();
            }
        }
        if (result.length() == 0) {
            throw FormatException.getFormatInstance();
        }
        DecoderResult decoderResult = new DecoderResult(null, result.toString(), null, ecLevel);
        decoderResult.setOther(resultMetadata);
        return decoderResult;
    }

    private static int decodeMacroBlock(int[] codewords, int codeIndex, PDF417ResultMetadata resultMetadata) throws FormatException {
        if (codeIndex + 2 > codewords[0]) {
            throw FormatException.getFormatInstance();
        }
        int[] segmentIndexArray = new int[2];
        int i = 0;
        while (i < 2) {
            segmentIndexArray[i] = codewords[codeIndex];
            i++;
            codeIndex++;
        }
        resultMetadata.setSegmentIndex(Integer.parseInt(decodeBase900toBase10(segmentIndexArray, 2)));
        StringBuilder fileId = new StringBuilder();
        int codeIndex2 = textCompaction(codewords, codeIndex, fileId);
        resultMetadata.setFileId(fileId.toString());
        switch (codewords[codeIndex2]) {
            case MACRO_PDF417_TERMINATOR /*922*/:
                resultMetadata.setLastSegment(true);
                return codeIndex2 + 1;
            case BEGIN_MACRO_PDF417_OPTIONAL_FIELD /*923*/:
                int codeIndex3 = codeIndex2 + 1;
                int[] additionalOptionCodeWords = new int[(codewords[0] - codeIndex3)];
                int additionalOptionCodeWordsIndex = 0;
                boolean end = false;
                while (codeIndex3 < codewords[0] && !end) {
                    int codeIndex4 = codeIndex3 + 1;
                    int code = codewords[codeIndex3];
                    if (code < TEXT_COMPACTION_MODE_LATCH) {
                        additionalOptionCodeWords[additionalOptionCodeWordsIndex] = code;
                        additionalOptionCodeWordsIndex++;
                        codeIndex3 = codeIndex4;
                    } else {
                        switch (code) {
                            default:
                                throw FormatException.getFormatInstance();
                            case MACRO_PDF417_TERMINATOR /*922*/:
                                resultMetadata.setLastSegment(true);
                                codeIndex3 = codeIndex4 + 1;
                                end = true;
                                continue;
                        }
                    }
                }
                resultMetadata.setOptionalData(Arrays.copyOf(additionalOptionCodeWords, additionalOptionCodeWordsIndex));
                return codeIndex3;
            default:
                return codeIndex2;
        }
    }

    private static int textCompaction(int[] codewords, int codeIndex, StringBuilder result) {
        int[] textCompactionData = new int[((codewords[0] - codeIndex) << 1)];
        int[] byteCompactionData = new int[((codewords[0] - codeIndex) << 1)];
        int index = 0;
        boolean end = false;
        while (codeIndex < codewords[0] && !end) {
            int codeIndex2 = codeIndex + 1;
            int code = codewords[codeIndex];
            if (code < TEXT_COMPACTION_MODE_LATCH) {
                textCompactionData[index] = code / 30;
                textCompactionData[index + 1] = code % 30;
                index += 2;
                codeIndex = codeIndex2;
            } else {
                switch (code) {
                    case TEXT_COMPACTION_MODE_LATCH /*900*/:
                        textCompactionData[index] = TEXT_COMPACTION_MODE_LATCH;
                        index++;
                        codeIndex = codeIndex2;
                        continue;
                    case BYTE_COMPACTION_MODE_LATCH /*901*/:
                    case NUMERIC_COMPACTION_MODE_LATCH /*902*/:
                    case MACRO_PDF417_TERMINATOR /*922*/:
                    case BEGIN_MACRO_PDF417_OPTIONAL_FIELD /*923*/:
                    case BYTE_COMPACTION_MODE_LATCH_6 /*924*/:
                    case 928:
                        codeIndex = codeIndex2 - 1;
                        end = true;
                        continue;
                    case MODE_SHIFT_TO_BYTE_COMPACTION_MODE /*913*/:
                        textCompactionData[index] = MODE_SHIFT_TO_BYTE_COMPACTION_MODE;
                        codeIndex = codeIndex2 + 1;
                        byteCompactionData[index] = codewords[codeIndex2];
                        index++;
                        continue;
                    default:
                        codeIndex = codeIndex2;
                        continue;
                }
            }
        }
        decodeTextCompaction(textCompactionData, byteCompactionData, index, result);
        return codeIndex;
    }

    private static void decodeTextCompaction(int[] textCompactionData, int[] byteCompactionData, int length, StringBuilder result) {
        Mode subMode = Mode.ALPHA;
        Mode priorToShiftMode = Mode.ALPHA;
        for (int i = 0; i < length; i++) {
            int subModeCh = textCompactionData[i];
            char ch = 0;
            switch (subMode) {
                case ALPHA:
                    if (subModeCh >= 26) {
                        switch (subModeCh) {
                            case 26:
                                ch = ' ';
                                break;
                            case 27:
                                subMode = Mode.LOWER;
                                break;
                            case 28:
                                subMode = Mode.MIXED;
                                break;
                            case 29:
                                priorToShiftMode = subMode;
                                subMode = Mode.PUNCT_SHIFT;
                                break;
                            case TEXT_COMPACTION_MODE_LATCH /*900*/:
                                subMode = Mode.ALPHA;
                                break;
                            case MODE_SHIFT_TO_BYTE_COMPACTION_MODE /*913*/:
                                result.append((char) byteCompactionData[i]);
                                break;
                        }
                    } else {
                        ch = (char) (subModeCh + 65);
                        break;
                    }
                case LOWER:
                    if (subModeCh >= 26) {
                        switch (subModeCh) {
                            case 26:
                                ch = ' ';
                                break;
                            case 27:
                                priorToShiftMode = subMode;
                                subMode = Mode.ALPHA_SHIFT;
                                break;
                            case 28:
                                subMode = Mode.MIXED;
                                break;
                            case 29:
                                priorToShiftMode = subMode;
                                subMode = Mode.PUNCT_SHIFT;
                                break;
                            case TEXT_COMPACTION_MODE_LATCH /*900*/:
                                subMode = Mode.ALPHA;
                                break;
                            case MODE_SHIFT_TO_BYTE_COMPACTION_MODE /*913*/:
                                result.append((char) byteCompactionData[i]);
                                break;
                        }
                    } else {
                        ch = (char) (subModeCh + 97);
                        break;
                    }
                case MIXED:
                    if (subModeCh >= 25) {
                        switch (subModeCh) {
                            case 25:
                                subMode = Mode.PUNCT;
                                break;
                            case 26:
                                ch = ' ';
                                break;
                            case 27:
                                subMode = Mode.LOWER;
                                break;
                            case 28:
                                subMode = Mode.ALPHA;
                                break;
                            case 29:
                                priorToShiftMode = subMode;
                                subMode = Mode.PUNCT_SHIFT;
                                break;
                            case TEXT_COMPACTION_MODE_LATCH /*900*/:
                                subMode = Mode.ALPHA;
                                break;
                            case MODE_SHIFT_TO_BYTE_COMPACTION_MODE /*913*/:
                                result.append((char) byteCompactionData[i]);
                                break;
                        }
                    } else {
                        ch = MIXED_CHARS[subModeCh];
                        break;
                    }
                case PUNCT:
                    if (subModeCh >= 29) {
                        switch (subModeCh) {
                            case 29:
                                subMode = Mode.ALPHA;
                                break;
                            case TEXT_COMPACTION_MODE_LATCH /*900*/:
                                subMode = Mode.ALPHA;
                                break;
                            case MODE_SHIFT_TO_BYTE_COMPACTION_MODE /*913*/:
                                result.append((char) byteCompactionData[i]);
                                break;
                        }
                    } else {
                        ch = PUNCT_CHARS[subModeCh];
                        break;
                    }
                    break;
                case ALPHA_SHIFT:
                    subMode = priorToShiftMode;
                    if (subModeCh >= 26) {
                        switch (subModeCh) {
                            case 26:
                                ch = ' ';
                                break;
                            case TEXT_COMPACTION_MODE_LATCH /*900*/:
                                subMode = Mode.ALPHA;
                                break;
                        }
                    } else {
                        ch = (char) (subModeCh + 65);
                        break;
                    }
                case PUNCT_SHIFT:
                    subMode = priorToShiftMode;
                    if (subModeCh >= 29) {
                        switch (subModeCh) {
                            case 29:
                                subMode = Mode.ALPHA;
                                break;
                            case TEXT_COMPACTION_MODE_LATCH /*900*/:
                                subMode = Mode.ALPHA;
                                break;
                            case MODE_SHIFT_TO_BYTE_COMPACTION_MODE /*913*/:
                                result.append((char) byteCompactionData[i]);
                                break;
                        }
                    } else {
                        ch = PUNCT_CHARS[subModeCh];
                        break;
                    }
                    break;
            }
            if (ch != 0) {
                result.append(ch);
            }
        }
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 151 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int byteCompaction(int r18, int[] r19, java.nio.charset.Charset r20, int r21, java.lang.StringBuilder r22) {
        /*
            java.io.ByteArrayOutputStream r7 = new java.io.ByteArrayOutputStream
            r7.<init>()
            r5 = 0
            r12 = 0
            r8 = 0
            switch(r18) {
                case 901: goto L_0x001d;
                case 924: goto L_0x0084;
                default: goto L_0x000c;
            }
        L_0x000c:
            java.lang.String r14 = new java.lang.String
            byte[] r15 = r7.toByteArray()
            r0 = r20
            r14.<init>(r15, r0)
            r0 = r22
            r0.append(r14)
            return r21
        L_0x001d:
            r14 = 6
            int[] r2 = new int[r14]
            int r4 = r21 + 1
            r11 = r19[r21]
            r21 = r4
        L_0x0026:
            r14 = 0
            r14 = r19[r14]
            r0 = r21
            if (r0 >= r14) goto L_0x0065
            if (r8 != 0) goto L_0x0065
            int r6 = r5 + 1
            r2[r5] = r11
            r14 = 900(0x384, double:4.447E-321)
            long r14 = r14 * r12
            long r0 = (long) r11
            r16 = r0
            long r12 = r14 + r16
            int r4 = r21 + 1
            r11 = r19[r21]
            switch(r11) {
                case 900: goto L_0x005a;
                case 901: goto L_0x005a;
                case 902: goto L_0x005a;
                case 922: goto L_0x005a;
                case 923: goto L_0x005a;
                case 924: goto L_0x005a;
                case 928: goto L_0x005a;
                default: goto L_0x0042;
            }
        L_0x0042:
            int r14 = r6 % 5
            if (r14 != 0) goto L_0x00c3
            if (r6 <= 0) goto L_0x00c3
            r10 = 0
        L_0x0049:
            r14 = 6
            if (r10 >= r14) goto L_0x005f
            int r14 = 5 - r10
            int r14 = r14 * 8
            long r14 = r12 >> r14
            int r14 = (int) r14
            byte r14 = (byte) r14
            r7.write(r14)
            int r10 = r10 + 1
            goto L_0x0049
        L_0x005a:
            int r21 = r4 + -1
            r8 = 1
            r5 = r6
            goto L_0x0026
        L_0x005f:
            r12 = 0
            r5 = 0
            r21 = r4
            goto L_0x0026
        L_0x0065:
            r14 = 0
            r14 = r19[r14]
            r0 = r21
            if (r0 != r14) goto L_0x0075
            r14 = 900(0x384, float:1.261E-42)
            if (r11 >= r14) goto L_0x0075
            int r6 = r5 + 1
            r2[r5] = r11
            r5 = r6
        L_0x0075:
            r9 = 0
        L_0x0076:
            if (r9 >= r5) goto L_0x000c
            r14 = r2[r9]
            byte r14 = (byte) r14
            r7.write(r14)
            int r9 = r9 + 1
            goto L_0x0076
        L_0x0081:
            r12 = 0
            r5 = 0
        L_0x0084:
            r14 = 0
            r14 = r19[r14]
            r0 = r21
            if (r0 >= r14) goto L_0x000c
            if (r8 != 0) goto L_0x000c
            int r4 = r21 + 1
            r3 = r19[r21]
            r14 = 900(0x384, float:1.261E-42)
            if (r3 >= r14) goto L_0x00b9
            int r5 = r5 + 1
            r14 = 900(0x384, double:4.447E-321)
            long r14 = r14 * r12
            long r0 = (long) r3
            r16 = r0
            long r12 = r14 + r16
            r21 = r4
        L_0x00a1:
            int r14 = r5 % 5
            if (r14 != 0) goto L_0x0084
            if (r5 <= 0) goto L_0x0084
            r10 = 0
        L_0x00a8:
            r14 = 6
            if (r10 >= r14) goto L_0x0081
            int r14 = 5 - r10
            int r14 = r14 * 8
            long r14 = r12 >> r14
            int r14 = (int) r14
            byte r14 = (byte) r14
            r7.write(r14)
            int r10 = r10 + 1
            goto L_0x00a8
        L_0x00b9:
            switch(r3) {
                case 900: goto L_0x00bf;
                case 901: goto L_0x00bf;
                case 902: goto L_0x00bf;
                case 922: goto L_0x00bf;
                case 923: goto L_0x00bf;
                case 924: goto L_0x00bf;
                case 928: goto L_0x00bf;
                default: goto L_0x00bc;
            }
        L_0x00bc:
            r21 = r4
            goto L_0x00a1
        L_0x00bf:
            int r21 = r4 + -1
            r8 = 1
            goto L_0x00a1
        L_0x00c3:
            r5 = r6
            r21 = r4
            goto L_0x0026
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DecodedBitStreamParser.byteCompaction(int, int[], java.nio.charset.Charset, int, java.lang.StringBuilder):int");
    }

    private static int numericCompaction(int[] codewords, int codeIndex, StringBuilder result) throws FormatException {
        int count = 0;
        boolean end = false;
        int[] numericCodewords = new int[15];
        while (codeIndex < codewords[0] && !end) {
            int codeIndex2 = codeIndex + 1;
            int code = codewords[codeIndex];
            if (codeIndex2 == codewords[0]) {
                end = true;
            }
            if (code >= TEXT_COMPACTION_MODE_LATCH) {
                switch (code) {
                    case TEXT_COMPACTION_MODE_LATCH /*900*/:
                    case BYTE_COMPACTION_MODE_LATCH /*901*/:
                    case MACRO_PDF417_TERMINATOR /*922*/:
                    case BEGIN_MACRO_PDF417_OPTIONAL_FIELD /*923*/:
                    case BYTE_COMPACTION_MODE_LATCH_6 /*924*/:
                    case 928:
                        codeIndex = codeIndex2 - 1;
                        end = true;
                        break;
                    default:
                        codeIndex = codeIndex2;
                        break;
                }
            } else {
                numericCodewords[count] = code;
                count++;
                codeIndex = codeIndex2;
            }
            if ((count % 15 == 0 || code == NUMERIC_COMPACTION_MODE_LATCH || end) && count > 0) {
                result.append(decodeBase900toBase10(numericCodewords, count));
                count = 0;
            }
        }
        return codeIndex;
    }

    private static String decodeBase900toBase10(int[] codewords, int count) throws FormatException {
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < count; i++) {
            result = result.add(EXP900[(count - i) - 1].multiply(BigInteger.valueOf((long) codewords[i])));
        }
        String resultString = result.toString();
        if (resultString.charAt(0) == '1') {
            return resultString.substring(1);
        }
        throw FormatException.getFormatInstance();
    }
}
