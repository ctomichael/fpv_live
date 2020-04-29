package com.google.protobuf;

import java.io.IOException;

public class LazyFieldLite {
    private static final ExtensionRegistryLite EMPTY_REGISTRY = ExtensionRegistryLite.getEmptyRegistry();
    private ByteString delayedBytes;
    private ExtensionRegistryLite extensionRegistry;
    private volatile ByteString memoizedBytes;
    protected volatile MessageLite value;

    public LazyFieldLite(ExtensionRegistryLite extensionRegistry2, ByteString bytes) {
        checkArguments(extensionRegistry2, bytes);
        this.extensionRegistry = extensionRegistry2;
        this.delayedBytes = bytes;
    }

    public LazyFieldLite() {
    }

    public static LazyFieldLite fromValue(MessageLite value2) {
        LazyFieldLite lf = new LazyFieldLite();
        lf.setValue(value2);
        return lf;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LazyFieldLite)) {
            return false;
        }
        LazyFieldLite other = (LazyFieldLite) o;
        MessageLite value1 = this.value;
        MessageLite value2 = other.value;
        if (value1 == null && value2 == null) {
            return toByteString().equals(other.toByteString());
        }
        if (value1 != null && value2 != null) {
            return value1.equals(value2);
        }
        if (value1 != null) {
            return value1.equals(other.getValue(value1.getDefaultInstanceForType()));
        }
        return getValue(value2.getDefaultInstanceForType()).equals(value2);
    }

    public int hashCode() {
        return 1;
    }

    public boolean containsDefaultInstance() {
        return this.memoizedBytes == ByteString.EMPTY || (this.value == null && (this.delayedBytes == null || this.delayedBytes == ByteString.EMPTY));
    }

    public void clear() {
        this.delayedBytes = null;
        this.value = null;
        this.memoizedBytes = null;
    }

    public void set(LazyFieldLite other) {
        this.delayedBytes = other.delayedBytes;
        this.value = other.value;
        this.memoizedBytes = other.memoizedBytes;
        if (other.extensionRegistry != null) {
            this.extensionRegistry = other.extensionRegistry;
        }
    }

    public MessageLite getValue(MessageLite defaultInstance) {
        ensureInitialized(defaultInstance);
        return this.value;
    }

    public MessageLite setValue(MessageLite value2) {
        MessageLite originalValue = this.value;
        this.delayedBytes = null;
        this.memoizedBytes = null;
        this.value = value2;
        return originalValue;
    }

    public void merge(LazyFieldLite other) {
        if (!other.containsDefaultInstance()) {
            if (containsDefaultInstance()) {
                set(other);
                return;
            }
            if (this.extensionRegistry == null) {
                this.extensionRegistry = other.extensionRegistry;
            }
            if (this.delayedBytes != null && other.delayedBytes != null) {
                this.delayedBytes = this.delayedBytes.concat(other.delayedBytes);
            } else if (this.value == null && other.value != null) {
                setValue(mergeValueAndBytes(other.value, this.delayedBytes, this.extensionRegistry));
            } else if (this.value == null || other.value != null) {
                setValue(this.value.toBuilder().mergeFrom(other.value).build());
            } else {
                setValue(mergeValueAndBytes(this.value, other.delayedBytes, other.extensionRegistry));
            }
        }
    }

    public void mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry2) throws IOException {
        if (containsDefaultInstance()) {
            setByteString(input.readBytes(), extensionRegistry2);
            return;
        }
        if (this.extensionRegistry == null) {
            this.extensionRegistry = extensionRegistry2;
        }
        if (this.delayedBytes != null) {
            setByteString(this.delayedBytes.concat(input.readBytes()), this.extensionRegistry);
            return;
        }
        try {
            setValue(this.value.toBuilder().mergeFrom(input, extensionRegistry2).build());
        } catch (InvalidProtocolBufferException e) {
        }
    }

    private static MessageLite mergeValueAndBytes(MessageLite value2, ByteString otherBytes, ExtensionRegistryLite extensionRegistry2) {
        try {
            return value2.toBuilder().mergeFrom(otherBytes, extensionRegistry2).build();
        } catch (InvalidProtocolBufferException e) {
            return value2;
        }
    }

    public void setByteString(ByteString bytes, ExtensionRegistryLite extensionRegistry2) {
        checkArguments(extensionRegistry2, bytes);
        this.delayedBytes = bytes;
        this.extensionRegistry = extensionRegistry2;
        this.value = null;
        this.memoizedBytes = null;
    }

    public int getSerializedSize() {
        if (this.memoizedBytes != null) {
            return this.memoizedBytes.size();
        }
        if (this.delayedBytes != null) {
            return this.delayedBytes.size();
        }
        if (this.value != null) {
            return this.value.getSerializedSize();
        }
        return 0;
    }

    public ByteString toByteString() {
        if (this.memoizedBytes != null) {
            return this.memoizedBytes;
        }
        if (this.delayedBytes != null) {
            return this.delayedBytes;
        }
        synchronized (this) {
            if (this.memoizedBytes != null) {
                ByteString byteString = this.memoizedBytes;
                return byteString;
            }
            if (this.value == null) {
                this.memoizedBytes = ByteString.EMPTY;
            } else {
                this.memoizedBytes = this.value.toByteString();
            }
            ByteString byteString2 = this.memoizedBytes;
            return byteString2;
        }
    }

    /* access modifiers changed from: package-private */
    public void writeTo(Writer writer, int fieldNumber) throws IOException {
        if (this.memoizedBytes != null) {
            writer.writeBytes(fieldNumber, this.memoizedBytes);
        } else if (this.delayedBytes != null) {
            writer.writeBytes(fieldNumber, this.delayedBytes);
        } else if (this.value != null) {
            writer.writeMessage(fieldNumber, this.value);
        } else {
            writer.writeBytes(fieldNumber, ByteString.EMPTY);
        }
    }

    /* access modifiers changed from: protected */
    public void ensureInitialized(MessageLite defaultInstance) {
        if (this.value == null) {
            synchronized (this) {
                if (this.value == null) {
                    try {
                        if (this.delayedBytes != null) {
                            this.value = (MessageLite) defaultInstance.getParserForType().parseFrom(this.delayedBytes, this.extensionRegistry);
                            this.memoizedBytes = this.delayedBytes;
                        } else {
                            this.value = defaultInstance;
                            this.memoizedBytes = ByteString.EMPTY;
                        }
                    } catch (InvalidProtocolBufferException e) {
                        this.value = defaultInstance;
                        this.memoizedBytes = ByteString.EMPTY;
                    }
                    return;
                }
                return;
            }
        }
        return;
    }

    private static void checkArguments(ExtensionRegistryLite extensionRegistry2, ByteString bytes) {
        if (extensionRegistry2 == null) {
            throw new NullPointerException("found null ExtensionRegistry");
        } else if (bytes == null) {
            throw new NullPointerException("found null ByteString");
        }
    }
}
