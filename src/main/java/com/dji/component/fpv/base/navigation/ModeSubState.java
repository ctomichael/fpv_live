package com.dji.component.fpv.base.navigation;

import android.os.Parcel;
import android.os.Parcelable;
import com.billy.cc.core.component.CCUtil;

public class ModeSubState implements Parcelable {
    public static final Parcelable.Creator<ModeSubState> CREATOR = new Parcelable.Creator<ModeSubState>() {
        /* class com.dji.component.fpv.base.navigation.ModeSubState.AnonymousClass1 */

        public ModeSubState createFromParcel(Parcel source) {
            String simpleName = source.readString();
            String prefix = source.readString();
            ModeSubState modeSubState = new ModeSubState(simpleName);
            modeSubState.prefix = prefix;
            return modeSubState;
        }

        public ModeSubState[] newArray(int size) {
            return new ModeSubState[size];
        }
    };
    public static final ModeSubState UNKNOWN = new ModeSubState(CCUtil.PROCESS_UNKNOWN);
    protected String prefix;
    private final String simpleName;

    public ModeSubState(String simpleName2) {
        this.simpleName = simpleName2;
    }

    public String getName() {
        return this.simpleName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.simpleName);
        dest.writeString(this.prefix);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModeSubState)) {
            return false;
        }
        ModeSubState subState = (ModeSubState) o;
        if (!this.simpleName.equals(subState.simpleName)) {
            return false;
        }
        if (this.prefix != null) {
            return this.prefix.equals(subState.prefix);
        }
        if (subState.prefix != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.simpleName.hashCode() * 31) + (this.prefix != null ? this.prefix.hashCode() : 0);
    }

    public String toString() {
        return "ModeSubState{simpleName='" + this.simpleName + '\'' + ", prefix='" + this.prefix + '\'' + '}';
    }
}
