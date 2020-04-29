package dji.thirdparty.afinal.db.sqlite;

import dji.thirdparty.afinal.FinalDb;

public class ManyToOneLazyLoader<M, O> {
    FinalDb db;
    private Object fieldValue;
    boolean hasLoaded = false;
    Class<M> manyClazz;
    M manyEntity;
    Class<O> oneClazz;
    O oneEntity;

    public ManyToOneLazyLoader(M manyEntity2, Class<M> manyClazz2, Class<O> oneClazz2, FinalDb db2) {
        this.manyEntity = manyEntity2;
        this.manyClazz = manyClazz2;
        this.oneClazz = oneClazz2;
        this.db = db2;
    }

    public O get() {
        if (this.oneEntity == null && !this.hasLoaded) {
            this.db.loadManyToOne(null, this.manyEntity, this.manyClazz, this.oneClazz);
            this.hasLoaded = true;
        }
        return this.oneEntity;
    }

    public void set(O value) {
        this.oneEntity = value;
    }

    public Object getFieldValue() {
        return this.fieldValue;
    }

    public void setFieldValue(Object fieldValue2) {
        this.fieldValue = fieldValue2;
    }
}
