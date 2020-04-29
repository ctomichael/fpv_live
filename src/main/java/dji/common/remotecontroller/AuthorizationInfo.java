package dji.common.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class AuthorizationInfo {
    private String authCode;
    private String masterId;

    public String getMasterId() {
        return this.masterId;
    }

    public void setMasterId(String masterId2) {
        this.masterId = masterId2;
    }

    public String getAuthCode() {
        return this.authCode;
    }

    public void setAuthCode(String authCode2) {
        this.authCode = authCode2;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.masterId != null) {
            result = this.masterId.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.authCode != null) {
            i = this.authCode.hashCode();
        }
        return i2 + i;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof AuthorizationInfo)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.masterId == ((AuthorizationInfo) o).masterId && this.authCode == ((AuthorizationInfo) o).authCode;
    }
}
