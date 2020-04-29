package com.squareup.wire;

import com.squareup.wire.Message;
import com.squareup.wire.WireField;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

public abstract class ProtoAdapter<E> {
    public static final ProtoAdapter<Boolean> BOOL = new ProtoAdapter<Boolean>(FieldEncoding.VARINT, Boolean.class) {
        /* class com.squareup.wire.ProtoAdapter.AnonymousClass1 */

        public int encodedSize(Boolean value) {
            return 1;
        }

        public void encode(ProtoWriter writer, Boolean value) throws IOException {
            writer.writeVarint32(value.booleanValue() ? 1 : 0);
        }

        public Boolean decode(ProtoReader reader) throws IOException {
            int value = reader.readVarint32();
            if (value == 0) {
                return Boolean.FALSE;
            }
            if (value == 1) {
                return Boolean.TRUE;
            }
            throw new IOException(String.format("Invalid boolean value 0x%02x", Integer.valueOf(value)));
        }
    };
    public static final ProtoAdapter<ByteString> BYTES = new ProtoAdapter<ByteString>(FieldEncoding.LENGTH_DELIMITED, ByteString.class) {
        /* class com.squareup.wire.ProtoAdapter.AnonymousClass13 */

        public int encodedSize(ByteString value) {
            return value.size();
        }

        public void encode(ProtoWriter writer, ByteString value) throws IOException {
            writer.writeBytes(value);
        }

        public ByteString decode(ProtoReader reader) throws IOException {
            return reader.readBytes();
        }
    };
    public static final ProtoAdapter<Double> DOUBLE = new ProtoAdapter<Double>(FieldEncoding.FIXED64, Double.class) {
        /* class com.squareup.wire.ProtoAdapter.AnonymousClass11 */

        public int encodedSize(Double value) {
            return 8;
        }

        public void encode(ProtoWriter writer, Double value) throws IOException {
            writer.writeFixed64(Double.doubleToLongBits(value.doubleValue()));
        }

        public Double decode(ProtoReader reader) throws IOException {
            return Double.valueOf(Double.longBitsToDouble(reader.readFixed64()));
        }
    };
    public static final ProtoAdapter<Integer> FIXED32 = new ProtoAdapter<Integer>(FieldEncoding.FIXED32, Integer.class) {
        /* class com.squareup.wire.ProtoAdapter.AnonymousClass5 */

        public int encodedSize(Integer value) {
            return 4;
        }

        public void encode(ProtoWriter writer, Integer value) throws IOException {
            writer.writeFixed32(value.intValue());
        }

        public Integer decode(ProtoReader reader) throws IOException {
            return Integer.valueOf(reader.readFixed32());
        }
    };
    public static final ProtoAdapter<Long> FIXED64 = new ProtoAdapter<Long>(FieldEncoding.FIXED64, Long.class) {
        /* class com.squareup.wire.ProtoAdapter.AnonymousClass9 */

        public int encodedSize(Long value) {
            return 8;
        }

        public void encode(ProtoWriter writer, Long value) throws IOException {
            writer.writeFixed64(value.longValue());
        }

        public Long decode(ProtoReader reader) throws IOException {
            return Long.valueOf(reader.readFixed64());
        }
    };
    private static final int FIXED_32_SIZE = 4;
    private static final int FIXED_64_SIZE = 8;
    private static final int FIXED_BOOL_SIZE = 1;
    public static final ProtoAdapter<Float> FLOAT = new ProtoAdapter<Float>(FieldEncoding.FIXED32, Float.class) {
        /* class com.squareup.wire.ProtoAdapter.AnonymousClass10 */

        public int encodedSize(Float value) {
            return 4;
        }

        public void encode(ProtoWriter writer, Float value) throws IOException {
            writer.writeFixed32(Float.floatToIntBits(value.floatValue()));
        }

        public Float decode(ProtoReader reader) throws IOException {
            return Float.valueOf(Float.intBitsToFloat(reader.readFixed32()));
        }
    };
    public static final ProtoAdapter<Integer> INT32 = new ProtoAdapter<Integer>(FieldEncoding.VARINT, Integer.class) {
        /* class com.squareup.wire.ProtoAdapter.AnonymousClass2 */

        public int encodedSize(Integer value) {
            return ProtoWriter.int32Size(value.intValue());
        }

        public void encode(ProtoWriter writer, Integer value) throws IOException {
            writer.writeSignedVarint32(value.intValue());
        }

        public Integer decode(ProtoReader reader) throws IOException {
            return Integer.valueOf(reader.readVarint32());
        }
    };
    public static final ProtoAdapter<Long> INT64 = new ProtoAdapter<Long>(FieldEncoding.VARINT, Long.class) {
        /* class com.squareup.wire.ProtoAdapter.AnonymousClass6 */

        public int encodedSize(Long value) {
            return ProtoWriter.varint64Size(value.longValue());
        }

        public void encode(ProtoWriter writer, Long value) throws IOException {
            writer.writeVarint64(value.longValue());
        }

        public Long decode(ProtoReader reader) throws IOException {
            return Long.valueOf(reader.readVarint64());
        }
    };
    public static final ProtoAdapter<Integer> SFIXED32 = FIXED32;
    public static final ProtoAdapter<Long> SFIXED64 = FIXED64;
    public static final ProtoAdapter<Integer> SINT32 = new ProtoAdapter<Integer>(FieldEncoding.VARINT, Integer.class) {
        /* class com.squareup.wire.ProtoAdapter.AnonymousClass4 */

        public int encodedSize(Integer value) {
            return ProtoWriter.varint32Size(ProtoWriter.encodeZigZag32(value.intValue()));
        }

        public void encode(ProtoWriter writer, Integer value) throws IOException {
            writer.writeVarint32(ProtoWriter.encodeZigZag32(value.intValue()));
        }

        public Integer decode(ProtoReader reader) throws IOException {
            return Integer.valueOf(ProtoWriter.decodeZigZag32(reader.readVarint32()));
        }
    };
    public static final ProtoAdapter<Long> SINT64 = new ProtoAdapter<Long>(FieldEncoding.VARINT, Long.class) {
        /* class com.squareup.wire.ProtoAdapter.AnonymousClass8 */

        public int encodedSize(Long value) {
            return ProtoWriter.varint64Size(ProtoWriter.encodeZigZag64(value.longValue()));
        }

        public void encode(ProtoWriter writer, Long value) throws IOException {
            writer.writeVarint64(ProtoWriter.encodeZigZag64(value.longValue()));
        }

        public Long decode(ProtoReader reader) throws IOException {
            return Long.valueOf(ProtoWriter.decodeZigZag64(reader.readVarint64()));
        }
    };
    public static final ProtoAdapter<String> STRING = new ProtoAdapter<String>(FieldEncoding.LENGTH_DELIMITED, String.class) {
        /* class com.squareup.wire.ProtoAdapter.AnonymousClass12 */

        public int encodedSize(String value) {
            return ProtoWriter.utf8Length(value);
        }

        public void encode(ProtoWriter writer, String value) throws IOException {
            writer.writeString(value);
        }

        public String decode(ProtoReader reader) throws IOException {
            return reader.readString();
        }
    };
    public static final ProtoAdapter<Integer> UINT32 = new ProtoAdapter<Integer>(FieldEncoding.VARINT, Integer.class) {
        /* class com.squareup.wire.ProtoAdapter.AnonymousClass3 */

        public int encodedSize(Integer value) {
            return ProtoWriter.varint32Size(value.intValue());
        }

        public void encode(ProtoWriter writer, Integer value) throws IOException {
            writer.writeVarint32(value.intValue());
        }

        public Integer decode(ProtoReader reader) throws IOException {
            return Integer.valueOf(reader.readVarint32());
        }
    };
    public static final ProtoAdapter<Long> UINT64 = new ProtoAdapter<Long>(FieldEncoding.VARINT, Long.class) {
        /* class com.squareup.wire.ProtoAdapter.AnonymousClass7 */

        public int encodedSize(Long value) {
            return ProtoWriter.varint64Size(value.longValue());
        }

        public void encode(ProtoWriter writer, Long value) throws IOException {
            writer.writeVarint64(value.longValue());
        }

        public Long decode(ProtoReader reader) throws IOException {
            return Long.valueOf(reader.readVarint64());
        }
    };
    private final FieldEncoding fieldEncoding;
    final Class<?> javaType;
    ProtoAdapter<List<E>> packedAdapter;
    ProtoAdapter<List<E>> repeatedAdapter;

    public abstract E decode(ProtoReader protoReader) throws IOException;

    public abstract void encode(ProtoWriter protoWriter, Object obj) throws IOException;

    public abstract int encodedSize(Object obj);

    public ProtoAdapter(FieldEncoding fieldEncoding2, Class<?> javaType2) {
        this.fieldEncoding = fieldEncoding2;
        this.javaType = javaType2;
    }

    public static <M extends Message<M, B>, B extends Message.Builder<M, B>> ProtoAdapter<M> newMessageAdapter(Class<M> type) {
        return RuntimeMessageAdapter.create(type);
    }

    public static <E extends WireEnum> RuntimeEnumAdapter<E> newEnumAdapter(Class<E> type) {
        return new RuntimeEnumAdapter<>(type);
    }

    public static <K, V> ProtoAdapter<Map<K, V>> newMapAdapter(ProtoAdapter<K> keyAdapter, ProtoAdapter<V> valueAdapter) {
        return new MapProtoAdapter(keyAdapter, valueAdapter);
    }

    public static <M extends Message> ProtoAdapter<M> get(M message) {
        return get(message.getClass());
    }

    public static <M> ProtoAdapter<M> get(Class<M> type) {
        try {
            return (ProtoAdapter) type.getField("ADAPTER").get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalArgumentException("failed to access " + type.getName() + "#ADAPTER", e);
        }
    }

    static ProtoAdapter<?> get(String adapterString) {
        try {
            int hash = adapterString.indexOf(35);
            String className = adapterString.substring(0, hash);
            return (ProtoAdapter) Class.forName(className).getField(adapterString.substring(hash + 1)).get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalArgumentException("failed to access " + adapterString, e);
        }
    }

    public E redact(Object obj) {
        return null;
    }

    public int encodedSizeWithTag(int tag, E value) {
        int size = encodedSize(value);
        if (this.fieldEncoding == FieldEncoding.LENGTH_DELIMITED) {
            size += ProtoWriter.varint32Size(size);
        }
        return ProtoWriter.tagSize(tag) + size;
    }

    public void encodeWithTag(ProtoWriter writer, int tag, E value) throws IOException {
        writer.writeTag(tag, this.fieldEncoding);
        if (this.fieldEncoding == FieldEncoding.LENGTH_DELIMITED) {
            writer.writeVarint32(encodedSize(value));
        }
        encode(writer, value);
    }

    public final void encode(BufferedSink sink, Object obj) throws IOException {
        Preconditions.checkNotNull(obj, "value == null");
        Preconditions.checkNotNull(sink, "sink == null");
        encode(new ProtoWriter(sink), obj);
    }

    public final byte[] encode(E value) {
        Preconditions.checkNotNull(value, "value == null");
        Buffer buffer = new Buffer();
        try {
            encode(buffer, value);
            return buffer.readByteArray();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public final void encode(OutputStream stream, Object obj) throws IOException {
        Preconditions.checkNotNull(obj, "value == null");
        Preconditions.checkNotNull(stream, "stream == null");
        BufferedSink buffer = Okio.buffer(Okio.sink(stream));
        encode(buffer, obj);
        buffer.emit();
    }

    public final E decode(byte[] bytes) throws IOException {
        Preconditions.checkNotNull(bytes, "bytes == null");
        return decode(new Buffer().write(bytes));
    }

    public final E decode(ByteString bytes) throws IOException {
        Preconditions.checkNotNull(bytes, "bytes == null");
        return decode(new Buffer().write(bytes));
    }

    public final E decode(InputStream stream) throws IOException {
        Preconditions.checkNotNull(stream, "stream == null");
        return decode(Okio.buffer(Okio.source(stream)));
    }

    public final E decode(BufferedSource source) throws IOException {
        Preconditions.checkNotNull(source, "source == null");
        return decode(new ProtoReader(source));
    }

    public String toString(E value) {
        return value.toString();
    }

    /* access modifiers changed from: package-private */
    public ProtoAdapter<?> withLabel(WireField.Label label) {
        if (!label.isRepeated()) {
            return this;
        }
        if (label.isPacked()) {
            return asPacked();
        }
        return asRepeated();
    }

    public final ProtoAdapter<List<E>> asPacked() {
        ProtoAdapter<List<E>> adapter = this.packedAdapter;
        if (adapter != null) {
            return adapter;
        }
        ProtoAdapter<List<E>> adapter2 = createPacked();
        this.packedAdapter = adapter2;
        return adapter2;
    }

    public final ProtoAdapter<List<E>> asRepeated() {
        ProtoAdapter<List<E>> adapter = this.repeatedAdapter;
        if (adapter != null) {
            return adapter;
        }
        ProtoAdapter<List<E>> adapter2 = createRepeated();
        this.repeatedAdapter = adapter2;
        return adapter2;
    }

    private ProtoAdapter<List<E>> createPacked() {
        if (this.fieldEncoding != FieldEncoding.LENGTH_DELIMITED) {
            return new ProtoAdapter<List<E>>(FieldEncoding.LENGTH_DELIMITED, List.class) {
                /* class com.squareup.wire.ProtoAdapter.AnonymousClass14 */

                /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                 method: com.squareup.wire.ProtoAdapter.encodeWithTag(com.squareup.wire.ProtoWriter, int, java.lang.Object):void
                 arg types: [com.squareup.wire.ProtoWriter, int, java.util.List<E>]
                 candidates:
                  com.squareup.wire.ProtoAdapter.14.encodeWithTag(com.squareup.wire.ProtoWriter, int, java.util.List):void
                  com.squareup.wire.ProtoAdapter.encodeWithTag(com.squareup.wire.ProtoWriter, int, java.lang.Object):void */
                public void encodeWithTag(ProtoWriter writer, int tag, List<E> value) throws IOException {
                    if (!value.isEmpty()) {
                        ProtoAdapter.super.encodeWithTag(writer, tag, (Object) value);
                    }
                }

                public int encodedSize(List<E> value) {
                    int size = 0;
                    int count = value.size();
                    for (int i = 0; i < count; i++) {
                        size += ProtoAdapter.this.encodedSize(value.get(i));
                    }
                    return size;
                }

                /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                 method: com.squareup.wire.ProtoAdapter.encodedSizeWithTag(int, java.lang.Object):int
                 arg types: [int, java.util.List<E>]
                 candidates:
                  com.squareup.wire.ProtoAdapter.14.encodedSizeWithTag(int, java.util.List):int
                  com.squareup.wire.ProtoAdapter.encodedSizeWithTag(int, java.lang.Object):int */
                public int encodedSizeWithTag(int tag, List<E> value) {
                    if (value.isEmpty()) {
                        return 0;
                    }
                    return ProtoAdapter.super.encodedSizeWithTag(tag, (Object) value);
                }

                public void encode(ProtoWriter writer, List<E> value) throws IOException {
                    int count = value.size();
                    for (int i = 0; i < count; i++) {
                        ProtoAdapter.this.encode(writer, value.get(i));
                    }
                }

                public List<E> decode(ProtoReader reader) throws IOException {
                    return Collections.singletonList(ProtoAdapter.this.decode(reader));
                }

                public List<E> redact(List<E> list) {
                    return Collections.emptyList();
                }
            };
        }
        throw new IllegalArgumentException("Unable to pack a length-delimited type.");
    }

    private ProtoAdapter<List<E>> createRepeated() {
        return new ProtoAdapter<List<E>>(this.fieldEncoding, List.class) {
            /* class com.squareup.wire.ProtoAdapter.AnonymousClass15 */

            public int encodedSize(List<E> list) {
                throw new UnsupportedOperationException("Repeated values can only be sized with a tag.");
            }

            public int encodedSizeWithTag(int tag, List<E> value) {
                int size = 0;
                int count = value.size();
                for (int i = 0; i < count; i++) {
                    size += ProtoAdapter.this.encodedSizeWithTag(tag, value.get(i));
                }
                return size;
            }

            public void encode(ProtoWriter writer, List<E> list) {
                throw new UnsupportedOperationException("Repeated values can only be encoded with a tag.");
            }

            public void encodeWithTag(ProtoWriter writer, int tag, List<E> value) throws IOException {
                int count = value.size();
                for (int i = 0; i < count; i++) {
                    ProtoAdapter.this.encodeWithTag(writer, tag, value.get(i));
                }
            }

            public List<E> decode(ProtoReader reader) throws IOException {
                return Collections.singletonList(ProtoAdapter.this.decode(reader));
            }

            public List<E> redact(List<E> list) {
                return Collections.emptyList();
            }
        };
    }

    public static final class EnumConstantNotFoundException extends IllegalArgumentException {
        public final int value;

        EnumConstantNotFoundException(int value2, Class<?> type) {
            super("Unknown enum tag " + value2 + " for " + type.getCanonicalName());
            this.value = value2;
        }
    }

    private static final class MapProtoAdapter<K, V> extends ProtoAdapter<Map<K, V>> {
        private final MapEntryProtoAdapter<K, V> entryAdapter;

        MapProtoAdapter(ProtoAdapter<K> keyAdapter, ProtoAdapter<V> valueAdapter) {
            super(FieldEncoding.LENGTH_DELIMITED, null);
            this.entryAdapter = new MapEntryProtoAdapter<>(keyAdapter, valueAdapter);
        }

        public int encodedSize(Map<K, V> map) {
            throw new UnsupportedOperationException("Repeated values can only be sized with a tag.");
        }

        public int encodedSizeWithTag(int tag, Map<K, V> value) {
            int size = 0;
            for (Map.Entry<K, V> entry : value.entrySet()) {
                size += this.entryAdapter.encodedSizeWithTag(tag, entry);
            }
            return size;
        }

        public void encode(ProtoWriter writer, Map<K, V> map) {
            throw new UnsupportedOperationException("Repeated values can only be encoded with a tag.");
        }

        public void encodeWithTag(ProtoWriter writer, int tag, Map<K, V> value) throws IOException {
            for (Map.Entry<K, V> entry : value.entrySet()) {
                this.entryAdapter.encodeWithTag(writer, tag, entry);
            }
        }

        public Map<K, V> decode(ProtoReader reader) throws IOException {
            K key = null;
            V value = null;
            long token = reader.beginMessage();
            while (true) {
                int tag = reader.nextTag();
                if (tag != -1) {
                    switch (tag) {
                        case 1:
                            key = this.entryAdapter.keyAdapter.decode(reader);
                            break;
                        case 2:
                            value = this.entryAdapter.valueAdapter.decode(reader);
                            break;
                    }
                } else {
                    reader.endMessage(token);
                    if (key == null) {
                        throw new IllegalStateException("Map entry with null key");
                    } else if (value != null) {
                        return Collections.singletonMap(key, value);
                    } else {
                        throw new IllegalStateException("Map entry with null value");
                    }
                }
            }
        }

        public Map<K, V> redact(Map<K, V> map) {
            return Collections.emptyMap();
        }
    }

    private static final class MapEntryProtoAdapter<K, V> extends ProtoAdapter<Map.Entry<K, V>> {
        final ProtoAdapter<K> keyAdapter;
        final ProtoAdapter<V> valueAdapter;

        MapEntryProtoAdapter(ProtoAdapter<K> keyAdapter2, ProtoAdapter<V> valueAdapter2) {
            super(FieldEncoding.LENGTH_DELIMITED, null);
            this.keyAdapter = keyAdapter2;
            this.valueAdapter = valueAdapter2;
        }

        public int encodedSize(Map.Entry<K, V> value) {
            return this.keyAdapter.encodedSizeWithTag(1, value.getKey()) + this.valueAdapter.encodedSizeWithTag(2, value.getValue());
        }

        public void encode(ProtoWriter writer, Map.Entry<K, V> value) throws IOException {
            this.keyAdapter.encodeWithTag(writer, 1, value.getKey());
            this.valueAdapter.encodeWithTag(writer, 2, value.getValue());
        }

        public Map.Entry<K, V> decode(ProtoReader reader) {
            throw new UnsupportedOperationException();
        }
    }
}
