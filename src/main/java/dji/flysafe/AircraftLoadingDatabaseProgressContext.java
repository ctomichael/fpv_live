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

public final class AircraftLoadingDatabaseProgressContext extends Message<AircraftLoadingDatabaseProgressContext, Builder> {
    public static final ProtoAdapter<AircraftLoadingDatabaseProgressContext> ADAPTER = new ProtoAdapter_AircraftLoadingDatabaseProgressContext();
    public static final Integer DEFAULT_LOADINGPROGRESS = 0;
    private static final long serialVersionUID = 0;
    @WireField(adapter = "com.squareup.wire.ProtoAdapter#UINT32", tag = 1)
    public final Integer loadingProgress;

    public AircraftLoadingDatabaseProgressContext(Integer loadingProgress2) {
        this(loadingProgress2, ByteString.EMPTY);
    }

    public AircraftLoadingDatabaseProgressContext(Integer loadingProgress2, ByteString unknownFields) {
        super(ADAPTER, unknownFields);
        this.loadingProgress = loadingProgress2;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.loadingProgress = this.loadingProgress;
        builder.addUnknownFields(unknownFields());
        return builder;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AircraftLoadingDatabaseProgressContext)) {
            return false;
        }
        AircraftLoadingDatabaseProgressContext o = (AircraftLoadingDatabaseProgressContext) other;
        if (!unknownFields().equals(o.unknownFields()) || !Internal.equals(this.loadingProgress, o.loadingProgress)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int result2 = (unknownFields().hashCode() * 37) + (this.loadingProgress != null ? this.loadingProgress.hashCode() : 0);
        this.hashCode = result2;
        return result2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.loadingProgress != null) {
            builder.append(", loadingProgress=").append(this.loadingProgress);
        }
        return builder.replace(0, 2, "AircraftLoadingDatabaseProgressContext{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<AircraftLoadingDatabaseProgressContext, Builder> {
        public Integer loadingProgress;

        public Builder loadingProgress(Integer loadingProgress2) {
            this.loadingProgress = loadingProgress2;
            return this;
        }

        public AircraftLoadingDatabaseProgressContext build() {
            return new AircraftLoadingDatabaseProgressContext(this.loadingProgress, super.buildUnknownFields());
        }
    }

    private static final class ProtoAdapter_AircraftLoadingDatabaseProgressContext extends ProtoAdapter<AircraftLoadingDatabaseProgressContext> {
        ProtoAdapter_AircraftLoadingDatabaseProgressContext() {
            super(FieldEncoding.LENGTH_DELIMITED, AircraftLoadingDatabaseProgressContext.class);
        }

        public int encodedSize(AircraftLoadingDatabaseProgressContext value) {
            return (value.loadingProgress != null ? ProtoAdapter.UINT32.encodedSizeWithTag(1, value.loadingProgress) : 0) + value.unknownFields().size();
        }

        public void encode(ProtoWriter writer, AircraftLoadingDatabaseProgressContext value) throws IOException {
            if (value.loadingProgress != null) {
                ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.loadingProgress);
            }
            writer.writeBytes(value.unknownFields());
        }

        public AircraftLoadingDatabaseProgressContext decode(ProtoReader reader) throws IOException {
            Builder builder = new Builder();
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            builder.loadingProgress(ProtoAdapter.UINT32.decode(reader));
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

        public AircraftLoadingDatabaseProgressContext redact(AircraftLoadingDatabaseProgressContext value) {
            Builder builder = value.newBuilder();
            builder.clearUnknownFields();
            return builder.build();
        }
    }
}
