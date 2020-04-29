package dji.midware.data.manager.packplugin.record;

import com.google.protobuf.AbstractParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.UnknownFieldSet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class CmdPackPlugin {
    /* access modifiers changed from: private */
    public static Descriptors.FileDescriptor descriptor;
    /* access modifiers changed from: private */
    public static final Descriptors.Descriptor internal_static_dji_midware_data_manager_packplugin_record_DJIV1Pack4Save_descriptor = getDescriptor().getMessageTypes().get(0);
    /* access modifiers changed from: private */
    public static final GeneratedMessageV3.FieldAccessorTable internal_static_dji_midware_data_manager_packplugin_record_DJIV1Pack4Save_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_dji_midware_data_manager_packplugin_record_DJIV1Pack4Save_descriptor, new String[]{"CmdSet", "CmdId", "SenderType", "SenderId", "Data", "Cmdtype", "Timestamp", "Seq"});

    public interface DJIV1Pack4SaveOrBuilder extends MessageOrBuilder {
        int getCmdId();

        int getCmdSet();

        int getCmdtype();

        ByteString getData();

        int getSenderId();

        int getSenderType();

        int getSeq();

        long getTimestamp();
    }

    private CmdPackPlugin() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static void registerAllExtensions(ExtensionRegistry registry) {
        registerAllExtensions((ExtensionRegistryLite) registry);
    }

    public static final class DJIV1Pack4Save extends GeneratedMessageV3 implements DJIV1Pack4SaveOrBuilder {
        public static final int CMDID_FIELD_NUMBER = 2;
        public static final int CMDSET_FIELD_NUMBER = 1;
        public static final int CMDTYPE_FIELD_NUMBER = 6;
        public static final int DATA_FIELD_NUMBER = 5;
        private static final DJIV1Pack4Save DEFAULT_INSTANCE = new DJIV1Pack4Save();
        /* access modifiers changed from: private */
        public static final Parser<DJIV1Pack4Save> PARSER = new AbstractParser<DJIV1Pack4Save>() {
            /* class dji.midware.data.manager.packplugin.record.CmdPackPlugin.DJIV1Pack4Save.AnonymousClass1 */

            public DJIV1Pack4Save parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new DJIV1Pack4Save(input, extensionRegistry);
            }
        };
        public static final int SENDERID_FIELD_NUMBER = 4;
        public static final int SENDERTYPE_FIELD_NUMBER = 3;
        public static final int SEQ_FIELD_NUMBER = 8;
        public static final int TIMESTAMP_FIELD_NUMBER = 7;
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int cmdId_;
        /* access modifiers changed from: private */
        public int cmdSet_;
        /* access modifiers changed from: private */
        public int cmdtype_;
        /* access modifiers changed from: private */
        public ByteString data_;
        private byte memoizedIsInitialized;
        /* access modifiers changed from: private */
        public int senderId_;
        /* access modifiers changed from: private */
        public int senderType_;
        /* access modifiers changed from: private */
        public int seq_;
        /* access modifiers changed from: private */
        public long timestamp_;

        private DJIV1Pack4Save(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
        }

        private DJIV1Pack4Save() {
            this.memoizedIsInitialized = -1;
            this.data_ = ByteString.EMPTY;
        }

        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private DJIV1Pack4Save(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            boolean done = false;
            while (!done) {
                try {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            done = true;
                            break;
                        case 8:
                            this.cmdSet_ = input.readUInt32();
                            break;
                        case 16:
                            this.cmdId_ = input.readUInt32();
                            break;
                        case 24:
                            this.senderType_ = input.readUInt32();
                            break;
                        case 32:
                            this.senderId_ = input.readUInt32();
                            break;
                        case 42:
                            this.data_ = input.readBytes();
                            break;
                        case 48:
                            this.cmdtype_ = input.readUInt32();
                            break;
                        case 56:
                            this.timestamp_ = input.readUInt64();
                            break;
                        case 64:
                            this.seq_ = input.readUInt32();
                            break;
                        default:
                            if (parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                break;
                            } else {
                                done = true;
                                break;
                            }
                    }
                } catch (InvalidProtocolBufferException e) {
                    throw e.setUnfinishedMessage(this);
                } catch (IOException e2) {
                    throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                } catch (Throwable th) {
                    this.unknownFields = unknownFields.build();
                    makeExtensionsImmutable();
                    throw th;
                }
            }
            this.unknownFields = unknownFields.build();
            makeExtensionsImmutable();
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return CmdPackPlugin.internal_static_dji_midware_data_manager_packplugin_record_DJIV1Pack4Save_descriptor;
        }

        /* access modifiers changed from: protected */
        public GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return CmdPackPlugin.internal_static_dji_midware_data_manager_packplugin_record_DJIV1Pack4Save_fieldAccessorTable.ensureFieldAccessorsInitialized(DJIV1Pack4Save.class, Builder.class);
        }

        public int getCmdSet() {
            return this.cmdSet_;
        }

        public int getCmdId() {
            return this.cmdId_;
        }

        public int getSenderType() {
            return this.senderType_;
        }

        public int getSenderId() {
            return this.senderId_;
        }

        public ByteString getData() {
            return this.data_;
        }

        public int getCmdtype() {
            return this.cmdtype_;
        }

        public long getTimestamp() {
            return this.timestamp_;
        }

        public int getSeq() {
            return this.seq_;
        }

        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(CodedOutputStream output) throws IOException {
            if (this.cmdSet_ != 0) {
                output.writeUInt32(1, this.cmdSet_);
            }
            if (this.cmdId_ != 0) {
                output.writeUInt32(2, this.cmdId_);
            }
            if (this.senderType_ != 0) {
                output.writeUInt32(3, this.senderType_);
            }
            if (this.senderId_ != 0) {
                output.writeUInt32(4, this.senderId_);
            }
            if (!this.data_.isEmpty()) {
                output.writeBytes(5, this.data_);
            }
            if (this.cmdtype_ != 0) {
                output.writeUInt32(6, this.cmdtype_);
            }
            if (this.timestamp_ != 0) {
                output.writeUInt64(7, this.timestamp_);
            }
            if (this.seq_ != 0) {
                output.writeUInt32(8, this.seq_);
            }
            this.unknownFields.writeTo(output);
        }

        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            if (this.cmdSet_ != 0) {
                size2 = 0 + CodedOutputStream.computeUInt32Size(1, this.cmdSet_);
            }
            if (this.cmdId_ != 0) {
                size2 += CodedOutputStream.computeUInt32Size(2, this.cmdId_);
            }
            if (this.senderType_ != 0) {
                size2 += CodedOutputStream.computeUInt32Size(3, this.senderType_);
            }
            if (this.senderId_ != 0) {
                size2 += CodedOutputStream.computeUInt32Size(4, this.senderId_);
            }
            if (!this.data_.isEmpty()) {
                size2 += CodedOutputStream.computeBytesSize(5, this.data_);
            }
            if (this.cmdtype_ != 0) {
                size2 += CodedOutputStream.computeUInt32Size(6, this.cmdtype_);
            }
            if (this.timestamp_ != 0) {
                size2 += CodedOutputStream.computeUInt64Size(7, this.timestamp_);
            }
            if (this.seq_ != 0) {
                size2 += CodedOutputStream.computeUInt32Size(8, this.seq_);
            }
            int size3 = size2 + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof DJIV1Pack4Save)) {
                return super.equals(obj);
            }
            DJIV1Pack4Save other = (DJIV1Pack4Save) obj;
            if (getCmdSet() != other.getCmdSet()) {
                return false;
            }
            if (getCmdId() != other.getCmdId()) {
                return false;
            }
            if (getSenderType() != other.getSenderType()) {
                return false;
            }
            if (getSenderId() != other.getSenderId()) {
                return false;
            }
            if (!getData().equals(other.getData())) {
                return false;
            }
            if (getCmdtype() != other.getCmdtype()) {
                return false;
            }
            if (getTimestamp() != other.getTimestamp()) {
                return false;
            }
            if (getSeq() != other.getSeq()) {
                return false;
            }
            if (!this.unknownFields.equals(other.unknownFields)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = ((((((((((((((((((((((((((((((((((getDescriptor().hashCode() + 779) * 37) + 1) * 53) + getCmdSet()) * 37) + 2) * 53) + getCmdId()) * 37) + 3) * 53) + getSenderType()) * 37) + 4) * 53) + getSenderId()) * 37) + 5) * 53) + getData().hashCode()) * 37) + 6) * 53) + getCmdtype()) * 37) + 7) * 53) + Internal.hashLong(getTimestamp())) * 37) + 8) * 53) + getSeq()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash;
            return hash;
        }

        public static DJIV1Pack4Save parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static DJIV1Pack4Save parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static DJIV1Pack4Save parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static DJIV1Pack4Save parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static DJIV1Pack4Save parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static DJIV1Pack4Save parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static DJIV1Pack4Save parseFrom(InputStream input) throws IOException {
            return (DJIV1Pack4Save) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static DJIV1Pack4Save parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (DJIV1Pack4Save) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static DJIV1Pack4Save parseDelimitedFrom(InputStream input) throws IOException {
            return (DJIV1Pack4Save) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static DJIV1Pack4Save parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (DJIV1Pack4Save) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static DJIV1Pack4Save parseFrom(CodedInputStream input) throws IOException {
            return (DJIV1Pack4Save) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static DJIV1Pack4Save parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (DJIV1Pack4Save) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(DJIV1Pack4Save prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            if (this == DEFAULT_INSTANCE) {
                return new Builder();
            }
            return new Builder().mergeFrom(this);
        }

        /* access modifiers changed from: protected */
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            return new Builder(parent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements DJIV1Pack4SaveOrBuilder {
            private int cmdId_;
            private int cmdSet_;
            private int cmdtype_;
            private ByteString data_;
            private int senderId_;
            private int senderType_;
            private int seq_;
            private long timestamp_;

            public static final Descriptors.Descriptor getDescriptor() {
                return CmdPackPlugin.internal_static_dji_midware_data_manager_packplugin_record_DJIV1Pack4Save_descriptor;
            }

            /* access modifiers changed from: protected */
            public GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return CmdPackPlugin.internal_static_dji_midware_data_manager_packplugin_record_DJIV1Pack4Save_fieldAccessorTable.ensureFieldAccessorsInitialized(DJIV1Pack4Save.class, Builder.class);
            }

            private Builder() {
                this.data_ = ByteString.EMPTY;
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.data_ = ByteString.EMPTY;
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (DJIV1Pack4Save.alwaysUseFieldBuilders) {
                }
            }

            public Builder clear() {
                super.clear();
                this.cmdSet_ = 0;
                this.cmdId_ = 0;
                this.senderType_ = 0;
                this.senderId_ = 0;
                this.data_ = ByteString.EMPTY;
                this.cmdtype_ = 0;
                this.timestamp_ = 0;
                this.seq_ = 0;
                return this;
            }

            public Descriptors.Descriptor getDescriptorForType() {
                return CmdPackPlugin.internal_static_dji_midware_data_manager_packplugin_record_DJIV1Pack4Save_descriptor;
            }

            public DJIV1Pack4Save getDefaultInstanceForType() {
                return DJIV1Pack4Save.getDefaultInstance();
            }

            public DJIV1Pack4Save build() {
                DJIV1Pack4Save result = buildPartial();
                if (result.isInitialized()) {
                    return result;
                }
                throw newUninitializedMessageException((Message) result);
            }

            public DJIV1Pack4Save buildPartial() {
                DJIV1Pack4Save result = new DJIV1Pack4Save(this);
                int unused = result.cmdSet_ = this.cmdSet_;
                int unused2 = result.cmdId_ = this.cmdId_;
                int unused3 = result.senderType_ = this.senderType_;
                int unused4 = result.senderId_ = this.senderId_;
                ByteString unused5 = result.data_ = this.data_;
                int unused6 = result.cmdtype_ = this.cmdtype_;
                long unused7 = result.timestamp_ = this.timestamp_;
                int unused8 = result.seq_ = this.seq_;
                onBuilt();
                return result;
            }

            public Builder clone() {
                return (Builder) super.clone();
            }

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: com.google.protobuf.GeneratedMessageV3.Builder.setField(com.google.protobuf.Descriptors$FieldDescriptor, java.lang.Object):BuilderType
             arg types: [com.google.protobuf.Descriptors$FieldDescriptor, java.lang.Object]
             candidates:
              dji.midware.data.manager.packplugin.record.CmdPackPlugin.DJIV1Pack4Save.Builder.setField(com.google.protobuf.Descriptors$FieldDescriptor, java.lang.Object):com.google.protobuf.Message$Builder
              dji.midware.data.manager.packplugin.record.CmdPackPlugin.DJIV1Pack4Save.Builder.setField(com.google.protobuf.Descriptors$FieldDescriptor, java.lang.Object):dji.midware.data.manager.packplugin.record.CmdPackPlugin$DJIV1Pack4Save$Builder
              com.google.protobuf.GeneratedMessageV3.Builder.setField(com.google.protobuf.Descriptors$FieldDescriptor, java.lang.Object):com.google.protobuf.Message$Builder
              com.google.protobuf.Message.Builder.setField(com.google.protobuf.Descriptors$FieldDescriptor, java.lang.Object):com.google.protobuf.Message$Builder
              com.google.protobuf.GeneratedMessageV3.Builder.setField(com.google.protobuf.Descriptors$FieldDescriptor, java.lang.Object):BuilderType */
            public Builder setField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.setField(field, value);
            }

            public Builder clearField(Descriptors.FieldDescriptor field) {
                return (Builder) super.clearField(field);
            }

            public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
                return (Builder) super.clearOneof(oneof);
            }

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: com.google.protobuf.GeneratedMessageV3.Builder.setRepeatedField(com.google.protobuf.Descriptors$FieldDescriptor, int, java.lang.Object):BuilderType
             arg types: [com.google.protobuf.Descriptors$FieldDescriptor, int, java.lang.Object]
             candidates:
              dji.midware.data.manager.packplugin.record.CmdPackPlugin.DJIV1Pack4Save.Builder.setRepeatedField(com.google.protobuf.Descriptors$FieldDescriptor, int, java.lang.Object):com.google.protobuf.Message$Builder
              dji.midware.data.manager.packplugin.record.CmdPackPlugin.DJIV1Pack4Save.Builder.setRepeatedField(com.google.protobuf.Descriptors$FieldDescriptor, int, java.lang.Object):dji.midware.data.manager.packplugin.record.CmdPackPlugin$DJIV1Pack4Save$Builder
              com.google.protobuf.GeneratedMessageV3.Builder.setRepeatedField(com.google.protobuf.Descriptors$FieldDescriptor, int, java.lang.Object):com.google.protobuf.Message$Builder
              com.google.protobuf.Message.Builder.setRepeatedField(com.google.protobuf.Descriptors$FieldDescriptor, int, java.lang.Object):com.google.protobuf.Message$Builder
              com.google.protobuf.GeneratedMessageV3.Builder.setRepeatedField(com.google.protobuf.Descriptors$FieldDescriptor, int, java.lang.Object):BuilderType */
            public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
                return (Builder) super.setRepeatedField(field, index, value);
            }

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: com.google.protobuf.GeneratedMessageV3.Builder.addRepeatedField(com.google.protobuf.Descriptors$FieldDescriptor, java.lang.Object):BuilderType
             arg types: [com.google.protobuf.Descriptors$FieldDescriptor, java.lang.Object]
             candidates:
              dji.midware.data.manager.packplugin.record.CmdPackPlugin.DJIV1Pack4Save.Builder.addRepeatedField(com.google.protobuf.Descriptors$FieldDescriptor, java.lang.Object):com.google.protobuf.Message$Builder
              dji.midware.data.manager.packplugin.record.CmdPackPlugin.DJIV1Pack4Save.Builder.addRepeatedField(com.google.protobuf.Descriptors$FieldDescriptor, java.lang.Object):dji.midware.data.manager.packplugin.record.CmdPackPlugin$DJIV1Pack4Save$Builder
              com.google.protobuf.GeneratedMessageV3.Builder.addRepeatedField(com.google.protobuf.Descriptors$FieldDescriptor, java.lang.Object):com.google.protobuf.Message$Builder
              com.google.protobuf.Message.Builder.addRepeatedField(com.google.protobuf.Descriptors$FieldDescriptor, java.lang.Object):com.google.protobuf.Message$Builder
              com.google.protobuf.GeneratedMessageV3.Builder.addRepeatedField(com.google.protobuf.Descriptors$FieldDescriptor, java.lang.Object):BuilderType */
            public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
                return (Builder) super.addRepeatedField(field, value);
            }

            /* Debug info: failed to restart local var, previous not found, register: 1 */
            public Builder mergeFrom(Message other) {
                if (other instanceof DJIV1Pack4Save) {
                    return mergeFrom((DJIV1Pack4Save) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(DJIV1Pack4Save other) {
                if (other != DJIV1Pack4Save.getDefaultInstance()) {
                    if (other.getCmdSet() != 0) {
                        setCmdSet(other.getCmdSet());
                    }
                    if (other.getCmdId() != 0) {
                        setCmdId(other.getCmdId());
                    }
                    if (other.getSenderType() != 0) {
                        setSenderType(other.getSenderType());
                    }
                    if (other.getSenderId() != 0) {
                        setSenderId(other.getSenderId());
                    }
                    if (other.getData() != ByteString.EMPTY) {
                        setData(other.getData());
                    }
                    if (other.getCmdtype() != 0) {
                        setCmdtype(other.getCmdtype());
                    }
                    if (other.getTimestamp() != 0) {
                        setTimestamp(other.getTimestamp());
                    }
                    if (other.getSeq() != 0) {
                        setSeq(other.getSeq());
                    }
                    mergeUnknownFields(other.unknownFields);
                    onChanged();
                }
                return this;
            }

            public final boolean isInitialized() {
                return true;
            }

            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                try {
                    DJIV1Pack4Save parsedMessage = (DJIV1Pack4Save) DJIV1Pack4Save.PARSER.parsePartialFrom(input, extensionRegistry);
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    return this;
                } catch (InvalidProtocolBufferException e) {
                    DJIV1Pack4Save parsedMessage2 = (DJIV1Pack4Save) e.getUnfinishedMessage();
                    throw e.unwrapIOException();
                } catch (Throwable th) {
                    if (0 != 0) {
                        mergeFrom((DJIV1Pack4Save) null);
                    }
                    throw th;
                }
            }

            public int getCmdSet() {
                return this.cmdSet_;
            }

            public Builder setCmdSet(int value) {
                this.cmdSet_ = value;
                onChanged();
                return this;
            }

            public Builder clearCmdSet() {
                this.cmdSet_ = 0;
                onChanged();
                return this;
            }

            public int getCmdId() {
                return this.cmdId_;
            }

            public Builder setCmdId(int value) {
                this.cmdId_ = value;
                onChanged();
                return this;
            }

            public Builder clearCmdId() {
                this.cmdId_ = 0;
                onChanged();
                return this;
            }

            public int getSenderType() {
                return this.senderType_;
            }

            public Builder setSenderType(int value) {
                this.senderType_ = value;
                onChanged();
                return this;
            }

            public Builder clearSenderType() {
                this.senderType_ = 0;
                onChanged();
                return this;
            }

            public int getSenderId() {
                return this.senderId_;
            }

            public Builder setSenderId(int value) {
                this.senderId_ = value;
                onChanged();
                return this;
            }

            public Builder clearSenderId() {
                this.senderId_ = 0;
                onChanged();
                return this;
            }

            public ByteString getData() {
                return this.data_;
            }

            public Builder setData(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.data_ = value;
                onChanged();
                return this;
            }

            public Builder clearData() {
                this.data_ = DJIV1Pack4Save.getDefaultInstance().getData();
                onChanged();
                return this;
            }

            public int getCmdtype() {
                return this.cmdtype_;
            }

            public Builder setCmdtype(int value) {
                this.cmdtype_ = value;
                onChanged();
                return this;
            }

            public Builder clearCmdtype() {
                this.cmdtype_ = 0;
                onChanged();
                return this;
            }

            public long getTimestamp() {
                return this.timestamp_;
            }

            public Builder setTimestamp(long value) {
                this.timestamp_ = value;
                onChanged();
                return this;
            }

            public Builder clearTimestamp() {
                this.timestamp_ = 0;
                onChanged();
                return this;
            }

            public int getSeq() {
                return this.seq_;
            }

            public Builder setSeq(int value) {
                this.seq_ = value;
                onChanged();
                return this;
            }

            public Builder clearSeq() {
                this.seq_ = 0;
                onChanged();
                return this;
            }

            public final Builder setUnknownFields(UnknownFieldSet unknownFields) {
                return (Builder) super.setUnknownFields(unknownFields);
            }

            public final Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
                return (Builder) super.mergeUnknownFields(unknownFields);
            }
        }

        public static DJIV1Pack4Save getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<DJIV1Pack4Save> parser() {
            return PARSER;
        }

        public Parser<DJIV1Pack4Save> getParserForType() {
            return PARSER;
        }

        public DJIV1Pack4Save getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    static {
        Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(new String[]{"\n\u0013CmdPackPlugin.proto\u0012*dji.midware.data.manager.packplugin.record\"\u0001\n\u000eDJIV1Pack4Save\u0012\u000e\n\u0006cmdSet\u0018\u0001 \u0001(\r\u0012\r\n\u0005cmdId\u0018\u0002 \u0001(\r\u0012\u0012\n\nsenderType\u0018\u0003 \u0001(\r\u0012\u0010\n\bsenderId\u0018\u0004 \u0001(\r\u0012\f\n\u0004data\u0018\u0005 \u0001(\f\u0012\u000f\n\u0007cmdtype\u0018\u0006 \u0001(\r\u0012\u0011\n\ttimestamp\u0018\u0007 \u0001(\u0004\u0012\u000b\n\u0003seq\u0018\b \u0001(\rb\u0006proto3"}, new Descriptors.FileDescriptor[0], new Descriptors.FileDescriptor.InternalDescriptorAssigner() {
            /* class dji.midware.data.manager.packplugin.record.CmdPackPlugin.AnonymousClass1 */

            public ExtensionRegistry assignDescriptors(Descriptors.FileDescriptor root) {
                Descriptors.FileDescriptor unused = CmdPackPlugin.descriptor = root;
                return null;
            }
        });
    }
}
