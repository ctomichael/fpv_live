package dji.thirdparty.afinal.db.sqlite;

import android.database.Cursor;
import dji.thirdparty.afinal.FinalDb;
import dji.thirdparty.afinal.db.table.ManyToOne;
import dji.thirdparty.afinal.db.table.OneToMany;
import dji.thirdparty.afinal.db.table.Property;
import dji.thirdparty.afinal.db.table.TableInfo;
import java.util.HashMap;
import java.util.Map;

public class CursorUtils {
    public static <T> T getEntity(Cursor cursor, Class<T> clazz, FinalDb db) {
        if (cursor != null) {
            try {
                TableInfo table = TableInfo.get((Class<?>) clazz);
                int columnCount = cursor.getColumnCount();
                if (columnCount > 0) {
                    T entity = clazz.newInstance();
                    for (int i = 0; i < columnCount; i++) {
                        String column = cursor.getColumnName(i);
                        Property property = table.propertyMap.get(column);
                        if (property != null) {
                            property.setValue(entity, cursor, i);
                        } else if (table.getId().getColumn().equals(column)) {
                            table.getId().setValue(entity, cursor, i);
                        }
                    }
                    for (OneToMany oneToManyProp : table.oneToManyMap.values()) {
                        if (oneToManyProp.getDataType() == OneToManyLazyLoader.class) {
                            oneToManyProp.setValue(entity, new OneToManyLazyLoader(entity, clazz, oneToManyProp.getOneClass(), db));
                        }
                    }
                    for (ManyToOne manyToOneProp : table.manyToOneMap.values()) {
                        if (manyToOneProp.getDataType() == ManyToOneLazyLoader.class) {
                            ManyToOneLazyLoader manyToOneLazyLoader = new ManyToOneLazyLoader(entity, clazz, manyToOneProp.getManyClass(), db);
                            manyToOneLazyLoader.setFieldValue(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(manyToOneProp.getColumn()))));
                            manyToOneProp.setValue(entity, manyToOneLazyLoader);
                        }
                    }
                    return entity;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static DbModel getDbModel(Cursor cursor) {
        if (cursor == null || cursor.getColumnCount() <= 0) {
            return null;
        }
        DbModel model = new DbModel();
        int columnCount = cursor.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            model.set(cursor.getColumnName(i), cursor.getString(i));
        }
        return model;
    }

    public static <T> T dbModel2Entity(DbModel dbModel, Class<?> clazz) {
        if (dbModel != null) {
            HashMap<String, Object> dataMap = dbModel.getDataMap();
            try {
                Object newInstance = clazz.newInstance();
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    String column = (String) entry.getKey();
                    TableInfo table = TableInfo.get(clazz);
                    Property property = table.propertyMap.get(column);
                    if (property != null) {
                        property.setValue(newInstance, entry.getValue() == null ? null : entry.getValue().toString());
                    } else if (table.getId().getColumn().equals(column)) {
                        table.getId().setValue(newInstance, entry.getValue() == null ? null : entry.getValue().toString());
                    }
                }
                return newInstance;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
