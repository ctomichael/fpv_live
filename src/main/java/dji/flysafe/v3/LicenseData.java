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

public final class LicenseData extends Message<LicenseData, Builder> {
    public static final ProtoAdapter<LicenseData> ADAPTER = new ProtoAdapter_LicenseData();
    private static final long serialVersionUID = 0;
    @WireField(adapter = "dji.flysafe.v3.LicenseDataArea#ADAPTER", tag = 1)
    public final LicenseDataArea area;
    @WireField(adapter = "dji.flysafe.v3.LicenseDataCircle#ADAPTER", tag = 2)
    public final LicenseDataCircle circle;
    @WireField(adapter = "dji.flysafe.v3.LicenseDataCountry#ADAPTER", tag = 3)
    public final LicenseDataCountry country;
    @WireField(adapter = "dji.flysafe.v3.LicenseDataHeight#ADAPTER", tag = 4)
    public final LicenseDataHeight height;
    @WireField(adapter = "dji.flysafe.v3.LicenseDataPolygon#ADAPTER", tag = 5)
    public final LicenseDataPolygon polygon;

    public LicenseData(LicenseDataArea area2, LicenseDataCircle circle2, LicenseDataCountry country2, LicenseDataHeight height2, LicenseDataPolygon polygon2) {
        this(area2, circle2, country2, height2, polygon2, ByteString.EMPTY);
    }

    public LicenseData(LicenseDataArea area2, LicenseDataCircle circle2, LicenseDataCountry country2, LicenseDataHeight height2, LicenseDataPolygon polygon2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        if (Internal.countNonNull(area2, circle2, country2, height2, polygon2) > 1) {
            throw new IllegalArgumentException("at most one of area, circle, country, height, polygon may be non-null");
        }
        this.area = area2;
        this.circle = circle2;
        this.country = country2;
        this.height = height2;
        this.polygon = polygon2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.area = this.area;
        builder.circle = this.circle;
        builder.country = this.country;
        builder.height = this.height;
        builder.polygon = this.polygon;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LicenseData)) {
            return false;
        }
        LicenseData o = (LicenseData) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.area, o.area) || !Internal.equals(this.circle, o.circle) || !Internal.equals(this.country, o.country) || !Internal.equals(this.height, o.height) || !Internal.equals(this.polygon, o.polygon)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.area != null ? this.area.hashCode() : 0)) * 37;
        if (this.circle != null) {
            i = this.circle.hashCode();
        } else {
            i = 0;
        }
        int i5 = (hashCode + i) * 37;
        if (this.country != null) {
            i2 = this.country.hashCode();
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 37;
        if (this.height != null) {
            i3 = this.height.hashCode();
        } else {
            i3 = 0;
        }
        int i7 = (i6 + i3) * 37;
        if (this.polygon != null) {
            i4 = this.polygon.hashCode();
        }
        int result2 = i7 + i4;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.area != null) {
            builder.append(", area=").append(this.area);
        }
        if (this.circle != null) {
            builder.append(", circle=").append(this.circle);
        }
        if (this.country != null) {
            builder.append(", country=").append(this.country);
        }
        if (this.height != null) {
            builder.append(", height=").append(this.height);
        }
        if (this.polygon != null) {
            builder.append(", polygon=").append(this.polygon);
        }
        return builder.replace(0, 2, "LicenseData{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<LicenseData, Builder> {
        public LicenseDataArea area;
        public LicenseDataCircle circle;
        public LicenseDataCountry country;
        public LicenseDataHeight height;
        public LicenseDataPolygon polygon;

        public Builder area(LicenseDataArea area2) {
            this.area = area2;
            this.circle = null;
            this.country = null;
            this.height = null;
            this.polygon = null;
            return this;
        }

        public Builder circle(LicenseDataCircle circle2) {
            this.circle = circle2;
            this.area = null;
            this.country = null;
            this.height = null;
            this.polygon = null;
            return this;
        }

        public Builder country(LicenseDataCountry country2) {
            this.country = country2;
            this.area = null;
            this.circle = null;
            this.height = null;
            this.polygon = null;
            return this;
        }

        public Builder height(LicenseDataHeight height2) {
            this.height = height2;
            this.area = null;
            this.circle = null;
            this.country = null;
            this.polygon = null;
            return this;
        }

        public Builder polygon(LicenseDataPolygon polygon2) {
            this.polygon = polygon2;
            this.area = null;
            this.circle = null;
            this.country = null;
            this.height = null;
            return this;
        }

        public LicenseData build() {
            return new LicenseData(this.area, this.circle, this.country, this.height, this.polygon, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_LicenseData extends ProtoAdapter<LicenseData> {
        ProtoAdapter_LicenseData() {
            super(FieldEncoding.LENGTH_DELIMITED, LicenseData.class);
        }

        public int encodedSize(LicenseData value) {
            int i;
            int i2;
            int i3;
            int i4 = 0;
            int encodedSizeWithTag = value.area != null ? LicenseDataArea.ADAPTER.encodedSizeWithTag(1, value.area) : 0;
            if (value.circle != null) {
                i = LicenseDataCircle.ADAPTER.encodedSizeWithTag(2, value.circle);
            } else {
                i = 0;
            }
            int i5 = i + encodedSizeWithTag;
            if (value.country != null) {
                i2 = LicenseDataCountry.ADAPTER.encodedSizeWithTag(3, value.country);
            } else {
                i2 = 0;
            }
            int i6 = i5 + i2;
            if (value.height != null) {
                i3 = LicenseDataHeight.ADAPTER.encodedSizeWithTag(4, value.height);
            } else {
                i3 = 0;
            }
            int i7 = i3 + i6;
            if (value.polygon != null) {
                i4 = LicenseDataPolygon.ADAPTER.encodedSizeWithTag(5, value.polygon);
            }
            return i7 + i4 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, LicenseData value) throws IOException {
            if (value.area != null) {
                LicenseDataArea.ADAPTER.encodeWithTag(writer, 1, value.area);
            }
            if (value.circle != null) {
                LicenseDataCircle.ADAPTER.encodeWithTag(writer, 2, value.circle);
            }
            if (value.country != null) {
                LicenseDataCountry.ADAPTER.encodeWithTag(writer, 3, value.country);
            }
            if (value.height != null) {
                LicenseDataHeight.ADAPTER.encodeWithTag(writer, 4, value.height);
            }
            if (value.polygon != null) {
                LicenseDataPolygon.ADAPTER.encodeWithTag(writer, 5, value.polygon);
            }
            writer.writeBytes(value.unknownFields());
        }

        public LicenseData decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.area(LicenseDataArea.ADAPTER.decode(reader));
                            break;
                        case 2:
                            builder.circle(LicenseDataCircle.ADAPTER.decode(reader));
                            break;
                        case 3:
                            builder.country(LicenseDataCountry.ADAPTER.decode(reader));
                            break;
                        case 4:
                            builder.height(LicenseDataHeight.ADAPTER.decode(reader));
                            break;
                        case 5:
                            builder.polygon(LicenseDataPolygon.ADAPTER.decode(reader));
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

        public LicenseData redact(LicenseData value) {
            Builder builder = value.newBuilder();
            if (builder.area != null) {
                builder.area = LicenseDataArea.ADAPTER.redact(builder.area);
            }
            if (builder.circle != null) {
                builder.circle = LicenseDataCircle.ADAPTER.redact(builder.circle);
            }
            if (builder.country != null) {
                builder.country = LicenseDataCountry.ADAPTER.redact(builder.country);
            }
            if (builder.height != null) {
                builder.height = LicenseDataHeight.ADAPTER.redact(builder.height);
            }
            if (builder.polygon != null) {
                builder.polygon = LicenseDataPolygon.ADAPTER.redact(builder.polygon);
            }
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
