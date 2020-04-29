package dji.flysafe;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import okio.ByteString;

public final class FlySafeLimitInfoRecordContext extends Message<FlySafeLimitInfoRecordContext, Builder> {
    public static final ProtoAdapter<FlySafeLimitInfoRecordContext> ADAPTER = new ProtoAdapter_FlySafeLimitInfoRecordContext();
    public static final ByteString DEFAULT_INFO = ByteString.EMPTY;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BYTES", tag = 2)
    public final ByteString info;

    public FlySafeLimitInfoRecordContext(ByteString info2) {
        this(info2, ByteString.EMPTY);
    }

    public FlySafeLimitInfoRecordContext(ByteString info2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.info = info2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.info = this.info;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FlySafeLimitInfoRecordContext)) {
            return false;
        }
        FlySafeLimitInfoRecordContext o = (FlySafeLimitInfoRecordContext) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.info, o.info)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int result2 = (unknownFields().hashCode() * 37) + (this.info != null ? this.info.hashCode() : 0);
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.info != null) {
            builder.append(", info=").append(this.info);
        }
        return builder.replace(0, 2, "FlySafeLimitInfoRecordContext{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<FlySafeLimitInfoRecordContext, Builder> {
        public ByteString info;

        public Builder info(ByteString info2) {
            this.info = info2;
            return this;
        }

        public FlySafeLimitInfoRecordContext build() {
            return new FlySafeLimitInfoRecordContext(this.info, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_FlySafeLimitInfoRecordContext extends ProtoAdapter<FlySafeLimitInfoRecordContext> {
        ProtoAdapter_FlySafeLimitInfoRecordContext() {
            super(FieldEncoding.LENGTH_DELIMITED, FlySafeLimitInfoRecordContext.class);
        }

        public int encodedSize(FlySafeLimitInfoRecordContext value) {
            return (value.info != null ? ProtoAdapter.BYTES.encodedSizeWithTag(2, value.info) : 0) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, FlySafeLimitInfoRecordContext value) throws IOException {
            if (value.info != null) {
                ProtoAdapter.BYTES.encodeWithTag(writer, 2, value.info);
            }
            writer.writeBytes(value.unknownFields());
        }

        public FlySafeLimitInfoRecordContext decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 2:
                            builder.info(ProtoAdapter.BYTES.decode(reader));
                            break;
                        default:
                            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
                            builder.addUnknownField(tag, fieldEncoding, fieldEncoding.rawProtoAdapter().decode(reader));
                            break;
                    }
                } else {
                    reader.endMessage(token);
                    return builder.build();
                }
            }
        }

        public FlySafeLimitInfoRecordContext redact(FlySafeLimitInfoRecordContext value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
