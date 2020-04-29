package com.squareup.wire;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.wire.Message;
import com.squareup.wire.Message.Builder;
import java.io.IOException;
import java.lang.reflect.Array;
import okio.ByteString;

public abstract class AndroidMessage<M extends Message<M, B>, B extends Message.Builder<M, B>> extends Message<M, B> implements Parcelable {
    public static <E> Parcelable.Creator<E> newCreator(ProtoAdapter<E> adapter) {
        return new ProtoAdapterCreator(adapter);
    }

    protected AndroidMessage(ProtoAdapter<M> adapter, ByteString unknownFields) {
        super(adapter, unknownFields);
    }

    public final void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(encode());
    }

    public final int describeContents() {
        return 0;
    }

    private static final class ProtoAdapterCreator<M> implements Parcelable.Creator<M> {
        private final ProtoAdapter<M> adapter;

        ProtoAdapterCreator(ProtoAdapter<M> adapter2) {
            this.adapter = adapter2;
        }

        public M createFromParcel(Parcel in2) {
            try {
                return this.adapter.decode(in2.createByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public M[] newArray(int size) {
            return (Object[]) Array.newInstance(this.adapter.javaType, size);
        }
    }
}
