package dji.common.flightcontroller.accesslocker;

public class UserAccountInfo {
    private final String code;
    private final String username;

    public UserAccountInfo(String username2, String securityCode) {
        this.username = username2;
        this.code = securityCode;
    }

    public String getUsername() {
        return this.username;
    }

    public String getSecurityCode() {
        return this.code;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccountInfo)) {
            return false;
        }
        UserAccountInfo that = (UserAccountInfo) o;
        if (getUsername() == null ? that.getUsername() != null : !getUsername().equals(that.getUsername())) {
            return false;
        }
        if (getSecurityCode() != null) {
            return getSecurityCode().equals(that.getSecurityCode());
        }
        if (that.getSecurityCode() != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (getUsername() != null) {
            result = getUsername().hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (getSecurityCode() != null) {
            i = getSecurityCode().hashCode();
        }
        return i2 + i;
    }
}
