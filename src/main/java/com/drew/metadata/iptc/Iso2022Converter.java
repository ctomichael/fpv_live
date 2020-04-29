package com.drew.metadata.iptc;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

public final class Iso2022Converter {
    private static final int DOT = 14844066;
    private static final byte ESC = 27;
    private static final String ISO_8859_1 = "ISO-8859-1";
    private static final byte LATIN_CAPITAL_A = 65;
    private static final byte LATIN_CAPITAL_G = 71;
    private static final byte PERCENT_SIGN = 37;
    private static final String UTF_8 = "UTF-8";

    @Nullable
    public static String convertISO2022CharsetToJavaCharset(@NotNull byte[] bytes) {
        if (bytes.length > 2 && bytes[0] == 27 && bytes[1] == 37 && bytes[2] == 71) {
            return UTF_8;
        }
        if (bytes.length > 3 && bytes[0] == 27 && ((bytes[3] & 255) | ((bytes[2] & 255) << 8) | ((bytes[1] & 255) << Tnaf.POW_2_WIDTH)) == DOT && bytes[4] == 65) {
            return "ISO-8859-1";
        }
        return null;
    }

    @Nullable
    static Charset guessCharSet(@NotNull byte[] bytes) {
        String[] arr$ = {UTF_8, System.getProperty("file.encoding"), "ISO-8859-1"};
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Charset charset = Charset.forName(arr$[i$]);
            try {
                charset.newDecoder().decode(ByteBuffer.wrap(bytes));
                return charset;
            } catch (CharacterCodingException e) {
                i$++;
            }
        }
        return null;
    }

    private Iso2022Converter() {
    }
}
