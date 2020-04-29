package dji.thirdparty.afinal.db.sqlite;

import java.util.HashMap;

public class DbModel {
    private HashMap<String, Object> dataMap = new HashMap<>();

    public Object get(String column) {
        return this.dataMap.get(column);
    }

    public String getString(String column) {
        return String.valueOf(get(column));
    }

    public int getInt(String column) {
        return Integer.valueOf(getString(column)).intValue();
    }

    public boolean getBoolean(String column) {
        return Boolean.valueOf(getString(column)).booleanValue();
    }

    public double getDouble(String column) {
        return Double.valueOf(getString(column)).doubleValue();
    }

    public float getFloat(String column) {
        return Float.valueOf(getString(column)).floatValue();
    }

    public long getLong(String column) {
        return Long.valueOf(getString(column)).longValue();
    }

    public void set(String key, Object value) {
        this.dataMap.put(key, value);
    }

    public HashMap<String, Object> getDataMap() {
        return this.dataMap;
    }
}
