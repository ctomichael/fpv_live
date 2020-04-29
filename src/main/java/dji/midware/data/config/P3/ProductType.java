package dji.midware.data.config.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;

@Keep
@EXClassNullAway
public enum ProductType {
    None(0, "Unknown"),
    Orange(1, "Inspire 1"),
    litchiC(2, "P3 Standard", true),
    litchiS(3, "P3 Advanced"),
    litchiX(4, "P3 Professional"),
    Longan(5, "OSMO", true),
    N1(6, "M100"),
    Tomato(7, "Phantom 4"),
    Grape2(8, "LB2", false, false),
    BigBanana(9, "Inspire 1 Pro", false, true),
    A3(10, "DJI Device", false, false),
    PM820(11, "DJI Device", false, true),
    P34K(12, "P3 4K", true, true),
    KumquatX(13, "Mavic Pro", false, true, false),
    Olives(14, "Zenmuse XT", false, true),
    OrangeRAW(15, "Inspire 1 Raw"),
    A2(16, "DJI Device", false, false),
    Orange2(17, "Inspire 2", false, true),
    LonganPro(18, "OSMO Pro", true, true),
    LonganRaw(19, "OSMO Raw", true, true),
    LonganZoom(20, "OSMO+", true, true),
    KumquatS(21, "Mavic", false, true, false),
    LonganMobile(22, "OSMO Mobile", false, true),
    OrangeCV600(23, DJICameraAbstraction.DisplayNameZ3),
    Pomato(24, "Phantom 4 PRO"),
    N3(25, "N3 FC", false, false),
    Mammoth(26, "SPARK", false, true, false),
    PM820PRO(27, "PM820 Pro", false, true),
    Potato(28, "Phantom 4 Advanced", false, true),
    AG405(30, "AG405", false, true),
    M200(31, "MATRICE 200", false, true),
    M210(33, "MATRICE 210", false, true),
    M210RTK(34, "MATRICE 210RTK", false, true),
    WM230(38, "Mavic Air", false, true, false),
    WM240(42, "Mavic 2", false, true, false),
    LonganMobile2(39, "OSMO Mobile 2", false, true),
    PomatoSDR(44, "Phantom 4 PRO V2.0", false, true),
    PomatoRTK(45, "Phantom 4 ProfessionalR", false, true),
    PM420(46, "MATRICE M200 V2", false, true),
    PM420PRO(47, "MATRICE M210 V2", false, true),
    PM420PRO_RTK(48, "MATRICE M210RTK V2", false, true),
    RM500(56, "RM500", false, false),
    WM245(58, "WM245", false, true, false),
    WM160(59, DJICameraAbstraction.DisplayNameWM160, false, true),
    OTHER(100, "OTHER");
    
    private int data;
    private boolean isCompleteMachine = true;
    private boolean isFromWifi = false;
    private boolean isGDR = true;
    private String name;

    private ProductType(int _data, String productName) {
        this.data = _data;
        this.name = productName;
    }

    private ProductType(int _data, String productName, boolean isFromWifi2) {
        this.data = _data;
        this.name = productName;
        this.isFromWifi = isFromWifi2;
    }

    private ProductType(int _data, String productName, boolean isFromWifi2, boolean isCompleteMachine2) {
        this.data = _data;
        this.name = productName;
        this.isFromWifi = isFromWifi2;
        this.isCompleteMachine = isCompleteMachine2;
    }

    private ProductType(int _data, String productName, boolean isFromWifi2, boolean isCompleteMachine2, boolean isGDR2) {
        this.data = _data;
        this.name = productName;
        this.isFromWifi = isFromWifi2;
        this.isCompleteMachine = isCompleteMachine2;
        this.isGDR = isGDR2;
    }

    public int value() {
        return this.data;
    }

    public String _name() {
        return this.name;
    }

    public boolean isFromWifi() {
        return this.isFromWifi;
    }

    public boolean isGDR() {
        return this.isGDR;
    }

    public boolean isCompleteMachine() {
        return this.isCompleteMachine;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static ProductType find(int b) {
        ProductType result = OTHER;
        ProductType[] items = values();
        for (int i = 0; i < items.length; i++) {
            if (items[i]._equals(b)) {
                return items[i];
            }
        }
        return result;
    }

    public String toString() {
        return super.toString();
    }

    public static boolean isValidType(ProductType type) {
        return (type == null || None == type || OTHER == type) ? false : true;
    }
}
