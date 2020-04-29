package com.dji.api;

import com.dji.util.Util;

public class QuestionnaireHttpApi {
    public static String mCloudControlUrl = "https://api.djiservice.org/api/cloudcontrol/config";
    public static String mDomain = "https://app-h5.dji.com/YXBwLWg1LW/activate-quiz.html";
    public static String mDomainTest = "https://test-app-h5.dbeta.me/YXBwLWg1LW/activate-quiz.html";

    public static void setDomainTest(String domainTest) {
        mDomainTest = domainTest;
    }

    public static String getQuestionnaireUrl() {
        return Util.generateUrl(mDomain, mDomainTest, null);
    }
}
