package dji.thirdparty.afinal.db.sqlite;

import dji.thirdparty.afinal.FinalDb;
import java.util.ArrayList;
import java.util.List;

public class OneToManyLazyLoader<O, M> {
    FinalDb db;
    List<M> entities;
    Class<M> listItemClazz;
    Class<O> ownerClazz;
    O ownerEntity;

    public OneToManyLazyLoader(O ownerEntity2, Class<O> ownerClazz2, Class<M> listItemclazz, FinalDb db2) {
        this.ownerEntity = ownerEntity2;
        this.ownerClazz = ownerClazz2;
        this.listItemClazz = listItemclazz;
        this.db = db2;
    }

    public List<M> getList() {
        if (this.entities == null) {
            this.db.loadOneToMany(this.ownerEntity, this.ownerClazz, this.listItemClazz);
        }
        if (this.entities == null) {
            this.entities = new ArrayList();
        }
        return this.entities;
    }

    public void setList(List<M> value) {
        this.entities = value;
    }
}
