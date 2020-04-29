package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class URLTOResultParser extends ResultParser {
    public URIParsedResult parse(Result result) {
        int titleEnd;
        String title = null;
        String rawText = getMassagedText(result);
        if ((!rawText.startsWith("urlto:") && !rawText.startsWith("URLTO:")) || (titleEnd = rawText.indexOf(58, 6)) < 0) {
            return null;
        }
        if (titleEnd > 6) {
            title = rawText.substring(6, titleEnd);
        }
        return new URIParsedResult(rawText.substring(titleEnd + 1), title);
    }
}
