package dji.flysafe.v3;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import okio.ByteString;

public final class License extends Message<License, Builder> {
    public static final ProtoAdapter<License> ADAPTER = new ProtoAdapter_License();
    public static final String DEFAULT_DESCRIPTION = "";
    public static final ByteString DEFAULT_DESC_JAVA = ByteString.EMPTY;
    public static final Integer DEFAULT_END_AT = 0;
    public static final Integer DEFAULT_ID = 0;
    public static final Integer DEFAULT_START_AT = 0;
    public static final Boolean DEFAULT_USER_ONLY = false;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "dji.flysafe.v3.LicenseData#ADAPTER", tag = 6)
    public final LicenseData data;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BYTES", tag = 7)
    public final ByteString desc_java;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String description;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 4)
    public final Integer end_at;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 1)
    public final Integer id;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 3)
    public final Integer start_at;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 5)
    public final Boolean user_only;

    public License(Integer id2, String description2, Integer start_at2, Integer end_at2, Boolean user_only2, LicenseData data2, ByteString desc_java2) {
        this(id2, description2, start_at2, end_at2, user_only2, data2, desc_java2, ByteString.EMPTY);
    }

    public License(Integer id2, String description2, Integer start_at2, Integer end_at2, Boolean user_only2, LicenseData data2, ByteString desc_java2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.id = id2;
        this.description = description2;
        this.start_at = start_at2;
        this.end_at = end_at2;
        this.user_only = user_only2;
        this.data = data2;
        this.desc_java = desc_java2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.id = this.id;
        builder.description = this.description;
        builder.start_at = this.start_at;
        builder.end_at = this.end_at;
        builder.user_only = this.user_only;
        builder.data = this.data;
        builder.desc_java = this.desc_java;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof License)) {
            return false;
        }
        License o = (License) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.id, o.id) || !Internal.equals(this.description, o.description) || !Internal.equals(this.start_at, o.start_at) || !Internal.equals(this.end_at, o.end_at) || !Internal.equals(this.user_only, o.user_only) || !Internal.equals(this.data, o.data) || !Internal.equals(this.desc_java, o.desc_java)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.id != null ? this.id.hashCode() : 0)) * 37;
        if (this.description != null) {
            i = this.description.hashCode();
        } else {
            i = 0;
        }
        int i7 = (hashCode + i) * 37;
        if (this.start_at != null) {
            i2 = this.start_at.hashCode();
        } else {
            i2 = 0;
        }
        int i8 = (i7 + i2) * 37;
        if (this.end_at != null) {
            i3 = this.end_at.hashCode();
        } else {
            i3 = 0;
        }
        int i9 = (i8 + i3) * 37;
        if (this.user_only != null) {
            i4 = this.user_only.hashCode();
        } else {
            i4 = 0;
        }
        int i10 = (i9 + i4) * 37;
        if (this.data != null) {
            i5 = this.data.hashCode();
        } else {
            i5 = 0;
        }
        int i11 = (i10 + i5) * 37;
        if (this.desc_java != null) {
            i6 = this.desc_java.hashCode();
        }
        int result2 = i11 + i6;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.id != null) {
            builder.append(", id=").append(this.id);
        }
        if (this.description != null) {
            builder.append(", description=").append(this.description);
        }
        if (this.start_at != null) {
            builder.append(", start_at=").append(this.start_at);
        }
        if (this.end_at != null) {
            builder.append(", end_at=").append(this.end_at);
        }
        if (this.user_only != null) {
            builder.append(", user_only=").append(this.user_only);
        }
        if (this.data != null) {
            builder.append(", data=").append(this.data);
        }
        if (this.desc_java != null) {
            builder.append(", desc_java=").append(this.desc_java);
        }
        return builder.replace(0, 2, "License{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<License, Builder> {
        public LicenseData data;
        public ByteString desc_java;
        public String description;
        public Integer end_at;
        public Integer id;
        public Integer start_at;
        public Boolean user_only;

        public Builder id(Integer id2) {
            this.id = id2;
            return this;
        }

        public Builder description(String description2) {
            this.description = description2;
            return this;
        }

        public Builder start_at(Integer start_at2) {
            this.start_at = start_at2;
            return this;
        }

        public Builder end_at(Integer end_at2) {
            this.end_at = end_at2;
            return this;
        }

        public Builder user_only(Boolean user_only2) {
            this.user_only = user_only2;
            return this;
        }

        public Builder data(LicenseData data2) {
            this.data = data2;
            return this;
        }

        public Builder desc_java(ByteString desc_java2) {
            this.desc_java = desc_java2;
            return this;
        }

        public License build() {
            return new License(this.id, this.description, this.start_at, this.end_at, this.user_only, this.data, this.desc_java, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_License extends ProtoAdapter<License> {
        ProtoAdapter_License() {
            super(FieldEncoding.LENGTH_DELIMITED, License.class);
        }

        public int encodedSize(License value) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6 = 0;
            int encodedSizeWithTag = value.id != null ? ProtoAdapter.UINT32.encodedSizeWithTag(1, value.id) : 0;
            if (value.description != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.description);
            } else {
                i = 0;
            }
            int i7 = i + encodedSizeWithTag;
            if (value.start_at != null) {
                i2 = ProtoAdapter.UINT32.encodedSizeWithTag(3, value.start_at);
            } else {
                i2 = 0;
            }
            int i8 = i7 + i2;
            if (value.end_at != null) {
                i3 = ProtoAdapter.UINT32.encodedSizeWithTag(4, value.end_at);
            } else {
                i3 = 0;
            }
            int i9 = i8 + i3;
            if (value.user_only != null) {
                i4 = ProtoAdapter.BOOL.encodedSizeWithTag(5, value.user_only);
            } else {
                i4 = 0;
            }
            int i10 = i9 + i4;
            if (value.data != null) {
                i5 = LicenseData.ADAPTER.encodedSizeWithTag(6, value.data);
            } else {
                i5 = 0;
            }
            int i11 = i5 + i10;
            if (value.desc_java != null) {
                i6 = ProtoAdapter.BYTES.encodedSizeWithTag(7, value.desc_java);
            }
            return i11 + i6 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, License value) throws IOException {
            if (value.id != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.id);
            }
            if (value.description != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.description);
            }
            if (value.start_at != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.start_at);
            }
            if (value.end_at != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.end_at);
            }
            if (value.user_only != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 5, value.user_only);
            }
            if (value.data != null) {
                LicenseData.ADAPTER.encodeWithTag(writer, 6, value.data);
            }
            if (value.desc_java != null) {
                ProtoAdapter.BYTES.encodeWithTag(writer, 7, value.desc_java);
            }
            writer.writeBytes(value.unknownFields());
        }

        public License decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.id(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 2:
                            builder.description(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            builder.start_at(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 4:
                            builder.end_at(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 5:
                            builder.user_only(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 6:
                            builder.data(LicenseData.ADAPTER.decode(reader));
                            break;
                        case 7:
                            builder.desc_java(ProtoAdapter.BYTES.decode(reader));
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

        public License redact(License value) {
            Builder builder = value.newBuilder();
            if (builder.data != null) {
                builder.data = LicenseData.ADAPTER.redact(builder.data);
            }
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
