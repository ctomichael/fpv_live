package dji.pilot.publics.control.rc;

import android.support.annotation.Keep;
import android.text.TextUtils;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class DJIDLPackageInfo {
    public static final int STATE_FAILED = 4;
    public static final int STATE_FINISHED = 3;
    public static final int STATE_INIT = 0;
    public static final int STATE_PAUSE = 1;
    public static final int STATE_RUNNING = 2;
    public static final int TYPE_RC = 0;
    public int _id = 0;
    public String mAbsPath = null;
    public long mDLSize = 0;
    public int mDLStatus = 0;
    public String mDLUrl = null;
    public String mFileName = null;
    public long mPackageSize = 0;
    public int mProductId = 0;
    public int mRealProductId = 0;
    public int mType = 0;
    public String mVersion = null;

    public boolean equals(Object o) {
        boolean ret = super.equals(o);
        if (ret || !(o instanceof DJIDLPackageInfo) || !TextUtils.equals(this.mAbsPath, ((DJIDLPackageInfo) o).mAbsPath)) {
            return ret;
        }
        return true;
    }

    public int hashCode() {
        if (this.mAbsPath != null) {
            return this.mAbsPath.hashCode() + 527;
        }
        return 17;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append("path[").append(this.mAbsPath).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        sb.append("size[").append(String.valueOf(this.mPackageSize)).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        sb.append("status[").append(String.valueOf(this.mDLStatus)).append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        return sb.toString();
    }
}
