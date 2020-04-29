package dji.publics.widget.util;

import java.text.NumberFormat;

public class ThreadLocalFormatUtil {
    private static ThreadLocal<NumberFormat> mNumberLocal = new ThreadLocal<>();

    public static String format(double formatStr) {
        NumberFormat numberFormat = mNumberLocal.get();
        if (numberFormat == null) {
            numberFormat = NumberFormat.getPercentInstance();
            numberFormat.setMaximumFractionDigits(0);
            mNumberLocal.set(numberFormat);
        }
        return numberFormat.format(formatStr);
    }
}
