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

public final class LicenseDataArea extends Message<LicenseDataArea, Builder> {
    public static final ProtoAdapter<LicenseDataArea> ADAPTER = new ProtoAdapter_LicenseDataArea();
    public static final Integer DEFAULT_HEIGHT_LIMIT = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", label = WireField.Label.REPEATED, tag = 1)
    public final List<Integer> area_ids;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 2)
    public final Integer height_limit;

    public LicenseDataArea(List<Integer> area_ids2, Integer height_limit2) {
        this(area_ids2, height_limit2, ByteString.EMPTY);
    }

    public LicenseDataArea(List<Integer> area_ids2, Integer height_limit2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.area_ids = Internal.immutableCopyOf("area_ids", area_ids2);
        this.height_limit = height_limit2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.area_ids = Internal.copyOf("area_ids", this.area_ids);
        builder.height_limit = this.height_limit;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LicenseDataArea)) {
            return false;
        }
        LicenseDataArea o = (LicenseDataArea) other;
        if (!unknownFields().equals(o.unknownFields()) || !this.area_ids.equals(o.area_ids) || !Internal.equals(this.height_limit, o.height_limit)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int result2 = (((unknownFields().hashCode() * 37) + this.area_ids.hashCode()) * 37) + (this.height_limit != null ? this.height_limit.hashCode() : 0);
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!this.area_ids.isEmpty()) {
            builder.append(", area_ids=").append(this.area_ids);
        }
        if (this.height_limit != null) {
            builder.append(", height_limit=").append(this.height_limit);
        }
        return builder.replace(0, 2, "LicenseDataArea{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<LicenseDataArea, Builder> {
        public List<Integer> area_ids = Internal.newMutableList();
        public Integer height_limit;

        public Builder area_ids(List<Integer> area_ids2) {
            Internal.checkElementsNotNull(area_ids2);
            this.area_ids = area_ids2;
            return this;
        }

        public Builder height_limit(Integer height_limit2) {
            this.height_limit = height_limit2;
            return this;
        }

        public LicenseDataArea build() {
            return new LicenseDataArea(this.area_ids, this.height_limit, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_LicenseDataArea extends ProtoAdapter<LicenseDataArea> {
        ProtoAdapter_LicenseDataArea() {
            super(FieldEncoding.LENGTH_DELIMITED, LicenseDataArea.class);
        }

        public int encodedSize(LicenseDataArea value) {
            return (value.height_limit != null ? ProtoAdapter.UINT32.encodedSizeWithTag(2, value.height_limit) : 0) + ProtoAdapter.UINT32.asRepeated().encodedSizeWithTag(1, value.area_ids) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, LicenseDataArea value) throws IOException {
            ProtoAdapter.UINT32.asRepeated().encodeWithTag(writer, 1, value.area_ids);
            if (value.height_limit != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.height_limit);
            }
            writer.writeBytes(value.unknownFields());
        }

        public LicenseDataArea decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.area_ids.add(ProtoAdapter.UINT32.decode(reader));
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

        public LicenseDataArea redact(LicenseDataArea value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
