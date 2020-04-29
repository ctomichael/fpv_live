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

public final class AppUnlockVerifyResponseModel extends Message<AppUnlockVerifyResponseModel, Builder> {
    public static final ProtoAdapter<AppUnlockVerifyResponseModel> ADAPTER = new ProtoAdapter_AppUnlockVerifyResponseModel();
    public static final String DEFAULT_COUNTRY = "";
    public static final String DEFAULT_SIGNATURE = "";
    public static final Integer DEFAULT_TIME = 0;
    public static final String DEFAULT_TYPE = "";
    public static final String DEFAULT_URL = "";
    public static final String DEFAULT_URLKEY = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String country;
    @WireField(adapter = "dji.flysafe.AppUnlockUserVerifyExtraData#ADAPTER", tag = 8)
    public final AppUnlockUserVerifyExtraData extraData;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 3)
    public final String signature;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 2)
    public final Integer time;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 4)
    public final String type;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 5)
    public final String url;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 6)
    public final String urlKey;
    @WireField(adapter = "dji.flysafe.AppUnlockUserVerifyDataItem#ADAPTER", label = WireField.Label.REPEATED, tag = 7)
    public final List<AppUnlockUserVerifyDataItem> verifyDataItems;

    public AppUnlockVerifyResponseModel(String country2, Integer time2, String signature2, String type2, String url2, String urlKey2, List<AppUnlockUserVerifyDataItem> verifyDataItems2, AppUnlockUserVerifyExtraData extraData2) {
        this(country2, time2, signature2, type2, url2, urlKey2, verifyDataItems2, extraData2, ByteString.EMPTY);
    }

    public AppUnlockVerifyResponseModel(String country2, Integer time2, String signature2, String type2, String url2, String urlKey2, List<AppUnlockUserVerifyDataItem> verifyDataItems2, AppUnlockUserVerifyExtraData extraData2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.country = country2;
        this.time = time2;
        this.signature = signature2;
        this.type = type2;
        this.url = url2;
        this.urlKey = urlKey2;
        this.verifyDataItems = Internal.immutableCopyOf("verifyDataItems", verifyDataItems2);
        this.extraData = extraData2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.country = this.country;
        builder.time = this.time;
        builder.signature = this.signature;
        builder.type = this.type;
        builder.url = this.url;
        builder.urlKey = this.urlKey;
        builder.verifyDataItems = Internal.copyOf("verifyDataItems", this.verifyDataItems);
        builder.extraData = this.extraData;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AppUnlockVerifyResponseModel)) {
            return false;
        }
        AppUnlockVerifyResponseModel o = (AppUnlockVerifyResponseModel) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.country, o.country) || !Internal.equals(this.time, o.time) || !Internal.equals(this.signature, o.signature) || !Internal.equals(this.type, o.type) || !Internal.equals(this.url, o.url) || !Internal.equals(this.urlKey, o.urlKey) || !this.verifyDataItems.equals(o.verifyDataItems) || !Internal.equals(this.extraData, o.extraData)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.country != null ? this.country.hashCode() : 0)) * 37;
        if (this.time != null) {
            i = this.time.hashCode();
        } else {
            i = 0;
        }
        int i7 = (hashCode + i) * 37;
        if (this.signature != null) {
            i2 = this.signature.hashCode();
        } else {
            i2 = 0;
        }
        int i8 = (i7 + i2) * 37;
        if (this.type != null) {
            i3 = this.type.hashCode();
        } else {
            i3 = 0;
        }
        int i9 = (i8 + i3) * 37;
        if (this.url != null) {
            i4 = this.url.hashCode();
        } else {
            i4 = 0;
        }
        int i10 = (i9 + i4) * 37;
        if (this.urlKey != null) {
            i5 = this.urlKey.hashCode();
        } else {
            i5 = 0;
        }
        int hashCode2 = (((i10 + i5) * 37) + this.verifyDataItems.hashCode()) * 37;
        if (this.extraData != null) {
            i6 = this.extraData.hashCode();
        }
        int result2 = hashCode2 + i6;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.country != null) {
            builder.append(", country=").append(this.country);
        }
        if (this.time != null) {
            builder.append(", time=").append(this.time);
        }
        if (this.signature != null) {
            builder.append(", signature=").append(this.signature);
        }
        if (this.type != null) {
            builder.append(", type=").append(this.type);
        }
        if (this.url != null) {
            builder.append(", url=").append(this.url);
        }
        if (this.urlKey != null) {
            builder.append(", urlKey=").append(this.urlKey);
        }
        if (!this.verifyDataItems.isEmpty()) {
            builder.append(", verifyDataItems=").append(this.verifyDataItems);
        }
        if (this.extraData != null) {
            builder.append(", extraData=").append(this.extraData);
        }
        return builder.replace(0, 2, "AppUnlockVerifyResponseModel{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<AppUnlockVerifyResponseModel, Builder> {
        public String country;
        public AppUnlockUserVerifyExtraData extraData;
        public String signature;
        public Integer time;
        public String type;
        public String url;
        public String urlKey;
        public List<AppUnlockUserVerifyDataItem> verifyDataItems = Internal.newMutableList();

        public Builder country(String country2) {
            this.country = country2;
            return this;
        }

        public Builder time(Integer time2) {
            this.time = time2;
            return this;
        }

        public Builder signature(String signature2) {
            this.signature = signature2;
            return this;
        }

        public Builder type(String type2) {
            this.type = type2;
            return this;
        }

        public Builder url(String url2) {
            this.url = url2;
            return this;
        }

        public Builder urlKey(String urlKey2) {
            this.urlKey = urlKey2;
            return this;
        }

        public Builder verifyDataItems(List<AppUnlockUserVerifyDataItem> verifyDataItems2) {
            Internal.checkElementsNotNull(verifyDataItems2);
            this.verifyDataItems = verifyDataItems2;
            return this;
        }

        public Builder extraData(AppUnlockUserVerifyExtraData extraData2) {
            this.extraData = extraData2;
            return this;
        }

        public AppUnlockVerifyResponseModel build() {
            return new AppUnlockVerifyResponseModel(this.country, this.time, this.signature, this.type, this.url, this.urlKey, this.verifyDataItems, this.extraData, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_AppUnlockVerifyResponseModel extends ProtoAdapter<AppUnlockVerifyResponseModel> {
        ProtoAdapter_AppUnlockVerifyResponseModel() {
            super(FieldEncoding.LENGTH_DELIMITED, AppUnlockVerifyResponseModel.class);
        }

        public int encodedSize(AppUnlockVerifyResponseModel value) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6 = 0;
            int encodedSizeWithTag = value.country != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.country) : 0;
            if (value.time != null) {
                i = ProtoAdapter.UINT32.encodedSizeWithTag(2, value.time);
            } else {
                i = 0;
            }
            int i7 = i + encodedSizeWithTag;
            if (value.signature != null) {
                i2 = ProtoAdapter.STRING.encodedSizeWithTag(3, value.signature);
            } else {
                i2 = 0;
            }
            int i8 = i7 + i2;
            if (value.type != null) {
                i3 = ProtoAdapter.STRING.encodedSizeWithTag(4, value.type);
            } else {
                i3 = 0;
            }
            int i9 = i8 + i3;
            if (value.url != null) {
                i4 = ProtoAdapter.STRING.encodedSizeWithTag(5, value.url);
            } else {
                i4 = 0;
            }
            int i10 = i9 + i4;
            if (value.urlKey != null) {
                i5 = ProtoAdapter.STRING.encodedSizeWithTag(6, value.urlKey);
            } else {
                i5 = 0;
            }
            int encodedSizeWithTag2 = i5 + i10 + AppUnlockUserVerifyDataItem.ADAPTER.asRepeated().encodedSizeWithTag(7, value.verifyDataItems);
            if (value.extraData != null) {
                i6 = AppUnlockUserVerifyExtraData.ADAPTER.encodedSizeWithTag(8, value.extraData);
            }
            return encodedSizeWithTag2 + i6 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, AppUnlockVerifyResponseModel value) throws IOException {
            if (value.country != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.country);
            }
            if (value.time != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.time);
            }
            if (value.signature != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 3, value.signature);
            }
            if (value.type != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 4, value.type);
            }
            if (value.url != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 5, value.url);
            }
            if (value.urlKey != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 6, value.urlKey);
            }
            AppUnlockUserVerifyDataItem.ADAPTER.asRepeated().encodeWithTag(writer, 7, value.verifyDataItems);
            if (value.extraData != null) {
                AppUnlockUserVerifyExtraData.ADAPTER.encodeWithTag(writer, 8, value.extraData);
            }
            writer.writeBytes(value.unknownFields());
        }

        public AppUnlockVerifyResponseModel decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.country(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.time(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 3:
                            builder.signature(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 4:
                            builder.type(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 5:
                            builder.url(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 6:
                            builder.urlKey(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 7:
                            builder.verifyDataItems.add(AppUnlockUserVerifyDataItem.ADAPTER.decode(reader));
                            break;
                        case 8:
                            builder.extraData(AppUnlockUserVerifyExtraData.ADAPTER.decode(reader));
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

        public AppUnlockVerifyResponseModel redact(AppUnlockVerifyResponseModel value) {
            Builder builder = value.newBuilder();
            Internal.redactElements(builder.verifyDataItems, AppUnlockUserVerifyDataItem.ADAPTER);
            if (builder.extraData != null) {
                builder.extraData = AppUnlockUserVerifyExtraData.ADAPTER.redact(builder.extraData);
            }
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
