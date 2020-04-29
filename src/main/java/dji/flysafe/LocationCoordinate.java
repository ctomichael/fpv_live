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

public final class LocationCoordinate extends Message<LocationCoordinate, Builder> {
    public static final ProtoAdapter<LocationCoordinate> ADAPTER = new ProtoAdapter_LocationCoordinate();
    public static final Double DEFAULT_LATITUDE = Double.valueOf(0.0d);
    public static final Double DEFAULT_LONGITUDE = Double.valueOf(0.0d);
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 1)
    public final Double latitude;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 2)
    public final Double longitude;

    public LocationCoordinate(Double latitude2, Double longitude2) {
        this(latitude2, longitude2, ByteString.EMPTY);
    }

    public LocationCoordinate(Double latitude2, Double longitude2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.latitude = latitude2;
        this.longitude = longitude2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.latitude = this.latitude;
        builder.longitude = this.longitude;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LocationCoordinate)) {
            return false;
        }
        LocationCoordinate o = (LocationCoordinate) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.latitude, o.latitude) || !Internal.equals(this.longitude, o.longitude)) {
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
        if (this.latitude != null) {
            i = this.latitude.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.longitude != null) {
            i2 = this.longitude.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.latitude != null) {
            builder.append(", latitude=").append(this.latitude);
        }
        if (this.longitude != null) {
            builder.append(", longitude=").append(this.longitude);
        }
        return builder.replace(0, 2, "LocationCoordinate{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<LocationCoordinate, Builder> {
        public Double latitude;
        public Double longitude;

        public Builder latitude(Double latitude2) {
            this.latitude = latitude2;
            return this;
        }

        public Builder longitude(Double longitude2) {
            this.longitude = longitude2;
            return this;
        }

        public LocationCoordinate build() {
            return new LocationCoordinate(this.latitude, this.longitude, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_LocationCoordinate extends ProtoAdapter<LocationCoordinate> {
        ProtoAdapter_LocationCoordinate() {
            super(FieldEncoding.LENGTH_DELIMITED, LocationCoordinate.class);
        }

        public int encodedSize(LocationCoordinate value) {
            int i = 0;
            int encodedSizeWithTag = value.latitude != null ? ProtoAdapter.DOUBLE.encodedSizeWithTag(1, value.latitude) : 0;
            if (value.longitude != null) {
                i = ProtoAdapter.DOUBLE.encodedSizeWithTag(2, value.longitude);
            }
            return encodedSizeWithTag + i + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, LocationCoordinate value) throws IOException {
            if (value.latitude != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 1, value.latitude);
            }
            if (value.longitude != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 2, value.longitude);
            }
            writer.writeBytes(value.unknownFields());
        }

        public LocationCoordinate decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.latitude(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 2:
                            builder.longitude(ProtoAdapter.DOUBLE.decode(reader));
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

        public LocationCoordinate redact(LocationCoordinate value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
