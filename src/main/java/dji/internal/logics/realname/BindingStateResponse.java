package dji.internal.logics.realname;

import dji.fieldAnnotation.EXClassNullAway;
import java.io.Serializable;

@EXClassNullAway
public class BindingStateResponse implements Serializable {
    private String mobile;
    private int status;

    public int getStatus() {
        return this.status;
    }

    public String getMobileNum() {
        return this.mobile;
    }
}
