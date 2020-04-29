package com.amap.openapi;

import android.text.TextUtils;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.network.HttpRequest;
import com.amap.location.common.network.HttpResponse;
import com.amap.location.common.util.b;
import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import javax.crypto.Cipher;
import org.msgpack.core.MessagePack;
import org.xeustechnologies.jtar.TarHeader;

/* compiled from: CloudUtils */
public class e {
    private static final byte[] a = {TarHeader.LF_NORMAL, 92, TarHeader.LF_NORMAL, Draft_75.CR, 6, 9, 42, -122, 72, -122, -9, Draft_75.CR, 1, 1, 1, 5, 0, 3, 75, 0, TarHeader.LF_NORMAL, 72, 2, 65, 0, -18, 74, 99, -65, 73, -9, -121, 104, MessagePack.Code.FIXEXT1, 122, 126, -72, -9, 4, -10, -30, 102, -73, 101, Draft_75.CR, -119, -83, -90, -101, 87, -8, -70, 57, 80, 65, 125, -117, MessagePack.Code.UINT64, -118, 45, -73, 75, 39, MessagePack.Code.INT64, -16, -116, 34, MessagePack.Code.ARRAY16, -118, -121, -78, -72, Byte.MIN_VALUE, 67, -15, -31, 23, -7, -21, -72, -127, -89, -95, -23, 121, MessagePack.Code.BIN8, 24, 5, -75, 2, 3, 1, 0, 1};

    public static boolean a(String str) {
        int indexOf;
        if (TextUtils.isEmpty(str) || (indexOf = str.indexOf("$")) <= 0) {
            return false;
        }
        try {
            String substring = str.substring(0, indexOf);
            String substring2 = str.substring(indexOf + 1, str.length());
            if (TextUtils.isEmpty(substring)) {
                return false;
            }
            byte[] a2 = a(b.a(substring, "", ""));
            byte[] b = b(substring2);
            if (a2 == null || b == null) {
                return false;
            }
            return Arrays.equals(a2, b);
        } catch (Exception e) {
            return false;
        }
    }

    public static byte[] a(String str, byte[] bArr, d dVar) {
        try {
            if (dVar.f() != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("et", "111");
                hashMap.put("Accept-Encoding", "gzip");
                HttpRequest httpRequest = new HttpRequest();
                httpRequest.headers = hashMap;
                httpRequest.body = bArr;
                httpRequest.url = str;
                HttpResponse post = dVar.f().post(httpRequest);
                if (post == null || post.statusCode != 200) {
                    return null;
                }
                return post.body;
            }
            ALLog.trace("CloudUtils", "网络库为空");
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    private static byte[] a(byte[] bArr) {
        try {
            PublicKey generatePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(a));
            Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            instance.init(2, generatePublic);
            return instance.doFinal(bArr);
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] b(String str) {
        try {
            return MessageDigest.getInstance("SHA1").digest(str.getBytes());
        } catch (Exception e) {
            return null;
        }
    }
}
