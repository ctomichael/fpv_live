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

public final class FPVStatusBarItem extends Message<FPVStatusBarItem, Builder> {
    public static final ProtoAdapter<FPVStatusBarItem> ADAPTER = new ProtoAdapter_FPVStatusBarItem();
    public static final Integer DEFAULT_LEVEL = 0;
    public static final Boolean DEFAULT_NEEDBLINKED = false;
    public static final String DEFAULT_WARNINGTEXT = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 3)
    public final Integer level;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 2)
    public final Boolean needBlinked;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String warningText;

    public FPVStatusBarItem(String warningText2, Boolean needBlinked2, Integer level2) {
        this(warningText2, needBlinked2, level2, ByteString.EMPTY);
    }

    public FPVStatusBarItem(String warningText2, Boolean needBlinked2, Integer level2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.warningText = warningText2;
        this.needBlinked = needBlinked2;
        this.level = level2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.warningText = this.warningText;
        builder.needBlinked = this.needBlinked;
        builder.level = this.level;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FPVStatusBarItem)) {
            return false;
        }
        FPVStatusBarItem o = (FPVStatusBarItem) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.warningText, o.warningText) || !Internal.equals(this.needBlinked, o.needBlinked) || !Internal.equals(this.level, o.level)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.warningText != null ? this.warningText.hashCode() : 0)) * 37;
        if (this.needBlinked != null) {
            i = this.needBlinked.hashCode();
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
        if (this.warningText != null) {
            builder.append(", warningText=").append(this.warningText);
        }
        if (this.needBlinked != null) {
            builder.append(", needBlinked=").append(this.needBlinked);
        }
        if (this.level != null) {
            builder.append(", level=").append(this.level);
        }
        return builder.replace(0, 2, "FPVStatusBarItem{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<FPVStatusBarItem, Builder> {
        public Integer level;
        public Boolean needBlinked;
        public String warningText;

        public Builder warningText(String warningText2) {
            this.warningText = warningText2;
            return this;
        }

        public Builder needBlinked(Boolean needBlinked2) {
            this.needBlinked = needBlinked2;
            return this;
        }

        public Builder level(Integer level2) {
            this.level = level2;
            return this;
        }

        public FPVStatusBarItem build() {
            return new FPVStatusBarItem(this.warningText, this.needBlinked, this.level, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_FPVStatusBarItem extends ProtoAdapter<FPVStatusBarItem> {
        ProtoAdapter_FPVStatusBarItem() {
            super(FieldEncoding.LENGTH_DELIMITED, FPVStatusBarItem.class);
        }

        public int encodedSize(FPVStatusBarItem value) {
            int i;
            int i2 = 0;
            int encodedSizeWithTag = value.warningText != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.warningText) : 0;
            if (value.needBlinked != null) {
                i = ProtoAdapter.BOOL.encodedSizeWithTag(2, value.needBlinked);
            } else {
                i = 0;
            }
            int i3 = encodedSizeWithTag + i;
            if (value.level != null) {
                i2 = ProtoAdapter.INT32.encodedSizeWithTag(3, value.level);
            }
            return i3 + i2 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, FPVStatusBarItem value) throws IOException {
            if (value.warningText != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.warningText);
            }
            if (value.needBlinked != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 2, value.needBlinked);
            }
            if (value.level != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 3, value.level);
            }
            writer.writeBytes(value.unknownFields());
        }

        public FPVStatusBarItem decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.warningText(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.needBlinked(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 3:
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

        public FPVStatusBarItem redact(FPVStatusBarItem value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
