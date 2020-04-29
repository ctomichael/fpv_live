package dji.internal.logics.realname;

import dji.fieldAnnotation.EXClassNullAway;
import java.io.Serializable;

@EXClassNullAway
public class DeleteBindResponse implements Serializable {
    private String msg;
    private int status;

    public int getStatus() {
        return this.status;
    }

    public String getMsg() {
        return this.msg;
    }
}
