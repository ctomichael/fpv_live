package dji.flyrecord;

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

public final class RecordPreviwData extends Message<RecordPreviwData, Builder> {
    public static final ProtoAdapter<RecordPreviwData> ADAPTER = new ProtoAdapter_RecordPreviwData();
    public static final Integer DEFAULT_CODE = 0;
    public static final String DEFAULT_ERRMSG = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 1)
    public final Integer code;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 3)
    public final String errmsg;
    @WireField(adapter = "dji.flyrecord.RecordPreviwInfo#ADAPTER", label = WireField.Label.REPEATED, tag = 2)
    public final List<RecordPreviwInfo> listData;

    public RecordPreviwData(Integer code2, List<RecordPreviwInfo> listData2, String errmsg2) {
        this(code2, listData2, errmsg2, ByteString.EMPTY);
    }

    public RecordPreviwData(Integer code2, List<RecordPreviwInfo> listData2, String errmsg2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.code = code2;
        this.listData = Internal.immutableCopyOf("listData", listData2);
        this.errmsg = errmsg2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.code = this.code;
        builder.listData = Internal.copyOf("listData", this.listData);
        builder.errmsg = this.errmsg;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RecordPreviwData)) {
            return false;
        }
        RecordPreviwData o = (RecordPreviwData) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.code, o.code) || !this.listData.equals(o.listData) || !Internal.equals(this.errmsg, o.errmsg)) {
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
        int hashCode = ((((unknownFields().hashCode() * 37) + (this.code != null ? this.code.hashCode() : 0)) * 37) + this.listData.hashCode()) * 37;
        if (this.errmsg != null) {
            i = this.errmsg.hashCode();
        }
        int result2 = hashCode + i;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.code != null) {
            builder.append(", code=").append(this.code);
        }
        if (!this.listData.isEmpty()) {
            builder.append(", listData=").append(this.listData);
        }
        if (this.errmsg != null) {
            builder.append(", errmsg=").append(this.errmsg);
        }
        return builder.replace(0, 2, "RecordPreviwData{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<RecordPreviwData, Builder> {
        public Integer code;
        public String errmsg;
        public List<RecordPreviwInfo> listData = Internal.newMutableList();

        public Builder code(Integer code2) {
            this.code = code2;
            return this;
        }

        public Builder listData(List<RecordPreviwInfo> listData2) {
            Internal.checkElementsNotNull(listData2);
            this.listData = listData2;
            return this;
        }

        public Builder errmsg(String errmsg2) {
            this.errmsg = errmsg2;
            return this;
        }

        public RecordPreviwData build() {
            return new RecordPreviwData(this.code, this.listData, this.errmsg, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_RecordPreviwData extends ProtoAdapter<RecordPreviwData> {
        ProtoAdapter_RecordPreviwData() {
            super(FieldEncoding.LENGTH_DELIMITED, RecordPreviwData.class);
        }

        public int encodedSize(RecordPreviwData value) {
            int i = 0;
            int encodedSizeWithTag = (value.code != null ? ProtoAdapter.INT32.encodedSizeWithTag(1, value.code) : 0) + RecordPreviwInfo.ADAPTER.asRepeated().encodedSizeWithTag(2, value.listData);
            if (value.errmsg != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(3, value.errmsg);
            }
            return encodedSizeWithTag + i + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, RecordPreviwData value) throws IOException {
            if (value.code != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 1, value.code);
            }
            RecordPreviwInfo.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.listData);
            if (value.errmsg != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 3, value.errmsg);
            }
            writer.writeBytes(value.unknownFields());
        }

        public RecordPreviwData decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.code(ProtoAdapter.INT32.decode(reader));
                            break;
                        case 2:
                            builder.listData.add(RecordPreviwInfo.ADAPTER.decode(reader));
                            break;
                        case 3:
                            builder.errmsg(ProtoAdapter.STRING.decode(reader));
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

        public RecordPreviwData redact(RecordPreviwData value) {
            Builder builder = value.newBuilder();
            Internal.redactElements(builder.listData, RecordPreviwInfo.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
