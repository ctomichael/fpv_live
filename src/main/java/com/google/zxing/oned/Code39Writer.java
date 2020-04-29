package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;
import kotlin.text.Typography;

public final class Code39Writer extends OneDimensionalCodeWriter {
    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) throws WriterException {
        if (format == BarcodeFormat.CODE_39) {
            return super.encode(contents, format, width, height, hints);
        }
        throw new IllegalArgumentException("Can only encode CODE_39, but got " + format);
    }

    public boolean[] encode(String contents) {
        int length = contents.length();
        if (length > 80) {
            throw new IllegalArgumentException("Requested contents should be less than 80 digits long, but got " + length);
        }
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            } else if ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%".indexOf(contents.charAt(i)) < 0) {
                contents = tryToConvertToExtendedMode(contents);
                length = contents.length();
                if (length > 80) {
                    throw new IllegalArgumentException("Requested contents should be less than 80 digits long, but got " + length + " (extended full ASCII mode)");
                }
            } else {
                i++;
            }
        }
        int[] widths = new int[9];
        int codeWidth = length + 25;
        for (int i2 = 0; i2 < length; i2++) {
            toIntArray(Code39Reader.CHARACTER_ENCODINGS["0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%".indexOf(contents.charAt(i2))], widths);
            for (int i3 = 0; i3 < 9; i3++) {
                codeWidth += widths[i3];
            }
        }
        boolean[] result = new boolean[codeWidth];
        toIntArray(148, widths);
        int pos = appendPattern(result, 0, widths, true);
        int[] narrowWhite = {1};
        int pos2 = pos + appendPattern(result, pos, narrowWhite, false);
        for (int i4 = 0; i4 < length; i4++) {
            toIntArray(Code39Reader.CHARACTER_ENCODINGS["0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%".indexOf(contents.charAt(i4))], widths);
            int pos3 = pos2 + appendPattern(result, pos2, widths, true);
            pos2 = pos3 + appendPattern(result, pos3, narrowWhite, false);
        }
        toIntArray(148, widths);
        appendPattern(result, pos2, widths, true);
        return result;
    }

    private static void toIntArray(int a, int[] toReturn) {
        for (int i = 0; i < 9; i++) {
            toReturn[i] = (a & (1 << (8 - i))) == 0 ? 1 : 2;
        }
    }

    private static String tryToConvertToExtendedMode(String contents) {
        int length = contents.length();
        StringBuilder extendedContent = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char character = contents.charAt(i);
            switch (character) {
                case 0:
                    extendedContent.append("%U");
                    break;
                case ' ':
                case '-':
                case '.':
                    extendedContent.append(character);
                    break;
                case '@':
                    extendedContent.append("%V");
                    break;
                case '`':
                    extendedContent.append("%W");
                    break;
                default:
                    if (character <= 0 || character >= 27) {
                        if (character <= 26 || character >= ' ') {
                            if ((character <= ' ' || character >= '-') && character != '/' && character != ':') {
                                if (character <= '/' || character >= ':') {
                                    if (character <= ':' || character >= '@') {
                                        if (character <= '@' || character >= '[') {
                                            if (character <= 'Z' || character >= '`') {
                                                if (character <= '`' || character >= '{') {
                                                    if (character > 'z' && character < 128) {
                                                        extendedContent.append('%');
                                                        extendedContent.append((char) ((character - '{') + 80));
                                                        break;
                                                    } else {
                                                        throw new IllegalArgumentException("Requested content contains a non-encodable character: '" + contents.charAt(i) + "'");
                                                    }
                                                } else {
                                                    extendedContent.append('+');
                                                    extendedContent.append((char) ((character - 'a') + 65));
                                                    break;
                                                }
                                            } else {
                                                extendedContent.append('%');
                                                extendedContent.append((char) ((character - '[') + 75));
                                                break;
                                            }
                                        } else {
                                            extendedContent.append((char) ((character - 'A') + 65));
                                            break;
                                        }
                                    } else {
                                        extendedContent.append('%');
                                        extendedContent.append((char) ((character - ';') + 70));
                                        break;
                                    }
                                } else {
                                    extendedContent.append((char) ((character - '0') + 48));
                                    break;
                                }
                            } else {
                                extendedContent.append('/');
                                extendedContent.append((char) ((character - '!') + 65));
                                break;
                            }
                        } else {
                            extendedContent.append('%');
                            extendedContent.append((char) ((character - 27) + 65));
                            break;
                        }
                    } else {
                        extendedContent.append((char) Typography.dollar);
                        extendedContent.append((char) ((character - 1) + 65));
                        break;
                    }
                    break;
            }
        }
        return extendedContent.toString();
    }
}
