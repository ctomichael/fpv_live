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

public final class ShowRTHTipsContext extends Message<ShowRTHTipsContext, Builder> {
    public static final ProtoAdapter<ShowRTHTipsContext> ADAPTER = new ProtoAdapter_ShowRTHTipsContext();
    public static final RTHTipsEventType DEFAULT_TYPE = RTHTipsEventType.NearBy;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "dji.flysafe.RTHTipsEventType#ADAPTER", tag = 1)
    public final RTHTipsEventType type;

    public ShowRTHTipsContext(RTHTipsEventType type2) {
        this(type2, ByteString.EMPTY);
    }

    public ShowRTHTipsContext(RTHTipsEventType type2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.type = type2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.type = this.type;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ShowRTHTipsContext)) {
            return false;
        }
        ShowRTHTipsContext o = (ShowRTHTipsContext) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.type, o.type)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int result2 = (unknownFields().hashCode() * 37) + (this.type != null ? this.type.hashCode() : 0);
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.type != null) {
            builder.append(", type=").append(this.type);
        }
        return builder.replace(0, 2, "ShowRTHTipsContext{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<ShowRTHTipsContext, Builder> {
        public RTHTipsEventType type;

        public Builder type(RTHTipsEventType type2) {
            this.type = type2;
            return this;
        }

        public ShowRTHTipsContext build() {
            return new ShowRTHTipsContext(this.type, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_ShowRTHTipsContext extends ProtoAdapter<ShowRTHTipsContext> {
        ProtoAdapter_ShowRTHTipsContext() {
            super(FieldEncoding.LENGTH_DELIMITED, ShowRTHTipsContext.class);
        }

        public int encodedSize(ShowRTHTipsContext value) {
            return (value.type != null ? RTHTipsEventType.ADAPTER.encodedSizeWithTag(1, value.type) : 0) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, ShowRTHTipsContext value) throws IOException {
            if (value.type != null) {
                RTHTipsEventType.ADAPTER.encodeWithTag(writer, 1, value.type);
            }
            writer.writeBytes(value.unknownFields());
        }

        public ShowRTHTipsContext decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            try {
                                builder.type(RTHTipsEventType.ADAPTER.decode(reader));
                                break;
                            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
                                builder.addUnknownField(tag, FieldEncoding.VARINT, Long.valueOf((long) e.value));
                                break;
                            }
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

        public ShowRTHTipsContext redact(ShowRTHTipsContext value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
