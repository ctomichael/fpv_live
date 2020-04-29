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

public final class AppUnlockUserVerifyDataItem extends Message<AppUnlockUserVerifyDataItem, Builder> {
    public static final ProtoAdapter<AppUnlockUserVerifyDataItem> ADAPTER = new ProtoAdapter_AppUnlockUserVerifyDataItem();
    public static final String DEFAULT_ACCOUNT = "";
    public static final String DEFAULT_ADDRESS = "";
    public static final Integer DEFAULT_AGREED = 0;
    public static final String DEFAULT_COMPANY = "";
    public static final String DEFAULT_CREATEDATA = "";
    public static final String DEFAULT_DATE = "";
    public static final String DEFAULT_EXT1 = "";
    public static final String DEFAULT_EXT2 = "";
    public static final String DEFAULT_NAME = "";
    public static final String DEFAULT_TITLE = "";
    public static final String DEFAULT_UPDATEDATE = "";
    public static final Integer DEFAULT_VERIFYDATAID = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String account;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 8)
    public final String address;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 5)
    public final Integer agreed;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 6)
    public final String company;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 12)
    public final String createData;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 4)
    public final String date;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 9)
    public final String ext1;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 10)
    public final String ext2;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 3)
    public final String name;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 7)
    public final String title;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 11)
    public final String updateDate;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 1)
    public final Integer verifyDataID;

    public AppUnlockUserVerifyDataItem(Integer verifyDataID2, String account2, String name2, String date2, Integer agreed2, String company2, String title2, String address2, String ext12, String ext22, String updateDate2, String createData2) {
        this(verifyDataID2, account2, name2, date2, agreed2, company2, title2, address2, ext12, ext22, updateDate2, createData2, ByteString.EMPTY);
    }

    public AppUnlockUserVerifyDataItem(Integer verifyDataID2, String account2, String name2, String date2, Integer agreed2, String company2, String title2, String address2, String ext12, String ext22, String updateDate2, String createData2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.verifyDataID = verifyDataID2;
        this.account = account2;
        this.name = name2;
        this.date = date2;
        this.agreed = agreed2;
        this.company = company2;
        this.title = title2;
        this.address = address2;
        this.ext1 = ext12;
        this.ext2 = ext22;
        this.updateDate = updateDate2;
        this.createData = createData2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.verifyDataID = this.verifyDataID;
        builder.account = this.account;
        builder.name = this.name;
        builder.date = this.date;
        builder.agreed = this.agreed;
        builder.company = this.company;
        builder.title = this.title;
        builder.address = this.address;
        builder.ext1 = this.ext1;
        builder.ext2 = this.ext2;
        builder.updateDate = this.updateDate;
        builder.createData = this.createData;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AppUnlockUserVerifyDataItem)) {
            return false;
        }
        AppUnlockUserVerifyDataItem o = (AppUnlockUserVerifyDataItem) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.verifyDataID, o.verifyDataID) || !Internal.equals(this.account, o.account) || !Internal.equals(this.name, o.name) || !Internal.equals(this.date, o.date) || !Internal.equals(this.agreed, o.agreed) || !Internal.equals(this.company, o.company) || !Internal.equals(this.title, o.title) || !Internal.equals(this.address, o.address) || !Internal.equals(this.ext1, o.ext1) || !Internal.equals(this.ext2, o.ext2) || !Internal.equals(this.updateDate, o.updateDate) || !Internal.equals(this.createData, o.createData)) {
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
        int i10;
        int i11 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.verifyDataID != null ? this.verifyDataID.hashCode() : 0)) * 37;
        if (this.account != null) {
            i = this.account.hashCode();
        } else {
            i = 0;
        }
        int i12 = (hashCode + i) * 37;
        if (this.name != null) {
            i2 = this.name.hashCode();
        } else {
            i2 = 0;
        }
        int i13 = (i12 + i2) * 37;
        if (this.date != null) {
            i3 = this.date.hashCode();
        } else {
            i3 = 0;
        }
        int i14 = (i13 + i3) * 37;
        if (this.agreed != null) {
            i4 = this.agreed.hashCode();
        } else {
            i4 = 0;
        }
        int i15 = (i14 + i4) * 37;
        if (this.company != null) {
            i5 = this.company.hashCode();
        } else {
            i5 = 0;
        }
        int i16 = (i15 + i5) * 37;
        if (this.title != null) {
            i6 = this.title.hashCode();
        } else {
            i6 = 0;
        }
        int i17 = (i16 + i6) * 37;
        if (this.address != null) {
            i7 = this.address.hashCode();
        } else {
            i7 = 0;
        }
        int i18 = (i17 + i7) * 37;
        if (this.ext1 != null) {
            i8 = this.ext1.hashCode();
        } else {
            i8 = 0;
        }
        int i19 = (i18 + i8) * 37;
        if (this.ext2 != null) {
            i9 = this.ext2.hashCode();
        } else {
            i9 = 0;
        }
        int i20 = (i19 + i9) * 37;
        if (this.updateDate != null) {
            i10 = this.updateDate.hashCode();
        } else {
            i10 = 0;
        }
        int i21 = (i20 + i10) * 37;
        if (this.createData != null) {
            i11 = this.createData.hashCode();
        }
        int result2 = i21 + i11;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.verifyDataID != null) {
            builder.append(", verifyDataID=").append(this.verifyDataID);
        }
        if (this.account != null) {
            builder.append(", account=").append(this.account);
        }
        if (this.name != null) {
            builder.append(", name=").append(this.name);
        }
        if (this.date != null) {
            builder.append(", date=").append(this.date);
        }
        if (this.agreed != null) {
            builder.append(", agreed=").append(this.agreed);
        }
        if (this.company != null) {
            builder.append(", company=").append(this.company);
        }
        if (this.title != null) {
            builder.append(", title=").append(this.title);
        }
        if (this.address != null) {
            builder.append(", address=").append(this.address);
        }
        if (this.ext1 != null) {
            builder.append(", ext1=").append(this.ext1);
        }
        if (this.ext2 != null) {
            builder.append(", ext2=").append(this.ext2);
        }
        if (this.updateDate != null) {
            builder.append(", updateDate=").append(this.updateDate);
        }
        if (this.createData != null) {
            builder.append(", createData=").append(this.createData);
        }
        return builder.replace(0, 2, "AppUnlockUserVerifyDataItem{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<AppUnlockUserVerifyDataItem, Builder> {
        public String account;
        public String address;
        public Integer agreed;
        public String company;
        public String createData;
        public String date;
        public String ext1;
        public String ext2;
        public String name;
        public String title;
        public String updateDate;
        public Integer verifyDataID;

        public Builder verifyDataID(Integer verifyDataID2) {
            this.verifyDataID = verifyDataID2;
            return this;
        }

        public Builder account(String account2) {
            this.account = account2;
            return this;
        }

        public Builder name(String name2) {
            this.name = name2;
            return this;
        }

        public Builder date(String date2) {
            this.date = date2;
            return this;
        }

        public Builder agreed(Integer agreed2) {
            this.agreed = agreed2;
            return this;
        }

        public Builder company(String company2) {
            this.company = company2;
            return this;
        }

        public Builder title(String title2) {
            this.title = title2;
            return this;
        }

        public Builder address(String address2) {
            this.address = address2;
            return this;
        }

        public Builder ext1(String ext12) {
            this.ext1 = ext12;
            return this;
        }

        public Builder ext2(String ext22) {
            this.ext2 = ext22;
            return this;
        }

        public Builder updateDate(String updateDate2) {
            this.updateDate = updateDate2;
            return this;
        }

        public Builder createData(String createData2) {
            this.createData = createData2;
            return this;
        }

        public AppUnlockUserVerifyDataItem build() {
            return new AppUnlockUserVerifyDataItem(this.verifyDataID, this.account, this.name, this.date, this.agreed, this.company, this.title, this.address, this.ext1, this.ext2, this.updateDate, this.createData, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_AppUnlockUserVerifyDataItem extends ProtoAdapter<AppUnlockUserVerifyDataItem> {
        ProtoAdapter_AppUnlockUserVerifyDataItem() {
            super(FieldEncoding.LENGTH_DELIMITED, AppUnlockUserVerifyDataItem.class);
        }

        public int encodedSize(AppUnlockUserVerifyDataItem value) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            int i8;
            int i9;
            int i10;
            int i11 = 0;
            int encodedSizeWithTag = value.verifyDataID != null ? ProtoAdapter.UINT32.encodedSizeWithTag(1, value.verifyDataID) : 0;
            if (value.account != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.account);
            } else {
                i = 0;
            }
            int i12 = i + encodedSizeWithTag;
            if (value.name != null) {
                i2 = ProtoAdapter.STRING.encodedSizeWithTag(3, value.name);
            } else {
                i2 = 0;
            }
            int i13 = i12 + i2;
            if (value.date != null) {
                i3 = ProtoAdapter.STRING.encodedSizeWithTag(4, value.date);
            } else {
                i3 = 0;
            }
            int i14 = i13 + i3;
            if (value.agreed != null) {
                i4 = ProtoAdapter.UINT32.encodedSizeWithTag(5, value.agreed);
            } else {
                i4 = 0;
            }
            int i15 = i14 + i4;
            if (value.company != null) {
                i5 = ProtoAdapter.STRING.encodedSizeWithTag(6, value.company);
            } else {
                i5 = 0;
            }
            int i16 = i15 + i5;
            if (value.title != null) {
                i6 = ProtoAdapter.STRING.encodedSizeWithTag(7, value.title);
            } else {
                i6 = 0;
            }
            int i17 = i16 + i6;
            if (value.address != null) {
                i7 = ProtoAdapter.STRING.encodedSizeWithTag(8, value.address);
            } else {
                i7 = 0;
            }
            int i18 = i17 + i7;
            if (value.ext1 != null) {
                i8 = ProtoAdapter.STRING.encodedSizeWithTag(9, value.ext1);
            } else {
                i8 = 0;
            }
            int i19 = i18 + i8;
            if (value.ext2 != null) {
                i9 = ProtoAdapter.STRING.encodedSizeWithTag(10, value.ext2);
            } else {
                i9 = 0;
            }
            int i20 = i19 + i9;
            if (value.updateDate != null) {
                i10 = ProtoAdapter.STRING.encodedSizeWithTag(11, value.updateDate);
            } else {
                i10 = 0;
            }
            int i21 = i10 + i20;
            if (value.createData != null) {
                i11 = ProtoAdapter.STRING.encodedSizeWithTag(12, value.createData);
            }
            return i21 + i11 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, AppUnlockUserVerifyDataItem value) throws IOException {
            if (value.verifyDataID != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.verifyDataID);
            }
            if (value.account != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.account);
            }
            if (value.name != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 3, value.name);
            }
            if (value.date != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 4, value.date);
            }
            if (value.agreed != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.agreed);
            }
            if (value.company != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 6, value.company);
            }
            if (value.title != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 7, value.title);
            }
            if (value.address != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 8, value.address);
            }
            if (value.ext1 != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 9, value.ext1);
            }
            if (value.ext2 != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 10, value.ext2);
            }
            if (value.updateDate != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 11, value.updateDate);
            }
            if (value.createData != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 12, value.createData);
            }
            writer.writeBytes(value.unknownFields());
        }

        public AppUnlockUserVerifyDataItem decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.verifyDataID(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 2:
                            builder.account(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            builder.name(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 4:
                            builder.date(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 5:
                            builder.agreed(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 6:
                            builder.company(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 7:
                            builder.title(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 8:
                            builder.address(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 9:
                            builder.ext1(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 10:
                            builder.ext2(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 11:
                            builder.updateDate(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 12:
                            builder.createData(ProtoAdapter.STRING.decode(reader));
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

        public AppUnlockUserVerifyDataItem redact(AppUnlockUserVerifyDataItem value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
