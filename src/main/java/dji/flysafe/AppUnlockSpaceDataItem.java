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

public final class AppUnlockSpaceDataItem extends Message<AppUnlockSpaceDataItem, Builder> {
    public static final ProtoAdapter<AppUnlockSpaceDataItem> ADAPTER = new ProtoAdapter_AppUnlockSpaceDataItem();
    public static final String DEFAULT_ACCOUNT = "";
    public static final String DEFAULT_AREAID = "";
    public static final String DEFAULT_BEGINDATASTR = "";
    public static final Integer DEFAULT_BEGINTIME = 0;
    public static final String DEFAULT_CITY = "";
    public static final String DEFAULT_COUNTRY = "";
    public static final String DEFAULT_DATASOURCETYPE = "";
    public static final Boolean DEFAULT_DISABLE = false;
    public static final String DEFAULT_ENDDATESTR = "";
    public static final Integer DEFAULT_ENDTIME = 0;
    public static final Integer DEFAULT_ITEMID = 0;
    public static final String DEFAULT_LOCATION = "";
    public static final String DEFAULT_MCSN = "";
    public static final String DEFAULT_OSTYPE = "";
    public static final String DEFAULT_PLACES = "";
    public static final String DEFAULT_SIGNATURE = "";
    public static final String DEFAULT_STATUSSTR = "";
    public static final String DEFAULT_TIMEZONE = "";
    public static final String DEFAULT_TYPE = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 7)
    public final String MCSN;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String account;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 6)
    public final String areaID;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 15)
    public final String beginDataStr;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 3)
    public final Integer beginTime;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 13)
    public final String city;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 12)
    public final String country;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 5)
    public final String dataSourceType;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 17)
    public final Boolean disable;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 16)
    public final String endDateStr;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 4)
    public final Integer endTime;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 1)
    public final Integer itemID;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 11)
    public final String location;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 19)
    public final String osType;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 9)
    public final String places;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 8)
    public final String signature;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 18)
    public final String statusStr;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 10)
    public final String timezone;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 14)
    public final String type;

    public AppUnlockSpaceDataItem(Integer itemID2, String account2, Integer beginTime2, Integer endTime2, String dataSourceType2, String areaID2, String MCSN2, String signature2, String places2, String timezone2, String location2, String country2, String city2, String type2, String beginDataStr2, String endDateStr2, Boolean disable2, String statusStr2, String osType2) {
        this(itemID2, account2, beginTime2, endTime2, dataSourceType2, areaID2, MCSN2, signature2, places2, timezone2, location2, country2, city2, type2, beginDataStr2, endDateStr2, disable2, statusStr2, osType2, ByteString.EMPTY);
    }

    public AppUnlockSpaceDataItem(Integer itemID2, String account2, Integer beginTime2, Integer endTime2, String dataSourceType2, String areaID2, String MCSN2, String signature2, String places2, String timezone2, String location2, String country2, String city2, String type2, String beginDataStr2, String endDateStr2, Boolean disable2, String statusStr2, String osType2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.itemID = itemID2;
        this.account = account2;
        this.beginTime = beginTime2;
        this.endTime = endTime2;
        this.dataSourceType = dataSourceType2;
        this.areaID = areaID2;
        this.MCSN = MCSN2;
        this.signature = signature2;
        this.places = places2;
        this.timezone = timezone2;
        this.location = location2;
        this.country = country2;
        this.city = city2;
        this.type = type2;
        this.beginDataStr = beginDataStr2;
        this.endDateStr = endDateStr2;
        this.disable = disable2;
        this.statusStr = statusStr2;
        this.osType = osType2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.itemID = this.itemID;
        builder.account = this.account;
        builder.beginTime = this.beginTime;
        builder.endTime = this.endTime;
        builder.dataSourceType = this.dataSourceType;
        builder.areaID = this.areaID;
        builder.MCSN = this.MCSN;
        builder.signature = this.signature;
        builder.places = this.places;
        builder.timezone = this.timezone;
        builder.location = this.location;
        builder.country = this.country;
        builder.city = this.city;
        builder.type = this.type;
        builder.beginDataStr = this.beginDataStr;
        builder.endDateStr = this.endDateStr;
        builder.disable = this.disable;
        builder.statusStr = this.statusStr;
        builder.osType = this.osType;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AppUnlockSpaceDataItem)) {
            return false;
        }
        AppUnlockSpaceDataItem o = (AppUnlockSpaceDataItem) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.itemID, o.itemID) || !Internal.equals(this.account, o.account) || !Internal.equals(this.beginTime, o.beginTime) || !Internal.equals(this.endTime, o.endTime) || !Internal.equals(this.dataSourceType, o.dataSourceType) || !Internal.equals(this.areaID, o.areaID) || !Internal.equals(this.MCSN, o.MCSN) || !Internal.equals(this.signature, o.signature) || !Internal.equals(this.places, o.places) || !Internal.equals(this.timezone, o.timezone) || !Internal.equals(this.location, o.location) || !Internal.equals(this.country, o.country) || !Internal.equals(this.city, o.city) || !Internal.equals(this.type, o.type) || !Internal.equals(this.beginDataStr, o.beginDataStr) || !Internal.equals(this.endDateStr, o.endDateStr) || !Internal.equals(this.disable, o.disable) || !Internal.equals(this.statusStr, o.statusStr) || !Internal.equals(this.osType, o.osType)) {
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
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.itemID != null ? this.itemID.hashCode() : 0)) * 37;
        if (this.account != null) {
            i = this.account.hashCode();
        } else {
            i = 0;
        }
        int i19 = (hashCode + i) * 37;
        if (this.beginTime != null) {
            i2 = this.beginTime.hashCode();
        } else {
            i2 = 0;
        }
        int i20 = (i19 + i2) * 37;
        if (this.endTime != null) {
            i3 = this.endTime.hashCode();
        } else {
            i3 = 0;
        }
        int i21 = (i20 + i3) * 37;
        if (this.dataSourceType != null) {
            i4 = this.dataSourceType.hashCode();
        } else {
            i4 = 0;
        }
        int i22 = (i21 + i4) * 37;
        if (this.areaID != null) {
            i5 = this.areaID.hashCode();
        } else {
            i5 = 0;
        }
        int i23 = (i22 + i5) * 37;
        if (this.MCSN != null) {
            i6 = this.MCSN.hashCode();
        } else {
            i6 = 0;
        }
        int i24 = (i23 + i6) * 37;
        if (this.signature != null) {
            i7 = this.signature.hashCode();
        } else {
            i7 = 0;
        }
        int i25 = (i24 + i7) * 37;
        if (this.places != null) {
            i8 = this.places.hashCode();
        } else {
            i8 = 0;
        }
        int i26 = (i25 + i8) * 37;
        if (this.timezone != null) {
            i9 = this.timezone.hashCode();
        } else {
            i9 = 0;
        }
        int i27 = (i26 + i9) * 37;
        if (this.location != null) {
            i10 = this.location.hashCode();
        } else {
            i10 = 0;
        }
        int i28 = (i27 + i10) * 37;
        if (this.country != null) {
            i11 = this.country.hashCode();
        } else {
            i11 = 0;
        }
        int i29 = (i28 + i11) * 37;
        if (this.city != null) {
            i12 = this.city.hashCode();
        } else {
            i12 = 0;
        }
        int i30 = (i29 + i12) * 37;
        if (this.type != null) {
            i13 = this.type.hashCode();
        } else {
            i13 = 0;
        }
        int i31 = (i30 + i13) * 37;
        if (this.beginDataStr != null) {
            i14 = this.beginDataStr.hashCode();
        } else {
            i14 = 0;
        }
        int i32 = (i31 + i14) * 37;
        if (this.endDateStr != null) {
            i15 = this.endDateStr.hashCode();
        } else {
            i15 = 0;
        }
        int i33 = (i32 + i15) * 37;
        if (this.disable != null) {
            i16 = this.disable.hashCode();
        } else {
            i16 = 0;
        }
        int i34 = (i33 + i16) * 37;
        if (this.statusStr != null) {
            i17 = this.statusStr.hashCode();
        } else {
            i17 = 0;
        }
        int i35 = (i34 + i17) * 37;
        if (this.osType != null) {
            i18 = this.osType.hashCode();
        }
        int result2 = i35 + i18;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.itemID != null) {
            builder.append(", itemID=").append(this.itemID);
        }
        if (this.account != null) {
            builder.append(", account=").append(this.account);
        }
        if (this.beginTime != null) {
            builder.append(", beginTime=").append(this.beginTime);
        }
        if (this.endTime != null) {
            builder.append(", endTime=").append(this.endTime);
        }
        if (this.dataSourceType != null) {
            builder.append(", dataSourceType=").append(this.dataSourceType);
        }
        if (this.areaID != null) {
            builder.append(", areaID=").append(this.areaID);
        }
        if (this.MCSN != null) {
            builder.append(", MCSN=").append(this.MCSN);
        }
        if (this.signature != null) {
            builder.append(", signature=").append(this.signature);
        }
        if (this.places != null) {
            builder.append(", places=").append(this.places);
        }
        if (this.timezone != null) {
            builder.append(", timezone=").append(this.timezone);
        }
        if (this.location != null) {
            builder.append(", location=").append(this.location);
        }
        if (this.country != null) {
            builder.append(", country=").append(this.country);
        }
        if (this.city != null) {
            builder.append(", city=").append(this.city);
        }
        if (this.type != null) {
            builder.append(", type=").append(this.type);
        }
        if (this.beginDataStr != null) {
            builder.append(", beginDataStr=").append(this.beginDataStr);
        }
        if (this.endDateStr != null) {
            builder.append(", endDateStr=").append(this.endDateStr);
        }
        if (this.disable != null) {
            builder.append(", disable=").append(this.disable);
        }
        if (this.statusStr != null) {
            builder.append(", statusStr=").append(this.statusStr);
        }
        if (this.osType != null) {
            builder.append(", osType=").append(this.osType);
        }
        return builder.replace(0, 2, "AppUnlockSpaceDataItem{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<AppUnlockSpaceDataItem, Builder> {
        public String MCSN;
        public String account;
        public String areaID;
        public String beginDataStr;
        public Integer beginTime;
        public String city;
        public String country;
        public String dataSourceType;
        public Boolean disable;
        public String endDateStr;
        public Integer endTime;
        public Integer itemID;
        public String location;
        public String osType;
        public String places;
        public String signature;
        public String statusStr;
        public String timezone;
        public String type;

        public Builder itemID(Integer itemID2) {
            this.itemID = itemID2;
            return this;
        }

        public Builder account(String account2) {
            this.account = account2;
            return this;
        }

        public Builder beginTime(Integer beginTime2) {
            this.beginTime = beginTime2;
            return this;
        }

        public Builder endTime(Integer endTime2) {
            this.endTime = endTime2;
            return this;
        }

        public Builder dataSourceType(String dataSourceType2) {
            this.dataSourceType = dataSourceType2;
            return this;
        }

        public Builder areaID(String areaID2) {
            this.areaID = areaID2;
            return this;
        }

        public Builder MCSN(String MCSN2) {
            this.MCSN = MCSN2;
            return this;
        }

        public Builder signature(String signature2) {
            this.signature = signature2;
            return this;
        }

        public Builder places(String places2) {
            this.places = places2;
            return this;
        }

        public Builder timezone(String timezone2) {
            this.timezone = timezone2;
            return this;
        }

        public Builder location(String location2) {
            this.location = location2;
            return this;
        }

        public Builder country(String country2) {
            this.country = country2;
            return this;
        }

        public Builder city(String city2) {
            this.city = city2;
            return this;
        }

        public Builder type(String type2) {
            this.type = type2;
            return this;
        }

        public Builder beginDataStr(String beginDataStr2) {
            this.beginDataStr = beginDataStr2;
            return this;
        }

        public Builder endDateStr(String endDateStr2) {
            this.endDateStr = endDateStr2;
            return this;
        }

        public Builder disable(Boolean disable2) {
            this.disable = disable2;
            return this;
        }

        public Builder statusStr(String statusStr2) {
            this.statusStr = statusStr2;
            return this;
        }

        public Builder osType(String osType2) {
            this.osType = osType2;
            return this;
        }

        public AppUnlockSpaceDataItem build() {
            return new AppUnlockSpaceDataItem(this.itemID, this.account, this.beginTime, this.endTime, this.dataSourceType, this.areaID, this.MCSN, this.signature, this.places, this.timezone, this.location, this.country, this.city, this.type, this.beginDataStr, this.endDateStr, this.disable, this.statusStr, this.osType, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_AppUnlockSpaceDataItem extends ProtoAdapter<AppUnlockSpaceDataItem> {
        ProtoAdapter_AppUnlockSpaceDataItem() {
            super(FieldEncoding.LENGTH_DELIMITED, AppUnlockSpaceDataItem.class);
        }

        public int encodedSize(AppUnlockSpaceDataItem value) {
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
            int i12;
            int i13;
            int i14;
            int i15;
            int i16;
            int i17;
            int i18 = 0;
            int encodedSizeWithTag = value.itemID != null ? ProtoAdapter.UINT32.encodedSizeWithTag(1, value.itemID) : 0;
            if (value.account != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.account);
            } else {
                i = 0;
            }
            int i19 = i + encodedSizeWithTag;
            if (value.beginTime != null) {
                i2 = ProtoAdapter.UINT32.encodedSizeWithTag(3, value.beginTime);
            } else {
                i2 = 0;
            }
            int i20 = i19 + i2;
            if (value.endTime != null) {
                i3 = ProtoAdapter.UINT32.encodedSizeWithTag(4, value.endTime);
            } else {
                i3 = 0;
            }
            int i21 = i20 + i3;
            if (value.dataSourceType != null) {
                i4 = ProtoAdapter.STRING.encodedSizeWithTag(5, value.dataSourceType);
            } else {
                i4 = 0;
            }
            int i22 = i21 + i4;
            if (value.areaID != null) {
                i5 = ProtoAdapter.STRING.encodedSizeWithTag(6, value.areaID);
            } else {
                i5 = 0;
            }
            int i23 = i22 + i5;
            if (value.MCSN != null) {
                i6 = ProtoAdapter.STRING.encodedSizeWithTag(7, value.MCSN);
            } else {
                i6 = 0;
            }
            int i24 = i23 + i6;
            if (value.signature != null) {
                i7 = ProtoAdapter.STRING.encodedSizeWithTag(8, value.signature);
            } else {
                i7 = 0;
            }
            int i25 = i24 + i7;
            if (value.places != null) {
                i8 = ProtoAdapter.STRING.encodedSizeWithTag(9, value.places);
            } else {
                i8 = 0;
            }
            int i26 = i25 + i8;
            if (value.timezone != null) {
                i9 = ProtoAdapter.STRING.encodedSizeWithTag(10, value.timezone);
            } else {
                i9 = 0;
            }
            int i27 = i26 + i9;
            if (value.location != null) {
                i10 = ProtoAdapter.STRING.encodedSizeWithTag(11, value.location);
            } else {
                i10 = 0;
            }
            int i28 = i27 + i10;
            if (value.country != null) {
                i11 = ProtoAdapter.STRING.encodedSizeWithTag(12, value.country);
            } else {
                i11 = 0;
            }
            int i29 = i28 + i11;
            if (value.city != null) {
                i12 = ProtoAdapter.STRING.encodedSizeWithTag(13, value.city);
            } else {
                i12 = 0;
            }
            int i30 = i29 + i12;
            if (value.type != null) {
                i13 = ProtoAdapter.STRING.encodedSizeWithTag(14, value.type);
            } else {
                i13 = 0;
            }
            int i31 = i30 + i13;
            if (value.beginDataStr != null) {
                i14 = ProtoAdapter.STRING.encodedSizeWithTag(15, value.beginDataStr);
            } else {
                i14 = 0;
            }
            int i32 = i31 + i14;
            if (value.endDateStr != null) {
                i15 = ProtoAdapter.STRING.encodedSizeWithTag(16, value.endDateStr);
            } else {
                i15 = 0;
            }
            int i33 = i32 + i15;
            if (value.disable != null) {
                i16 = ProtoAdapter.BOOL.encodedSizeWithTag(17, value.disable);
            } else {
                i16 = 0;
            }
            int i34 = i33 + i16;
            if (value.statusStr != null) {
                i17 = ProtoAdapter.STRING.encodedSizeWithTag(18, value.statusStr);
            } else {
                i17 = 0;
            }
            int i35 = i17 + i34;
            if (value.osType != null) {
                i18 = ProtoAdapter.STRING.encodedSizeWithTag(19, value.osType);
            }
            return i35 + i18 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, AppUnlockSpaceDataItem value) throws IOException {
            if (value.itemID != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.itemID);
            }
            if (value.account != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.account);
            }
            if (value.beginTime != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.beginTime);
            }
            if (value.endTime != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.endTime);
            }
            if (value.dataSourceType != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 5, value.dataSourceType);
            }
            if (value.areaID != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 6, value.areaID);
            }
            if (value.MCSN != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 7, value.MCSN);
            }
            if (value.signature != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 8, value.signature);
            }
            if (value.places != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 9, value.places);
            }
            if (value.timezone != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 10, value.timezone);
            }
            if (value.location != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 11, value.location);
            }
            if (value.country != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 12, value.country);
            }
            if (value.city != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 13, value.city);
            }
            if (value.type != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 14, value.type);
            }
            if (value.beginDataStr != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 15, value.beginDataStr);
            }
            if (value.endDateStr != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 16, value.endDateStr);
            }
            if (value.disable != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 17, value.disable);
            }
            if (value.statusStr != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 18, value.statusStr);
            }
            if (value.osType != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 19, value.osType);
            }
            writer.writeBytes(value.unknownFields());
        }

        public AppUnlockSpaceDataItem decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.itemID(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 2:
                            builder.account(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            builder.beginTime(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 4:
                            builder.endTime(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 5:
                            builder.dataSourceType(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 6:
                            builder.areaID(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 7:
                            builder.MCSN(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 8:
                            builder.signature(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 9:
                            builder.places(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 10:
                            builder.timezone(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 11:
                            builder.location(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 12:
                            builder.country(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 13:
                            builder.city(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 14:
                            builder.type(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 15:
                            builder.beginDataStr(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 16:
                            builder.endDateStr(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 17:
                            builder.disable(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 18:
                            builder.statusStr(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 19:
                            builder.osType(ProtoAdapter.STRING.decode(reader));
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

        public AppUnlockSpaceDataItem redact(AppUnlockSpaceDataItem value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
