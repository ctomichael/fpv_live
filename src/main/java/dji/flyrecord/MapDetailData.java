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

public final class MapDetailData extends Message<MapDetailData, Builder> {
    public static final ProtoAdapter<MapDetailData> ADAPTER = new ProtoAdapter_MapDetailData();
    public static final Integer DEFAULT_CODE = 0;
    public static final Double DEFAULT_DATE = Double.valueOf(0.0d);
    public static final String DEFAULT_DEVICENAME = "";
    public static final String DEFAULT_DEVICETYPE = "";
    public static final Double DEFAULT_DURATION = Double.valueOf(0.0d);
    public static final String DEFAULT_LOCATION = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 8)
    public final Integer code;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 4)
    public final Double date;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String deviceName;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String deviceType;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 5)
    public final Double duration;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 3)
    public final String location;
    @WireField(adapter = "dji.flyrecord.NFZArrType#ADAPTER", label = WireField.Label.REPEATED, tag = 7)
    public final List<NFZArrType> nfzArr;
    @WireField(adapter = "dji.flyrecord.PositionType#ADAPTER", label = WireField.Label.REPEATED, tag = 6)
    public final List<PositionType> points;

    public MapDetailData(String deviceName2, String deviceType2, String location2, Double date2, Double duration2, List<PositionType> points2, List<NFZArrType> nfzArr2, Integer code2) {
        this(deviceName2, deviceType2, location2, date2, duration2, points2, nfzArr2, code2, ByteString.EMPTY);
    }

    public MapDetailData(String deviceName2, String deviceType2, String location2, Double date2, Double duration2, List<PositionType> points2, List<NFZArrType> nfzArr2, Integer code2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.deviceName = deviceName2;
        this.deviceType = deviceType2;
        this.location = location2;
        this.date = date2;
        this.duration = duration2;
        this.points = Internal.immutableCopyOf("points", points2);
        this.nfzArr = Internal.immutableCopyOf("nfzArr", nfzArr2);
        this.code = code2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.deviceName = this.deviceName;
        builder.deviceType = this.deviceType;
        builder.location = this.location;
        builder.date = this.date;
        builder.duration = this.duration;
        builder.points = Internal.copyOf("points", this.points);
        builder.nfzArr = Internal.copyOf("nfzArr", this.nfzArr);
        builder.code = this.code;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MapDetailData)) {
            return false;
        }
        MapDetailData o = (MapDetailData) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.deviceName, o.deviceName) || !Internal.equals(this.deviceType, o.deviceType) || !Internal.equals(this.location, o.location) || !Internal.equals(this.date, o.date) || !Internal.equals(this.duration, o.duration) || !this.points.equals(o.points) || !this.nfzArr.equals(o.nfzArr) || !Internal.equals(this.code, o.code)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.deviceName != null ? this.deviceName.hashCode() : 0)) * 37;
        if (this.deviceType != null) {
            i = this.deviceType.hashCode();
        } else {
            i = 0;
        }
        int i6 = (hashCode + i) * 37;
        if (this.location != null) {
            i2 = this.location.hashCode();
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 37;
        if (this.date != null) {
            i3 = this.date.hashCode();
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 37;
        if (this.duration != null) {
            i4 = this.duration.hashCode();
        } else {
            i4 = 0;
        }
        int hashCode2 = (((((i8 + i4) * 37) + this.points.hashCode()) * 37) + this.nfzArr.hashCode()) * 37;
        if (this.code != null) {
            i5 = this.code.hashCode();
        }
        int result2 = hashCode2 + i5;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.deviceName != null) {
            builder.append(", deviceName=").append(this.deviceName);
        }
        if (this.deviceType != null) {
            builder.append(", deviceType=").append(this.deviceType);
        }
        if (this.location != null) {
            builder.append(", location=").append(this.location);
        }
        if (this.date != null) {
            builder.append(", date=").append(this.date);
        }
        if (this.duration != null) {
            builder.append(", duration=").append(this.duration);
        }
        if (!this.points.isEmpty()) {
            builder.append(", points=").append(this.points);
        }
        if (!this.nfzArr.isEmpty()) {
            builder.append(", nfzArr=").append(this.nfzArr);
        }
        if (this.code != null) {
            builder.append(", code=").append(this.code);
        }
        return builder.replace(0, 2, "MapDetailData{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<MapDetailData, Builder> {
        public Integer code;
        public Double date;
        public String deviceName;
        public String deviceType;
        public Double duration;
        public String location;
        public List<NFZArrType> nfzArr = Internal.newMutableList();
        public List<PositionType> points = Internal.newMutableList();

        public Builder deviceName(String deviceName2) {
            this.deviceName = deviceName2;
            return this;
        }

        public Builder deviceType(String deviceType2) {
            this.deviceType = deviceType2;
            return this;
        }

        public Builder location(String location2) {
            this.location = location2;
            return this;
        }

        public Builder date(Double date2) {
            this.date = date2;
            return this;
        }

        public Builder duration(Double duration2) {
            this.duration = duration2;
            return this;
        }

        public Builder points(List<PositionType> points2) {
            Internal.checkElementsNotNull(points2);
            this.points = points2;
            return this;
        }

        public Builder nfzArr(List<NFZArrType> nfzArr2) {
            Internal.checkElementsNotNull(nfzArr2);
            this.nfzArr = nfzArr2;
            return this;
        }

        public Builder code(Integer code2) {
            this.code = code2;
            return this;
        }

        public MapDetailData build() {
            return new MapDetailData(this.deviceName, this.deviceType, this.location, this.date, this.duration, this.points, this.nfzArr, this.code, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_MapDetailData extends ProtoAdapter<MapDetailData> {
        ProtoAdapter_MapDetailData() {
            super(FieldEncoding.LENGTH_DELIMITED, MapDetailData.class);
        }

        public int encodedSize(MapDetailData value) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5 = 0;
            int encodedSizeWithTag = value.deviceName != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.deviceName) : 0;
            if (value.deviceType != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.deviceType);
            } else {
                i = 0;
            }
            int i6 = i + encodedSizeWithTag;
            if (value.location != null) {
                i2 = ProtoAdapter.STRING.encodedSizeWithTag(3, value.location);
            } else {
                i2 = 0;
            }
            int i7 = i6 + i2;
            if (value.date != null) {
                i3 = ProtoAdapter.DOUBLE.encodedSizeWithTag(4, value.date);
            } else {
                i3 = 0;
            }
            int i8 = i7 + i3;
            if (value.duration != null) {
                i4 = ProtoAdapter.DOUBLE.encodedSizeWithTag(5, value.duration);
            } else {
                i4 = 0;
            }
            int encodedSizeWithTag2 = i4 + i8 + PositionType.ADAPTER.asRepeated().encodedSizeWithTag(6, value.points) + NFZArrType.ADAPTER.asRepeated().encodedSizeWithTag(7, value.nfzArr);
            if (value.code != null) {
                i5 = ProtoAdapter.INT32.encodedSizeWithTag(8, value.code);
            }
            return encodedSizeWithTag2 + i5 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, MapDetailData value) throws IOException {
            if (value.deviceName != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.deviceName);
            }
            if (value.deviceType != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.deviceType);
            }
            if (value.location != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 3, value.location);
            }
            if (value.date != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 4, value.date);
            }
            if (value.duration != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 5, value.duration);
            }
            PositionType.ADAPTER.asRepeated().encodeWithTag(writer, 6, value.points);
            NFZArrType.ADAPTER.asRepeated().encodeWithTag(writer, 7, value.nfzArr);
            if (value.code != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 8, value.code);
            }
            writer.writeBytes(value.unknownFields());
        }

        public MapDetailData decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.deviceName(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.deviceType(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            builder.location(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 4:
                            builder.date(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 5:
                            builder.duration(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 6:
                            builder.points.add(PositionType.ADAPTER.decode(reader));
                            break;
                        case 7:
                            builder.nfzArr.add(NFZArrType.ADAPTER.decode(reader));
                            break;
                        case 8:
                            builder.code(ProtoAdapter.INT32.decode(reader));
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

        public MapDetailData redact(MapDetailData value) {
            Builder builder = value.newBuilder();
            Internal.redactElements(builder.points, PositionType.ADAPTER);
            Internal.redactElements(builder.nfzArr, NFZArrType.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
