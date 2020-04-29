package com.google.protobuf;

import com.google.protobuf.MapEntryLite;
import com.google.protobuf.Writer;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

final class CodedOutputStreamWriter implements Writer {
    private final CodedOutputStream output;

    public static CodedOutputStreamWriter forCodedOutput(CodedOutputStream output2) {
        if (output2.wrapper != null) {
            return output2.wrapper;
        }
        return new CodedOutputStreamWriter(output2);
    }

    private CodedOutputStreamWriter(CodedOutputStream output2) {
        this.output = (CodedOutputStream) Internal.checkNotNull(output2, "output");
        this.output.wrapper = this;
    }

    public Writer.FieldOrder fieldOrder() {
        return Writer.FieldOrder.ASCENDING;
    }

    public int getTotalBytesWritten() {
        return this.output.getTotalBytesWritten();
    }

    public void writeSFixed32(int fieldNumber, int value) throws IOException {
        this.output.writeSFixed32(fieldNumber, value);
    }

    public void writeInt64(int fieldNumber, long value) throws IOException {
        this.output.writeInt64(fieldNumber, value);
    }

    public void writeSFixed64(int fieldNumber, long value) throws IOException {
        this.output.writeSFixed64(fieldNumber, value);
    }

    public void writeFloat(int fieldNumber, float value) throws IOException {
        this.output.writeFloat(fieldNumber, value);
    }

    public void writeDouble(int fieldNumber, double value) throws IOException {
        this.output.writeDouble(fieldNumber, value);
    }

    public void writeEnum(int fieldNumber, int value) throws IOException {
        this.output.writeEnum(fieldNumber, value);
    }

    public void writeUInt64(int fieldNumber, long value) throws IOException {
        this.output.writeUInt64(fieldNumber, value);
    }

    public void writeInt32(int fieldNumber, int value) throws IOException {
        this.output.writeInt32(fieldNumber, value);
    }

    public void writeFixed64(int fieldNumber, long value) throws IOException {
        this.output.writeFixed64(fieldNumber, value);
    }

    public void writeFixed32(int fieldNumber, int value) throws IOException {
        this.output.writeFixed32(fieldNumber, value);
    }

    public void writeBool(int fieldNumber, boolean value) throws IOException {
        this.output.writeBool(fieldNumber, value);
    }

    public void writeString(int fieldNumber, String value) throws IOException {
        this.output.writeString(fieldNumber, value);
    }

    public void writeBytes(int fieldNumber, ByteString value) throws IOException {
        this.output.writeBytes(fieldNumber, value);
    }

    public void writeUInt32(int fieldNumber, int value) throws IOException {
        this.output.writeUInt32(fieldNumber, value);
    }

    public void writeSInt32(int fieldNumber, int value) throws IOException {
        this.output.writeSInt32(fieldNumber, value);
    }

    public void writeSInt64(int fieldNumber, long value) throws IOException {
        this.output.writeSInt64(fieldNumber, value);
    }

    public void writeMessage(int fieldNumber, Object value) throws IOException {
        this.output.writeMessage(fieldNumber, (MessageLite) value);
    }

    public void writeMessage(int fieldNumber, Object value, Schema schema) throws IOException {
        this.output.writeMessage(fieldNumber, (MessageLite) value, schema);
    }

    public void writeGroup(int fieldNumber, Object value) throws IOException {
        this.output.writeGroup(fieldNumber, (MessageLite) value);
    }

    public void writeGroup(int fieldNumber, Object value, Schema schema) throws IOException {
        this.output.writeGroup(fieldNumber, (MessageLite) value, schema);
    }

    public void writeStartGroup(int fieldNumber) throws IOException {
        this.output.writeTag(fieldNumber, 3);
    }

    public void writeEndGroup(int fieldNumber) throws IOException {
        this.output.writeTag(fieldNumber, 4);
    }

    public final void writeMessageSetItem(int fieldNumber, Object value) throws IOException {
        if (value instanceof ByteString) {
            this.output.writeRawMessageSetExtension(fieldNumber, (ByteString) value);
        } else {
            this.output.writeMessageSetExtension(fieldNumber, (MessageLite) value);
        }
    }

    public void writeInt32List(int fieldNumber, List<Integer> value, boolean packed) throws IOException {
        if (packed) {
            this.output.writeTag(fieldNumber, 2);
            int dataSize = 0;
            for (int i = 0; i < value.size(); i++) {
                dataSize += CodedOutputStream.computeInt32SizeNoTag(value.get(i).intValue());
            }
            this.output.writeUInt32NoTag(dataSize);
            for (int i2 = 0; i2 < value.size(); i2++) {
                this.output.writeInt32NoTag(value.get(i2).intValue());
            }
            return;
        }
        for (int i3 = 0; i3 < value.size(); i3++) {
            this.output.writeInt32(fieldNumber, value.get(i3).intValue());
        }
    }

    public void writeFixed32List(int fieldNumber, List<Integer> value, boolean packed) throws IOException {
        if (packed) {
            this.output.writeTag(fieldNumber, 2);
            int dataSize = 0;
            for (int i = 0; i < value.size(); i++) {
                dataSize += CodedOutputStream.computeFixed32SizeNoTag(value.get(i).intValue());
            }
            this.output.writeUInt32NoTag(dataSize);
            for (int i2 = 0; i2 < value.size(); i2++) {
                this.output.writeFixed32NoTag(value.get(i2).intValue());
            }
            return;
        }
        for (int i3 = 0; i3 < value.size(); i3++) {
            this.output.writeFixed32(fieldNumber, value.get(i3).intValue());
        }
    }

    public void writeInt64List(int fieldNumber, List<Long> value, boolean packed) throws IOException {
        if (packed) {
            this.output.writeTag(fieldNumber, 2);
            int dataSize = 0;
            for (int i = 0; i < value.size(); i++) {
                dataSize += CodedOutputStream.computeInt64SizeNoTag(value.get(i).longValue());
            }
            this.output.writeUInt32NoTag(dataSize);
            for (int i2 = 0; i2 < value.size(); i2++) {
                this.output.writeInt64NoTag(value.get(i2).longValue());
            }
            return;
        }
        for (int i3 = 0; i3 < value.size(); i3++) {
            this.output.writeInt64(fieldNumber, value.get(i3).longValue());
        }
    }

    public void writeUInt64List(int fieldNumber, List<Long> value, boolean packed) throws IOException {
        if (packed) {
            this.output.writeTag(fieldNumber, 2);
            int dataSize = 0;
            for (int i = 0; i < value.size(); i++) {
                dataSize += CodedOutputStream.computeUInt64SizeNoTag(value.get(i).longValue());
            }
            this.output.writeUInt32NoTag(dataSize);
            for (int i2 = 0; i2 < value.size(); i2++) {
                this.output.writeUInt64NoTag(value.get(i2).longValue());
            }
            return;
        }
        for (int i3 = 0; i3 < value.size(); i3++) {
            this.output.writeUInt64(fieldNumber, value.get(i3).longValue());
        }
    }

    public void writeFixed64List(int fieldNumber, List<Long> value, boolean packed) throws IOException {
        if (packed) {
            this.output.writeTag(fieldNumber, 2);
            int dataSize = 0;
            for (int i = 0; i < value.size(); i++) {
                dataSize += CodedOutputStream.computeFixed64SizeNoTag(value.get(i).longValue());
            }
            this.output.writeUInt32NoTag(dataSize);
            for (int i2 = 0; i2 < value.size(); i2++) {
                this.output.writeFixed64NoTag(value.get(i2).longValue());
            }
            return;
        }
        for (int i3 = 0; i3 < value.size(); i3++) {
            this.output.writeFixed64(fieldNumber, value.get(i3).longValue());
        }
    }

    public void writeFloatList(int fieldNumber, List<Float> value, boolean packed) throws IOException {
        if (packed) {
            this.output.writeTag(fieldNumber, 2);
            int dataSize = 0;
            for (int i = 0; i < value.size(); i++) {
                dataSize += CodedOutputStream.computeFloatSizeNoTag(value.get(i).floatValue());
            }
            this.output.writeUInt32NoTag(dataSize);
            for (int i2 = 0; i2 < value.size(); i2++) {
                this.output.writeFloatNoTag(value.get(i2).floatValue());
            }
            return;
        }
        for (int i3 = 0; i3 < value.size(); i3++) {
            this.output.writeFloat(fieldNumber, value.get(i3).floatValue());
        }
    }

    public void writeDoubleList(int fieldNumber, List<Double> value, boolean packed) throws IOException {
        if (packed) {
            this.output.writeTag(fieldNumber, 2);
            int dataSize = 0;
            for (int i = 0; i < value.size(); i++) {
                dataSize += CodedOutputStream.computeDoubleSizeNoTag(value.get(i).doubleValue());
            }
            this.output.writeUInt32NoTag(dataSize);
            for (int i2 = 0; i2 < value.size(); i2++) {
                this.output.writeDoubleNoTag(value.get(i2).doubleValue());
            }
            return;
        }
        for (int i3 = 0; i3 < value.size(); i3++) {
            this.output.writeDouble(fieldNumber, value.get(i3).doubleValue());
        }
    }

    public void writeEnumList(int fieldNumber, List<Integer> value, boolean packed) throws IOException {
        if (packed) {
            this.output.writeTag(fieldNumber, 2);
            int dataSize = 0;
            for (int i = 0; i < value.size(); i++) {
                dataSize += CodedOutputStream.computeEnumSizeNoTag(value.get(i).intValue());
            }
            this.output.writeUInt32NoTag(dataSize);
            for (int i2 = 0; i2 < value.size(); i2++) {
                this.output.writeEnumNoTag(value.get(i2).intValue());
            }
            return;
        }
        for (int i3 = 0; i3 < value.size(); i3++) {
            this.output.writeEnum(fieldNumber, value.get(i3).intValue());
        }
    }

    public void writeBoolList(int fieldNumber, List<Boolean> value, boolean packed) throws IOException {
        if (packed) {
            this.output.writeTag(fieldNumber, 2);
            int dataSize = 0;
            for (int i = 0; i < value.size(); i++) {
                dataSize += CodedOutputStream.computeBoolSizeNoTag(value.get(i).booleanValue());
            }
            this.output.writeUInt32NoTag(dataSize);
            for (int i2 = 0; i2 < value.size(); i2++) {
                this.output.writeBoolNoTag(value.get(i2).booleanValue());
            }
            return;
        }
        for (int i3 = 0; i3 < value.size(); i3++) {
            this.output.writeBool(fieldNumber, value.get(i3).booleanValue());
        }
    }

    public void writeStringList(int fieldNumber, List<String> value) throws IOException {
        if (value instanceof LazyStringList) {
            LazyStringList lazyList = (LazyStringList) value;
            for (int i = 0; i < value.size(); i++) {
                writeLazyString(fieldNumber, lazyList.getRaw(i));
            }
            return;
        }
        for (int i2 = 0; i2 < value.size(); i2++) {
            this.output.writeString(fieldNumber, value.get(i2));
        }
    }

    private void writeLazyString(int fieldNumber, Object value) throws IOException {
        if (value instanceof String) {
            this.output.writeString(fieldNumber, (String) value);
        } else {
            this.output.writeBytes(fieldNumber, (ByteString) value);
        }
    }

    public void writeBytesList(int fieldNumber, List<ByteString> value) throws IOException {
        for (int i = 0; i < value.size(); i++) {
            this.output.writeBytes(fieldNumber, value.get(i));
        }
    }

    public void writeUInt32List(int fieldNumber, List<Integer> value, boolean packed) throws IOException {
        if (packed) {
            this.output.writeTag(fieldNumber, 2);
            int dataSize = 0;
            for (int i = 0; i < value.size(); i++) {
                dataSize += CodedOutputStream.computeUInt32SizeNoTag(value.get(i).intValue());
            }
            this.output.writeUInt32NoTag(dataSize);
            for (int i2 = 0; i2 < value.size(); i2++) {
                this.output.writeUInt32NoTag(value.get(i2).intValue());
            }
            return;
        }
        for (int i3 = 0; i3 < value.size(); i3++) {
            this.output.writeUInt32(fieldNumber, value.get(i3).intValue());
        }
    }

    public void writeSFixed32List(int fieldNumber, List<Integer> value, boolean packed) throws IOException {
        if (packed) {
            this.output.writeTag(fieldNumber, 2);
            int dataSize = 0;
            for (int i = 0; i < value.size(); i++) {
                dataSize += CodedOutputStream.computeSFixed32SizeNoTag(value.get(i).intValue());
            }
            this.output.writeUInt32NoTag(dataSize);
            for (int i2 = 0; i2 < value.size(); i2++) {
                this.output.writeSFixed32NoTag(value.get(i2).intValue());
            }
            return;
        }
        for (int i3 = 0; i3 < value.size(); i3++) {
            this.output.writeSFixed32(fieldNumber, value.get(i3).intValue());
        }
    }

    public void writeSFixed64List(int fieldNumber, List<Long> value, boolean packed) throws IOException {
        if (packed) {
            this.output.writeTag(fieldNumber, 2);
            int dataSize = 0;
            for (int i = 0; i < value.size(); i++) {
                dataSize += CodedOutputStream.computeSFixed64SizeNoTag(value.get(i).longValue());
            }
            this.output.writeUInt32NoTag(dataSize);
            for (int i2 = 0; i2 < value.size(); i2++) {
                this.output.writeSFixed64NoTag(value.get(i2).longValue());
            }
            return;
        }
        for (int i3 = 0; i3 < value.size(); i3++) {
            this.output.writeSFixed64(fieldNumber, value.get(i3).longValue());
        }
    }

    public void writeSInt32List(int fieldNumber, List<Integer> value, boolean packed) throws IOException {
        if (packed) {
            this.output.writeTag(fieldNumber, 2);
            int dataSize = 0;
            for (int i = 0; i < value.size(); i++) {
                dataSize += CodedOutputStream.computeSInt32SizeNoTag(value.get(i).intValue());
            }
            this.output.writeUInt32NoTag(dataSize);
            for (int i2 = 0; i2 < value.size(); i2++) {
                this.output.writeSInt32NoTag(value.get(i2).intValue());
            }
            return;
        }
        for (int i3 = 0; i3 < value.size(); i3++) {
            this.output.writeSInt32(fieldNumber, value.get(i3).intValue());
        }
    }

    public void writeSInt64List(int fieldNumber, List<Long> value, boolean packed) throws IOException {
        if (packed) {
            this.output.writeTag(fieldNumber, 2);
            int dataSize = 0;
            for (int i = 0; i < value.size(); i++) {
                dataSize += CodedOutputStream.computeSInt64SizeNoTag(value.get(i).longValue());
            }
            this.output.writeUInt32NoTag(dataSize);
            for (int i2 = 0; i2 < value.size(); i2++) {
                this.output.writeSInt64NoTag(value.get(i2).longValue());
            }
            return;
        }
        for (int i3 = 0; i3 < value.size(); i3++) {
            this.output.writeSInt64(fieldNumber, value.get(i3).longValue());
        }
    }

    public void writeMessageList(int fieldNumber, List<?> value) throws IOException {
        for (int i = 0; i < value.size(); i++) {
            writeMessage(fieldNumber, value.get(i));
        }
    }

    public void writeMessageList(int fieldNumber, List<?> value, Schema schema) throws IOException {
        for (int i = 0; i < value.size(); i++) {
            writeMessage(fieldNumber, value.get(i), schema);
        }
    }

    public void writeGroupList(int fieldNumber, List<?> value) throws IOException {
        for (int i = 0; i < value.size(); i++) {
            writeGroup(fieldNumber, value.get(i));
        }
    }

    public void writeGroupList(int fieldNumber, List<?> value, Schema schema) throws IOException {
        for (int i = 0; i < value.size(); i++) {
            writeGroup(fieldNumber, value.get(i), schema);
        }
    }

    public <K, V> void writeMap(int fieldNumber, MapEntryLite.Metadata<K, V> metadata, Map<K, V> map) throws IOException {
        if (this.output.isSerializationDeterministic()) {
            writeDeterministicMap(fieldNumber, metadata, map);
            return;
        }
        for (Map.Entry<K, V> entry : map.entrySet()) {
            this.output.writeTag(fieldNumber, 2);
            this.output.writeUInt32NoTag(MapEntryLite.computeSerializedSize(metadata, entry.getKey(), entry.getValue()));
            MapEntryLite.writeTo(this.output, metadata, entry.getKey(), entry.getValue());
        }
    }

    private <K, V> void writeDeterministicMap(int fieldNumber, MapEntryLite.Metadata<K, V> metadata, Map<K, V> map) throws IOException {
        switch (metadata.keyType) {
            case BOOL:
                V value = map.get(Boolean.FALSE);
                if (value != null) {
                    writeDeterministicBooleanMapEntry(fieldNumber, false, value, metadata);
                }
                V value2 = map.get(Boolean.TRUE);
                if (value2 != null) {
                    writeDeterministicBooleanMapEntry(fieldNumber, true, value2, metadata);
                    return;
                }
                return;
            case FIXED32:
            case INT32:
            case SFIXED32:
            case SINT32:
            case UINT32:
                writeDeterministicIntegerMap(fieldNumber, metadata, map);
                return;
            case FIXED64:
            case INT64:
            case SFIXED64:
            case SINT64:
            case UINT64:
                writeDeterministicLongMap(fieldNumber, metadata, map);
                return;
            case STRING:
                writeDeterministicStringMap(fieldNumber, metadata, map);
                return;
            default:
                throw new IllegalArgumentException("does not support key type: " + metadata.keyType);
        }
    }

    private <V> void writeDeterministicBooleanMapEntry(int fieldNumber, boolean key, V value, MapEntryLite.Metadata<Boolean, V> metadata) throws IOException {
        this.output.writeTag(fieldNumber, 2);
        this.output.writeUInt32NoTag(MapEntryLite.computeSerializedSize(metadata, Boolean.valueOf(key), value));
        MapEntryLite.writeTo(this.output, metadata, Boolean.valueOf(key), value);
    }

    private <V> void writeDeterministicIntegerMap(int fieldNumber, MapEntryLite.Metadata<Integer, V> metadata, Map<Integer, V> map) throws IOException {
        int[] keys = new int[map.size()];
        int index = 0;
        for (Integer num : map.keySet()) {
            keys[index] = num.intValue();
            index++;
        }
        Arrays.sort(keys);
        for (int key : keys) {
            V value = map.get(Integer.valueOf(key));
            this.output.writeTag(fieldNumber, 2);
            this.output.writeUInt32NoTag(MapEntryLite.computeSerializedSize(metadata, Integer.valueOf(key), value));
            MapEntryLite.writeTo(this.output, metadata, Integer.valueOf(key), value);
        }
    }

    private <V> void writeDeterministicLongMap(int fieldNumber, MapEntryLite.Metadata<Long, V> metadata, Map<Long, V> map) throws IOException {
        long[] keys = new long[map.size()];
        int index = 0;
        for (Long l : map.keySet()) {
            keys[index] = l.longValue();
            index++;
        }
        Arrays.sort(keys);
        for (long key : keys) {
            V value = map.get(Long.valueOf(key));
            this.output.writeTag(fieldNumber, 2);
            this.output.writeUInt32NoTag(MapEntryLite.computeSerializedSize(metadata, Long.valueOf(key), value));
            MapEntryLite.writeTo(this.output, metadata, Long.valueOf(key), value);
        }
    }

    private <V> void writeDeterministicStringMap(int fieldNumber, MapEntryLite.Metadata<String, V> metadata, Map<String, V> map) throws IOException {
        String[] keys = new String[map.size()];
        int index = 0;
        for (String k : map.keySet()) {
            keys[index] = k;
            index++;
        }
        Arrays.sort(keys);
        for (String key : keys) {
            V value = map.get(key);
            this.output.writeTag(fieldNumber, 2);
            this.output.writeUInt32NoTag(MapEntryLite.computeSerializedSize(metadata, key, value));
            MapEntryLite.writeTo(this.output, metadata, key, value);
        }
    }
}
