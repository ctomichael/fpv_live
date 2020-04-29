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

public final class LatLng extends Message<LatLng, Builder> {
    public static final ProtoAdapter<LatLng> ADAPTER = new ProtoAdapter_LatLng();
    public static final Double DEFAULT_LAT = Double.valueOf(0.0d);
    public static final Double DEFAULT_LNG = Double.valueOf(0.0d);
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 1)
    public final Double lat;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 2)
    public final Double lng;

    public LatLng(Double lat2, Double lng2) {
        this(lat2, lng2, ByteString.EMPTY);
    }

    public LatLng(Double lat2, Double lng2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.lat = lat2;
        this.lng = lng2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.lat = this.lat;
        builder.lng = this.lng;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LatLng)) {
            return false;
        }
        LatLng o = (LatLng) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.lat, o.lat) || !Internal.equals(this.lng, o.lng)) {
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
        if (this.lat != null) {
            i = this.lat.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.lng != null) {
            i2 = this.lng.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.lat != null) {
            builder.append(", lat=").append(this.lat);
        }
        if (this.lng != null) {
            builder.append(", lng=").append(this.lng);
        }
        return builder.replace(0, 2, "LatLng{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<LatLng, Builder> {
        public Double lat;
        public Double lng;

        public Builder lat(Double lat2) {
            this.lat = lat2;
            return this;
        }

        public Builder lng(Double lng2) {
            this.lng = lng2;
            return this;
        }

        public LatLng build() {
            return new LatLng(this.lat, this.lng, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_LatLng extends ProtoAdapter<LatLng> {
        ProtoAdapter_LatLng() {
            super(FieldEncoding.LENGTH_DELIMITED, LatLng.class);
        }

        public int encodedSize(LatLng value) {
            int i = 0;
            int encodedSizeWithTag = value.lat != null ? ProtoAdapter.DOUBLE.encodedSizeWithTag(1, value.lat) : 0;
            if (value.lng != null) {
                i = ProtoAdapter.DOUBLE.encodedSizeWithTag(2, value.lng);
            }
            return encodedSizeWithTag + i + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, LatLng value) throws IOException {
            if (value.lat != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 1, value.lat);
            }
            if (value.lng != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 2, value.lng);
            }
            writer.writeBytes(value.unknownFields());
        }

        public LatLng decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.lat(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 2:
                            builder.lng(ProtoAdapter.DOUBLE.decode(reader));
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

        public LatLng redact(LatLng value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
