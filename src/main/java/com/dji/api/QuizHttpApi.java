package com.dji.api;

import com.dji.api.protocol.IQuizHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.util.Util;

@EXClassNullAway
public class QuizHttpApi implements IQuizHttpApi {
    private static String sTestCheckDomain = "";
    private static String sTestUrlFrontUrl = "";

    public static void setTestCheckDomain(String testCheckDomain) {
        sTestCheckDomain = testCheckDomain;
    }

    public static void setTestUrlFrontUrl(String testUrlFrontUrl) {
        sTestUrlFrontUrl = testUrlFrontUrl;
    }

    public static String generateCheckQuizUrl(String... route) {
        return Util.generateUrl(IQuizHttpApi.CHECKT_DOMAIN, sTestCheckDomain, route);
    }

    public static String generateFrontQuizUrl() {
        return Util.getDomain(IQuizHttpApi.URL_FRONT_URL, sTestUrlFrontUrl);
    }
}
