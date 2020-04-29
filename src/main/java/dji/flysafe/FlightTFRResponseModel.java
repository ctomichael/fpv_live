package dji.flysafe;

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

public final class FlightTFRResponseModel extends Message<FlightTFRResponseModel, Builder> {
    public static final ProtoAdapter<FlightTFRResponseModel> ADAPTER = new ProtoAdapter_FlightTFRResponseModel();
    public static final String DEFAULT_AMBA_TFRS_DATA = "";
    public static final String DEFAULT_AMBA_UID = "";
    public static final String DEFAULT_TFRS_DATA = "";
    public static final String DEFAULT_UID = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 5)
    public final String amba_tfrs_data;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 4)
    public final String amba_uid;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String tfrs_data;
    @WireField(adapter = "dji.flysafe.LimitArea#ADAPTER", label = WireField.Label.REPEATED, tag = 3)
    public final List<LimitArea> tfrs_infos;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String uid;

    public FlightTFRResponseModel(String tfrs_data2, String uid2, List<LimitArea> tfrs_infos2, String amba_uid2, String amba_tfrs_data2) {
        this(tfrs_data2, uid2, tfrs_infos2, amba_uid2, amba_tfrs_data2, ByteString.EMPTY);
    }

    public FlightTFRResponseModel(String tfrs_data2, String uid2, List<LimitArea> tfrs_infos2, String amba_uid2, String amba_tfrs_data2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.tfrs_data = tfrs_data2;
        this.uid = uid2;
        this.tfrs_infos = Internal.immutableCopyOf("tfrs_infos", tfrs_infos2);
        this.amba_uid = amba_uid2;
        this.amba_tfrs_data = amba_tfrs_data2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.tfrs_data = this.tfrs_data;
        builder.uid = this.uid;
        builder.tfrs_infos = Internal.copyOf("tfrs_infos", this.tfrs_infos);
        builder.amba_uid = this.amba_uid;
        builder.amba_tfrs_data = this.amba_tfrs_data;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FlightTFRResponseModel)) {
            return false;
        }
        FlightTFRResponseModel o = (FlightTFRResponseModel) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.tfrs_data, o.tfrs_data) || !Internal.equals(this.uid, o.uid) || !this.tfrs_infos.equals(o.tfrs_infos) || !Internal.equals(this.amba_uid, o.amba_uid) || !Internal.equals(this.amba_tfrs_data, o.amba_tfrs_data)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.tfrs_data != null ? this.tfrs_data.hashCode() : 0)) * 37;
        if (this.uid != null) {
            i = this.uid.hashCode();
        } else {
            i = 0;
        }
        int hashCode2 = (((hashCode + i) * 37) + this.tfrs_infos.hashCode()) * 37;
        if (this.amba_uid != null) {
            i2 = this.amba_uid.hashCode();
        } else {
            i2 = 0;
        }
        int i4 = (hashCode2 + i2) * 37;
        if (this.amba_tfrs_data != null) {
            i3 = this.amba_tfrs_data.hashCode();
        }
        int result2 = i4 + i3;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.tfrs_data != null) {
            builder.append(", tfrs_data=").append(this.tfrs_data);
        }
        if (this.uid != null) {
            builder.append(", uid=").append(this.uid);
        }
        if (!this.tfrs_infos.isEmpty()) {
            builder.append(", tfrs_infos=").append(this.tfrs_infos);
        }
        if (this.amba_uid != null) {
            builder.append(", amba_uid=").append(this.amba_uid);
        }
        if (this.amba_tfrs_data != null) {
            builder.append(", amba_tfrs_data=").append(this.amba_tfrs_data);
        }
        return builder.replace(0, 2, "FlightTFRResponseModel{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<FlightTFRResponseModel, Builder> {
        public String amba_tfrs_data;
        public String amba_uid;
        public String tfrs_data;
        public List<LimitArea> tfrs_infos = Internal.newMutableList();
        public String uid;

        public Builder tfrs_data(String tfrs_data2) {
            this.tfrs_data = tfrs_data2;
            return this;
        }

        public Builder uid(String uid2) {
            this.uid = uid2;
            return this;
        }

        public Builder tfrs_infos(List<LimitArea> tfrs_infos2) {
            Internal.checkElementsNotNull(tfrs_infos2);
            this.tfrs_infos = tfrs_infos2;
            return this;
        }

        public Builder amba_uid(String amba_uid2) {
            this.amba_uid = amba_uid2;
            return this;
        }

        public Builder amba_tfrs_data(String amba_tfrs_data2) {
            this.amba_tfrs_data = amba_tfrs_data2;
            return this;
        }

        public FlightTFRResponseModel build() {
            return new FlightTFRResponseModel(this.tfrs_data, this.uid, this.tfrs_infos, this.amba_uid, this.amba_tfrs_data, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_FlightTFRResponseModel extends ProtoAdapter<FlightTFRResponseModel> {
        ProtoAdapter_FlightTFRResponseModel() {
            super(FieldEncoding.LENGTH_DELIMITED, FlightTFRResponseModel.class);
        }

        public int encodedSize(FlightTFRResponseModel value) {
            int i;
            int i2;
            int i3 = 0;
            int encodedSizeWithTag = value.tfrs_data != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.tfrs_data) : 0;
            if (value.uid != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.uid);
            } else {
                i = 0;
            }
            int encodedSizeWithTag2 = LimitArea.ADAPTER.asRepeated().encodedSizeWithTag(3, value.tfrs_infos) + encodedSizeWithTag + i;
            if (value.amba_uid != null) {
                i2 = ProtoAdapter.STRING.encodedSizeWithTag(4, value.amba_uid);
            } else {
                i2 = 0;
            }
            int i4 = i2 + encodedSizeWithTag2;
            if (value.amba_tfrs_data != null) {
                i3 = ProtoAdapter.STRING.encodedSizeWithTag(5, value.amba_tfrs_data);
            }
            return i4 + i3 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, FlightTFRResponseModel value) throws IOException {
            if (value.tfrs_data != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.tfrs_data);
            }
            if (value.uid != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.uid);
            }
            LimitArea.ADAPTER.asRepeated().encodeWithTag(writer, 3, value.tfrs_infos);
            if (value.amba_uid != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 4, value.amba_uid);
            }
            if (value.amba_tfrs_data != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 5, value.amba_tfrs_data);
            }
            writer.writeBytes(value.unknownFields());
        }

        public FlightTFRResponseModel decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.tfrs_data(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.uid(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            builder.tfrs_infos.add(LimitArea.ADAPTER.decode(reader));
                            break;
                        case 4:
                            builder.amba_uid(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 5:
                            builder.amba_tfrs_data(ProtoAdapter.STRING.decode(reader));
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

        public FlightTFRResponseModel redact(FlightTFRResponseModel value) {
            Builder builder = value.newBuilder();
            Internal.redactElements(builder.tfrs_infos, LimitArea.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
