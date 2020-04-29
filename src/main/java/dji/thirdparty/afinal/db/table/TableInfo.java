package dji.thirdparty.afinal.db.table;

import dji.thirdparty.afinal.exception.DbException;
import dji.thirdparty.afinal.utils.ClassUtils;
import dji.thirdparty.afinal.utils.FieldUtils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public class TableInfo {
    private static final HashMap<String, TableInfo> tableInfoMap = new HashMap<>();
    private boolean checkDatabese;
    private String className;
    private Id id;
    public final HashMap<String, ManyToOne> manyToOneMap = new HashMap<>();
    public final HashMap<String, OneToMany> oneToManyMap = new HashMap<>();
    public final HashMap<String, Property> propertyMap = new HashMap<>();
    private String tableName;

    private TableInfo() {
    }

    public static HashMap<String, TableInfo> getTableInfoMap() {
        return tableInfoMap;
    }

    public static TableInfo get(Class<?> clazz) {
        if (clazz == null) {
            throw new DbException("table info get error,because the clazz is null");
        }
        TableInfo tableInfo = tableInfoMap.get(clazz.getName());
        if (tableInfo == null) {
            tableInfo = new TableInfo();
            tableInfo.setTableName(ClassUtils.getTableName(clazz));
            tableInfo.setClassName(clazz.getName());
            Field idField = ClassUtils.getPrimaryKeyField(clazz);
            if (idField != null) {
                Id id2 = new Id();
                id2.setColumn(FieldUtils.getColumnByField(idField));
                id2.setFieldName(idField.getName());
                id2.setSet(FieldUtils.getFieldSetMethod(clazz, idField));
                id2.setGet(FieldUtils.getFieldGetMethod(clazz, idField));
                id2.setDataType(idField.getType());
                id2.setField(idField);
                tableInfo.setId(id2);
                List<Property> pList = ClassUtils.getPropertyList(clazz);
                if (pList != null) {
                    for (Property p : pList) {
                        if (p != null) {
                            tableInfo.propertyMap.put(p.getColumn(), p);
                        }
                    }
                }
                List<ManyToOne> mList = ClassUtils.getManyToOneList(clazz);
                if (mList != null) {
                    for (ManyToOne m : mList) {
                        if (m != null) {
                            tableInfo.manyToOneMap.put(m.getColumn(), m);
                        }
                    }
                }
                List<OneToMany> oList = ClassUtils.getOneToManyList(clazz);
                if (oList != null) {
                    for (OneToMany o : oList) {
                        if (o != null) {
                            tableInfo.oneToManyMap.put(o.getColumn(), o);
                        }
                    }
                }
                tableInfoMap.put(clazz.getName(), tableInfo);
            } else {
                throw new DbException("the class[" + clazz + "]'s idField is null , \n you can define _id,id property or use annotation @id to solution this exception");
            }
        }
        if (tableInfo != null) {
            return tableInfo;
        }
        throw new DbException("the class[" + clazz + "]'s table is null");
    }

    public static TableInfo get(String className2) {
        try {
            return get(Class.forName(className2));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className2) {
        this.className = className2;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName2) {
        this.tableName = tableName2;
    }

    public Id getId() {
        return this.id;
    }

    public void setId(Id id2) {
        this.id = id2;
    }

    public static void resetCheckState(Class<?> clazz) {
        TableInfo ti = getTableInfoMap().get(clazz.getName());
        if (ti != null) {
            ti.setCheckDatabese(false);
        }
    }

    public boolean isCheckDatabese() {
        return this.checkDatabese;
    }

    public void setCheckDatabese(boolean checkDatabese2) {
        this.checkDatabese = checkDatabese2;
    }
}
