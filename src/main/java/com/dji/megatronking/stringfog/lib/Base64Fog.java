package com.dji.megatronking.stringfog.lib;

import com.dji.megatronking.stringfog.StringFogJNI;
import com.dji.megatronking.stringfog.lib.annotation.StringFogIgnore;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@StringFogIgnore
public final class Base64Fog {
    public static final String DEFAULT_KEY = "9*0JJxCcz10cr8ap";
    private static long lastTime = 0;
    private static Map<String, String> stringMap;

    public static synchronized String encode(String data) {
        String encode;
        synchronized (Base64Fog.class) {
            encode = encode(data, DEFAULT_KEY);
        }
        return encode;
    }

    public static synchronized String encode(String data, String key) {
        String newData;
        synchronized (Base64Fog.class) {
            try {
                newData = new String(Base64.encode(xor(data.getBytes("UTF-8"), key), 2));
            } catch (UnsupportedEncodingException e) {
                newData = new String(Base64.encode(xor(data.getBytes(), key), 2));
            }
        }
        return newData;
    }

    public static synchronized String decode(String data) {
        String decode;
        synchronized (Base64Fog.class) {
            decode = decode(data, DEFAULT_KEY);
        }
        return decode;
    }

    public static synchronized String decode(String data, String key) {
        String str;
        String newData;
        synchronized (Base64Fog.class) {
            StringFogJNI.test();
            if (data == null) {
                str = "";
            } else {
                if (stringMap == null) {
                    stringMap = new HashMap();
                } else if (stringMap.containsKey(data)) {
                    str = stringMap.get(data);
                }
                try {
                    newData = new String(StringFogJNI.nativeGetXXXX(data), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    newData = new String(StringFogJNI.nativeGetXXXX(data));
                }
                if (newData != null) {
                    stringMap.put(data, newData);
                }
                str = newData;
            }
        }
        return str;
    }

    private static byte[] xor(byte[] data, String key) {
        int len = data.length;
        int lenKey = key.length();
        int i = 0;
        int j = 0;
        while (i < len) {
            if (j >= lenKey) {
                j = 0;
            }
            data[i] = (byte) (data[i] ^ key.charAt(j));
            i++;
            j++;
        }
        return data;
    }
}
