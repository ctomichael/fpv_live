package dji.log.impl;

import dji.log.DJILogConstant;
import dji.log.DJILogUtils;
import dji.log.IFileFormat;
import java.util.Locale;

public class SimpleFileFormat implements IFileFormat {
    public String format(long info_time, String info_level, String info_tag, String info_msg) {
        return String.format(Locale.US, DJILogConstant.FORMAT_FILE, info_level, DJILogUtils.format(info_time, DJILogConstant.LOG_INFO_TIME_FORMAT), info_msg);
    }
}
