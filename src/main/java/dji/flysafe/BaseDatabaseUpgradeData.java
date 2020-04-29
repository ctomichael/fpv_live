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

public final class BaseDatabaseUpgradeData extends Message<BaseDatabaseUpgradeData, Builder> {
    public static final ProtoAdapter<BaseDatabaseUpgradeData> ADAPTER = new ProtoAdapter_BaseDatabaseUpgradeData();
    public static final Integer DEFAULT_FILESIZE = 0;
    public static final Integer DEFAULT_STATUS = 0;
    public static final Integer DEFAULT_UPGRADESTATE = 0;
    public static final String DEFAULT_UPGRADEURL = "";
    public static final String DEFAULT_VERSION = "";
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 2)
    public final Integer fileSize;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 5)
    public final Integer status;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 4)
    public final Integer upgradeState;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 3)
    public final String upgradeURL;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String version;

    public BaseDatabaseUpgradeData(String version2, Integer fileSize2, String upgradeURL2, Integer upgradeState2, Integer status2) {
        this(version2, fileSize2, upgradeURL2, upgradeState2, status2, ByteString.EMPTY);
    }

    public BaseDatabaseUpgradeData(String version2, Integer fileSize2, String upgradeURL2, Integer upgradeState2, Integer status2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.version = version2;
        this.fileSize = fileSize2;
        this.upgradeURL = upgradeURL2;
        this.upgradeState = upgradeState2;
        this.status = status2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.version = this.version;
        builder.fileSize = this.fileSize;
        builder.upgradeURL = this.upgradeURL;
        builder.upgradeState = this.upgradeState;
        builder.status = this.status;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BaseDatabaseUpgradeData)) {
            return false;
        }
        BaseDatabaseUpgradeData o = (BaseDatabaseUpgradeData) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.version, o.version) || !Internal.equals(this.fileSize, o.fileSize) || !Internal.equals(this.upgradeURL, o.upgradeURL) || !Internal.equals(this.upgradeState, o.upgradeState) || !Internal.equals(this.status, o.status)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.version != null ? this.version.hashCode() : 0)) * 37;
        if (this.fileSize != null) {
            i = this.fileSize.hashCode();
        } else {
            i = 0;
        }
        int i5 = (hashCode + i) * 37;
        if (this.upgradeURL != null) {
            i2 = this.upgradeURL.hashCode();
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 37;
        if (this.upgradeState != null) {
            i3 = this.upgradeState.hashCode();
        } else {
            i3 = 0;
        }
        int i7 = (i6 + i3) * 37;
        if (this.status != null) {
            i4 = this.status.hashCode();
        }
        int result2 = i7 + i4;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.version != null) {
            builder.append(", version=").append(this.version);
        }
        if (this.fileSize != null) {
            builder.append(", fileSize=").append(this.fileSize);
        }
        if (this.upgradeURL != null) {
            builder.append(", upgradeURL=").append(this.upgradeURL);
        }
        if (this.upgradeState != null) {
            builder.append(", upgradeState=").append(this.upgradeState);
        }
        if (this.status != null) {
            builder.append(", status=").append(this.status);
        }
        return builder.replace(0, 2, "BaseDatabaseUpgradeData{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<BaseDatabaseUpgradeData, Builder> {
        public Integer fileSize;
        public Integer status;
        public Integer upgradeState;
        public String upgradeURL;
        public String version;

        public Builder version(String version2) {
            this.version = version2;
            return this;
        }

        public Builder fileSize(Integer fileSize2) {
            this.fileSize = fileSize2;
            return this;
        }

        public Builder upgradeURL(String upgradeURL2) {
            this.upgradeURL = upgradeURL2;
            return this;
        }

        public Builder upgradeState(Integer upgradeState2) {
            this.upgradeState = upgradeState2;
            return this;
        }

        public Builder status(Integer status2) {
            this.status = status2;
            return this;
        }

        public BaseDatabaseUpgradeData build() {
            return new BaseDatabaseUpgradeData(this.version, this.fileSize, this.upgradeURL, this.upgradeState, this.status, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_BaseDatabaseUpgradeData extends ProtoAdapter<BaseDatabaseUpgradeData> {
        ProtoAdapter_BaseDatabaseUpgradeData() {
            super(FieldEncoding.LENGTH_DELIMITED, BaseDatabaseUpgradeData.class);
        }

        public int encodedSize(BaseDatabaseUpgradeData value) {
            int i;
            int i2;
            int i3;
            int i4 = 0;
            int encodedSizeWithTag = value.version != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.version) : 0;
            if (value.fileSize != null) {
                i = ProtoAdapter.UINT32.encodedSizeWithTag(2, value.fileSize);
            } else {
                i = 0;
            }
            int i5 = i + encodedSizeWithTag;
            if (value.upgradeURL != null) {
                i2 = ProtoAdapter.STRING.encodedSizeWithTag(3, value.upgradeURL);
            } else {
                i2 = 0;
            }
            int i6 = i5 + i2;
            if (value.upgradeState != null) {
                i3 = ProtoAdapter.UINT32.encodedSizeWithTag(4, value.upgradeState);
            } else {
                i3 = 0;
            }
            int i7 = i3 + i6;
            if (value.status != null) {
                i4 = ProtoAdapter.UINT32.encodedSizeWithTag(5, value.status);
            }
            return i7 + i4 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, BaseDatabaseUpgradeData value) throws IOException {
            if (value.version != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.version);
            }
            if (value.fileSize != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.fileSize);
            }
            if (value.upgradeURL != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 3, value.upgradeURL);
            }
            if (value.upgradeState != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.upgradeState);
            }
            if (value.status != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.status);
            }
            writer.writeBytes(value.unknownFields());
        }

        public BaseDatabaseUpgradeData decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.version(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.fileSize(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 3:
                            builder.upgradeURL(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 4:
                            builder.upgradeState(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 5:
                            builder.status(ProtoAdapter.UINT32.decode(reader));
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

        public BaseDatabaseUpgradeData redact(BaseDatabaseUpgradeData value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
