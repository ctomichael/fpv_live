package dji.flyrecord;

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

public final class FPVBusinessData extends Message<FPVBusinessData, Builder> {
    public static final ProtoAdapter<FPVBusinessData> ADAPTER = new ProtoAdapter_FPVBusinessData();
    public static final Boolean DEFAULT_CHECKLISTCHANGE = false;
    public static final Boolean DEFAULT_HASPLATFORMDATA = false;
    public static final Boolean DEFAULT_HASTIPS = false;
    public static final Integer DEFAULT_PLATFORM = 0;
    public static final Boolean DEFAULT_STATUSCHANGE = false;
    public static final Integer DEFAULT_VERSION = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 7)
    public final Boolean checkListChange;
    @WireField(adapter = "dji.flyrecord.FPVCheckListItem#ADAPTER", label = WireField.Label.REPEATED, tag = 4)
    public final List<FPVCheckListItem> checklistArray;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 8)
    public final Boolean hasPlatformData;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 5)
    public final Boolean hasTips;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 9)
    public final Integer platform;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BOOL", tag = 6)
    public final Boolean statusChange;
    @WireField(adapter = "dji.flyrecord.FPVStatusBarItem#ADAPTER", tag = 2)
    public final FPVStatusBarItem statusItem;
    @WireField(adapter = "dji.flyrecord.FPVTipsItem#ADAPTER", label = WireField.Label.REPEATED, tag = 3)
    public final List<FPVTipsItem> tipsArray;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 1)
    public final Integer version;

    public FPVBusinessData(Integer version2, FPVStatusBarItem statusItem2, List<FPVTipsItem> tipsArray2, List<FPVCheckListItem> checklistArray2, Boolean hasTips2, Boolean statusChange2, Boolean checkListChange2, Boolean hasPlatformData2, Integer platform2) {
        this(version2, statusItem2, tipsArray2, checklistArray2, hasTips2, statusChange2, checkListChange2, hasPlatformData2, platform2, ByteString.EMPTY);
    }

    public FPVBusinessData(Integer version2, FPVStatusBarItem statusItem2, List<FPVTipsItem> tipsArray2, List<FPVCheckListItem> checklistArray2, Boolean hasTips2, Boolean statusChange2, Boolean checkListChange2, Boolean hasPlatformData2, Integer platform2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.version = version2;
        this.statusItem = statusItem2;
        this.tipsArray = Internal.immutableCopyOf("tipsArray", tipsArray2);
        this.checklistArray = Internal.immutableCopyOf("checklistArray", checklistArray2);
        this.hasTips = hasTips2;
        this.statusChange = statusChange2;
        this.checkListChange = checkListChange2;
        this.hasPlatformData = hasPlatformData2;
        this.platform = platform2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.version = this.version;
        builder.statusItem = this.statusItem;
        builder.tipsArray = Internal.copyOf("tipsArray", this.tipsArray);
        builder.checklistArray = Internal.copyOf("checklistArray", this.checklistArray);
        builder.hasTips = this.hasTips;
        builder.statusChange = this.statusChange;
        builder.checkListChange = this.checkListChange;
        builder.hasPlatformData = this.hasPlatformData;
        builder.platform = this.platform;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FPVBusinessData)) {
            return false;
        }
        FPVBusinessData o = (FPVBusinessData) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.version, o.version) || !Internal.equals(this.statusItem, o.statusItem) || !this.tipsArray.equals(o.tipsArray) || !this.checklistArray.equals(o.checklistArray) || !Internal.equals(this.hasTips, o.hasTips) || !Internal.equals(this.statusChange, o.statusChange) || !Internal.equals(this.checkListChange, o.checkListChange) || !Internal.equals(this.hasPlatformData, o.hasPlatformData) || !Internal.equals(this.platform, o.platform)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.version != null ? this.version.hashCode() : 0)) * 37;
        if (this.statusItem != null) {
            i = this.statusItem.hashCode();
        } else {
            i = 0;
        }
        int hashCode2 = (((((hashCode + i) * 37) + this.tipsArray.hashCode()) * 37) + this.checklistArray.hashCode()) * 37;
        if (this.hasTips != null) {
            i2 = this.hasTips.hashCode();
        } else {
            i2 = 0;
        }
        int i7 = (hashCode2 + i2) * 37;
        if (this.statusChange != null) {
            i3 = this.statusChange.hashCode();
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 37;
        if (this.checkListChange != null) {
            i4 = this.checkListChange.hashCode();
        } else {
            i4 = 0;
        }
        int i9 = (i8 + i4) * 37;
        if (this.hasPlatformData != null) {
            i5 = this.hasPlatformData.hashCode();
        } else {
            i5 = 0;
        }
        int i10 = (i9 + i5) * 37;
        if (this.platform != null) {
            i6 = this.platform.hashCode();
        }
        int result2 = i10 + i6;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.version != null) {
            builder.append(", version=").append(this.version);
        }
        if (this.statusItem != null) {
            builder.append(", statusItem=").append(this.statusItem);
        }
        if (!this.tipsArray.isEmpty()) {
            builder.append(", tipsArray=").append(this.tipsArray);
        }
        if (!this.checklistArray.isEmpty()) {
            builder.append(", checklistArray=").append(this.checklistArray);
        }
        if (this.hasTips != null) {
            builder.append(", hasTips=").append(this.hasTips);
        }
        if (this.statusChange != null) {
            builder.append(", statusChange=").append(this.statusChange);
        }
        if (this.checkListChange != null) {
            builder.append(", checkListChange=").append(this.checkListChange);
        }
        if (this.hasPlatformData != null) {
            builder.append(", hasPlatformData=").append(this.hasPlatformData);
        }
        if (this.platform != null) {
            builder.append(", platform=").append(this.platform);
        }
        return builder.replace(0, 2, "FPVBusinessData{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<FPVBusinessData, Builder> {
        public Boolean checkListChange;
        public List<FPVCheckListItem> checklistArray = Internal.newMutableList();
        public Boolean hasPlatformData;
        public Boolean hasTips;
        public Integer platform;
        public Boolean statusChange;
        public FPVStatusBarItem statusItem;
        public List<FPVTipsItem> tipsArray = Internal.newMutableList();
        public Integer version;

        public Builder version(Integer version2) {
            this.version = version2;
            return this;
        }

        public Builder statusItem(FPVStatusBarItem statusItem2) {
            this.statusItem = statusItem2;
            return this;
        }

        public Builder tipsArray(List<FPVTipsItem> tipsArray2) {
            Internal.checkElementsNotNull(tipsArray2);
            this.tipsArray = tipsArray2;
            return this;
        }

        public Builder checklistArray(List<FPVCheckListItem> checklistArray2) {
            Internal.checkElementsNotNull(checklistArray2);
            this.checklistArray = checklistArray2;
            return this;
        }

        public Builder hasTips(Boolean hasTips2) {
            this.hasTips = hasTips2;
            return this;
        }

        public Builder statusChange(Boolean statusChange2) {
            this.statusChange = statusChange2;
            return this;
        }

        public Builder checkListChange(Boolean checkListChange2) {
            this.checkListChange = checkListChange2;
            return this;
        }

        public Builder hasPlatformData(Boolean hasPlatformData2) {
            this.hasPlatformData = hasPlatformData2;
            return this;
        }

        public Builder platform(Integer platform2) {
            this.platform = platform2;
            return this;
        }

        public FPVBusinessData build() {
            return new FPVBusinessData(this.version, this.statusItem, this.tipsArray, this.checklistArray, this.hasTips, this.statusChange, this.checkListChange, this.hasPlatformData, this.platform, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_FPVBusinessData extends ProtoAdapter<FPVBusinessData> {
        ProtoAdapter_FPVBusinessData() {
            super(FieldEncoding.LENGTH_DELIMITED, FPVBusinessData.class);
        }

        public int encodedSize(FPVBusinessData value) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6 = 0;
            int encodedSizeWithTag = value.version != null ? ProtoAdapter.INT32.encodedSizeWithTag(1, value.version) : 0;
            if (value.statusItem != null) {
                i = FPVStatusBarItem.ADAPTER.encodedSizeWithTag(2, value.statusItem);
            } else {
                i = 0;
            }
            int encodedSizeWithTag2 = FPVCheckListItem.ADAPTER.asRepeated().encodedSizeWithTag(4, value.checklistArray) + encodedSizeWithTag + i + FPVTipsItem.ADAPTER.asRepeated().encodedSizeWithTag(3, value.tipsArray);
            if (value.hasTips != null) {
                i2 = ProtoAdapter.BOOL.encodedSizeWithTag(5, value.hasTips);
            } else {
                i2 = 0;
            }
            int i7 = encodedSizeWithTag2 + i2;
            if (value.statusChange != null) {
                i3 = ProtoAdapter.BOOL.encodedSizeWithTag(6, value.statusChange);
            } else {
                i3 = 0;
            }
            int i8 = i7 + i3;
            if (value.checkListChange != null) {
                i4 = ProtoAdapter.BOOL.encodedSizeWithTag(7, value.checkListChange);
            } else {
                i4 = 0;
            }
            int i9 = i8 + i4;
            if (value.hasPlatformData != null) {
                i5 = ProtoAdapter.BOOL.encodedSizeWithTag(8, value.hasPlatformData);
            } else {
                i5 = 0;
            }
            int i10 = i5 + i9;
            if (value.platform != null) {
                i6 = ProtoAdapter.INT32.encodedSizeWithTag(9, value.platform);
            }
            return i10 + i6 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, FPVBusinessData value) throws IOException {
            if (value.version != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 1, value.version);
            }
            if (value.statusItem != null) {
                FPVStatusBarItem.ADAPTER.encodeWithTag(writer, 2, value.statusItem);
            }
            FPVTipsItem.ADAPTER.asRepeated().encodeWithTag(writer, 3, value.tipsArray);
            FPVCheckListItem.ADAPTER.asRepeated().encodeWithTag(writer, 4, value.checklistArray);
            if (value.hasTips != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 5, value.hasTips);
            }
            if (value.statusChange != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 6, value.statusChange);
            }
            if (value.checkListChange != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 7, value.checkListChange);
            }
            if (value.hasPlatformData != null) {
                ProtoAdapter.BOOL.encodeWithTag(writer, 8, value.hasPlatformData);
            }
            if (value.platform != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 9, value.platform);
            }
            writer.writeBytes(value.unknownFields());
        }

        public FPVBusinessData decode(ProtoReader reader) throws IOException {
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
                            builder.statusItem(FPVStatusBarItem.ADAPTER.decode(reader));
                            break;
                        case 3:
                            builder.tipsArray.add(FPVTipsItem.ADAPTER.decode(reader));
                            break;
                        case 4:
                            builder.checklistArray.add(FPVCheckListItem.ADAPTER.decode(reader));
                            break;
                        case 5:
                            builder.hasTips(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 6:
                            builder.statusChange(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 7:
                            builder.checkListChange(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 8:
                            builder.hasPlatformData(ProtoAdapter.BOOL.decode(reader));
                            break;
                        case 9:
                            builder.platform(ProtoAdapter.INT32.decode(reader));
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

        public FPVBusinessData redact(FPVBusinessData value) {
            Builder builder = value.newBuilder();
            if (builder.statusItem != null) {
                builder.statusItem = FPVStatusBarItem.ADAPTER.redact(builder.statusItem);
            }
            Internal.redactElements(builder.tipsArray, FPVTipsItem.ADAPTER);
            Internal.redactElements(builder.checklistArray, FPVCheckListItem.ADAPTER);
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
