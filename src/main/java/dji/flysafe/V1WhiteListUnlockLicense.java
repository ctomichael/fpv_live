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

public final class V1WhiteListUnlockLicense extends Message<V1WhiteListUnlockLicense, Builder> {
    public static final ProtoAdapter<V1WhiteListUnlockLicense> ADAPTER = new ProtoAdapter_V1WhiteListUnlockLicense();
    public static final String DEFAULT_ACCOUNT = "";
    public static final String DEFAULT_CREATETIME = "";
    public static final Boolean DEFAULT_DISABLE = false;
    public static final Integer DEFAULT_ID = 0;
    public static final String DEFAULT_LICENSEKEY = "";
    public static final String DEFAULT_MCSN = "";
    public static final String DEFAULT_NAME = "";
    public static final String DEFAULT_REGISTER_PHONE = "";
    public static final String DEFAULT_UPDATETIME = "";
    public static final String DEFAULT_USERID = "";
    public static final Integer DEFAULT_USERUNLOCKID = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 4)
    public final Integer ID;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 6)
    public final String MCSN;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String account;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 9)
    public final String createTime;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 2)
    public final Boolean disable;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 7)
    public final String licenseKey;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 10)
    public final String name;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 3)
    public final String register_phone;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 8)
    public final String updateTime;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 11)
    public final String userID;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 5)
    public final Integer userUnlockID;

    public V1WhiteListUnlockLicense(String account2, Boolean disable2, String register_phone2, Integer ID2, Integer userUnlockID2, String MCSN2, String licenseKey2, String updateTime2, String createTime2, String name2, String userID2) {
        this(account2, disable2, register_phone2, ID2, userUnlockID2, MCSN2, licenseKey2, updateTime2, createTime2, name2, userID2, ByteString.EMPTY);
    }

    public V1WhiteListUnlockLicense(String account2, Boolean disable2, String register_phone2, Integer ID2, Integer userUnlockID2, String MCSN2, String licenseKey2, String updateTime2, String createTime2, String name2, String userID2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.account = account2;
        this.disable = disable2;
        this.register_phone = register_phone2;
        this.ID = ID2;
        this.userUnlockID = userUnlockID2;
        this.MCSN = MCSN2;
        this.licenseKey = licenseKey2;
        this.updateTime = updateTime2;
        this.createTime = createTime2;
        this.name = name2;
        this.userID = userID2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.account = this.account;
        builder.disable = this.disable;
        builder.register_phone = this.register_phone;
        builder.ID = this.ID;
        builder.userUnlockID = this.userUnlockID;
        builder.MCSN = this.MCSN;
        builder.licenseKey = this.licenseKey;
        builder.updateTime = this.updateTime;
        builder.createTime = this.createTime;
        builder.name = this.name;
        builder.userID = this.userID;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof V1WhiteListUnlockLicense)) {
            return false;
        }
        V1WhiteListUnlockLicense o = (V1WhiteListUnlockLicense) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.account, o.account) || !Internal.equals(this.disable, o.disable) || !Internal.equals(this.register_phone, o.register_phone) || !Internal.equals(this.ID, o.ID) || !Internal.equals(this.userUnlockID, o.userUnlockID) || !Internal.equals(this.MCSN, o.MCSN) || !Internal.equals(this.licenseKey, o.licenseKey) || !Internal.equals(this.updateTime, o.updateTime) || !Internal.equals(this.createTime, o.createTime) || !Internal.equals(this.name, o.name) || !Internal.equals(this.userID, o.userID)) {
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
        int i9;
        int i10 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.account != null ? this.account.hashCode() : 0)) * 37;
        if (this.disable != null) {
            i = this.disable.hashCode();
        } else {
            i = 0;
        }
        int i11 = (hashCode + i) * 37;
        if (this.register_phone != null) {
            i2 = this.register_phone.hashCode();
        } else {
            i2 = 0;
        }
        int i12 = (i11 + i2) * 37;
        if (this.ID != null) {
            i3 = this.ID.hashCode();
        } else {
            i3 = 0;
        }
        int i13 = (i12 + i3) * 37;
        if (this.userUnlockID != null) {
            i4 = this.userUnlockID.hashCode();
        } else {
            i4 = 0;
        }
        int i14 = (i13 + i4) * 37;
        if (this.MCSN != null) {
            i5 = this.MCSN.hashCode();
        } else {
            i5 = 0;
        }
        int i15 = (i14 + i5) * 37;
        if (this.licenseKey != null) {
            i6 = this.licenseKey.hashCode();
        } else {
            i6 = 0;
        }
        int i16 = (i15 + i6) * 37;
        if (this.updateTime != null) {
            i7 = this.updateTime.hashCode();
        } else {
            i7 = 0;
        }
        int i17 = (i16 + i7) * 37;
        if (this.createTime != null) {
            i8 = this.createTime.hashCode();
        } else {
            i8 = 0;
        }
        int i18 = (i17 + i8) * 37;
        if (this.name != null) {
            i9 = this.name.hashCode();
        } else {
            i9 = 0;
        }
        int i19 = (i18 + i9) * 37;
        if (this.userID != null) {
            i10 = this.userID.hashCode();
        }
        int result2 = i19 + i10;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.account != null) {
            builder.append(", account=").append(this.account);
        }
        if (this.disable != null) {
            builder.append(", disable=").append(this.disable);
        }
        if (this.register_phone != null) {
            builder.append(", register_phone=").append(this.register_phone);
        }
        if (this.ID != null) {
            builder.append(", ID=").append(this.ID);
        }
        if (this.userUnlockID != null) {
            builder.append(", userUnlockID=").append(this.userUnlockID);
        }
        if (this.MCSN != null) {
            builder.append(", MCSN=").append(this.MCSN);
        }
        if (this.licenseKey != null) {
            builder.append(", licenseKey=").append(this.licenseKey);
        }
        if (this.updateTime != null) {
            builder.append(", updateTime=").append(this.updateTime);
        }
        if (this.createTime != null) {
            builder.append(", createTime=").append(this.createTime);
        }
        if (this.name != null) {
            builder.append(", name=").append(this.name);
        }
        if (this.userID != null) {
            builder.append(", userID=").append(this.userID);
        }
        return builder.replace(0, 2, "V1WhiteListUnlockLicense{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<V1WhiteListUnlockLicense, Builder> {
        public Integer ID;
        public String MCSN;
        public String account;
        public String createTime;
        public Boolean disable;
        public String licenseKey;
        public String name;
        public String register_phone;
        public String updateTime;
        public String userID;
        public Integer userUnlockID;

        public Builder account(String account2) {
            this.account = account2;
            return this;
        }

        public Builder disable(Boolean disable2) {
            this.disable = disable2;
            return this;
        }

        public Builder register_phone(String register_phone2) {
            this.register_phone = register_phone2;
            return this;
        }

        public Builder ID(Integer ID2) {
            this.ID = ID2;
            return this;
        }

        public Builder userUnlockID(Integer userUnlockID2) {
            this.userUnlockID = userUnlockID2;
            return this;
        }

        public Builder MCSN(String MCSN2) {
            this.MCSN = MCSN2;
            return this;
        }

        public Builder licenseKey(String licenseKey2) {
            this.licenseKey = licenseKey2;
            return this;
        }

        public Builder updateTime(String updateTime2) {
            this.updateTime = updateTime2;
            return this;
        }

        public Builder createTime(String createTime2) {
            this.createTime = createTime2;
            return this;
        }

        public Builder name(String name2) {
            this.name = name2;
            return this;
        }

        public Builder userID(String userID2) {
            this.userID = userID2;
            return this;
        }

        public V1WhiteListUnlockLicense build() {
            return new V1WhiteListUnlockLicense(this.account, this.disable, this.register_phone, this.ID, this.userUnlockID, this.MCSN, this.licenseKey, this.updateTime, this.createTime, this.name, this.userID, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_V1WhiteListUnlockLicense extends ProtoAdapter<V1WhiteListUnlockLicense> {
        ProtoAdapter_V1WhiteListUnlockLicense() {
            super(FieldEncoding.LENGTH_DELIMITED, V1WhiteListUnlockLicense.class);
        }

        public int encodedSize(V1WhiteListUnlockLicense value) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            int i8;
            int i9;
            int i10 = 0;
            int encodedSizeWithTag = value.account != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.account) : 0;
            if (value.disable != null) {
                i = ProtoAdapter.BOOL.encodedSizeWithTag(2, value.disable);
            } else {
                i = 0;
            }
            int i11 = i + encodedSizeWithTag;
            if (value.register_phone != null) {
                i2 = ProtoAdapter.STRING.encodedSizeWithTag(3, value.register_phone);
            } else {
                i2 = 0;
            }
            int i12 = i11 + i2;
            if (value.ID != null) {
                i3 = ProtoAdapter.UINT32.encodedSizeWithTag(4, value.ID);
            } else {
                i3 = 0;
            }
            int i13 = i12 + i3;
            if (value.userUnlockID != null) {
                i4 = ProtoAdapter.UINT32.encodedSizeWithTag(5, value.userUnlockID);
            } else {
                i4 = 0;
            }
            int i14 = i13 + i4;
            if (value.MCSN != null) {
                i5 = ProtoAdapter.STRING.encodedSizeWithTag(6, value.MCSN);
            } else {
                i5 = 0;
            }
            int i15 = i14 + i5;
            if (value.licenseKey != null) {
                i6 = ProtoAdapter.STRING.encodedSizeWithTag(7, value.licenseKey);
            } else {
                i6 = 0;
            }
            int i16 = i15 + i6;
            if (value.updateTime != null) {
                i7 = ProtoAdapter.STRING.encodedSizeWithTag(8, value.updateTime);
            } else {
                i7 = 0;
            }
            int i17 = i16 + i7;
            if (value.createTime != null) {
                i8 = ProtoAdapter.STRING.encodedSizeWithTag(9, value.createTime);
            } else {
                i8 = 0;
            }
            int i18 = i17 + i8;
            if (value.name != null) {
                i9 = ProtoAdapter.STRING.encodedSizeWithTag(10, value.name);
            } else {
                i9 = 0;
            }
            int i19 = i9 + i18;
            if (value.userID != null) {
                i10 = ProtoAdapter.STRING.encodedSizeWithTag(11, value.userID);
            }
            return i19 + i10 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, V1WhiteListUnlockLicense value) throws IOException {
            if (value.account != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.account);
            }
            if (value.disable != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 2, value.disable);
            }
            if (value.register_phone != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 3, value.register_phone);
            }
            if (value.ID != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.ID);
            }
            if (value.userUnlockID != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.userUnlockID);
            }
            if (value.MCSN != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 6, value.MCSN);
            }
            if (value.licenseKey != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 7, value.licenseKey);
            }
            if (value.updateTime != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 8, value.updateTime);
            }
            if (value.createTime != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 9, value.createTime);
            }
            if (value.name != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 10, value.name);
            }
            if (value.userID != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 11, value.userID);
            }
            writer.writeBytes(value.unknownFields());
        }

        public V1WhiteListUnlockLicense decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.account(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.disable(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 3:
                            builder.register_phone(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 4:
                            builder.ID(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 5:
                            builder.userUnlockID(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 6:
                            builder.MCSN(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 7:
                            builder.licenseKey(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 8:
                            builder.updateTime(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 9:
                            builder.createTime(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 10:
                            builder.name(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 11:
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

        public V1WhiteListUnlockLicense redact(V1WhiteListUnlockLicense value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
