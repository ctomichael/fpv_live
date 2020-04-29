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
public final class PostAbfuncsResponse extends Message<PostAbfuncsResponse, Builder> {
    public static final ProtoAdapter<PostAbfuncsResponse> ADAPTER = new ProtoAdapter_PostAbfuncsResponse();
    public static final Boolean DEFAULT_AB_FUNCS_RESULT = false;
    public static final String DEFAULT_AES_RESULT = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 1)
    public final Boolean ab_funcs_result;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String aes_result;

    public PostAbfuncsResponse(Boolean ab_funcs_result2, String aes_result2) {
        this(ab_funcs_result2, aes_result2, ByteString.EMPTY);
    }

    public PostAbfuncsResponse(Boolean ab_funcs_result2, String aes_result2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.ab_funcs_result = ab_funcs_result2;
        this.aes_result = aes_result2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.ab_funcs_result = this.ab_funcs_result;
        builder.aes_result = this.aes_result;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PostAbfuncsResponse)) {
            return false;
        }
        PostAbfuncsResponse o = (PostAbfuncsResponse) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.ab_funcs_result, o.ab_funcs_result) || !Internal.equals(this.aes_result, o.aes_result)) {
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
        int hashCode = unknownFields().hashCode() * 37;
        if (this.ab_funcs_result != null) {
            i = this.ab_funcs_result.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.aes_result != null) {
            i2 = this.aes_result.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.ab_funcs_result != null) {
            builder.append(", ab_funcs_result=").append(this.ab_funcs_result);
        }
        if (this.aes_result != null) {
            builder.append(", aes_result=").append(this.aes_result);
        }
        return builder.replace(0, 2, "PostAbfuncsResponse{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<PostAbfuncsResponse, Builder> {
        public Boolean ab_funcs_result;
        public String aes_result;

        public Builder ab_funcs_result(Boolean ab_funcs_result2) {
            this.ab_funcs_result = ab_funcs_result2;
            return this;
        }

        public Builder aes_result(String aes_result2) {
            this.aes_result = aes_result2;
            return this;
        }

        public PostAbfuncsResponse build() {
            return new PostAbfuncsResponse(this.ab_funcs_result, this.aes_result, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_PostAbfuncsResponse extends ProtoAdapter<PostAbfuncsResponse> {
        ProtoAdapter_PostAbfuncsResponse() {
            super(FieldEncoding.LENGTH_DELIMITED, PostAbfuncsResponse.class);
        }

        public int encodedSize(PostAbfuncsResponse value) {
            int i = 0;
            int encodedSizeWithTag = value.ab_funcs_result != null ? ProtoAdapter.BOOL.encodedSizeWithTag(1, value.ab_funcs_result) : 0;
            if (value.aes_result != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.aes_result);
            }
            return encodedSizeWithTag + i + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, PostAbfuncsResponse value) throws IOException {
            if (value.ab_funcs_result != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 1, value.ab_funcs_result);
            }
            if (value.aes_result != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.aes_result);
            }
            writer.writeBytes(value.unknownFields());
        }

        public PostAbfuncsResponse decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.ab_funcs_result(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 2:
                            builder.aes_result(ProtoAdapter.STRING.decode(reader));
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

        public PostAbfuncsResponse redact(PostAbfuncsResponse value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
