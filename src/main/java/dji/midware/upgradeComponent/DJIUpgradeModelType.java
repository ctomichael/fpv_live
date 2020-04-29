package dji.midware.upgradeComponent;

import android.support.annotation.Keep;

@Keep
public enum DJIUpgradeModelType {
    Unknown(0),
    TomatoX(1),
    Orange(2),
    KumquatX(3),
    KumquatL(4),
    TomatoPro(5),
    RC001(6),
    MammothAC(7),
    RC002(8),
    TomatoAdvanced(9),
    PM410(10),
    PM930(11),
    RC003(12),
    WM230AC(13),
    WM230RC(14),
    TomatoProSDR(15),
    RCP4PSDR(16),
    WM240AC(17),
    WM240RC(18),
    WM240RCPigeon(19),
    WM245AC(20),
    WM245RC(21),
    WM245RCPigeon(22),
    WM246AC(23),
    WM160AC(24),
    WM160RC(25),
    RCP4PSDRScreen(30);
    
    private int value;

    private DJIUpgradeModelType(int value2) {
        this.value = value2;
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0033, code lost:
        if (r1.equals(dji.midware.upgradeComponent.DJIUpgradeProductID.WM330) != false) goto L_0x0026;
     */
    @android.support.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static dji.midware.upgradeComponent.DJIUpgradeModelType getDJIUpgradeModelTypeFromDroneInfo(java.lang.String r5) {
        /*
            r2 = 0
            boolean r3 = android.text.TextUtils.isEmpty(r5)
            if (r3 == 0) goto L_0x000a
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.Unknown
        L_0x0009:
            return r2
        L_0x000a:
            r1 = r5
            java.lang.String r3 = " "
            java.lang.String[] r0 = r5.split(r3)
            int r3 = r0.length
            if (r3 <= 0) goto L_0x0017
            r1 = r0[r2]
        L_0x0017:
            java.util.Locale r3 = java.util.Locale.ENGLISH
            java.lang.String r1 = r1.toLowerCase(r3)
            r3 = -1
            int r4 = r1.hashCode()
            switch(r4) {
                case -785927066: goto L_0x006d;
                case 106733110: goto L_0x0083;
                case 106737977: goto L_0x008f;
                case 113195029: goto L_0x00db;
                case 113195866: goto L_0x0041;
                case 113195867: goto L_0x004c;
                case 113195868: goto L_0x0057;
                case 113195897: goto L_0x009b;
                case 113195928: goto L_0x00a7;
                case 113195933: goto L_0x00b4;
                case 113195934: goto L_0x00c1;
                case 113196858: goto L_0x002c;
                case 113196859: goto L_0x0062;
                case 113196860: goto L_0x0078;
                case 113196863: goto L_0x00ce;
                case 113199710: goto L_0x0036;
                default: goto L_0x0025;
            }
        L_0x0025:
            r2 = r3
        L_0x0026:
            switch(r2) {
                case 0: goto L_0x00e8;
                case 1: goto L_0x00ec;
                case 2: goto L_0x00f0;
                case 3: goto L_0x00f4;
                case 4: goto L_0x00f4;
                case 5: goto L_0x00f8;
                case 6: goto L_0x00fc;
                case 7: goto L_0x0100;
                case 8: goto L_0x0104;
                case 9: goto L_0x0108;
                case 10: goto L_0x010c;
                case 11: goto L_0x0110;
                case 12: goto L_0x0114;
                case 13: goto L_0x0118;
                case 14: goto L_0x011c;
                case 15: goto L_0x0120;
                default: goto L_0x0029;
            }
        L_0x0029:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.Unknown
            goto L_0x0009
        L_0x002c:
            java.lang.String r4 = "wm330"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0025
            goto L_0x0026
        L_0x0036:
            java.lang.String r2 = "wm620"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 1
            goto L_0x0026
        L_0x0041:
            java.lang.String r2 = "wm220"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 2
            goto L_0x0026
        L_0x004c:
            java.lang.String r2 = "wm221"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 3
            goto L_0x0026
        L_0x0057:
            java.lang.String r2 = "wm222"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 4
            goto L_0x0026
        L_0x0062:
            java.lang.String r2 = "wm331"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 5
            goto L_0x0026
        L_0x006d:
            java.lang.String r2 = "wm100a"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 6
            goto L_0x0026
        L_0x0078:
            java.lang.String r2 = "wm332"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 7
            goto L_0x0026
        L_0x0083:
            java.lang.String r2 = "pm410"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 8
            goto L_0x0026
        L_0x008f:
            java.lang.String r2 = "pm930"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 9
            goto L_0x0026
        L_0x009b:
            java.lang.String r2 = "wm230"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 10
            goto L_0x0026
        L_0x00a7:
            java.lang.String r2 = "wm240"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 11
            goto L_0x0026
        L_0x00b4:
            java.lang.String r2 = "wm245"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 12
            goto L_0x0026
        L_0x00c1:
            java.lang.String r2 = "wm246"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 13
            goto L_0x0026
        L_0x00ce:
            java.lang.String r2 = "wm335"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 14
            goto L_0x0026
        L_0x00db:
            java.lang.String r2 = "wm160"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 15
            goto L_0x0026
        L_0x00e8:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.TomatoX
            goto L_0x0009
        L_0x00ec:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.Orange
            goto L_0x0009
        L_0x00f0:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.KumquatX
            goto L_0x0009
        L_0x00f4:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.KumquatL
            goto L_0x0009
        L_0x00f8:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.TomatoPro
            goto L_0x0009
        L_0x00fc:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.MammothAC
            goto L_0x0009
        L_0x0100:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.TomatoAdvanced
            goto L_0x0009
        L_0x0104:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.PM410
            goto L_0x0009
        L_0x0108:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.PM930
            goto L_0x0009
        L_0x010c:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.WM230AC
            goto L_0x0009
        L_0x0110:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.WM240AC
            goto L_0x0009
        L_0x0114:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.WM245AC
            goto L_0x0009
        L_0x0118:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.WM246AC
            goto L_0x0009
        L_0x011c:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.TomatoProSDR
            goto L_0x0009
        L_0x0120:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.WM160AC
            goto L_0x0009
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.upgradeComponent.DJIUpgradeModelType.getDJIUpgradeModelTypeFromDroneInfo(java.lang.String):dji.midware.upgradeComponent.DJIUpgradeModelType");
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0033, code lost:
        if (r1.equals(dji.midware.upgradeComponent.DJIUpgradeProductID.RC002) != false) goto L_0x0026;
     */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00ea  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00ee  */
    @android.support.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static dji.midware.upgradeComponent.DJIUpgradeModelType getDJIUpgradeModelTypeFromRCInfo(java.lang.String r5) {
        /*
            r2 = 0
            boolean r3 = android.text.TextUtils.isEmpty(r5)
            if (r3 == 0) goto L_0x000a
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.Unknown
        L_0x0009:
            return r2
        L_0x000a:
            r1 = r5
            java.lang.String r3 = " "
            java.lang.String[] r0 = r5.split(r3)
            int r3 = r0.length
            if (r3 <= 0) goto L_0x0017
            r1 = r0[r2]
        L_0x0017:
            java.util.Locale r3 = java.util.Locale.ENGLISH
            java.lang.String r1 = r1.toLowerCase(r3)
            r3 = -1
            int r4 = r1.hashCode()
            switch(r4) {
                case -1244857411: goto L_0x008f;
                case 108278368: goto L_0x0036;
                case 108278369: goto L_0x002c;
                case 108278370: goto L_0x0041;
                case 108280382: goto L_0x0057;
                case 113195928: goto L_0x006d;
                case 113195933: goto L_0x0078;
                case 113196863: goto L_0x0083;
                case 643876507: goto L_0x009b;
                case 1407078250: goto L_0x004c;
                case 1407108041: goto L_0x0062;
                default: goto L_0x0025;
            }
        L_0x0025:
            r2 = r3
        L_0x0026:
            switch(r2) {
                case 0: goto L_0x00a7;
                case 1: goto L_0x00ab;
                case 2: goto L_0x00af;
                case 3: goto L_0x00b3;
                case 4: goto L_0x00b3;
                case 5: goto L_0x00b7;
                case 6: goto L_0x00bb;
                case 7: goto L_0x00dd;
                case 8: goto L_0x00ff;
                case 9: goto L_0x0103;
                case 10: goto L_0x0107;
                default: goto L_0x0029;
            }
        L_0x0029:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.Unknown
            goto L_0x0009
        L_0x002c:
            java.lang.String r4 = "rc002"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0025
            goto L_0x0026
        L_0x0036:
            java.lang.String r2 = "rc001"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 1
            goto L_0x0026
        L_0x0041:
            java.lang.String r2 = "rc003"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 2
            goto L_0x0026
        L_0x004c:
            java.lang.String r2 = "wm230rc"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 3
            goto L_0x0026
        L_0x0057:
            java.lang.String r2 = "rc230"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 4
            goto L_0x0026
        L_0x0062:
            java.lang.String r2 = "wm240rc"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 5
            goto L_0x0026
        L_0x006d:
            java.lang.String r2 = "wm240"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 6
            goto L_0x0026
        L_0x0078:
            java.lang.String r2 = "wm245"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 7
            goto L_0x0026
        L_0x0083:
            java.lang.String r2 = "wm335"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 8
            goto L_0x0026
        L_0x008f:
            java.lang.String r2 = "gl300k"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 9
            goto L_0x0026
        L_0x009b:
            java.lang.String r2 = "wm160_rc"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0025
            r2 = 10
            goto L_0x0026
        L_0x00a7:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.RC002
            goto L_0x0009
        L_0x00ab:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.RC001
            goto L_0x0009
        L_0x00af:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.RC003
            goto L_0x0009
        L_0x00b3:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.WM230RC
            goto L_0x0009
        L_0x00b7:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.WM240RC
            goto L_0x0009
        L_0x00bb:
            java.lang.String r2 = "wm240 rc ver.b"
            java.lang.String r3 = r5.toLowerCase()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00cc
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.WM240RCPigeon
            goto L_0x0009
        L_0x00cc:
            java.lang.String r2 = "wm240 rc ver.a"
            java.lang.String r3 = r5.toLowerCase()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00dd
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.WM240RC
            goto L_0x0009
        L_0x00dd:
            java.lang.String r2 = "wm245 rc ver.b"
            java.lang.String r3 = r5.toLowerCase()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ee
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.WM245RCPigeon
            goto L_0x0009
        L_0x00ee:
            java.lang.String r2 = "wm245 rc ver.a"
            java.lang.String r3 = r5.toLowerCase()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ff
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.WM245RC
            goto L_0x0009
        L_0x00ff:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.RCP4PSDR
            goto L_0x0009
        L_0x0103:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.RCP4PSDRScreen
            goto L_0x0009
        L_0x0107:
            dji.midware.upgradeComponent.DJIUpgradeModelType r2 = dji.midware.upgradeComponent.DJIUpgradeModelType.WM160RC
            goto L_0x0009
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.upgradeComponent.DJIUpgradeModelType.getDJIUpgradeModelTypeFromRCInfo(java.lang.String):dji.midware.upgradeComponent.DJIUpgradeModelType");
    }
}
