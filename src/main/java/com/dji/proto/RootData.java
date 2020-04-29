package com.dji.proto;

import com.dji.fieldAnnotation.EXClassNullAway;
import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireEnum;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import okio.ByteString;

@EXClassNullAway
public final class RootData extends Message<RootData, Builder> {
    public static final ProtoAdapter<RootData> ADAPTER = new ProtoAdapter_RootData();
    public static final String DEFAULT_APP_KEY = "";
    public static final Integer DEFAULT_APP_VERSION = 0;
    public static final String DEFAULT_CMD = "";
    public static final ByteString DEFAULT_CONTENT = ByteString.EMPTY;
    public static final EncryptionType DEFAULT_ENCRYPTION = EncryptionType.NONE;
    public static final Integer DEFAULT_PLAINTEXT_LEN = 0;
    public static final PlatformType DEFAULT_PLATFORM = PlatformType.SERVER;
    public static final String DEFAULT_SIGNATURE = "";
    public static final Integer DEFAULT_TIMESTAMP = 0;
    public static final Integer DEFAULT_VERSION = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 4)
    public final String app_key;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 5)
    public final Integer app_version;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 8)
    public final String cmd;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BYTES", tag = 7)
    public final ByteString content;
    @WireField(adapter = "com.dji.proto.RootData$EncryptionType#ADAPTER", tag = 2)
    public final EncryptionType encryption;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 6)
    public final Integer plaintext_len;
    @WireField(adapter = "com.dji.proto.RootData$PlatformType#ADAPTER", tag = 3)
    public final PlatformType platform;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 10)
    public final String signature;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 9)
    public final Integer timestamp;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 1)
    public final Integer version;

    public RootData(Integer version2, EncryptionType encryption2, PlatformType platform2, String app_key2, Integer app_version2, Integer plaintext_len2, ByteString content2, String cmd2, Integer timestamp2, String signature2) {
        this(version2, encryption2, platform2, app_key2, app_version2, plaintext_len2, content2, cmd2, timestamp2, signature2, ByteString.EMPTY);
    }

    public RootData(Integer version2, EncryptionType encryption2, PlatformType platform2, String app_key2, Integer app_version2, Integer plaintext_len2, ByteString content2, String cmd2, Integer timestamp2, String signature2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.version = version2;
        this.encryption = encryption2;
        this.platform = platform2;
        this.app_key = app_key2;
        this.app_version = app_version2;
        this.plaintext_len = plaintext_len2;
        this.content = content2;
        this.cmd = cmd2;
        this.timestamp = timestamp2;
        this.signature = signature2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.version = this.version;
        builder.encryption = this.encryption;
        builder.platform = this.platform;
        builder.app_key = this.app_key;
        builder.app_version = this.app_version;
        builder.plaintext_len = this.plaintext_len;
        builder.content = this.content;
        builder.cmd = this.cmd;
        builder.timestamp = this.timestamp;
        builder.signature = this.signature;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RootData)) {
            return false;
        }
        RootData o = (RootData) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.version, o.version) || !Internal.equals(this.encryption, o.encryption) || !Internal.equals(this.platform, o.platform) || !Internal.equals(this.app_key, o.app_key) || !Internal.equals(this.app_version, o.app_version) || !Internal.equals(this.plaintext_len, o.plaintext_len) || !Internal.equals(this.content, o.content) || !Internal.equals(this.cmd, o.cmd) || !Internal.equals(this.timestamp, o.timestamp) || !Internal.equals(this.signature, o.signature)) {
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
        int i9 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.version != null ? this.version.hashCode() : 0)) * 37;
        if (this.encryption != null) {
            i = this.encryption.hashCode();
        } else {
            i = 0;
        }
        int i10 = (hashCode + i) * 37;
        if (this.platform != null) {
            i2 = this.platform.hashCode();
        } else {
            i2 = 0;
        }
        int i11 = (i10 + i2) * 37;
        if (this.app_key != null) {
            i3 = this.app_key.hashCode();
        } else {
            i3 = 0;
        }
        int i12 = (i11 + i3) * 37;
        if (this.app_version != null) {
            i4 = this.app_version.hashCode();
        } else {
            i4 = 0;
        }
        int i13 = (i12 + i4) * 37;
        if (this.plaintext_len != null) {
            i5 = this.plaintext_len.hashCode();
        } else {
            i5 = 0;
        }
        int i14 = (i13 + i5) * 37;
        if (this.content != null) {
            i6 = this.content.hashCode();
        } else {
            i6 = 0;
        }
        int i15 = (i14 + i6) * 37;
        if (this.cmd != null) {
            i7 = this.cmd.hashCode();
        } else {
            i7 = 0;
        }
        int i16 = (i15 + i7) * 37;
        if (this.timestamp != null) {
            i8 = this.timestamp.hashCode();
        } else {
            i8 = 0;
        }
        int i17 = (i16 + i8) * 37;
        if (this.signature != null) {
            i9 = this.signature.hashCode();
        }
        int result2 = i17 + i9;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.version != null) {
            builder.append(", version=").append(this.version);
        }
        if (this.encryption != null) {
            builder.append(", encryption=").append(this.encryption);
        }
        if (this.platform != null) {
            builder.append(", platform=").append(this.platform);
        }
        if (this.app_key != null) {
            builder.append(", app_key=").append(this.app_key);
        }
        if (this.app_version != null) {
            builder.append(", app_version=").append(this.app_version);
        }
        if (this.plaintext_len != null) {
            builder.append(", plaintext_len=").append(this.plaintext_len);
        }
        if (this.content != null) {
            builder.append(", content=").append(this.content);
        }
        if (this.cmd != null) {
            builder.append(", cmd=").append(this.cmd);
        }
        if (this.timestamp != null) {
            builder.append(", timestamp=").append(this.timestamp);
        }
        if (this.signature != null) {
            builder.append(", signature=").append(this.signature);
        }
        return builder.replace(0, 2, "RootData{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<RootData, Builder> {
        public String app_key;
        public Integer app_version;
        public String cmd;
        public ByteString content;
        public EncryptionType encryption;
        public Integer plaintext_len;
        public PlatformType platform;
        public String signature;
        public Integer timestamp;
        public Integer version;

        public Builder version(Integer version2) {
            this.version = version2;
            return this;
        }

        public Builder encryption(EncryptionType encryption2) {
            this.encryption = encryption2;
            return this;
        }

        public Builder platform(PlatformType platform2) {
            this.platform = platform2;
            return this;
        }

        public Builder app_key(String app_key2) {
            this.app_key = app_key2;
            return this;
        }

        public Builder app_version(Integer app_version2) {
            this.app_version = app_version2;
            return this;
        }

        public Builder plaintext_len(Integer plaintext_len2) {
            this.plaintext_len = plaintext_len2;
            return this;
        }

        public Builder content(ByteString content2) {
            this.content = content2;
            return this;
        }

        public Builder cmd(String cmd2) {
            this.cmd = cmd2;
            return this;
        }

        public Builder timestamp(Integer timestamp2) {
            this.timestamp = timestamp2;
            return this;
        }

        public Builder signature(String signature2) {
            this.signature = signature2;
            return this;
        }

        public RootData build() {
            return new RootData(this.version, this.encryption, this.platform, this.app_key, this.app_version, this.plaintext_len, this.content, this.cmd, this.timestamp, this.signature, super.buildUnknownFields());
        }
    }

    public enum EncryptionType implements WireEnum {
        NONE(0),
        AES(1),
        CUSTOM(2);
        
        public static final ProtoAdapter<EncryptionType> ADAPTER = ProtoAdapter.newEnumAdapter(EncryptionType.class);
        private final int value;

        private EncryptionType(int value2) {
            this.value = value2;
        }

        public static EncryptionType fromValue(int value2) {
            switch (value2) {
                case 0:
                    return NONE;
                case 1:
                    return AES;
                case 2:
                    return CUSTOM;
                default:
                    return null;
            }
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum PlatformType implements WireEnum {
        SERVER(0),
        ANDROID(1),
        IOS(2),
        PC(3);
        
        public static final ProtoAdapter<PlatformType> ADAPTER = ProtoAdapter.newEnumAdapter(PlatformType.class);
        private final int value;

        private PlatformType(int value2) {
            this.value = value2;
        }

        public static PlatformType fromValue(int value2) {
            switch (value2) {
                case 0:
                    return SERVER;
                case 1:
                    return ANDROID;
                case 2:
                    return IOS;
                case 3:
                    return PC;
                default:
                    return null;
            }
        }

        public int getValue() {
            return this.value;
        }
    }

    private static final class ProtoAdapter_RootData extends ProtoAdapter<RootData> {
        ProtoAdapter_RootData() {
            super(FieldEncoding.LENGTH_DELIMITED, RootData.class);
        }

        public int encodedSize(RootData value) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            int i8;
            int i9 = 0;
            int encodedSizeWithTag = value.version != null ? ProtoAdapter.INT32.encodedSizeWithTag(1, value.version) : 0;
            if (value.encryption != null) {
                i = EncryptionType.ADAPTER.encodedSizeWithTag(2, value.encryption);
            } else {
                i = 0;
            }
            int i10 = i + encodedSizeWithTag;
            if (value.platform != null) {
                i2 = PlatformType.ADAPTER.encodedSizeWithTag(3, value.platform);
            } else {
                i2 = 0;
            }
            int i11 = i10 + i2;
            if (value.app_key != null) {
                i3 = ProtoAdapter.STRING.encodedSizeWithTag(4, value.app_key);
            } else {
                i3 = 0;
            }
            int i12 = i11 + i3;
            if (value.app_version != null) {
                i4 = ProtoAdapter.INT32.encodedSizeWithTag(5, value.app_version);
            } else {
                i4 = 0;
            }
            int i13 = i12 + i4;
            if (value.plaintext_len != null) {
                i5 = ProtoAdapter.INT32.encodedSizeWithTag(6, value.plaintext_len);
            } else {
                i5 = 0;
            }
            int i14 = i13 + i5;
            if (value.content != null) {
                i6 = ProtoAdapter.BYTES.encodedSizeWithTag(7, value.content);
            } else {
                i6 = 0;
            }
            int i15 = i14 + i6;
            if (value.cmd != null) {
                i7 = ProtoAdapter.STRING.encodedSizeWithTag(8, value.cmd);
            } else {
                i7 = 0;
            }
            int i16 = i15 + i7;
            if (value.timestamp != null) {
                i8 = ProtoAdapter.INT32.encodedSizeWithTag(9, value.timestamp);
            } else {
                i8 = 0;
            }
            int i17 = i8 + i16;
            if (value.signature != null) {
                i9 = ProtoAdapter.STRING.encodedSizeWithTag(10, value.signature);
            }
            return i17 + i9 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, RootData value) throws IOException {
            if (value.version != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 1, value.version);
            }
            if (value.encryption != null) {
                EncryptionType.ADAPTER.encodeWithTag(writer, 2, value.encryption);
            }
            if (value.platform != null) {
                PlatformType.ADAPTER.encodeWithTag(writer, 3, value.platform);
            }
            if (value.app_key != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 4, value.app_key);
            }
            if (value.app_version != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 5, value.app_version);
            }
            if (value.plaintext_len != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 6, value.plaintext_len);
            }
            if (value.content != null) {
                ProtoAdapter.BYTES.encodeWithTag(writer, 7, value.content);
            }
            if (value.cmd != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 8, value.cmd);
            }
            if (value.timestamp != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 9, value.timestamp);
            }
            if (value.signature != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 10, value.signature);
            }
            writer.writeBytes(value.unknownFields());
        }

        public RootData decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.version(ProtoAdapter.INT32.decode(reader));
                            break;
                        case 2:
                            try {
                                builder.encryption(EncryptionType.ADAPTER.decode(reader));
                                break;
                            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
                                builder.addUnknownField(tag, FieldEncoding.VARINT, Long.valueOf((long) e.value));
                                break;
                            }
                        case 3:
                            try {
                                builder.platform(PlatformType.ADAPTER.decode(reader));
                                break;
                            } catch (ProtoAdapter.EnumConstantNotFoundException e2) {
                                builder.addUnknownField(tag, FieldEncoding.VARINT, Long.valueOf((long) e2.value));
                                break;
                            }
                        case 4:
                            builder.app_key(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 5:
                            builder.app_version(ProtoAdapter.INT32.decode(reader));
                            break;
                        case 6:
                            builder.plaintext_len(ProtoAdapter.INT32.decode(reader));
                            break;
                        case 7:
                            builder.content(ProtoAdapter.BYTES.decode(reader));
                            break;
                        case 8:
                            builder.cmd(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 9:
                            builder.timestamp(ProtoAdapter.INT32.decode(reader));
                            break;
                        case 10:
                            builder.signature(ProtoAdapter.STRING.decode(reader));
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

        public RootData redact(RootData value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
