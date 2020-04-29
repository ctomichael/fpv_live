package com.dji.video.framing.demo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUtil {
    private static final String TAG = "TestUtil";
    private static DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");

    public static String getTimeString() {
        return dateFormat.format(new Date());
    }
}
