package dji.thirdparty.afinal.db.table;

public class OneToMany extends Property {
    private Class<?> oneClass;

    public Class<?> getOneClass() {
        return this.oneClass;
    }

    public void setOneClass(Class<?> oneClass2) {
        this.oneClass = oneClass2;
    }
}
