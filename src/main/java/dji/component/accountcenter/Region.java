package dji.component.accountcenter;

import android.support.annotation.Keep;
import java.io.Serializable;

@Keep
public class Region implements Serializable {
    private static final long serialVersionUID = 7680360453111304240L;
    public boolean hasChild = false;
    public String mCode = null;
    protected long mFilePointer = 0;
    public String mName = null;

    public long getFilePointer() {
        return this.mFilePointer;
    }

    public void setFilePointer(long filePointer) {
        this.mFilePointer = filePointer;
    }

    public boolean equals(Object o) {
        boolean ret = super.equals(o);
        if (ret || !(o instanceof Region)) {
            return ret;
        }
        Region r = (Region) o;
        if (r.mCode == null || !r.mCode.equals(this.mCode)) {
            return ret;
        }
        return true;
    }

    public int hashCode() {
        if (this.mCode != null) {
            return this.mCode.hashCode() + 527;
        }
        return 17;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(48);
        sb.append("code[").append(this.mCode).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        sb.append("name[").append(this.mName).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        return sb.toString();
    }
}
