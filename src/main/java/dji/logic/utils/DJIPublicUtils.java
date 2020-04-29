package dji.logic.utils;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIPublicUtils {
    public static long formatVersion(String version) {
        long result = 0;
        if (version != null && !version.isEmpty()) {
            String[] firms = version.split("\\.");
            int i = 0;
            while (i < firms.length) {
                try {
                    result = (256 * result) + ((long) Integer.parseInt(firms[i], 10));
                    i++;
                } catch (NumberFormatException e) {
                }
            }
        }
        return result;
    }

    private DJIPublicUtils() {
    }
}
