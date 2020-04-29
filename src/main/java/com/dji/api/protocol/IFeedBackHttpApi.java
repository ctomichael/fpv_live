package com.dji.api.protocol;

import com.dji.api.base.IHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface IFeedBackHttpApi extends IHttpApi {
    public static final String API_FEEDBACK_QUESTION = "feedback/syncfeedbackParams";
    public static final String API_FEEDBACK_UPLOAD = "feedback/uploadOrder";
    public static final String DOMAIN = "https://feedback-external.djicorp.com";
}
