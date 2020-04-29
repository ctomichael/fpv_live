package dji.common.battery;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class BatteryUtils {
    public static final int BatteryNumber = 1;
    public static final int CommunicationStatus = 2;
    public static final int DifferentFirmware = 7;
    public static final int DifferentVoltage = 4;
    public static final int DifferentVoltageRearrange = 3;
    public static final int Discharge = 9;
    public static final int HasDamagedCell = 6;
    public static final int HasSmallVoltage = 12;
    public static final int LowCellVoltage = 5;
    public static final int LowTemperature = 11;
    public static final int OverHeat = 10;
    public static final int ShortCut = 8;

    public static boolean isErrorBatteryStatus(long status, int checkValue) {
        switch (checkValue) {
            case 1:
                if ((281474976710656L & status) != 0) {
                    return true;
                }
                return false;
            case 2:
                if ((562949953421312L & status) != 0) {
                    return true;
                }
                return false;
            case 3:
                if ((1125899906842624L & status) != 0) {
                    return true;
                }
                return false;
            case 4:
                if ((2251799813685248L & status) != 0) {
                    return true;
                }
                return false;
            case 5:
                if ((3968 & status) != 0) {
                    return true;
                }
                return false;
            case 6:
                if ((126976 & status) != 0) {
                    return true;
                }
                return false;
            case 7:
                if ((18014398509481984L & status) != 0) {
                    return true;
                }
                return false;
            case 8:
                if ((64 & status) != 0) {
                    return true;
                }
                return false;
            case 9:
                if ((1 & status) == 0 && (2 & status) == 0) {
                    return false;
                }
                return true;
            case 10:
                if ((4 & status) == 0 && (8 & status) == 0) {
                    return false;
                }
                return true;
            case 11:
                if ((16 & status) == 0 && (32 & status) == 0) {
                    return false;
                }
                return true;
            case 12:
                if ((4503599627370496L & status) != 0) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
}
