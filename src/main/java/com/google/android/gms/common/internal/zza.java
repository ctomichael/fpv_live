package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;

final class zza implements Parcelable.Creator<BinderWrapper> {
    zza() {
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new BinderWrapper[i];
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new BinderWrapper(parcel, null);
    }
}
