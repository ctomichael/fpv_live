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

public final class AppUnlockSapceExtraData extends Message<AppUnlockSapceExtraData, Builder> {
    public static final ProtoAdapter<AppUnlockSapceExtraData> ADAPTER = new ProtoAdapter_AppUnlockSapceExtraData();
    public static final Integer DEFAULT_COUNT = 0;
    public static final String DEFAULT_MESSAGE = "";
    public static final Integer DEFAULT_PAGENUMBER = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 2)
    public final Integer count;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String message;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 3)
    public final Integer pageNumber;

    public AppUnlockSapceExtraData(String message2, Integer count2, Integer pageNumber2) {
        this(message2, count2, pageNumber2, ByteString.EMPTY);
    }

    public AppUnlockSapceExtraData(String message2, Integer count2, Integer pageNumber2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.message = message2;
        this.count = count2;
        this.pageNumber = pageNumber2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.message = this.message;
        builder.count = this.count;
        builder.pageNumber = this.pageNumber;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AppUnlockSapceExtraData)) {
            return false;
        }
        AppUnlockSapceExtraData o = (AppUnlockSapceExtraData) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.message, o.message) || !Internal.equals(this.count, o.count) || !Internal.equals(this.pageNumber, o.pageNumber)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.message != null ? this.message.hashCode() : 0)) * 37;
        if (this.count != null) {
            i = this.count.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.pageNumber != null) {
            i2 = this.pageNumber.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.message != null) {
            builder.append(", message=").append(this.message);
        }
        if (this.count != null) {
            builder.append(", count=").append(this.count);
        }
        if (this.pageNumber != null) {
            builder.append(", pageNumber=").append(this.pageNumber);
        }
        return builder.replace(0, 2, "AppUnlockSapceExtraData{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<AppUnlockSapceExtraData, Builder> {
        public Integer count;
        public String message;
        public Integer pageNumber;

        public Builder message(String message2) {
            this.message = message2;
            return this;
        }

        public Builder count(Integer count2) {
            this.count = count2;
            return this;
        }

        public Builder pageNumber(Integer pageNumber2) {
            this.pageNumber = pageNumber2;
            return this;
        }

        public AppUnlockSapceExtraData build() {
            return new AppUnlockSapceExtraData(this.message, this.count, this.pageNumber, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_AppUnlockSapceExtraData extends ProtoAdapter<AppUnlockSapceExtraData> {
        ProtoAdapter_AppUnlockSapceExtraData() {
            super(FieldEncoding.LENGTH_DELIMITED, AppUnlockSapceExtraData.class);
        }

        public int encodedSize(AppUnlockSapceExtraData value) {
            int i;
            int i2 = 0;
            int encodedSizeWithTag = value.message != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.message) : 0;
            if (value.count != null) {
                i = ProtoAdapter.UINT32.encodedSizeWithTag(2, value.count);
            } else {
                i = 0;
            }
            int i3 = encodedSizeWithTag + i;
            if (value.pageNumber != null) {
                i2 = ProtoAdapter.UINT32.encodedSizeWithTag(3, value.pageNumber);
            }
            return i3 + i2 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, AppUnlockSapceExtraData value) throws IOException {
            if (value.message != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.message);
            }
            if (value.count != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.count);
            }
            if (value.pageNumber != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.pageNumber);
            }
            writer.writeBytes(value.unknownFields());
        }

        public AppUnlockSapceExtraData decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.message(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.count(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 3:
                            builder.pageNumber(ProtoAdapter.UINT32.decode(reader));
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

        public AppUnlockSapceExtraData redact(AppUnlockSapceExtraData value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
