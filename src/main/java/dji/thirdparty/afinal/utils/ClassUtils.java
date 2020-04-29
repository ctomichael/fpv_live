package dji.thirdparty.afinal.utils;

import dji.publics.protocol.ResponseBase;
import dji.thirdparty.afinal.annotation.sqlite.Id;
import dji.thirdparty.afinal.annotation.sqlite.InheritProperty;
import dji.thirdparty.afinal.annotation.sqlite.Table;
import dji.thirdparty.afinal.db.sqlite.ManyToOneLazyLoader;
import dji.thirdparty.afinal.db.table.ManyToOne;
import dji.thirdparty.afinal.db.table.OneToMany;
import dji.thirdparty.afinal.db.table.Property;
import dji.thirdparty.afinal.exception.DbException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ClassUtils {
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.lang.String.replace(char, char):java.lang.String}
     arg types: [int, int]
     candidates:
      ClspMth{java.lang.String.replace(java.lang.CharSequence, java.lang.CharSequence):java.lang.String}
      ClspMth{java.lang.String.replace(char, char):java.lang.String} */
    public static String getTableName(Class<?> clazz) {
        Table table = (Table) clazz.getAnnotation(Table.class);
        if (table == null || table.name().trim().length() == 0) {
            return clazz.getName().replace('.', '_');
        }
        return table.name();
    }

    public static Object getPrimaryKeyValue(Object entity) {
        return FieldUtils.getFieldValue(entity, getPrimaryKeyField(entity.getClass()));
    }

    public static boolean isInheritPropertyFromParents(Class<?> clazz) {
        InheritProperty inheritProperty = (InheritProperty) clazz.getAnnotation(InheritProperty.class);
        if (inheritProperty == null || inheritProperty.inherit()) {
            return true;
        }
        return false;
    }

    public static List<Field> customGetFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        boolean isInherit = isInheritPropertyFromParents(clazz);
        while (clazz != null && !clazz.equals(Object.class)) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            if (!isInherit) {
                break;
            }
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }

    public static String getPrimaryKeyColumn(Class<?> clazz) {
        String primaryKey = null;
        List<Field> fields = customGetFields(clazz);
        if (fields != null) {
            Id idAnnotation = null;
            Field idField = null;
            Iterator<Field> it2 = fields.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                Field field = it2.next();
                idAnnotation = (Id) field.getAnnotation(Id.class);
                if (idAnnotation != null) {
                    idField = field;
                    break;
                }
            }
            if (idAnnotation != null) {
                primaryKey = idAnnotation.column();
                if (primaryKey == null || primaryKey.trim().length() == 0) {
                    primaryKey = idField.getName();
                }
            } else {
                for (Field field2 : fields) {
                    if ("_id".equals(field2.getName())) {
                        return "_id";
                    }
                }
                for (Field field3 : fields) {
                    if (ResponseBase.STRING_ID.equals(field3.getName())) {
                        return ResponseBase.STRING_ID;
                    }
                }
            }
            return primaryKey;
        }
        throw new RuntimeException("this model[" + clazz + "] has no field");
    }

    public static Field getPrimaryKeyField(Class<?> clazz) {
        Field primaryKeyField = null;
        List<Field> fields = customGetFields(clazz);
        if (fields != null) {
            Iterator<Field> it2 = fields.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                Field field = it2.next();
                if (field.getAnnotation(Id.class) != null) {
                    primaryKeyField = field;
                    break;
                }
            }
            if (primaryKeyField == null) {
                Iterator<Field> it3 = fields.iterator();
                while (true) {
                    if (!it3.hasNext()) {
                        break;
                    }
                    Field field2 = it3.next();
                    if ("_id".equals(field2.getName())) {
                        primaryKeyField = field2;
                        break;
                    }
                }
            }
            if (primaryKeyField != null) {
                return primaryKeyField;
            }
            for (Field field3 : fields) {
                if (ResponseBase.STRING_ID.equals(field3.getName())) {
                    return field3;
                }
            }
            return primaryKeyField;
        }
        throw new RuntimeException("this model[" + clazz + "] has no field");
    }

    public static String getPrimaryKeyFieldName(Class<?> clazz) {
        Field f = getPrimaryKeyField(clazz);
        if (f == null) {
            return null;
        }
        return f.getName();
    }

    public static List<Property> getPropertyList(Class<?> clazz) {
        List<Property> plist = new ArrayList<>();
        try {
            List<Field> fields = customGetFields(clazz);
            String primaryKeyFieldName = getPrimaryKeyFieldName(clazz);
            for (Field f : fields) {
                if (!FieldUtils.isTransient(f) && FieldUtils.isBaseDateType(f) && !f.getName().equals(primaryKeyFieldName) && !f.getName().equals("serialVersionUID")) {
                    Property property = new Property();
                    property.setColumn(FieldUtils.getColumnByField(f));
                    property.setFieldName(f.getName());
                    property.setDataType(f.getType());
                    property.setDefaultValue(FieldUtils.getPropertyDefaultValue(f));
                    property.setSet(FieldUtils.getFieldSetMethod(clazz, f));
                    property.setGet(FieldUtils.getFieldGetMethod(clazz, f));
                    property.setField(f);
                    plist.add(property);
                }
            }
            return plist;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List<ManyToOne> getManyToOneList(Class<?> clazz) {
        List<ManyToOne> mList = new ArrayList<>();
        try {
            for (Field f : customGetFields(clazz)) {
                if (!FieldUtils.isTransient(f) && FieldUtils.isManyToOne(f)) {
                    ManyToOne mto = new ManyToOne();
                    if (f.getType() == ManyToOneLazyLoader.class) {
                        Class<?> pClazz = (Class) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[1];
                        if (pClazz != null) {
                            mto.setManyClass(pClazz);
                        }
                    } else {
                        mto.setManyClass(f.getType());
                    }
                    mto.setColumn(FieldUtils.getColumnByField(f));
                    mto.setFieldName(f.getName());
                    mto.setDataType(f.getType());
                    mto.setSet(FieldUtils.getFieldSetMethod(clazz, f));
                    mto.setGet(FieldUtils.getFieldGetMethod(clazz, f));
                    mList.add(mto);
                }
            }
            return mList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List<OneToMany> getOneToManyList(Class<?> clazz) {
        List<OneToMany> oList = new ArrayList<>();
        try {
            for (Field f : customGetFields(clazz)) {
                if (!FieldUtils.isTransient(f) && FieldUtils.isOneToMany(f)) {
                    OneToMany otm = new OneToMany();
                    otm.setColumn(FieldUtils.getColumnByField(f));
                    otm.setFieldName(f.getName());
                    if (f.getGenericType() instanceof ParameterizedType) {
                        ParameterizedType pType = (ParameterizedType) f.getGenericType();
                        if (pType.getActualTypeArguments().length == 1) {
                            Class<?> pClazz = (Class) pType.getActualTypeArguments()[0];
                            if (pClazz != null) {
                                otm.setOneClass(pClazz);
                            }
                        } else {
                            Class<?> pClazz2 = (Class) pType.getActualTypeArguments()[1];
                            if (pClazz2 != null) {
                                otm.setOneClass(pClazz2);
                            }
                        }
                        otm.setDataType(f.getType());
                        otm.setSet(FieldUtils.getFieldSetMethod(clazz, f));
                        otm.setGet(FieldUtils.getFieldGetMethod(clazz, f));
                        oList.add(otm);
                    } else {
                        throw new DbException("getOneToManyList Exception:" + f.getName() + "'s type is null");
                    }
                }
            }
            return oList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
