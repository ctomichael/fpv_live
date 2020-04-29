package com.amap.location.common.model;

import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import java.util.Locale;
import kotlin.jvm.internal.ShortCompanionObject;

public class CellState {
    public static final int I_CDMA_T = 2;
    public static final int I_DEF_CGI_T = 0;
    public static final int I_GSM_T = 1;
    public static final int I_LTE_T = 3;
    public static final int I_WCDMA_T = 4;
    public int bid = 0;
    @Deprecated
    public short cellAge = 0;
    public int cid = 0;
    public int lac = 0;
    @Deprecated
    public long lastUpdateTimeMills = 0;
    public long lastUpdateUtcMills = 0;
    public int latitude;
    public int longitude;
    public int mcc = 0;
    public int mnc = 0;
    public boolean newapi = true;
    public int nid = 0;
    public short pci = ShortCompanionObject.MAX_VALUE;
    public boolean registered;
    public int sid = 0;
    public int signalStrength = 99;
    public int type = 0;

    public CellState(int i, boolean z) {
        this.type = i;
        this.registered = z;
    }

    public CellState(int i, boolean z, boolean z2) {
        this.type = i;
        this.registered = z;
        this.newapi = z2;
    }

    private boolean bidValid(int i) {
        return i >= 0 && i <= 65535;
    }

    private boolean cidValid(int i) {
        return i >= 0 && i <= 268435455;
    }

    private boolean lacValid(int i) {
        return i >= 0 && i <= 65535;
    }

    private boolean nidValid(int i) {
        return i >= 0 && i <= 65535;
    }

    private boolean sidValid(int i) {
        return i > 0 && i <= 32767;
    }

    public CellState clone() {
        CellState cellState = new CellState(this.type, this.registered, this.newapi);
        cellState.mcc = this.mcc;
        cellState.mnc = this.mnc;
        cellState.lac = this.lac;
        cellState.cid = this.cid;
        cellState.sid = this.sid;
        cellState.nid = this.nid;
        cellState.bid = this.bid;
        cellState.signalStrength = this.signalStrength;
        cellState.latitude = this.latitude;
        cellState.longitude = this.longitude;
        cellState.cellAge = this.cellAge;
        cellState.lastUpdateTimeMills = this.lastUpdateTimeMills;
        cellState.lastUpdateUtcMills = this.lastUpdateUtcMills;
        cellState.pci = this.pci;
        return cellState;
    }

    public String getKey() {
        String keyWithOutInterface = getKeyWithOutInterface();
        if (TextUtils.isEmpty(keyWithOutInterface)) {
            return "";
        }
        return (this.newapi ? 1 : 0) + "#" + keyWithOutInterface;
    }

    public String getKeyWithOutInterface() {
        switch (this.type) {
            case 1:
            case 3:
            case 4:
                return this.type + "#" + this.mcc + "#" + this.mnc + "#" + this.lac + "#" + this.cid;
            case 2:
                return this.type + "#" + this.sid + "#" + this.nid + "#" + this.bid;
            default:
                return "";
        }
    }

    public boolean isValid() {
        switch (this.type) {
            case 1:
            case 3:
            case 4:
                return lacValid(this.lac) && cidValid(this.cid);
            case 2:
                return sidValid(this.sid) && nidValid(this.nid) && bidValid(this.bid);
            default:
                return false;
        }
    }

    public String toString() {
        switch (this.type) {
            case 1:
                return String.format(Locale.CHINA, "[type=GSM, mcc=%d, mnc=%d, lac=%d, cid=%d, sig=%d, age=%d, reg=%b, new=%b]", Integer.valueOf(this.mcc), Integer.valueOf(this.mnc), Integer.valueOf(this.lac), Integer.valueOf(this.cid), Integer.valueOf(this.signalStrength), Short.valueOf(this.cellAge), Boolean.valueOf(this.registered), Boolean.valueOf(this.newapi));
            case 2:
                return String.format(Locale.CHINA, "[type=CDMA, mcc=%d, mnc=%d, sid=%d, nid=%d, bid=%d, sig=%d, age=%d, reg=%b, new=%b]", Integer.valueOf(this.mcc), Integer.valueOf(this.mnc), Integer.valueOf(this.sid), Integer.valueOf(this.nid), Integer.valueOf(this.bid), Integer.valueOf(this.signalStrength), Short.valueOf(this.cellAge), Boolean.valueOf(this.registered), Boolean.valueOf(this.newapi));
            case 3:
                return String.format(Locale.CHINA, "[type=LTE, mcc=%d, mnc=%d, lac=%d, cid=%d, sig=%d, age=%d, reg=%b, new=%b, pci=%d]", Integer.valueOf(this.mcc), Integer.valueOf(this.mnc), Integer.valueOf(this.lac), Integer.valueOf(this.cid), Integer.valueOf(this.signalStrength), Short.valueOf(this.cellAge), Boolean.valueOf(this.registered), Boolean.valueOf(this.newapi), Short.valueOf(this.pci));
            case 4:
                return String.format(Locale.CHINA, "[type=WCDMA, mcc=%d, mnc=%d, lac=%d, cid=%d, sig=%d, age=%d, reg=%b, new=%b, psc=%d]", Integer.valueOf(this.mcc), Integer.valueOf(this.mnc), Integer.valueOf(this.lac), Integer.valueOf(this.cid), Integer.valueOf(this.signalStrength), Short.valueOf(this.cellAge), Boolean.valueOf(this.registered), Boolean.valueOf(this.newapi), Short.valueOf(this.pci));
            default:
                return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }
}
