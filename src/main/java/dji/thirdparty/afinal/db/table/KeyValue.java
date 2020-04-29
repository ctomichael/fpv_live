package dji.thirdparty.afinal.db.table;

import dji.thirdparty.afinal.utils.FieldUtils;
import java.util.Date;

public class KeyValue {
    private String key;
    private Object value;

    public KeyValue(String key2, Object value2) {
        this.key = key2;
        this.value = value2;
    }

    public KeyValue() {
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key2) {
        this.key = key2;
    }

    public Object getValue() {
        if ((this.value instanceof Date) || (this.value instanceof java.sql.Date)) {
            return FieldUtils.SDF.get().format(this.value);
        }
        return this.value;
    }

    public void setValue(Object value2) {
        this.value = value2;
    }
}
