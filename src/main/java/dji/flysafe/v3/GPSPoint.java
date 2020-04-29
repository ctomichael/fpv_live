package dji.flysafe.v3;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import okio.ByteString;

public final class GPSPoint extends Message<GPSPoint, Builder> {
    public static final ProtoAdapter<GPSPoint> ADAPTER = new ProtoAdapter_GPSPoint();
    public static final Float DEFAULT_LAT = Float.valueOf(0.0f);
    public static final Float DEFAULT_LNG = Float.valueOf(0.0f);
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#FLOAT", tag = 1)
    public final Float lat;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#FLOAT", tag = 2)
    public final Float lng;

    public GPSPoint(Float lat2, Float lng2) {
        this(lat2, lng2, ByteString.EMPTY);
    }

    public GPSPoint(Float lat2, Float lng2, ByteString unknownFields) {
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
        if (!(other instanceof GPSPoint)) {
            return false;
        }
        GPSPoint o = (GPSPoint) other;
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
        return builder.replace(0, 2, "GPSPoint{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<GPSPoint, Builder> {
        public Float lat;
        public Float lng;

        public Builder lat(Float lat2) {
            this.lat = lat2;
            return this;
        }

        public Builder lng(Float lng2) {
            this.lng = lng2;
            return this;
        }

        public GPSPoint build() {
            return new GPSPoint(this.lat, this.lng, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_GPSPoint extends ProtoAdapter<GPSPoint> {
        ProtoAdapter_GPSPoint() {
            super(FieldEncoding.LENGTH_DELIMITED, GPSPoint.class);
        }

        public int encodedSize(GPSPoint value) {
            int i = 0;
            int encodedSizeWithTag = value.lat != null ? ProtoAdapter.FLOAT.encodedSizeWithTag(1, value.lat) : 0;
            if (value.lng != null) {
                i = ProtoAdapter.FLOAT.encodedSizeWithTag(2, value.lng);
            }
            return encodedSizeWithTag + i + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, GPSPoint value) throws IOException {
            if (value.lat != null) {
                ProtoAdapter.FLOAT.encodeWithTag(writer, 1, value.lat);
            }
            if (value.lng != null) {
                ProtoAdapter.FLOAT.encodeWithTag(writer, 2, value.lng);
            }
            writer.writeBytes(value.unknownFields());
        }

        public GPSPoint decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.lat(ProtoAdapter.FLOAT.decode(reader));
                            break;
                        case 2:
                            builder.lng(ProtoAdapter.FLOAT.decode(reader));
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

        public GPSPoint redact(GPSPoint value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
