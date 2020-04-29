package com.amap.openapi;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import java.util.List;

/* compiled from: CellUtil */
public class ax {
    private static byte a(TelephonyManager telephonyManager) {
        try {
            return (byte) telephonyManager.getNetworkType();
        } catch (Throwable th) {
            return 0;
        }
    }

    private static int a(int i) {
        return (i * 2) - 113;
    }

    public static CellInfo a(List<CellInfo> list) {
        if (Build.VERSION.SDK_INT < 17) {
            return null;
        }
        if (list != null) {
            for (CellInfo cellInfo : list) {
                if (((cellInfo instanceof CellInfoLte) || (cellInfo instanceof CellInfoCdma) || (cellInfo instanceof CellInfoGsm)) && cellInfo.isRegistered()) {
                    return cellInfo;
                }
                if (Build.VERSION.SDK_INT >= 18 && (cellInfo instanceof CellInfoWcdma) && cellInfo.isRegistered()) {
                    return cellInfo;
                }
            }
        }
        return null;
    }

    public static void a(@NonNull Context context, @NonNull q qVar, @Nullable CellLocation cellLocation, @Nullable SignalStrength signalStrength, @Nullable List<CellInfo> list) {
        List list2;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        qVar.a(a(telephonyManager), b(telephonyManager));
        if (cellLocation != null) {
            if (cellLocation instanceof GsmCellLocation) {
                try {
                    list2 = telephonyManager.getNeighboringCellInfo();
                } catch (Throwable th) {
                }
                a(qVar, cellLocation, signalStrength, list2);
            }
            list2 = null;
            a(qVar, cellLocation, signalStrength, list2);
        }
        if (list != null) {
            a(qVar, list);
        }
    }

    private static void a(@NonNull q qVar, @NonNull CellLocation cellLocation, @Nullable SignalStrength signalStrength, @Nullable List<NeighboringCellInfo> list) {
        if (cellLocation instanceof GsmCellLocation) {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
            T wVar = new w();
            wVar.c = gsmCellLocation.getLac();
            wVar.d = gsmCellLocation.getCid();
            if (Build.VERSION.SDK_INT >= 9) {
                wVar.i = gsmCellLocation.getPsc();
            }
            if (signalStrength != null) {
                int gsmSignalStrength = signalStrength.getGsmSignalStrength();
                wVar.e = gsmSignalStrength == 99 ? Integer.MAX_VALUE : a(gsmSignalStrength);
            }
            r rVar = new r();
            rVar.a = 1;
            rVar.f = wVar;
            rVar.b = 1;
            rVar.c = 0;
            qVar.c.add(rVar);
            if (list != null) {
                for (NeighboringCellInfo neighboringCellInfo : list) {
                    T wVar2 = new w();
                    wVar2.c = neighboringCellInfo.getLac();
                    wVar2.d = neighboringCellInfo.getCid();
                    wVar2.e = neighboringCellInfo.getRssi();
                    wVar2.i = neighboringCellInfo.getPsc();
                    r rVar2 = new r();
                    rVar2.a = 1;
                    rVar2.f = wVar2;
                    rVar2.b = 0;
                    rVar2.c = 0;
                    qVar.c.add(rVar2);
                }
            }
        } else if (cellLocation instanceof CdmaCellLocation) {
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
            T pVar = new p();
            pVar.d = cdmaCellLocation.getBaseStationLatitude();
            pVar.e = cdmaCellLocation.getBaseStationLongitude();
            pVar.a = cdmaCellLocation.getSystemId();
            pVar.b = cdmaCellLocation.getNetworkId();
            pVar.c = cdmaCellLocation.getBaseStationId();
            if (signalStrength != null) {
                pVar.f = signalStrength.getCdmaDbm();
            }
            r rVar3 = new r();
            rVar3.a = 2;
            rVar3.f = pVar;
            rVar3.b = 1;
            rVar3.c = 0;
            qVar.c.add(rVar3);
        }
    }

    private static void a(@NonNull q qVar, @NonNull List<CellInfo> list) {
        if (Build.VERSION.SDK_INT >= 17) {
            for (CellInfo cellInfo : list) {
                if (cellInfo instanceof CellInfoCdma) {
                    CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;
                    CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
                    T pVar = new p();
                    pVar.d = cellIdentity.getLatitude();
                    pVar.e = cellIdentity.getLongitude();
                    pVar.a = cellIdentity.getSystemId();
                    pVar.b = cellIdentity.getNetworkId();
                    pVar.c = cellIdentity.getBasestationId();
                    pVar.f = cellInfoCdma.getCellSignalStrength().getCdmaDbm();
                    pVar.g = cellInfoCdma.getCellSignalStrength().getAsuLevel();
                    r rVar = new r();
                    rVar.a = 2;
                    rVar.f = pVar;
                    rVar.b = cellInfo.isRegistered() ? (byte) 1 : 0;
                    rVar.c = 1;
                    qVar.c.add(rVar);
                } else if (cellInfo instanceof CellInfoGsm) {
                    CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                    CellIdentityGsm cellIdentity2 = cellInfoGsm.getCellIdentity();
                    T wVar = new w();
                    wVar.a = cellIdentity2.getMcc();
                    wVar.b = cellIdentity2.getMnc();
                    wVar.c = cellIdentity2.getLac();
                    wVar.d = cellIdentity2.getCid();
                    wVar.e = cellInfoGsm.getCellSignalStrength().getDbm();
                    wVar.h = cellInfoGsm.getCellSignalStrength().getAsuLevel();
                    if (Build.VERSION.SDK_INT >= 24) {
                        wVar.f = cellIdentity2.getArfcn();
                        wVar.g = cellIdentity2.getBsic();
                    }
                    r rVar2 = new r();
                    rVar2.a = 1;
                    rVar2.f = wVar;
                    rVar2.b = cellInfo.isRegistered() ? (byte) 1 : 0;
                    rVar2.c = 1;
                    qVar.c.add(rVar2);
                } else if (cellInfo instanceof CellInfoLte) {
                    CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                    CellIdentityLte cellIdentity3 = cellInfoLte.getCellIdentity();
                    T xVar = new x();
                    xVar.a = cellIdentity3.getMcc();
                    xVar.b = cellIdentity3.getMnc();
                    xVar.c = cellIdentity3.getTac();
                    xVar.d = cellIdentity3.getCi();
                    xVar.e = cellIdentity3.getPci();
                    xVar.f = cellInfoLte.getCellSignalStrength().getDbm();
                    xVar.h = cellInfoLte.getCellSignalStrength().getAsuLevel();
                    if (Build.VERSION.SDK_INT >= 24) {
                        xVar.g = cellIdentity3.getEarfcn();
                    }
                    r rVar3 = new r();
                    rVar3.a = 3;
                    rVar3.f = xVar;
                    rVar3.b = cellInfo.isRegistered() ? (byte) 1 : 0;
                    rVar3.c = 1;
                    qVar.c.add(rVar3);
                } else if (Build.VERSION.SDK_INT >= 18 && (cellInfo instanceof CellInfoWcdma)) {
                    CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                    CellIdentityWcdma cellIdentity4 = cellInfoWcdma.getCellIdentity();
                    T zVar = new z();
                    zVar.a = cellIdentity4.getMcc();
                    zVar.b = cellIdentity4.getMnc();
                    zVar.c = cellIdentity4.getLac();
                    zVar.d = cellIdentity4.getCid();
                    zVar.e = cellIdentity4.getPsc();
                    zVar.f = cellInfoWcdma.getCellSignalStrength().getDbm();
                    zVar.h = cellInfoWcdma.getCellSignalStrength().getAsuLevel();
                    if (Build.VERSION.SDK_INT >= 24) {
                        zVar.g = cellIdentity4.getUarfcn();
                    }
                    r rVar4 = new r();
                    rVar4.a = 4;
                    rVar4.f = zVar;
                    rVar4.b = cellInfo.isRegistered() ? (byte) 1 : 0;
                    rVar4.c = 1;
                    qVar.c.add(rVar4);
                }
            }
        }
    }

    public static boolean a(Context context) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            return (Build.VERSION.SDK_INT >= 17 ? Settings.Global.getInt(contentResolver, "airplane_mode_on", 0) : Settings.System.getInt(contentResolver, "airplane_mode_on", 0)) != 0;
        } catch (Throwable th) {
            return false;
        }
    }

    public static boolean a(CellInfo cellInfo, CellInfo cellInfo2) {
        if (cellInfo == cellInfo2) {
            return true;
        }
        if (cellInfo == null || cellInfo2 == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 17) {
            if ((cellInfo instanceof CellInfoGsm) && (cellInfo2 instanceof CellInfoGsm)) {
                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                CellIdentityGsm cellIdentity2 = ((CellInfoGsm) cellInfo2).getCellIdentity();
                return cellIdentity.getCid() == cellIdentity2.getCid() && cellIdentity.getLac() == cellIdentity2.getLac();
            } else if ((cellInfo instanceof CellInfoCdma) && (cellInfo2 instanceof CellInfoCdma)) {
                CellIdentityCdma cellIdentity3 = ((CellInfoCdma) cellInfo).getCellIdentity();
                CellIdentityCdma cellIdentity4 = ((CellInfoCdma) cellInfo2).getCellIdentity();
                return cellIdentity3.getBasestationId() == cellIdentity4.getBasestationId() && cellIdentity3.getNetworkId() == cellIdentity4.getNetworkId() && cellIdentity3.getSystemId() == cellIdentity4.getSystemId();
            } else if ((cellInfo instanceof CellInfoLte) && (cellInfo2 instanceof CellInfoLte)) {
                CellIdentityLte cellIdentity5 = ((CellInfoLte) cellInfo).getCellIdentity();
                CellIdentityLte cellIdentity6 = ((CellInfoLte) cellInfo2).getCellIdentity();
                return cellIdentity5.getCi() == cellIdentity6.getCi() && cellIdentity5.getTac() == cellIdentity6.getTac();
            } else if (Build.VERSION.SDK_INT >= 18 && (cellInfo instanceof CellInfoWcdma) && (cellInfo2 instanceof CellInfoWcdma)) {
                CellIdentityWcdma cellIdentity7 = ((CellInfoWcdma) cellInfo).getCellIdentity();
                CellIdentityWcdma cellIdentity8 = ((CellInfoWcdma) cellInfo2).getCellIdentity();
                return cellIdentity7.getCid() == cellIdentity8.getCid() && cellIdentity7.getLac() == cellIdentity8.getLac();
            }
        }
        return false;
    }

    public static boolean a(CellLocation cellLocation, CellLocation cellLocation2) {
        if (cellLocation == cellLocation2) {
            return true;
        }
        if (cellLocation == null || cellLocation2 == null) {
            return false;
        }
        if ((cellLocation instanceof GsmCellLocation) && (cellLocation2 instanceof GsmCellLocation)) {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
            GsmCellLocation gsmCellLocation2 = (GsmCellLocation) cellLocation2;
            return gsmCellLocation.getCid() == gsmCellLocation2.getCid() && gsmCellLocation.getLac() == gsmCellLocation2.getLac();
        } else if (!(cellLocation instanceof CdmaCellLocation) || !(cellLocation2 instanceof CdmaCellLocation)) {
            return false;
        } else {
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
            CdmaCellLocation cdmaCellLocation2 = (CdmaCellLocation) cellLocation2;
            return cdmaCellLocation.getBaseStationId() == cdmaCellLocation2.getBaseStationId() && cdmaCellLocation.getNetworkId() == cdmaCellLocation2.getNetworkId() && cdmaCellLocation.getSystemId() == cdmaCellLocation2.getSystemId();
        }
    }

    private static String b(TelephonyManager telephonyManager) {
        String str = null;
        try {
            str = telephonyManager.getNetworkOperator();
        } catch (Throwable th) {
        }
        return str == null ? "" : str;
    }
}
