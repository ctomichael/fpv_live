package com.dji.proto.demo;

import com.dji.fieldAnnotation.EXClassNullAway;
import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import okio.ByteString;

@EXClassNullAway
public final class DemoLatLngModel extends Message<DemoLatLngModel, Builder> {
    public static final ProtoAdapter<DemoLatLngModel> ADAPTER = new ProtoAdapter_DemoLatLngModel();
    public static final Integer DEFAULT_LAT = 0;
    public static final Integer DEFAULT_LNG = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 1)
    public final Integer lat;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 2)
    public final Integer lng;

    public DemoLatLngModel(Integer lat2, Integer lng2) {
        this(lat2, lng2, ByteString.EMPTY);
    }

    public DemoLatLngModel(Integer lat2, Integer lng2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.lat = lat2;
        this.lng = lng2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.lat = this.lat;
        builder.lng = this.lng;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DemoLatLngModel)) {
            return false;
        }
        DemoLatLngModel o = (DemoLatLngModel) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.lat, o.lat) || !Internal.equals(this.lng, o.lng)) {
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
        if (this.lat != null) {
            i = this.lat.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.lng != null) {
            i2 = this.lng.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.lat != null) {
            builder.append(", lat=").append(this.lat);
        }
        if (this.lng != null) {
            builder.append(", lng=").append(this.lng);
        }
        return builder.replace(0, 2, "DemoLatLngModel{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<DemoLatLngModel, Builder> {
        public Integer lat;
        public Integer lng;

        public Builder lat(Integer lat2) {
            this.lat = lat2;
            return this;
        }

        public Builder lng(Integer lng2) {
            this.lng = lng2;
            return this;
        }

        public DemoLatLngModel build() {
            return new DemoLatLngModel(this.lat, this.lng, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_DemoLatLngModel extends ProtoAdapter<DemoLatLngModel> {
        ProtoAdapter_DemoLatLngModel() {
            super(FieldEncoding.LENGTH_DELIMITED, DemoLatLngModel.class);
        }

        public int encodedSize(DemoLatLngModel value) {
            int i = 0;
            int encodedSizeWithTag = value.lat != null ? ProtoAdapter.INT32.encodedSizeWithTag(1, value.lat) : 0;
            if (value.lng != null) {
                i = ProtoAdapter.INT32.encodedSizeWithTag(2, value.lng);
            }
            return encodedSizeWithTag + i + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, DemoLatLngModel value) throws IOException {
            if (value.lat != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 1, value.lat);
            }
            if (value.lng != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 2, value.lng);
            }
            writer.writeBytes(value.unknownFields());
        }

        public DemoLatLngModel decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.lat(ProtoAdapter.INT32.decode(reader));
                            break;
                        case 2:
                            builder.lng(ProtoAdapter.INT32.decode(reader));
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

        public DemoLatLngModel redact(DemoLatLngModel value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
