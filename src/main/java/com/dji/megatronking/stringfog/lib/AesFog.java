package com.dji.megatronking.stringfog.lib;

import dji.tools.security.WhiteBoxAES;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AesFog {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String decode(String data) {
        byte[] bArr = new byte[0];
        return new String(WhiteBoxAES.decryptFromWhiteBox(hex2byte(data)));
    }

    private static String byte2hex(byte[] b) {
        String hs = "";
        for (byte b2 : b) {
            String stmp = Integer.toHexString(b2 & 255);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    private static byte[] hex2byte(String strhex) {
        byte[] b = null;
        if (strhex != null) {
            int l = strhex.length();
            if (l % 2 != 1) {
                b = new byte[(l / 2)];
                for (int i = 0; i != l / 2; i++) {
                    b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, (i * 2) + 2), 16);
                }
            }
        }
        return b;
    }
}
