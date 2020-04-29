package com.billy.cc.core.component;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class CCResult {
    public static final int CODE_ERROR_BUSINESS = 1;
    public static final int CODE_ERROR_CALLBACK_NOT_INVOKED = -10;
    public static final int CODE_ERROR_CANCELED = -8;
    public static final int CODE_ERROR_COMPONENT_NAME_EMPTY = -2;
    @Deprecated
    public static final int CODE_ERROR_CONNECT_FAILED = -7;
    public static final int CODE_ERROR_CONTEXT_NULL = -6;
    public static final int CODE_ERROR_DEFAULT = -1;
    public static final int CODE_ERROR_EXCEPTION_RESULT = -4;
    public static final int CODE_ERROR_NO_COMPONENT_FOUND = -5;
    public static final int CODE_ERROR_NULL_RESULT = -3;
    public static final int CODE_ERROR_REMOTE_CC_DELIVERY_FAILED = -11;
    public static final int CODE_ERROR_TIMEOUT = -9;
    public static final int CODE_SUCCESS = 0;
    private static final String KEY_CODE = "code";
    private static final String KEY_DATA = "data";
    private static final String KEY_ERROR_MESSAGE = "errorMessage";
    private static final String KEY_SUCCESS = "success";
    private int code;
    private Map<String, Object> data;
    private String errorMessage;
    private boolean success;

    public static CCResult error(String message) {
        CCResult result = new CCResult();
        result.code = 1;
        result.success = false;
        result.errorMessage = message;
        return result;
    }

    public static CCResult error(String key, Object value) {
        CCResult result = new CCResult();
        result.code = 1;
        result.success = false;
        result.data = new HashMap(4);
        result.data.put(key, value);
        return result;
    }

    static CCResult error(int code2) {
        CCResult result = new CCResult();
        result.code = code2;
        result.success = false;
        return result;
    }

    public static CCResult success(String key, Object value) {
        Map<String, Object> data2 = new HashMap<>(2);
        data2.put(key, value);
        return success(data2);
    }

    public static CCResult success() {
        return success(null);
    }

    public static CCResult success(Map<String, Object> data2) {
        CCResult result = new CCResult();
        result.code = 0;
        result.success = true;
        result.data = data2;
        return result;
    }

    static CCResult defaultNullResult() {
        return error(-3);
    }

    static CCResult defaultExceptionResult(Throwable e) {
        if (e != null) {
            e.printStackTrace();
        }
        return error(-4);
    }

    @Deprecated
    public static CCResult fromString(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return fromJSONObject(new JSONObject(str));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Deprecated
    private static CCResult fromJSONObject(JSONObject json) {
        if (json == null) {
            return null;
        }
        CCResult result = new CCResult();
        result.success = json.optBoolean("success");
        result.code = json.optInt(KEY_CODE);
        result.errorMessage = json.optString(KEY_ERROR_MESSAGE);
        result.data = CCUtil.convertToMap(json.optJSONObject(KEY_DATA));
        return result;
    }

    public String toString() {
        JSONObject json = new JSONObject();
        putValue(json, "success", Boolean.valueOf(this.success));
        putValue(json, KEY_CODE, Integer.valueOf(this.code));
        putValue(json, KEY_ERROR_MESSAGE, this.errorMessage);
        putValue(json, KEY_DATA, CCUtil.convertToJson(this.data));
        try {
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void putValue(JSONObject json, String key, Object value) {
        if (json != null && key != null) {
            try {
                json.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success2) {
        this.success = success2;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage2) {
        this.errorMessage = errorMessage2;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code2) {
        this.code = code2;
    }

    @Deprecated
    public JSONObject getData() {
        return CCUtil.convertToJson(this.data);
    }

    @Deprecated
    public void setData(JSONObject data2) {
        this.data = CCUtil.convertToMap(data2);
    }

    public Map<String, Object> getDataMap() {
        return this.data;
    }

    public <T> T getDataItem(String key, T defaultValue) {
        T item = getDataItem(key);
        return item == null ? defaultValue : item;
    }

    public <T> T getDataItem(String key) {
        if (this.data != null) {
            try {
                return this.data.get(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setDataMap(Map<String, Object> data2) {
        this.data = data2;
    }

    public CCResult addData(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap(16);
        }
        this.data.put(key, value);
        return this;
    }
}
