package dji.pilot.publics.model;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DJIUpgradePackListModel {
    public DJIUpgradeAnnouncement announcement;
    public DJIUpgradeAnnouncement announcementAndroid;
    public DJIUpgradeAppVersion application;
    public ArrayList<DJIUpgradePack> versionlist;
    public ArrayList<DJIUpgradePack> versionlistHG300;
    public ArrayList<DJIUpgradePack> versionlista3;
    public ArrayList<DJIUpgradePack> versionlistc;
    public ArrayList<DJIUpgradePack> versionlistcv600;
    public ArrayList<DJIUpgradePack> versionlisthg;
    public ArrayList<DJIUpgradePack> versionlisthgX5;
    public ArrayList<DJIUpgradePack> versionlisthgX5R;
    public ArrayList<DJIUpgradePack> versionlistlb2;
    public ArrayList<DJIUpgradePack> versionlistm100;
    public ArrayList<DJIUpgradePack> versionlistm600;
    public ArrayList<DJIUpgradePack> versionlistm600pro;
    public ArrayList<DJIUpgradePack> versionlistn3;
    public ArrayList<DJIUpgradePack> versionlistp4a;
    public ArrayList<DJIUpgradePack> versionlistp4p;
    public ArrayList<DJIUpgradePack> versionlists;
    public ArrayList<DJIUpgradePack> versionlistx;
    public ArrayList<DJIUpgradePack> versionlistx5;
    public ArrayList<DJIUpgradePack> versionlistx5r;
    public ArrayList<DJIUpgradePack> versionlistxt;
    public ArrayList<DJIUpgradePack> versionlistxw;
    public ArrayList<DJIUpgradePack> versionlistz3;
    public ArrayList<DJIUpgradePack> versionlistz30;

    @Keep
    public static class ReleaseNote {
        public String cn;
        public String en;
    }

    @Keep
    public static class DJIUpgradeAnnouncement {
        public String en;
        public String guid;
        public String jp;
        public String zh;
    }

    @Keep
    public static class DJIUpgradeAppVersion {

        /* renamed from: android  reason: collision with root package name */
        public String f19android;
        public int significant1;
    }

    @Keep
    public static class DJIUpgradeDevice {
        public int isLock;
        public String name;
        public String version;
    }

    public ArrayList<DJIUpgradePack> getVersionList(ProductType productType) {
        switch (productType) {
            case Orange:
            case BigBanana:
            case Olives:
            case OrangeRAW:
            case OrangeCV600:
                return this.versionlist;
            case litchiX:
                return this.versionlistx;
            case litchiS:
                return this.versionlists;
            case litchiC:
                return this.versionlistc;
            case N1:
                return this.versionlistm100;
            case PM820:
                return this.versionlistm600;
            case PM820PRO:
                return this.versionlistm600pro;
            case Longan:
                return this.versionlisthg;
            case LonganPro:
                return this.versionlisthgX5;
            case LonganRaw:
                return this.versionlisthgX5R;
            case LonganZoom:
                return this.versionlistcv600;
            case LonganMobile:
                return this.versionlistHG300;
            case Grape2:
            case A2:
                return this.versionlistlb2;
            case P34K:
                return this.versionlistxw;
            case Tomato:
                return this.versionlistx;
            case Pomato:
                return this.versionlistp4p;
            case Potato:
                return this.versionlistp4a;
            default:
                return null;
        }
    }

    @Keep
    public static class DJIUpgradePack {
        public int android_ignore;
        public long date;
        public String m0100;
        public String m0101;
        public String m0104;
        public String m0106;
        public String m0305;
        public String m0306;
        public String m0400;
        public String m0401;
        public String m0402;
        public String m0403;
        public String m0407;
        public String m0500;
        public String m0700;
        public String m0800;
        public String m0801;
        public String m0807;
        public String m0900;
        public String m0901;
        public String m1100;
        public String m1101;
        public String m1102;
        public String m1103;
        public String m1104;
        public String m1105;
        public String m1106;
        public String m1200;
        public String m1201;
        public String m1202;
        public String m1203;
        public String m1204;
        public String m1205;
        public String m1300;
        public String m1301;
        public String m1400;
        public String m1401;
        public String m1405;
        public String m1500;
        public String m1501;
        public String m1600;
        public String m1601;
        public String m1700;
        public String m1701;
        public String m1900;
        public String m2000;
        public String m2001;
        public String m2002;
        public String m2003;
        public String m2500;
        public String m2501;
        public String m2601;
        public String m2700;
        public String m2900;
        public String packurl;
        public int priority;
        public String rc1url;
        public String rcurl;
        public String rcversion;
        public ReleaseNote release_note;
        public String version;

        public static String getVersion(String s) {
            return s.split("&")[0];
        }

        public static int getFlag(String s) {
            return Integer.parseInt(s.split("&")[1]);
        }
    }
}
