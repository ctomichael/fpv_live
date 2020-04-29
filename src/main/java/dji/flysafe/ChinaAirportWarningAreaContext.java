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

public final class ChinaAirportWarningAreaContext extends Message<ChinaAirportWarningAreaContext, Builder> {
    public static final ProtoAdapter<ChinaAirportWarningAreaContext> ADAPTER = new ProtoAdapter_ChinaAirportWarningAreaContext();
    public static final Boolean DEFAULT_ISINCHINAAIRPORTWARNINGAREA = false;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 2)
    public final Boolean isInChinaAirportWarningArea;

    public ChinaAirportWarningAreaContext(Boolean isInChinaAirportWarningArea2) {
        this(isInChinaAirportWarningArea2, ByteString.EMPTY);
    }

    public ChinaAirportWarningAreaContext(Boolean isInChinaAirportWarningArea2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.isInChinaAirportWarningArea = isInChinaAirportWarningArea2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.isInChinaAirportWarningArea = this.isInChinaAirportWarningArea;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ChinaAirportWarningAreaContext)) {
            return false;
        }
        ChinaAirportWarningAreaContext o = (ChinaAirportWarningAreaContext) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.isInChinaAirportWarningArea, o.isInChinaAirportWarningArea)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int result2 = (unknownFields().hashCode() * 37) + (this.isInChinaAirportWarningArea != null ? this.isInChinaAirportWarningArea.hashCode() : 0);
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.isInChinaAirportWarningArea != null) {
            builder.append(", isInChinaAirportWarningArea=").append(this.isInChinaAirportWarningArea);
        }
        return builder.replace(0, 2, "ChinaAirportWarningAreaContext{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<ChinaAirportWarningAreaContext, Builder> {
        public Boolean isInChinaAirportWarningArea;

        public Builder isInChinaAirportWarningArea(Boolean isInChinaAirportWarningArea2) {
            this.isInChinaAirportWarningArea = isInChinaAirportWarningArea2;
            return this;
        }

        public ChinaAirportWarningAreaContext build() {
            return new ChinaAirportWarningAreaContext(this.isInChinaAirportWarningArea, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_ChinaAirportWarningAreaContext extends ProtoAdapter<ChinaAirportWarningAreaContext> {
        ProtoAdapter_ChinaAirportWarningAreaContext() {
            super(FieldEncoding.LENGTH_DELIMITED, ChinaAirportWarningAreaContext.class);
        }

        public int encodedSize(ChinaAirportWarningAreaContext value) {
            return (value.isInChinaAirportWarningArea != null ? ProtoAdapter.BOOL.encodedSizeWithTag(2, value.isInChinaAirportWarningArea) : 0) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, ChinaAirportWarningAreaContext value) throws IOException {
            if (value.isInChinaAirportWarningArea != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 2, value.isInChinaAirportWarningArea);
            }
            writer.writeBytes(value.unknownFields());
        }

        public ChinaAirportWarningAreaContext decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 2:
                            builder.isInChinaAirportWarningArea(ProtoAdapter.BOOL.decode(reader));
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

        public ChinaAirportWarningAreaContext redact(ChinaAirportWarningAreaContext value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
