package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class ResponseForGimbalControl {
    private boolean isAgree;
    private int requesterId;

    public ResponseForGimbalControl(int requesterId2, boolean isAgree2) {
        this.requesterId = requesterId2;
        this.isAgree = isAgree2;
    }

    public int getRequesterId() {
        return this.requesterId;
    }

    public boolean isAgree() {
        return this.isAgree;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResponseForGimbalControl that = (ResponseForGimbalControl) o;
        if (this.requesterId != that.requesterId) {
            return false;
        }
        if (this.isAgree != that.isAgree) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.requesterId * 31) + (this.isAgree ? 1 : 0);
    }
}
