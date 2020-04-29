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

public final class ShowAreasContext extends Message<ShowAreasContext, Builder> {
    public static final ProtoAdapter<ShowAreasContext> ADAPTER = new ProtoAdapter_ShowAreasContext();
    private static final long serialVersionUID = 0;
    @WireField(adapter = "dji.flysafe.LimitArea#ADAPTER", label = WireField.Label.REPEATED, tag = 2)
    public final List<LimitArea> areas;

    public ShowAreasContext(List<LimitArea> areas2) {
        this(areas2, ByteString.EMPTY);
    }

    public ShowAreasContext(List<LimitArea> areas2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.areas = Internal.immutableCopyOf("areas", areas2);
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.areas = Internal.copyOf("areas", this.areas);
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ShowAreasContext)) {
            return false;
        }
        ShowAreasContext o = (ShowAreasContext) other;
        if (!unknownFields().equals(o.unknownFields()) || !this.areas.equals(o.areas)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int result2 = (unknownFields().hashCode() * 37) + this.areas.hashCode();
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!this.areas.isEmpty()) {
            builder.append(", areas=").append(this.areas);
        }
        return builder.replace(0, 2, "ShowAreasContext{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<ShowAreasContext, Builder> {
        public List<LimitArea> areas = Internal.newMutableList();

        public Builder areas(List<LimitArea> areas2) {
            Internal.checkElementsNotNull(areas2);
            this.areas = areas2;
            return this;
        }

        public ShowAreasContext build() {
            return new ShowAreasContext(this.areas, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_ShowAreasContext extends ProtoAdapter<ShowAreasContext> {
        ProtoAdapter_ShowAreasContext() {
            super(FieldEncoding.LENGTH_DELIMITED, ShowAreasContext.class);
        }

        public int encodedSize(ShowAreasContext value) {
            return LimitArea.ADAPTER.asRepeated().encodedSizeWithTag(2, value.areas) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, ShowAreasContext value) throws IOException {
            LimitArea.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.areas);
            writer.writeBytes(value.unknownFields());
        }

        public ShowAreasContext decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 2:
                            builder.areas.add(LimitArea.ADAPTER.decode(reader));
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

        public ShowAreasContext redact(ShowAreasContext value) {
            Builder builder = value.newBuilder();
            Internal.redactElements(builder.areas, LimitArea.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
