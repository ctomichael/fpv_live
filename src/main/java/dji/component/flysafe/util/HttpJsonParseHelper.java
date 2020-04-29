package dji.component.flysafe.util;

import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpJsonParseHelper {
    private static Gson gson;

    private static synchronized Gson getExposeGson() {
        Gson gson2;
        synchronized (HttpJsonParseHelper.class) {
            if (gson == null) {
                gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            }
            gson2 = gson;
        }
        return gson2;
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
    public static <T> T toBeanWithExpose(String s, Class<T> classOfT) {
        try {
            return getExposeGson().fromJson(s, (Class) classOfT);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static String parseCustomKey(String jsonStr, String keyName) {
        if (keyName == null) {
            return null;
        }
        JSONObject jObject = parseJSONObject(jsonStr);
        if (jObject != null) {
            return jObject.optString(keyName);
        }
        NFZLogUtil.savedLOGE("Parse custom key, jObject null! Json String: " + jsonStr + " key name: " + keyName);
        return null;
    }

    @Nullable
    public static String parseCustomKey(@Nullable JSONObject jObject, String keyName) {
        if (keyName == null) {
            return null;
        }
        if (jObject != null) {
            return jObject.optString(keyName);
        }
        NFZLogUtil.savedLOGE("Parse custom key, jObject null! Json String:  key name: " + keyName);
        return null;
    }

    @Nullable
    public static JSONObject parseJSONObject(String jsonStr) {
        try {
            return new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
