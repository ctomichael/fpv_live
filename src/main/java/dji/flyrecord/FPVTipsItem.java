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

public final class FPVTipsItem extends Message<FPVTipsItem, Builder> {
    public static final ProtoAdapter<FPVTipsItem> ADAPTER = new ProtoAdapter_FPVTipsItem();
    public static final Double DEFAULT_CONTINUETIME = Double.valueOf(0.0d);
    public static final String DEFAULT_IDENTIFIER = "";
    public static final Integer DEFAULT_LEVEL = 0;
    public static final Double DEFAULT_STARTTIME = Double.valueOf(0.0d);
    public static final String DEFAULT_TIPS = "";
    public static final Integer DEFAULT_TYPE = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 5)
    public final Double continueTime;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String identifier;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 1)
    public final Integer level;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 4)
    public final Double startTime;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 3)
    public final String tips;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 6)
    public final Integer type;

    public FPVTipsItem(Integer level2, String identifier2, String tips2, Double startTime2, Double continueTime2, Integer type2) {
        this(level2, identifier2, tips2, startTime2, continueTime2, type2, ByteString.EMPTY);
    }

    public FPVTipsItem(Integer level2, String identifier2, String tips2, Double startTime2, Double continueTime2, Integer type2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.level = level2;
        this.identifier = identifier2;
        this.tips = tips2;
        this.startTime = startTime2;
        this.continueTime = continueTime2;
        this.type = type2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.level = this.level;
        builder.identifier = this.identifier;
        builder.tips = this.tips;
        builder.startTime = this.startTime;
        builder.continueTime = this.continueTime;
        builder.type = this.type;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FPVTipsItem)) {
            return false;
        }
        FPVTipsItem o = (FPVTipsItem) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.level, o.level) || !Internal.equals(this.identifier, o.identifier) || !Internal.equals(this.tips, o.tips) || !Internal.equals(this.startTime, o.startTime) || !Internal.equals(this.continueTime, o.continueTime) || !Internal.equals(this.type, o.type)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.level != null ? this.level.hashCode() : 0)) * 37;
        if (this.identifier != null) {
            i = this.identifier.hashCode();
        } else {
            i = 0;
        }
        int i6 = (hashCode + i) * 37;
        if (this.tips != null) {
            i2 = this.tips.hashCode();
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 37;
        if (this.startTime != null) {
            i3 = this.startTime.hashCode();
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 37;
        if (this.continueTime != null) {
            i4 = this.continueTime.hashCode();
        } else {
            i4 = 0;
        }
        int i9 = (i8 + i4) * 37;
        if (this.type != null) {
            i5 = this.type.hashCode();
        }
        int result2 = i9 + i5;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.level != null) {
            builder.append(", level=").append(this.level);
        }
        if (this.identifier != null) {
            builder.append(", identifier=").append(this.identifier);
        }
        if (this.tips != null) {
            builder.append(", tips=").append(this.tips);
        }
        if (this.startTime != null) {
            builder.append(", startTime=").append(this.startTime);
        }
        if (this.continueTime != null) {
            builder.append(", continueTime=").append(this.continueTime);
        }
        if (this.type != null) {
            builder.append(", type=").append(this.type);
        }
        return builder.replace(0, 2, "FPVTipsItem{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<FPVTipsItem, Builder> {
        public Double continueTime;
        public String identifier;
        public Integer level;
        public Double startTime;
        public String tips;
        public Integer type;

        public Builder level(Integer level2) {
            this.level = level2;
            return this;
        }

        public Builder identifier(String identifier2) {
            this.identifier = identifier2;
            return this;
        }

        public Builder tips(String tips2) {
            this.tips = tips2;
            return this;
        }

        public Builder startTime(Double startTime2) {
            this.startTime = startTime2;
            return this;
        }

        public Builder continueTime(Double continueTime2) {
            this.continueTime = continueTime2;
            return this;
        }

        public Builder type(Integer type2) {
            this.type = type2;
            return this;
        }

        public FPVTipsItem build() {
            return new FPVTipsItem(this.level, this.identifier, this.tips, this.startTime, this.continueTime, this.type, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_FPVTipsItem extends ProtoAdapter<FPVTipsItem> {
        ProtoAdapter_FPVTipsItem() {
            super(FieldEncoding.LENGTH_DELIMITED, FPVTipsItem.class);
        }

        public int encodedSize(FPVTipsItem value) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5 = 0;
            int encodedSizeWithTag = value.level != null ? ProtoAdapter.INT32.encodedSizeWithTag(1, value.level) : 0;
            if (value.identifier != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.identifier);
            } else {
                i = 0;
            }
            int i6 = i + encodedSizeWithTag;
            if (value.tips != null) {
                i2 = ProtoAdapter.STRING.encodedSizeWithTag(3, value.tips);
            } else {
                i2 = 0;
            }
            int i7 = i6 + i2;
            if (value.startTime != null) {
                i3 = ProtoAdapter.DOUBLE.encodedSizeWithTag(4, value.startTime);
            } else {
                i3 = 0;
            }
            int i8 = i7 + i3;
            if (value.continueTime != null) {
                i4 = ProtoAdapter.DOUBLE.encodedSizeWithTag(5, value.continueTime);
            } else {
                i4 = 0;
            }
            int i9 = i4 + i8;
            if (value.type != null) {
                i5 = ProtoAdapter.INT32.encodedSizeWithTag(6, value.type);
            }
            return i9 + i5 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, FPVTipsItem value) throws IOException {
            if (value.level != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 1, value.level);
            }
            if (value.identifier != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.identifier);
            }
            if (value.tips != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 3, value.tips);
            }
            if (value.startTime != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 4, value.startTime);
            }
            if (value.continueTime != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 5, value.continueTime);
            }
            if (value.type != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 6, value.type);
            }
            writer.writeBytes(value.unknownFields());
        }

        public FPVTipsItem decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.level(ProtoAdapter.INT32.decode(reader));
                            break;
                        case 2:
                            builder.identifier(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            builder.tips(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 4:
                            builder.startTime(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 5:
                            builder.continueTime(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 6:
                            builder.type(ProtoAdapter.INT32.decode(reader));
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

        public FPVTipsItem redact(FPVTipsItem value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
