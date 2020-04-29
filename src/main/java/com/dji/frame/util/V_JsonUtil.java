package com.dji.frame.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class V_JsonUtil {
    public static Gson gson;

    public static synchronized Gson getGson() {
        Gson gson2;
        synchronized (V_JsonUtil.class) {
            if (gson == null) {
                gson = new Gson();
            }
            gson2 = gson;
        }
        return gson2;
    }

    @Deprecated
    public static <T> T getOne(String s, Class<T> classOfT) {
        return toBean(s, classOfT);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.gson.Gson.fromJson(java.lang.String, java.lang.Class):T
     arg types: [java.lang.String, java.lang.Class<T>]
     candidates:
      com.google.gson.Gson.fromJson(com.google.gson.JsonElement, java.lang.Class):T
      com.google.gson.Gson.fromJson(com.google.gson.JsonElement, java.lang.reflect.Type):T
      com.google.gson.Gson.fromJson(com.google.gson.stream.JsonReader, java.lang.reflect.Type):T
      com.google.gson.Gson.fromJson(java.io.Reader, java.lang.Class):T
      com.google.gson.Gson.fromJson(java.io.Reader, java.lang.reflect.Type):T
      com.google.gson.Gson.fromJson(java.lang.String, java.lang.reflect.Type):T
      com.google.gson.Gson.fromJson(java.lang.String, java.lang.Class):T */
    public static <T> T toBean(String s, Class<T> classOfT) {
        try {
            return getGson().fromJson(s, (Class) classOfT);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> String toJson(T obj) {
        return getGson().toJson((Object) obj);
    }

    public static <T> String toJson(List<T> objs) {
        return getGson().toJson(objs);
    }

    public static <T> List<T> getList(String s, TypeToken<List<T>> token) {
        return (List) getGson().fromJson(s, token.getType());
    }

    public static String jsonFormatter(String uglyJsonStr) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(uglyJsonStr));
    }
}
