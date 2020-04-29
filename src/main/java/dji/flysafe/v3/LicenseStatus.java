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

public final class LicenseStatus extends Message<LicenseStatus, Builder> {
    public static final ProtoAdapter<LicenseStatus> ADAPTER = new ProtoAdapter_LicenseStatus();
    public static final Boolean DEFAULT_ENABLE = false;
    public static final Boolean DEFAULT_INVALID = false;
    public static final Boolean DEFAULT_IN_VALID_DATE = false;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 1)
    public final Boolean enable;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 2)
    public final Boolean in_valid_date;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 3)
    public final Boolean invalid;

    public LicenseStatus(Boolean enable2, Boolean in_valid_date2, Boolean invalid2) {
        this(enable2, in_valid_date2, invalid2, ByteString.EMPTY);
    }

    public LicenseStatus(Boolean enable2, Boolean in_valid_date2, Boolean invalid2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.enable = enable2;
        this.in_valid_date = in_valid_date2;
        this.invalid = invalid2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.enable = this.enable;
        builder.in_valid_date = this.in_valid_date;
        builder.invalid = this.invalid;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LicenseStatus)) {
            return false;
        }
        LicenseStatus o = (LicenseStatus) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.enable, o.enable) || !Internal.equals(this.in_valid_date, o.in_valid_date) || !Internal.equals(this.invalid, o.invalid)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.enable != null ? this.enable.hashCode() : 0)) * 37;
        if (this.in_valid_date != null) {
            i = this.in_valid_date.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.invalid != null) {
            i2 = this.invalid.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.enable != null) {
            builder.append(", enable=").append(this.enable);
        }
        if (this.in_valid_date != null) {
            builder.append(", in_valid_date=").append(this.in_valid_date);
        }
        if (this.invalid != null) {
            builder.append(", invalid=").append(this.invalid);
        }
        return builder.replace(0, 2, "LicenseStatus{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<LicenseStatus, Builder> {
        public Boolean enable;
        public Boolean in_valid_date;
        public Boolean invalid;

        public Builder enable(Boolean enable2) {
            this.enable = enable2;
            return this;
        }

        public Builder in_valid_date(Boolean in_valid_date2) {
            this.in_valid_date = in_valid_date2;
            return this;
        }

        public Builder invalid(Boolean invalid2) {
            this.invalid = invalid2;
            return this;
        }

        public LicenseStatus build() {
            return new LicenseStatus(this.enable, this.in_valid_date, this.invalid, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_LicenseStatus extends ProtoAdapter<LicenseStatus> {
        ProtoAdapter_LicenseStatus() {
            super(FieldEncoding.LENGTH_DELIMITED, LicenseStatus.class);
        }

        public int encodedSize(LicenseStatus value) {
            int i;
            int i2 = 0;
            int encodedSizeWithTag = value.enable != null ? ProtoAdapter.BOOL.encodedSizeWithTag(1, value.enable) : 0;
            if (value.in_valid_date != null) {
                i = ProtoAdapter.BOOL.encodedSizeWithTag(2, value.in_valid_date);
            } else {
                i = 0;
            }
            int i3 = encodedSizeWithTag + i;
            if (value.invalid != null) {
                i2 = ProtoAdapter.BOOL.encodedSizeWithTag(3, value.invalid);
            }
            return i3 + i2 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, LicenseStatus value) throws IOException {
            if (value.enable != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 1, value.enable);
            }
            if (value.in_valid_date != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 2, value.in_valid_date);
            }
            if (value.invalid != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 3, value.invalid);
            }
            writer.writeBytes(value.unknownFields());
        }

        public LicenseStatus decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.enable(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 2:
                            builder.in_valid_date(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 3:
                            builder.invalid(ProtoAdapter.BOOL.decode(reader));
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

        public LicenseStatus redact(LicenseStatus value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
