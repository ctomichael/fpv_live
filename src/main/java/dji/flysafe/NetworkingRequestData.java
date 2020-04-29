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

public final class NetworkingRequestData extends Message<NetworkingRequestData, Builder> {
    public static final ProtoAdapter<NetworkingRequestData> ADAPTER = new ProtoAdapter_NetworkingRequestData();
    public static final String DEFAULT_REQUEST_API = "";
    public static final String DEFAULT_REQUEST_METHOD = "";
    public static final String DEFAULT_REQUEST_PARAMS_STRING = "";
    public static final String DEFAULT_REQUEST_SIGNATURE = "";
    public static final Boolean DEFAULT_REQUEST_USED_FORM = false;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String request_api;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 4)
    public final String request_method;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String request_params_string;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 3)
    public final String request_signature;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 5)
    public final Boolean request_used_form;

    public NetworkingRequestData(String request_params_string2, String request_api2, String request_signature2, String request_method2, Boolean request_used_form2) {
        this(request_params_string2, request_api2, request_signature2, request_method2, request_used_form2, ByteString.EMPTY);
    }

    public NetworkingRequestData(String request_params_string2, String request_api2, String request_signature2, String request_method2, Boolean request_used_form2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.request_params_string = request_params_string2;
        this.request_api = request_api2;
        this.request_signature = request_signature2;
        this.request_method = request_method2;
        this.request_used_form = request_used_form2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.request_params_string = this.request_params_string;
        builder.request_api = this.request_api;
        builder.request_signature = this.request_signature;
        builder.request_method = this.request_method;
        builder.request_used_form = this.request_used_form;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof NetworkingRequestData)) {
            return false;
        }
        NetworkingRequestData o = (NetworkingRequestData) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.request_params_string, o.request_params_string) || !Internal.equals(this.request_api, o.request_api) || !Internal.equals(this.request_signature, o.request_signature) || !Internal.equals(this.request_method, o.request_method) || !Internal.equals(this.request_used_form, o.request_used_form)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.request_params_string != null ? this.request_params_string.hashCode() : 0)) * 37;
        if (this.request_api != null) {
            i = this.request_api.hashCode();
        } else {
            i = 0;
        }
        int i5 = (hashCode + i) * 37;
        if (this.request_signature != null) {
            i2 = this.request_signature.hashCode();
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 37;
        if (this.request_method != null) {
            i3 = this.request_method.hashCode();
        } else {
            i3 = 0;
        }
        int i7 = (i6 + i3) * 37;
        if (this.request_used_form != null) {
            i4 = this.request_used_form.hashCode();
        }
        int result2 = i7 + i4;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.request_params_string != null) {
            builder.append(", request_params_string=").append(this.request_params_string);
        }
        if (this.request_api != null) {
            builder.append(", request_api=").append(this.request_api);
        }
        if (this.request_signature != null) {
            builder.append(", request_signature=").append(this.request_signature);
        }
        if (this.request_method != null) {
            builder.append(", request_method=").append(this.request_method);
        }
        if (this.request_used_form != null) {
            builder.append(", request_used_form=").append(this.request_used_form);
        }
        return builder.replace(0, 2, "NetworkingRequestData{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<NetworkingRequestData, Builder> {
        public String request_api;
        public String request_method;
        public String request_params_string;
        public String request_signature;
        public Boolean request_used_form;

        public Builder request_params_string(String request_params_string2) {
            this.request_params_string = request_params_string2;
            return this;
        }

        public Builder request_api(String request_api2) {
            this.request_api = request_api2;
            return this;
        }

        public Builder request_signature(String request_signature2) {
            this.request_signature = request_signature2;
            return this;
        }

        public Builder request_method(String request_method2) {
            this.request_method = request_method2;
            return this;
        }

        public Builder request_used_form(Boolean request_used_form2) {
            this.request_used_form = request_used_form2;
            return this;
        }

        public NetworkingRequestData build() {
            return new NetworkingRequestData(this.request_params_string, this.request_api, this.request_signature, this.request_method, this.request_used_form, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_NetworkingRequestData extends ProtoAdapter<NetworkingRequestData> {
        ProtoAdapter_NetworkingRequestData() {
            super(FieldEncoding.LENGTH_DELIMITED, NetworkingRequestData.class);
        }

        public int encodedSize(NetworkingRequestData value) {
            int i;
            int i2;
            int i3;
            int i4 = 0;
            int encodedSizeWithTag = value.request_params_string != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.request_params_string) : 0;
            if (value.request_api != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.request_api);
            } else {
                i = 0;
            }
            int i5 = i + encodedSizeWithTag;
            if (value.request_signature != null) {
                i2 = ProtoAdapter.STRING.encodedSizeWithTag(3, value.request_signature);
            } else {
                i2 = 0;
            }
            int i6 = i5 + i2;
            if (value.request_method != null) {
                i3 = ProtoAdapter.STRING.encodedSizeWithTag(4, value.request_method);
            } else {
                i3 = 0;
            }
            int i7 = i3 + i6;
            if (value.request_used_form != null) {
                i4 = ProtoAdapter.BOOL.encodedSizeWithTag(5, value.request_used_form);
            }
            return i7 + i4 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, NetworkingRequestData value) throws IOException {
            if (value.request_params_string != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.request_params_string);
            }
            if (value.request_api != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.request_api);
            }
            if (value.request_signature != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 3, value.request_signature);
            }
            if (value.request_method != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 4, value.request_method);
            }
            if (value.request_used_form != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 5, value.request_used_form);
            }
            writer.writeBytes(value.unknownFields());
        }

        public NetworkingRequestData decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.request_params_string(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.request_api(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            builder.request_signature(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 4:
                            builder.request_method(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 5:
                            builder.request_used_form(ProtoAdapter.BOOL.decode(reader));
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

        public NetworkingRequestData redact(NetworkingRequestData value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
