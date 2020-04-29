package dji.dbox.upgrade.p4.model;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class DJIUpUrlModel {
    public Apis apis;
    public String signature;
    public String state;

    @Keep
    public static class Apis {
        public String allfile;
        public String config;
        public String down;
        public String firm;
        public String test;
    }
}
