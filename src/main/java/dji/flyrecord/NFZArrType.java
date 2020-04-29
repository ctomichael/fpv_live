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

public final class NFZArrType extends Message<NFZArrType, Builder> {
    public static final ProtoAdapter<NFZArrType> ADAPTER = new ProtoAdapter_NFZArrType();
    public static final String DEFAULT_TYPE = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "dji.flyrecord.LatLng#ADAPTER", label = WireField.Label.REPEATED, tag = 2)
    public final List<LatLng> latLngs;
    @WireField(adapter = "dji.flyrecord.Options#ADAPTER", tag = 3)
    public final Options options;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String type;

    public NFZArrType(String type2, List<LatLng> latLngs2, Options options2) {
        this(type2, latLngs2, options2, ByteString.EMPTY);
    }

    public NFZArrType(String type2, List<LatLng> latLngs2, Options options2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.type = type2;
        this.latLngs = Internal.immutableCopyOf("latLngs", latLngs2);
        this.options = options2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.type = this.type;
        builder.latLngs = Internal.copyOf("latLngs", this.latLngs);
        builder.options = this.options;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof NFZArrType)) {
            return false;
        }
        NFZArrType o = (NFZArrType) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.type, o.type) || !this.latLngs.equals(o.latLngs) || !Internal.equals(this.options, o.options)) {
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
        int hashCode = ((((unknownFields().hashCode() * 37) + (this.type != null ? this.type.hashCode() : 0)) * 37) + this.latLngs.hashCode()) * 37;
        if (this.options != null) {
            i = this.options.hashCode();
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
        if (!this.latLngs.isEmpty()) {
            builder.append(", latLngs=").append(this.latLngs);
        }
        if (this.options != null) {
            builder.append(", options=").append(this.options);
        }
        return builder.replace(0, 2, "NFZArrType{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<NFZArrType, Builder> {
        public List<LatLng> latLngs = Internal.newMutableList();
        public Options options;
        public String type;

        public Builder type(String type2) {
            this.type = type2;
            return this;
        }

        public Builder latLngs(List<LatLng> latLngs2) {
            Internal.checkElementsNotNull(latLngs2);
            this.latLngs = latLngs2;
            return this;
        }

        public Builder options(Options options2) {
            this.options = options2;
            return this;
        }

        public NFZArrType build() {
            return new NFZArrType(this.type, this.latLngs, this.options, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_NFZArrType extends ProtoAdapter<NFZArrType> {
        ProtoAdapter_NFZArrType() {
            super(FieldEncoding.LENGTH_DELIMITED, NFZArrType.class);
        }

        public int encodedSize(NFZArrType value) {
            int i = 0;
            int encodedSizeWithTag = (value.type != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.type) : 0) + LatLng.ADAPTER.asRepeated().encodedSizeWithTag(2, value.latLngs);
            if (value.options != null) {
                i = Options.ADAPTER.encodedSizeWithTag(3, value.options);
            }
            return encodedSizeWithTag + i + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, NFZArrType value) throws IOException {
            if (value.type != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.type);
            }
            LatLng.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.latLngs);
            if (value.options != null) {
                Options.ADAPTER.encodeWithTag(writer, 3, value.options);
            }
            writer.writeBytes(value.unknownFields());
        }

        public NFZArrType decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.type(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.latLngs.add(LatLng.ADAPTER.decode(reader));
                            break;
                        case 3:
                            builder.options(Options.ADAPTER.decode(reader));
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

        public NFZArrType redact(NFZArrType value) {
            Builder builder = value.newBuilder();
            Internal.redactElements(builder.latLngs, LatLng.ADAPTER);
            if (builder.options != null) {
                builder.options = Options.ADAPTER.redact(builder.options);
            }
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
