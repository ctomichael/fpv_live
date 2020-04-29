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

public final class AppUnlockUserVerifyExtraData extends Message<AppUnlockUserVerifyExtraData, Builder> {
    public static final ProtoAdapter<AppUnlockUserVerifyExtraData> ADAPTER = new ProtoAdapter_AppUnlockUserVerifyExtraData();
    public static final String DEFAULT_MESSAGE = "";
    public static final String DEFAULT_USERID = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String message;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String userID;

    public AppUnlockUserVerifyExtraData(String message2, String userID2) {
        this(message2, userID2, ByteString.EMPTY);
    }

    public AppUnlockUserVerifyExtraData(String message2, String userID2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.message = message2;
        this.userID = userID2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.message = this.message;
        builder.userID = this.userID;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AppUnlockUserVerifyExtraData)) {
            return false;
        }
        AppUnlockUserVerifyExtraData o = (AppUnlockUserVerifyExtraData) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.message, o.message) || !Internal.equals(this.userID, o.userID)) {
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
        if (this.message != null) {
            i = this.message.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.userID != null) {
            i2 = this.userID.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.message != null) {
            builder.append(", message=").append(this.message);
        }
        if (this.userID != null) {
            builder.append(", userID=").append(this.userID);
        }
        return builder.replace(0, 2, "AppUnlockUserVerifyExtraData{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<AppUnlockUserVerifyExtraData, Builder> {
        public String message;
        public String userID;

        public Builder message(String message2) {
            this.message = message2;
            return this;
        }

        public Builder userID(String userID2) {
            this.userID = userID2;
            return this;
        }

        public AppUnlockUserVerifyExtraData build() {
            return new AppUnlockUserVerifyExtraData(this.message, this.userID, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_AppUnlockUserVerifyExtraData extends ProtoAdapter<AppUnlockUserVerifyExtraData> {
        ProtoAdapter_AppUnlockUserVerifyExtraData() {
            super(FieldEncoding.LENGTH_DELIMITED, AppUnlockUserVerifyExtraData.class);
        }

        public int encodedSize(AppUnlockUserVerifyExtraData value) {
            int i = 0;
            int encodedSizeWithTag = value.message != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.message) : 0;
            if (value.userID != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.userID);
            }
            return encodedSizeWithTag + i + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, AppUnlockUserVerifyExtraData value) throws IOException {
            if (value.message != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.message);
            }
            if (value.userID != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.userID);
            }
            writer.writeBytes(value.unknownFields());
        }

        public AppUnlockUserVerifyExtraData decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.message(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.userID(ProtoAdapter.STRING.decode(reader));
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

        public AppUnlockUserVerifyExtraData redact(AppUnlockUserVerifyExtraData value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
