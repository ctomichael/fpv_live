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

public final class DeviceActiveWithMetaInfoV3 extends Message<DeviceActiveWithMetaInfoV3, Builder> {
    public static final ProtoAdapter<DeviceActiveWithMetaInfoV3> ADAPTER = new ProtoAdapter_DeviceActiveWithMetaInfoV3();
    public static final String DEFAULT_APP_VERSION = "";
    public static final String DEFAULT_DEVICE_NICKNAME = "";
    public static final String DEFAULT_FIRMWARE_VERSION = "";
    public static final String DEFAULT_GPS = "";
    public static final Integer DEFAULT_PRODUCT_TYPE = 0;
    public static final Integer DEFAULT_SUB_PRODUCT_TYPE = 0;
    public static final String DEFAULT_UID = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 6)
    public final String app_version;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BYTES", label = WireField.Label.REPEATED, tag = 1)
    public final List<ByteString> contents;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 7)
    public final String device_nickname;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 5)
    public final String firmware_version;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 8)
    public final String gps;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 3)
    public final Integer product_type;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 4)
    public final Integer sub_product_type;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String uid;

    public DeviceActiveWithMetaInfoV3(List<ByteString> contents2, String uid2, Integer product_type2, Integer sub_product_type2, String firmware_version2, String app_version2, String device_nickname2, String gps2) {
        this(contents2, uid2, product_type2, sub_product_type2, firmware_version2, app_version2, device_nickname2, gps2, ByteString.EMPTY);
    }

    public DeviceActiveWithMetaInfoV3(List<ByteString> contents2, String uid2, Integer product_type2, Integer sub_product_type2, String firmware_version2, String app_version2, String device_nickname2, String gps2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.contents = Internal.immutableCopyOf("contents", contents2);
        this.uid = uid2;
        this.product_type = product_type2;
        this.sub_product_type = sub_product_type2;
        this.firmware_version = firmware_version2;
        this.app_version = app_version2;
        this.device_nickname = device_nickname2;
        this.gps = gps2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.contents = Internal.copyOf("contents", this.contents);
        builder.uid = this.uid;
        builder.product_type = this.product_type;
        builder.sub_product_type = this.sub_product_type;
        builder.firmware_version = this.firmware_version;
        builder.app_version = this.app_version;
        builder.device_nickname = this.device_nickname;
        builder.gps = this.gps;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeviceActiveWithMetaInfoV3)) {
            return false;
        }
        DeviceActiveWithMetaInfoV3 o = (DeviceActiveWithMetaInfoV3) other;
        if (!unknownFields().equals(o.unknownFields()) || !this.contents.equals(o.contents) || !Internal.equals(this.uid, o.uid) || !Internal.equals(this.product_type, o.product_type) || !Internal.equals(this.sub_product_type, o.sub_product_type) || !Internal.equals(this.firmware_version, o.firmware_version) || !Internal.equals(this.app_version, o.app_version) || !Internal.equals(this.device_nickname, o.device_nickname) || !Internal.equals(this.gps, o.gps)) {
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
        int i6 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((((unknownFields().hashCode() * 37) + this.contents.hashCode()) * 37) + (this.uid != null ? this.uid.hashCode() : 0)) * 37;
        if (this.product_type != null) {
            i = this.product_type.hashCode();
        } else {
            i = 0;
        }
        int i7 = (hashCode + i) * 37;
        if (this.sub_product_type != null) {
            i2 = this.sub_product_type.hashCode();
        } else {
            i2 = 0;
        }
        int i8 = (i7 + i2) * 37;
        if (this.firmware_version != null) {
            i3 = this.firmware_version.hashCode();
        } else {
            i3 = 0;
        }
        int i9 = (i8 + i3) * 37;
        if (this.app_version != null) {
            i4 = this.app_version.hashCode();
        } else {
            i4 = 0;
        }
        int i10 = (i9 + i4) * 37;
        if (this.device_nickname != null) {
            i5 = this.device_nickname.hashCode();
        } else {
            i5 = 0;
        }
        int i11 = (i10 + i5) * 37;
        if (this.gps != null) {
            i6 = this.gps.hashCode();
        }
        int result2 = i11 + i6;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!this.contents.isEmpty()) {
            builder.append(", contents=").append(this.contents);
        }
        if (this.uid != null) {
            builder.append(", uid=").append(this.uid);
        }
        if (this.product_type != null) {
            builder.append(", product_type=").append(this.product_type);
        }
        if (this.sub_product_type != null) {
            builder.append(", sub_product_type=").append(this.sub_product_type);
        }
        if (this.firmware_version != null) {
            builder.append(", firmware_version=").append(this.firmware_version);
        }
        if (this.app_version != null) {
            builder.append(", app_version=").append(this.app_version);
        }
        if (this.device_nickname != null) {
            builder.append(", device_nickname=").append(this.device_nickname);
        }
        if (this.gps != null) {
            builder.append(", gps=").append(this.gps);
        }
        return builder.replace(0, 2, "DeviceActiveWithMetaInfoV3{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<DeviceActiveWithMetaInfoV3, Builder> {
        public String app_version;
        public List<ByteString> contents = Internal.newMutableList();
        public String device_nickname;
        public String firmware_version;
        public String gps;
        public Integer product_type;
        public Integer sub_product_type;
        public String uid;

        public Builder contents(List<ByteString> contents2) {
            Internal.checkElementsNotNull(contents2);
            this.contents = contents2;
            return this;
        }

        public Builder uid(String uid2) {
            this.uid = uid2;
            return this;
        }

        public Builder product_type(Integer product_type2) {
            this.product_type = product_type2;
            return this;
        }

        public Builder sub_product_type(Integer sub_product_type2) {
            this.sub_product_type = sub_product_type2;
            return this;
        }

        public Builder firmware_version(String firmware_version2) {
            this.firmware_version = firmware_version2;
            return this;
        }

        public Builder app_version(String app_version2) {
            this.app_version = app_version2;
            return this;
        }

        public Builder device_nickname(String device_nickname2) {
            this.device_nickname = device_nickname2;
            return this;
        }

        public Builder gps(String gps2) {
            this.gps = gps2;
            return this;
        }

        public DeviceActiveWithMetaInfoV3 build() {
            return new DeviceActiveWithMetaInfoV3(this.contents, this.uid, this.product_type, this.sub_product_type, this.firmware_version, this.app_version, this.device_nickname, this.gps, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_DeviceActiveWithMetaInfoV3 extends ProtoAdapter<DeviceActiveWithMetaInfoV3> {
        ProtoAdapter_DeviceActiveWithMetaInfoV3() {
            super(FieldEncoding.LENGTH_DELIMITED, DeviceActiveWithMetaInfoV3.class);
        }

        public int encodedSize(DeviceActiveWithMetaInfoV3 value) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7 = 0;
            int encodedSizeWithTag = ProtoAdapter.BYTES.asRepeated().encodedSizeWithTag(1, value.contents);
            if (value.uid != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.uid);
            } else {
                i = 0;
            }
            int i8 = encodedSizeWithTag + i;
            if (value.product_type != null) {
                i2 = ProtoAdapter.INT32.encodedSizeWithTag(3, value.product_type);
            } else {
                i2 = 0;
            }
            int i9 = i8 + i2;
            if (value.sub_product_type != null) {
                i3 = ProtoAdapter.INT32.encodedSizeWithTag(4, value.sub_product_type);
            } else {
                i3 = 0;
            }
            int i10 = i9 + i3;
            if (value.firmware_version != null) {
                i4 = ProtoAdapter.STRING.encodedSizeWithTag(5, value.firmware_version);
            } else {
                i4 = 0;
            }
            int i11 = i10 + i4;
            if (value.app_version != null) {
                i5 = ProtoAdapter.STRING.encodedSizeWithTag(6, value.app_version);
            } else {
                i5 = 0;
            }
            int i12 = i11 + i5;
            if (value.device_nickname != null) {
                i6 = ProtoAdapter.STRING.encodedSizeWithTag(7, value.device_nickname);
            } else {
                i6 = 0;
            }
            int i13 = i6 + i12;
            if (value.gps != null) {
                i7 = ProtoAdapter.STRING.encodedSizeWithTag(8, value.gps);
            }
            return i13 + i7 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, DeviceActiveWithMetaInfoV3 value) throws IOException {
            ProtoAdapter.BYTES.asRepeated().encodeWithTag(writer, 1, value.contents);
            if (value.uid != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.uid);
            }
            if (value.product_type != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 3, value.product_type);
            }
            if (value.sub_product_type != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 4, value.sub_product_type);
            }
            if (value.firmware_version != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 5, value.firmware_version);
            }
            if (value.app_version != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 6, value.app_version);
            }
            if (value.device_nickname != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 7, value.device_nickname);
            }
            if (value.gps != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 8, value.gps);
            }
            writer.writeBytes(value.unknownFields());
        }

        public DeviceActiveWithMetaInfoV3 decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.contents.add(ProtoAdapter.BYTES.decode(reader));
                            break;
                        case 2:
                            builder.uid(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            builder.product_type(ProtoAdapter.INT32.decode(reader));
                            break;
                        case 4:
                            builder.sub_product_type(ProtoAdapter.INT32.decode(reader));
                            break;
                        case 5:
                            builder.firmware_version(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 6:
                            builder.app_version(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 7:
                            builder.device_nickname(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 8:
                            builder.gps(ProtoAdapter.STRING.decode(reader));
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

        public DeviceActiveWithMetaInfoV3 redact(DeviceActiveWithMetaInfoV3 value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
