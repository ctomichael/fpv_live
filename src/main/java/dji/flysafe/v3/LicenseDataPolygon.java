package dji.flysafe.v3;

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

public final class LicenseDataPolygon extends Message<LicenseDataPolygon, Builder> {
    public static final ProtoAdapter<LicenseDataPolygon> ADAPTER = new ProtoAdapter_LicenseDataPolygon();
    public static final Integer DEFAULT_HEIGHT_LIMIT = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 2)
    public final Integer height_limit;
    @WireField(adapter = "dji.flysafe.v3.GPSPoint#ADAPTER", label = WireField.Label.REPEATED, tag = 1)
    public final List<GPSPoint> points;

    public LicenseDataPolygon(List<GPSPoint> points2, Integer height_limit2) {
        this(points2, height_limit2, ByteString.EMPTY);
    }

    public LicenseDataPolygon(List<GPSPoint> points2, Integer height_limit2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.points = Internal.immutableCopyOf("points", points2);
        this.height_limit = height_limit2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.points = Internal.copyOf("points", this.points);
        builder.height_limit = this.height_limit;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LicenseDataPolygon)) {
            return false;
        }
        LicenseDataPolygon o = (LicenseDataPolygon) other;
        if (!unknownFields().equals(o.unknownFields()) || !this.points.equals(o.points) || !Internal.equals(this.height_limit, o.height_limit)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int result2 = (((unknownFields().hashCode() * 37) + this.points.hashCode()) * 37) + (this.height_limit != null ? this.height_limit.hashCode() : 0);
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!this.points.isEmpty()) {
            builder.append(", points=").append(this.points);
        }
        if (this.height_limit != null) {
            builder.append(", height_limit=").append(this.height_limit);
        }
        return builder.replace(0, 2, "LicenseDataPolygon{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<LicenseDataPolygon, Builder> {
        public Integer height_limit;
        public List<GPSPoint> points = Internal.newMutableList();

        public Builder points(List<GPSPoint> points2) {
            Internal.checkElementsNotNull(points2);
            this.points = points2;
            return this;
        }

        public Builder height_limit(Integer height_limit2) {
            this.height_limit = height_limit2;
            return this;
        }

        public LicenseDataPolygon build() {
            return new LicenseDataPolygon(this.points, this.height_limit, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_LicenseDataPolygon extends ProtoAdapter<LicenseDataPolygon> {
        ProtoAdapter_LicenseDataPolygon() {
            super(FieldEncoding.LENGTH_DELIMITED, LicenseDataPolygon.class);
        }

        public int encodedSize(LicenseDataPolygon value) {
            return (value.height_limit != null ? ProtoAdapter.UINT32.encodedSizeWithTag(2, value.height_limit) : 0) + GPSPoint.ADAPTER.asRepeated().encodedSizeWithTag(1, value.points) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, LicenseDataPolygon value) throws IOException {
            GPSPoint.ADAPTER.asRepeated().encodeWithTag(writer, 1, value.points);
            if (value.height_limit != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.height_limit);
            }
            writer.writeBytes(value.unknownFields());
        }

        public LicenseDataPolygon decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.points.add(GPSPoint.ADAPTER.decode(reader));
                            break;
                        case 2:
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

        public LicenseDataPolygon redact(LicenseDataPolygon value) {
            Builder builder = value.newBuilder();
            Internal.redactElements(builder.points, GPSPoint.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
