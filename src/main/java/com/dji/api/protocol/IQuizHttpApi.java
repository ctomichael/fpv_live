package com.dji.api.protocol;

import com.dji.api.base.IHttpApi;
import com.dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface IQuizHttpApi extends IHttpApi {
    public static final String CHECKT_DOMAIN = "https://djigo-hk.djiservice.org";
    public static final String URL_CHECK_QUIZ = "api/v1/quiz/status";
    public static final String URL_FRONT_URL = "https://quiz.djicdn.com/quiz/index.html";
    public static final String URL_QUESTION_NAIRE = "api/v1/quiz/questionnaire";
}
