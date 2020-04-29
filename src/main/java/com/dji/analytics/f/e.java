package com.dji.analytics.f;

import com.dji.analytics.a.a;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

/* compiled from: MsgpackUtil */
public class e {
    public static byte[] a(HashMap<String, a> hashMap) {
        if (!a((Map) hashMap)) {
            return null;
        }
        Object[] array = hashMap.values().toArray();
        HashMap[] hashMapArr = new HashMap[array.length];
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= array.length) {
                return a(hashMapArr);
            }
            hashMapArr[i2] = ((a) array[i2]).a();
            i = i2 + 1;
        }
    }

    public static byte[] a(HashMap<String, Object>[] hashMapArr) {
        MessageBufferPacker newDefaultBufferPacker = MessagePack.newDefaultBufferPacker();
        try {
            newDefaultBufferPacker.packArrayHeader(hashMapArr.length);
            for (HashMap<String, Object> hashMap : hashMapArr) {
                a(newDefaultBufferPacker, hashMap);
            }
            try {
                newDefaultBufferPacker.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            try {
                newDefaultBufferPacker.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                newDefaultBufferPacker.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            throw th;
        }
        return newDefaultBufferPacker.toByteArray();
    }

    private static void a(MessageBufferPacker messageBufferPacker, HashMap<String, Object> hashMap) {
        messageBufferPacker.packMapHeader(hashMap.size());
        for (String str : hashMap.keySet()) {
            messageBufferPacker.packString(str);
            Object obj = hashMap.get(str);
            if (obj instanceof Integer) {
                messageBufferPacker.packInt(((Integer) obj).intValue());
            } else if (obj instanceof Long) {
                messageBufferPacker.packLong(((Long) obj).longValue());
            } else if (obj instanceof String) {
                messageBufferPacker.packString((String) obj);
            } else if (obj instanceof HashMap) {
                a(messageBufferPacker, (HashMap) obj);
            }
        }
    }

    private static boolean a(Map map) {
        if (map == null || map.size() <= 0) {
            return false;
        }
        return true;
    }
}
