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

public final class V1WhiteListUnlockLicenseResponseModel extends Message<V1WhiteListUnlockLicenseResponseModel, Builder> {
    public static final ProtoAdapter<V1WhiteListUnlockLicenseResponseModel> ADAPTER = new ProtoAdapter_V1WhiteListUnlockLicenseResponseModel();
    private static final long serialVersionUID = 0;
    @WireField(adapter = "dji.flysafe.V1WhiteListUnlockLicense#ADAPTER", label = WireField.Label.REPEATED, tag = 1)
    public final List<V1WhiteListUnlockLicense> whiteListUnlockLicenses;

    public V1WhiteListUnlockLicenseResponseModel(List<V1WhiteListUnlockLicense> whiteListUnlockLicenses2) {
        this(whiteListUnlockLicenses2, ByteString.EMPTY);
    }

    public V1WhiteListUnlockLicenseResponseModel(List<V1WhiteListUnlockLicense> whiteListUnlockLicenses2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.whiteListUnlockLicenses = Internal.immutableCopyOf("whiteListUnlockLicenses", whiteListUnlockLicenses2);
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.whiteListUnlockLicenses = Internal.copyOf("whiteListUnlockLicenses", this.whiteListUnlockLicenses);
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof V1WhiteListUnlockLicenseResponseModel)) {
            return false;
        }
        V1WhiteListUnlockLicenseResponseModel o = (V1WhiteListUnlockLicenseResponseModel) other;
        if (!unknownFields().equals(o.unknownFields()) || !this.whiteListUnlockLicenses.equals(o.whiteListUnlockLicenses)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int result2 = (unknownFields().hashCode() * 37) + this.whiteListUnlockLicenses.hashCode();
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!this.whiteListUnlockLicenses.isEmpty()) {
            builder.append(", whiteListUnlockLicenses=").append(this.whiteListUnlockLicenses);
        }
        return builder.replace(0, 2, "V1WhiteListUnlockLicenseResponseModel{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<V1WhiteListUnlockLicenseResponseModel, Builder> {
        public List<V1WhiteListUnlockLicense> whiteListUnlockLicenses = Internal.newMutableList();

        public Builder whiteListUnlockLicenses(List<V1WhiteListUnlockLicense> whiteListUnlockLicenses2) {
            Internal.checkElementsNotNull(whiteListUnlockLicenses2);
            this.whiteListUnlockLicenses = whiteListUnlockLicenses2;
            return this;
        }

        public V1WhiteListUnlockLicenseResponseModel build() {
            return new V1WhiteListUnlockLicenseResponseModel(this.whiteListUnlockLicenses, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_V1WhiteListUnlockLicenseResponseModel extends ProtoAdapter<V1WhiteListUnlockLicenseResponseModel> {
        ProtoAdapter_V1WhiteListUnlockLicenseResponseModel() {
            super(FieldEncoding.LENGTH_DELIMITED, V1WhiteListUnlockLicenseResponseModel.class);
        }

        public int encodedSize(V1WhiteListUnlockLicenseResponseModel value) {
            return V1WhiteListUnlockLicense.ADAPTER.asRepeated().encodedSizeWithTag(1, value.whiteListUnlockLicenses) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, V1WhiteListUnlockLicenseResponseModel value) throws IOException {
            V1WhiteListUnlockLicense.ADAPTER.asRepeated().encodeWithTag(writer, 1, value.whiteListUnlockLicenses);
            writer.writeBytes(value.unknownFields());
        }

        public V1WhiteListUnlockLicenseResponseModel decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.whiteListUnlockLicenses.add(V1WhiteListUnlockLicense.ADAPTER.decode(reader));
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

        public V1WhiteListUnlockLicenseResponseModel redact(V1WhiteListUnlockLicenseResponseModel value) {
            Builder builder = value.newBuilder();
            Internal.redactElements(builder.whiteListUnlockLicenses, V1WhiteListUnlockLicense.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
