package com.billy.cc.core.component.remote;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

public class BinderWrapper implements Parcelable {
    public static final Parcelable.Creator<BinderWrapper> CREATOR = new Parcelable.Creator<BinderWrapper>() {
        /* class com.billy.cc.core.component.remote.BinderWrapper.AnonymousClass1 */

        public BinderWrapper createFromParcel(Parcel source) {
            return new BinderWrapper(source);
        }

        public BinderWrapper[] newArray(int size) {
            return new BinderWrapper[size];
        }
    };
    private final IBinder binder;

    public BinderWrapper(IBinder binder2) {
        this.binder = binder2;
    }

    public BinderWrapper(Parcel in2) {
        this.binder = in2.readStrongBinder();
    }

    public IBinder getBinder() {
        return this.binder;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(this.binder);
    }
}
