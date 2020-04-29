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

public final class SupervisorRecordContext extends Message<SupervisorRecordContext, Builder> {
    public static final ProtoAdapter<SupervisorRecordContext> ADAPTER = new ProtoAdapter_SupervisorRecordContext();
    public static final String DEFAULT_APP_DYNAMIC_DB_UUID = "";
    public static final String DEFAULT_DJI_FLIGHT_DYNAMIC_DB_UUID = "";
    public static final Integer DEFAULT_LIMIT_STATE = 0;
    public static final Integer DEFAULT_TFR_UPDATE_STATE = 0;
    public static final Integer DEFAULT_UPLOAD_NUMBER = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 5)
    public final String app_dynamic_db_uuid;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#STRING", tag = 6)
    public final String dji_flight_dynamic_db_uuid;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 4)
    public final Integer limit_state;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 2)
    public final Integer tfr_update_state;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 3)
    public final Integer upload_number;

    public SupervisorRecordContext(Integer tfr_update_state2, Integer upload_number2, Integer limit_state2, String app_dynamic_db_uuid2, String dji_flight_dynamic_db_uuid2) {
        this(tfr_update_state2, upload_number2, limit_state2, app_dynamic_db_uuid2, dji_flight_dynamic_db_uuid2, ByteString.EMPTY);
    }

    public SupervisorRecordContext(Integer tfr_update_state2, Integer upload_number2, Integer limit_state2, String app_dynamic_db_uuid2, String dji_flight_dynamic_db_uuid2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.tfr_update_state = tfr_update_state2;
        this.upload_number = upload_number2;
        this.limit_state = limit_state2;
        this.app_dynamic_db_uuid = app_dynamic_db_uuid2;
        this.dji_flight_dynamic_db_uuid = dji_flight_dynamic_db_uuid2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.tfr_update_state = this.tfr_update_state;
        builder.upload_number = this.upload_number;
        builder.limit_state = this.limit_state;
        builder.app_dynamic_db_uuid = this.app_dynamic_db_uuid;
        builder.dji_flight_dynamic_db_uuid = this.dji_flight_dynamic_db_uuid;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SupervisorRecordContext)) {
            return false;
        }
        SupervisorRecordContext o = (SupervisorRecordContext) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.tfr_update_state, o.tfr_update_state) || !Internal.equals(this.upload_number, o.upload_number) || !Internal.equals(this.limit_state, o.limit_state) || !Internal.equals(this.app_dynamic_db_uuid, o.app_dynamic_db_uuid) || !Internal.equals(this.dji_flight_dynamic_db_uuid, o.dji_flight_dynamic_db_uuid)) {
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
        int hashCode = ((unknownFields().hashCode() * 37) + (this.tfr_update_state != null ? this.tfr_update_state.hashCode() : 0)) * 37;
        if (this.upload_number != null) {
            i = this.upload_number.hashCode();
        } else {
            i = 0;
        }
        int i5 = (hashCode + i) * 37;
        if (this.limit_state != null) {
            i2 = this.limit_state.hashCode();
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 37;
        if (this.app_dynamic_db_uuid != null) {
            i3 = this.app_dynamic_db_uuid.hashCode();
        } else {
            i3 = 0;
        }
        int i7 = (i6 + i3) * 37;
        if (this.dji_flight_dynamic_db_uuid != null) {
            i4 = this.dji_flight_dynamic_db_uuid.hashCode();
        }
        int result2 = i7 + i4;
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.tfr_update_state != null) {
            builder.append(", tfr_update_state=").append(this.tfr_update_state);
        }
        if (this.upload_number != null) {
            builder.append(", upload_number=").append(this.upload_number);
        }
        if (this.limit_state != null) {
            builder.append(", limit_state=").append(this.limit_state);
        }
        if (this.app_dynamic_db_uuid != null) {
            builder.append(", app_dynamic_db_uuid=").append(this.app_dynamic_db_uuid);
        }
        if (this.dji_flight_dynamic_db_uuid != null) {
            builder.append(", dji_flight_dynamic_db_uuid=").append(this.dji_flight_dynamic_db_uuid);
        }
        return builder.replace(0, 2, "SupervisorRecordContext{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<SupervisorRecordContext, Builder> {
        public String app_dynamic_db_uuid;
        public String dji_flight_dynamic_db_uuid;
        public Integer limit_state;
        public Integer tfr_update_state;
        public Integer upload_number;

        public Builder tfr_update_state(Integer tfr_update_state2) {
            this.tfr_update_state = tfr_update_state2;
            return this;
        }

        public Builder upload_number(Integer upload_number2) {
            this.upload_number = upload_number2;
            return this;
        }

        public Builder limit_state(Integer limit_state2) {
            this.limit_state = limit_state2;
            return this;
        }

        public Builder app_dynamic_db_uuid(String app_dynamic_db_uuid2) {
            this.app_dynamic_db_uuid = app_dynamic_db_uuid2;
            return this;
        }

        public Builder dji_flight_dynamic_db_uuid(String dji_flight_dynamic_db_uuid2) {
            this.dji_flight_dynamic_db_uuid = dji_flight_dynamic_db_uuid2;
            return this;
        }

        public SupervisorRecordContext build() {
            return new SupervisorRecordContext(this.tfr_update_state, this.upload_number, this.limit_state, this.app_dynamic_db_uuid, this.dji_flight_dynamic_db_uuid, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_SupervisorRecordContext extends ProtoAdapter<SupervisorRecordContext> {
        ProtoAdapter_SupervisorRecordContext() {
            super(FieldEncoding.LENGTH_DELIMITED, SupervisorRecordContext.class);
        }

        public int encodedSize(SupervisorRecordContext value) {
            int i;
            int i2;
            int i3;
            int i4 = 0;
            int encodedSizeWithTag = value.tfr_update_state != null ? ProtoAdapter.UINT32.encodedSizeWithTag(2, value.tfr_update_state) : 0;
            if (value.upload_number != null) {
                i = ProtoAdapter.UINT32.encodedSizeWithTag(3, value.upload_number);
            } else {
                i = 0;
            }
            int i5 = i + encodedSizeWithTag;
            if (value.limit_state != null) {
                i2 = ProtoAdapter.UINT32.encodedSizeWithTag(4, value.limit_state);
            } else {
                i2 = 0;
            }
            int i6 = i5 + i2;
            if (value.app_dynamic_db_uuid != null) {
                i3 = ProtoAdapter.STRING.encodedSizeWithTag(5, value.app_dynamic_db_uuid);
            } else {
                i3 = 0;
            }
            int i7 = i3 + i6;
            if (value.dji_flight_dynamic_db_uuid != null) {
                i4 = ProtoAdapter.STRING.encodedSizeWithTag(6, value.dji_flight_dynamic_db_uuid);
            }
            return i7 + i4 + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, SupervisorRecordContext value) throws IOException {
            if (value.tfr_update_state != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.tfr_update_state);
            }
            if (value.upload_number != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.upload_number);
            }
            if (value.limit_state != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.limit_state);
            }
            if (value.app_dynamic_db_uuid != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 5, value.app_dynamic_db_uuid);
            }
            if (value.dji_flight_dynamic_db_uuid != null) {
                ProtoAdapter.STRING.encodeWithTag(writer, 6, value.dji_flight_dynamic_db_uuid);
            }
            writer.writeBytes(value.unknownFields());
        }

        public SupervisorRecordContext decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 2:
                            builder.tfr_update_state(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 3:
                            builder.upload_number(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 4:
                            builder.limit_state(ProtoAdapter.UINT32.decode(reader));
                            break;
                        case 5:
                            builder.app_dynamic_db_uuid(ProtoAdapter.STRING.decode(reader));
                            break;
                        case 6:
                            builder.dji_flight_dynamic_db_uuid(ProtoAdapter.STRING.decode(reader));
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

        public SupervisorRecordContext redact(SupervisorRecordContext value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
