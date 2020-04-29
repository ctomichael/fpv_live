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

public final class FPVCheckListItem extends Message<FPVCheckListItem, Builder> {
    public static final ProtoAdapter<FPVCheckListItem> ADAPTER = new ProtoAdapter_FPVCheckListItem();
    public static final String DEFAULT_INFO = "";
    public static final Integer DEFAULT_LEVEL = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String info;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 2)
    public final Integer level;

    public FPVCheckListItem(String info2, Integer level2) {
        this(info2, level2, ByteString.EMPTY);
    }

    public FPVCheckListItem(String info2, Integer level2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.info = info2;
        this.level = level2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.info = this.info;
        builder.level = this.level;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FPVCheckListItem)) {
            return false;
        }
        FPVCheckListItem o = (FPVCheckListItem) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.info, o.info) || !Internal.equals(this.level, o.level)) {
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
        int hashCode = unknownFields().hashCode() * 37;
        if (this.info != null) {
            i = this.info.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.level != null) {
            i2 = this.level.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.info != null) {
            builder.append(", info=").append(this.info);
        }
        if (this.level != null) {
            builder.append(", level=").append(this.level);
        }
        return builder.replace(0, 2, "FPVCheckListItem{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<FPVCheckListItem, Builder> {
        public String info;
        public Integer level;

        public Builder info(String info2) {
            this.info = info2;
            return this;
        }

        public Builder level(Integer level2) {
            this.level = level2;
            return this;
        }

        public FPVCheckListItem build() {
            return new FPVCheckListItem(this.info, this.level, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_FPVCheckListItem extends ProtoAdapter<FPVCheckListItem> {
        ProtoAdapter_FPVCheckListItem() {
            super(FieldEncoding.LENGTH_DELIMITED, FPVCheckListItem.class);
        }

        public int encodedSize(FPVCheckListItem value) {
            int i = 0;
            int encodedSizeWithTag = value.info != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.info) : 0;
            if (value.level != null) {
                i = ProtoAdapter.INT32.encodedSizeWithTag(2, value.level);
            }
            return encodedSizeWithTag + i + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, FPVCheckListItem value) throws IOException {
            if (value.info != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.info);
            }
            if (value.level != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 2, value.level);
            }
            writer.writeBytes(value.unknownFields());
        }

        public FPVCheckListItem decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.info(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.level(ProtoAdapter.INT32.decode(reader));
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

        public FPVCheckListItem redact(FPVCheckListItem value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
