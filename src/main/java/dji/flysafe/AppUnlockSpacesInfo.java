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

public final class AppUnlockSpacesInfo extends Message<AppUnlockSpacesInfo, Builder> {
    public static final ProtoAdapter<AppUnlockSpacesInfo> ADAPTER = new ProtoAdapter_AppUnlockSpacesInfo();
    public static final Integer DEFAULT_BEGINTIME = 0;
    public static final Integer DEFAULT_ENDTIME = 0;
    public static final Integer DEFAULT_ITEMID = 0;
    public static final String DEFAULT_MCSN = "";
    public static final Integer DEFAULT_SPACEID = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 4)
    public final String MCSN;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 1)
    public final Integer beginTime;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 2)
    public final Integer endTime;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 5)
    public final Integer itemID;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 3)
    public final Integer spaceID;

    public AppUnlockSpacesInfo(Integer beginTime2, Integer endTime2, Integer spaceID2, String MCSN2, Integer itemID2) {
        this(beginTime2, endTime2, spaceID2, MCSN2, itemID2, ByteString.EMPTY);
    }

    public AppUnlockSpacesInfo(Integer beginTime2, Integer endTime2, Integer spaceID2, String MCSN2, Integer itemID2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.beginTime = beginTime2;
        this.endTime = endTime2;
        this.spaceID = spaceID2;
        this.MCSN = MCSN2;
        this.itemID = itemID2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.beginTime = this.beginTime;
        builder.endTime = this.endTime;
        builder.spaceID = this.spaceID;
        builder.MCSN = this.MCSN;
        builder.itemID = this.itemID;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AppUnlockSpacesInfo)) {
            return false;
        }
        AppUnlockSpacesInfo o = (AppUnlockSpacesInfo) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.beginTime, o.beginTime) || !Internal.equals(this.endTime, o.endTime) || !Internal.equals(this.spaceID, o.spaceID) || !Internal.equals(this.MCSN, o.MCSN) || !Internal.equals(this.itemID, o.itemID)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.beginTime != null ? this.beginTime.hashCode() : 0)) * 37;
        if (this.endTime != null) {
            i = this.endTime.hashCode();
        } else {
            i = 0;
        }
        int i5 = (hashCode + i) * 37;
        if (this.spaceID != null) {
            i2 = this.spaceID.hashCode();
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 37;
        if (this.MCSN != null) {
            i3 = this.MCSN.hashCode();
        } else {
            i3 = 0;
        }
        int i7 = (i6 + i3) * 37;
        if (this.itemID != null) {
            i4 = this.itemID.hashCode();
        }
        int result2 = i7 + i4;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.beginTime != null) {
            builder.append(", beginTime=").append(this.beginTime);
        }
        if (this.endTime != null) {
            builder.append(", endTime=").append(this.endTime);
        }
        if (this.spaceID != null) {
            builder.append(", spaceID=").append(this.spaceID);
        }
        if (this.MCSN != null) {
            builder.append(", MCSN=").append(this.MCSN);
        }
        if (this.itemID != null) {
            builder.append(", itemID=").append(this.itemID);
        }
        return builder.replace(0, 2, "AppUnlockSpacesInfo{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<AppUnlockSpacesInfo, Builder> {
        public String MCSN;
        public Integer beginTime;
        public Integer endTime;
        public Integer itemID;
        public Integer spaceID;

        public Builder beginTime(Integer beginTime2) {
            this.beginTime = beginTime2;
            return this;
        }

        public Builder endTime(Integer endTime2) {
            this.endTime = endTime2;
            return this;
        }

        public Builder spaceID(Integer spaceID2) {
            this.spaceID = spaceID2;
            return this;
        }

        public Builder MCSN(String MCSN2) {
            this.MCSN = MCSN2;
            return this;
        }

        public Builder itemID(Integer itemID2) {
            this.itemID = itemID2;
            return this;
        }

        public AppUnlockSpacesInfo build() {
            return new AppUnlockSpacesInfo(this.beginTime, this.endTime, this.spaceID, this.MCSN, this.itemID, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_AppUnlockSpacesInfo extends ProtoAdapter<AppUnlockSpacesInfo> {
        ProtoAdapter_AppUnlockSpacesInfo() {
            super(FieldEncoding.LENGTH_DELIMITED, AppUnlockSpacesInfo.class);
        }

        public int encodedSize(AppUnlockSpacesInfo value) {
            int i;
            int i2;
            int i3;
            int i4 = 0;
            int encodedSizeWithTag = value.beginTime != null ? ProtoAdapter.UINT32.encodedSizeWithTag(1, value.beginTime) : 0;
            if (value.endTime != null) {
                i = ProtoAdapter.UINT32.encodedSizeWithTag(2, value.endTime);
            } else {
                i = 0;
            }
            int i5 = i + encodedSizeWithTag;
            if (value.spaceID != null) {
                i2 = ProtoAdapter.UINT32.encodedSizeWithTag(3, value.spaceID);
            } else {
                i2 = 0;
            }
            int i6 = i5 + i2;
            if (value.MCSN != null) {
                i3 = ProtoAdapter.STRING.encodedSizeWithTag(4, value.MCSN);
            } else {
                i3 = 0;
            }
            int i7 = i3 + i6;
            if (value.itemID != null) {
                i4 = ProtoAdapter.UINT32.encodedSizeWithTag(5, value.itemID);
            }
            return i7 + i4 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, AppUnlockSpacesInfo value) throws IOException {
            if (value.beginTime != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.beginTime);
            }
            if (value.endTime != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.endTime);
            }
            if (value.spaceID != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.spaceID);
            }
            if (value.MCSN != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 4, value.MCSN);
            }
            if (value.itemID != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.itemID);
            }
            writer.writeBytes(value.unknownFields());
        }

        public AppUnlockSpacesInfo decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.beginTime(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 2:
                            builder.endTime(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 3:
                            builder.spaceID(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 4:
                            builder.MCSN(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 5:
                            builder.itemID(ProtoAdapter.UINT32.decode(reader));
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

        public AppUnlockSpacesInfo redact(AppUnlockSpacesInfo value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
