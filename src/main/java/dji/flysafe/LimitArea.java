package dji.flysafe;

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

public final class LimitArea extends Message<LimitArea, Builder> {
    public static final ProtoAdapter<LimitArea> ADAPTER = new ProtoAdapter_LimitArea();
    public static final Long DEFAULT_AREA_ID = 0L;
    public static final Long DEFAULT_BEGIN_TIME = 0L;
    public static final String DEFAULT_CITY = "";
    public static final Integer DEFAULT_COUNTRY = 0;
    public static final Long DEFAULT_END_TIME = 0L;
    public static final Long DEFAULT_INTERNAL_HASH = 0L;
    public static final Boolean DEFAULT_IS_DISABLE = false;
    public static final Boolean DEFAULT_IS_UNLOCKED = false;
    public static final Boolean DEFAULT_IS_WARNING = false;
    public static final Double DEFAULT_LATITUDE = Double.valueOf(0.0d);
    public static final LimitAreaLevel DEFAULT_LEVEL = LimitAreaLevel.Warning;
    public static final Long DEFAULT_LIMIT_HEIGHT = 0L;
    public static final Double DEFAULT_LONGITUDE = Double.valueOf(0.0d);
    public static final String DEFAULT_NAME = "";
    public static final Integer DEFAULT_OUTER_RADIUS = 0;
    public static final String DEFAULT_PHONE_NUMBER = "";
    public static final String DEFAULT_POINT_DESC = "";
    public static final String DEFAULT_POLYGON_SHAPE = "";
    public static final Integer DEFAULT_RADIUS = 0;
    public static final Float DEFAULT_SEARCH_RADIUS = Float.valueOf(0.0f);
    public static final LimitAreaShape DEFAULT_SHAPE = LimitAreaShape.Circle;
    public static final LimitAreaSource DEFAULT_SOURCE = LimitAreaSource.DJISourceOne;
    public static final Long DEFAULT_SUB_AREA_ID = 0L;
    public static final LimitAreaType DEFAULT_TYPE = LimitAreaType.DJIAirport;
    public static final Long DEFAULT_UPDATED_TIME = 0L;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT64", tag = 1)
    public final Long area_id;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT64", tag = 22)
    public final Long begin_time;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 26)
    public final String city;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 14)
    public final Integer country;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT64", tag = 23)
    public final Long end_time;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT64", tag = 20)
    public final Long internal_hash;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 25)
    public final Boolean is_disable;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 4)
    public final Boolean is_unlocked;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 12)
    public final Boolean is_warning;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 8)
    public final Double latitude;
    @WireField(adapter = "dji.flysafe.LimitAreaLevel#ADAPTER", tag = 13)
    public final LimitAreaLevel level;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT64", tag = 17)
    public final Long limit_height;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#DOUBLE", tag = 9)
    public final Double longitude;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 27)
    public final String name;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 11)
    public final Integer outer_radius;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 3)
    public final String phone_number;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 15)
    public final String point_desc;
    @WireField(adapter = "dji.flysafe.LocationCoordinate#ADAPTER", label = WireField.Label.REPEATED, tag = 19)
    public final List<LocationCoordinate> polygon_points;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 24)
    public final String polygon_shape;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 10)
    public final Integer radius;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#FLOAT", tag = 2)
    public final Float search_radius;
    @WireField(adapter = "dji.flysafe.LimitAreaShape#ADAPTER", tag = 6)
    public final LimitAreaShape shape;
    @WireField(adapter = "dji.flysafe.LimitAreaSource#ADAPTER", tag = 5)
    public final LimitAreaSource source;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT64", tag = 16)
    public final Long sub_area_id;
    @WireField(adapter = "dji.flysafe.LimitArea#ADAPTER", label = WireField.Label.REPEATED, tag = 18)
    public final List<LimitArea> sub_shapes;
    @WireField(adapter = "dji.flysafe.LimitAreaType#ADAPTER", tag = 7)
    public final LimitAreaType type;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT64", tag = 21)
    public final Long updated_time;

    public LimitArea(Long area_id2, Float search_radius2, String phone_number2, Boolean is_unlocked2, LimitAreaSource source2, LimitAreaShape shape2, LimitAreaType type2, Double latitude2, Double longitude2, Integer radius2, Integer outer_radius2, Boolean is_warning2, LimitAreaLevel level2, Integer country2, String point_desc2, Long sub_area_id2, Long limit_height2, List<LimitArea> sub_shapes2, List<LocationCoordinate> polygon_points2, Long internal_hash2, Long updated_time2, Long begin_time2, Long end_time2, String polygon_shape2, Boolean is_disable2, String city2, String name2) {
        this(area_id2, search_radius2, phone_number2, is_unlocked2, source2, shape2, type2, latitude2, longitude2, radius2, outer_radius2, is_warning2, level2, country2, point_desc2, sub_area_id2, limit_height2, sub_shapes2, polygon_points2, internal_hash2, updated_time2, begin_time2, end_time2, polygon_shape2, is_disable2, city2, name2, ByteString.EMPTY);
    }

    public LimitArea(Long area_id2, Float search_radius2, String phone_number2, Boolean is_unlocked2, LimitAreaSource source2, LimitAreaShape shape2, LimitAreaType type2, Double latitude2, Double longitude2, Integer radius2, Integer outer_radius2, Boolean is_warning2, LimitAreaLevel level2, Integer country2, String point_desc2, Long sub_area_id2, Long limit_height2, List<LimitArea> sub_shapes2, List<LocationCoordinate> polygon_points2, Long internal_hash2, Long updated_time2, Long begin_time2, Long end_time2, String polygon_shape2, Boolean is_disable2, String city2, String name2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.area_id = area_id2;
        this.search_radius = search_radius2;
        this.phone_number = phone_number2;
        this.is_unlocked = is_unlocked2;
        this.source = source2;
        this.shape = shape2;
        this.type = type2;
        this.latitude = latitude2;
        this.longitude = longitude2;
        this.radius = radius2;
        this.outer_radius = outer_radius2;
        this.is_warning = is_warning2;
        this.level = level2;
        this.country = country2;
        this.point_desc = point_desc2;
        this.sub_area_id = sub_area_id2;
        this.limit_height = limit_height2;
        this.sub_shapes = Internal.immutableCopyOf("sub_shapes", sub_shapes2);
        this.polygon_points = Internal.immutableCopyOf("polygon_points", polygon_points2);
        this.internal_hash = internal_hash2;
        this.updated_time = updated_time2;
        this.begin_time = begin_time2;
        this.end_time = end_time2;
        this.polygon_shape = polygon_shape2;
        this.is_disable = is_disable2;
        this.city = city2;
        this.name = name2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.area_id = this.area_id;
        builder.search_radius = this.search_radius;
        builder.phone_number = this.phone_number;
        builder.is_unlocked = this.is_unlocked;
        builder.source = this.source;
        builder.shape = this.shape;
        builder.type = this.type;
        builder.latitude = this.latitude;
        builder.longitude = this.longitude;
        builder.radius = this.radius;
        builder.outer_radius = this.outer_radius;
        builder.is_warning = this.is_warning;
        builder.level = this.level;
        builder.country = this.country;
        builder.point_desc = this.point_desc;
        builder.sub_area_id = this.sub_area_id;
        builder.limit_height = this.limit_height;
        builder.sub_shapes = Internal.copyOf("sub_shapes", this.sub_shapes);
        builder.polygon_points = Internal.copyOf("polygon_points", this.polygon_points);
        builder.internal_hash = this.internal_hash;
        builder.updated_time = this.updated_time;
        builder.begin_time = this.begin_time;
        builder.end_time = this.end_time;
        builder.polygon_shape = this.polygon_shape;
        builder.is_disable = this.is_disable;
        builder.city = this.city;
        builder.name = this.name;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LimitArea)) {
            return false;
        }
        LimitArea o = (LimitArea) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.area_id, o.area_id) || !Internal.equals(this.search_radius, o.search_radius) || !Internal.equals(this.phone_number, o.phone_number) || !Internal.equals(this.is_unlocked, o.is_unlocked) || !Internal.equals(this.source, o.source) || !Internal.equals(this.shape, o.shape) || !Internal.equals(this.type, o.type) || !Internal.equals(this.latitude, o.latitude) || !Internal.equals(this.longitude, o.longitude) || !Internal.equals(this.radius, o.radius) || !Internal.equals(this.outer_radius, o.outer_radius) || !Internal.equals(this.is_warning, o.is_warning) || !Internal.equals(this.level, o.level) || !Internal.equals(this.country, o.country) || !Internal.equals(this.point_desc, o.point_desc) || !Internal.equals(this.sub_area_id, o.sub_area_id) || !Internal.equals(this.limit_height, o.limit_height) || !this.sub_shapes.equals(o.sub_shapes) || !this.polygon_points.equals(o.polygon_points) || !Internal.equals(this.internal_hash, o.internal_hash) || !Internal.equals(this.updated_time, o.updated_time) || !Internal.equals(this.begin_time, o.begin_time) || !Internal.equals(this.end_time, o.end_time) || !Internal.equals(this.polygon_shape, o.polygon_shape) || !Internal.equals(this.is_disable, o.is_disable) || !Internal.equals(this.city, o.city) || !Internal.equals(this.name, o.name)) {
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
        int i18;
        int i19;
        int i20;
        int i21;
        int i22;
        int i23;
        int i24 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.area_id != null ? this.area_id.hashCode() : 0)) * 37;
        if (this.search_radius != null) {
            i = this.search_radius.hashCode();
        } else {
            i = 0;
        }
        int i25 = (hashCode + i) * 37;
        if (this.phone_number != null) {
            i2 = this.phone_number.hashCode();
        } else {
            i2 = 0;
        }
        int i26 = (i25 + i2) * 37;
        if (this.is_unlocked != null) {
            i3 = this.is_unlocked.hashCode();
        } else {
            i3 = 0;
        }
        int i27 = (i26 + i3) * 37;
        if (this.source != null) {
            i4 = this.source.hashCode();
        } else {
            i4 = 0;
        }
        int i28 = (i27 + i4) * 37;
        if (this.shape != null) {
            i5 = this.shape.hashCode();
        } else {
            i5 = 0;
        }
        int i29 = (i28 + i5) * 37;
        if (this.type != null) {
            i6 = this.type.hashCode();
        } else {
            i6 = 0;
        }
        int i30 = (i29 + i6) * 37;
        if (this.latitude != null) {
            i7 = this.latitude.hashCode();
        } else {
            i7 = 0;
        }
        int i31 = (i30 + i7) * 37;
        if (this.longitude != null) {
            i8 = this.longitude.hashCode();
        } else {
            i8 = 0;
        }
        int i32 = (i31 + i8) * 37;
        if (this.radius != null) {
            i9 = this.radius.hashCode();
        } else {
            i9 = 0;
        }
        int i33 = (i32 + i9) * 37;
        if (this.outer_radius != null) {
            i10 = this.outer_radius.hashCode();
        } else {
            i10 = 0;
        }
        int i34 = (i33 + i10) * 37;
        if (this.is_warning != null) {
            i11 = this.is_warning.hashCode();
        } else {
            i11 = 0;
        }
        int i35 = (i34 + i11) * 37;
        if (this.level != null) {
            i12 = this.level.hashCode();
        } else {
            i12 = 0;
        }
        int i36 = (i35 + i12) * 37;
        if (this.country != null) {
            i13 = this.country.hashCode();
        } else {
            i13 = 0;
        }
        int i37 = (i36 + i13) * 37;
        if (this.point_desc != null) {
            i14 = this.point_desc.hashCode();
        } else {
            i14 = 0;
        }
        int i38 = (i37 + i14) * 37;
        if (this.sub_area_id != null) {
            i15 = this.sub_area_id.hashCode();
        } else {
            i15 = 0;
        }
        int i39 = (i38 + i15) * 37;
        if (this.limit_height != null) {
            i16 = this.limit_height.hashCode();
        } else {
            i16 = 0;
        }
        int hashCode2 = (((((i39 + i16) * 37) + this.sub_shapes.hashCode()) * 37) + this.polygon_points.hashCode()) * 37;
        if (this.internal_hash != null) {
            i17 = this.internal_hash.hashCode();
        } else {
            i17 = 0;
        }
        int i40 = (hashCode2 + i17) * 37;
        if (this.updated_time != null) {
            i18 = this.updated_time.hashCode();
        } else {
            i18 = 0;
        }
        int i41 = (i40 + i18) * 37;
        if (this.begin_time != null) {
            i19 = this.begin_time.hashCode();
        } else {
            i19 = 0;
        }
        int i42 = (i41 + i19) * 37;
        if (this.end_time != null) {
            i20 = this.end_time.hashCode();
        } else {
            i20 = 0;
        }
        int i43 = (i42 + i20) * 37;
        if (this.polygon_shape != null) {
            i21 = this.polygon_shape.hashCode();
        } else {
            i21 = 0;
        }
        int i44 = (i43 + i21) * 37;
        if (this.is_disable != null) {
            i22 = this.is_disable.hashCode();
        } else {
            i22 = 0;
        }
        int i45 = (i44 + i22) * 37;
        if (this.city != null) {
            i23 = this.city.hashCode();
        } else {
            i23 = 0;
        }
        int i46 = (i45 + i23) * 37;
        if (this.name != null) {
            i24 = this.name.hashCode();
        }
        int result2 = i46 + i24;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.area_id != null) {
            builder.append(", area_id=").append(this.area_id);
        }
        if (this.search_radius != null) {
            builder.append(", search_radius=").append(this.search_radius);
        }
        if (this.phone_number != null) {
            builder.append(", phone_number=").append(this.phone_number);
        }
        if (this.is_unlocked != null) {
            builder.append(", is_unlocked=").append(this.is_unlocked);
        }
        if (this.source != null) {
            builder.append(", source=").append(this.source);
        }
        if (this.shape != null) {
            builder.append(", shape=").append(this.shape);
        }
        if (this.type != null) {
            builder.append(", type=").append(this.type);
        }
        if (this.latitude != null) {
            builder.append(", latitude=").append(this.latitude);
        }
        if (this.longitude != null) {
            builder.append(", longitude=").append(this.longitude);
        }
        if (this.radius != null) {
            builder.append(", radius=").append(this.radius);
        }
        if (this.outer_radius != null) {
            builder.append(", outer_radius=").append(this.outer_radius);
        }
        if (this.is_warning != null) {
            builder.append(", is_warning=").append(this.is_warning);
        }
        if (this.level != null) {
            builder.append(", level=").append(this.level);
        }
        if (this.country != null) {
            builder.append(", country=").append(this.country);
        }
        if (this.point_desc != null) {
            builder.append(", point_desc=").append(this.point_desc);
        }
        if (this.sub_area_id != null) {
            builder.append(", sub_area_id=").append(this.sub_area_id);
        }
        if (this.limit_height != null) {
            builder.append(", limit_height=").append(this.limit_height);
        }
        if (!this.sub_shapes.isEmpty()) {
            builder.append(", sub_shapes=").append(this.sub_shapes);
        }
        if (!this.polygon_points.isEmpty()) {
            builder.append(", polygon_points=").append(this.polygon_points);
        }
        if (this.internal_hash != null) {
            builder.append(", internal_hash=").append(this.internal_hash);
        }
        if (this.updated_time != null) {
            builder.append(", updated_time=").append(this.updated_time);
        }
        if (this.begin_time != null) {
            builder.append(", begin_time=").append(this.begin_time);
        }
        if (this.end_time != null) {
            builder.append(", end_time=").append(this.end_time);
        }
        if (this.polygon_shape != null) {
            builder.append(", polygon_shape=").append(this.polygon_shape);
        }
        if (this.is_disable != null) {
            builder.append(", is_disable=").append(this.is_disable);
        }
        if (this.city != null) {
            builder.append(", city=").append(this.city);
        }
        if (this.name != null) {
            builder.append(", name=").append(this.name);
        }
        return builder.replace(0, 2, "LimitArea{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<LimitArea, Builder> {
        public Long area_id;
        public Long begin_time;
        public String city;
        public Integer country;
        public Long end_time;
        public Long internal_hash;
        public Boolean is_disable;
        public Boolean is_unlocked;
        public Boolean is_warning;
        public Double latitude;
        public LimitAreaLevel level;
        public Long limit_height;
        public Double longitude;
        public String name;
        public Integer outer_radius;
        public String phone_number;
        public String point_desc;
        public List<LocationCoordinate> polygon_points = Internal.newMutableList();
        public String polygon_shape;
        public Integer radius;
        public Float search_radius;
        public LimitAreaShape shape;
        public LimitAreaSource source;
        public Long sub_area_id;
        public List<LimitArea> sub_shapes = Internal.newMutableList();
        public LimitAreaType type;
        public Long updated_time;

        public Builder area_id(Long area_id2) {
            this.area_id = area_id2;
            return this;
        }

        public Builder search_radius(Float search_radius2) {
            this.search_radius = search_radius2;
            return this;
        }

        public Builder phone_number(String phone_number2) {
            this.phone_number = phone_number2;
            return this;
        }

        public Builder is_unlocked(Boolean is_unlocked2) {
            this.is_unlocked = is_unlocked2;
            return this;
        }

        public Builder source(LimitAreaSource source2) {
            this.source = source2;
            return this;
        }

        public Builder shape(LimitAreaShape shape2) {
            this.shape = shape2;
            return this;
        }

        public Builder type(LimitAreaType type2) {
            this.type = type2;
            return this;
        }

        public Builder latitude(Double latitude2) {
            this.latitude = latitude2;
            return this;
        }

        public Builder longitude(Double longitude2) {
            this.longitude = longitude2;
            return this;
        }

        public Builder radius(Integer radius2) {
            this.radius = radius2;
            return this;
        }

        public Builder outer_radius(Integer outer_radius2) {
            this.outer_radius = outer_radius2;
            return this;
        }

        public Builder is_warning(Boolean is_warning2) {
            this.is_warning = is_warning2;
            return this;
        }

        public Builder level(LimitAreaLevel level2) {
            this.level = level2;
            return this;
        }

        public Builder country(Integer country2) {
            this.country = country2;
            return this;
        }

        public Builder point_desc(String point_desc2) {
            this.point_desc = point_desc2;
            return this;
        }

        public Builder sub_area_id(Long sub_area_id2) {
            this.sub_area_id = sub_area_id2;
            return this;
        }

        public Builder limit_height(Long limit_height2) {
            this.limit_height = limit_height2;
            return this;
        }

        public Builder sub_shapes(List<LimitArea> sub_shapes2) {
            Internal.checkElementsNotNull(sub_shapes2);
            this.sub_shapes = sub_shapes2;
            return this;
        }

        public Builder polygon_points(List<LocationCoordinate> polygon_points2) {
            Internal.checkElementsNotNull(polygon_points2);
            this.polygon_points = polygon_points2;
            return this;
        }

        public Builder internal_hash(Long internal_hash2) {
            this.internal_hash = internal_hash2;
            return this;
        }

        public Builder updated_time(Long updated_time2) {
            this.updated_time = updated_time2;
            return this;
        }

        public Builder begin_time(Long begin_time2) {
            this.begin_time = begin_time2;
            return this;
        }

        public Builder end_time(Long end_time2) {
            this.end_time = end_time2;
            return this;
        }

        public Builder polygon_shape(String polygon_shape2) {
            this.polygon_shape = polygon_shape2;
            return this;
        }

        public Builder is_disable(Boolean is_disable2) {
            this.is_disable = is_disable2;
            return this;
        }

        public Builder city(String city2) {
            this.city = city2;
            return this;
        }

        public Builder name(String name2) {
            this.name = name2;
            return this;
        }

        public LimitArea build() {
            return new LimitArea(this.area_id, this.search_radius, this.phone_number, this.is_unlocked, this.source, this.shape, this.type, this.latitude, this.longitude, this.radius, this.outer_radius, this.is_warning, this.level, this.country, this.point_desc, this.sub_area_id, this.limit_height, this.sub_shapes, this.polygon_points, this.internal_hash, this.updated_time, this.begin_time, this.end_time, this.polygon_shape, this.is_disable, this.city, this.name, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_LimitArea extends ProtoAdapter<LimitArea> {
        ProtoAdapter_LimitArea() {
            super(FieldEncoding.LENGTH_DELIMITED, LimitArea.class);
        }

        public int encodedSize(LimitArea value) {
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
            int i18;
            int i19;
            int i20;
            int i21;
            int i22;
            int i23;
            int i24 = 0;
            int encodedSizeWithTag = value.area_id != null ? ProtoAdapter.UINT64.encodedSizeWithTag(1, value.area_id) : 0;
            if (value.search_radius != null) {
                i = ProtoAdapter.FLOAT.encodedSizeWithTag(2, value.search_radius);
            } else {
                i = 0;
            }
            int i25 = i + encodedSizeWithTag;
            if (value.phone_number != null) {
                i2 = ProtoAdapter.STRING.encodedSizeWithTag(3, value.phone_number);
            } else {
                i2 = 0;
            }
            int i26 = i25 + i2;
            if (value.is_unlocked != null) {
                i3 = ProtoAdapter.BOOL.encodedSizeWithTag(4, value.is_unlocked);
            } else {
                i3 = 0;
            }
            int i27 = i26 + i3;
            if (value.source != null) {
                i4 = LimitAreaSource.ADAPTER.encodedSizeWithTag(5, value.source);
            } else {
                i4 = 0;
            }
            int i28 = i27 + i4;
            if (value.shape != null) {
                i5 = LimitAreaShape.ADAPTER.encodedSizeWithTag(6, value.shape);
            } else {
                i5 = 0;
            }
            int i29 = i28 + i5;
            if (value.type != null) {
                i6 = LimitAreaType.ADAPTER.encodedSizeWithTag(7, value.type);
            } else {
                i6 = 0;
            }
            int i30 = i29 + i6;
            if (value.latitude != null) {
                i7 = ProtoAdapter.DOUBLE.encodedSizeWithTag(8, value.latitude);
            } else {
                i7 = 0;
            }
            int i31 = i30 + i7;
            if (value.longitude != null) {
                i8 = ProtoAdapter.DOUBLE.encodedSizeWithTag(9, value.longitude);
            } else {
                i8 = 0;
            }
            int i32 = i31 + i8;
            if (value.radius != null) {
                i9 = ProtoAdapter.UINT32.encodedSizeWithTag(10, value.radius);
            } else {
                i9 = 0;
            }
            int i33 = i32 + i9;
            if (value.outer_radius != null) {
                i10 = ProtoAdapter.UINT32.encodedSizeWithTag(11, value.outer_radius);
            } else {
                i10 = 0;
            }
            int i34 = i33 + i10;
            if (value.is_warning != null) {
                i11 = ProtoAdapter.BOOL.encodedSizeWithTag(12, value.is_warning);
            } else {
                i11 = 0;
            }
            int i35 = i34 + i11;
            if (value.level != null) {
                i12 = LimitAreaLevel.ADAPTER.encodedSizeWithTag(13, value.level);
            } else {
                i12 = 0;
            }
            int i36 = i35 + i12;
            if (value.country != null) {
                i13 = ProtoAdapter.UINT32.encodedSizeWithTag(14, value.country);
            } else {
                i13 = 0;
            }
            int i37 = i36 + i13;
            if (value.point_desc != null) {
                i14 = ProtoAdapter.STRING.encodedSizeWithTag(15, value.point_desc);
            } else {
                i14 = 0;
            }
            int i38 = i37 + i14;
            if (value.sub_area_id != null) {
                i15 = ProtoAdapter.UINT64.encodedSizeWithTag(16, value.sub_area_id);
            } else {
                i15 = 0;
            }
            int i39 = i38 + i15;
            if (value.limit_height != null) {
                i16 = ProtoAdapter.UINT64.encodedSizeWithTag(17, value.limit_height);
            } else {
                i16 = 0;
            }
            int encodedSizeWithTag2 = LocationCoordinate.ADAPTER.asRepeated().encodedSizeWithTag(19, value.polygon_points) + i16 + i39 + LimitArea.ADAPTER.asRepeated().encodedSizeWithTag(18, value.sub_shapes);
            if (value.internal_hash != null) {
                i17 = ProtoAdapter.UINT64.encodedSizeWithTag(20, value.internal_hash);
            } else {
                i17 = 0;
            }
            int i40 = encodedSizeWithTag2 + i17;
            if (value.updated_time != null) {
                i18 = ProtoAdapter.UINT64.encodedSizeWithTag(21, value.updated_time);
            } else {
                i18 = 0;
            }
            int i41 = i40 + i18;
            if (value.begin_time != null) {
                i19 = ProtoAdapter.UINT64.encodedSizeWithTag(22, value.begin_time);
            } else {
                i19 = 0;
            }
            int i42 = i41 + i19;
            if (value.end_time != null) {
                i20 = ProtoAdapter.UINT64.encodedSizeWithTag(23, value.end_time);
            } else {
                i20 = 0;
            }
            int i43 = i42 + i20;
            if (value.polygon_shape != null) {
                i21 = ProtoAdapter.STRING.encodedSizeWithTag(24, value.polygon_shape);
            } else {
                i21 = 0;
            }
            int i44 = i43 + i21;
            if (value.is_disable != null) {
                i22 = ProtoAdapter.BOOL.encodedSizeWithTag(25, value.is_disable);
            } else {
                i22 = 0;
            }
            int i45 = i44 + i22;
            if (value.city != null) {
                i23 = ProtoAdapter.STRING.encodedSizeWithTag(26, value.city);
            } else {
                i23 = 0;
            }
            int i46 = i23 + i45;
            if (value.name != null) {
                i24 = ProtoAdapter.STRING.encodedSizeWithTag(27, value.name);
            }
            return i46 + i24 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, LimitArea value) throws IOException {
            if (value.area_id != null) {
                ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.area_id);
            }
            if (value.search_radius != null) {
                ProtoAdapter.FLOAT.encodeWithTag(writer, 2, value.search_radius);
            }
            if (value.phone_number != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 3, value.phone_number);
            }
            if (value.is_unlocked != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 4, value.is_unlocked);
            }
            if (value.source != null) {
                LimitAreaSource.ADAPTER.encodeWithTag(writer, 5, value.source);
            }
            if (value.shape != null) {
                LimitAreaShape.ADAPTER.encodeWithTag(writer, 6, value.shape);
            }
            if (value.type != null) {
                LimitAreaType.ADAPTER.encodeWithTag(writer, 7, value.type);
            }
            if (value.latitude != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 8, value.latitude);
            }
            if (value.longitude != null) {
                ProtoAdapter.DOUBLE.encodeWithTag(writer, 9, value.longitude);
            }
            if (value.radius != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 10, value.radius);
            }
            if (value.outer_radius != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 11, value.outer_radius);
            }
            if (value.is_warning != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 12, value.is_warning);
            }
            if (value.level != null) {
                LimitAreaLevel.ADAPTER.encodeWithTag(writer, 13, value.level);
            }
            if (value.country != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 14, value.country);
            }
            if (value.point_desc != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 15, value.point_desc);
            }
            if (value.sub_area_id != null) {
                ProtoAdapter.UINT64.encodeWithTag(writer, 16, value.sub_area_id);
            }
            if (value.limit_height != null) {
                ProtoAdapter.UINT64.encodeWithTag(writer, 17, value.limit_height);
            }
            LimitArea.ADAPTER.asRepeated().encodeWithTag(writer, 18, value.sub_shapes);
            LocationCoordinate.ADAPTER.asRepeated().encodeWithTag(writer, 19, value.polygon_points);
            if (value.internal_hash != null) {
                ProtoAdapter.UINT64.encodeWithTag(writer, 20, value.internal_hash);
            }
            if (value.updated_time != null) {
                ProtoAdapter.UINT64.encodeWithTag(writer, 21, value.updated_time);
            }
            if (value.begin_time != null) {
                ProtoAdapter.UINT64.encodeWithTag(writer, 22, value.begin_time);
            }
            if (value.end_time != null) {
                ProtoAdapter.UINT64.encodeWithTag(writer, 23, value.end_time);
            }
            if (value.polygon_shape != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 24, value.polygon_shape);
            }
            if (value.is_disable != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 25, value.is_disable);
            }
            if (value.city != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 26, value.city);
            }
            if (value.name != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 27, value.name);
            }
            writer.writeBytes(value.unknownFields());
        }

        public LimitArea decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.area_id(ProtoAdapter.UINT64.decode(reader));
                            break;
                        case 2:
                            builder.search_radius(ProtoAdapter.FLOAT.decode(reader));
                            break;
                        case 3:
                            builder.phone_number(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 4:
                            builder.is_unlocked(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 5:
                            try {
                                builder.source(LimitAreaSource.ADAPTER.decode(reader));
                                break;
                            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
                                builder.addUnknownField(tag, FieldEncoding.VARINT, Long.valueOf((long) e.value));
                                break;
                            }
                        case 6:
                            try {
                                builder.shape(LimitAreaShape.ADAPTER.decode(reader));
                                break;
                            } catch (ProtoAdapter.EnumConstantNotFoundException e2) {
                                builder.addUnknownField(tag, FieldEncoding.VARINT, Long.valueOf((long) e2.value));
                                break;
                            }
                        case 7:
                            try {
                                builder.type(LimitAreaType.ADAPTER.decode(reader));
                                break;
                            } catch (ProtoAdapter.EnumConstantNotFoundException e3) {
                                builder.addUnknownField(tag, FieldEncoding.VARINT, Long.valueOf((long) e3.value));
                                break;
                            }
                        case 8:
                            builder.latitude(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 9:
                            builder.longitude(ProtoAdapter.DOUBLE.decode(reader));
                            break;
                        case 10:
                            builder.radius(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 11:
                            builder.outer_radius(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 12:
                            builder.is_warning(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 13:
                            try {
                                builder.level(LimitAreaLevel.ADAPTER.decode(reader));
                                break;
                            } catch (ProtoAdapter.EnumConstantNotFoundException e4) {
                                builder.addUnknownField(tag, FieldEncoding.VARINT, Long.valueOf((long) e4.value));
                                break;
                            }
                        case 14:
                            builder.country(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 15:
                            builder.point_desc(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 16:
                            builder.sub_area_id(ProtoAdapter.UINT64.decode(reader));
                            break;
                        case 17:
                            builder.limit_height(ProtoAdapter.UINT64.decode(reader));
                            break;
                        case 18:
                            builder.sub_shapes.add(LimitArea.ADAPTER.decode(reader));
                            break;
                        case 19:
                            builder.polygon_points.add(LocationCoordinate.ADAPTER.decode(reader));
                            break;
                        case 20:
                            builder.internal_hash(ProtoAdapter.UINT64.decode(reader));
                            break;
                        case 21:
                            builder.updated_time(ProtoAdapter.UINT64.decode(reader));
                            break;
                        case 22:
                            builder.begin_time(ProtoAdapter.UINT64.decode(reader));
                            break;
                        case 23:
                            builder.end_time(ProtoAdapter.UINT64.decode(reader));
                            break;
                        case 24:
                            builder.polygon_shape(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 25:
                            builder.is_disable(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 26:
                            builder.city(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 27:
                            builder.name(ProtoAdapter.STRING.decode(reader));
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

        public LimitArea redact(LimitArea value) {
            Builder builder = value.newBuilder();
            Internal.redactElements(builder.sub_shapes, LimitArea.ADAPTER);
            Internal.redactElements(builder.polygon_points, LocationCoordinate.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
