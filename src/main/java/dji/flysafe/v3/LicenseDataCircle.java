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

public final class LicenseDataCircle extends Message<LicenseDataCircle, Builder> {
    public static final ProtoAdapter<LicenseDataCircle> ADAPTER = new ProtoAdapter_LicenseDataCircle();
    public static final Integer DEFAULT_HEIGHT_LIMIT = 0;
    public static final Integer DEFAULT_RADIUS = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 3)
    public final Integer height_limit;
    @WireField(adapter = "dji.flysafe.v3.GPSPoint#ADAPTER", tag = 1)
    public final GPSPoint point;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 2)
    public final Integer radius;

    public LicenseDataCircle(GPSPoint point2, Integer radius2, Integer height_limit2) {
        this(point2, radius2, height_limit2, ByteString.EMPTY);
    }

    public LicenseDataCircle(GPSPoint point2, Integer radius2, Integer height_limit2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.point = point2;
        this.radius = radius2;
        this.height_limit = height_limit2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.point = this.point;
        builder.radius = this.radius;
        builder.height_limit = this.height_limit;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LicenseDataCircle)) {
            return false;
        }
        LicenseDataCircle o = (LicenseDataCircle) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.point, o.point) || !Internal.equals(this.radius, o.radius) || !Internal.equals(this.height_limit, o.height_limit)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.point != null ? this.point.hashCode() : 0)) * 37;
        if (this.radius != null) {
            i = this.radius.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.height_limit != null) {
            i2 = this.height_limit.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.point != null) {
            builder.append(", point=").append(this.point);
        }
        if (this.radius != null) {
            builder.append(", radius=").append(this.radius);
        }
        if (this.height_limit != null) {
            builder.append(", height_limit=").append(this.height_limit);
        }
        return builder.replace(0, 2, "LicenseDataCircle{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<LicenseDataCircle, Builder> {
        public Integer height_limit;
        public GPSPoint point;
        public Integer radius;

        public Builder point(GPSPoint point2) {
            this.point = point2;
            return this;
        }

        public Builder radius(Integer radius2) {
            this.radius = radius2;
            return this;
        }

        public Builder height_limit(Integer height_limit2) {
            this.height_limit = height_limit2;
            return this;
        }

        public LicenseDataCircle build() {
            return new LicenseDataCircle(this.point, this.radius, this.height_limit, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_LicenseDataCircle extends ProtoAdapter<LicenseDataCircle> {
        ProtoAdapter_LicenseDataCircle() {
            super(FieldEncoding.LENGTH_DELIMITED, LicenseDataCircle.class);
        }

        public int encodedSize(LicenseDataCircle value) {
            int i;
            int i2 = 0;
            int encodedSizeWithTag = value.point != null ? GPSPoint.ADAPTER.encodedSizeWithTag(1, value.point) : 0;
            if (value.radius != null) {
                i = ProtoAdapter.UINT32.encodedSizeWithTag(2, value.radius);
            } else {
                i = 0;
            }
            int i3 = encodedSizeWithTag + i;
            if (value.height_limit != null) {
                i2 = ProtoAdapter.UINT32.encodedSizeWithTag(3, value.height_limit);
            }
            return i3 + i2 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, LicenseDataCircle value) throws IOException {
            if (value.point != null) {
                GPSPoint.ADAPTER.encodeWithTag(writer, 1, value.point);
            }
            if (value.radius != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.radius);
            }
            if (value.height_limit != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.height_limit);
            }
            writer.writeBytes(value.unknownFields());
        }

        public LicenseDataCircle decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.point(GPSPoint.ADAPTER.decode(reader));
                            break;
                        case 2:
                            builder.radius(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 3:
                            builder.height_limit(ProtoAdapter.UINT32.decode(reader));
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

        public LicenseDataCircle redact(LicenseDataCircle value) {
            Builder builder = value.newBuilder();
            if (builder.point != null) {
                builder.point = GPSPoint.ADAPTER.redact(builder.point);
            }
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
