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

public final class ShowSpecialUnlockAreaContext extends Message<ShowSpecialUnlockAreaContext, Builder> {
    public static final ProtoAdapter<ShowSpecialUnlockAreaContext> ADAPTER = new ProtoAdapter_ShowSpecialUnlockAreaContext();
    public static final Integer DEFAULT_CIRCLE_RADIUS = 0;
    public static final Integer DEFAULT_END_DATE = 0;
    public static final Boolean DEFAULT_IS_SPECIAL_UNLOCK_VALID = false;
    public static final Integer DEFAULT_START_DATE = 0;
    public static final SpecialUnlockType DEFAULT_UNLOCK_TYPE = SpecialUnlockType.InvalidUnlock;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "dji.flysafe.LocationCoordinate#ADAPTER", tag = 4)
    public final LocationCoordinate circle_center;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 5)
    public final Integer circle_radius;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 8)
    public final Integer end_date;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 2)
    public final Boolean is_special_unlock_valid;
    @WireField(adapter = "dji.flysafe.LocationCoordinate#ADAPTER", label = WireField.Label.REPEATED, tag = 6)
    public final List<LocationCoordinate> polygon_points;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 7)
    public final Integer start_date;
    @WireField(adapter = "dji.flysafe.SpecialUnlockType#ADAPTER", tag = 3)
    public final SpecialUnlockType unlock_type;

    public ShowSpecialUnlockAreaContext(Boolean is_special_unlock_valid2, SpecialUnlockType unlock_type2, LocationCoordinate circle_center2, Integer circle_radius2, List<LocationCoordinate> polygon_points2, Integer start_date2, Integer end_date2) {
        this(is_special_unlock_valid2, unlock_type2, circle_center2, circle_radius2, polygon_points2, start_date2, end_date2, ByteString.EMPTY);
    }

    public ShowSpecialUnlockAreaContext(Boolean is_special_unlock_valid2, SpecialUnlockType unlock_type2, LocationCoordinate circle_center2, Integer circle_radius2, List<LocationCoordinate> polygon_points2, Integer start_date2, Integer end_date2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.is_special_unlock_valid = is_special_unlock_valid2;
        this.unlock_type = unlock_type2;
        this.circle_center = circle_center2;
        this.circle_radius = circle_radius2;
        this.polygon_points = Internal.immutableCopyOf("polygon_points", polygon_points2);
        this.start_date = start_date2;
        this.end_date = end_date2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.is_special_unlock_valid = this.is_special_unlock_valid;
        builder.unlock_type = this.unlock_type;
        builder.circle_center = this.circle_center;
        builder.circle_radius = this.circle_radius;
        builder.polygon_points = Internal.copyOf("polygon_points", this.polygon_points);
        builder.start_date = this.start_date;
        builder.end_date = this.end_date;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ShowSpecialUnlockAreaContext)) {
            return false;
        }
        ShowSpecialUnlockAreaContext o = (ShowSpecialUnlockAreaContext) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.is_special_unlock_valid, o.is_special_unlock_valid) || !Internal.equals(this.unlock_type, o.unlock_type) || !Internal.equals(this.circle_center, o.circle_center) || !Internal.equals(this.circle_radius, o.circle_radius) || !this.polygon_points.equals(o.polygon_points) || !Internal.equals(this.start_date, o.start_date) || !Internal.equals(this.end_date, o.end_date)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.is_special_unlock_valid != null ? this.is_special_unlock_valid.hashCode() : 0)) * 37;
        if (this.unlock_type != null) {
            i = this.unlock_type.hashCode();
        } else {
            i = 0;
        }
        int i6 = (hashCode + i) * 37;
        if (this.circle_center != null) {
            i2 = this.circle_center.hashCode();
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 37;
        if (this.circle_radius != null) {
            i3 = this.circle_radius.hashCode();
        } else {
            i3 = 0;
        }
        int hashCode2 = (((i7 + i3) * 37) + this.polygon_points.hashCode()) * 37;
        if (this.start_date != null) {
            i4 = this.start_date.hashCode();
        } else {
            i4 = 0;
        }
        int i8 = (hashCode2 + i4) * 37;
        if (this.end_date != null) {
            i5 = this.end_date.hashCode();
        }
        int result2 = i8 + i5;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.is_special_unlock_valid != null) {
            builder.append(", is_special_unlock_valid=").append(this.is_special_unlock_valid);
        }
        if (this.unlock_type != null) {
            builder.append(", unlock_type=").append(this.unlock_type);
        }
        if (this.circle_center != null) {
            builder.append(", circle_center=").append(this.circle_center);
        }
        if (this.circle_radius != null) {
            builder.append(", circle_radius=").append(this.circle_radius);
        }
        if (!this.polygon_points.isEmpty()) {
            builder.append(", polygon_points=").append(this.polygon_points);
        }
        if (this.start_date != null) {
            builder.append(", start_date=").append(this.start_date);
        }
        if (this.end_date != null) {
            builder.append(", end_date=").append(this.end_date);
        }
        return builder.replace(0, 2, "ShowSpecialUnlockAreaContext{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<ShowSpecialUnlockAreaContext, Builder> {
        public LocationCoordinate circle_center;
        public Integer circle_radius;
        public Integer end_date;
        public Boolean is_special_unlock_valid;
        public List<LocationCoordinate> polygon_points = Internal.newMutableList();
        public Integer start_date;
        public SpecialUnlockType unlock_type;

        public Builder is_special_unlock_valid(Boolean is_special_unlock_valid2) {
            this.is_special_unlock_valid = is_special_unlock_valid2;
            return this;
        }

        public Builder unlock_type(SpecialUnlockType unlock_type2) {
            this.unlock_type = unlock_type2;
            return this;
        }

        public Builder circle_center(LocationCoordinate circle_center2) {
            this.circle_center = circle_center2;
            return this;
        }

        public Builder circle_radius(Integer circle_radius2) {
            this.circle_radius = circle_radius2;
            return this;
        }

        public Builder polygon_points(List<LocationCoordinate> polygon_points2) {
            Internal.checkElementsNotNull(polygon_points2);
            this.polygon_points = polygon_points2;
            return this;
        }

        public Builder start_date(Integer start_date2) {
            this.start_date = start_date2;
            return this;
        }

        public Builder end_date(Integer end_date2) {
            this.end_date = end_date2;
            return this;
        }

        public ShowSpecialUnlockAreaContext build() {
            return new ShowSpecialUnlockAreaContext(this.is_special_unlock_valid, this.unlock_type, this.circle_center, this.circle_radius, this.polygon_points, this.start_date, this.end_date, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_ShowSpecialUnlockAreaContext extends ProtoAdapter<ShowSpecialUnlockAreaContext> {
        ProtoAdapter_ShowSpecialUnlockAreaContext() {
            super(FieldEncoding.LENGTH_DELIMITED, ShowSpecialUnlockAreaContext.class);
        }

        public int encodedSize(ShowSpecialUnlockAreaContext value) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5 = 0;
            int encodedSizeWithTag = value.is_special_unlock_valid != null ? ProtoAdapter.BOOL.encodedSizeWithTag(2, value.is_special_unlock_valid) : 0;
            if (value.unlock_type != null) {
                i = SpecialUnlockType.ADAPTER.encodedSizeWithTag(3, value.unlock_type);
            } else {
                i = 0;
            }
            int i6 = i + encodedSizeWithTag;
            if (value.circle_center != null) {
                i2 = LocationCoordinate.ADAPTER.encodedSizeWithTag(4, value.circle_center);
            } else {
                i2 = 0;
            }
            int i7 = i6 + i2;
            if (value.circle_radius != null) {
                i3 = ProtoAdapter.UINT32.encodedSizeWithTag(5, value.circle_radius);
            } else {
                i3 = 0;
            }
            int encodedSizeWithTag2 = LocationCoordinate.ADAPTER.asRepeated().encodedSizeWithTag(6, value.polygon_points) + i3 + i7;
            if (value.start_date != null) {
                i4 = ProtoAdapter.UINT32.encodedSizeWithTag(7, value.start_date);
            } else {
                i4 = 0;
            }
            int i8 = i4 + encodedSizeWithTag2;
            if (value.end_date != null) {
                i5 = ProtoAdapter.UINT32.encodedSizeWithTag(8, value.end_date);
            }
            return i8 + i5 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, ShowSpecialUnlockAreaContext value) throws IOException {
            if (value.is_special_unlock_valid != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 2, value.is_special_unlock_valid);
            }
            if (value.unlock_type != null) {
                SpecialUnlockType.ADAPTER.encodeWithTag(writer, 3, value.unlock_type);
            }
            if (value.circle_center != null) {
                LocationCoordinate.ADAPTER.encodeWithTag(writer, 4, value.circle_center);
            }
            if (value.circle_radius != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.circle_radius);
            }
            LocationCoordinate.ADAPTER.asRepeated().encodeWithTag(writer, 6, value.polygon_points);
            if (value.start_date != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 7, value.start_date);
            }
            if (value.end_date != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 8, value.end_date);
            }
            writer.writeBytes(value.unknownFields());
        }

        public ShowSpecialUnlockAreaContext decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 2:
                            builder.is_special_unlock_valid(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 3:
                            try {
                                builder.unlock_type(SpecialUnlockType.ADAPTER.decode(reader));
                                break;
                            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
                                builder.addUnknownField(tag, FieldEncoding.VARINT, Long.valueOf((long) e.value));
                                break;
                            }
                        case 4:
                            builder.circle_center(LocationCoordinate.ADAPTER.decode(reader));
                            break;
                        case 5:
                            builder.circle_radius(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 6:
                            builder.polygon_points.add(LocationCoordinate.ADAPTER.decode(reader));
                            break;
                        case 7:
                            builder.start_date(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 8:
                            builder.end_date(ProtoAdapter.UINT32.decode(reader));
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

        public ShowSpecialUnlockAreaContext redact(ShowSpecialUnlockAreaContext value) {
            Builder builder = value.newBuilder();
            if (builder.circle_center != null) {
                builder.circle_center = LocationCoordinate.ADAPTER.redact(builder.circle_center);
            }
            Internal.redactElements(builder.polygon_points, LocationCoordinate.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
