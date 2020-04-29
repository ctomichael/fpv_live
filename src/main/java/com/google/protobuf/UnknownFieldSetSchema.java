package com.google.protobuf;

import com.google.protobuf.UnknownFieldSet;
import java.io.IOException;

class UnknownFieldSetSchema extends UnknownFieldSchema<UnknownFieldSet, UnknownFieldSet.Builder> {
    private final boolean proto3;

    public UnknownFieldSetSchema(boolean proto32) {
        this.proto3 = proto32;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldDiscardUnknownFields(Reader reader) {
        return reader.shouldDiscardUnknownFields();
    }

    /* access modifiers changed from: package-private */
    public UnknownFieldSet.Builder newBuilder() {
        return UnknownFieldSet.newBuilder();
    }

    /* access modifiers changed from: package-private */
    public void addVarint(UnknownFieldSet.Builder fields, int number, long value) {
        fields.mergeField(number, UnknownFieldSet.Field.newBuilder().addVarint(value).build());
    }

    /* access modifiers changed from: package-private */
    public void addFixed32(UnknownFieldSet.Builder fields, int number, int value) {
        fields.mergeField(number, UnknownFieldSet.Field.newBuilder().addFixed32(value).build());
    }

    /* access modifiers changed from: package-private */
    public void addFixed64(UnknownFieldSet.Builder fields, int number, long value) {
        fields.mergeField(number, UnknownFieldSet.Field.newBuilder().addFixed64(value).build());
    }

    /* access modifiers changed from: package-private */
    public void addLengthDelimited(UnknownFieldSet.Builder fields, int number, ByteString value) {
        fields.mergeField(number, UnknownFieldSet.Field.newBuilder().addLengthDelimited(value).build());
    }

    /* access modifiers changed from: package-private */
    public void addGroup(UnknownFieldSet.Builder fields, int number, UnknownFieldSet subFieldSet) {
        fields.mergeField(number, UnknownFieldSet.Field.newBuilder().addGroup(subFieldSet).build());
    }

    /* access modifiers changed from: package-private */
    public UnknownFieldSet toImmutable(UnknownFieldSet.Builder fields) {
        return fields.build();
    }

    /* access modifiers changed from: package-private */
    public void writeTo(UnknownFieldSet message, Writer writer) throws IOException {
        message.writeTo(writer);
    }

    /* access modifiers changed from: package-private */
    public void writeAsMessageSetTo(UnknownFieldSet message, Writer writer) throws IOException {
        message.writeAsMessageSetTo(writer);
    }

    /* access modifiers changed from: package-private */
    public UnknownFieldSet getFromMessage(Object message) {
        return ((GeneratedMessageV3) message).unknownFields;
    }

    /* access modifiers changed from: package-private */
    public void setToMessage(Object message, UnknownFieldSet fields) {
        ((GeneratedMessageV3) message).unknownFields = fields;
    }

    /* access modifiers changed from: package-private */
    public UnknownFieldSet.Builder getBuilderFromMessage(Object message) {
        return ((GeneratedMessageV3) message).unknownFields.toBuilder();
    }

    /* access modifiers changed from: package-private */
    public void setBuilderToMessage(Object message, UnknownFieldSet.Builder builder) {
        ((GeneratedMessageV3) message).unknownFields = builder.build();
    }

    /* access modifiers changed from: package-private */
    public void makeImmutable(Object message) {
    }

    /* access modifiers changed from: package-private */
    public UnknownFieldSet merge(UnknownFieldSet message, UnknownFieldSet other) {
        return message.toBuilder().mergeFrom(other).build();
    }

    /* access modifiers changed from: package-private */
    public int getSerializedSize(UnknownFieldSet message) {
        return message.getSerializedSize();
    }

    /* access modifiers changed from: package-private */
    public int getSerializedSizeAsMessageSet(UnknownFieldSet unknowns) {
        return unknowns.getSerializedSizeAsMessageSet();
    }
}
