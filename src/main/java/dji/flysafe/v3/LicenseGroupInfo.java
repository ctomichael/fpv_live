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

public final class LicenseGroupInfo extends Message<LicenseGroupInfo, Builder> {
    public static final ProtoAdapter<LicenseGroupInfo> ADAPTER = new ProtoAdapter_LicenseGroupInfo();
    public static final Integer DEFAULT_ID = 0;
    public static final Integer DEFAULT_LICENSES_COUNT = 0;
    public static final ByteString DEFAULT_SN = ByteString.EMPTY;
    public static final Integer DEFAULT_TIMESTAMP = 0;
    public static final Long DEFAULT_USER_ID = 0L;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 1)
    public final Integer id;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 5)
    public final Integer licenses_count;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#BYTES", tag = 3)
    public final ByteString sn;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 2)
    public final Integer timestamp;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT64", tag = 4)
    public final Long user_id;

    public LicenseGroupInfo(Integer id2, Integer timestamp2, ByteString sn2, Long user_id2, Integer licenses_count2) {
        this(id2, timestamp2, sn2, user_id2, licenses_count2, ByteString.EMPTY);
    }

    public LicenseGroupInfo(Integer id2, Integer timestamp2, ByteString sn2, Long user_id2, Integer licenses_count2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.id = id2;
        this.timestamp = timestamp2;
        this.sn = sn2;
        this.user_id = user_id2;
        this.licenses_count = licenses_count2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.id = this.id;
        builder.timestamp = this.timestamp;
        builder.sn = this.sn;
        builder.user_id = this.user_id;
        builder.licenses_count = this.licenses_count;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LicenseGroupInfo)) {
            return false;
        }
        LicenseGroupInfo o = (LicenseGroupInfo) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.id, o.id) || !Internal.equals(this.timestamp, o.timestamp) || !Internal.equals(this.sn, o.sn) || !Internal.equals(this.user_id, o.user_id) || !Internal.equals(this.licenses_count, o.licenses_count)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.id != null ? this.id.hashCode() : 0)) * 37;
        if (this.timestamp != null) {
            i = this.timestamp.hashCode();
        } else {
            i = 0;
        }
        int i5 = (hashCode + i) * 37;
        if (this.sn != null) {
            i2 = this.sn.hashCode();
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 37;
        if (this.user_id != null) {
            i3 = this.user_id.hashCode();
        } else {
            i3 = 0;
        }
        int i7 = (i6 + i3) * 37;
        if (this.licenses_count != null) {
            i4 = this.licenses_count.hashCode();
        }
        int result2 = i7 + i4;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.id != null) {
            builder.append(", id=").append(this.id);
        }
        if (this.timestamp != null) {
            builder.append(", timestamp=").append(this.timestamp);
        }
        if (this.sn != null) {
            builder.append(", sn=").append(this.sn);
        }
        if (this.user_id != null) {
            builder.append(", user_id=").append(this.user_id);
        }
        if (this.licenses_count != null) {
            builder.append(", licenses_count=").append(this.licenses_count);
        }
        return builder.replace(0, 2, "LicenseGroupInfo{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<LicenseGroupInfo, Builder> {
        public Integer id;
        public Integer licenses_count;
        public ByteString sn;
        public Integer timestamp;
        public Long user_id;

        public Builder id(Integer id2) {
            this.id = id2;
            return this;
        }

        public Builder timestamp(Integer timestamp2) {
            this.timestamp = timestamp2;
            return this;
        }

        public Builder sn(ByteString sn2) {
            this.sn = sn2;
            return this;
        }

        public Builder user_id(Long user_id2) {
            this.user_id = user_id2;
            return this;
        }

        public Builder licenses_count(Integer licenses_count2) {
            this.licenses_count = licenses_count2;
            return this;
        }

        public LicenseGroupInfo build() {
            return new LicenseGroupInfo(this.id, this.timestamp, this.sn, this.user_id, this.licenses_count, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_LicenseGroupInfo extends ProtoAdapter<LicenseGroupInfo> {
        ProtoAdapter_LicenseGroupInfo() {
            super(FieldEncoding.LENGTH_DELIMITED, LicenseGroupInfo.class);
        }

        public int encodedSize(LicenseGroupInfo value) {
            int i;
            int i2;
            int i3;
            int i4 = 0;
            int encodedSizeWithTag = value.id != null ? ProtoAdapter.UINT32.encodedSizeWithTag(1, value.id) : 0;
            if (value.timestamp != null) {
                i = ProtoAdapter.UINT32.encodedSizeWithTag(2, value.timestamp);
            } else {
                i = 0;
            }
            int i5 = i + encodedSizeWithTag;
            if (value.sn != null) {
                i2 = ProtoAdapter.BYTES.encodedSizeWithTag(3, value.sn);
            } else {
                i2 = 0;
            }
            int i6 = i5 + i2;
            if (value.user_id != null) {
                i3 = ProtoAdapter.UINT64.encodedSizeWithTag(4, value.user_id);
            } else {
                i3 = 0;
            }
            int i7 = i3 + i6;
            if (value.licenses_count != null) {
                i4 = ProtoAdapter.UINT32.encodedSizeWithTag(5, value.licenses_count);
            }
            return i7 + i4 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, LicenseGroupInfo value) throws IOException {
            if (value.id != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.id);
            }
            if (value.timestamp != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.timestamp);
            }
            if (value.sn != null) {
                ProtoAdapter.BYTES.encodeWithTag(writer, 3, value.sn);
            }
            if (value.user_id != null) {
                ProtoAdapter.UINT64.encodeWithTag(writer, 4, value.user_id);
            }
            if (value.licenses_count != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.licenses_count);
            }
            writer.writeBytes(value.unknownFields());
        }

        public LicenseGroupInfo decode(ProtoReader reader) throws IOException {
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
                            builder.timestamp(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 3:
                            builder.sn(ProtoAdapter.BYTES.decode(reader));
                            break;
                        case 4:
                            builder.user_id(ProtoAdapter.UINT64.decode(reader));
                            break;
                        case 5:
                            builder.licenses_count(ProtoAdapter.UINT32.decode(reader));
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

        public LicenseGroupInfo redact(LicenseGroupInfo value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
