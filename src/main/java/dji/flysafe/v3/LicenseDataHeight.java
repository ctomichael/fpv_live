package dji.flysafe.v3;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import okio.ByteString;

public final class LicenseDataHeight extends Message<LicenseDataHeight, Builder> {
    public static final ProtoAdapter<LicenseDataHeight> ADAPTER = new ProtoAdapter_LicenseDataHeight();
    public static final Integer DEFAULT_HEIGHT_LIMIT = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 1)
    public final Integer height_limit;

    public LicenseDataHeight(Integer height_limit2) {
        this(height_limit2, ByteString.EMPTY);
    }

    public LicenseDataHeight(Integer height_limit2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.height_limit = height_limit2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.height_limit = this.height_limit;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LicenseDataHeight)) {
            return false;
        }
        LicenseDataHeight o = (LicenseDataHeight) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.height_limit, o.height_limit)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int result2 = (unknownFields().hashCode() * 37) + (this.height_limit != null ? this.height_limit.hashCode() : 0);
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.height_limit != null) {
            builder.append(", height_limit=").append(this.height_limit);
        }
        return builder.replace(0, 2, "LicenseDataHeight{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<LicenseDataHeight, Builder> {
        public Integer height_limit;

        public Builder height_limit(Integer height_limit2) {
            this.height_limit = height_limit2;
            return this;
        }

        public LicenseDataHeight build() {
            return new LicenseDataHeight(this.height_limit, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_LicenseDataHeight extends ProtoAdapter<LicenseDataHeight> {
        ProtoAdapter_LicenseDataHeight() {
            super(FieldEncoding.LENGTH_DELIMITED, LicenseDataHeight.class);
        }

        public int encodedSize(LicenseDataHeight value) {
            return (value.height_limit != null ? ProtoAdapter.UINT32.encodedSizeWithTag(1, value.height_limit) : 0) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, LicenseDataHeight value) throws IOException {
            if (value.height_limit != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.height_limit);
            }
            writer.writeBytes(value.unknownFields());
        }

        public LicenseDataHeight decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.height_limit(ProtoAdapter.UINT32.decode(reader));
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

        public LicenseDataHeight redact(LicenseDataHeight value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
