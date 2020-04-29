package com.squareup.wire;

import com.squareup.wire.Message;
import com.squareup.wire.Message.Builder;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.Serializable;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;

public abstract class Message<M extends Message<M, B>, B extends Builder<M, B>> implements Serializable {
    private static final long serialVersionUID = 0;
    private final transient ProtoAdapter<M> adapter;
    transient int cachedSerializedSize = 0;
    protected transient int hashCode = 0;
    private final transient ByteString unknownFields;

    public abstract Builder<M, B> newBuilder();

    protected Message(ProtoAdapter<M> adapter2, ByteString unknownFields2) {
        if (adapter2 == null) {
            throw new NullPointerException("adapter == null");
        } else if (unknownFields2 == null) {
            throw new NullPointerException("unknownFields == null");
        } else {
            this.adapter = adapter2;
            this.unknownFields = unknownFields2;
        }
    }

    public final ByteString unknownFields() {
        ByteString result = this.unknownFields;
        return result != null ? result : ByteString.EMPTY;
    }

    public final M withoutUnknownFields() {
        return newBuilder().clearUnknownFields().build();
    }

    public String toString() {
        return this.adapter.toString(this);
    }

    /* access modifiers changed from: protected */
    public final Object writeReplace() throws ObjectStreamException {
        return new MessageSerializedForm(encode(), getClass());
    }

    public final ProtoAdapter<M> adapter() {
        return this.adapter;
    }

    public final void encode(BufferedSink sink) throws IOException {
        this.adapter.encode(sink, this);
    }

    public final byte[] encode() {
        return this.adapter.encode(this);
    }

    public final void encode(OutputStream stream) throws IOException {
        this.adapter.encode(stream, this);
    }

    public static abstract class Builder<T extends Message<T, B>, B extends Builder<T, B>> {
        Buffer unknownFieldsBuffer;
        ProtoWriter unknownFieldsWriter;

        public abstract T build();

        protected Builder() {
        }

        public final Builder<T, B> addUnknownFields(ByteString unknownFields) {
            if (unknownFields.size() > 0) {
                if (this.unknownFieldsWriter == null) {
                    this.unknownFieldsBuffer = new Buffer();
                    this.unknownFieldsWriter = new ProtoWriter(this.unknownFieldsBuffer);
                }
                try {
                    this.unknownFieldsWriter.writeBytes(unknownFields);
                } catch (IOException e) {
                    throw new AssertionError();
                }
            }
            return this;
        }

        public final Builder<T, B> addUnknownField(int tag, FieldEncoding fieldEncoding, Object value) {
            if (this.unknownFieldsWriter == null) {
                this.unknownFieldsBuffer = new Buffer();
                this.unknownFieldsWriter = new ProtoWriter(this.unknownFieldsBuffer);
            }
            try {
                fieldEncoding.rawProtoAdapter().encodeWithTag(this.unknownFieldsWriter, tag, value);
                return this;
            } catch (IOException e) {
                throw new AssertionError();
            }
        }

        public final Builder<T, B> clearUnknownFields() {
            this.unknownFieldsWriter = null;
            this.unknownFieldsBuffer = null;
            return this;
        }

        public final ByteString buildUnknownFields() {
            return this.unknownFieldsBuffer != null ? this.unknownFieldsBuffer.clone().readByteString() : ByteString.EMPTY;
        }
    }
}
