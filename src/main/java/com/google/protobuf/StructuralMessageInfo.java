package com.google.protobuf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class StructuralMessageInfo implements MessageInfo {
    private final int[] checkInitialized;
    private final MessageLite defaultInstance;
    private final FieldInfo[] fields;
    private final boolean messageSetWireFormat;
    private final ProtoSyntax syntax;

    StructuralMessageInfo(ProtoSyntax syntax2, boolean messageSetWireFormat2, int[] checkInitialized2, FieldInfo[] fields2, Object defaultInstance2) {
        this.syntax = syntax2;
        this.messageSetWireFormat = messageSetWireFormat2;
        this.checkInitialized = checkInitialized2;
        this.fields = fields2;
        this.defaultInstance = (MessageLite) Internal.checkNotNull(defaultInstance2, "defaultInstance");
    }

    public ProtoSyntax getSyntax() {
        return this.syntax;
    }

    public boolean isMessageSetWireFormat() {
        return this.messageSetWireFormat;
    }

    public int[] getCheckInitialized() {
        return this.checkInitialized;
    }

    public FieldInfo[] getFields() {
        return this.fields;
    }

    public MessageLite getDefaultInstance() {
        return this.defaultInstance;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(int numFields) {
        return new Builder(numFields);
    }

    public static final class Builder {
        private int[] checkInitialized;
        private Object defaultInstance;
        private final List<FieldInfo> fields;
        private boolean messageSetWireFormat;
        private ProtoSyntax syntax;
        private boolean wasBuilt;

        public Builder() {
            this.checkInitialized = null;
            this.fields = new ArrayList();
        }

        public Builder(int numFields) {
            this.checkInitialized = null;
            this.fields = new ArrayList(numFields);
        }

        public void withDefaultInstance(Object defaultInstance2) {
            this.defaultInstance = defaultInstance2;
        }

        public void withSyntax(ProtoSyntax syntax2) {
            this.syntax = (ProtoSyntax) Internal.checkNotNull(syntax2, "syntax");
        }

        public void withMessageSetWireFormat(boolean messageSetWireFormat2) {
            this.messageSetWireFormat = messageSetWireFormat2;
        }

        public void withCheckInitialized(int[] checkInitialized2) {
            this.checkInitialized = checkInitialized2;
        }

        public void withField(FieldInfo field) {
            if (this.wasBuilt) {
                throw new IllegalStateException("Builder can only build once");
            }
            this.fields.add(field);
        }

        public StructuralMessageInfo build() {
            if (this.wasBuilt) {
                throw new IllegalStateException("Builder can only build once");
            } else if (this.syntax == null) {
                throw new IllegalStateException("Must specify a proto syntax");
            } else {
                this.wasBuilt = true;
                Collections.sort(this.fields);
                return new StructuralMessageInfo(this.syntax, this.messageSetWireFormat, this.checkInitialized, (FieldInfo[]) this.fields.toArray(new FieldInfo[0]), this.defaultInstance);
            }
        }
    }
}
