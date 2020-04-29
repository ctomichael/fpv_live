package com.google.protobuf;

import java.io.IOException;

class UnknownFieldSetLiteSchema extends UnknownFieldSchema<UnknownFieldSetLite, UnknownFieldSetLite> {
    UnknownFieldSetLiteSchema() {
    }

    /* access modifiers changed from: package-private */
    public boolean shouldDiscardUnknownFields(Reader reader) {
        return false;
    }

    /* access modifiers changed from: package-private */
    public UnknownFieldSetLite newBuilder() {
        return UnknownFieldSetLite.newInstance();
    }

    /* access modifiers changed from: package-private */
    public void addVarint(UnknownFieldSetLite fields, int number, long value) {
        fields.storeField(WireFormat.makeTag(number, 0), Long.valueOf(value));
    }

    /* access modifiers changed from: package-private */
    public void addFixed32(UnknownFieldSetLite fields, int number, int value) {
        fields.storeField(WireFormat.makeTag(number, 5), Integer.valueOf(value));
    }

    /* access modifiers changed from: package-private */
    public void addFixed64(UnknownFieldSetLite fields, int number, long value) {
        fields.storeField(WireFormat.makeTag(number, 1), Long.valueOf(value));
    }

    /* access modifiers changed from: package-private */
    public void addLengthDelimited(UnknownFieldSetLite fields, int number, ByteString value) {
        fields.storeField(WireFormat.makeTag(number, 2), value);
    }

    /* access modifiers changed from: package-private */
    public void addGroup(UnknownFieldSetLite fields, int number, UnknownFieldSetLite subFieldSet) {
        fields.storeField(WireFormat.makeTag(number, 3), subFieldSet);
    }

    /* access modifiers changed from: package-private */
    public UnknownFieldSetLite toImmutable(UnknownFieldSetLite fields) {
        fields.makeImmutable();
        return fields;
    }

    /* access modifiers changed from: package-private */
    public void setToMessage(Object message, UnknownFieldSetLite fields) {
        ((GeneratedMessageLite) message).unknownFields = fields;
    }

    /* access modifiers changed from: package-private */
    public UnknownFieldSetLite getFromMessage(Object message) {
        return ((GeneratedMessageLite) message).unknownFields;
    }

    /* access modifiers changed from: package-private */
    public UnknownFieldSetLite getBuilderFromMessage(Object message) {
        UnknownFieldSetLite unknownFields = getFromMessage(message);
        if (unknownFields != UnknownFieldSetLite.getDefaultInstance()) {
            return unknownFields;
        }
        UnknownFieldSetLite unknownFields2 = UnknownFieldSetLite.newInstance();
        setToMessage(message, unknownFields2);
        return unknownFields2;
    }

    /* access modifiers changed from: package-private */
    public void setBuilderToMessage(Object message, UnknownFieldSetLite fields) {
        setToMessage(message, fields);
    }

    /* access modifiers changed from: package-private */
    public void makeImmutable(Object message) {
        getFromMessage(message).makeImmutable();
    }

    /* access modifiers changed from: package-private */
    public void writeTo(UnknownFieldSetLite fields, Writer writer) throws IOException {
        fields.writeTo(writer);
    }

    /* access modifiers changed from: package-private */
    public void writeAsMessageSetTo(UnknownFieldSetLite fields, Writer writer) throws IOException {
        fields.writeAsMessageSetTo(writer);
    }

    /* access modifiers changed from: package-private */
    public UnknownFieldSetLite merge(UnknownFieldSetLite message, UnknownFieldSetLite other) {
        if (other.equals(UnknownFieldSetLite.getDefaultInstance())) {
            return message;
        }
        return UnknownFieldSetLite.mutableCopyOf(message, other);
    }

    /* access modifiers changed from: package-private */
    public int getSerializedSize(UnknownFieldSetLite unknowns) {
        return unknowns.getSerializedSize();
    }

    /* access modifiers changed from: package-private */
    public int getSerializedSizeAsMessageSet(UnknownFieldSetLite unknowns) {
        return unknowns.getSerializedSizeAsMessageSet();
    }
}
