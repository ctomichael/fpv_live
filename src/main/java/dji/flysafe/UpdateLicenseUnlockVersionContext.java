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

public final class UpdateLicenseUnlockVersionContext extends Message<UpdateLicenseUnlockVersionContext, Builder> {
    public static final ProtoAdapter<UpdateLicenseUnlockVersionContext> ADAPTER = new ProtoAdapter_UpdateLicenseUnlockVersionContext();
    public static final LicenseUnlockVersion DEFAULT_UNLOCK_VERSION = LicenseUnlockVersion.Version2;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "dji.flysafe.LicenseUnlockVersion#ADAPTER", tag = 2)
    public final LicenseUnlockVersion unlock_version;

    public UpdateLicenseUnlockVersionContext(LicenseUnlockVersion unlock_version2) {
        this(unlock_version2, ByteString.EMPTY);
    }

    public UpdateLicenseUnlockVersionContext(LicenseUnlockVersion unlock_version2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.unlock_version = unlock_version2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.unlock_version = this.unlock_version;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UpdateLicenseUnlockVersionContext)) {
            return false;
        }
        UpdateLicenseUnlockVersionContext o = (UpdateLicenseUnlockVersionContext) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.unlock_version, o.unlock_version)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int result2 = (unknownFields().hashCode() * 37) + (this.unlock_version != null ? this.unlock_version.hashCode() : 0);
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.unlock_version != null) {
            builder.append(", unlock_version=").append(this.unlock_version);
        }
        return builder.replace(0, 2, "UpdateLicenseUnlockVersionContext{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<UpdateLicenseUnlockVersionContext, Builder> {
        public LicenseUnlockVersion unlock_version;

        public Builder unlock_version(LicenseUnlockVersion unlock_version2) {
            this.unlock_version = unlock_version2;
            return this;
        }

        public UpdateLicenseUnlockVersionContext build() {
            return new UpdateLicenseUnlockVersionContext(this.unlock_version, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_UpdateLicenseUnlockVersionContext extends ProtoAdapter<UpdateLicenseUnlockVersionContext> {
        ProtoAdapter_UpdateLicenseUnlockVersionContext() {
            super(FieldEncoding.LENGTH_DELIMITED, UpdateLicenseUnlockVersionContext.class);
        }

        public int encodedSize(UpdateLicenseUnlockVersionContext value) {
            return (value.unlock_version != null ? LicenseUnlockVersion.ADAPTER.encodedSizeWithTag(2, value.unlock_version) : 0) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, UpdateLicenseUnlockVersionContext value) throws IOException {
            if (value.unlock_version != null) {
                LicenseUnlockVersion.ADAPTER.encodeWithTag(writer, 2, value.unlock_version);
            }
            writer.writeBytes(value.unknownFields());
        }

        public UpdateLicenseUnlockVersionContext decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 2:
                            try {
                                builder.unlock_version(LicenseUnlockVersion.ADAPTER.decode(reader));
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

        public UpdateLicenseUnlockVersionContext redact(UpdateLicenseUnlockVersionContext value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
