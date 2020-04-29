package dji.flysafe.v3;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.util.List;
import okio.ByteString;

public final class LicenseGroupModel extends Message<LicenseGroupModel, Builder> {
    public static final ProtoAdapter<LicenseGroupModel> ADAPTER = new ProtoAdapter_LicenseGroupModel();
    public static final Integer DEFAULT_CREATE_AT = 0;
    public static final Integer DEFAULT_VERSION = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 3)
    public final Integer create_at;
    @WireField(adapter = "dji.flysafe.v3.LicenseGroupInfo#ADAPTER", tag = 2)
    public final LicenseGroupInfo group_info;
    @WireField(adapter = "dji.flysafe.v3.LicenseModel#ADAPTER", label = WireField.Label.REPEATED, tag = 4)
    public final List<LicenseModel> license_models;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 1)
    public final Integer version;

    public LicenseGroupModel(Integer version2, LicenseGroupInfo group_info2, Integer create_at2, List<LicenseModel> license_models2) {
        this(version2, group_info2, create_at2, license_models2, ByteString.EMPTY);
    }

    public LicenseGroupModel(Integer version2, LicenseGroupInfo group_info2, Integer create_at2, List<LicenseModel> license_models2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.version = version2;
        this.group_info = group_info2;
        this.create_at = create_at2;
        this.license_models = Internal.immutableCopyOf("license_models", license_models2);
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.version = this.version;
        builder.group_info = this.group_info;
        builder.create_at = this.create_at;
        builder.license_models = Internal.copyOf("license_models", this.license_models);
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LicenseGroupModel)) {
            return false;
        }
        LicenseGroupModel o = (LicenseGroupModel) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.version, o.version) || !Internal.equals(this.group_info, o.group_info) || !Internal.equals(this.create_at, o.create_at) || !this.license_models.equals(o.license_models)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.version != null ? this.version.hashCode() : 0)) * 37;
        if (this.group_info != null) {
            i = this.group_info.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.create_at != null) {
            i2 = this.create_at.hashCode();
        }
        int result2 = ((i3 + i2) * 37) + this.license_models.hashCode();
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.version != null) {
            builder.append(", version=").append(this.version);
        }
        if (this.group_info != null) {
            builder.append(", group_info=").append(this.group_info);
        }
        if (this.create_at != null) {
            builder.append(", create_at=").append(this.create_at);
        }
        if (!this.license_models.isEmpty()) {
            builder.append(", license_models=").append(this.license_models);
        }
        return builder.replace(0, 2, "LicenseGroupModel{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<LicenseGroupModel, Builder> {
        public Integer create_at;
        public LicenseGroupInfo group_info;
        public List<LicenseModel> license_models = Internal.newMutableList();
        public Integer version;

        public Builder version(Integer version2) {
            this.version = version2;
            return this;
        }

        public Builder group_info(LicenseGroupInfo group_info2) {
            this.group_info = group_info2;
            return this;
        }

        public Builder create_at(Integer create_at2) {
            this.create_at = create_at2;
            return this;
        }

        public Builder license_models(List<LicenseModel> license_models2) {
            Internal.checkElementsNotNull(license_models2);
            this.license_models = license_models2;
            return this;
        }

        public LicenseGroupModel build() {
            return new LicenseGroupModel(this.version, this.group_info, this.create_at, this.license_models, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_LicenseGroupModel extends ProtoAdapter<LicenseGroupModel> {
        ProtoAdapter_LicenseGroupModel() {
            super(FieldEncoding.LENGTH_DELIMITED, LicenseGroupModel.class);
        }

        public int encodedSize(LicenseGroupModel value) {
            int i;
            int i2 = 0;
            int encodedSizeWithTag = value.version != null ? ProtoAdapter.UINT32.encodedSizeWithTag(1, value.version) : 0;
            if (value.group_info != null) {
                i = LicenseGroupInfo.ADAPTER.encodedSizeWithTag(2, value.group_info);
            } else {
                i = 0;
            }
            int i3 = encodedSizeWithTag + i;
            if (value.create_at != null) {
                i2 = ProtoAdapter.UINT32.encodedSizeWithTag(3, value.create_at);
            }
            return i3 + i2 + LicenseModel.ADAPTER.asRepeated().encodedSizeWithTag(4, value.license_models) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, LicenseGroupModel value) throws IOException {
            if (value.version != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.version);
            }
            if (value.group_info != null) {
                LicenseGroupInfo.ADAPTER.encodeWithTag(writer, 2, value.group_info);
            }
            if (value.create_at != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.create_at);
            }
            LicenseModel.ADAPTER.asRepeated().encodeWithTag(writer, 4, value.license_models);
            writer.writeBytes(value.unknownFields());
        }

        public LicenseGroupModel decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.version(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 2:
                            builder.group_info(LicenseGroupInfo.ADAPTER.decode(reader));
                            break;
                        case 3:
                            builder.create_at(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 4:
                            builder.license_models.add(LicenseModel.ADAPTER.decode(reader));
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

        public LicenseGroupModel redact(LicenseGroupModel value) {
            Builder builder = value.newBuilder();
            if (builder.group_info != null) {
                builder.group_info = LicenseGroupInfo.ADAPTER.redact(builder.group_info);
            }
            Internal.redactElements(builder.license_models, LicenseModel.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
