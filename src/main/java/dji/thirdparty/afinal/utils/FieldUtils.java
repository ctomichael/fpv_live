package dji.thirdparty.afinal.utils;

import dji.log.DJILogUtils;
import dji.thirdparty.afinal.annotation.sqlite.Id;
import dji.thirdparty.afinal.annotation.sqlite.ManyToOne;
import dji.thirdparty.afinal.annotation.sqlite.OneToMany;
import dji.thirdparty.afinal.annotation.sqlite.Property;
import dji.thirdparty.afinal.annotation.sqlite.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FieldUtils {
    public static final ThreadLocal<SimpleDateFormat> SDF = new ThreadLocal<SimpleDateFormat>() {
        /* class dji.thirdparty.afinal.utils.FieldUtils.AnonymousClass1 */

        {
            set(new SimpleDateFormat(DJILogUtils.FORMAT_2));
        }
    };

    public static Method getFieldGetMethod(Class<?> clazz, Field f) {
        String fn = f.getName();
        Method m = null;
        if (f.getType() == Boolean.TYPE) {
            m = getBooleanFieldGetMethod(clazz, fn);
        }
        if (m == null) {
            return getFieldGetMethod(clazz, fn);
        }
        return m;
    }

    public static Method getBooleanFieldGetMethod(Class<?> clazz, String fieldName) {
        String mn = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        if (isISStart(fieldName)) {
            mn = fieldName;
        }
        try {
            return clazz.getDeclaredMethod(mn, new Class[0]);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getBooleanFieldSetMethod(Class<?> clazz, Field f) {
        String fn = f.getName();
        String mn = "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
        if (isISStart(f.getName())) {
            mn = "set" + fn.substring(2, 3).toUpperCase() + fn.substring(3);
        }
        try {
            return clazz.getDeclaredMethod(mn, f.getType());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static boolean isISStart(String fieldName) {
        if (fieldName == null || fieldName.trim().length() == 0 || !fieldName.startsWith("is") || Character.isLowerCase(fieldName.charAt(2))) {
            return false;
        }
        return true;
    }

    public static Method getFieldGetMethod(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), new Class[0]);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getFieldSetMethod(Class<?> clazz, Field f) {
        String fn = f.getName();
        try {
            return clazz.getDeclaredMethod("set" + fn.substring(0, 1).toUpperCase() + fn.substring(1), f.getType());
        } catch (NoSuchMethodException e) {
            if (f.getType() == Boolean.TYPE) {
                return getBooleanFieldSetMethod(clazz, f);
            }
            return null;
        }
    }

    public static Method getFieldSetMethod(Class<?> clazz, String fieldName) {
        try {
            return getFieldSetMethod(clazz, clazz.getDeclaredField(fieldName));
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public static Object getFieldValue(Object entity, Field field) {
        return invoke(entity, getFieldGetMethod(entity.getClass(), field));
    }

    public static Object getFieldValue(Object entity, String fieldName) {
        return invoke(entity, getFieldGetMethod(entity.getClass(), fieldName));
    }

    public static void setFieldValue(Object entity, Field field, Object value) {
        int parseInt;
        try {
            Method set = getFieldSetMethod(entity.getClass(), field);
            if (set != null) {
                set.setAccessible(true);
                Class<?> type = field.getType();
                if (type == String.class) {
                    set.invoke(entity, value.toString());
                } else if (type == Integer.TYPE || type == Integer.class) {
                    Object[] objArr = new Object[1];
                    if (value == null) {
                        parseInt = null.intValue();
                    } else {
                        parseInt = Integer.parseInt(value.toString());
                    }
                    objArr[0] = Integer.valueOf(parseInt);
                    set.invoke(entity, objArr);
                } else if (type == Float.TYPE || type == Float.class) {
                    Object[] objArr2 = new Object[1];
                    objArr2[0] = Float.valueOf(value == null ? null.floatValue() : Float.parseFloat(value.toString()));
                    set.invoke(entity, objArr2);
                } else if (type == Long.TYPE || type == Long.class) {
                    Object[] objArr3 = new Object[1];
                    objArr3[0] = Long.valueOf(value == null ? null.longValue() : Long.parseLong(value.toString()));
                    set.invoke(entity, objArr3);
                } else if (type == Date.class) {
                    Object[] objArr4 = new Object[1];
                    objArr4[0] = value == null ? null : stringToDateTime(value.toString());
                    set.invoke(entity, objArr4);
                } else {
                    set.invoke(entity, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Field getFieldByColumnName(Class<?> clazz, String columnName) {
        Field[] fields;
        Field field = null;
        if (columnName == null || (fields = clazz.getDeclaredFields()) == null || fields.length <= 0) {
            return null;
        }
        if (columnName.equals(ClassUtils.getPrimaryKeyColumn(clazz))) {
            field = ClassUtils.getPrimaryKeyField(clazz);
        }
        if (field == null) {
            int length = fields.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Field f = fields[i];
                Property property = (Property) f.getAnnotation(Property.class);
                if (property != null && columnName.equals(property.column())) {
                    field = f;
                    break;
                }
                ManyToOne manyToOne = (ManyToOne) f.getAnnotation(ManyToOne.class);
                if (manyToOne != null && manyToOne.column().trim().length() != 0) {
                    field = f;
                    break;
                }
                i++;
            }
        }
        if (field == null) {
            return getFieldByName(clazz, columnName);
        }
        return field;
    }

    public static Field getFieldByName(Class<?> clazz, String fieldName) {
        if (fieldName == null) {
            return null;
        }
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String getColumnByField(Field field) {
        Property property = (Property) field.getAnnotation(Property.class);
        if (property != null && property.column().trim().length() != 0) {
            return property.column();
        }
        ManyToOne manyToOne = (ManyToOne) field.getAnnotation(ManyToOne.class);
        if (manyToOne != null && manyToOne.column().trim().length() != 0) {
            return manyToOne.column();
        }
        OneToMany oneToMany = (OneToMany) field.getAnnotation(OneToMany.class);
        if (oneToMany != null && oneToMany.manyColumn() != null && oneToMany.manyColumn().trim().length() != 0) {
            return oneToMany.manyColumn();
        }
        Id id = (Id) field.getAnnotation(Id.class);
        if (id == null || id.column().trim().length() == 0) {
            return field.getName();
        }
        return id.column();
    }

    public static String getPropertyDefaultValue(Field field) {
        Property property = (Property) field.getAnnotation(Property.class);
        if (property == null || property.defaultValue().trim().length() == 0) {
            return null;
        }
        return property.defaultValue();
    }

    public static boolean isTransient(Field f) {
        return f.getAnnotation(Transient.class) != null;
    }

    private static Object invoke(Object obj, Method method) {
        if (obj == null || method == null) {
            return null;
        }
        try {
            return method.invoke(obj, new Object[0]);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public static boolean isManyToOne(Field field) {
        return field.getAnnotation(ManyToOne.class) != null;
    }

    public static boolean isOneToMany(Field field) {
        return field.getAnnotation(OneToMany.class) != null;
    }

    public static boolean isManyToOneOrOneToMany(Field field) {
        return isManyToOne(field) || isOneToMany(field);
    }

    public static boolean isBaseDateType(Field field) {
        Class<?> clazz = field.getType();
        if (clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Byte.class) || clazz.equals(Long.class) || clazz.equals(Double.class) || clazz.equals(Float.class) || clazz.equals(Character.class) || clazz.equals(Short.class) || clazz.equals(Boolean.class) || clazz.equals(Date.class) || clazz.equals(Date.class) || clazz.equals(java.sql.Date.class) || clazz.equals(byte[].class) || clazz.isPrimitive()) {
            return true;
        }
        return false;
    }

    public static synchronized Date stringToDateTime(String strDate) {
        Date date;
        synchronized (FieldUtils.class) {
            if (strDate != null) {
                try {
                    date = SDF.get().parse(strDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            date = null;
        }
        return date;
    }
}
