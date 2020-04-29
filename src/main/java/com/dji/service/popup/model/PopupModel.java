package com.dji.service.popup.model;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class PopupModel {
    public String msg;
    public Result[] result;
    public int status = -1;
    public int total_count;

    @Keep
    public class Result {
        public String begin_date;
        public String content;
        public String end_date;
        public int id;
        public String jump_url;
        public int left_button_command;
        public String left_button_msg;
        public int popup_location;
        public int popup_time;
        public int right_button_command;
        public String right_button_msg;
        public String title;

        public Result() {
        }
    }
}
