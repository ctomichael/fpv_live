package com.dji.api;

import com.dji.api.protocol.IFeedBackHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;
import com.dji.util.Util;

@EXClassNullAway
public class FeedBackHttpApi implements IFeedBackHttpApi {
    private static final String TAG = FeedBackHttpApi.class.getSimpleName();
    private static String sDomainTest = "";

    public static void setDomainTest(String domainTest) {
        sDomainTest = domainTest;
    }

    public static String getFeedbackQuestion() {
        return generateUrl(IFeedBackHttpApi.API_FEEDBACK_QUESTION);
    }

    public static String getUploadData() {
        return generateUrl(IFeedBackHttpApi.API_FEEDBACK_UPLOAD);
    }

    private static String generateUrl(String... route) {
        return Util.generateUrl(IFeedBackHttpApi.DOMAIN, sDomainTest, route);
    }

    public static String getDomain() {
        return Util.getDomain(IFeedBackHttpApi.DOMAIN, sDomainTest);
    }
}
