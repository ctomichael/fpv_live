package dji.log.impl;

import dji.log.DJILogConstant;
import dji.log.IConsoleFormat;
import java.util.Locale;

public class SimpleConsoleFormat implements IConsoleFormat {
    public String formatMsg(int priority, String tag, String msg) {
        return String.format(Locale.US, DJILogConstant.FORMAT_CONSOLE, msg);
    }

    public String formatTag(int priority, String tag) {
        return tag;
    }
}
