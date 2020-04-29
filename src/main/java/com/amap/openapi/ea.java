package com.amap.openapi;

import com.amap.location.common.log.ALLog;
import com.amap.location.common.network.HttpRequest;
import com.amap.location.common.network.HttpResponse;
import com.amap.location.common.network.IHttpClient;
import com.amap.location.common.util.d;
import com.amap.location.security.Core;
import java.util.HashMap;

/* compiled from: NetHelper */
public class ea {
    public static boolean a(IHttpClient iHttpClient, String str, byte[] bArr, int i) {
        try {
            byte[] a = d.a(bArr);
            if (a == null || a.length == 0) {
                ALLog.trace("HttpRequestHelper", "gzip is null");
                return false;
            }
            byte[] xxt = Core.xxt(a, 1);
            if (xxt == null || xxt.length == 0) {
                ALLog.trace("HttpRequestHelper", "xxt is null");
                return false;
            } else if (iHttpClient == null) {
                return false;
            } else {
                HashMap hashMap = new HashMap();
                hashMap.put("ext", "120");
                HttpRequest httpRequest = new HttpRequest();
                httpRequest.url = str;
                httpRequest.headers = hashMap;
                httpRequest.body = xxt;
                httpRequest.timeout = i;
                HttpResponse post = iHttpClient.post(httpRequest);
                if (post == null || post.body == null) {
                    return false;
                }
                return post.statusCode == 200 && "true".equals(new String(post.body, "UTF-8"));
            }
        } catch (Throwable th) {
            return false;
        }
    }
}
