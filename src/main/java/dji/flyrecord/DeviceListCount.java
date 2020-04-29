package dji.flyrecord;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import okio.ByteString;

public final class DeviceListCount extends Message<DeviceListCount, Builder> {
    public static final ProtoAdapter<DeviceListCount> ADAPTER = new ProtoAdapter_DeviceListCount();
    public static final String DEFAULT_DEVICEID = "";
    public static final String DEFAULT_DEVICENAME = "";
    public static final Integer DEFAULT_RECORDNUM = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 1)
    public final String deviceId;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 2)
    public final String deviceName;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#INT32", tag = 3)
    public final Integer recordNum;

    public DeviceListCount(String deviceId2, String deviceName2, Integer recordNum2) {
        this(deviceId2, deviceName2, recordNum2, ByteString.EMPTY);
    }

    public DeviceListCount(String deviceId2, String deviceName2, Integer recordNum2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.deviceId = deviceId2;
        this.deviceName = deviceName2;
        this.recordNum = recordNum2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.deviceId = this.deviceId;
        builder.deviceName = this.deviceName;
        builder.recordNum = this.recordNum;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeviceListCount)) {
            return false;
        }
        DeviceListCount o = (DeviceListCount) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.deviceId, o.deviceId) || !Internal.equals(this.deviceName, o.deviceName) || !Internal.equals(this.recordNum, o.recordNum)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2 = 0;
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int hashCode = ((unknownFields().hashCode() * 37) + (this.deviceId != null ? this.deviceId.hashCode() : 0)) * 37;
        if (this.deviceName != null) {
            i = this.deviceName.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.recordNum != null) {
            i2 = this.recordNum.hashCode();
        }
        int result2 = i3 + i2;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.deviceId != null) {
            builder.append(", deviceId=").append(this.deviceId);
        }
        if (this.deviceName != null) {
            builder.append(", deviceName=").append(this.deviceName);
        }
        if (this.recordNum != null) {
            builder.append(", recordNum=").append(this.recordNum);
        }
        return builder.replace(0, 2, "DeviceListCount{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<DeviceListCount, Builder> {
        public String deviceId;
        public String deviceName;
        public Integer recordNum;

        public Builder deviceId(String deviceId2) {
            this.deviceId = deviceId2;
            return this;
        }

        public Builder deviceName(String deviceName2) {
            this.deviceName = deviceName2;
            return this;
        }

        public Builder recordNum(Integer recordNum2) {
            this.recordNum = recordNum2;
            return this;
        }

        public DeviceListCount build() {
            return new DeviceListCount(this.deviceId, this.deviceName, this.recordNum, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_DeviceListCount extends ProtoAdapter<DeviceListCount> {
        ProtoAdapter_DeviceListCount() {
            super(FieldEncoding.LENGTH_DELIMITED, DeviceListCount.class);
        }

        public int encodedSize(DeviceListCount value) {
            int i;
            int i2 = 0;
            int encodedSizeWithTag = value.deviceId != null ? ProtoAdapter.STRING.encodedSizeWithTag(1, value.deviceId) : 0;
            if (value.deviceName != null) {
                i = ProtoAdapter.STRING.encodedSizeWithTag(2, value.deviceName);
            } else {
                i = 0;
            }
            int i3 = encodedSizeWithTag + i;
            if (value.recordNum != null) {
                i2 = ProtoAdapter.INT32.encodedSizeWithTag(3, value.recordNum);
            }
            return i3 + i2 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, DeviceListCount value) throws IOException {
            if (value.deviceId != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 1, value.deviceId);
            }
            if (value.deviceName != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 2, value.deviceName);
            }
            if (value.recordNum != null) {
                ProtoAdapter.INT32.encodeWithTag(writer, 3, value.recordNum);
            }
            writer.writeBytes(value.unknownFields());
        }

        public DeviceListCount decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.deviceId(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 2:
                            builder.deviceName(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 3:
                            builder.recordNum(ProtoAdapter.INT32.decode(reader));
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

        public DeviceListCount redact(DeviceListCount value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
