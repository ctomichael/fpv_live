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

public final class V1WhiteListLicenseInfo extends Message<V1WhiteListLicenseInfo, Builder> {
    public static final ProtoAdapter<V1WhiteListLicenseInfo> ADAPTER = new ProtoAdapter_V1WhiteListLicenseInfo();
    public static final String DEFAULT_CONTAIN_SNS = "";
    public static final String DEFAULT_CREATE_TIME = "";
    public static final String DEFAULT_LICENSE_DATA = "";
    public static final String DEFAULT_LICENSE_KEY = "";
    public static final String DEFAULT_MAIN_KEY = "";
    public static final String DEFAULT_NAME = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 4)
    public final String contain_sns;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String create_time;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 3)
    public final String license_data;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 6)
    public final String license_key;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 5)
    public final String main_key;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String name;

    public V1WhiteListLicenseInfo(String name2, String create_time2, String license_data2, String contain_sns2, String main_key2, String license_key2) {
        this(name2, create_time2, license_data2, contain_sns2, main_key2, license_key2, ByteString.EMPTY);
    }

    public V1WhiteListLicenseInfo(String name2, String create_time2, String license_data2, String contain_sns2, String main_key2, String license_key2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.name = name2;
        this.create_time = create_time2;
        this.license_data = license_data2;
        this.contain_sns = contain_sns2;
        this.main_key = main_key2;
        this.license_key = license_key2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.name = this.name;
        builder.create_time = this.create_time;
        builder.license_data = this.license_data;
        builder.contain_sns = this.contain_sns;
        builder.main_key = this.main_key;
        builder.license_key = this.license_key;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof V1WhiteListLicenseInfo)) {
            return false;
        }
        V1WhiteListLicenseInfo o = (V1WhiteListLicenseInfo) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.name, o.name) || !Internal.equals(this.create_time, o.create_time) || !Internal.equals(this.license_data, o.license_data) || !Internal.equals(this.contain_sns, o.contain_sns) || !Internal.equals(this.main_key, o.main_key) || !Internal.equals(this.license_key, o.license_key)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.name != null ? this.name.hashCode() : 0)) * 37;
        if (this.create_time != null) {
            i = this.create_time.hashCode();
        } else {
            i = 0;
        }
        int i6 = (hashCode + i) * 37;
        if (this.license_data != null) {
            i2 = this.license_data.hashCode();
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 37;
        if (this.contain_sns != null) {
            i3 = this.contain_sns.hashCode();
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 37;
        if (this.main_key != null) {
            i4 = this.main_key.hashCode();
        } else {
            i4 = 0;
        }
        int i9 = (i8 + i4) * 37;
        if (this.license_key != null) {
            i5 = this.license_key.hashCode();
        }
        int result2 = i9 + i5;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.name != null) {
            builder.append(", name=").append(this.name);
        }
        if (this.create_time != null) {
            builder.append(", create_time=").append(this.create_time);
        }
        if (this.license_data != null) {
            builder.append(", license_data=").append(this.license_data);
        }
        if (this.contain_sns != null) {
            builder.append(", contain_sns=").append(this.contain_sns);
        }
        if (this.main_key != null) {
            builder.append(", main_key=").append(this.main_key);
        }
        if (this.license_key != null) {
            builder.append(", license_key=").append(this.license_key);
        }
        return builder.replace(0, 2, "V1WhiteListLicenseInfo{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<V1WhiteListLicenseInfo, Builder> {
        public String contain_sns;
        public String create_time;
        public String license_data;
        public String license_key;
        public String main_key;
        public String name;

        public Builder name(String name2) {
            this.name = name2;
            return this;
        }

        public Builder create_time(String create_time2) {
            this.create_time = create_time2;
            return this;
        }

        public Builder license_data(String license_data2) {
            this.license_data = license_data2;
            return this;
        }

        public Builder contain_sns(String contain_sns2) {
            this.contain_sns = contain_sns2;
            return this;
        }

        public Builder main_key(String main_key2) {
            this.main_key = main_key2;
            return this;
        }

        public Builder license_key(String license_key2) {
            this.license_key = license_key2;
            return this;
        }

        public V1WhiteListLicenseInfo build() {
            return new V1WhiteListLicenseInfo(this.name, this.create_time, this.license_data, this.contain_sns, this.main_key, this.license_key, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_V1WhiteListLicenseInfo extends ProtoAdapter<V1WhiteListLicenseInfo> {
        ProtoAdapter_V1WhiteListLicenseInfo() {
            super(FieldEncoding.LENGTH_DELIMITED, V1WhiteListLicenseInfo.class);
        }

        public int encodedSize(V1WhiteListLicenseInfo value) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5 = 0;
            int encodedSizeWithTag = value.name != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.name) : 0;
            if (value.create_time != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.create_time);
            } else {
                i = 0;
            }
            int i6 = i + encodedSizeWithTag;
            if (value.license_data != null) {
                i2 = ProtoAdapter.STRING.encodedSizeWithTag(3, value.license_data);
            } else {
                i2 = 0;
            }
            int i7 = i6 + i2;
            if (value.contain_sns != null) {
                i3 = ProtoAdapter.STRING.encodedSizeWithTag(4, value.contain_sns);
            } else {
                i3 = 0;
            }
            int i8 = i7 + i3;
            if (value.main_key != null) {
                i4 = ProtoAdapter.STRING.encodedSizeWithTag(5, value.main_key);
            } else {
                i4 = 0;
            }
            int i9 = i4 + i8;
            if (value.license_key != null) {
                i5 = ProtoAdapter.STRING.encodedSizeWithTag(6, value.license_key);
            }
            return i9 + i5 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, V1WhiteListLicenseInfo value) throws IOException {
            if (value.name != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.name);
            }
            if (value.create_time != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.create_time);
            }
            if (value.license_data != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 3, value.license_data);
            }
            if (value.contain_sns != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 4, value.contain_sns);
            }
            if (value.main_key != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 5, value.main_key);
            }
            if (value.license_key != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 6, value.license_key);
            }
            writer.writeBytes(value.unknownFields());
        }

        public V1WhiteListLicenseInfo decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.name(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.create_time(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            builder.license_data(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 4:
                            builder.contain_sns(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 5:
                            builder.main_key(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 6:
                            builder.license_key(ProtoAdapter.STRING.decode(reader));
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

        public V1WhiteListLicenseInfo redact(V1WhiteListLicenseInfo value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
