package com.amap.openapi;

import android.util.Base64;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import org.xeustechnologies.jtar.TarHeader;

/* compiled from: RSAUtil */
public class bb {
    private static final byte[] a = {61, 61, 81, 65, 65, 69, 119, 65, 67, TarHeader.LF_NORMAL, 74, 80, 115, 116, TarHeader.LF_FIFO, 75, 104, 76, 122, 97, 88, 99, TarHeader.LF_DIR, 71, TarHeader.LF_LINK, 122, 68, 70, 79, 104, 113, 113, 65, 97, 76, TarHeader.LF_FIFO, 65, 66, 87, TarHeader.LF_DIR, 103, 85, 84, 113, 71, 68, 69, 76, 80, 82, 106, TarHeader.LF_CHR, 66, 75, 75, 69, 98, TarHeader.LF_CONTIG, 84, 108, 115, 122, TarHeader.LF_CHR, 106, 76, TarHeader.LF_CONTIG, 88, 122, 70, 121, 73, 75, TarHeader.LF_BLK, TarHeader.LF_SYMLINK, 43, 101, 70, 121, 56, 105, 115, 105, 89, 120, 117, 112, TarHeader.LF_DIR, TarHeader.LF_NORMAL, 76, 81, 70, 86, 108, 110, 73, 65, 66, 74, 65, 83, 119, 65, 119, 83, 68, 65, 81, 66, 66, 69, 81, 65, 78, 99, 118, 104, 73, 90, 111, 75, 74, 89, 81, 68, 119, 119, 70, 77};

    public static byte[] a(byte[] bArr) throws Exception {
        PublicKey generatePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(new StringBuffer(new String(a)).reverse().toString().getBytes(), 2)));
        Cipher instance = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
        instance.init(1, generatePublic);
        return instance.doFinal(bArr);
    }
}
