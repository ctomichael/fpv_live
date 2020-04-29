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

public final class DevicePreviwInfo extends Message<DevicePreviwInfo, Builder> {
    public static final ProtoAdapter<DevicePreviwInfo> ADAPTER = new ProtoAdapter_DevicePreviwInfo();
    public static final Double DEFAULT_ALTITUDE = Double.valueOf(0.0d);
    public static final Double DEFAULT_CITIES = Double.valueOf(0.0d);
    public static final Integer DEFAULT_COUNTRIES = 0;
    public static final Double DEFAULT_DURATION = Double.valueOf(0.0d);
    public static final Integer DEFAULT_FLYTIMES = 0;
    public static final Double DEFAULT_LEVEL = Double.valueOf(0.0d);
    public static final Double DEFAULT_MAXMILEAGE = Double.valueOf(0.0d);
    public static final Double DEFAULT_MAXSPEED = Double.valueOf(0.0d);
    public static final Double DEFAULT_MILEAGE = Double.valueOf(0.0d);
    public static final Double DEFAULT_RANK = Double.valueOf(0.0d);
    public static final Double DEFAULT_RECORDDURATION = Double.valueOf(0.0d);
    public static final Integer DEFAULT_SHOTS = 0;
    public static final String DEFAULT_TITLE = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 8)
    public final Double Altitude;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 10)
    public final Double cities;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 9)
    public final Integer countries;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 5)
    public final Double duration;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 1)
    public final Integer flyTimes;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 12)
    public final Double level;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 3)
    public final Double maxMileage;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 2)
    public final Double maxSpeed;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 4)
    public final Double mileage;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 11)
    public final Double rank;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 6)
    public final Double recordDuration;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 7)
    public final Integer shots;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 13)
    public final String title;

    public DevicePreviwInfo(Integer flyTimes2, Double maxSpeed2, Double maxMileage2, Double mileage2, Double duration2, Double recordDuration2, Integer shots2, Double Altitude2, Integer countries2, Double cities2, Double rank2, Double level2, String title2) {
        this(flyTimes2, maxSpeed2, maxMileage2, mileage2, duration2, recordDuration2, shots2, Altitude2, countries2, cities2, rank2, level2, title2, ByteString.EMPTY);
    }

    public DevicePreviwInfo(Integer flyTimes2, Double maxSpeed2, Double maxMileage2, Double mileage2, Double duration2, Double recordDuration2, Integer shots2, Double Altitude2, Integer countries2, Double cities2, Double rank2, Double level2, String title2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.flyTimes = flyTimes2;
        this.maxSpeed = maxSpeed2;
        this.maxMileage = maxMileage2;
        this.mileage = mileage2;
        this.duration = duration2;
        this.recordDuration = recordDuration2;
        this.shots = shots2;
        this.Altitude = Altitude2;
        this.countries = countries2;
        this.cities = cities2;
        this.rank = rank2;
        this.level = level2;
        this.title = title2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.flyTimes = this.flyTimes;
        builder.maxSpeed = this.maxSpeed;
        builder.maxMileage = this.maxMileage;
        builder.mileage = this.mileage;
        builder.duration = this.duration;
        builder.recordDuration = this.recordDuration;
        builder.shots = this.shots;
        builder.Altitude = this.Altitude;
        builder.countries = this.countries;
        builder.cities = this.cities;
        builder.rank = this.rank;
        builder.level = this.level;
        builder.title = this.title;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DevicePreviwInfo)) {
            return false;
        }
        DevicePreviwInfo o = (DevicePreviwInfo) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.flyTimes, o.flyTimes) || !Internal.equals(this.maxSpeed, o.maxSpeed) || !Internal.equals(this.maxMileage, o.maxMileage) || !Internal.equals(this.mileage, o.mileage) || !Internal.equals(this.duration, o.duration) || !Internal.equals(this.recordDuration, o.recordDuration) || !Internal.equals(this.shots, o.shots) || !Internal.equals(this.Altitude, o.Altitude) || !Internal.equals(this.countries, o.countries) || !Internal.equals(this.cities, o.cities) || !Internal.equals(this.rank, o.rank) || !Internal.equals(this.level, o.level) || !Internal.equals(this.title, o.title)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.flyTimes != null ? this.flyTimes.hashCode() : 0)) * 37;
        if (this.maxSpeed != null) {
            i = this.maxSpeed.hashCode();
        } else {
            i = 0;
        }
        int i13 = (hashCode + i) * 37;
        if (this.maxMileage != null) {
            i2 = this.maxMileage.hashCode();
        } else {
            i2 = 0;
        }
        int i14 = (i13 + i2) * 37;
        if (this.mileage != null) {
            i3 = this.mileage.hashCode();
        } else {
            i3 = 0;
        }
        int i15 = (i14 + i3) * 37;
        if (this.duration != null) {
            i4 = this.duration.hashCode();
        } else {
            i4 = 0;
        }
        int i16 = (i15 + i4) * 37;
        if (this.recordDuration != null) {
            i5 = this.recordDuration.hashCode();
        } else {
            i5 = 0;
        }
        int i17 = (i16 + i5) * 37;
        if (this.shots != null) {
            i6 = this.shots.hashCode();
        } else {
            i6 = 0;
        }
        int i18 = (i17 + i6) * 37;
        if (this.Altitude != null) {
            i7 = this.Altitude.hashCode();
        } else {
            i7 = 0;
        }
        int i19 = (i18 + i7) * 37;
        if (this.countries != null) {
            i8 = this.countries.hashCode();
        } else {
            i8 = 0;
        }
        int i20 = (i19 + i8) * 37;
        if (this.cities != null) {
            i9 = this.cities.hashCode();
        } else {
            i9 = 0;
        }
        int i21 = (i20 + i9) * 37;
        if (this.rank != null) {
            i10 = this.rank.hashCode();
        } else {
            i10 = 0;
        }
        int i22 = (i21 + i10) * 37;
        if (this.level != null) {
            i11 = this.level.hashCode();
        } else {
            i11 = 0;
        }
        int i23 = (i22 + i11) * 37;
        if (this.title != null) {
            i12 = this.title.hashCode();
        }
        int result2 = i23 + i12;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.flyTimes != null) {
            builder.append(", flyTimes=").append(this.flyTimes);
        }
        if (this.maxSpeed != null) {
            builder.append(", maxSpeed=").append(this.maxSpeed);
        }
        if (this.maxMileage != null) {
            builder.append(", maxMileage=").append(this.maxMileage);
        }
        if (this.mileage != null) {
            builder.append(", mileage=").append(this.mileage);
        }
        if (this.duration != null) {
            builder.append(", duration=").append(this.duration);
        }
        if (this.recordDuration != null) {
            builder.append(", recordDuration=").append(this.recordDuration);
        }
        if (this.shots != null) {
            builder.append(", shots=").append(this.shots);
        }
        if (this.Altitude != null) {
            builder.append(", Altitude=").append(this.Altitude);
        }
        if (this.countries != null) {
            builder.append(", countries=").append(this.countries);
        }
        if (this.cities != null) {
            builder.append(", cities=").append(this.cities);
        }
        if (this.rank != null) {
            builder.append(", rank=").append(this.rank);
        }
        if (this.level != null) {
            builder.append(", level=").append(this.level);
        }
        if (this.title != null) {
            builder.append(", title=").append(this.title);
        }
        return builder.replace(0, 2, "DevicePreviwInfo{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<DevicePreviwInfo, Builder> {
        public Double Altitude;
        public Double cities;
        public Integer countries;
        public Double duration;
        public Integer flyTimes;
        public Double level;
        public Double maxMileage;
        public Double maxSpeed;
        public Double mileage;
        public Double rank;
        public Double recordDuration;
        public Integer shots;
        public String title;

        public Builder flyTimes(Integer flyTimes2) {
            this.flyTimes = flyTimes2;
            return this;
        }

        public Builder maxSpeed(Double maxSpeed2) {
            this.maxSpeed = maxSpeed2;
            return this;
        }

        public Builder maxMileage(Double maxMileage2) {
            this.maxMileage = maxMileage2;
            return this;
        }

        public Builder mileage(Double mileage2) {
            this.mileage = mileage2;
            return this;
        }

        public Builder duration(Double duration2) {
            this.duration = duration2;
            return this;
        }

        public Builder recordDuration(Double recordDuration2) {
            this.recordDuration = recordDuration2;
            return this;
        }

        public Builder shots(Integer shots2) {
            this.shots = shots2;
            return this;
        }

        public Builder Altitude(Double Altitude2) {
            this.Altitude = Altitude2;
            return this;
        }

        public Builder countries(Integer countries2) {
            this.countries = countries2;
            return this;
        }

        public Builder cities(Double cities2) {
            this.cities = cities2;
            return this;
        }

        public Builder rank(Double rank2) {
            this.rank = rank2;
            return this;
        }

        public Builder level(Double level2) {
            this.level = level2;
            return this;
        }

        public Builder title(String title2) {
            this.title = title2;
            return this;
        }

        public DevicePreviwInfo build() {
            return new DevicePreviwInfo(this.flyTimes, this.maxSpeed, this.maxMileage, this.mileage, this.duration, this.recordDuration, this.shots, this.Altitude, this.countries, this.cities, this.rank, this.level, this.title, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_DevicePreviwInfo extends ProtoAdapter<DevicePreviwInfo> {
        ProtoAdapter_DevicePreviwInfo() {
            super(FieldEncoding.LENGTH_DELIMITED, DevicePreviwInfo.class);
        }

        public int encodedSize(DevicePreviwInfo value) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            int i8;
            int i9;
            int i10;
            int i11;
            int i12 = 0;
            int encodedSizeWithTag = value.flyTimes != null ? ProtoAdapter.INT32.encodedSizeWithTag(1, value.flyTimes) : 0;
            if (value.maxSpeed != null) {
                i = ProtoAdapter.DOUBLE.encodedSizeWithTag(2, value.maxSpeed);
            } else {
                i = 0;
            }
            int i13 = i + encodedSizeWithTag;
            if (value.maxMileage != null) {
                i2 = ProtoAdapter.DOUBLE.encodedSizeWithTag(3, value.maxMileage);
            } else {
                i2 = 0;
            }
            int i14 = i13 + i2;
            if (value.mileage != null) {
                i3 = ProtoAdapter.DOUBLE.encodedSizeWithTag(4, value.mileage);
            } else {
                i3 = 0;
            }
            int i15 = i14 + i3;
            if (value.duration != null) {
                i4 = ProtoAdapter.DOUBLE.encodedSizeWithTag(5, value.duration);
            } else {
                i4 = 0;
            }
            int i16 = i15 + i4;
            if (value.recordDuration != null) {
                i5 = ProtoAdapter.DOUBLE.encodedSizeWithTag(6, value.recordDuration);
            } else {
                i5 = 0;
            }
            int i17 = i16 + i5;
            if (value.shots != null) {
                i6 = ProtoAdapter.INT32.encodedSizeWithTag(7, value.shots);
            } else {
                i6 = 0;
            }
            int i18 = i17 + i6;
            if (value.Altitude != null) {
                i7 = ProtoAdapter.DOUBLE.encodedSizeWithTag(8, value.Altitude);
            } else {
                i7 = 0;
            }
            int i19 = i18 + i7;
            if (value.countries != null) {
                i8 = ProtoAdapter.INT32.encodedSizeWithTag(9, value.countries);
            } else {
                i8 = 0;
            }
            int i20 = i19 + i8;
            if (value.cities != null) {
                i9 = ProtoAdapter.DOUBLE.encodedSizeWithTag(10, value.cities);
            } else {
                i9 = 0;
            }
            int i21 = i20 + i9;
            if (value.rank != null) {
                i10 = ProtoAdapter.DOUBLE.encodedSizeWithTag(11, value.rank);
            } else {
                i10 = 0;
            }
            int i22 = i21 + i10;
            if (value.level != null) {
                i11 = ProtoAdapter.DOUBLE.encodedSizeWithTag(12, value.level);
            } else {
                i11 = 0;
            }
            int i23 = i11 + i22;
            if (value.title != null) {
                i12 = ProtoAdapter.STRING.encodedSizeWithTag(13, value.title);
            }
            return i23 + i12 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, DevicePreviwInfo value) throws IOException {
            if (value.flyTimes != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 1, value.flyTimes);
            }
            if (value.maxSpeed != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 2, value.maxSpeed);
            }
            if (value.maxMileage != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 3, value.maxMileage);
            }
            if (value.mileage != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 4, value.mileage);
            }
            if (value.duration != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 5, value.duration);
            }
            if (value.recordDuration != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 6, value.recordDuration);
            }
            if (value.shots != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 7, value.shots);
            }
            if (value.Altitude != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 8, value.Altitude);
            }
            if (value.countries != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 9, value.countries);
            }
            if (value.cities != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 10, value.cities);
            }
            if (value.rank != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 11, value.rank);
            }
            if (value.level != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 12, value.level);
            }
            if (value.title != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 13, value.title);
            }
            writer.writeBytes(value.unknownFields());
        }

        public DevicePreviwInfo decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.flyTimes(ProtoAdapter.INT32.decode(reader));
                            break;
                        case 2:
                            builder.maxSpeed(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 3:
                            builder.maxMileage(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 4:
                            builder.mileage(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 5:
                            builder.duration(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 6:
                            builder.recordDuration(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 7:
                            builder.shots(ProtoAdapter.INT32.decode(reader));
                            break;
                        case 8:
                            builder.Altitude(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 9:
                            builder.countries(ProtoAdapter.INT32.decode(reader));
                            break;
                        case 10:
                            builder.cities(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 11:
                            builder.rank(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 12:
                            builder.level(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 13:
                            builder.title(ProtoAdapter.STRING.decode(reader));
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

        public DevicePreviwInfo redact(DevicePreviwInfo value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
