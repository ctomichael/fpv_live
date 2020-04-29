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

public final class ShowWarningContext extends Message<ShowWarningContext, Builder> {
    public static final ProtoAdapter<ShowWarningContext> ADAPTER = new ProtoAdapter_ShowWarningContext();
    public static final Double DEFAULT_LIMITHEIGHT = Double.valueOf(0.0d);
    public static final WarningEventType DEFAULT_TYPE = WarningEventType.TakeOffFailedUnderLimitArea;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "dji.flysafe.LimitArea#ADAPTER", label = WireField.Label.REPEATED, tag = 2)
    public final List<LimitArea> areas;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 3)
    public final Double limitHeight;
    @WireField(adapter = "dji.flysafe.WarningEventType#ADAPTER", tag = 1)
    public final WarningEventType type;

    public ShowWarningContext(WarningEventType type2, List<LimitArea> areas2, Double limitHeight2) {
        this(type2, areas2, limitHeight2, ByteString.EMPTY);
    }

    public ShowWarningContext(WarningEventType type2, List<LimitArea> areas2, Double limitHeight2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.type = type2;
        this.areas = Internal.immutableCopyOf("areas", areas2);
        this.limitHeight = limitHeight2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.type = this.type;
        builder.areas = Internal.copyOf("areas", this.areas);
        builder.limitHeight = this.limitHeight;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ShowWarningContext)) {
            return false;
        }
        ShowWarningContext o = (ShowWarningContext) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.type, o.type) || !this.areas.equals(o.areas) || !Internal.equals(this.limitHeight, o.limitHeight)) {
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
        int hashCode = ((((unknownFields().hashCode() * 37) + (this.type != null ? this.type.hashCode() : 0)) * 37) + this.areas.hashCode()) * 37;
        if (this.limitHeight != null) {
            i = this.limitHeight.hashCode();
        }
        int result2 = hashCode + i;
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
        return builder.replace(0, 2, "ShowWarningContext{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<ShowWarningContext, Builder> {
        public List<LimitArea> areas = Internal.newMutableList();
        public Double limitHeight;
        public WarningEventType type;

        public Builder type(WarningEventType type2) {
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

        public ShowWarningContext build() {
            return new ShowWarningContext(this.type, this.areas, this.limitHeight, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_ShowWarningContext extends ProtoAdapter<ShowWarningContext> {
        ProtoAdapter_ShowWarningContext() {
            super(FieldEncoding.LENGTH_DELIMITED, ShowWarningContext.class);
        }

        public int encodedSize(ShowWarningContext value) {
            int i = 0;
            int encodedSizeWithTag = (value.type != null ? WarningEventType.ADAPTER.encodedSizeWithTag(1, value.type) : 0) + LimitArea.ADAPTER.asRepeated().encodedSizeWithTag(2, value.areas);
            if (value.limitHeight != null) {
                i = ProtoAdapter.DOUBLE.encodedSizeWithTag(3, value.limitHeight);
            }
            return encodedSizeWithTag + i + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, ShowWarningContext value) throws IOException {
            if (value.type != null) {
                WarningEventType.ADAPTER.encodeWithTag(writer, 1, value.type);
            }
            LimitArea.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.areas);
            if (value.limitHeight != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 3, value.limitHeight);
            }
            writer.writeBytes(value.unknownFields());
        }

        public ShowWarningContext decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            try {
                                builder.type(WarningEventType.ADAPTER.decode(reader));
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

        public ShowWarningContext redact(ShowWarningContext value) {
            Builder builder = value.newBuilder();
            Internal.redactElements(builder.areas, LimitArea.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
