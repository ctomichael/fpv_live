package com.billy.cc.core.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.AppGlobals;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import com.billy.cc.core.component.remote.RemoteCC;
import com.billy.cc.core.component.remote.RemoteParamUtil;
import dji.component.accountcenter.IMemberProtocol;
import dji.thirdparty.ciphersql.database.SQLiteDatabase;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CCUtil {
    public static final String EXTRA_KEY_CALL_ID = "cc_extra_call_id";
    public static final String EXTRA_KEY_REMOTE_CC = "cc_extra_remote_cc";
    public static final String PROCESS_UNKNOWN = "UNKNOWN";
    private static String curProcessName = null;
    private static Boolean isRunningMainProcess = null;

    public static Map<String, Object> convertToMap(JSONObject json) {
        Map<String, Object> params = null;
        if (json == null) {
            return null;
        }
        try {
            Map<String, Object> params2 = new HashMap<>(json.length());
            try {
                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    try {
                        Object value = json.get(key);
                        if (value == JSONObject.NULL) {
                            value = null;
                        }
                        params2.put(key, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return params2;
            } catch (Exception e2) {
                e = e2;
                params = params2;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return params;
        }
    }

    @SuppressLint({"UseSparseArrays"})
    private static Object convertObjectToJson(Object v) {
        String jsonString;
        if (v == null) {
            return JSONObject.NULL;
        }
        if (v instanceof RemoteParamUtil.BaseParam) {
            jsonString = ((RemoteParamUtil.BaseParam) v).toString();
        } else if (v instanceof Map) {
            return convertToJson((Map) v);
        } else {
            if (v instanceof SparseArray) {
                Map<Integer, Object> map = new HashMap<>();
                SparseArray sp = (SparseArray) v;
                for (int i = 0; i < sp.size(); i++) {
                    map.put(Integer.valueOf(sp.keyAt(i)), sp.valueAt(i));
                }
                return convertToJson(map);
            } else if (v instanceof SparseIntArray) {
                Map<Integer, Integer> map2 = new HashMap<>();
                SparseIntArray sp2 = (SparseIntArray) v;
                for (int i2 = 0; i2 < sp2.size(); i2++) {
                    map2.put(Integer.valueOf(sp2.keyAt(i2)), Integer.valueOf(sp2.valueAt(i2)));
                }
                return convertToJson(map2);
            } else if (v instanceof SparseBooleanArray) {
                Map<Integer, Boolean> map3 = new HashMap<>();
                SparseBooleanArray sp3 = (SparseBooleanArray) v;
                for (int i3 = 0; i3 < sp3.size(); i3++) {
                    map3.put(Integer.valueOf(sp3.keyAt(i3)), Boolean.valueOf(sp3.valueAt(i3)));
                }
                return convertToJson(map3);
            } else if (Build.VERSION.SDK_INT >= 18 && (v instanceof SparseLongArray)) {
                Map<Integer, Long> map4 = new HashMap<>();
                SparseLongArray sp4 = (SparseLongArray) v;
                for (int i4 = 0; i4 < sp4.size(); i4++) {
                    map4.put(Integer.valueOf(sp4.keyAt(i4)), Long.valueOf(sp4.valueAt(i4)));
                }
                return convertToJson(map4);
            } else if ((v instanceof String) || (v instanceof Integer) || (v instanceof Long) || (v instanceof Float) || (v instanceof Double) || (v instanceof Boolean) || (v instanceof Short) || (v instanceof Byte) || (v instanceof CharSequence) || (v instanceof Character)) {
                return v;
            } else {
                jsonString = RemoteParamUtil.convertObject2JsonString(v);
            }
        }
        if (jsonString == null) {
            return null;
        }
        String jsonString2 = jsonString.trim();
        try {
            if (jsonString2.startsWith(IMemberProtocol.STRING_SEPERATOR_LEFT)) {
                return new JSONArray(jsonString2);
            }
            return jsonString2.startsWith("{") ? new JSONObject(jsonString2) : jsonString2;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject convertToJson(Map<?, ?> map) {
        if (map != null) {
            try {
                JSONObject json = new JSONObject();
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    Object value = entry.getValue();
                    Object key = entry.getKey();
                    if (key == null) {
                        key = "null";
                    }
                    if (value == null) {
                        json.put(key.toString(), (Object) null);
                    } else {
                        json.put(key.toString(), convertObjectToJson(value));
                    }
                }
                return json;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isMainProcess() {
        if (isRunningMainProcess == null) {
            Application application = CC.getApplication();
            if (application == null) {
                return false;
            }
            isRunningMainProcess = Boolean.valueOf(application.getPackageName().equals(getCurProcessName()));
        }
        return isRunningMainProcess.booleanValue();
    }

    public static String getCurProcessName() {
        if (curProcessName != null) {
            return curProcessName;
        }
        Application application = CC.getApplication();
        if (application == null) {
            return PROCESS_UNKNOWN;
        }
        try {
            ActivityManager manager = (ActivityManager) application.getSystemService("activity");
            if (manager != null) {
                for (ActivityManager.RunningAppProcessInfo appProcess : manager.getRunningAppProcesses()) {
                    if (appProcess.pid == Process.myPid()) {
                        curProcessName = appProcess.processName;
                        return curProcessName;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PROCESS_UNKNOWN;
    }

    public static String[] getCurProcessPkgList() {
        Application application = CC.getApplication();
        if (application == null) {
            return null;
        }
        try {
            ActivityManager manager = (ActivityManager) application.getSystemService("activity");
            if (manager == null) {
                return null;
            }
            for (ActivityManager.RunningAppProcessInfo appProcess : manager.getRunningAppProcesses()) {
                if (appProcess.pid == Process.myPid()) {
                    return appProcess.pkgList;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static Application initApplication() {
        try {
            Application app = ActivityThread.currentApplication();
            if (app != null) {
                return app;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Application app2 = AppGlobals.getInitialApplication();
            if (app2 == null) {
                return null;
            }
            return app2;
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void put(JSONObject json, String key, Object value) {
        try {
            json.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void navigateTo(CC cc, Class<? extends Activity> activityClass) {
        Intent intent = createNavigateIntent(cc, activityClass);
        intent.putExtra(EXTRA_KEY_REMOTE_CC, new RemoteCC(cc));
        intent.putExtra(EXTRA_KEY_CALL_ID, cc.getCallId());
        cc.getContext().startActivity(intent);
    }

    public static Intent createNavigateIntent(CC cc, Class<? extends Activity> activityClass) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, activityClass);
        if (!(context instanceof Activity)) {
            intent.addFlags(SQLiteDatabase.CREATE_IF_NECESSARY);
        }
        return intent;
    }

    public static <T> T getNavigateParam(@NonNull Activity activity, String key, T defaultValue) {
        Intent intent = activity.getIntent();
        if (intent != null) {
            return getNavigateParam(intent.getExtras(), key, defaultValue);
        }
        return defaultValue;
    }

    public static String getNavigateCallId(@NonNull Activity activity) {
        Intent intent = activity.getIntent();
        if (intent != null) {
            return intent.getStringExtra(EXTRA_KEY_CALL_ID);
        }
        return null;
    }

    public static <T> T getNavigateParam(Bundle bundle, String key, T defaultValue) {
        RemoteCC remoteCc;
        if (bundle == null || (remoteCc = (RemoteCC) bundle.getParcelable(EXTRA_KEY_REMOTE_CC)) == null) {
            return defaultValue;
        }
        return getParamItem(remoteCc, key, defaultValue);
    }

    private static <T> T getParamItem(RemoteCC remoteCc, String key, T defaultValue) {
        Map<String, Object> params = remoteCc.getParams();
        if (!params.containsKey(key)) {
            return defaultValue;
        }
        return params.get(key);
    }
}
