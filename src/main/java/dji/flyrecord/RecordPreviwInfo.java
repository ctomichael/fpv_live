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

public final class RecordPreviwInfo extends Message<RecordPreviwInfo, Builder> {
    public static final ProtoAdapter<RecordPreviwInfo> ADAPTER = new ProtoAdapter_RecordPreviwInfo();
    public static final Double DEFAULT_ALTITUDE = Double.valueOf(0.0d);
    public static final Double DEFAULT_DATE = Double.valueOf(0.0d);
    public static final Double DEFAULT_DURATION = Double.valueOf(0.0d);
    public static final String DEFAULT_FILEPATH = "";
    public static final Boolean DEFAULT_ISFOLLOW = false;
    public static final Boolean DEFAULT_ISSYNCED = false;
    public static final Double DEFAULT_LATITUDE = Double.valueOf(0.0d);
    public static final String DEFAULT_LOCATION = "";
    public static final Double DEFAULT_LONGTITUE = Double.valueOf(0.0d);
    public static final Double DEFAULT_MILEAGE = Double.valueOf(0.0d);
    public static final ByteString DEFAULT_PREVIEWPHOTO = ByteString.EMPTY;
    public static final String DEFAULT_SN = "";
    public static final String DEFAULT_URL = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 7)
    public final Double altitude;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 3)
    public final Double date;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 6)
    public final Double duration;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 10)
    public final String filePath;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 8)
    public final Boolean isFollow;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 9)
    public final Boolean isSynced;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 11)
    public final Double latitude;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 4)
    public final String location;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 12)
    public final Double longtitue;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 5)
    public final Double mileage;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BYTES", tag = 13)
    public final ByteString previewPhoto;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String sn;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String url;

    public RecordPreviwInfo(String sn2, String url2, Double date2, String location2, Double mileage2, Double duration2, Double altitude2, Boolean isFollow2, Boolean isSynced2, String filePath2, Double latitude2, Double longtitue2, ByteString previewPhoto2) {
        this(sn2, url2, date2, location2, mileage2, duration2, altitude2, isFollow2, isSynced2, filePath2, latitude2, longtitue2, previewPhoto2, ByteString.EMPTY);
    }

    public RecordPreviwInfo(String sn2, String url2, Double date2, String location2, Double mileage2, Double duration2, Double altitude2, Boolean isFollow2, Boolean isSynced2, String filePath2, Double latitude2, Double longtitue2, ByteString previewPhoto2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.sn = sn2;
        this.url = url2;
        this.date = date2;
        this.location = location2;
        this.mileage = mileage2;
        this.duration = duration2;
        this.altitude = altitude2;
        this.isFollow = isFollow2;
        this.isSynced = isSynced2;
        this.filePath = filePath2;
        this.latitude = latitude2;
        this.longtitue = longtitue2;
        this.previewPhoto = previewPhoto2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.sn = this.sn;
        builder.url = this.url;
        builder.date = this.date;
        builder.location = this.location;
        builder.mileage = this.mileage;
        builder.duration = this.duration;
        builder.altitude = this.altitude;
        builder.isFollow = this.isFollow;
        builder.isSynced = this.isSynced;
        builder.filePath = this.filePath;
        builder.latitude = this.latitude;
        builder.longtitue = this.longtitue;
        builder.previewPhoto = this.previewPhoto;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RecordPreviwInfo)) {
            return false;
        }
        RecordPreviwInfo o = (RecordPreviwInfo) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.sn, o.sn) || !Internal.equals(this.url, o.url) || !Internal.equals(this.date, o.date) || !Internal.equals(this.location, o.location) || !Internal.equals(this.mileage, o.mileage) || !Internal.equals(this.duration, o.duration) || !Internal.equals(this.altitude, o.altitude) || !Internal.equals(this.isFollow, o.isFollow) || !Internal.equals(this.isSynced, o.isSynced) || !Internal.equals(this.filePath, o.filePath) || !Internal.equals(this.latitude, o.latitude) || !Internal.equals(this.longtitue, o.longtitue) || !Internal.equals(this.previewPhoto, o.previewPhoto)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.sn != null ? this.sn.hashCode() : 0)) * 37;
        if (this.url != null) {
            i = this.url.hashCode();
        } else {
            i = 0;
        }
        int i13 = (hashCode + i) * 37;
        if (this.date != null) {
            i2 = this.date.hashCode();
        } else {
            i2 = 0;
        }
        int i14 = (i13 + i2) * 37;
        if (this.location != null) {
            i3 = this.location.hashCode();
        } else {
            i3 = 0;
        }
        int i15 = (i14 + i3) * 37;
        if (this.mileage != null) {
            i4 = this.mileage.hashCode();
        } else {
            i4 = 0;
        }
        int i16 = (i15 + i4) * 37;
        if (this.duration != null) {
            i5 = this.duration.hashCode();
        } else {
            i5 = 0;
        }
        int i17 = (i16 + i5) * 37;
        if (this.altitude != null) {
            i6 = this.altitude.hashCode();
        } else {
            i6 = 0;
        }
        int i18 = (i17 + i6) * 37;
        if (this.isFollow != null) {
            i7 = this.isFollow.hashCode();
        } else {
            i7 = 0;
        }
        int i19 = (i18 + i7) * 37;
        if (this.isSynced != null) {
            i8 = this.isSynced.hashCode();
        } else {
            i8 = 0;
        }
        int i20 = (i19 + i8) * 37;
        if (this.filePath != null) {
            i9 = this.filePath.hashCode();
        } else {
            i9 = 0;
        }
        int i21 = (i20 + i9) * 37;
        if (this.latitude != null) {
            i10 = this.latitude.hashCode();
        } else {
            i10 = 0;
        }
        int i22 = (i21 + i10) * 37;
        if (this.longtitue != null) {
            i11 = this.longtitue.hashCode();
        } else {
            i11 = 0;
        }
        int i23 = (i22 + i11) * 37;
        if (this.previewPhoto != null) {
            i12 = this.previewPhoto.hashCode();
        }
        int result2 = i23 + i12;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.sn != null) {
            builder.append(", sn=").append(this.sn);
        }
        if (this.url != null) {
            builder.append(", url=").append(this.url);
        }
        if (this.date != null) {
            builder.append(", date=").append(this.date);
        }
        if (this.location != null) {
            builder.append(", location=").append(this.location);
        }
        if (this.mileage != null) {
            builder.append(", mileage=").append(this.mileage);
        }
        if (this.duration != null) {
            builder.append(", duration=").append(this.duration);
        }
        if (this.altitude != null) {
            builder.append(", altitude=").append(this.altitude);
        }
        if (this.isFollow != null) {
            builder.append(", isFollow=").append(this.isFollow);
        }
        if (this.isSynced != null) {
            builder.append(", isSynced=").append(this.isSynced);
        }
        if (this.filePath != null) {
            builder.append(", filePath=").append(this.filePath);
        }
        if (this.latitude != null) {
            builder.append(", latitude=").append(this.latitude);
        }
        if (this.longtitue != null) {
            builder.append(", longtitue=").append(this.longtitue);
        }
        if (this.previewPhoto != null) {
            builder.append(", previewPhoto=").append(this.previewPhoto);
        }
        return builder.replace(0, 2, "RecordPreviwInfo{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<RecordPreviwInfo, Builder> {
        public Double altitude;
        public Double date;
        public Double duration;
        public String filePath;
        public Boolean isFollow;
        public Boolean isSynced;
        public Double latitude;
        public String location;
        public Double longtitue;
        public Double mileage;
        public ByteString previewPhoto;
        public String sn;
        public String url;

        public Builder sn(String sn2) {
            this.sn = sn2;
            return this;
        }

        public Builder url(String url2) {
            this.url = url2;
            return this;
        }

        public Builder date(Double date2) {
            this.date = date2;
            return this;
        }

        public Builder location(String location2) {
            this.location = location2;
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

        public Builder altitude(Double altitude2) {
            this.altitude = altitude2;
            return this;
        }

        public Builder isFollow(Boolean isFollow2) {
            this.isFollow = isFollow2;
            return this;
        }

        public Builder isSynced(Boolean isSynced2) {
            this.isSynced = isSynced2;
            return this;
        }

        public Builder filePath(String filePath2) {
            this.filePath = filePath2;
            return this;
        }

        public Builder latitude(Double latitude2) {
            this.latitude = latitude2;
            return this;
        }

        public Builder longtitue(Double longtitue2) {
            this.longtitue = longtitue2;
            return this;
        }

        public Builder previewPhoto(ByteString previewPhoto2) {
            this.previewPhoto = previewPhoto2;
            return this;
        }

        public RecordPreviwInfo build() {
            return new RecordPreviwInfo(this.sn, this.url, this.date, this.location, this.mileage, this.duration, this.altitude, this.isFollow, this.isSynced, this.filePath, this.latitude, this.longtitue, this.previewPhoto, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_RecordPreviwInfo extends ProtoAdapter<RecordPreviwInfo> {
        ProtoAdapter_RecordPreviwInfo() {
            super(FieldEncoding.LENGTH_DELIMITED, RecordPreviwInfo.class);
        }

        public int encodedSize(RecordPreviwInfo value) {
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
            int encodedSizeWithTag = value.sn != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.sn) : 0;
            if (value.url != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.url);
            } else {
                i = 0;
            }
            int i13 = i + encodedSizeWithTag;
            if (value.date != null) {
                i2 = ProtoAdapter.DOUBLE.encodedSizeWithTag(3, value.date);
            } else {
                i2 = 0;
            }
            int i14 = i13 + i2;
            if (value.location != null) {
                i3 = ProtoAdapter.STRING.encodedSizeWithTag(4, value.location);
            } else {
                i3 = 0;
            }
            int i15 = i14 + i3;
            if (value.mileage != null) {
                i4 = ProtoAdapter.DOUBLE.encodedSizeWithTag(5, value.mileage);
            } else {
                i4 = 0;
            }
            int i16 = i15 + i4;
            if (value.duration != null) {
                i5 = ProtoAdapter.DOUBLE.encodedSizeWithTag(6, value.duration);
            } else {
                i5 = 0;
            }
            int i17 = i16 + i5;
            if (value.altitude != null) {
                i6 = ProtoAdapter.DOUBLE.encodedSizeWithTag(7, value.altitude);
            } else {
                i6 = 0;
            }
            int i18 = i17 + i6;
            if (value.isFollow != null) {
                i7 = ProtoAdapter.BOOL.encodedSizeWithTag(8, value.isFollow);
            } else {
                i7 = 0;
            }
            int i19 = i18 + i7;
            if (value.isSynced != null) {
                i8 = ProtoAdapter.BOOL.encodedSizeWithTag(9, value.isSynced);
            } else {
                i8 = 0;
            }
            int i20 = i19 + i8;
            if (value.filePath != null) {
                i9 = ProtoAdapter.STRING.encodedSizeWithTag(10, value.filePath);
            } else {
                i9 = 0;
            }
            int i21 = i20 + i9;
            if (value.latitude != null) {
                i10 = ProtoAdapter.DOUBLE.encodedSizeWithTag(11, value.latitude);
            } else {
                i10 = 0;
            }
            int i22 = i21 + i10;
            if (value.longtitue != null) {
                i11 = ProtoAdapter.DOUBLE.encodedSizeWithTag(12, value.longtitue);
            } else {
                i11 = 0;
            }
            int i23 = i11 + i22;
            if (value.previewPhoto != null) {
                i12 = ProtoAdapter.BYTES.encodedSizeWithTag(13, value.previewPhoto);
            }
            return i23 + i12 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, RecordPreviwInfo value) throws IOException {
            if (value.sn != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.sn);
            }
            if (value.url != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.url);
            }
            if (value.date != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 3, value.date);
            }
            if (value.location != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 4, value.location);
            }
            if (value.mileage != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 5, value.mileage);
            }
            if (value.duration != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 6, value.duration);
            }
            if (value.altitude != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 7, value.altitude);
            }
            if (value.isFollow != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 8, value.isFollow);
            }
            if (value.isSynced != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 9, value.isSynced);
            }
            if (value.filePath != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 10, value.filePath);
            }
            if (value.latitude != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 11, value.latitude);
            }
            if (value.longtitue != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 12, value.longtitue);
            }
            if (value.previewPhoto != null) {
                ProtoAdapter.BYTES.encodeWithTag(writer, 13, value.previewPhoto);
            }
            writer.writeBytes(value.unknownFields());
        }

        public RecordPreviwInfo decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.sn(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.url(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            builder.date(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 4:
                            builder.location(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 5:
                            builder.mileage(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 6:
                            builder.duration(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 7:
                            builder.altitude(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 8:
                            builder.isFollow(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 9:
                            builder.isSynced(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 10:
                            builder.filePath(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 11:
                            builder.latitude(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 12:
                            builder.longtitue(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 13:
                            builder.previewPhoto(ProtoAdapter.BYTES.decode(reader));
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

        public RecordPreviwInfo redact(RecordPreviwInfo value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
