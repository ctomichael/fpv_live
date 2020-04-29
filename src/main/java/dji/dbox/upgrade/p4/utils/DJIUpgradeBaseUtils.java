package dji.dbox.upgrade.p4.utils;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIUpgradeBaseUtils {
    public static int compareFirVer(String ver1, String ver2) {
        try {
            String ver1s = ver1.replace(".", "");
            String ver2s = ver2.replace(".", "");
            int ver1Ints = Integer.parseInt(ver1s);
            int ver2Ints = Integer.parseInt(ver2s);
            if (ver1Ints > ver2Ints) {
                return 1;
            }
            if (ver1Ints != ver2Ints) {
                return -1;
            }
            return 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean isSameFirVer(String ver1, String ver2) {
        return compareFirVer(ver1, ver2) == 0;
    }
}
