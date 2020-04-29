package dji.component.accountcenter.response;

import com.drew.lang.annotations.NotNull;

public class ResposeThrowable extends Exception {
    private int errorCode;
    private Object object;

    public ResposeThrowable(int errorCode2, Object object2) {
        this.errorCode = errorCode2;
        this.object = object2;
    }

    @NotNull
    public int getErrorCode() {
        return this.errorCode;
    }

    public Object getObject() {
        return this.object;
    }

    public String toString() {
        return "Account Center response error, code: " + this.errorCode;
    }
}
