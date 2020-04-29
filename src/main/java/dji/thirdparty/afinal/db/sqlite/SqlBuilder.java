package dji.thirdparty.afinal.db.sqlite;

import android.text.TextUtils;
import android.util.Log;
import dji.thirdparty.afinal.db.table.Id;
import dji.thirdparty.afinal.db.table.KeyValue;
import dji.thirdparty.afinal.db.table.ManyToOne;
import dji.thirdparty.afinal.db.table.Property;
import dji.thirdparty.afinal.db.table.TableInfo;
import dji.thirdparty.afinal.exception.DbException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class SqlBuilder {
    public static SqlInfo buildInsertSql(Object entity) {
        List<KeyValue> keyValueList = getSaveKeyValueListByEntity(entity);
        StringBuffer strSQL = new StringBuffer();
        SqlInfo sqlInfo = null;
        if (keyValueList != null && keyValueList.size() > 0) {
            sqlInfo = new SqlInfo();
            strSQL.append("INSERT INTO ");
            strSQL.append(TableInfo.get(entity.getClass()).getTableName());
            strSQL.append(" (");
            for (KeyValue kv : keyValueList) {
                strSQL.append(kv.getKey()).append(",");
                sqlInfo.addValue(kv.getValue());
            }
            strSQL.deleteCharAt(strSQL.length() - 1);
            strSQL.append(") VALUES ( ");
            int length = keyValueList.size();
            for (int i = 0; i < length; i++) {
                strSQL.append("?,");
            }
            strSQL.deleteCharAt(strSQL.length() - 1);
            strSQL.append(")");
            sqlInfo.setSql(strSQL.toString());
        }
        return sqlInfo;
    }

    public static String buildInsertSqlNoValue(Object entity) {
        List<KeyValue> keyValueList = getSaveKeyValueListByEntity(entity);
        StringBuffer strSQL = new StringBuffer();
        if (keyValueList != null && keyValueList.size() > 0) {
            strSQL.append("INSERT OR REPLACE INTO ");
            strSQL.append(TableInfo.get(entity.getClass()).getTableName());
            strSQL.append(" (");
            for (KeyValue kv : keyValueList) {
                strSQL.append(kv.getKey()).append(",");
            }
            strSQL.deleteCharAt(strSQL.length() - 1);
            strSQL.append(") VALUES ( ");
            int length = keyValueList.size();
            for (int i = 0; i < length; i++) {
                strSQL.append("?,");
            }
            strSQL.deleteCharAt(strSQL.length() - 1);
            strSQL.append(")");
        }
        return strSQL.toString();
    }

    public static String buildInsertSqlNoValueWithId(Object entity, String key) {
        List<KeyValue> keyValueList = getSaveKeyValueListByEntityWithId(entity);
        String idKey = keyValueList.get(0).getKey();
        String tableName = TableInfo.get(entity.getClass()).getTableName();
        StringBuffer strSQL = new StringBuffer();
        if (keyValueList != null && keyValueList.size() > 0) {
            strSQL.append("INSERT OR REPLACE INTO ");
            strSQL.append(tableName);
            strSQL.append(" (");
            for (KeyValue kv : keyValueList) {
                strSQL.append(kv.getKey()).append(",");
            }
            strSQL.deleteCharAt(strSQL.length() - 1);
            strSQL.append(") VALUES ( ");
            int size = keyValueList.size();
            for (KeyValue kv2 : keyValueList) {
                if (kv2.getKey().equals(idKey)) {
                    strSQL.append("(select " + idKey + " from " + tableName + " where " + key + " = ?),");
                } else {
                    strSQL.append("?,");
                }
            }
            strSQL.deleteCharAt(strSQL.length() - 1);
            strSQL.append(")");
        }
        Log.d("", "ttttttt=" + strSQL.toString());
        return strSQL.toString();
    }

    public static List<KeyValue> getSaveKeyValueListByEntity(Object entity) {
        List<KeyValue> keyValueList = new ArrayList<>();
        TableInfo table = TableInfo.get(entity.getClass());
        Object idvalue = table.getId().getValue(entity);
        if (!(idvalue instanceof Integer) && (idvalue instanceof String) && idvalue != null) {
            keyValueList.add(new KeyValue(table.getId().getColumn(), idvalue));
        }
        for (Property property : table.propertyMap.values()) {
            KeyValue kv = property2KeyValue(property, entity);
            if (kv != null) {
                keyValueList.add(kv);
            }
        }
        for (ManyToOne many : table.manyToOneMap.values()) {
            KeyValue kv2 = manyToOne2KeyValue(many, entity);
            if (kv2 != null) {
                keyValueList.add(kv2);
            }
        }
        return keyValueList;
    }

    public static List<KeyValue> getSaveKeyValueListByEntityWithId(Object entity) {
        List<KeyValue> keyValueList = new ArrayList<>();
        TableInfo table = TableInfo.get(entity.getClass());
        keyValueList.add(new KeyValue(table.getId().getColumn(), table.getId().getValue(entity)));
        for (Property property : table.propertyMap.values()) {
            KeyValue kv = property2KeyValue(property, entity);
            if (kv != null) {
                keyValueList.add(kv);
            }
        }
        for (ManyToOne many : table.manyToOneMap.values()) {
            KeyValue kv2 = manyToOne2KeyValue(many, entity);
            if (kv2 != null) {
                keyValueList.add(kv2);
            }
        }
        return keyValueList;
    }

    public static String[] getSaveKeyValueArrayByEntity(TableInfo table, Object entity) {
        Collection<Property> propertys = table.propertyMap.values();
        String[] keyValueArr = new String[propertys.size()];
        int index = 0;
        for (Property property : propertys) {
            KeyValue kv = property2KeyValue(property, entity);
            if (kv != null) {
                keyValueArr[index] = kv.getValue().toString();
            }
            index++;
        }
        return keyValueArr;
    }

    public static String[] getSaveKeyValueArrayByEntityWithId(TableInfo table, Object entity, String key, String value) {
        Collection<Property> propertys = table.propertyMap.values();
        String[] keyValueArr = new String[(propertys.size() + 1)];
        keyValueArr[0] = value;
        int index = 1;
        for (Property property : propertys) {
            KeyValue kv = property2KeyValue(property, entity);
            if (kv != null) {
                keyValueArr[index] = kv.getValue().toString();
            }
            index++;
        }
        return keyValueArr;
    }

    private static String getDeleteSqlBytableName(String tableName) {
        return "DELETE FROM " + tableName;
    }

    public static SqlInfo buildDeleteSql(Object entity) {
        TableInfo table = TableInfo.get(entity.getClass());
        Id id = table.getId();
        Object idvalue = id.getValue(entity);
        if (idvalue == null) {
            throw new DbException("getDeleteSQL:" + entity.getClass() + " id value is null");
        }
        StringBuffer strSQL = new StringBuffer(getDeleteSqlBytableName(table.getTableName()));
        strSQL.append(" WHERE ").append(id.getColumn()).append("=?");
        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql(strSQL.toString());
        sqlInfo.addValue(idvalue);
        return sqlInfo;
    }

    public static SqlInfo buildDeleteSql(Class<?> clazz, Object idValue) {
        TableInfo table = TableInfo.get(clazz);
        Id id = table.getId();
        if (idValue == null) {
            throw new DbException("getDeleteSQL:idValue is null");
        }
        StringBuffer strSQL = new StringBuffer(getDeleteSqlBytableName(table.getTableName()));
        strSQL.append(" WHERE ").append(id.getColumn()).append("=?");
        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql(strSQL.toString());
        sqlInfo.addValue(idValue);
        return sqlInfo;
    }

    public static String buildDeleteSql(Class<?> clazz, String strWhere) {
        StringBuffer strSQL = new StringBuffer(getDeleteSqlBytableName(TableInfo.get(clazz).getTableName()));
        if (!TextUtils.isEmpty(strWhere)) {
            strSQL.append(" WHERE ");
            strSQL.append(strWhere);
        }
        return strSQL.toString();
    }

    private static String getSelectSqlByTableName(String tableName) {
        return new StringBuffer("SELECT * FROM ").append(tableName).toString();
    }

    public static String getSelectSQL(Class<?> clazz, Object idValue) {
        TableInfo table = TableInfo.get(clazz);
        StringBuffer strSQL = new StringBuffer(getSelectSqlByTableName(table.getTableName()));
        strSQL.append(" WHERE ");
        strSQL.append(getPropertyStrSql(table.getId().getColumn(), idValue));
        return strSQL.toString();
    }

    public static SqlInfo getSelectSqlAsSqlInfo(Class<?> clazz, Object idValue) {
        TableInfo table = TableInfo.get(clazz);
        StringBuffer strSQL = new StringBuffer(getSelectSqlByTableName(table.getTableName()));
        strSQL.append(" WHERE ").append(table.getId().getColumn()).append("=?");
        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql(strSQL.toString());
        sqlInfo.addValue(idValue);
        return sqlInfo;
    }

    public static String getSelectSQL(Class<?> clazz) {
        return getSelectSqlByTableName(TableInfo.get(clazz).getTableName());
    }

    public static String getSelectSQLByWhere(Class<?> clazz, String strWhere) {
        StringBuffer strSQL = new StringBuffer(getSelectSqlByTableName(TableInfo.get(clazz).getTableName()));
        if (!TextUtils.isEmpty(strWhere)) {
            strSQL.append(" WHERE ").append(strWhere);
        }
        return strSQL.toString();
    }

    public static SqlInfo getUpdateSqlAsSqlInfo(Object entity) {
        TableInfo table = TableInfo.get(entity.getClass());
        Object idvalue = table.getId().getValue(entity);
        if (idvalue == null) {
            throw new DbException("this entity[" + entity.getClass() + "]'s id value is null");
        }
        List<KeyValue> keyValueList = new ArrayList<>();
        for (Property property : table.propertyMap.values()) {
            KeyValue kv = property2KeyValue(property, entity);
            if (kv != null) {
                keyValueList.add(kv);
            }
        }
        for (ManyToOne many : table.manyToOneMap.values()) {
            KeyValue kv2 = manyToOne2KeyValue(many, entity);
            if (kv2 != null) {
                keyValueList.add(kv2);
            }
        }
        if (keyValueList == null || keyValueList.size() == 0) {
            return null;
        }
        SqlInfo sqlInfo = new SqlInfo();
        StringBuffer strSQL = new StringBuffer("UPDATE ");
        strSQL.append(table.getTableName());
        strSQL.append(" SET ");
        for (KeyValue kv3 : keyValueList) {
            strSQL.append(kv3.getKey()).append("=?,");
            sqlInfo.addValue(kv3.getValue());
        }
        strSQL.deleteCharAt(strSQL.length() - 1);
        strSQL.append(" WHERE ").append(table.getId().getColumn()).append("=?");
        sqlInfo.addValue(idvalue);
        sqlInfo.setSql(strSQL.toString());
        return sqlInfo;
    }

    public static SqlInfo getUpdateSqlAsSqlInfo(Object entity, String strWhere) {
        TableInfo table = TableInfo.get(entity.getClass());
        List<KeyValue> keyValueList = new ArrayList<>();
        for (Property property : table.propertyMap.values()) {
            KeyValue kv = property2KeyValue(property, entity);
            if (kv != null) {
                keyValueList.add(kv);
            }
        }
        for (ManyToOne many : table.manyToOneMap.values()) {
            KeyValue kv2 = manyToOne2KeyValue(many, entity);
            if (kv2 != null) {
                keyValueList.add(kv2);
            }
        }
        if (keyValueList == null || keyValueList.size() == 0) {
            throw new DbException("this entity[" + entity.getClass() + "] has no property");
        }
        SqlInfo sqlInfo = new SqlInfo();
        StringBuffer strSQL = new StringBuffer("UPDATE ");
        strSQL.append(table.getTableName());
        strSQL.append(" SET ");
        for (KeyValue kv3 : keyValueList) {
            strSQL.append(kv3.getKey()).append("=?,");
            sqlInfo.addValue(kv3.getValue());
        }
        strSQL.deleteCharAt(strSQL.length() - 1);
        if (!TextUtils.isEmpty(strWhere)) {
            strSQL.append(" WHERE ").append(strWhere);
        }
        sqlInfo.setSql(strSQL.toString());
        return sqlInfo;
    }

    public static String getCreatTableSQL(Class<?> clazz) {
        TableInfo table = TableInfo.get(clazz);
        Id id = table.getId();
        StringBuffer strSQL = new StringBuffer();
        strSQL.append("CREATE TABLE IF NOT EXISTS ");
        strSQL.append(table.getTableName());
        strSQL.append(" ( ");
        Class<?> primaryClazz = id.getDataType();
        if (primaryClazz == Integer.TYPE || primaryClazz == Integer.class || primaryClazz == Long.TYPE || primaryClazz == Long.class) {
            strSQL.append(id.getColumn()).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        } else {
            strSQL.append(id.getColumn()).append(" TEXT PRIMARY KEY,");
        }
        for (Property property : table.propertyMap.values()) {
            strSQL.append(property.getColumn());
            Class<?> dataType = property.getDataType();
            if (dataType == Integer.TYPE || dataType == Integer.class || dataType == Long.TYPE || dataType == Long.class) {
                strSQL.append(" INTEGER");
            } else if (dataType == Float.TYPE || dataType == Float.class || dataType == Double.TYPE || dataType == Double.class) {
                strSQL.append(" REAL");
            } else if (dataType == Boolean.TYPE || dataType == Boolean.class) {
                strSQL.append(" NUMERIC");
            }
            strSQL.append(",");
        }
        for (ManyToOne manyToOne : table.manyToOneMap.values()) {
            strSQL.append(manyToOne.getColumn()).append(" INTEGER").append(",");
        }
        strSQL.deleteCharAt(strSQL.length() - 1);
        strSQL.append(" )");
        return strSQL.toString();
    }

    private static String getPropertyStrSql(String key, Object value) {
        StringBuffer sbSQL = new StringBuffer(key).append("=");
        if ((value instanceof String) || (value instanceof Date) || (value instanceof java.sql.Date)) {
            sbSQL.append("'").append(value).append("'");
        } else {
            sbSQL.append(value);
        }
        return sbSQL.toString();
    }

    private static KeyValue property2KeyValue(Property property, Object entity) {
        String pcolumn = property.getColumn();
        Object value = property.getValue(entity);
        if (value != null) {
            return new KeyValue(pcolumn, value);
        }
        if (property.getDefaultValue() == null || property.getDefaultValue().trim().length() == 0) {
            return null;
        }
        return new KeyValue(pcolumn, property.getDefaultValue());
    }

    private static KeyValue manyToOne2KeyValue(ManyToOne many, Object entity) {
        Object manyvalue;
        String manycolumn = many.getColumn();
        Object manyobject = many.getValue(entity);
        if (manyobject == null) {
            return null;
        }
        if (manyobject.getClass() == ManyToOneLazyLoader.class) {
            manyvalue = TableInfo.get(many.getManyClass()).getId().getValue(((ManyToOneLazyLoader) manyobject).get());
        } else {
            manyvalue = TableInfo.get(manyobject.getClass()).getId().getValue(manyobject);
        }
        if (manycolumn == null || manyvalue == null) {
            return null;
        }
        return new KeyValue(manycolumn, manyvalue);
    }
}
