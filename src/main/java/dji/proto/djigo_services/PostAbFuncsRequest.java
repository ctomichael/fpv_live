package dji.proto.djigo_services;

import com.dji.fieldAnnotation.EXClassNullAway;
import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import okio.ByteString;

@EXClassNullAway
public final class PostAbFuncsRequest extends Message<PostAbFuncsRequest, Builder> {
    public static final ProtoAdapter<PostAbFuncsRequest> ADAPTER = new ProtoAdapter_PostAbFuncsRequest();
    public static final String DEFAULT_APP_UUID = "";
    public static final String DEFAULT_FUNC_UUID = "";
    public static final Integer DEFAULT_PRODUCT_TYPE = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String app_uuid;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String func_uuid;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 3)
    public final Integer product_type;

    public PostAbFuncsRequest(String app_uuid2, String func_uuid2, Integer product_type2) {
        this(app_uuid2, func_uuid2, product_type2, ByteString.EMPTY);
    }

    public PostAbFuncsRequest(String app_uuid2, String func_uuid2, Integer product_type2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.app_uuid = app_uuid2;
        this.func_uuid = func_uuid2;
        this.product_type = product_type2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.app_uuid = this.app_uuid;
        builder.func_uuid = this.func_uuid;
        builder.product_type = this.product_type;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PostAbFuncsRequest)) {
            return false;
        }
        PostAbFuncsRequest o = (PostAbFuncsRequest) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.app_uuid, o.app_uuid) || !Internal.equals(this.func_uuid, o.func_uuid) || !Internal.equals(this.product_type, o.product_type)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.app_uuid != null ? this.app_uuid.hashCode() : 0)) * 37;
        if (this.func_uuid != null) {
            i = this.func_uuid.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.product_type != null) {
            i2 = this.product_type.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.app_uuid != null) {
            builder.append(", app_uuid=").append(this.app_uuid);
        }
        if (this.func_uuid != null) {
            builder.append(", func_uuid=").append(this.func_uuid);
        }
        if (this.product_type != null) {
            builder.append(", product_type=").append(this.product_type);
        }
        return builder.replace(0, 2, "PostAbFuncsRequest{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<PostAbFuncsRequest, Builder> {
        public String app_uuid;
        public String func_uuid;
        public Integer product_type;

        public Builder app_uuid(String app_uuid2) {
            this.app_uuid = app_uuid2;
            return this;
        }

        public Builder func_uuid(String func_uuid2) {
            this.func_uuid = func_uuid2;
            return this;
        }

        public Builder product_type(Integer product_type2) {
            this.product_type = product_type2;
            return this;
        }

        public PostAbFuncsRequest build() {
            return new PostAbFuncsRequest(this.app_uuid, this.func_uuid, this.product_type, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_PostAbFuncsRequest extends ProtoAdapter<PostAbFuncsRequest> {
        ProtoAdapter_PostAbFuncsRequest() {
            super(FieldEncoding.LENGTH_DELIMITED, PostAbFuncsRequest.class);
        }

        public int encodedSize(PostAbFuncsRequest value) {
            int i;
            int i2 = 0;
            int encodedSizeWithTag = value.app_uuid != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.app_uuid) : 0;
            if (value.func_uuid != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.func_uuid);
            } else {
                i = 0;
            }
            int i3 = encodedSizeWithTag + i;
            if (value.product_type != null) {
                i2 = ProtoAdapter.INT32.encodedSizeWithTag(3, value.product_type);
            }
            return i3 + i2 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, PostAbFuncsRequest value) throws IOException {
            if (value.app_uuid != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.app_uuid);
            }
            if (value.func_uuid != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.func_uuid);
            }
            if (value.product_type != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 3, value.product_type);
            }
            writer.writeBytes(value.unknownFields());
        }

        public PostAbFuncsRequest decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.app_uuid(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.func_uuid(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            builder.product_type(ProtoAdapter.INT32.decode(reader));
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

        public PostAbFuncsRequest redact(PostAbFuncsRequest value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
