package dji.common.remotecontroller;

import dji.common.remotecontroller.ProfessionalRC;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.natives.GroudStation;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;
import java.util.List;

@EXClassNullAway
public class RcProUsersConfig {
    private String username_now;
    private List<ProfessionalRC.ProRCUserBean> users = new ArrayList();

    public byte[] toBytes() {
        byte[] result = new byte[1026];
        ProfessionalRC.ProRCUserBean currentUsr = getUserNow();
        byte[] config_now_bytes = getConfigBytes(currentUsr, currentUsr.getConfigNow());
        System.arraycopy(config_now_bytes, 0, result, 0, config_now_bytes.length);
        int point = 0 + config_now_bytes.length;
        for (int i = 0; i < this.users.size(); i++) {
            ProfessionalRC.ProRCUserBean usr = this.users.get(i);
            for (int j = 0; j < 3; j++) {
                byte[] config_now_bytes2 = getConfigBytes(usr, usr.getConfigs().get(j));
                System.arraycopy(config_now_bytes2, 0, result, point, config_now_bytes2.length);
                point += config_now_bytes2.length;
            }
        }
        for (int i2 = point; i2 < 1026; i2++) {
            result[i2] = -1;
        }
        calcCrc(result);
        return result;
    }

    private byte[] getConfigBytes(ProfessionalRC.ProRCUserBean usr, ProfessionalRC.ProRCConfigBean config) {
        byte[] config_tmp = new byte[64];
        byte[] tmp = usr.getUserInfoHeader();
        System.arraycopy(tmp, 0, config_tmp, 0, tmp.length);
        int point = 0 + tmp.length;
        byte[] tmp2 = config.toBytes();
        System.arraycopy(tmp2, 0, config_tmp, point, tmp2.length);
        int point2 = point + tmp2.length;
        for (int i = point2; i < point2 + 26; i++) {
            config_tmp[i] = -1;
        }
        int point3 = point2 + 26;
        calcCrc(config_tmp);
        return config_tmp;
    }

    public void calcCrc(byte[] buffer) {
        int length = buffer.length;
        byte[] crcs = BytesUtil.getBytes(GroudStation.native_calcCrc16(buffer, length - 2));
        buffer[length - 2] = crcs[0];
        buffer[length - 1] = crcs[1];
    }

    public String getUsernameNow() {
        return this.username_now;
    }

    public ProfessionalRC.ProRCUserBean getUserNow() {
        for (int i = 0; i < this.users.size(); i++) {
            if (this.users.get(i).getUsername().equals(this.username_now)) {
                return this.users.get(i);
            }
        }
        return null;
    }

    public int getCurUserIndex() {
        for (int i = 0; i < this.users.size(); i++) {
            ProfessionalRC.ProRCUserBean user = this.users.get(i);
            if (user.getUsername() != null && user.getUsername().equals(this.username_now)) {
                return i;
            }
        }
        return -1;
    }

    public void setCurrentUsername(String username_now2) {
        this.username_now = username_now2;
    }

    public void setUsers(List<ProfessionalRC.ProRCUserBean> users2) {
        this.users = users2;
    }

    public List<ProfessionalRC.ProRCUserBean> getUsers() {
        return this.users;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RcProUsersConfig)) {
            return false;
        }
        RcProUsersConfig that = (RcProUsersConfig) o;
        if (this.username_now == null ? that.username_now != null : !this.username_now.equals(that.username_now)) {
            return false;
        }
        if (this.users != null) {
            return this.users.equals(that.users);
        }
        if (that.users != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.username_now != null) {
            result = this.username_now.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.users != null) {
            i = this.users.hashCode();
        }
        return i2 + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(", users=");
        if (getUsers() != null) {
            for (ProfessionalRC.ProRCUserBean bean : this.users) {
                sb.append(bean.toString()).append("\n");
            }
        }
        return "RcProUsersConfig{username_now='" + this.username_now + '\'' + sb.toString() + '}';
    }
}
