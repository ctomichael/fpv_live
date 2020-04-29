package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public final class EAN13Writer extends UPCEANWriter {
    private static final int CODE_WIDTH = 95;

    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) throws WriterException {
        if (format == BarcodeFormat.EAN_13) {
            return super.encode(contents, format, width, height, hints);
        }
        throw new IllegalArgumentException("Can only encode EAN_13, but got " + format);
    }

    public boolean[] encode(String contents) {
        int length = contents.length();
        switch (length) {
            case 12:
                try {
                    contents = contents + UPCEANReader.getStandardUPCEANChecksum(contents);
                    break;
                } catch (FormatException fe) {
                    throw new IllegalArgumentException(fe);
                }
            case 13:
                try {
                    if (!UPCEANReader.checkStandardUPCEANChecksum(contents)) {
                        throw new IllegalArgumentException("Contents do not pass checksum");
                    }
                } catch (FormatException e) {
                    throw new IllegalArgumentException("Illegal contents");
                }
                break;
            default:
                throw new IllegalArgumentException("Requested contents should be 12 or 13 digits long, but got " + length);
        }
        int parities = EAN13Reader.FIRST_DIGIT_ENCODINGS[Character.digit(contents.charAt(0), 10)];
        boolean[] result = new boolean[95];
        int pos = appendPattern(result, 0, UPCEANReader.START_END_PATTERN, true) + 0;
        for (int i = 1; i <= 6; i++) {
            int digit = Character.digit(contents.charAt(i), 10);
            if (((parities >> (6 - i)) & 1) == 1) {
                digit += 10;
            }
            pos += appendPattern(result, pos, UPCEANReader.L_AND_G_PATTERNS[digit], false);
        }
        int pos2 = pos + appendPattern(result, pos, UPCEANReader.MIDDLE_PATTERN, false);
        for (int i2 = 7; i2 <= 12; i2++) {
            pos2 += appendPattern(result, pos2, UPCEANReader.L_PATTERNS[Character.digit(contents.charAt(i2), 10)], true);
        }
        appendPattern(result, pos2, UPCEANReader.START_END_PATTERN, true);
        return result;
    }
}
