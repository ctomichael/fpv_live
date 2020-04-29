package dji.thirdparty.afinal.db.sqlite;

import java.util.LinkedList;

public class SqlInfo {
    private LinkedList<Object> bindArgs;
    private String sql;

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql2) {
        this.sql = sql2;
    }

    public LinkedList<Object> getBindArgs() {
        return this.bindArgs;
    }

    public void setBindArgs(LinkedList<Object> bindArgs2) {
        this.bindArgs = bindArgs2;
    }

    public Object[] getBindArgsAsArray() {
        if (this.bindArgs != null) {
            return this.bindArgs.toArray();
        }
        return null;
    }

    public String[] getBindArgsAsStringArray() {
        if (this.bindArgs == null) {
            return null;
        }
        String[] strings = new String[this.bindArgs.size()];
        for (int i = 0; i < this.bindArgs.size(); i++) {
            strings[i] = this.bindArgs.get(i).toString();
        }
        return strings;
    }

    public void addValue(Object obj) {
        if (this.bindArgs == null) {
            this.bindArgs = new LinkedList<>();
        }
        this.bindArgs.add(obj);
    }
}
