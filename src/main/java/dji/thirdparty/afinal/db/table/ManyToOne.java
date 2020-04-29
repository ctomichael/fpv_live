package dji.thirdparty.afinal.db.table;

public class ManyToOne extends Property {
    private Class<?> manyClass;

    public Class<?> getManyClass() {
        return this.manyClass;
    }

    public void setManyClass(Class<?> manyClass2) {
        this.manyClass = manyClass2;
    }
}
