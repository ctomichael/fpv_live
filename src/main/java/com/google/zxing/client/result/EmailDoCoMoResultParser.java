package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.regex.Pattern;

public final class EmailDoCoMoResultParser extends AbstractDoCoMoResultParser {
    private static final Pattern ATEXT_ALPHANUMERIC = Pattern.compile("[a-zA-Z0-9@.!#$%&'*+\\-/=?^_`{|}~]+");

    public EmailAddressParsedResult parse(Result result) {
        String[] tos;
        String rawText = getMassagedText(result);
        if (!rawText.startsWith("MATMSG:") || (tos = matchDoCoMoPrefixedField("TO:", rawText, true)) == null) {
            return null;
        }
        for (String str : tos) {
            if (!isBasicallyValidEmailAddress(str)) {
                return null;
            }
        }
        return new EmailAddressParsedResult(tos, null, null, matchSingleDoCoMoPrefixedField("SUB:", rawText, false), matchSingleDoCoMoPrefixedField("BODY:", rawText, false));
    }

    static boolean isBasicallyValidEmailAddress(String email) {
        return email != null && ATEXT_ALPHANUMERIC.matcher(email).matches() && email.indexOf(64) >= 0;
    }
}
