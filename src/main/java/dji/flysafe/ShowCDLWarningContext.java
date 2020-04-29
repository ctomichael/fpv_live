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

public final class ShowCDLWarningContext extends Message<ShowCDLWarningContext, Builder> {
    public static final ProtoAdapter<ShowCDLWarningContext> ADAPTER = new ProtoAdapter_ShowCDLWarningContext();
    public static final Integer DEFAULT_COUNTDOWN = 0;
    public static final Double DEFAULT_LIMITHEIGHT = Double.valueOf(0.0d);
    public static final CDLWarningEventType DEFAULT_TYPE = CDLWarningEventType.InLimitArea;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "dji.flysafe.LimitArea#ADAPTER", label = WireField.Label.REPEATED, tag = 2)
    public final List<LimitArea> areas;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 4)
    public final Integer countdown;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 3)
    public final Double limitHeight;
    @WireField(adapter = "dji.flysafe.CDLWarningEventType#ADAPTER", tag = 1)
    public final CDLWarningEventType type;

    public ShowCDLWarningContext(CDLWarningEventType type2, List<LimitArea> areas2, Double limitHeight2, Integer countdown2) {
        this(type2, areas2, limitHeight2, countdown2, ByteString.EMPTY);
    }

    public ShowCDLWarningContext(CDLWarningEventType type2, List<LimitArea> areas2, Double limitHeight2, Integer countdown2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.type = type2;
        this.areas = Internal.immutableCopyOf("areas", areas2);
        this.limitHeight = limitHeight2;
        this.countdown = countdown2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.type = this.type;
        builder.areas = Internal.copyOf("areas", this.areas);
        builder.limitHeight = this.limitHeight;
        builder.countdown = this.countdown;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ShowCDLWarningContext)) {
            return false;
        }
        ShowCDLWarningContext o = (ShowCDLWarningContext) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.type, o.type) || !this.areas.equals(o.areas) || !Internal.equals(this.limitHeight, o.limitHeight) || !Internal.equals(this.countdown, o.countdown)) {
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
        int hashCode = ((((unknownFields().hashCode() * 37) + (this.type != null ? this.type.hashCode() : 0)) * 37) + this.areas.hashCode()) * 37;
        if (this.limitHeight != null) {
            i = this.limitHeight.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.countdown != null) {
            i2 = this.countdown.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.type != null) {
            builder.append(", type=").append(this.type);
        }
        if (!this.areas.isEmpty()) {
            builder.append(", areas=").append(this.areas);
        }
        if (this.limitHeight != null) {
            builder.append(", limitHeight=").append(this.limitHeight);
        }
        if (this.countdown != null) {
            builder.append(", countdown=").append(this.countdown);
        }
        return builder.replace(0, 2, "ShowCDLWarningContext{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<ShowCDLWarningContext, Builder> {
        public List<LimitArea> areas = Internal.newMutableList();
        public Integer countdown;
        public Double limitHeight;
        public CDLWarningEventType type;

        public Builder type(CDLWarningEventType type2) {
            this.type = type2;
            return this;
        }

        public Builder areas(List<LimitArea> areas2) {
            Internal.checkElementsNotNull(areas2);
            this.areas = areas2;
            return this;
        }

        public Builder limitHeight(Double limitHeight2) {
            this.limitHeight = limitHeight2;
            return this;
        }

        public Builder countdown(Integer countdown2) {
            this.countdown = countdown2;
            return this;
        }

        public ShowCDLWarningContext build() {
            return new ShowCDLWarningContext(this.type, this.areas, this.limitHeight, this.countdown, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_ShowCDLWarningContext extends ProtoAdapter<ShowCDLWarningContext> {
        ProtoAdapter_ShowCDLWarningContext() {
            super(FieldEncoding.LENGTH_DELIMITED, ShowCDLWarningContext.class);
        }

        public int encodedSize(ShowCDLWarningContext value) {
            int i;
            int i2 = 0;
            int encodedSizeWithTag = LimitArea.ADAPTER.asRepeated().encodedSizeWithTag(2, value.areas) + (value.type != null ? CDLWarningEventType.ADAPTER.encodedSizeWithTag(1, value.type) : 0);
            if (value.limitHeight != null) {
                i = ProtoAdapter.DOUBLE.encodedSizeWithTag(3, value.limitHeight);
            } else {
                i = 0;
            }
            int i3 = i + encodedSizeWithTag;
            if (value.countdown != null) {
                i2 = ProtoAdapter.UINT32.encodedSizeWithTag(4, value.countdown);
            }
            return i3 + i2 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, ShowCDLWarningContext value) throws IOException {
            if (value.type != null) {
                CDLWarningEventType.ADAPTER.encodeWithTag(writer, 1, value.type);
            }
            LimitArea.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.areas);
            if (value.limitHeight != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 3, value.limitHeight);
            }
            if (value.countdown != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.countdown);
            }
            writer.writeBytes(value.unknownFields());
        }

        public ShowCDLWarningContext decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            try {
                                builder.type(CDLWarningEventType.ADAPTER.decode(reader));
                                break;
                            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
                                builder.addUnknownField(tag, FieldEncoding.VARINT, Long.valueOf((long) e.value));
                                break;
                            }
                        case 2:
                            builder.areas.add(LimitArea.ADAPTER.decode(reader));
                            break;
                        case 3:
                            builder.limitHeight(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 4:
                            builder.countdown(ProtoAdapter.UINT32.decode(reader));
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

        public ShowCDLWarningContext redact(ShowCDLWarningContext value) {
            Builder builder = value.newBuilder();
            Internal.redactElements(builder.areas, LimitArea.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
