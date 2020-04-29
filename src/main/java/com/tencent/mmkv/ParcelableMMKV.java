package com.tencent.mmkv;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import java.io.IOException;

public final class ParcelableMMKV implements Parcelable {
    public static final Parcelable.Creator<ParcelableMMKV> CREATOR = new Parcelable.Creator<ParcelableMMKV>() {
        /* class com.tencent.mmkv.ParcelableMMKV.AnonymousClass1 */

        public ParcelableMMKV createFromParcel(Parcel source) {
            String mmapID = source.readString();
            ParcelFileDescriptor fd = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(source);
            ParcelFileDescriptor metaFD = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(source);
            String cryptKey = source.readString();
            if (fd == null || metaFD == null) {
                return null;
            }
            return new ParcelableMMKV(mmapID, fd.detachFd(), metaFD.detachFd(), cryptKey);
        }

        public ParcelableMMKV[] newArray(int size) {
            return new ParcelableMMKV[size];
        }
    };
    private int ashmemFD;
    private int ashmemMetaFD;
    private String cryptKey;
    private String mmapID;

    public ParcelableMMKV(MMKV mmkv) {
        this.ashmemFD = -1;
        this.ashmemMetaFD = -1;
        this.cryptKey = null;
        this.mmapID = mmkv.mmapID();
        this.ashmemFD = mmkv.ashmemFD();
        this.ashmemMetaFD = mmkv.ashmemMetaFD();
        this.cryptKey = mmkv.cryptKey();
    }

    private ParcelableMMKV(String id, int fd, int metaFD, String key) {
        this.ashmemFD = -1;
        this.ashmemMetaFD = -1;
        this.cryptKey = null;
        this.mmapID = id;
        this.ashmemFD = fd;
        this.ashmemMetaFD = metaFD;
        this.cryptKey = key;
    }

    public MMKV toMMKV() {
        if (this.ashmemFD < 0 || this.ashmemMetaFD < 0) {
            return null;
        }
        return MMKV.mmkvWithAshmemFD(this.mmapID, this.ashmemFD, this.ashmemMetaFD, this.cryptKey);
    }

    public int describeContents() {
        return 1;
    }

    public void writeToParcel(Parcel dest, int flags) {
        try {
            dest.writeString(this.mmapID);
            ParcelFileDescriptor fd = ParcelFileDescriptor.fromFd(this.ashmemFD);
            ParcelFileDescriptor metaFD = ParcelFileDescriptor.fromFd(this.ashmemMetaFD);
            int flags2 = flags | 1;
            fd.writeToParcel(dest, flags2);
            metaFD.writeToParcel(dest, flags2);
            if (this.cryptKey != null) {
                dest.writeString(this.cryptKey);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
