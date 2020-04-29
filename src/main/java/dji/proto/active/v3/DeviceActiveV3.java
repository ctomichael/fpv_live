package dji.proto.active.v3;

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

public final class DeviceActiveV3 extends Message<DeviceActiveV3, Builder> {
    public static final ProtoAdapter<DeviceActiveV3> ADAPTER = new ProtoAdapter_DeviceActiveV3();
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BYTES", label = WireField.Label.REPEATED, tag = 1)
    public final List<ByteString> contents;

    public DeviceActiveV3(List<ByteString> contents2) {
        this(contents2, ByteString.EMPTY);
    }

    public DeviceActiveV3(List<ByteString> contents2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.contents = Internal.immutableCopyOf("contents", contents2);
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.contents = Internal.copyOf("contents", this.contents);
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeviceActiveV3)) {
            return false;
        }
        DeviceActiveV3 o = (DeviceActiveV3) other;
        if (!unknownFields().equals(o.unknownFields()) || !this.contents.equals(o.contents)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int result2 = (unknownFields().hashCode() * 37) + this.contents.hashCode();
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!this.contents.isEmpty()) {
            builder.append(", contents=").append(this.contents);
        }
        return builder.replace(0, 2, "DeviceActiveV3{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<DeviceActiveV3, Builder> {
        public List<ByteString> contents = Internal.newMutableList();

        public Builder contents(List<ByteString> contents2) {
            Internal.checkElementsNotNull(contents2);
            this.contents = contents2;
            return this;
        }

        public DeviceActiveV3 build() {
            return new DeviceActiveV3(this.contents, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_DeviceActiveV3 extends ProtoAdapter<DeviceActiveV3> {
        ProtoAdapter_DeviceActiveV3() {
            super(FieldEncoding.LENGTH_DELIMITED, DeviceActiveV3.class);
        }

        public int encodedSize(DeviceActiveV3 value) {
            return ProtoAdapter.BYTES.asRepeated().encodedSizeWithTag(1, value.contents) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, DeviceActiveV3 value) throws IOException {
            ProtoAdapter.BYTES.asRepeated().encodeWithTag(writer, 1, value.contents);
            writer.writeBytes(value.unknownFields());
        }

        public DeviceActiveV3 decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.contents.add(ProtoAdapter.BYTES.decode(reader));
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

        public DeviceActiveV3 redact(DeviceActiveV3 value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
