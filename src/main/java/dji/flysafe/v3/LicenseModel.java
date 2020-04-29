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

public final class LicenseModel extends Message<LicenseModel, Builder> {
    public static final ProtoAdapter<LicenseModel> ADAPTER = new ProtoAdapter_LicenseModel();
    private static final long serialVersionUID = 0;
    @WireField(adapter = "dji.flysafe.v3.License#ADAPTER", tag = 1)
    public final License license;
    @WireField(adapter = "dji.flysafe.v3.LicenseStatus#ADAPTER", tag = 2)
    public final LicenseStatus license_status;

    public LicenseModel(License license2, LicenseStatus license_status2) {
        this(license2, license_status2, ByteString.EMPTY);
    }

    public LicenseModel(License license2, LicenseStatus license_status2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.license = license2;
        this.license_status = license_status2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.license = this.license;
        builder.license_status = this.license_status;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LicenseModel)) {
            return false;
        }
        LicenseModel o = (LicenseModel) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.license, o.license) || !Internal.equals(this.license_status, o.license_status)) {
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
        if (this.license != null) {
            i = this.license.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.license_status != null) {
            i2 = this.license_status.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.license != null) {
            builder.append(", license=").append(this.license);
        }
        if (this.license_status != null) {
            builder.append(", license_status=").append(this.license_status);
        }
        return builder.replace(0, 2, "LicenseModel{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<LicenseModel, Builder> {
        public License license;
        public LicenseStatus license_status;

        public Builder license(License license2) {
            this.license = license2;
            return this;
        }

        public Builder license_status(LicenseStatus license_status2) {
            this.license_status = license_status2;
            return this;
        }

        public LicenseModel build() {
            return new LicenseModel(this.license, this.license_status, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_LicenseModel extends ProtoAdapter<LicenseModel> {
        ProtoAdapter_LicenseModel() {
            super(FieldEncoding.LENGTH_DELIMITED, LicenseModel.class);
        }

        public int encodedSize(LicenseModel value) {
            int i = 0;
            int encodedSizeWithTag = value.license != null ? License.ADAPTER.encodedSizeWithTag(1, value.license) : 0;
            if (value.license_status != null) {
                i = LicenseStatus.ADAPTER.encodedSizeWithTag(2, value.license_status);
            }
            return encodedSizeWithTag + i + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, LicenseModel value) throws IOException {
            if (value.license != null) {
                License.ADAPTER.encodeWithTag(writer, 1, value.license);
            }
            if (value.license_status != null) {
                LicenseStatus.ADAPTER.encodeWithTag(writer, 2, value.license_status);
            }
            writer.writeBytes(value.unknownFields());
        }

        public LicenseModel decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.license(License.ADAPTER.decode(reader));
                            break;
                        case 2:
                            builder.license_status(LicenseStatus.ADAPTER.decode(reader));
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

        public LicenseModel redact(LicenseModel value) {
            Builder builder = value.newBuilder();
            if (builder.license != null) {
                builder.license = License.ADAPTER.redact(builder.license);
            }
            if (builder.license_status != null) {
                builder.license_status = LicenseStatus.ADAPTER.redact(builder.license_status);
            }
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
