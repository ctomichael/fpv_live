package dji.flyrecord;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import okio.ByteString;

public final class DevicePreviwData extends Message<DevicePreviwData, Builder> {
    public static final ProtoAdapter<DevicePreviwData> ADAPTER = new ProtoAdapter_DevicePreviwData();
    public static final Integer DEFAULT_CODE = 0;
    public static final String DEFAULT_ERRMSG = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 1)
    public final Integer code;
    @WireField(adapter = "dji.flyrecord.DevicePreviwInfo#ADAPTER", tag = 2)
    public final DevicePreviwInfo data;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 3)
    public final String errmsg;

    public DevicePreviwData(Integer code2, DevicePreviwInfo data2, String errmsg2) {
        this(code2, data2, errmsg2, ByteString.EMPTY);
    }

    public DevicePreviwData(Integer code2, DevicePreviwInfo data2, String errmsg2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.code = code2;
        this.data = data2;
        this.errmsg = errmsg2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.code = this.code;
        builder.data = this.data;
        builder.errmsg = this.errmsg;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DevicePreviwData)) {
            return false;
        }
        DevicePreviwData o = (DevicePreviwData) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.code, o.code) || !Internal.equals(this.data, o.data) || !Internal.equals(this.errmsg, o.errmsg)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.code != null ? this.code.hashCode() : 0)) * 37;
        if (this.data != null) {
            i = this.data.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.errmsg != null) {
            i2 = this.errmsg.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.code != null) {
            builder.append(", code=").append(this.code);
        }
        if (this.data != null) {
            builder.append(", data=").append(this.data);
        }
        if (this.errmsg != null) {
            builder.append(", errmsg=").append(this.errmsg);
        }
        return builder.replace(0, 2, "DevicePreviwData{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<DevicePreviwData, Builder> {
        public Integer code;
        public DevicePreviwInfo data;
        public String errmsg;

        public Builder code(Integer code2) {
            this.code = code2;
            return this;
        }

        public Builder data(DevicePreviwInfo data2) {
            this.data = data2;
            return this;
        }

        public Builder errmsg(String errmsg2) {
            this.errmsg = errmsg2;
            return this;
        }

        public DevicePreviwData build() {
            return new DevicePreviwData(this.code, this.data, this.errmsg, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_DevicePreviwData extends ProtoAdapter<DevicePreviwData> {
        ProtoAdapter_DevicePreviwData() {
            super(FieldEncoding.LENGTH_DELIMITED, DevicePreviwData.class);
        }

        public int encodedSize(DevicePreviwData value) {
            int i;
            int i2 = 0;
            int encodedSizeWithTag = value.code != null ? ProtoAdapter.INT32.encodedSizeWithTag(1, value.code) : 0;
            if (value.data != null) {
                i = DevicePreviwInfo.ADAPTER.encodedSizeWithTag(2, value.data);
            } else {
                i = 0;
            }
            int i3 = encodedSizeWithTag + i;
            if (value.errmsg != null) {
                i2 = ProtoAdapter.STRING.encodedSizeWithTag(3, value.errmsg);
            }
            return i3 + i2 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, DevicePreviwData value) throws IOException {
            if (value.code != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 1, value.code);
            }
            if (value.data != null) {
                DevicePreviwInfo.ADAPTER.encodeWithTag(writer, 2, value.data);
            }
            if (value.errmsg != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 3, value.errmsg);
            }
            writer.writeBytes(value.unknownFields());
        }

        public DevicePreviwData decode(ProtoReader reader) throws IOException {
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
                            builder.data(DevicePreviwInfo.ADAPTER.decode(reader));
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

        public DevicePreviwData redact(DevicePreviwData value) {
            Builder builder = value.newBuilder();
            if (builder.data != null) {
                builder.data = DevicePreviwInfo.ADAPTER.redact(builder.data);
            }
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
