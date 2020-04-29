package dji.thirdparty.afinal.db.table;

import android.database.Cursor;
import android.util.Log;
import dji.log.DJILogUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Property {
    private static SimpleDateFormat sdf = new SimpleDateFormat(DJILogUtils.FORMAT_2);
    private String column;
    private Class<?> dataType;
    private String defaultValue;
    private Field field;
    private String fieldName;
    private Method get;
    private Method set;

    public void setValue(Object receiver, Cursor cursor, int index) {
        if (cursor != null) {
            try {
                if (this.dataType == String.class) {
                    if (this.set == null) {
                        this.field.set(receiver, cursor.getString(index));
                        return;
                    }
                    this.set.invoke(receiver, cursor.getString(index));
                } else if (this.dataType == Integer.TYPE || this.dataType == Integer.class) {
                    if (this.set == null) {
                        this.field.set(receiver, Integer.valueOf(cursor.getInt(index)));
                        return;
                    }
                    this.set.invoke(receiver, Integer.valueOf(cursor.getInt(index)));
                } else if (this.dataType == Float.TYPE || this.dataType == Float.class) {
                    if (this.set == null) {
                        this.field.set(receiver, Float.valueOf(cursor.getFloat(index)));
                        return;
                    }
                    this.set.invoke(receiver, Float.valueOf(cursor.getFloat(index)));
                } else if (this.dataType == Double.TYPE || this.dataType == Double.class) {
                    if (this.set == null) {
                        this.field.set(receiver, Double.valueOf(cursor.getDouble(index)));
                        return;
                    }
                    this.set.invoke(receiver, Double.valueOf(cursor.getDouble(index)));
                } else if (this.dataType == Long.TYPE || this.dataType == Long.class) {
                    if (this.set == null) {
                        this.field.set(receiver, Long.valueOf(cursor.getLong(index)));
                        return;
                    }
                    this.set.invoke(receiver, Long.valueOf(cursor.getLong(index)));
                } else if (this.dataType == Date.class || this.dataType == java.sql.Date.class) {
                    if (this.set == null) {
                        this.field.set(receiver, stringToDateTime(cursor.getString(index)));
                        return;
                    }
                    this.set.invoke(receiver, stringToDateTime(cursor.getString(index)));
                } else if (this.dataType == Boolean.TYPE || this.dataType == Boolean.class) {
                    if (this.set == null) {
                        this.field.set(receiver, Boolean.valueOf("1".equals(cursor.getString(index))));
                        return;
                    }
                    this.set.invoke(receiver, Boolean.valueOf("1".equals(cursor.getString(index))));
                } else if (this.dataType == byte[].class) {
                    if (this.set == null) {
                        this.field.set(receiver, cursor.getBlob(index));
                        return;
                    }
                    this.set.invoke(receiver, cursor.getBlob(index));
                } else if (this.set == null) {
                    this.field.set(receiver, cursor.getString(index));
                } else {
                    this.set.invoke(receiver, cursor.getString(index));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setValue(Object receiver, Object value) {
        if (value != null) {
            try {
                if (this.dataType == String.class) {
                    if (this.set == null) {
                        this.field.set(receiver, value.toString());
                        return;
                    }
                    this.set.invoke(receiver, value.toString());
                } else if (this.dataType == Integer.TYPE || this.dataType == Integer.class) {
                    if (this.set == null) {
                        this.field.set(receiver, Integer.valueOf(Integer.parseInt(value.toString())));
                        return;
                    }
                    this.set.invoke(receiver, Integer.valueOf(Integer.parseInt(value.toString())));
                } else if (this.dataType == Float.TYPE || this.dataType == Float.class) {
                    if (this.set == null) {
                        this.field.set(receiver, Float.valueOf(Float.parseFloat(value.toString())));
                        return;
                    }
                    this.set.invoke(receiver, Float.valueOf(Float.parseFloat(value.toString())));
                } else if (this.dataType == Double.TYPE || this.dataType == Double.class) {
                    Log.d("", "afinaltest value=" + value + "     tos=" + value.toString());
                    if (this.set == null) {
                        this.field.set(receiver, Double.valueOf(Double.parseDouble(value.toString())));
                        return;
                    }
                    this.set.invoke(receiver, Double.valueOf(Double.parseDouble(value.toString())));
                } else if (this.dataType == Long.TYPE || this.dataType == Long.class) {
                    if (this.set == null) {
                        this.field.set(receiver, Long.valueOf(Long.parseLong(value.toString())));
                        return;
                    }
                    this.set.invoke(receiver, Long.valueOf(Long.parseLong(value.toString())));
                } else if (this.dataType == Date.class || this.dataType == java.sql.Date.class) {
                    if (this.set == null) {
                        this.field.set(receiver, stringToDateTime(value.toString()));
                        return;
                    }
                    this.set.invoke(receiver, stringToDateTime(value.toString()));
                } else if (this.dataType == Boolean.TYPE || this.dataType == Boolean.class) {
                    if (this.set == null) {
                        this.field.set(receiver, Boolean.valueOf("1".equals(value.toString())));
                        return;
                    }
                    this.set.invoke(receiver, Boolean.valueOf("1".equals(value.toString())));
                } else if (this.set == null) {
                    this.field.set(receiver, value);
                } else {
                    this.set.invoke(receiver, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T getValue(Object obj) {
        if (obj != null) {
            try {
                if (this.get == null) {
                    return this.field.get(obj);
                }
                return this.get.invoke(obj, new Object[0]);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
        return null;
    }

    private static Date stringToDateTime(String strDate) {
        if (strDate != null) {
            try {
                return sdf.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName2) {
        this.fieldName = fieldName2;
    }

    public String getColumn() {
        return this.column;
    }

    public void setColumn(String column2) {
        this.column = column2;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue2) {
        this.defaultValue = defaultValue2;
    }

    public Class<?> getDataType() {
        return this.dataType;
    }

    public void setDataType(Class<?> dataType2) {
        this.dataType = dataType2;
    }

    public Method getGet() {
        return this.get;
    }

    public void setGet(Method get2) {
        this.get = get2;
    }

    public Method getSet() {
        return this.set;
    }

    public void setSet(Method set2) {
        this.set = set2;
    }

    public Field getField() {
        return this.field;
    }

    public void setField(Field field2) {
        this.field = field2;
    }
}
