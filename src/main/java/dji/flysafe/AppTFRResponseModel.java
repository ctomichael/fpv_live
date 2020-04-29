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

public final class AppTFRResponseModel extends Message<AppTFRResponseModel, Builder> {
    public static final ProtoAdapter<AppTFRResponseModel> ADAPTER = new ProtoAdapter_AppTFRResponseModel();
    public static final Integer DEFAULT_TIME_STAMP = 0;
    public static final String DEFAULT_UID = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "dji.flysafe.LimitArea#ADAPTER", label = WireField.Label.REPEATED, tag = 3)
    public final List<LimitArea> tfrs_infos;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 1)
    public final Integer time_stamp;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String uid;

    public AppTFRResponseModel(Integer time_stamp2, String uid2, List<LimitArea> tfrs_infos2) {
        this(time_stamp2, uid2, tfrs_infos2, ByteString.EMPTY);
    }

    public AppTFRResponseModel(Integer time_stamp2, String uid2, List<LimitArea> tfrs_infos2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.time_stamp = time_stamp2;
        this.uid = uid2;
        this.tfrs_infos = Internal.immutableCopyOf("tfrs_infos", tfrs_infos2);
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.time_stamp = this.time_stamp;
        builder.uid = this.uid;
        builder.tfrs_infos = Internal.copyOf("tfrs_infos", this.tfrs_infos);
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AppTFRResponseModel)) {
            return false;
        }
        AppTFRResponseModel o = (AppTFRResponseModel) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.time_stamp, o.time_stamp) || !Internal.equals(this.uid, o.uid) || !this.tfrs_infos.equals(o.tfrs_infos)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.time_stamp != null ? this.time_stamp.hashCode() : 0)) * 37;
        if (this.uid != null) {
            i = this.uid.hashCode();
        }
        int result2 = ((hashCode + i) * 37) + this.tfrs_infos.hashCode();
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.time_stamp != null) {
            builder.append(", time_stamp=").append(this.time_stamp);
        }
        if (this.uid != null) {
            builder.append(", uid=").append(this.uid);
        }
        if (!this.tfrs_infos.isEmpty()) {
            builder.append(", tfrs_infos=").append(this.tfrs_infos);
        }
        return builder.replace(0, 2, "AppTFRResponseModel{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<AppTFRResponseModel, Builder> {
        public List<LimitArea> tfrs_infos = Internal.newMutableList();
        public Integer time_stamp;
        public String uid;

        public Builder time_stamp(Integer time_stamp2) {
            this.time_stamp = time_stamp2;
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

        public AppTFRResponseModel build() {
            return new AppTFRResponseModel(this.time_stamp, this.uid, this.tfrs_infos, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_AppTFRResponseModel extends ProtoAdapter<AppTFRResponseModel> {
        ProtoAdapter_AppTFRResponseModel() {
            super(FieldEncoding.LENGTH_DELIMITED, AppTFRResponseModel.class);
        }

        public int encodedSize(AppTFRResponseModel value) {
            int i = 0;
            int encodedSizeWithTag = value.time_stamp != null ? ProtoAdapter.UINT32.encodedSizeWithTag(1, value.time_stamp) : 0;
            if (value.uid != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.uid);
            }
            return encodedSizeWithTag + i + LimitArea.ADAPTER.asRepeated().encodedSizeWithTag(3, value.tfrs_infos) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, AppTFRResponseModel value) throws IOException {
            if (value.time_stamp != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.time_stamp);
            }
            if (value.uid != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.uid);
            }
            LimitArea.ADAPTER.asRepeated().encodeWithTag(writer, 3, value.tfrs_infos);
            writer.writeBytes(value.unknownFields());
        }

        public AppTFRResponseModel decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.time_stamp(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 2:
                            builder.uid(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            builder.tfrs_infos.add(LimitArea.ADAPTER.decode(reader));
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

        public AppTFRResponseModel redact(AppTFRResponseModel value) {
            Builder builder = value.newBuilder();
            Internal.redactElements(builder.tfrs_infos, LimitArea.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
