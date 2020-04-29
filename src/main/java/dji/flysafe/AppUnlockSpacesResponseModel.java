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

public final class AppUnlockSpacesResponseModel extends Message<AppUnlockSpacesResponseModel, Builder> {
    public static final ProtoAdapter<AppUnlockSpacesResponseModel> ADAPTER = new ProtoAdapter_AppUnlockSpacesResponseModel();
    public static final String DEFAULT_SIGNATURE = "";
    public static final AppUnlockSpaceDataStatus DEFAULT_STATUS = AppUnlockSpaceDataStatus.Inactivate;
    public static final Integer DEFAULT_TIME = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "dji.flysafe.AppUnlockSapceExtraData#ADAPTER", tag = 4)
    public final AppUnlockSapceExtraData extraData;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String signature;
    @WireField(adapter = "dji.flysafe.AppUnlockSpaceDataStatus#ADAPTER", tag = 3)
    public final AppUnlockSpaceDataStatus status;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 1)
    public final Integer time;
    @WireField(adapter = "dji.flysafe.AppUnlockSpaceDataItem#ADAPTER", label = WireField.Label.REPEATED, tag = 5)
    public final List<AppUnlockSpaceDataItem> unlockSpaceItems;

    public AppUnlockSpacesResponseModel(Integer time2, String signature2, AppUnlockSpaceDataStatus status2, AppUnlockSapceExtraData extraData2, List<AppUnlockSpaceDataItem> unlockSpaceItems2) {
        this(time2, signature2, status2, extraData2, unlockSpaceItems2, ByteString.EMPTY);
    }

    public AppUnlockSpacesResponseModel(Integer time2, String signature2, AppUnlockSpaceDataStatus status2, AppUnlockSapceExtraData extraData2, List<AppUnlockSpaceDataItem> unlockSpaceItems2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.time = time2;
        this.signature = signature2;
        this.status = status2;
        this.extraData = extraData2;
        this.unlockSpaceItems = Internal.immutableCopyOf("unlockSpaceItems", unlockSpaceItems2);
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.time = this.time;
        builder.signature = this.signature;
        builder.status = this.status;
        builder.extraData = this.extraData;
        builder.unlockSpaceItems = Internal.copyOf("unlockSpaceItems", this.unlockSpaceItems);
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AppUnlockSpacesResponseModel)) {
            return false;
        }
        AppUnlockSpacesResponseModel o = (AppUnlockSpacesResponseModel) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.time, o.time) || !Internal.equals(this.signature, o.signature) || !Internal.equals(this.status, o.status) || !Internal.equals(this.extraData, o.extraData) || !this.unlockSpaceItems.equals(o.unlockSpaceItems)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.time != null ? this.time.hashCode() : 0)) * 37;
        if (this.signature != null) {
            i = this.signature.hashCode();
        } else {
            i = 0;
        }
        int i4 = (hashCode + i) * 37;
        if (this.status != null) {
            i2 = this.status.hashCode();
        } else {
            i2 = 0;
        }
        int i5 = (i4 + i2) * 37;
        if (this.extraData != null) {
            i3 = this.extraData.hashCode();
        }
        int result2 = ((i5 + i3) * 37) + this.unlockSpaceItems.hashCode();
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.time != null) {
            builder.append(", time=").append(this.time);
        }
        if (this.signature != null) {
            builder.append(", signature=").append(this.signature);
        }
        if (this.status != null) {
            builder.append(", status=").append(this.status);
        }
        if (this.extraData != null) {
            builder.append(", extraData=").append(this.extraData);
        }
        if (!this.unlockSpaceItems.isEmpty()) {
            builder.append(", unlockSpaceItems=").append(this.unlockSpaceItems);
        }
        return builder.replace(0, 2, "AppUnlockSpacesResponseModel{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<AppUnlockSpacesResponseModel, Builder> {
        public AppUnlockSapceExtraData extraData;
        public String signature;
        public AppUnlockSpaceDataStatus status;
        public Integer time;
        public List<AppUnlockSpaceDataItem> unlockSpaceItems = Internal.newMutableList();

        public Builder time(Integer time2) {
            this.time = time2;
            return this;
        }

        public Builder signature(String signature2) {
            this.signature = signature2;
            return this;
        }

        public Builder status(AppUnlockSpaceDataStatus status2) {
            this.status = status2;
            return this;
        }

        public Builder extraData(AppUnlockSapceExtraData extraData2) {
            this.extraData = extraData2;
            return this;
        }

        public Builder unlockSpaceItems(List<AppUnlockSpaceDataItem> unlockSpaceItems2) {
            Internal.checkElementsNotNull(unlockSpaceItems2);
            this.unlockSpaceItems = unlockSpaceItems2;
            return this;
        }

        public AppUnlockSpacesResponseModel build() {
            return new AppUnlockSpacesResponseModel(this.time, this.signature, this.status, this.extraData, this.unlockSpaceItems, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_AppUnlockSpacesResponseModel extends ProtoAdapter<AppUnlockSpacesResponseModel> {
        ProtoAdapter_AppUnlockSpacesResponseModel() {
            super(FieldEncoding.LENGTH_DELIMITED, AppUnlockSpacesResponseModel.class);
        }

        public int encodedSize(AppUnlockSpacesResponseModel value) {
            int i;
            int i2;
            int i3 = 0;
            int encodedSizeWithTag = value.time != null ? ProtoAdapter.UINT32.encodedSizeWithTag(1, value.time) : 0;
            if (value.signature != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.signature);
            } else {
                i = 0;
            }
            int i4 = i + encodedSizeWithTag;
            if (value.status != null) {
                i2 = AppUnlockSpaceDataStatus.ADAPTER.encodedSizeWithTag(3, value.status);
            } else {
                i2 = 0;
            }
            int i5 = i2 + i4;
            if (value.extraData != null) {
                i3 = AppUnlockSapceExtraData.ADAPTER.encodedSizeWithTag(4, value.extraData);
            }
            return i5 + i3 + AppUnlockSpaceDataItem.ADAPTER.asRepeated().encodedSizeWithTag(5, value.unlockSpaceItems) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, AppUnlockSpacesResponseModel value) throws IOException {
            if (value.time != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.time);
            }
            if (value.signature != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.signature);
            }
            if (value.status != null) {
                AppUnlockSpaceDataStatus.ADAPTER.encodeWithTag(writer, 3, value.status);
            }
            if (value.extraData != null) {
                AppUnlockSapceExtraData.ADAPTER.encodeWithTag(writer, 4, value.extraData);
            }
            AppUnlockSpaceDataItem.ADAPTER.asRepeated().encodeWithTag(writer, 5, value.unlockSpaceItems);
            writer.writeBytes(value.unknownFields());
        }

        public AppUnlockSpacesResponseModel decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.time(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 2:
                            builder.signature(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            try {
                                builder.status(AppUnlockSpaceDataStatus.ADAPTER.decode(reader));
                                break;
                            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
                                builder.addUnknownField(tag, FieldEncoding.VARINT, Long.valueOf((long) e.value));
                                break;
                            }
                        case 4:
                            builder.extraData(AppUnlockSapceExtraData.ADAPTER.decode(reader));
                            break;
                        case 5:
                            builder.unlockSpaceItems.add(AppUnlockSpaceDataItem.ADAPTER.decode(reader));
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

        public AppUnlockSpacesResponseModel redact(AppUnlockSpacesResponseModel value) {
            Builder builder = value.newBuilder();
            if (builder.extraData != null) {
                builder.extraData = AppUnlockSapceExtraData.ADAPTER.redact(builder.extraData);
            }
            Internal.redactElements(builder.unlockSpaceItems, AppUnlockSpaceDataItem.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
