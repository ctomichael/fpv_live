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

public final class LicenseGroup extends Message<LicenseGroup, Builder> {
    public static final ProtoAdapter<LicenseGroup> ADAPTER = new ProtoAdapter_LicenseGroup();
    public static final Integer DEFAULT_VERSION = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "dji.flysafe.v3.LicenseGroupInfo#ADAPTER", tag = 2)
    public final LicenseGroupInfo info;
    @WireField(adapter = "dji.flysafe.v3.License#ADAPTER", label = WireField.Label.REPEATED, tag = 3)
    public final List<License> licenses;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 1)
    public final Integer version;

    public LicenseGroup(Integer version2, LicenseGroupInfo info2, List<License> licenses2) {
        this(version2, info2, licenses2, ByteString.EMPTY);
    }

    public LicenseGroup(Integer version2, LicenseGroupInfo info2, List<License> licenses2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.version = version2;
        this.info = info2;
        this.licenses = Internal.immutableCopyOf("licenses", licenses2);
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.version = this.version;
        builder.info = this.info;
        builder.licenses = Internal.copyOf("licenses", this.licenses);
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LicenseGroup)) {
            return false;
        }
        LicenseGroup o = (LicenseGroup) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.version, o.version) || !Internal.equals(this.info, o.info) || !this.licenses.equals(o.licenses)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.version != null ? this.version.hashCode() : 0)) * 37;
        if (this.info != null) {
            i = this.info.hashCode();
        }
        int result2 = ((hashCode + i) * 37) + this.licenses.hashCode();
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.version != null) {
            builder.append(", version=").append(this.version);
        }
        if (this.info != null) {
            builder.append(", info=").append(this.info);
        }
        if (!this.licenses.isEmpty()) {
            builder.append(", licenses=").append(this.licenses);
        }
        return builder.replace(0, 2, "LicenseGroup{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<LicenseGroup, Builder> {
        public LicenseGroupInfo info;
        public List<License> licenses = Internal.newMutableList();
        public Integer version;

        public Builder version(Integer version2) {
            this.version = version2;
            return this;
        }

        public Builder info(LicenseGroupInfo info2) {
            this.info = info2;
            return this;
        }

        public Builder licenses(List<License> licenses2) {
            Internal.checkElementsNotNull(licenses2);
            this.licenses = licenses2;
            return this;
        }

        public LicenseGroup build() {
            return new LicenseGroup(this.version, this.info, this.licenses, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_LicenseGroup extends ProtoAdapter<LicenseGroup> {
        ProtoAdapter_LicenseGroup() {
            super(FieldEncoding.LENGTH_DELIMITED, LicenseGroup.class);
        }

        public int encodedSize(LicenseGroup value) {
            int i = 0;
            int encodedSizeWithTag = value.version != null ? ProtoAdapter.UINT32.encodedSizeWithTag(1, value.version) : 0;
            if (value.info != null) {
                i = LicenseGroupInfo.ADAPTER.encodedSizeWithTag(2, value.info);
            }
            return encodedSizeWithTag + i + License.ADAPTER.asRepeated().encodedSizeWithTag(3, value.licenses) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, LicenseGroup value) throws IOException {
            if (value.version != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.version);
            }
            if (value.info != null) {
                LicenseGroupInfo.ADAPTER.encodeWithTag(writer, 2, value.info);
            }
            License.ADAPTER.asRepeated().encodeWithTag(writer, 3, value.licenses);
            writer.writeBytes(value.unknownFields());
        }

        public LicenseGroup decode(ProtoReader reader) throws IOException {
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
                            builder.info(LicenseGroupInfo.ADAPTER.decode(reader));
                            break;
                        case 3:
                            builder.licenses.add(License.ADAPTER.decode(reader));
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

        public LicenseGroup redact(LicenseGroup value) {
            Builder builder = value.newBuilder();
            if (builder.info != null) {
                builder.info = LicenseGroupInfo.ADAPTER.redact(builder.info);
            }
            Internal.redactElements(builder.licenses, License.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
