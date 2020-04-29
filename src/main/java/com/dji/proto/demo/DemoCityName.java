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
public final class DemoCityName extends Message<DemoCityName, Builder> {
    public static final ProtoAdapter<DemoCityName> ADAPTER = new ProtoAdapter_DemoCityName();
    public static final String DEFAULT_CITY = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String city;

    public DemoCityName(String city2) {
        this(city2, ByteString.EMPTY);
    }

    public DemoCityName(String city2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.city = city2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.city = this.city;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DemoCityName)) {
            return false;
        }
        DemoCityName o = (DemoCityName) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.city, o.city)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int result2 = (unknownFields().hashCode() * 37) + (this.city != null ? this.city.hashCode() : 0);
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.city != null) {
            builder.append(", city=").append(this.city);
        }
        return builder.replace(0, 2, "DemoCityName{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<DemoCityName, Builder> {
        public String city;

        public Builder city(String city2) {
            this.city = city2;
            return this;
        }

        public DemoCityName build() {
            return new DemoCityName(this.city, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_DemoCityName extends ProtoAdapter<DemoCityName> {
        ProtoAdapter_DemoCityName() {
            super(FieldEncoding.LENGTH_DELIMITED, DemoCityName.class);
        }

        public int encodedSize(DemoCityName value) {
            return (value.city != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.city) : 0) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, DemoCityName value) throws IOException {
            if (value.city != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.city);
            }
            writer.writeBytes(value.unknownFields());
        }

        public DemoCityName decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.city(ProtoAdapter.STRING.decode(reader));
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

        public DemoCityName redact(DemoCityName value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
