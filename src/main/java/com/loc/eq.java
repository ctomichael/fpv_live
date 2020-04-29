package com.loc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import dji.component.flysafe.FlyForbidProtocol;
import java.util.ArrayList;
import org.bouncycastle.asn1.eac.CertificateBody;

@SuppressLint({"NewApi"})
/* compiled from: Req */
public final class eq {
    protected static String J = null;
    protected static String L = null;
    protected String A = null;
    protected String B = null;
    protected ArrayList<ed> C = new ArrayList<>();
    protected String D = null;
    protected String E = null;
    protected ArrayList<ScanResult> F = new ArrayList<>();
    protected String G = null;
    protected String H = null;
    protected byte[] I = null;
    protected String K = null;
    protected String M = null;
    protected String N = null;
    private byte[] O = null;
    private int P = 0;
    public String a = "1";
    protected short b = 0;
    protected String c = null;
    protected String d = null;
    protected String e = null;
    protected String f = null;
    protected String g = null;
    public String h = null;
    public String i = null;
    protected String j = null;
    protected String k = null;
    protected String l = null;
    protected String m = null;
    protected String n = null;
    protected String o = null;
    protected String p = null;
    protected String q = null;
    protected String r = null;
    protected String s = null;
    protected String t = null;
    protected String u = null;
    protected String v = null;
    protected String w = null;
    protected String x = null;
    protected String y = null;
    protected int z = 0;

    private static int a(String str, byte[] bArr, int i2) {
        int i3 = CertificateBody.profileType;
        try {
            if (TextUtils.isEmpty(str)) {
                bArr[i2] = 0;
                return i2 + 1;
            }
            byte[] bytes = str.getBytes("GBK");
            int length = bytes.length;
            if (length <= 127) {
                i3 = length;
            }
            bArr[i2] = (byte) i3;
            int i4 = i2 + 1;
            System.arraycopy(bytes, 0, bArr, i4, i3);
            return i3 + i4;
        } catch (Throwable th) {
            es.a(th, "Req", "copyContentWithByteLen");
            bArr[i2] = 0;
            return i2 + 1;
        }
    }

    private String a(String str, int i2) {
        String[] split = this.B.split("\\*")[i2].split(",");
        if ("lac".equals(str)) {
            return split[0];
        }
        if ("cellid".equals(str)) {
            return split[1];
        }
        if ("signal".equals(str)) {
            return split[2];
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000f, code lost:
        if (r0.length != 6) goto L_0x0011;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] a(java.lang.String r8) {
        /*
            r7 = this;
            r6 = 2
            r2 = 0
            r5 = 6
            java.lang.String r0 = ":"
            java.lang.String[] r0 = r8.split(r0)
            byte[] r1 = new byte[r5]
            if (r0 == 0) goto L_0x0011
            int r3 = r0.length     // Catch:{ Throwable -> 0x0044 }
            if (r3 == r5) goto L_0x001f
        L_0x0011:
            r0 = 6
            java.lang.String[] r0 = new java.lang.String[r0]     // Catch:{ Throwable -> 0x0044 }
            r3 = r2
        L_0x0015:
            if (r3 >= r5) goto L_0x001f
            java.lang.String r4 = "0"
            r0[r3] = r4     // Catch:{ Throwable -> 0x0044 }
            int r3 = r3 + 1
            goto L_0x0015
        L_0x001f:
            int r3 = r0.length     // Catch:{ Throwable -> 0x0044 }
            if (r2 >= r3) goto L_0x0042
            r3 = r0[r2]     // Catch:{ Throwable -> 0x0044 }
            int r3 = r3.length()     // Catch:{ Throwable -> 0x0044 }
            if (r3 <= r6) goto L_0x0034
            r3 = r0[r2]     // Catch:{ Throwable -> 0x0044 }
            r4 = 0
            r5 = 2
            java.lang.String r3 = r3.substring(r4, r5)     // Catch:{ Throwable -> 0x0044 }
            r0[r2] = r3     // Catch:{ Throwable -> 0x0044 }
        L_0x0034:
            r3 = r0[r2]     // Catch:{ Throwable -> 0x0044 }
            r4 = 16
            int r3 = java.lang.Integer.parseInt(r3, r4)     // Catch:{ Throwable -> 0x0044 }
            byte r3 = (byte) r3     // Catch:{ Throwable -> 0x0044 }
            r1[r2] = r3     // Catch:{ Throwable -> 0x0044 }
            int r2 = r2 + 1
            goto L_0x001f
        L_0x0042:
            r0 = r1
        L_0x0043:
            return r0
        L_0x0044:
            r0 = move-exception
            java.lang.String r1 = "Req"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "getMacBa "
            r2.<init>(r3)
            java.lang.StringBuilder r2 = r2.append(r8)
            java.lang.String r2 = r2.toString()
            com.loc.es.a(r0, r1, r2)
            java.lang.String r0 = "00:00:00:00:00:00"
            byte[] r0 = r7.a(r0)
            goto L_0x0043
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.eq.a(java.lang.String):byte[]");
    }

    private String b(String str) {
        if (!this.A.contains(str + ">")) {
            return "0";
        }
        int indexOf = this.A.indexOf(str + ">");
        return this.A.substring(indexOf + str.length() + 1, this.A.indexOf("</" + str));
    }

    public final void a(Context context, boolean z2, boolean z3, ee eeVar, eg egVar, ConnectivityManager connectivityManager, String str, String str2) {
        String str3;
        String str4;
        String sb;
        String f2 = u.f(context);
        int g2 = fa.g();
        String str5 = "";
        String str6 = "";
        this.K = str2;
        String str7 = "api_serverSDK_130905";
        if (!z3) {
            str7 = "UC_nlp_20131029";
            str3 = "BKZCHMBBSSUK7U8GLUKHBB56CCFF78U";
        } else {
            str3 = "S128DF1572465B890OE3F7A13167KLEI";
        }
        StringBuilder sb2 = new StringBuilder();
        int e2 = eeVar.e();
        int f3 = eeVar.f();
        TelephonyManager g3 = eeVar.g();
        ArrayList<ed> a2 = eeVar.a();
        ArrayList<ed> b2 = eeVar.b();
        ArrayList<ScanResult> c2 = egVar.c();
        String str8 = f3 == 2 ? "1" : "0";
        if (g3 != null) {
            if (TextUtils.isEmpty(es.d)) {
                try {
                    es.d = x.u(context);
                } catch (Throwable th) {
                    es.a(th, "Aps", "getApsReq part4");
                }
            }
            if (TextUtils.isEmpty(es.d) && Build.VERSION.SDK_INT < 29) {
                es.d = "888888888888888";
            }
            if (TextUtils.isEmpty(es.e)) {
                try {
                    es.e = g3.getSubscriberId();
                } catch (SecurityException e3) {
                } catch (Throwable th2) {
                    es.a(th2, "Aps", "getApsReq part2");
                }
            }
            if (TextUtils.isEmpty(es.e) && Build.VERSION.SDK_INT < 29) {
                es.e = "888888888888888";
            }
        }
        NetworkInfo networkInfo = null;
        try {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Throwable th3) {
            es.a(th3, "Aps", "getApsReq part");
        }
        boolean a3 = egVar.a(connectivityManager);
        if (fa.a(networkInfo) != -1) {
            str5 = fa.b(g3);
            str6 = a3 ? "2" : "1";
        }
        if (!a2.isEmpty()) {
            StringBuilder sb3 = new StringBuilder();
            switch (f3) {
                case 1:
                    ed edVar = a2.get(0);
                    sb3.delete(0, sb3.length());
                    sb3.append("<mcc>").append(edVar.a).append("</mcc>");
                    sb3.append("<mnc>").append(edVar.b).append("</mnc>");
                    sb3.append("<lac>").append(edVar.c).append("</lac>");
                    sb3.append("<cellid>").append(edVar.d);
                    sb3.append("</cellid>");
                    sb3.append("<signal>").append(edVar.j);
                    sb3.append("</signal>");
                    String sb4 = sb3.toString();
                    int i2 = 1;
                    while (true) {
                        int i3 = i2;
                        if (i3 >= a2.size()) {
                            sb = sb4;
                            break;
                        } else {
                            ed edVar2 = a2.get(i3);
                            sb2.append(edVar2.c).append(",");
                            sb2.append(edVar2.d).append(",");
                            sb2.append(edVar2.j);
                            if (i3 < a2.size() - 1) {
                                sb2.append("*");
                            }
                            i2 = i3 + 1;
                        }
                    }
                case 2:
                    ed edVar3 = a2.get(0);
                    sb3.delete(0, sb3.length());
                    sb3.append("<mcc>").append(edVar3.a).append("</mcc>");
                    sb3.append("<sid>").append(edVar3.g).append("</sid>");
                    sb3.append("<nid>").append(edVar3.h).append("</nid>");
                    sb3.append("<bid>").append(edVar3.i).append("</bid>");
                    if (edVar3.f > 0 && edVar3.e > 0) {
                        sb3.append("<lon>").append(edVar3.f).append("</lon>");
                        sb3.append("<lat>").append(edVar3.e).append("</lat>");
                    }
                    sb3.append("<signal>").append(edVar3.j).append("</signal>");
                    sb = sb3.toString();
                    break;
                default:
                    sb = "";
                    break;
            }
            sb3.delete(0, sb3.length());
            str4 = sb;
        } else {
            str4 = "";
        }
        if ((e2 & 4) != 4 || b2.isEmpty()) {
            this.C.clear();
        } else {
            this.C.clear();
            this.C.addAll(b2);
        }
        StringBuilder sb5 = new StringBuilder();
        if (egVar.p) {
            if (a3) {
                WifiInfo g4 = egVar.g();
                if (eg.a(g4)) {
                    sb5.append(g4.getBSSID()).append(",");
                    int rssi = g4.getRssi();
                    if (rssi < -128) {
                        rssi = 0;
                    } else if (rssi > 127) {
                        rssi = 0;
                    }
                    sb5.append(rssi).append(",");
                    String ssid = g4.getSSID();
                    int i4 = 32;
                    try {
                        i4 = g4.getSSID().getBytes("UTF-8").length;
                    } catch (Exception e4) {
                    }
                    if (i4 >= 32) {
                        ssid = "unkwn";
                    }
                    sb5.append(ssid.replace("*", "."));
                }
            }
            if (!(c2 == null || this.F == null)) {
                this.F.clear();
                this.F.addAll(c2);
            }
        } else {
            egVar.d();
            if (this.F != null) {
                this.F.clear();
            }
        }
        this.b = 0;
        if (!z2) {
            this.b = (short) (this.b | 2);
        }
        this.c = str7;
        this.d = str3;
        this.f = fa.e();
        this.g = FlyForbidProtocol.PLATFORM_NAME_FOR_JNI + fa.f();
        this.h = fa.b(context);
        this.i = str8;
        this.j = "0";
        this.k = "0";
        this.l = "0";
        this.m = "0";
        this.n = "0";
        this.o = f2;
        this.p = es.d;
        this.q = es.e;
        this.s = String.valueOf(g2);
        this.t = fa.j(context);
        this.v = "4.7.1";
        this.w = str;
        this.u = "";
        this.x = str5;
        this.y = str6;
        this.z = e2;
        this.A = str4;
        this.B = sb2.toString();
        this.D = eeVar.k();
        this.G = eg.k();
        this.E = sb5.toString();
        try {
            if (TextUtils.isEmpty(J)) {
                J = x.g(context);
            }
        } catch (Throwable th4) {
        }
        try {
            if (TextUtils.isEmpty(L)) {
                L = x.a(context);
            }
        } catch (Throwable th5) {
        }
        try {
            if (TextUtils.isEmpty(this.N)) {
                this.N = x.h(context);
            }
        } catch (Throwable th6) {
        }
        sb2.delete(0, sb2.length());
        sb5.delete(0, sb5.length());
    }

    /* JADX WARNING: Removed duplicated region for block: B:231:0x06a1  */
    /* JADX WARNING: Removed duplicated region for block: B:234:0x06b6  */
    /* JADX WARNING: Removed duplicated region for block: B:239:0x06d0 A[Catch:{ Throwable -> 0x0904 }] */
    /* JADX WARNING: Removed duplicated region for block: B:241:0x06d3 A[Catch:{ Throwable -> 0x0904 }] */
    /* JADX WARNING: Removed duplicated region for block: B:247:0x06e8 A[Catch:{ Throwable -> 0x090c }] */
    /* JADX WARNING: Removed duplicated region for block: B:251:0x06fd  */
    /* JADX WARNING: Removed duplicated region for block: B:265:0x073a  */
    /* JADX WARNING: Removed duplicated region for block: B:268:0x074d  */
    /* JADX WARNING: Removed duplicated region for block: B:271:0x0771  */
    /* JADX WARNING: Removed duplicated region for block: B:274:0x07a5  */
    /* JADX WARNING: Removed duplicated region for block: B:298:0x083c  */
    /* JADX WARNING: Removed duplicated region for block: B:329:0x08f5 A[SYNTHETIC, Splitter:B:329:0x08f5] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final byte[] a() {
        /*
            r18 = this;
            r0 = r18
            java.lang.String r2 = r0.a
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0011
            java.lang.String r2 = ""
            r0 = r18
            r0.a = r2
        L_0x0011:
            r0 = r18
            java.lang.String r2 = r0.c
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0022
            java.lang.String r2 = ""
            r0 = r18
            r0.c = r2
        L_0x0022:
            r0 = r18
            java.lang.String r2 = r0.d
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0033
            java.lang.String r2 = ""
            r0 = r18
            r0.d = r2
        L_0x0033:
            r0 = r18
            java.lang.String r2 = r0.e
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0044
            java.lang.String r2 = ""
            r0 = r18
            r0.e = r2
        L_0x0044:
            r0 = r18
            java.lang.String r2 = r0.f
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0055
            java.lang.String r2 = ""
            r0 = r18
            r0.f = r2
        L_0x0055:
            r0 = r18
            java.lang.String r2 = r0.g
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0066
            java.lang.String r2 = ""
            r0 = r18
            r0.g = r2
        L_0x0066:
            r0 = r18
            java.lang.String r2 = r0.h
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0077
            java.lang.String r2 = ""
            r0 = r18
            r0.h = r2
        L_0x0077:
            r0 = r18
            java.lang.String r2 = r0.i
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0088
            java.lang.String r2 = ""
            r0 = r18
            r0.i = r2
        L_0x0088:
            r0 = r18
            java.lang.String r2 = r0.j
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x04ab
            java.lang.String r2 = "0"
            r0 = r18
            r0.j = r2
        L_0x0099:
            r0 = r18
            java.lang.String r2 = r0.k
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x04ce
            java.lang.String r2 = "0"
            r0 = r18
            r0.k = r2
        L_0x00aa:
            r0 = r18
            java.lang.String r2 = r0.l
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x00bb
            java.lang.String r2 = ""
            r0 = r18
            r0.l = r2
        L_0x00bb:
            r0 = r18
            java.lang.String r2 = r0.m
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x00cc
            java.lang.String r2 = ""
            r0 = r18
            r0.m = r2
        L_0x00cc:
            r0 = r18
            java.lang.String r2 = r0.n
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x00dd
            java.lang.String r2 = ""
            r0 = r18
            r0.n = r2
        L_0x00dd:
            r0 = r18
            java.lang.String r2 = r0.o
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x00ee
            java.lang.String r2 = ""
            r0 = r18
            r0.o = r2
        L_0x00ee:
            r0 = r18
            java.lang.String r2 = r0.p
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x00ff
            java.lang.String r2 = ""
            r0 = r18
            r0.p = r2
        L_0x00ff:
            r0 = r18
            java.lang.String r2 = r0.q
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0110
            java.lang.String r2 = ""
            r0 = r18
            r0.q = r2
        L_0x0110:
            r0 = r18
            java.lang.String r2 = r0.r
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0121
            java.lang.String r2 = ""
            r0 = r18
            r0.r = r2
        L_0x0121:
            r0 = r18
            java.lang.String r2 = r0.s
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0132
            java.lang.String r2 = ""
            r0 = r18
            r0.s = r2
        L_0x0132:
            r0 = r18
            java.lang.String r2 = r0.t
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0143
            java.lang.String r2 = ""
            r0 = r18
            r0.t = r2
        L_0x0143:
            r0 = r18
            java.lang.String r2 = r0.u
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0154
            java.lang.String r2 = ""
            r0 = r18
            r0.u = r2
        L_0x0154:
            r0 = r18
            java.lang.String r2 = r0.v
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0165
            java.lang.String r2 = ""
            r0 = r18
            r0.v = r2
        L_0x0165:
            r0 = r18
            java.lang.String r2 = r0.w
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0176
            java.lang.String r2 = ""
            r0 = r18
            r0.w = r2
        L_0x0176:
            r0 = r18
            java.lang.String r2 = r0.x
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0187
            java.lang.String r2 = ""
            r0 = r18
            r0.x = r2
        L_0x0187:
            r0 = r18
            java.lang.String r2 = r0.y
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x04f1
            java.lang.String r2 = "0"
            r0 = r18
            r0.y = r2
        L_0x0198:
            r0 = r18
            int r2 = r0.z
            if (r2 <= 0) goto L_0x01a2
            r3 = 15
            if (r2 <= r3) goto L_0x0514
        L_0x01a2:
            r2 = 0
        L_0x01a3:
            if (r2 != 0) goto L_0x01aa
            r2 = 0
            r0 = r18
            r0.z = r2
        L_0x01aa:
            r0 = r18
            java.lang.String r2 = r0.A
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x01bb
            java.lang.String r2 = ""
            r0 = r18
            r0.A = r2
        L_0x01bb:
            r0 = r18
            java.lang.String r2 = r0.B
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x01cc
            java.lang.String r2 = ""
            r0 = r18
            r0.B = r2
        L_0x01cc:
            r0 = r18
            java.lang.String r2 = r0.E
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x01dd
            java.lang.String r2 = ""
            r0 = r18
            r0.E = r2
        L_0x01dd:
            r0 = r18
            java.lang.String r2 = r0.G
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x01ee
            java.lang.String r2 = ""
            r0 = r18
            r0.G = r2
        L_0x01ee:
            r0 = r18
            java.lang.String r2 = r0.H
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x01ff
            java.lang.String r2 = ""
            r0 = r18
            r0.H = r2
        L_0x01ff:
            java.lang.String r2 = com.loc.eq.J
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x020c
            java.lang.String r2 = ""
            com.loc.eq.J = r2
        L_0x020c:
            r0 = r18
            byte[] r2 = r0.I
            if (r2 != 0) goto L_0x0219
            r2 = 0
            byte[] r2 = new byte[r2]
            r0 = r18
            r0.I = r2
        L_0x0219:
            r0 = r18
            java.lang.String r2 = r0.N
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x022a
            java.lang.String r2 = ""
            r0 = r18
            r0.N = r2
        L_0x022a:
            r2 = 2
            byte[] r10 = new byte[r2]
            r2 = 4
            byte[] r8 = new byte[r2]
            r2 = 4096(0x1000, float:5.74E-42)
            r0 = r18
            byte[] r3 = r0.I
            if (r3 == 0) goto L_0x0241
            r0 = r18
            byte[] r2 = r0.I
            int r2 = r2.length
            int r2 = r2 + 1
            int r2 = r2 + 4096
        L_0x0241:
            r0 = r18
            byte[] r3 = r0.O
            if (r3 == 0) goto L_0x024d
            r0 = r18
            int r3 = r0.P
            if (r2 <= r3) goto L_0x0517
        L_0x024d:
            byte[] r3 = new byte[r2]
            r0 = r18
            r0.O = r3
            r0 = r18
            r0.P = r2
        L_0x0257:
            r2 = 0
            r0 = r18
            java.lang.String r4 = r0.a
            byte r4 = com.loc.fa.i(r4)
            r3[r2] = r4
            r0 = r18
            short r2 = r0.b
            r4 = 0
            byte[] r2 = com.loc.fa.a(r2, r4)
            r4 = 0
            r5 = 1
            int r6 = r2.length
            java.lang.System.arraycopy(r2, r4, r3, r5, r6)
            int r2 = r2.length
            int r2 = r2 + 1
            r0 = r18
            java.lang.String r4 = r0.c
            int r2 = a(r4, r3, r2)
            r0 = r18
            java.lang.String r4 = r0.d
            int r2 = a(r4, r3, r2)
            r0 = r18
            java.lang.String r4 = r0.o
            int r2 = a(r4, r3, r2)
            r0 = r18
            java.lang.String r4 = r0.e
            int r2 = a(r4, r3, r2)
            r0 = r18
            java.lang.String r4 = r0.f
            int r2 = a(r4, r3, r2)
            r0 = r18
            java.lang.String r4 = r0.g
            int r2 = a(r4, r3, r2)
            r0 = r18
            java.lang.String r4 = r0.u
            int r2 = a(r4, r3, r2)
            r0 = r18
            java.lang.String r4 = r0.h
            int r2 = a(r4, r3, r2)
            r0 = r18
            java.lang.String r4 = r0.p
            int r2 = a(r4, r3, r2)
            r0 = r18
            java.lang.String r4 = r0.q
            int r4 = a(r4, r3, r2)
            r0 = r18
            java.lang.String r2 = r0.t     // Catch:{ Throwable -> 0x0537 }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x0537 }
            if (r2 == 0) goto L_0x051e
            r2 = 0
            r3[r4] = r2     // Catch:{ Throwable -> 0x0537 }
            int r2 = r4 + 1
        L_0x02d3:
            r0 = r18
            java.lang.String r4 = r0.v
            int r2 = a(r4, r3, r2)
            r0 = r18
            java.lang.String r4 = r0.w
            int r2 = a(r4, r3, r2)
            java.lang.String r4 = com.loc.eq.J
            int r2 = a(r4, r3, r2)
            java.lang.String r4 = com.loc.eq.L
            int r2 = a(r4, r3, r2)
            r0 = r18
            java.lang.String r4 = r0.x
            int r2 = a(r4, r3, r2)
            r0 = r18
            java.lang.String r4 = r0.y
            byte r4 = java.lang.Byte.parseByte(r4)
            r3[r2] = r4
            int r2 = r2 + 1
            r0 = r18
            java.lang.String r4 = r0.j
            byte r4 = java.lang.Byte.parseByte(r4)
            r3[r2] = r4
            int r2 = r2 + 1
            r0 = r18
            int r4 = r0.z
            r5 = r4 & 3
            r0 = r18
            int r4 = r0.z
            byte r4 = (byte) r4
            r3[r2] = r4
            int r2 = r2 + 1
            r4 = 1
            if (r5 == r4) goto L_0x0324
            r4 = 2
            if (r5 != r4) goto L_0x03ac
        L_0x0324:
            java.lang.String r4 = "mcc"
            r0 = r18
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.fa.b(r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            r4 = 1
            if (r5 != r4) goto L_0x0548
            java.lang.String r4 = "mnc"
            r0 = r18
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.fa.b(r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            java.lang.String r4 = "lac"
            r0 = r18
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.fa.b(r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            java.lang.String r4 = "cellid"
            r0 = r18
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.fa.c(r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
        L_0x0377:
            java.lang.String r4 = "signal"
            r0 = r18
            java.lang.String r4 = r0.b(r4)
            int r4 = java.lang.Integer.parseInt(r4)
            r6 = 127(0x7f, float:1.78E-43)
            if (r4 <= r6) goto L_0x05b1
            r4 = 0
        L_0x0389:
            byte r4 = (byte) r4
            r3[r2] = r4
            int r2 = r2 + 1
            r4 = 0
            byte[] r4 = com.loc.fa.a(r4, r10)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r2 = r2 + 2
            r4 = 1
            if (r5 != r4) goto L_0x0614
            r0 = r18
            java.lang.String r4 = r0.B
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 == 0) goto L_0x05b8
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
        L_0x03ac:
            r0 = r18
            java.lang.String r4 = r0.D
            if (r4 == 0) goto L_0x061f
            r0 = r18
            int r5 = r0.z
            r5 = r5 & 8
            r6 = 8
            if (r5 != r6) goto L_0x061f
            java.lang.String r5 = "GBK"
            byte[] r4 = r4.getBytes(r5)     // Catch:{ Exception -> 0x061e }
            int r5 = r4.length     // Catch:{ Exception -> 0x061e }
            r6 = 60
            int r5 = java.lang.Math.min(r5, r6)     // Catch:{ Exception -> 0x061e }
            byte r6 = (byte) r5     // Catch:{ Exception -> 0x061e }
            r3[r2] = r6     // Catch:{ Exception -> 0x061e }
            int r2 = r2 + 1
            r6 = 0
            java.lang.System.arraycopy(r4, r6, r3, r2, r5)     // Catch:{ Exception -> 0x061e }
            int r2 = r2 + r5
            r4 = r2
        L_0x03d5:
            r0 = r18
            java.util.ArrayList<com.loc.ed> r9 = r0.C
            int r5 = r9.size()
            r0 = r18
            int r2 = r0.z
            r2 = r2 & 4
            r6 = 4
            if (r2 != r6) goto L_0x0692
            if (r5 <= 0) goto L_0x0692
            r2 = 0
            java.lang.Object r2 = r9.get(r2)
            com.loc.ed r2 = (com.loc.ed) r2
            boolean r2 = r2.p
            if (r2 != 0) goto L_0x03f6
            int r2 = r5 + -1
            r5 = r2
        L_0x03f6:
            byte r2 = (byte) r5
            r3[r4] = r2
            int r4 = r4 + 1
            r2 = 0
            r7 = r2
        L_0x03fd:
            if (r7 >= r5) goto L_0x0697
            java.lang.Object r2 = r9.get(r7)
            com.loc.ed r2 = (com.loc.ed) r2
            boolean r6 = r2.p
            if (r6 == 0) goto L_0x04a6
            int r6 = r2.k
            r11 = 1
            if (r6 == r11) goto L_0x0418
            int r6 = r2.k
            r11 = 3
            if (r6 == r11) goto L_0x0418
            int r6 = r2.k
            r11 = 4
            if (r6 != r11) goto L_0x0627
        L_0x0418:
            int r6 = r2.k
            byte r6 = (byte) r6
            boolean r11 = r2.n
            if (r11 == 0) goto L_0x0422
            r6 = r6 | 8
            byte r6 = (byte) r6
        L_0x0422:
            r3[r4] = r6
            int r4 = r4 + 1
            int r6 = r2.a
            byte[] r6 = com.loc.fa.a(r6, r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.b
            byte[] r6 = com.loc.fa.a(r6, r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.c
            byte[] r6 = com.loc.fa.a(r6, r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.d
            byte[] r6 = com.loc.fa.b(r6, r8)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
        L_0x045a:
            int r6 = r2.j
            r11 = 127(0x7f, float:1.78E-43)
            if (r6 <= r11) goto L_0x068a
            r6 = 99
        L_0x0462:
            byte r6 = (byte) r6
            r3[r4] = r6
            int r4 = r4 + 1
            short r6 = r2.l
            byte[] r6 = com.loc.fa.a(r6, r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            java.lang.String r6 = "5.1"
            java.lang.Double r6 = java.lang.Double.valueOf(r6)
            double r12 = r6.doubleValue()
            r14 = 4617315517961601024(0x4014000000000000, double:5.0)
            int r6 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
            if (r6 < 0) goto L_0x04a6
            int r6 = r2.k
            r11 = 3
            if (r6 == r11) goto L_0x048f
            int r6 = r2.k
            r11 = 4
            if (r6 != r11) goto L_0x04a6
        L_0x048f:
            int r2 = r2.o
            r6 = 32767(0x7fff, float:4.5916E-41)
            if (r2 <= r6) goto L_0x0497
            r2 = 32767(0x7fff, float:4.5916E-41)
        L_0x0497:
            if (r2 >= 0) goto L_0x049b
            r2 = 32767(0x7fff, float:4.5916E-41)
        L_0x049b:
            byte[] r2 = com.loc.fa.a(r2, r10)
            r6 = 0
            int r11 = r2.length
            java.lang.System.arraycopy(r2, r6, r3, r4, r11)
            int r2 = r2.length
            int r4 = r4 + r2
        L_0x04a6:
            int r2 = r7 + 1
            r7 = r2
            goto L_0x03fd
        L_0x04ab:
            java.lang.String r2 = "0"
            r0 = r18
            java.lang.String r3 = r0.j
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0099
            java.lang.String r2 = "2"
            r0 = r18
            java.lang.String r3 = r0.j
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0099
            java.lang.String r2 = "0"
            r0 = r18
            r0.j = r2
            goto L_0x0099
        L_0x04ce:
            java.lang.String r2 = "0"
            r0 = r18
            java.lang.String r3 = r0.k
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x00aa
            java.lang.String r2 = "1"
            r0 = r18
            java.lang.String r3 = r0.k
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x00aa
            java.lang.String r2 = "0"
            r0 = r18
            r0.k = r2
            goto L_0x00aa
        L_0x04f1:
            java.lang.String r2 = "1"
            r0 = r18
            java.lang.String r3 = r0.y
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0198
            java.lang.String r2 = "2"
            r0 = r18
            java.lang.String r3 = r0.y
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0198
            java.lang.String r2 = "0"
            r0 = r18
            r0.y = r2
            goto L_0x0198
        L_0x0514:
            r2 = 1
            goto L_0x01a3
        L_0x0517:
            r0 = r18
            byte[] r2 = r0.O
            r3 = r2
            goto L_0x0257
        L_0x051e:
            r0 = r18
            java.lang.String r2 = r0.t     // Catch:{ Throwable -> 0x0537 }
            r0 = r18
            byte[] r2 = r0.a(r2)     // Catch:{ Throwable -> 0x0537 }
            int r5 = r2.length     // Catch:{ Throwable -> 0x0537 }
            byte r5 = (byte) r5     // Catch:{ Throwable -> 0x0537 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x0537 }
            int r4 = r4 + 1
            r5 = 0
            int r6 = r2.length     // Catch:{ Throwable -> 0x0537 }
            java.lang.System.arraycopy(r2, r5, r3, r4, r6)     // Catch:{ Throwable -> 0x0537 }
            int r2 = r2.length     // Catch:{ Throwable -> 0x0537 }
            int r2 = r2 + r4
            goto L_0x02d3
        L_0x0537:
            r2 = move-exception
            java.lang.String r5 = "Req"
            java.lang.String r6 = "buildV4Dot219"
            com.loc.es.a(r2, r5, r6)
            r2 = 0
            r3[r4] = r2
            int r2 = r4 + 1
            goto L_0x02d3
        L_0x0548:
            r4 = 2
            if (r5 != r4) goto L_0x0377
            java.lang.String r4 = "sid"
            r0 = r18
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.fa.b(r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            java.lang.String r4 = "nid"
            r0 = r18
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.fa.b(r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            java.lang.String r4 = "bid"
            r0 = r18
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.fa.b(r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            java.lang.String r4 = "lon"
            r0 = r18
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.fa.c(r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            java.lang.String r4 = "lat"
            r0 = r18
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.fa.c(r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            goto L_0x0377
        L_0x05b1:
            r6 = -128(0xffffffffffffff80, float:NaN)
            if (r4 >= r6) goto L_0x0389
            r4 = 0
            goto L_0x0389
        L_0x05b8:
            r0 = r18
            java.lang.String r4 = r0.B
            java.lang.String r5 = "\\*"
            java.lang.String[] r4 = r4.split(r5)
            int r5 = r4.length
            byte r4 = (byte) r5
            r3[r2] = r4
            int r2 = r2 + 1
            r4 = 0
        L_0x05ca:
            if (r4 >= r5) goto L_0x03ac
            java.lang.String r6 = "lac"
            r0 = r18
            java.lang.String r6 = r0.a(r6, r4)
            byte[] r6 = com.loc.fa.b(r6)
            r7 = 0
            int r9 = r6.length
            java.lang.System.arraycopy(r6, r7, r3, r2, r9)
            int r6 = r6.length
            int r2 = r2 + r6
            java.lang.String r6 = "cellid"
            r0 = r18
            java.lang.String r6 = r0.a(r6, r4)
            byte[] r6 = com.loc.fa.c(r6)
            r7 = 0
            int r9 = r6.length
            java.lang.System.arraycopy(r6, r7, r3, r2, r9)
            int r6 = r6.length
            int r6 = r6 + r2
            java.lang.String r2 = "signal"
            r0 = r18
            java.lang.String r2 = r0.a(r2, r4)
            int r2 = java.lang.Integer.parseInt(r2)
            r7 = 127(0x7f, float:1.78E-43)
            if (r2 <= r7) goto L_0x060e
            r2 = 0
        L_0x0606:
            byte r2 = (byte) r2
            r3[r6] = r2
            int r2 = r6 + 1
            int r4 = r4 + 1
            goto L_0x05ca
        L_0x060e:
            r7 = -128(0xffffffffffffff80, float:NaN)
            if (r2 >= r7) goto L_0x0606
            r2 = 0
            goto L_0x0606
        L_0x0614:
            r4 = 2
            if (r5 != r4) goto L_0x03ac
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
            goto L_0x03ac
        L_0x061e:
            r4 = move-exception
        L_0x061f:
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
            r4 = r2
            goto L_0x03d5
        L_0x0627:
            int r6 = r2.k
            r11 = 2
            if (r6 != r11) goto L_0x045a
            int r6 = r2.k
            byte r6 = (byte) r6
            boolean r11 = r2.n
            if (r11 == 0) goto L_0x0636
            r6 = r6 | 8
            byte r6 = (byte) r6
        L_0x0636:
            r3[r4] = r6
            int r4 = r4 + 1
            int r6 = r2.a
            byte[] r6 = com.loc.fa.a(r6, r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.g
            byte[] r6 = com.loc.fa.a(r6, r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.h
            byte[] r6 = com.loc.fa.a(r6, r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.i
            byte[] r6 = com.loc.fa.a(r6, r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.f
            byte[] r6 = com.loc.fa.b(r6, r8)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.e
            byte[] r6 = com.loc.fa.b(r6, r8)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            goto L_0x045a
        L_0x068a:
            r11 = -128(0xffffffffffffff80, float:NaN)
            if (r6 >= r11) goto L_0x0462
            r6 = 99
            goto L_0x0462
        L_0x0692:
            r2 = 0
            r3[r4] = r2
            int r4 = r4 + 1
        L_0x0697:
            r0 = r18
            java.lang.String r2 = r0.E
            int r2 = r2.length()
            if (r2 != 0) goto L_0x07a5
            r2 = 0
            r3[r4] = r2
            int r2 = r4 + 1
        L_0x06a6:
            r0 = r18
            java.util.ArrayList<android.net.wifi.ScanResult> r11 = r0.F
            int r4 = r11.size()
            r5 = 25
            int r12 = java.lang.Math.min(r4, r5)
            if (r12 != 0) goto L_0x083c
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
        L_0x06bb:
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
            r0 = r18
            java.lang.String r4 = r0.H     // Catch:{ Throwable -> 0x0904 }
            java.lang.String r5 = "GBK"
            byte[] r4 = r4.getBytes(r5)     // Catch:{ Throwable -> 0x0904 }
            int r5 = r4.length     // Catch:{ Throwable -> 0x0904 }
            r6 = 127(0x7f, float:1.78E-43)
            if (r5 <= r6) goto L_0x06d1
            r4 = 0
        L_0x06d1:
            if (r4 != 0) goto L_0x08f5
            r4 = 0
            r3[r2] = r4     // Catch:{ Throwable -> 0x0904 }
            int r2 = r2 + 1
        L_0x06d8:
            r4 = 2
            byte[] r4 = new byte[r4]
            r4 = {0, 0} // fill-array
            r0 = r18
            java.lang.String r5 = r0.K     // Catch:{ Throwable -> 0x090c }
            boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x090c }
            if (r5 != 0) goto L_0x06f4
            r0 = r18
            java.lang.String r4 = r0.K     // Catch:{ Throwable -> 0x090c }
            int r4 = r4.length()     // Catch:{ Throwable -> 0x090c }
            byte[] r4 = com.loc.fa.a(r4, r10)     // Catch:{ Throwable -> 0x090c }
        L_0x06f4:
            r6 = 0
            r7 = 2
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)     // Catch:{ Throwable -> 0x090c }
            int r2 = r2 + 2
            if (r5 != 0) goto L_0x070f
            r0 = r18
            java.lang.String r4 = r0.K     // Catch:{ Throwable -> 0x091b }
            java.lang.String r5 = "GBK"
            byte[] r4 = r4.getBytes(r5)     // Catch:{ Throwable -> 0x091b }
            r5 = 0
            int r6 = r4.length     // Catch:{ Throwable -> 0x091b }
            java.lang.System.arraycopy(r4, r5, r3, r2, r6)     // Catch:{ Throwable -> 0x091b }
            int r4 = r4.length     // Catch:{ Throwable -> 0x091b }
            int r2 = r2 + r4
        L_0x070f:
            r4 = 2
            byte[] r4 = new byte[r4]
            r5 = 0
            r6 = 0
            r4[r5] = r6
            r5 = 1
            r6 = 0
            r4[r5] = r6
            r4 = 0
            byte[] r4 = com.loc.fa.a(r4, r10)     // Catch:{ Throwable -> 0x0911 }
            r5 = 0
            r6 = 2
            java.lang.System.arraycopy(r4, r5, r3, r2, r6)     // Catch:{ Throwable -> 0x0911 }
            int r2 = r2 + 2
        L_0x0726:
            r4 = 2
            byte[] r4 = new byte[r4]
            r4 = {0, 0} // fill-array
            r5 = 0
            r6 = 2
            java.lang.System.arraycopy(r4, r5, r3, r2, r6)     // Catch:{ Throwable -> 0x0916 }
            int r2 = r2 + 2
        L_0x0733:
            r4 = 0
            r0 = r18
            byte[] r5 = r0.I
            if (r5 == 0) goto L_0x073f
            r0 = r18
            byte[] r4 = r0.I
            int r4 = r4.length
        L_0x073f:
            r5 = 0
            byte[] r5 = com.loc.fa.a(r4, r5)
            r6 = 0
            int r7 = r5.length
            java.lang.System.arraycopy(r5, r6, r3, r2, r7)
            int r5 = r5.length
            int r2 = r2 + r5
            if (r4 <= 0) goto L_0x0760
            r0 = r18
            byte[] r4 = r0.I
            r5 = 0
            r0 = r18
            byte[] r6 = r0.I
            int r6 = r6.length
            java.lang.System.arraycopy(r4, r5, r3, r2, r6)
            r0 = r18
            byte[] r4 = r0.I
            int r4 = r4.length
            int r2 = r2 + r4
        L_0x0760:
            java.lang.String r4 = "5.1"
            java.lang.Double r4 = java.lang.Double.valueOf(r4)
            double r4 = r4.doubleValue()
            r6 = 4617315517961601024(0x4014000000000000, double:5.0)
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 < 0) goto L_0x077e
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
            r0 = r18
            java.lang.String r4 = r0.N
            int r2 = a(r4, r3, r2)
        L_0x077e:
            byte[] r4 = new byte[r2]
            r5 = 0
            r6 = 0
            java.lang.System.arraycopy(r3, r5, r4, r6, r2)
            java.util.zip.CRC32 r3 = new java.util.zip.CRC32
            r3.<init>()
            r3.update(r4)
            long r6 = r3.getValue()
            byte[] r3 = com.loc.fa.a(r6)
            int r5 = r2 + 8
            byte[] r5 = new byte[r5]
            r6 = 0
            r7 = 0
            java.lang.System.arraycopy(r4, r6, r5, r7, r2)
            r4 = 0
            r6 = 8
            java.lang.System.arraycopy(r3, r4, r5, r2, r6)
            return r5
        L_0x07a5:
            r2 = 1
            r3[r4] = r2
            int r4 = r4 + 1
            r0 = r18
            java.lang.String r2 = r0.E     // Catch:{ Throwable -> 0x0810 }
            java.lang.String r5 = ","
            java.lang.String[] r5 = r2.split(r5)     // Catch:{ Throwable -> 0x0810 }
            r2 = 0
            r2 = r5[r2]     // Catch:{ Throwable -> 0x0810 }
            r0 = r18
            byte[] r2 = r0.a(r2)     // Catch:{ Throwable -> 0x0810 }
            r6 = 0
            int r7 = r2.length     // Catch:{ Throwable -> 0x0810 }
            java.lang.System.arraycopy(r2, r6, r3, r4, r7)     // Catch:{ Throwable -> 0x0810 }
            int r2 = r2.length     // Catch:{ Throwable -> 0x0810 }
            int r4 = r4 + r2
            r2 = 2
            r2 = r5[r2]     // Catch:{ Throwable -> 0x07fa }
            java.lang.String r6 = "GBK"
            byte[] r6 = r2.getBytes(r6)     // Catch:{ Throwable -> 0x07fa }
            int r2 = r6.length     // Catch:{ Throwable -> 0x07fa }
            r7 = 127(0x7f, float:1.78E-43)
            if (r2 <= r7) goto L_0x07d6
            r2 = 127(0x7f, float:1.78E-43)
        L_0x07d6:
            byte r7 = (byte) r2     // Catch:{ Throwable -> 0x07fa }
            r3[r4] = r7     // Catch:{ Throwable -> 0x07fa }
            int r4 = r4 + 1
            r7 = 0
            java.lang.System.arraycopy(r6, r7, r3, r4, r2)     // Catch:{ Throwable -> 0x07fa }
            int r4 = r4 + r2
        L_0x07e0:
            r2 = 1
            r2 = r5[r2]     // Catch:{ Throwable -> 0x0810 }
            int r2 = java.lang.Integer.parseInt(r2)     // Catch:{ Throwable -> 0x0810 }
            r5 = 127(0x7f, float:1.78E-43)
            if (r2 <= r5) goto L_0x080a
            r2 = 0
        L_0x07ec:
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Throwable -> 0x0810 }
            byte r2 = java.lang.Byte.parseByte(r2)     // Catch:{ Throwable -> 0x0810 }
            r3[r4] = r2     // Catch:{ Throwable -> 0x0810 }
            int r2 = r4 + 1
            goto L_0x06a6
        L_0x07fa:
            r2 = move-exception
            java.lang.String r6 = "Req"
            java.lang.String r7 = "buildV4Dot214"
            com.loc.es.a(r2, r6, r7)     // Catch:{ Throwable -> 0x0810 }
            r2 = 0
            r3[r4] = r2     // Catch:{ Throwable -> 0x0810 }
            int r4 = r4 + 1
            goto L_0x07e0
        L_0x080a:
            r5 = -128(0xffffffffffffff80, float:NaN)
            if (r2 >= r5) goto L_0x07ec
            r2 = 0
            goto L_0x07ec
        L_0x0810:
            r2 = move-exception
            java.lang.String r5 = "Req"
            java.lang.String r6 = "buildV4Dot216"
            com.loc.es.a(r2, r5, r6)
            java.lang.String r2 = "00:00:00:00:00:00"
            r0 = r18
            byte[] r2 = r0.a(r2)
            r5 = 0
            int r6 = r2.length
            java.lang.System.arraycopy(r2, r5, r3, r4, r6)
            int r2 = r2.length
            int r2 = r2 + r4
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
            java.lang.String r4 = "0"
            byte r4 = java.lang.Byte.parseByte(r4)
            r3[r2] = r4
            int r2 = r2 + 1
            goto L_0x06a6
        L_0x083c:
            byte r4 = (byte) r12
            r3[r2] = r4
            int r6 = r2 + 1
            int r2 = com.loc.fa.d()
            r4 = 17
            if (r2 < r4) goto L_0x08cf
            r2 = 1
            r9 = r2
        L_0x084b:
            r4 = 0
            if (r9 == 0) goto L_0x0856
            long r4 = com.loc.fa.c()
            r14 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 / r14
        L_0x0856:
            r2 = 0
            r8 = r2
        L_0x0858:
            if (r8 >= r12) goto L_0x08e0
            java.lang.Object r2 = r11.get(r8)
            android.net.wifi.ScanResult r2 = (android.net.wifi.ScanResult) r2
            java.lang.String r7 = r2.BSSID
            r0 = r18
            byte[] r7 = r0.a(r7)
            r13 = 0
            int r14 = r7.length
            java.lang.System.arraycopy(r7, r13, r3, r6, r14)
            int r7 = r7.length
            int r6 = r6 + r7
            java.lang.String r7 = r2.SSID     // Catch:{ Exception -> 0x08d3 }
            java.lang.String r13 = "GBK"
            byte[] r7 = r7.getBytes(r13)     // Catch:{ Exception -> 0x08d3 }
            int r13 = r7.length     // Catch:{ Exception -> 0x08d3 }
            byte r13 = (byte) r13     // Catch:{ Exception -> 0x08d3 }
            r3[r6] = r13     // Catch:{ Exception -> 0x08d3 }
            int r6 = r6 + 1
            r13 = 0
            int r14 = r7.length     // Catch:{ Exception -> 0x08d3 }
            java.lang.System.arraycopy(r7, r13, r3, r6, r14)     // Catch:{ Exception -> 0x08d3 }
            int r7 = r7.length     // Catch:{ Exception -> 0x08d3 }
            int r6 = r6 + r7
        L_0x0885:
            int r7 = r2.level
            r13 = 127(0x7f, float:1.78E-43)
            if (r7 <= r13) goto L_0x08da
            r7 = 0
        L_0x088c:
            java.lang.String r7 = java.lang.String.valueOf(r7)
            byte r7 = java.lang.Byte.parseByte(r7)
            r3[r6] = r7
            int r7 = r6 + 1
            if (r9 == 0) goto L_0x08aa
            long r14 = r2.timestamp
            r16 = 1000000(0xf4240, double:4.940656E-318)
            long r14 = r14 / r16
            r16 = 1
            long r14 = r14 + r16
            long r14 = r4 - r14
            int r6 = (int) r14
            if (r6 >= 0) goto L_0x08ab
        L_0x08aa:
            r6 = 0
        L_0x08ab:
            r13 = 65535(0xffff, float:9.1834E-41)
            if (r6 <= r13) goto L_0x08b3
            r6 = 65535(0xffff, float:9.1834E-41)
        L_0x08b3:
            byte[] r6 = com.loc.fa.a(r6, r10)
            r13 = 0
            int r14 = r6.length
            java.lang.System.arraycopy(r6, r13, r3, r7, r14)
            int r6 = r6.length
            int r6 = r6 + r7
            int r2 = r2.frequency
            byte[] r2 = com.loc.fa.a(r2, r10)
            r7 = 0
            int r13 = r2.length
            java.lang.System.arraycopy(r2, r7, r3, r6, r13)
            int r2 = r2.length
            int r6 = r6 + r2
            int r2 = r8 + 1
            r8 = r2
            goto L_0x0858
        L_0x08cf:
            r2 = 0
            r9 = r2
            goto L_0x084b
        L_0x08d3:
            r7 = move-exception
            r7 = 0
            r3[r6] = r7
            int r6 = r6 + 1
            goto L_0x0885
        L_0x08da:
            r13 = -128(0xffffffffffffff80, float:NaN)
            if (r7 >= r13) goto L_0x088c
            r7 = 0
            goto L_0x088c
        L_0x08e0:
            r0 = r18
            java.lang.String r2 = r0.G
            int r2 = java.lang.Integer.parseInt(r2)
            byte[] r2 = com.loc.fa.a(r2, r10)
            r4 = 0
            int r5 = r2.length
            java.lang.System.arraycopy(r2, r4, r3, r6, r5)
            int r2 = r2.length
            int r2 = r2 + r6
            goto L_0x06bb
        L_0x08f5:
            int r5 = r4.length     // Catch:{ Throwable -> 0x0904 }
            byte r5 = (byte) r5     // Catch:{ Throwable -> 0x0904 }
            r3[r2] = r5     // Catch:{ Throwable -> 0x0904 }
            int r2 = r2 + 1
            r5 = 0
            int r6 = r4.length     // Catch:{ Throwable -> 0x0904 }
            java.lang.System.arraycopy(r4, r5, r3, r2, r6)     // Catch:{ Throwable -> 0x0904 }
            int r4 = r4.length     // Catch:{ Throwable -> 0x0904 }
            int r2 = r2 + r4
            goto L_0x06d8
        L_0x0904:
            r4 = move-exception
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
            goto L_0x06d8
        L_0x090c:
            r4 = move-exception
            int r2 = r2 + 2
            goto L_0x070f
        L_0x0911:
            r4 = move-exception
            int r2 = r2 + 2
            goto L_0x0726
        L_0x0916:
            r4 = move-exception
            int r2 = r2 + 2
            goto L_0x0733
        L_0x091b:
            r4 = move-exception
            goto L_0x070f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.eq.a():byte[]");
    }
}
