package dji.common.product;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;

@Keep
@EXClassNullAway
public enum Model {
    INSPIRE_1("Inspire 1"),
    INSPIRE_1_PRO("Inspire 1 Pro"),
    INSPIRE_1_RAW("Inspire 1 RAW"),
    INSPIRE_2("Inspire 2"),
    MATRICE_100("Matrice 100"),
    PHANTOM_3_ADVANCED("Phantom 3 Advanced"),
    PHANTOM_3_PROFESSIONAL("Phantom 3 Professional"),
    PHANTOM_3_STANDARD("Phantom 3 Standard"),
    Phantom_3_4K("Phantom 3 4K"),
    Phantom_3_SE("Phantom 3 SE"),
    PHANTOM_4("Phantom 4"),
    PHANTOM_4_PRO("Phantom 4 Pro"),
    PHANTOM_4_ADVANCED("Phantom 4 Advanced"),
    PHANTOM_4_PRO_V2("Phantom 4 Pro V2.0"),
    PHANTOM_4_RTK("Phantom 4 RTK"),
    OSMO("Osmo"),
    OSMO_MOBILE("Osmo Mobile"),
    OSMO_MOBILE_2("Osmo Mobile 2"),
    OSMO_PRO("Osmo Pro"),
    OSMO_RAW("Osmo RAW"),
    OSMO_PLUS("Osmo+"),
    MATRICE_600("Matrice 600"),
    MATRICE_200("M200"),
    MATRICE_210("M210"),
    MATRICE_210_RTK("M210RTK"),
    MATRICE_PM420("M200 V2"),
    MATRICE_PM420PRO("M210 V2"),
    MATRICE_PM420PRO_RTK("M210RTK V2"),
    MATRICE_600_PRO("Matrice 600 Pro"),
    A3("A3"),
    N3("N3"),
    UNKNOWN_AIRCRAFT("Unknown Aircraft"),
    UNKNOWN_HANDHELD("Unknown Handheld"),
    MAVIC_PRO("Mavic Pro"),
    Spark("Spark"),
    MAVIC_AIR("Mavic Air"),
    MAVIC_2_ZOOM("Mavic 2 Zoom"),
    MAVIC_2_PRO("Mavic 2 Pro"),
    MAVIC_2("Mavic 2"),
    MAVIC_2_ENTERPRISE("Mavic 2 Enterprise"),
    MAVIC_2_ENTERPRISE_DUAL("Mavic 2 Enterprise Dual"),
    ZENMUSE_Z3(DJICameraAbstraction.DisplayNameZ3),
    WM160("Mavic Mini"),
    DISCONNECT("Disconnect");
    
    private String value;

    private Model(String value2) {
        this.value = value2;
    }

    public String getDisplayName() {
        return this.value;
    }
}
