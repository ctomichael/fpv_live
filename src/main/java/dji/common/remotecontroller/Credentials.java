package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Credentials {
    private int ID;
    private String name;
    private String password;

    public Credentials(int ID2, String name2, String password2) {
        this.ID = ID2;
        this.name = name2;
        this.password = password2;
    }

    public int getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Credentials that = (Credentials) o;
        if (this.ID != that.ID) {
            return false;
        }
        if (this.name != null) {
            if (!this.name.equals(that.name)) {
                return false;
            }
        } else if (that.name != null) {
            return false;
        }
        if (this.password != null) {
            z = this.password.equals(that.password);
        } else if (that.password != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int i;
        int i2 = 0;
        int i3 = this.ID * 31;
        if (this.name != null) {
            i = this.name.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.password != null) {
            i2 = this.password.hashCode();
        }
        return i4 + i2;
    }
}
