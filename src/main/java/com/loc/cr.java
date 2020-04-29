package com.loc;

import android.content.Context;
import android.text.TextUtils;
import com.amap.location.common.network.HttpRequest;
import com.amap.location.common.network.HttpResponse;
import com.amap.location.common.network.IHttpClient;
import com.amap.location.security.Core;
import com.loc.ac;
import dji.component.accountcenter.IMemberProtocol;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/* compiled from: HttpClient */
public final class cr implements IHttpClient {
    private static ac e = null;
    Context a = null;
    private String b = null;
    private int c = 1;
    private ci d = null;

    public cr(Context context) {
        this.a = context;
    }

    private ac a() {
        try {
            String b2 = this.d.b();
            String[] strArr = {"com.amap.api.location", "com.loc", "com.amap.api.fence", "com.amap.co"};
            if (e == null) {
                e = new ac.a("loc", b2, "AMAP_Location_SDK_Android " + b2).a(strArr).a(b2).a();
            }
        } catch (Throwable th) {
        }
        return e;
    }

    private static String a(String str) {
        String[] split;
        if (TextUtils.isEmpty(str) || (split = str.split("\\$")) == null) {
            return null;
        }
        return split[0];
    }

    private boolean a(String str, byte[] bArr) {
        try {
            if (!str.contains("http://control.aps.amap.com/conf/r?type=3&mid=300&sver=140")) {
                return true;
            }
            String str2 = new String(Core.xxt(a(bArr), -1), "UTF-8");
            if (this.c == -1) {
                return true;
            }
            int a2 = cl.a(this.a, "his_config", "version");
            if (a2 != -1) {
                String b2 = cl.b(this.a, "his_config", "command");
                if (a(b2).equals(a(str2))) {
                    return true;
                }
                if (a2 == this.c) {
                    cl.a(this.a, "LocationCloudConfig", "command", b2);
                    try {
                        aq.b(a(), "cloudcheck", "云控项有改变，版本号未变");
                    } catch (Throwable th) {
                    }
                    return false;
                }
                cl.a(this.a, "his_config", str2, this.c);
                return true;
            }
            cl.a(this.a, "his_config", str2, this.c);
            return true;
        } catch (Throwable th2) {
            th2.printStackTrace();
            return true;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0022 A[SYNTHETIC, Splitter:B:15:0x0022] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0027 A[SYNTHETIC, Splitter:B:18:0x0027] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x002c A[SYNTHETIC, Splitter:B:21:0x002c] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x004a A[SYNTHETIC, Splitter:B:36:0x004a] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x004f A[SYNTHETIC, Splitter:B:39:0x004f] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0054 A[SYNTHETIC, Splitter:B:42:0x0054] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] a(byte[] r7) {
        /*
            r0 = 0
            java.io.ByteArrayInputStream r3 = new java.io.ByteArrayInputStream     // Catch:{ Throwable -> 0x0075, all -> 0x0043 }
            r3.<init>(r7)     // Catch:{ Throwable -> 0x0075, all -> 0x0043 }
            java.util.zip.GZIPInputStream r2 = new java.util.zip.GZIPInputStream     // Catch:{ Throwable -> 0x007a, all -> 0x0068 }
            r2.<init>(r3)     // Catch:{ Throwable -> 0x007a, all -> 0x0068 }
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x007e, all -> 0x006d }
            r1.<init>()     // Catch:{ Throwable -> 0x007e, all -> 0x006d }
            r4 = 512(0x200, float:7.175E-43)
            byte[] r4 = new byte[r4]     // Catch:{ Throwable -> 0x001f, all -> 0x0071 }
        L_0x0014:
            int r5 = r2.read(r4)     // Catch:{ Throwable -> 0x001f, all -> 0x0071 }
            if (r5 < 0) goto L_0x0030
            r6 = 0
            r1.write(r4, r6, r5)     // Catch:{ Throwable -> 0x001f, all -> 0x0071 }
            goto L_0x0014
        L_0x001f:
            r4 = move-exception
        L_0x0020:
            if (r3 == 0) goto L_0x0025
            r3.close()     // Catch:{ IOException -> 0x005c }
        L_0x0025:
            if (r2 == 0) goto L_0x002a
            r2.close()     // Catch:{ IOException -> 0x005e }
        L_0x002a:
            if (r1 == 0) goto L_0x002f
            r1.close()     // Catch:{ IOException -> 0x0060 }
        L_0x002f:
            return r0
        L_0x0030:
            r1.flush()     // Catch:{ Throwable -> 0x001f, all -> 0x0071 }
            byte[] r0 = r1.toByteArray()     // Catch:{ Throwable -> 0x001f, all -> 0x0071 }
            r3.close()     // Catch:{ IOException -> 0x0058 }
        L_0x003a:
            r2.close()     // Catch:{ IOException -> 0x005a }
        L_0x003d:
            r1.close()     // Catch:{ IOException -> 0x0041 }
            goto L_0x002f
        L_0x0041:
            r1 = move-exception
            goto L_0x002f
        L_0x0043:
            r1 = move-exception
            r4 = r1
            r5 = r0
            r2 = r0
            r3 = r0
        L_0x0048:
            if (r3 == 0) goto L_0x004d
            r3.close()     // Catch:{ IOException -> 0x0062 }
        L_0x004d:
            if (r2 == 0) goto L_0x0052
            r2.close()     // Catch:{ IOException -> 0x0064 }
        L_0x0052:
            if (r5 == 0) goto L_0x0057
            r5.close()     // Catch:{ IOException -> 0x0066 }
        L_0x0057:
            throw r4
        L_0x0058:
            r3 = move-exception
            goto L_0x003a
        L_0x005a:
            r2 = move-exception
            goto L_0x003d
        L_0x005c:
            r3 = move-exception
            goto L_0x0025
        L_0x005e:
            r2 = move-exception
            goto L_0x002a
        L_0x0060:
            r1 = move-exception
            goto L_0x002f
        L_0x0062:
            r0 = move-exception
            goto L_0x004d
        L_0x0064:
            r0 = move-exception
            goto L_0x0052
        L_0x0066:
            r0 = move-exception
            goto L_0x0057
        L_0x0068:
            r1 = move-exception
            r4 = r1
            r5 = r0
            r2 = r0
            goto L_0x0048
        L_0x006d:
            r1 = move-exception
            r4 = r1
            r5 = r0
            goto L_0x0048
        L_0x0071:
            r0 = move-exception
            r4 = r0
            r5 = r1
            goto L_0x0048
        L_0x0075:
            r1 = move-exception
            r1 = r0
            r2 = r0
            r3 = r0
            goto L_0x0020
        L_0x007a:
            r1 = move-exception
            r1 = r0
            r2 = r0
            goto L_0x0020
        L_0x007e:
            r1 = move-exception
            r1 = r0
            goto L_0x0020
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.cr.a(byte[]):byte[]");
    }

    public final void a(int i) {
        this.c = i;
    }

    public final void a(ci ciVar) {
        this.d = ciVar;
        if (ciVar != null) {
            this.b = ciVar.d();
        }
        if (TextUtils.isEmpty(this.b)) {
            this.b = u.f(this.a);
        }
    }

    public final HttpResponse post(HttpRequest httpRequest) {
        Proxy proxy;
        Map<String, String> map;
        bk bkVar;
        boolean z = true;
        String str = httpRequest.url;
        if (str.contains("http://offline.aps.amap.com/LoadOfflineData/repeatData")) {
            str = str.replace("http://offline.aps.amap.com/LoadOfflineData/repeatData", "http://apilocate.amap.com/offline/down");
        } else if (str.contains("http://cgicol.amap.com/dataPipeline/uploadData")) {
            str = str.replace("http://cgicol.amap.com/dataPipeline/uploadData", "http://apilocate.amap.com/offline/up");
        } else {
            z = false;
        }
        HttpResponse httpResponse = new HttpResponse();
        try {
            proxy = ab.a(this.a);
        } catch (Throwable th) {
            proxy = null;
        }
        try {
            cq cqVar = new cq();
            cqVar.a(str);
            cqVar.a(proxy);
            cqVar.b(httpRequest.timeout);
            cqVar.a(httpRequest.timeout);
            Map<String, String> map2 = httpRequest.headers;
            if (z) {
                String a2 = w.a();
                String a3 = str.contains("?") ? w.a(this.a, a2, ad.d(str.substring(str.lastIndexOf("?") + 1))) : w.a(this.a, a2, "key=" + this.b);
                map = map2 == null ? new HashMap<>() : map2;
                map.put("User-Agent", "AMAP_Location_SDK_Android " + this.d.b());
                map.put(IMemberProtocol.STRING_KEY, this.b);
                map.put("ts", a2);
                map.put("scode", a3);
            } else {
                map = map2;
            }
            if (map != null) {
                cqVar.a(map);
            }
            cqVar.a = httpRequest.body;
            bg.a();
            bkVar = bg.a(cqVar, false);
            try {
                byte[] bArr = bkVar.a;
                if (!a(str, bArr)) {
                    return null;
                }
                httpResponse.body = bArr;
                httpResponse.statusCode = 200;
                httpResponse.headers = bkVar.b;
                return httpResponse;
            } catch (t e2) {
                e = e2;
                if (bkVar != null) {
                    httpResponse.body = bkVar.a;
                    httpResponse.statusCode = e.e();
                    httpResponse.headers = bkVar.b;
                    return httpResponse;
                }
                return null;
            }
        } catch (t e3) {
            e = e3;
            bkVar = null;
        } catch (Throwable th2) {
            return null;
        }
    }
}
