package com.loc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import dji.pilot.publics.util.DJITimeUtils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import org.json.JSONObject;

/* compiled from: Cache */
public final class ei {
    Hashtable<String, ArrayList<a>> a = new Hashtable<>();
    boolean b = true;
    long c = 0;
    String d = null;
    ed e = null;
    boolean f = true;
    boolean g = true;
    String h = String.valueOf(AMapLocationClientOption.GeoLanguage.DEFAULT);
    private long i = 0;
    private boolean j = false;
    private String k = "2.0.201501131131".replace(".", "");
    private String l = null;
    private String m = null;
    private long n = 0;

    /* compiled from: Cache */
    static class a {
        private AMapLocationServer a = null;
        private String b = null;

        protected a() {
        }

        public final AMapLocationServer a() {
            return this.a;
        }

        public final void a(AMapLocationServer aMapLocationServer) {
            this.a = aMapLocationServer;
        }

        public final void a(String str) {
            if (TextUtils.isEmpty(str)) {
                this.b = null;
            } else {
                this.b = str.replace("##", "#");
            }
        }

        public final String b() {
            return this.b;
        }
    }

    private AMapLocationServer a(String str, StringBuilder sb) {
        a aVar;
        a aVar2;
        try {
            if (str.contains("cgiwifi")) {
                aVar = a(sb, str);
                if (aVar != null) {
                    aVar2 = aVar;
                }
                aVar2 = aVar;
            } else if (str.contains("wifi")) {
                aVar = a(sb, str);
                if (aVar != null) {
                    aVar2 = aVar;
                }
                aVar2 = aVar;
            } else {
                aVar2 = (!str.contains("cgi") || !this.a.containsKey(str)) ? null : (a) this.a.get(str).get(0);
            }
            if (aVar2 != null && fa.a(aVar2.a())) {
                AMapLocationServer a2 = aVar2.a();
                a2.e(AmapLoc.TYPE_CACHE);
                a2.h(aVar2.b());
                if (er.b(a2.getTime())) {
                    if (fa.a(a2)) {
                        this.c = 0;
                    }
                    a2.setLocationType(4);
                    return a2;
                } else if (this.a != null && this.a.containsKey(str)) {
                    this.a.get(str).remove(aVar2);
                }
            }
        } catch (Throwable th) {
            es.a(th, "Cache", "get1");
        }
        return null;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v3, resolved type: com.loc.ei$a} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.loc.ei.a a(java.lang.StringBuilder r19, java.lang.String r20) {
        /*
            r18 = this;
            r0 = r18
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ei$a>> r2 = r0.a
            boolean r2 = r2.isEmpty()
            if (r2 != 0) goto L_0x0010
            boolean r2 = android.text.TextUtils.isEmpty(r19)
            if (r2 == 0) goto L_0x0012
        L_0x0010:
            r4 = 0
        L_0x0011:
            return r4
        L_0x0012:
            r0 = r18
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ei$a>> r2 = r0.a
            r0 = r20
            boolean r2 = r2.containsKey(r0)
            if (r2 != 0) goto L_0x0020
            r4 = 0
            goto L_0x0011
        L_0x0020:
            r8 = 0
            java.util.Hashtable r11 = new java.util.Hashtable
            r11.<init>()
            java.util.Hashtable r12 = new java.util.Hashtable
            r12.<init>()
            java.util.Hashtable r13 = new java.util.Hashtable
            r13.<init>()
            r0 = r18
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ei$a>> r2 = r0.a
            r0 = r20
            java.lang.Object r2 = r2.get(r0)
            java.util.ArrayList r2 = (java.util.ArrayList) r2
            int r3 = r2.size()
            int r3 = r3 + -1
            r9 = r3
        L_0x0043:
            if (r9 < 0) goto L_0x01a7
            java.lang.Object r3 = r2.get(r9)
            r4 = r3
            com.loc.ei$a r4 = (com.loc.ei.a) r4
            java.lang.String r3 = r4.b()
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x01a2
            r5 = 0
            java.lang.String r3 = r4.b()
            boolean r6 = android.text.TextUtils.isEmpty(r3)
            if (r6 != 0) goto L_0x0067
            boolean r6 = android.text.TextUtils.isEmpty(r19)
            if (r6 == 0) goto L_0x0083
        L_0x0067:
            r3 = 0
        L_0x0068:
            if (r3 == 0) goto L_0x00ea
            r3 = 1
            java.lang.String r5 = r4.b()
            java.lang.String r6 = r19.toString()
            boolean r5 = com.loc.fa.a(r5, r6)
            if (r5 == 0) goto L_0x00e9
        L_0x0079:
            r11.clear()
            r12.clear()
            r13.clear()
            goto L_0x0011
        L_0x0083:
            java.lang.String r6 = ",access"
            boolean r6 = r3.contains(r6)
            if (r6 == 0) goto L_0x0098
            java.lang.String r6 = ",access"
            r0 = r19
            int r6 = r0.indexOf(r6)
            r7 = -1
            if (r6 != r7) goto L_0x009a
        L_0x0098:
            r3 = 0
            goto L_0x0068
        L_0x009a:
            java.lang.String r6 = ",access"
            java.lang.String[] r3 = r3.split(r6)
            r6 = 0
            r6 = r3[r6]
            java.lang.String r7 = "#"
            boolean r6 = r6.contains(r7)
            if (r6 == 0) goto L_0x00c8
            r6 = 0
            r6 = r3[r6]
            r7 = 0
            r3 = r3[r7]
            java.lang.String r7 = "#"
            int r3 = r3.lastIndexOf(r7)
            int r3 = r3 + 1
            java.lang.String r3 = r6.substring(r3)
        L_0x00c0:
            boolean r6 = android.text.TextUtils.isEmpty(r3)
            if (r6 == 0) goto L_0x00cc
            r3 = 0
            goto L_0x0068
        L_0x00c8:
            r6 = 0
            r3 = r3[r6]
            goto L_0x00c0
        L_0x00cc:
            java.lang.String r6 = r19.toString()
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.StringBuilder r3 = r7.append(r3)
            java.lang.String r7 = ",access"
            java.lang.StringBuilder r3 = r3.append(r7)
            java.lang.String r3 = r3.toString()
            boolean r3 = r6.contains(r3)
            goto L_0x0068
        L_0x00e9:
            r5 = r3
        L_0x00ea:
            java.lang.String r3 = r4.b()
            a(r3, r11)
            java.lang.String r3 = r19.toString()
            a(r3, r12)
            r13.clear()
            java.util.Set r3 = r11.keySet()
            java.util.Iterator r6 = r3.iterator()
        L_0x0103:
            boolean r3 = r6.hasNext()
            if (r3 == 0) goto L_0x0116
            java.lang.Object r3 = r6.next()
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r7 = ""
            r13.put(r3, r7)
            goto L_0x0103
        L_0x0116:
            java.util.Set r3 = r12.keySet()
            java.util.Iterator r6 = r3.iterator()
        L_0x011e:
            boolean r3 = r6.hasNext()
            if (r3 == 0) goto L_0x0131
            java.lang.Object r3 = r6.next()
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r7 = ""
            r13.put(r3, r7)
            goto L_0x011e
        L_0x0131:
            java.util.Set r14 = r13.keySet()
            int r3 = r14.size()
            double[] r15 = new double[r3]
            int r3 = r14.size()
            double[] r0 = new double[r3]
            r16 = r0
            r3 = 0
            java.util.Iterator r17 = r14.iterator()
            r10 = r3
        L_0x0149:
            if (r17 == 0) goto L_0x0175
            boolean r3 = r17.hasNext()
            if (r3 == 0) goto L_0x0175
            java.lang.Object r3 = r17.next()
            java.lang.String r3 = (java.lang.String) r3
            boolean r6 = r11.containsKey(r3)
            if (r6 == 0) goto L_0x016f
            r6 = 4607182418800017408(0x3ff0000000000000, double:1.0)
        L_0x015f:
            r15[r10] = r6
            boolean r3 = r12.containsKey(r3)
            if (r3 == 0) goto L_0x0172
            r6 = 4607182418800017408(0x3ff0000000000000, double:1.0)
        L_0x0169:
            r16[r10] = r6
            int r3 = r10 + 1
            r10 = r3
            goto L_0x0149
        L_0x016f:
            r6 = 0
            goto L_0x015f
        L_0x0172:
            r6 = 0
            goto L_0x0169
        L_0x0175:
            r14.clear()
            double[] r3 = a(r15, r16)
            r6 = 0
            r6 = r3[r6]
            r14 = 4605380979056443392(0x3fe99999a0000000, double:0.800000011920929)
            int r6 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
            if (r6 >= 0) goto L_0x0079
            r6 = 1
            r6 = r3[r6]
            r14 = 4603741668684706349(0x3fe3c6a7ef9db22d, double:0.618)
            int r6 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
            if (r6 >= 0) goto L_0x0079
            if (r5 == 0) goto L_0x01a2
            r5 = 0
            r6 = r3[r5]
            r14 = 4603741668684706349(0x3fe3c6a7ef9db22d, double:0.618)
            int r3 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
            if (r3 >= 0) goto L_0x0079
        L_0x01a2:
            int r3 = r9 + -1
            r9 = r3
            goto L_0x0043
        L_0x01a7:
            r4 = r8
            goto L_0x0079
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ei.a(java.lang.StringBuilder, java.lang.String):com.loc.ei$a");
    }

    private String a(String str, StringBuilder sb, Context context) {
        if (context == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            if (this.l == null) {
                this.l = eh.a("MD5", u.c(context));
            }
            if (str.contains("&")) {
                str = str.substring(0, str.indexOf("&"));
            }
            String substring = str.substring(str.lastIndexOf("#") + 1);
            if (substring.equals("cgi")) {
                jSONObject.put("cgi", str.substring(0, str.length() - 12));
            } else if (!TextUtils.isEmpty(sb) && sb.indexOf(",access") != -1) {
                jSONObject.put("cgi", str.substring(0, str.length() - (substring.length() + 9)));
                String[] split = sb.toString().split(",access");
                jSONObject.put("mmac", split[0].contains("#") ? split[0].substring(split[0].lastIndexOf("#") + 1) : split[0]);
            }
            try {
                return y.b(eh.c(jSONObject.toString().getBytes("UTF-8"), this.l));
            } catch (UnsupportedEncodingException e2) {
                return null;
            }
        } catch (Throwable th) {
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:107:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x024c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(android.content.Context r10, java.lang.String r11) throws java.lang.Exception {
        /*
            r9 = this;
            r2 = 0
            boolean r0 = com.loc.er.o()
            if (r0 != 0) goto L_0x0008
        L_0x0007:
            return
        L_0x0008:
            if (r10 == 0) goto L_0x0007
            java.lang.String r0 = "hmdb"
            r1 = 0
            r3 = 0
            android.database.sqlite.SQLiteDatabase r7 = r10.openOrCreateDatabase(r0, r1, r3)     // Catch:{ Throwable -> 0x02fe, all -> 0x02f0 }
            java.lang.String r0 = "hist"
            boolean r0 = com.loc.fa.a(r7, r0)     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            if (r0 != 0) goto L_0x0045
            if (r7 == 0) goto L_0x0007
            boolean r0 = r7.isOpen()     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            if (r0 == 0) goto L_0x0007
            r7.close()     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            goto L_0x0007
        L_0x0028:
            r0 = move-exception
            r1 = r2
            r3 = r7
        L_0x002b:
            java.lang.String r2 = "DB"
            java.lang.String r4 = "fetchHist p2"
            com.loc.es.a(r0, r2, r4)     // Catch:{ all -> 0x02f9 }
            if (r1 == 0) goto L_0x0039
            r1.close()
        L_0x0039:
            if (r3 == 0) goto L_0x0007
            boolean r0 = r3.isOpen()
            if (r0 == 0) goto L_0x0007
            r3.close()
            goto L_0x0007
        L_0x0045:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            r8.<init>()     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            java.lang.String r0 = "SELECT feature, nb, loc FROM "
            r8.append(r0)     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            java.lang.String r0 = "hist"
            java.lang.StringBuilder r0 = r8.append(r0)     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            java.lang.String r1 = r9.k     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            r0.append(r1)     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            long r0 = com.loc.fa.b()     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            long r4 = com.loc.er.n()     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            long r0 = r0 - r4
            java.lang.String r3 = " WHERE time > "
            java.lang.StringBuilder r3 = r8.append(r3)     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            r3.append(r0)     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            if (r11 == 0) goto L_0x008f
            java.lang.String r0 = " and feature = '"
            java.lang.StringBuilder r0 = r8.append(r0)     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            r1.<init>()     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            java.lang.StringBuilder r1 = r1.append(r11)     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            java.lang.String r3 = "'"
            java.lang.StringBuilder r1 = r1.append(r3)     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            r0.append(r1)     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
        L_0x008f:
            java.lang.String r0 = " ORDER BY time ASC;"
            r8.append(r0)     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            java.lang.String r0 = r8.toString()     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            r1 = 0
            android.database.Cursor r6 = r7.rawQuery(r0, r1)     // Catch:{ Throwable -> 0x0028, all -> 0x02f5 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r2.<init>()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r0 = r9.l     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r0 != 0) goto L_0x00b4
            java.lang.String r0 = "MD5"
            java.lang.String r1 = com.loc.u.c(r10)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r0 = com.loc.eh.a(r0, r1)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r9.l = r0     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
        L_0x00b4:
            if (r6 == 0) goto L_0x0197
            boolean r0 = r6.moveToFirst()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r0 == 0) goto L_0x0197
        L_0x00bc:
            r0 = 0
            java.lang.String r0 = r6.getString(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r1 = "{"
            boolean r0 = r0.startsWith(r1)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r0 == 0) goto L_0x01d0
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r0 = 0
            java.lang.String r0 = r6.getString(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r1.<init>(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r0 = 0
            int r3 = r2.length()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r2.delete(r0, r3)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r0 = 1
            java.lang.String r0 = r6.getString(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r0 != 0) goto L_0x01a9
            r0 = 1
            java.lang.String r0 = r6.getString(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r2.append(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
        L_0x00ef:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r3 = 2
            java.lang.String r3 = r6.getString(r3)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r0.<init>(r3)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r3 = "type"
            boolean r3 = com.loc.fa.a(r0, r3)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r3 == 0) goto L_0x010b
            java.lang.String r3 = "type"
            java.lang.String r4 = "new"
            r0.put(r3, r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
        L_0x010b:
            com.autonavi.aps.amapapi.model.AMapLocationServer r3 = new com.autonavi.aps.amapapi.model.AMapLocationServer     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = ""
            r3.<init>(r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r3.b(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r0 = "mmac"
            boolean r0 = com.loc.fa.a(r1, r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r0 == 0) goto L_0x0292
            java.lang.String r0 = "cgi"
            boolean r0 = com.loc.fa.a(r1, r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r0 == 0) goto L_0x0292
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r0.<init>()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = "cgi"
            java.lang.String r4 = r1.getString(r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = "#"
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r4.<init>()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.StringBuilder r0 = r4.append(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = "network#"
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = "cgi"
            java.lang.String r1 = r1.getString(r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = "#"
            boolean r1 = r1.contains(r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r1 == 0) goto L_0x027c
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r1.<init>()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.StringBuilder r0 = r1.append(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r1 = "cgiwifi"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r1 = r0.toString()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
        L_0x017b:
            r5 = 0
            r0 = r9
            r4 = r10
            r0.a(r1, r2, r3, r4, r5)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
        L_0x0181:
            boolean r0 = r6.moveToNext()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r0 != 0) goto L_0x00bc
            r0 = 0
            int r1 = r2.length()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r2.delete(r0, r1)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r0 = 0
            int r1 = r8.length()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r8.delete(r0, r1)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
        L_0x0197:
            if (r6 == 0) goto L_0x019c
            r6.close()
        L_0x019c:
            if (r7 == 0) goto L_0x0007
            boolean r0 = r7.isOpen()
            if (r0 == 0) goto L_0x0007
            r7.close()
            goto L_0x0007
        L_0x01a9:
            java.lang.String r0 = "mmac"
            boolean r0 = com.loc.fa.a(r1, r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r0 == 0) goto L_0x00ef
            java.lang.String r0 = "#"
            java.lang.StringBuilder r0 = r2.append(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r3 = "mmac"
            java.lang.String r3 = r1.getString(r3)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r0.append(r3)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r0 = ",access"
            r2.append(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            goto L_0x00ef
        L_0x01cb:
            r0 = move-exception
            r1 = r6
            r3 = r7
            goto L_0x002b
        L_0x01d0:
            r0 = 0
            java.lang.String r0 = r6.getString(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            byte[] r0 = com.loc.y.b(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r3 = new java.lang.String     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = r9.l     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            byte[] r0 = com.loc.eh.d(r0, r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = "UTF-8"
            r3.<init>(r0, r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r1.<init>(r3)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r0 = 0
            int r3 = r2.length()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r2.delete(r0, r3)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r0 = 1
            java.lang.String r0 = r6.getString(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r0 != 0) goto L_0x025b
            r0 = 1
            java.lang.String r0 = r6.getString(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            byte[] r0 = com.loc.y.b(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r3 = new java.lang.String     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = r9.l     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            byte[] r0 = com.loc.eh.d(r0, r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = "UTF-8"
            r3.<init>(r0, r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r2.append(r3)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
        L_0x0219:
            r0 = 2
            java.lang.String r0 = r6.getString(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            byte[] r3 = com.loc.y.b(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = new java.lang.String     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r5 = r9.l     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            byte[] r3 = com.loc.eh.d(r3, r5)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r5 = "UTF-8"
            r4.<init>(r3, r5)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r0.<init>(r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r3 = "type"
            boolean r3 = com.loc.fa.a(r0, r3)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r3 == 0) goto L_0x010b
            java.lang.String r3 = "type"
            java.lang.String r4 = "new"
            r0.put(r3, r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            goto L_0x010b
        L_0x0249:
            r0 = move-exception
        L_0x024a:
            if (r6 == 0) goto L_0x024f
            r6.close()
        L_0x024f:
            if (r7 == 0) goto L_0x025a
            boolean r1 = r7.isOpen()
            if (r1 == 0) goto L_0x025a
            r7.close()
        L_0x025a:
            throw r0
        L_0x025b:
            java.lang.String r0 = "mmac"
            boolean r0 = com.loc.fa.a(r1, r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r0 == 0) goto L_0x0219
            java.lang.String r0 = "#"
            java.lang.StringBuilder r0 = r2.append(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r3 = "mmac"
            java.lang.String r3 = r1.getString(r3)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r0.append(r3)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r0 = ",access"
            r2.append(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            goto L_0x0219
        L_0x027c:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r1.<init>()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.StringBuilder r0 = r1.append(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r1 = "wifi"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r1 = r0.toString()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            goto L_0x017b
        L_0x0292:
            java.lang.String r0 = "cgi"
            boolean r0 = com.loc.fa.a(r1, r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r0 == 0) goto L_0x0181
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r0.<init>()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = "cgi"
            java.lang.String r4 = r1.getString(r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = "#"
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r4.<init>()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.StringBuilder r0 = r4.append(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = "network#"
            java.lang.StringBuilder r0 = r0.append(r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = "cgi"
            java.lang.String r1 = r1.getString(r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r4 = "#"
            boolean r1 = r1.contains(r4)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            if (r1 == 0) goto L_0x0181
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            r1.<init>()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.StringBuilder r0 = r1.append(r0)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r1 = "cgi"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            java.lang.String r1 = r0.toString()     // Catch:{ Throwable -> 0x01cb, all -> 0x0249 }
            goto L_0x017b
        L_0x02f0:
            r0 = move-exception
            r6 = r2
            r7 = r2
            goto L_0x024a
        L_0x02f5:
            r0 = move-exception
            r6 = r2
            goto L_0x024a
        L_0x02f9:
            r0 = move-exception
            r6 = r1
            r7 = r3
            goto L_0x024a
        L_0x02fe:
            r0 = move-exception
            r1 = r2
            r3 = r2
            goto L_0x002b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ei.a(android.content.Context, java.lang.String):void");
    }

    private void a(String str, AMapLocation aMapLocation, StringBuilder sb, Context context) throws Exception {
        SQLiteDatabase sQLiteDatabase = null;
        if (context != null) {
            if (this.l == null) {
                this.l = eh.a("MD5", u.c(context));
            }
            String a2 = a(str, sb, context);
            StringBuilder sb2 = new StringBuilder();
            try {
                SQLiteDatabase openOrCreateDatabase = context.openOrCreateDatabase("hmdb", 0, null);
                sb2.append("CREATE TABLE IF NOT EXISTS hist");
                sb2.append(this.k);
                sb2.append(" (feature VARCHAR PRIMARY KEY, nb VARCHAR, loc VARCHAR, time VARCHAR);");
                openOrCreateDatabase.execSQL(sb2.toString());
                sb2.delete(0, sb2.length());
                sb2.append("REPLACE INTO ");
                sb2.append("hist").append(this.k);
                sb2.append(" VALUES (?, ?, ?, ?)");
                Object[] objArr = {a2, eh.c(sb.toString().getBytes("UTF-8"), this.l), eh.c(aMapLocation.toStr().getBytes("UTF-8"), this.l), Long.valueOf(aMapLocation.getTime())};
                for (int i2 = 1; i2 < 3; i2++) {
                    objArr[i2] = y.b((byte[]) objArr[i2]);
                }
                openOrCreateDatabase.execSQL(sb2.toString(), objArr);
                sb2.delete(0, sb2.length());
                sb2.delete(0, sb2.length());
                if (openOrCreateDatabase != null && openOrCreateDatabase.isOpen()) {
                    openOrCreateDatabase.close();
                }
            } catch (Throwable th) {
                sb2.delete(0, sb2.length());
                if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
                    sQLiteDatabase.close();
                }
                throw th;
            }
        }
    }

    private static void a(String str, Hashtable<String, String> hashtable) {
        if (!TextUtils.isEmpty(str)) {
            hashtable.clear();
            String[] split = str.split("#");
            for (String str2 : split) {
                if (!TextUtils.isEmpty(str2) && !str2.contains("|")) {
                    hashtable.put(str2, "");
                }
            }
        }
    }

    private static double[] a(double[] dArr, double[] dArr2) {
        double[] dArr3 = new double[3];
        double d2 = 0.0d;
        double d3 = 0.0d;
        double d4 = 0.0d;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < dArr.length; i4++) {
            d3 += dArr[i4] * dArr[i4];
            d4 += dArr2[i4] * dArr2[i4];
            d2 += dArr[i4] * dArr2[i4];
            if (dArr2[i4] == 1.0d) {
                i2++;
                if (dArr[i4] == 1.0d) {
                    i3++;
                }
            }
        }
        dArr3[0] = d2 / (Math.sqrt(d4) * Math.sqrt(d3));
        dArr3[1] = (1.0d * ((double) i3)) / ((double) i2);
        dArr3[2] = (double) i3;
        for (int i5 = 0; i5 < 2; i5++) {
            if (dArr3[i5] > 1.0d) {
                dArr3[i5] = 1.0d;
            }
        }
        return dArr3;
    }

    private boolean b() {
        long c2 = fa.c() - this.i;
        if (this.i == 0) {
            return false;
        }
        if (this.a.size() > 360) {
            return true;
        }
        return c2 > 36000000;
    }

    private void c() {
        this.i = 0;
        if (!this.a.isEmpty()) {
            this.a.clear();
        }
        this.j = false;
    }

    public final AMapLocationServer a(Context context, String str, StringBuilder sb, boolean z) {
        if (TextUtils.isEmpty(str) || !er.o()) {
            return null;
        }
        String str2 = str + "&" + this.f + "&" + this.g + "&" + this.h;
        if (str2.contains("gps") || !er.o() || sb == null) {
            return null;
        }
        if (b()) {
            c();
            return null;
        }
        if (z && !this.j) {
            try {
                String a2 = a(str2, sb, context);
                c();
                a(context, a2);
            } catch (Throwable th) {
            }
        }
        if (!this.a.isEmpty()) {
            return a(str2, sb);
        }
        return null;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.loc.ei.a(android.content.Context, java.lang.String, java.lang.StringBuilder, boolean):com.autonavi.aps.amapapi.model.AMapLocationServer
     arg types: [android.content.Context, java.lang.String, java.lang.StringBuilder, int]
     candidates:
      com.loc.ei.a(java.lang.String, com.amap.api.location.AMapLocation, java.lang.StringBuilder, android.content.Context):void
      com.loc.ei.a(android.content.Context, java.lang.String, java.lang.StringBuilder, boolean):com.autonavi.aps.amapapi.model.AMapLocationServer */
    public final AMapLocationServer a(ee eeVar, boolean z, AMapLocationServer aMapLocationServer, eg egVar, StringBuilder sb, String str, Context context) {
        boolean z2;
        boolean z3;
        if (!(!(this.b && er.o()) ? false : aMapLocationServer == null || er.b(aMapLocationServer.getTime()))) {
            return null;
        }
        try {
            ed c2 = eeVar.c();
            boolean z4 = !(c2 == null && this.e == null) && (this.e == null || !this.e.equals(c2));
            if (aMapLocationServer != null) {
                z2 = aMapLocationServer.getAccuracy() > 299.0f && egVar.c().size() > 5;
            } else {
                z2 = false;
            }
            if (aMapLocationServer == null || this.d == null || z2 || z4) {
                z3 = false;
            } else {
                z3 = fa.a(this.d, sb.toString());
                boolean z5 = this.c != 0 && fa.c() - this.c < 3000;
                if ((z3 || z5) && fa.a(aMapLocationServer)) {
                    aMapLocationServer.e(AmapLoc.TYPE_CACHE);
                    aMapLocationServer.setLocationType(2);
                    return aMapLocationServer;
                }
            }
            if (!z3) {
                this.c = fa.c();
            } else {
                this.c = 0;
            }
            if (this.m == null || str.equals(this.m)) {
                if (this.m == null) {
                    this.n = fa.b();
                    this.m = str;
                } else {
                    this.n = fa.b();
                }
            } else if (fa.b() - this.n < 3000) {
                str = this.m;
            } else {
                this.n = fa.b();
                this.m = str;
            }
            AMapLocationServer aMapLocationServer2 = null;
            if (!z2 && !z) {
                aMapLocationServer2 = a(context, str, sb, false);
            }
            if ((!z && !fa.a(aMapLocationServer2)) || z2) {
                return null;
            }
            if (z) {
                return null;
            }
            this.c = 0;
            aMapLocationServer2.setLocationType(4);
            return aMapLocationServer2;
        } catch (Throwable th) {
            return null;
        }
    }

    public final void a() {
        this.c = 0;
        this.d = null;
    }

    public final void a(Context context) {
        if (!this.j) {
            try {
                c();
                a(context, (String) null);
            } catch (Throwable th) {
                es.a(th, "Cache", "loadDB");
            }
            this.j = true;
        }
    }

    public final void a(AMapLocationClientOption aMapLocationClientOption) {
        this.g = aMapLocationClientOption.isNeedAddress();
        this.f = aMapLocationClientOption.isOffset();
        this.b = aMapLocationClientOption.isLocationCacheEnable();
        this.h = String.valueOf(aMapLocationClientOption.getGeoLanguage());
    }

    public final void a(ed edVar) {
        this.e = edVar;
    }

    public final void a(String str) {
        this.d = str;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void a(java.lang.String r8, java.lang.StringBuilder r9, com.autonavi.aps.amapapi.model.AMapLocationServer r10, android.content.Context r11, boolean r12) {
        /*
            r7 = this;
            r0 = 1
            r1 = 0
            boolean r2 = com.loc.fa.a(r10)     // Catch:{ Throwable -> 0x0184 }
            if (r2 != 0) goto L_0x0009
        L_0x0008:
            return
        L_0x0009:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0184 }
            r2.<init>()     // Catch:{ Throwable -> 0x0184 }
            java.lang.StringBuilder r2 = r2.append(r8)     // Catch:{ Throwable -> 0x0184 }
            java.lang.String r3 = "&"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x0184 }
            boolean r3 = r10.isOffset()     // Catch:{ Throwable -> 0x0184 }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x0184 }
            java.lang.String r3 = "&"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x0184 }
            boolean r3 = r10.i()     // Catch:{ Throwable -> 0x0184 }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x0184 }
            java.lang.String r3 = "&"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x0184 }
            java.lang.String r3 = r10.j()     // Catch:{ Throwable -> 0x0184 }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x0184 }
            java.lang.String r6 = r2.toString()     // Catch:{ Throwable -> 0x0184 }
            boolean r2 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Throwable -> 0x0184 }
            if (r2 != 0) goto L_0x004f
            boolean r2 = com.loc.fa.a(r10)     // Catch:{ Throwable -> 0x0184 }
            if (r2 != 0) goto L_0x00df
        L_0x004f:
            r0 = r1
        L_0x0050:
            if (r0 == 0) goto L_0x0008
            java.lang.String r0 = r10.e()     // Catch:{ Throwable -> 0x0184 }
            java.lang.String r2 = "mem"
            boolean r0 = r0.equals(r2)     // Catch:{ Throwable -> 0x0184 }
            if (r0 != 0) goto L_0x0008
            java.lang.String r0 = r10.e()     // Catch:{ Throwable -> 0x0184 }
            java.lang.String r2 = "file"
            boolean r0 = r0.equals(r2)     // Catch:{ Throwable -> 0x0184 }
            if (r0 != 0) goto L_0x0008
            java.lang.String r0 = r10.e()     // Catch:{ Throwable -> 0x0184 }
            java.lang.String r2 = "wifioff"
            boolean r0 = r0.equals(r2)     // Catch:{ Throwable -> 0x0184 }
            if (r0 != 0) goto L_0x0008
            java.lang.String r0 = "-3"
            java.lang.String r2 = r10.d()     // Catch:{ Throwable -> 0x0184 }
            boolean r0 = r0.equals(r2)     // Catch:{ Throwable -> 0x0184 }
            if (r0 != 0) goto L_0x0008
            boolean r0 = r7.b()     // Catch:{ Throwable -> 0x0184 }
            if (r0 == 0) goto L_0x008f
            r7.c()     // Catch:{ Throwable -> 0x0184 }
        L_0x008f:
            org.json.JSONObject r0 = r10.f()     // Catch:{ Throwable -> 0x0184 }
            java.lang.String r2 = "offpct"
            boolean r2 = com.loc.fa.a(r0, r2)     // Catch:{ Throwable -> 0x0184 }
            if (r2 == 0) goto L_0x00a5
            java.lang.String r2 = "offpct"
            r0.remove(r2)     // Catch:{ Throwable -> 0x0184 }
            r10.a(r0)     // Catch:{ Throwable -> 0x0184 }
        L_0x00a5:
            java.lang.String r0 = "wifi"
            boolean r0 = r6.contains(r0)     // Catch:{ Throwable -> 0x0184 }
            if (r0 == 0) goto L_0x019c
            boolean r0 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x0184 }
            if (r0 != 0) goto L_0x0008
            float r0 = r10.getAccuracy()     // Catch:{ Throwable -> 0x0184 }
            r2 = 1133903872(0x43960000, float:300.0)
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 < 0) goto L_0x0190
            java.lang.String r0 = r9.toString()     // Catch:{ Throwable -> 0x0184 }
            java.lang.String r2 = "#"
            java.lang.String[] r3 = r0.split(r2)     // Catch:{ Throwable -> 0x0184 }
            int r4 = r3.length     // Catch:{ Throwable -> 0x0184 }
            r2 = r1
            r0 = r1
        L_0x00cc:
            if (r2 >= r4) goto L_0x00f7
            r1 = r3[r2]     // Catch:{ Throwable -> 0x0184 }
            java.lang.String r5 = ","
            boolean r1 = r1.contains(r5)     // Catch:{ Throwable -> 0x0184 }
            if (r1 == 0) goto L_0x00db
            int r0 = r0 + 1
        L_0x00db:
            int r1 = r2 + 1
            r2 = r1
            goto L_0x00cc
        L_0x00df:
            java.lang.String r2 = "#"
            boolean r2 = r6.startsWith(r2)     // Catch:{ Throwable -> 0x0184 }
            if (r2 == 0) goto L_0x00eb
            r0 = r1
            goto L_0x0050
        L_0x00eb:
            java.lang.String r2 = "network"
            boolean r2 = r6.contains(r2)     // Catch:{ Throwable -> 0x0184 }
            if (r2 != 0) goto L_0x0050
            r0 = r1
            goto L_0x0050
        L_0x00f7:
            r1 = 8
            if (r0 >= r1) goto L_0x0008
        L_0x00fb:
            java.lang.String r0 = "cgiwifi"
            boolean r0 = r6.contains(r0)     // Catch:{ Throwable -> 0x0184 }
            if (r0 == 0) goto L_0x012d
            java.lang.String r0 = r10.g()     // Catch:{ Throwable -> 0x0184 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x0184 }
            if (r0 != 0) goto L_0x012d
            java.lang.String r0 = "cgiwifi"
            java.lang.String r1 = "cgi"
            java.lang.String r1 = r6.replace(r0, r1)     // Catch:{ Throwable -> 0x0184 }
            com.autonavi.aps.amapapi.model.AMapLocationServer r3 = r10.h()     // Catch:{ Throwable -> 0x0184 }
            boolean r0 = com.loc.fa.a(r3)     // Catch:{ Throwable -> 0x0184 }
            if (r0 == 0) goto L_0x012d
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0184 }
            r2.<init>()     // Catch:{ Throwable -> 0x0184 }
            r5 = 1
            r0 = r7
            r4 = r11
            r0.a(r1, r2, r3, r4, r5)     // Catch:{ Throwable -> 0x0184 }
        L_0x012d:
            com.autonavi.aps.amapapi.model.AMapLocationServer r0 = r7.a(r6, r9)     // Catch:{ Throwable -> 0x0184 }
            boolean r1 = com.loc.fa.a(r0)     // Catch:{ Throwable -> 0x0184 }
            if (r1 == 0) goto L_0x0146
            java.lang.String r0 = r0.toStr()     // Catch:{ Throwable -> 0x0184 }
            r1 = 3
            java.lang.String r1 = r10.toStr(r1)     // Catch:{ Throwable -> 0x0184 }
            boolean r0 = r0.equals(r1)     // Catch:{ Throwable -> 0x0184 }
            if (r0 != 0) goto L_0x0008
        L_0x0146:
            long r0 = com.loc.fa.c()     // Catch:{ Throwable -> 0x0184 }
            r7.i = r0     // Catch:{ Throwable -> 0x0184 }
            com.loc.ei$a r1 = new com.loc.ei$a     // Catch:{ Throwable -> 0x0184 }
            r1.<init>()     // Catch:{ Throwable -> 0x0184 }
            r1.a(r10)     // Catch:{ Throwable -> 0x0184 }
            boolean r0 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x0184 }
            if (r0 == 0) goto L_0x01c0
            r0 = 0
        L_0x015b:
            r1.a(r0)     // Catch:{ Throwable -> 0x0184 }
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ei$a>> r0 = r7.a     // Catch:{ Throwable -> 0x0184 }
            boolean r0 = r0.containsKey(r6)     // Catch:{ Throwable -> 0x0184 }
            if (r0 == 0) goto L_0x01c5
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ei$a>> r0 = r7.a     // Catch:{ Throwable -> 0x0184 }
            java.lang.Object r0 = r0.get(r6)     // Catch:{ Throwable -> 0x0184 }
            java.util.ArrayList r0 = (java.util.ArrayList) r0     // Catch:{ Throwable -> 0x0184 }
            r0.add(r1)     // Catch:{ Throwable -> 0x0184 }
        L_0x0171:
            if (r12 == 0) goto L_0x0008
            r7.a(r6, r10, r9, r11)     // Catch:{ Throwable -> 0x0178 }
            goto L_0x0008
        L_0x0178:
            r0 = move-exception
            java.lang.String r1 = "Cache"
            java.lang.String r2 = "add"
            com.loc.es.a(r0, r1, r2)     // Catch:{ Throwable -> 0x0184 }
            goto L_0x0008
        L_0x0184:
            r0 = move-exception
            java.lang.String r1 = "Cache"
            java.lang.String r2 = "add"
            com.loc.es.a(r0, r1, r2)
            goto L_0x0008
        L_0x0190:
            float r0 = r10.getAccuracy()     // Catch:{ Throwable -> 0x0184 }
            r1 = 1077936128(0x40400000, float:3.0)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto L_0x00fb
            goto L_0x0008
        L_0x019c:
            java.lang.String r0 = "cgi"
            boolean r0 = r6.contains(r0)     // Catch:{ Throwable -> 0x0184 }
            if (r0 == 0) goto L_0x012d
            if (r9 == 0) goto L_0x01b1
            java.lang.String r0 = ","
            int r0 = r9.indexOf(r0)     // Catch:{ Throwable -> 0x0184 }
            r1 = -1
            if (r0 != r1) goto L_0x0008
        L_0x01b1:
            java.lang.String r0 = "4"
            java.lang.String r1 = r10.d()     // Catch:{ Throwable -> 0x0184 }
            boolean r0 = r0.equals(r1)     // Catch:{ Throwable -> 0x0184 }
            if (r0 == 0) goto L_0x012d
            goto L_0x0008
        L_0x01c0:
            java.lang.String r0 = r9.toString()     // Catch:{ Throwable -> 0x0184 }
            goto L_0x015b
        L_0x01c5:
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ Throwable -> 0x0184 }
            r0.<init>()     // Catch:{ Throwable -> 0x0184 }
            r0.add(r1)     // Catch:{ Throwable -> 0x0184 }
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ei$a>> r1 = r7.a     // Catch:{ Throwable -> 0x0184 }
            r1.put(r6, r0)     // Catch:{ Throwable -> 0x0184 }
            goto L_0x0171
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ei.a(java.lang.String, java.lang.StringBuilder, com.autonavi.aps.amapapi.model.AMapLocationServer, android.content.Context, boolean):void");
    }

    public final void b(Context context) {
        SQLiteDatabase sQLiteDatabase = null;
        try {
            c();
            if (context != null) {
                try {
                    sQLiteDatabase = context.openOrCreateDatabase("hmdb", 0, null);
                    if (fa.a(sQLiteDatabase, "hist")) {
                        sQLiteDatabase.delete("hist" + this.k, "time<?", new String[]{String.valueOf(fa.b() - DJITimeUtils.MILLIS_IN_DAY)});
                        if (sQLiteDatabase != null) {
                            if (sQLiteDatabase.isOpen()) {
                                sQLiteDatabase.close();
                            }
                        }
                    } else if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
                        sQLiteDatabase.close();
                    }
                } catch (Throwable th) {
                    es.a(th, "DB", "clearHist p2");
                    if (sQLiteDatabase != null) {
                        if (sQLiteDatabase.isOpen()) {
                            sQLiteDatabase.close();
                        }
                    }
                }
            }
            this.j = false;
            this.d = null;
            this.n = 0;
        } catch (Throwable th2) {
            es.a(th2, "Cache", "destroy part");
        }
    }
}
