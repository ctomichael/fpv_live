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

public final class SpeicalAreaTermsInfo extends Message<SpeicalAreaTermsInfo, Builder> {
    public static final ProtoAdapter<SpeicalAreaTermsInfo> ADAPTER = new ProtoAdapter_SpeicalAreaTermsInfo();
    public static final Integer DEFAULT_AREAID = 0;
    public static final Integer DEFAULT_BEGINTIME = 0;
    public static final Integer DEFAULT_ENDTIME = 0;
    public static final String DEFAULT_MCSN = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 4)
    public final String MCSN;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 3)
    public final Integer areaID;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 1)
    public final Integer beginTime;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 2)
    public final Integer endTime;

    public SpeicalAreaTermsInfo(Integer beginTime2, Integer endTime2, Integer areaID2, String MCSN2) {
        this(beginTime2, endTime2, areaID2, MCSN2, ByteString.EMPTY);
    }

    public SpeicalAreaTermsInfo(Integer beginTime2, Integer endTime2, Integer areaID2, String MCSN2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.beginTime = beginTime2;
        this.endTime = endTime2;
        this.areaID = areaID2;
        this.MCSN = MCSN2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.beginTime = this.beginTime;
        builder.endTime = this.endTime;
        builder.areaID = this.areaID;
        builder.MCSN = this.MCSN;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SpeicalAreaTermsInfo)) {
            return false;
        }
        SpeicalAreaTermsInfo o = (SpeicalAreaTermsInfo) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.beginTime, o.beginTime) || !Internal.equals(this.endTime, o.endTime) || !Internal.equals(this.areaID, o.areaID) || !Internal.equals(this.MCSN, o.MCSN)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.beginTime != null ? this.beginTime.hashCode() : 0)) * 37;
        if (this.endTime != null) {
            i = this.endTime.hashCode();
        } else {
            i = 0;
        }
        int i4 = (hashCode + i) * 37;
        if (this.areaID != null) {
            i2 = this.areaID.hashCode();
        } else {
            i2 = 0;
        }
        int i5 = (i4 + i2) * 37;
        if (this.MCSN != null) {
            i3 = this.MCSN.hashCode();
        }
        int result2 = i5 + i3;
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
        if (this.areaID != null) {
            builder.append(", areaID=").append(this.areaID);
        }
        if (this.MCSN != null) {
            builder.append(", MCSN=").append(this.MCSN);
        }
        return builder.replace(0, 2, "SpeicalAreaTermsInfo{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<SpeicalAreaTermsInfo, Builder> {
        public String MCSN;
        public Integer areaID;
        public Integer beginTime;
        public Integer endTime;

        public Builder beginTime(Integer beginTime2) {
            this.beginTime = beginTime2;
            return this;
        }

        public Builder endTime(Integer endTime2) {
            this.endTime = endTime2;
            return this;
        }

        public Builder areaID(Integer areaID2) {
            this.areaID = areaID2;
            return this;
        }

        public Builder MCSN(String MCSN2) {
            this.MCSN = MCSN2;
            return this;
        }

        public SpeicalAreaTermsInfo build() {
            return new SpeicalAreaTermsInfo(this.beginTime, this.endTime, this.areaID, this.MCSN, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_SpeicalAreaTermsInfo extends ProtoAdapter<SpeicalAreaTermsInfo> {
        ProtoAdapter_SpeicalAreaTermsInfo() {
            super(FieldEncoding.LENGTH_DELIMITED, SpeicalAreaTermsInfo.class);
        }

        public int encodedSize(SpeicalAreaTermsInfo value) {
            int i;
            int i2;
            int i3 = 0;
            int encodedSizeWithTag = value.beginTime != null ? ProtoAdapter.UINT32.encodedSizeWithTag(1, value.beginTime) : 0;
            if (value.endTime != null) {
                i = ProtoAdapter.UINT32.encodedSizeWithTag(2, value.endTime);
            } else {
                i = 0;
            }
            int i4 = i + encodedSizeWithTag;
            if (value.areaID != null) {
                i2 = ProtoAdapter.UINT32.encodedSizeWithTag(3, value.areaID);
            } else {
                i2 = 0;
            }
            int i5 = i2 + i4;
            if (value.MCSN != null) {
                i3 = ProtoAdapter.STRING.encodedSizeWithTag(4, value.MCSN);
            }
            return i5 + i3 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, SpeicalAreaTermsInfo value) throws IOException {
            if (value.beginTime != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.beginTime);
            }
            if (value.endTime != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.endTime);
            }
            if (value.areaID != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.areaID);
            }
            if (value.MCSN != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 4, value.MCSN);
            }
            writer.writeBytes(value.unknownFields());
        }

        public SpeicalAreaTermsInfo decode(ProtoReader reader) throws IOException {
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
                            builder.areaID(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 4:
                            builder.MCSN(ProtoAdapter.STRING.decode(reader));
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

        public SpeicalAreaTermsInfo redact(SpeicalAreaTermsInfo value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
