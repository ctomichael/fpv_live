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

public final class LicenseDataCountry extends Message<LicenseDataCountry, Builder> {
    public static final ProtoAdapter<LicenseDataCountry> ADAPTER = new ProtoAdapter_LicenseDataCountry();
    public static final Integer DEFAULT_COUNTRY_NUMBER = 0;
    public static final Integer DEFAULT_HEIGHT_LIMIT = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 1)
    public final Integer country_number;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 2)
    public final Integer height_limit;

    public LicenseDataCountry(Integer country_number2, Integer height_limit2) {
        this(country_number2, height_limit2, ByteString.EMPTY);
    }

    public LicenseDataCountry(Integer country_number2, Integer height_limit2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.country_number = country_number2;
        this.height_limit = height_limit2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.country_number = this.country_number;
        builder.height_limit = this.height_limit;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LicenseDataCountry)) {
            return false;
        }
        LicenseDataCountry o = (LicenseDataCountry) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.country_number, o.country_number) || !Internal.equals(this.height_limit, o.height_limit)) {
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
        if (this.country_number != null) {
            i = this.country_number.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.height_limit != null) {
            i2 = this.height_limit.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.country_number != null) {
            builder.append(", country_number=").append(this.country_number);
        }
        if (this.height_limit != null) {
            builder.append(", height_limit=").append(this.height_limit);
        }
        return builder.replace(0, 2, "LicenseDataCountry{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<LicenseDataCountry, Builder> {
        public Integer country_number;
        public Integer height_limit;

        public Builder country_number(Integer country_number2) {
            this.country_number = country_number2;
            return this;
        }

        public Builder height_limit(Integer height_limit2) {
            this.height_limit = height_limit2;
            return this;
        }

        public LicenseDataCountry build() {
            return new LicenseDataCountry(this.country_number, this.height_limit, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_LicenseDataCountry extends ProtoAdapter<LicenseDataCountry> {
        ProtoAdapter_LicenseDataCountry() {
            super(FieldEncoding.LENGTH_DELIMITED, LicenseDataCountry.class);
        }

        public int encodedSize(LicenseDataCountry value) {
            int i = 0;
            int encodedSizeWithTag = value.country_number != null ? ProtoAdapter.UINT32.encodedSizeWithTag(1, value.country_number) : 0;
            if (value.height_limit != null) {
                i = ProtoAdapter.UINT32.encodedSizeWithTag(2, value.height_limit);
            }
            return encodedSizeWithTag + i + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, LicenseDataCountry value) throws IOException {
            if (value.country_number != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.country_number);
            }
            if (value.height_limit != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.height_limit);
            }
            writer.writeBytes(value.unknownFields());
        }

        public LicenseDataCountry decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.country_number(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 2:
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

        public LicenseDataCountry redact(LicenseDataCountry value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
