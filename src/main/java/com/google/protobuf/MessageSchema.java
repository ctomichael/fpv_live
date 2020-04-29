package com.google.protobuf;

import com.google.protobuf.ArrayDecoders;
import com.google.protobuf.ByteString;
import com.google.protobuf.FieldSet;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MapEntryLite;
import com.google.protobuf.WireFormat;
import com.google.protobuf.Writer;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import sun.misc.Unsafe;

final class MessageSchema<T> implements Schema<T> {
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private static final int ENFORCE_UTF8_MASK = 536870912;
    private static final int FIELD_TYPE_MASK = 267386880;
    private static final int INTS_PER_FIELD = 3;
    private static final int OFFSET_BITS = 20;
    private static final int OFFSET_MASK = 1048575;
    static final int ONEOF_TYPE_OFFSET = 51;
    private static final int REQUIRED_MASK = 268435456;
    private static final Unsafe UNSAFE = UnsafeUtil.getUnsafe();
    private final int[] buffer;
    private final int checkInitializedCount;
    private final MessageLite defaultInstance;
    private final ExtensionSchema<?> extensionSchema;
    private final boolean hasExtensions;
    private final int[] intArray;
    private final ListFieldSchema listFieldSchema;
    private final boolean lite;
    private final MapFieldSchema mapFieldSchema;
    private final int maxFieldNumber;
    private final int minFieldNumber;
    private final NewInstanceSchema newInstanceSchema;
    private final Object[] objects;
    private final boolean proto3;
    private final int repeatedFieldOffsetStart;
    private final UnknownFieldSchema<?, ?> unknownFieldSchema;
    private final boolean useCachedSizeField;

    private MessageSchema(int[] buffer2, Object[] objects2, int minFieldNumber2, int maxFieldNumber2, MessageLite defaultInstance2, boolean proto32, boolean useCachedSizeField2, int[] intArray2, int checkInitialized, int mapFieldPositions, NewInstanceSchema newInstanceSchema2, ListFieldSchema listFieldSchema2, UnknownFieldSchema<?, ?> unknownFieldSchema2, ExtensionSchema<?> extensionSchema2, MapFieldSchema mapFieldSchema2) {
        this.buffer = buffer2;
        this.objects = objects2;
        this.minFieldNumber = minFieldNumber2;
        this.maxFieldNumber = maxFieldNumber2;
        this.lite = defaultInstance2 instanceof GeneratedMessageLite;
        this.proto3 = proto32;
        this.hasExtensions = extensionSchema2 != null && extensionSchema2.hasExtensions(defaultInstance2);
        this.useCachedSizeField = useCachedSizeField2;
        this.intArray = intArray2;
        this.checkInitializedCount = checkInitialized;
        this.repeatedFieldOffsetStart = mapFieldPositions;
        this.newInstanceSchema = newInstanceSchema2;
        this.listFieldSchema = listFieldSchema2;
        this.unknownFieldSchema = unknownFieldSchema2;
        this.extensionSchema = extensionSchema2;
        this.defaultInstance = defaultInstance2;
        this.mapFieldSchema = mapFieldSchema2;
    }

    static <T> MessageSchema<T> newSchema(Class<T> cls, MessageInfo messageInfo, NewInstanceSchema newInstanceSchema2, ListFieldSchema listFieldSchema2, UnknownFieldSchema<?, ?> unknownFieldSchema2, ExtensionSchema<?> extensionSchema2, MapFieldSchema mapFieldSchema2) {
        if (messageInfo instanceof RawMessageInfo) {
            return newSchemaForRawMessageInfo((RawMessageInfo) messageInfo, newInstanceSchema2, listFieldSchema2, unknownFieldSchema2, extensionSchema2, mapFieldSchema2);
        }
        return newSchemaForMessageInfo((StructuralMessageInfo) messageInfo, newInstanceSchema2, listFieldSchema2, unknownFieldSchema2, extensionSchema2, mapFieldSchema2);
    }

    static <T> MessageSchema<T> newSchemaForRawMessageInfo(RawMessageInfo messageInfo, NewInstanceSchema newInstanceSchema2, ListFieldSchema listFieldSchema2, UnknownFieldSchema<?, ?> unknownFieldSchema2, ExtensionSchema<?> extensionSchema2, MapFieldSchema mapFieldSchema2) {
        int i;
        int oneofCount;
        int i2;
        int i3;
        int minFieldNumber2;
        int i4;
        int maxFieldNumber2;
        int i5;
        int numEntries;
        int i6;
        int mapFieldCount;
        int i7;
        int i8;
        int checkInitialized;
        int[] intArray2;
        int objectsPosition;
        int next;
        int next2;
        int next3;
        int next4;
        int next5;
        int next6;
        int next7;
        int i9;
        int next8;
        int objectsPosition2;
        int i10;
        int fieldOffset;
        int presenceFieldOffset;
        int presenceMaskShift;
        int i11;
        Field hasBitsField;
        int i12;
        int next9;
        Field oneofField;
        Field oneofCaseField;
        int i13;
        int next10;
        int next11;
        int i14;
        int next12;
        int next13;
        int next14;
        boolean isProto3 = messageInfo.getSyntax() == ProtoSyntax.PROTO3;
        String info = messageInfo.getStringInfo();
        int length = info.length();
        int i15 = 0 + 1;
        int next15 = info.charAt(0);
        if (next15 >= 55296) {
            int result = next15 & 8191;
            int shift = 13;
            while (true) {
                int i16 = i15;
                i15 = i16 + 1;
                next14 = info.charAt(i16);
                if (next14 < 55296) {
                    break;
                }
                result |= (next14 & 8191) << shift;
                shift += 13;
            }
            next15 = result | (next14 << shift);
            i = i15;
        } else {
            i = i15;
        }
        int flags = next15;
        int i17 = i + 1;
        int next16 = info.charAt(i);
        if (next16 >= 55296) {
            int result2 = next16 & 8191;
            int shift2 = 13;
            while (true) {
                int i18 = i17;
                i17 = i18 + 1;
                next13 = info.charAt(i18);
                if (next13 < 55296) {
                    break;
                }
                result2 |= (next13 & 8191) << shift2;
                shift2 += 13;
            }
            next16 = result2 | (next13 << shift2);
        }
        if (next16 == 0) {
            oneofCount = 0;
            minFieldNumber2 = 0;
            maxFieldNumber2 = 0;
            numEntries = 0;
            mapFieldCount = 0;
            checkInitialized = 0;
            intArray2 = EMPTY_INT_ARRAY;
            objectsPosition = 0;
            i8 = i17;
        } else {
            int i19 = i17 + 1;
            int next17 = info.charAt(i17);
            if (next17 >= 55296) {
                int result3 = next17 & 8191;
                int shift3 = 13;
                while (true) {
                    i9 = i19 + 1;
                    next8 = info.charAt(i19);
                    if (next8 < 55296) {
                        break;
                    }
                    result3 |= (next8 & 8191) << shift3;
                    shift3 += 13;
                    i19 = i9;
                }
                next17 = result3 | (next8 << shift3);
                i19 = i9;
            }
            oneofCount = next17;
            int i20 = i19 + 1;
            int next18 = info.charAt(i19);
            if (next18 >= 55296) {
                int result4 = next18 & 8191;
                int shift4 = 13;
                while (true) {
                    int i21 = i20;
                    i20 = i21 + 1;
                    next7 = info.charAt(i21);
                    if (next7 < 55296) {
                        break;
                    }
                    result4 |= (next7 & 8191) << shift4;
                    shift4 += 13;
                }
                next18 = result4 | (next7 << shift4);
                i2 = i20;
            } else {
                i2 = i20;
            }
            int hasBitsCount = next18;
            int i22 = i2 + 1;
            int next19 = info.charAt(i2);
            if (next19 >= 55296) {
                int result5 = next19 & 8191;
                int shift5 = 13;
                while (true) {
                    int i23 = i22;
                    i22 = i23 + 1;
                    next6 = info.charAt(i23);
                    if (next6 < 55296) {
                        break;
                    }
                    result5 |= (next6 & 8191) << shift5;
                    shift5 += 13;
                }
                next19 = result5 | (next6 << shift5);
                i3 = i22;
            } else {
                i3 = i22;
            }
            minFieldNumber2 = next19;
            int i24 = i3 + 1;
            int next20 = info.charAt(i3);
            if (next20 >= 55296) {
                int result6 = next20 & 8191;
                int shift6 = 13;
                while (true) {
                    int i25 = i24;
                    i24 = i25 + 1;
                    next5 = info.charAt(i25);
                    if (next5 < 55296) {
                        break;
                    }
                    result6 |= (next5 & 8191) << shift6;
                    shift6 += 13;
                }
                next20 = result6 | (next5 << shift6);
                i4 = i24;
            } else {
                i4 = i24;
            }
            maxFieldNumber2 = next20;
            int i26 = i4 + 1;
            int next21 = info.charAt(i4);
            if (next21 >= 55296) {
                int result7 = next21 & 8191;
                int shift7 = 13;
                while (true) {
                    int i27 = i26;
                    i26 = i27 + 1;
                    next4 = info.charAt(i27);
                    if (next4 < 55296) {
                        break;
                    }
                    result7 |= (next4 & 8191) << shift7;
                    shift7 += 13;
                }
                next21 = result7 | (next4 << shift7);
                i5 = i26;
            } else {
                i5 = i26;
            }
            numEntries = next21;
            int i28 = i5 + 1;
            int next22 = info.charAt(i5);
            if (next22 >= 55296) {
                int result8 = next22 & 8191;
                int shift8 = 13;
                while (true) {
                    int i29 = i28;
                    i28 = i29 + 1;
                    next3 = info.charAt(i29);
                    if (next3 < 55296) {
                        break;
                    }
                    result8 |= (next3 & 8191) << shift8;
                    shift8 += 13;
                }
                next22 = result8 | (next3 << shift8);
                i6 = i28;
            } else {
                i6 = i28;
            }
            mapFieldCount = next22;
            int i30 = i6 + 1;
            int next23 = info.charAt(i6);
            if (next23 >= 55296) {
                int result9 = next23 & 8191;
                int shift9 = 13;
                while (true) {
                    int i31 = i30;
                    i30 = i31 + 1;
                    next2 = info.charAt(i31);
                    if (next2 < 55296) {
                        break;
                    }
                    result9 |= (next2 & 8191) << shift9;
                    shift9 += 13;
                }
                next23 = result9 | (next2 << shift9);
                i7 = i30;
            } else {
                i7 = i30;
            }
            int repeatedFieldCount = next23;
            int i32 = i7 + 1;
            int next24 = info.charAt(i7);
            if (next24 >= 55296) {
                int result10 = next24 & 8191;
                int shift10 = 13;
                while (true) {
                    int i33 = i32;
                    i32 = i33 + 1;
                    next = info.charAt(i33);
                    if (next < 55296) {
                        break;
                    }
                    result10 |= (next & 8191) << shift10;
                    shift10 += 13;
                }
                next24 = result10 | (next << shift10);
                i8 = i32;
            } else {
                i8 = i32;
            }
            checkInitialized = next24;
            intArray2 = new int[(checkInitialized + mapFieldCount + repeatedFieldCount)];
            objectsPosition = (oneofCount * 2) + hasBitsCount;
        }
        Unsafe unsafe = UNSAFE;
        Object[] messageInfoObjects = messageInfo.getObjects();
        int checkInitializedPosition = 0;
        Class<?> messageClass = messageInfo.getDefaultInstance().getClass();
        int[] buffer2 = new int[(numEntries * 3)];
        Object[] objects2 = new Object[(numEntries * 2)];
        int mapFieldIndex = checkInitialized;
        int repeatedFieldIndex = checkInitialized + mapFieldCount;
        int bufferIndex = 0;
        while (true) {
            int bufferIndex2 = bufferIndex;
            int repeatedFieldIndex2 = repeatedFieldIndex;
            int mapFieldIndex2 = mapFieldIndex;
            int checkInitializedPosition2 = checkInitializedPosition;
            int objectsPosition3 = objectsPosition2;
            int i34 = i10;
            if (i34 >= length) {
                return new MessageSchema<>(buffer2, objects2, minFieldNumber2, maxFieldNumber2, messageInfo.getDefaultInstance(), isProto3, false, intArray2, checkInitialized, checkInitialized + mapFieldCount, newInstanceSchema2, listFieldSchema2, unknownFieldSchema2, extensionSchema2, mapFieldSchema2);
            }
            int i35 = i34 + 1;
            int next25 = info.charAt(i34);
            if (next25 >= 55296) {
                int result11 = next25 & 8191;
                int shift11 = 13;
                while (true) {
                    i14 = i35 + 1;
                    next12 = info.charAt(i35);
                    if (next12 < 55296) {
                        break;
                    }
                    result11 |= (next12 & 8191) << shift11;
                    shift11 += 13;
                    i35 = i14;
                }
                next25 = result11 | (next12 << shift11);
                i35 = i14;
            }
            int fieldNumber = next25;
            int i36 = i35 + 1;
            int next26 = info.charAt(i35);
            if (next26 >= 55296) {
                int result12 = next26 & 8191;
                int shift12 = 13;
                while (true) {
                    int i37 = i36;
                    i36 = i37 + 1;
                    next11 = info.charAt(i37);
                    if (next11 < 55296) {
                        break;
                    }
                    result12 |= (next11 & 8191) << shift12;
                    shift12 += 13;
                }
                next26 = result12 | (next11 << shift12);
            }
            int fieldTypeWithExtraBits = next26;
            int fieldType = fieldTypeWithExtraBits & 255;
            if ((fieldTypeWithExtraBits & 1024) != 0) {
                checkInitializedPosition = checkInitializedPosition2 + 1;
                intArray2[checkInitializedPosition2] = bufferIndex2;
            } else {
                checkInitializedPosition = checkInitializedPosition2;
            }
            if (fieldType >= 51) {
                i10 = i36 + 1;
                int next27 = info.charAt(i36);
                if (next27 >= 55296) {
                    int result13 = next27 & 8191;
                    int shift13 = 13;
                    while (true) {
                        i13 = i10 + 1;
                        next10 = info.charAt(i10);
                        if (next10 < 55296) {
                            break;
                        }
                        result13 |= (next10 & 8191) << shift13;
                        shift13 += 13;
                        i10 = i13;
                    }
                    next27 = result13 | (next10 << shift13);
                    i10 = i13;
                }
                int oneofIndex = next27;
                int oneofFieldType = fieldType - 51;
                if (oneofFieldType == 9 || oneofFieldType == 17) {
                    objectsPosition2 = objectsPosition3 + 1;
                    objects2[((bufferIndex2 / 3) * 2) + 1] = messageInfoObjects[objectsPosition3];
                } else if (oneofFieldType == 12 && (flags & 1) == 1) {
                    objectsPosition2 = objectsPosition3 + 1;
                    objects2[((bufferIndex2 / 3) * 2) + 1] = messageInfoObjects[objectsPosition3];
                } else {
                    objectsPosition2 = objectsPosition3;
                }
                int index = oneofIndex * 2;
                Object o = messageInfoObjects[index];
                if (o instanceof Field) {
                    oneofField = (Field) o;
                } else {
                    oneofField = reflectField(messageClass, (String) o);
                    messageInfoObjects[index] = oneofField;
                }
                fieldOffset = (int) unsafe.objectFieldOffset(oneofField);
                int index2 = index + 1;
                Object o2 = messageInfoObjects[index2];
                if (o2 instanceof Field) {
                    oneofCaseField = (Field) o2;
                } else {
                    oneofCaseField = reflectField(messageClass, (String) o2);
                    messageInfoObjects[index2] = oneofCaseField;
                }
                presenceFieldOffset = (int) unsafe.objectFieldOffset(oneofCaseField);
                presenceMaskShift = 0;
                repeatedFieldIndex = repeatedFieldIndex2;
                mapFieldIndex = mapFieldIndex2;
            } else {
                int objectsPosition4 = objectsPosition3 + 1;
                Field field = reflectField(messageClass, (String) messageInfoObjects[objectsPosition3]);
                if (fieldType == 9 || fieldType == 17) {
                    objects2[((bufferIndex2 / 3) * 2) + 1] = field.getType();
                    mapFieldIndex = mapFieldIndex2;
                } else if (fieldType == 27 || fieldType == 49) {
                    objects2[((bufferIndex2 / 3) * 2) + 1] = messageInfoObjects[objectsPosition4];
                    mapFieldIndex = mapFieldIndex2;
                    objectsPosition4++;
                } else {
                    if (fieldType == 12 || fieldType == 30 || fieldType == 44) {
                        if ((flags & 1) == 1) {
                            objects2[((bufferIndex2 / 3) * 2) + 1] = messageInfoObjects[objectsPosition4];
                            mapFieldIndex = mapFieldIndex2;
                            objectsPosition4++;
                        }
                    } else if (fieldType == 50) {
                        mapFieldIndex = mapFieldIndex2 + 1;
                        intArray2[mapFieldIndex2] = bufferIndex2;
                        int objectsPosition5 = objectsPosition4 + 1;
                        objects2[(bufferIndex2 / 3) * 2] = messageInfoObjects[objectsPosition4];
                        if ((fieldTypeWithExtraBits & 2048) != 0) {
                            objectsPosition4 = objectsPosition5 + 1;
                            objects2[((bufferIndex2 / 3) * 2) + 1] = messageInfoObjects[objectsPosition5];
                        } else {
                            objectsPosition4 = objectsPosition5;
                        }
                    }
                    mapFieldIndex = mapFieldIndex2;
                }
                fieldOffset = (int) unsafe.objectFieldOffset(field);
                if ((flags & 1) != 1 || fieldType > 17) {
                    presenceFieldOffset = 0;
                    presenceMaskShift = 0;
                    i11 = i36;
                } else {
                    i11 = i36 + 1;
                    int next28 = info.charAt(i36);
                    if (next28 >= 55296) {
                        int result14 = next28 & 8191;
                        int shift14 = 13;
                        while (true) {
                            i12 = i11 + 1;
                            next9 = info.charAt(i11);
                            if (next9 < 55296) {
                                break;
                            }
                            result14 |= (next9 & 8191) << shift14;
                            shift14 += 13;
                            i11 = i12;
                        }
                        next28 = result14 | (next9 << shift14);
                        i11 = i12;
                    }
                    int hasBitsIndex = next28;
                    int index3 = (oneofCount * 2) + (hasBitsIndex / 32);
                    Object o3 = messageInfoObjects[index3];
                    if (o3 instanceof Field) {
                        hasBitsField = (Field) o3;
                    } else {
                        hasBitsField = reflectField(messageClass, (String) o3);
                        messageInfoObjects[index3] = hasBitsField;
                    }
                    presenceFieldOffset = (int) unsafe.objectFieldOffset(hasBitsField);
                    presenceMaskShift = hasBitsIndex % 32;
                }
                if (fieldType < 18 || fieldType > 49) {
                    repeatedFieldIndex = repeatedFieldIndex2;
                } else {
                    repeatedFieldIndex = repeatedFieldIndex2 + 1;
                    intArray2[repeatedFieldIndex2] = fieldOffset;
                }
            }
            int bufferIndex3 = bufferIndex2 + 1;
            buffer2[bufferIndex2] = fieldNumber;
            int bufferIndex4 = bufferIndex3 + 1;
            buffer2[bufferIndex3] = ((fieldTypeWithExtraBits & 256) != 0 ? 268435456 : 0) | ((fieldTypeWithExtraBits & 512) != 0 ? 536870912 : 0) | (fieldType << 20) | fieldOffset;
            bufferIndex = bufferIndex4 + 1;
            buffer2[bufferIndex4] = (presenceMaskShift << 20) | presenceFieldOffset;
        }
    }

    private static Field reflectField(Class<?> messageClass, String fieldName) {
        try {
            return messageClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Field[] fields = messageClass.getDeclaredFields();
            for (Field field : fields) {
                if (fieldName.equals(field.getName())) {
                    return field;
                }
            }
            throw new RuntimeException("Field " + fieldName + " for " + messageClass.getName() + " not found. Known fields are " + Arrays.toString(fields));
        }
    }

    static <T> MessageSchema<T> newSchemaForMessageInfo(StructuralMessageInfo messageInfo, NewInstanceSchema newInstanceSchema2, ListFieldSchema listFieldSchema2, UnknownFieldSchema<?, ?> unknownFieldSchema2, ExtensionSchema<?> extensionSchema2, MapFieldSchema mapFieldSchema2) {
        int minFieldNumber2;
        int maxFieldNumber2;
        boolean isProto3 = messageInfo.getSyntax() == ProtoSyntax.PROTO3;
        FieldInfo[] fis = messageInfo.getFields();
        if (fis.length == 0) {
            minFieldNumber2 = 0;
            maxFieldNumber2 = 0;
        } else {
            minFieldNumber2 = fis[0].getFieldNumber();
            maxFieldNumber2 = fis[fis.length - 1].getFieldNumber();
        }
        int numEntries = fis.length;
        int[] buffer2 = new int[(numEntries * 3)];
        Object[] objects2 = new Object[(numEntries * 2)];
        int mapFieldCount = 0;
        int repeatedFieldCount = 0;
        for (FieldInfo fi : fis) {
            if (fi.getType() == FieldType.MAP) {
                mapFieldCount++;
            } else if (fi.getType().id() >= 18 && fi.getType().id() <= 49) {
                repeatedFieldCount++;
            }
        }
        int[] mapFieldPositions = mapFieldCount > 0 ? new int[mapFieldCount] : null;
        int[] repeatedFieldOffsets = repeatedFieldCount > 0 ? new int[repeatedFieldCount] : null;
        int mapFieldCount2 = 0;
        int repeatedFieldCount2 = 0;
        int[] checkInitialized = messageInfo.getCheckInitialized();
        if (checkInitialized == null) {
            checkInitialized = EMPTY_INT_ARRAY;
        }
        int checkInitializedIndex = 0;
        int fieldIndex = 0;
        int bufferIndex = 0;
        while (fieldIndex < fis.length) {
            FieldInfo fi2 = fis[fieldIndex];
            int fieldNumber = fi2.getFieldNumber();
            storeFieldData(fi2, buffer2, bufferIndex, isProto3, objects2);
            if (checkInitializedIndex < checkInitialized.length && checkInitialized[checkInitializedIndex] == fieldNumber) {
                checkInitialized[checkInitializedIndex] = bufferIndex;
                checkInitializedIndex++;
            }
            if (fi2.getType() == FieldType.MAP) {
                mapFieldPositions[mapFieldCount2] = bufferIndex;
                mapFieldCount2++;
            } else if (fi2.getType().id() >= 18 && fi2.getType().id() <= 49) {
                repeatedFieldOffsets[repeatedFieldCount2] = (int) UnsafeUtil.objectFieldOffset(fi2.getField());
                repeatedFieldCount2++;
            }
            fieldIndex++;
            bufferIndex += 3;
        }
        if (mapFieldPositions == null) {
            mapFieldPositions = EMPTY_INT_ARRAY;
        }
        if (repeatedFieldOffsets == null) {
            repeatedFieldOffsets = EMPTY_INT_ARRAY;
        }
        int[] combined = new int[(checkInitialized.length + mapFieldPositions.length + repeatedFieldOffsets.length)];
        System.arraycopy(checkInitialized, 0, combined, 0, checkInitialized.length);
        System.arraycopy(mapFieldPositions, 0, combined, checkInitialized.length, mapFieldPositions.length);
        System.arraycopy(repeatedFieldOffsets, 0, combined, checkInitialized.length + mapFieldPositions.length, repeatedFieldOffsets.length);
        return new MessageSchema<>(buffer2, objects2, minFieldNumber2, maxFieldNumber2, messageInfo.getDefaultInstance(), isProto3, true, combined, checkInitialized.length, checkInitialized.length + mapFieldPositions.length, newInstanceSchema2, listFieldSchema2, unknownFieldSchema2, extensionSchema2, mapFieldSchema2);
    }

    private static void storeFieldData(FieldInfo fi, int[] buffer2, int bufferIndex, boolean proto32, Object[] objects2) {
        int fieldOffset;
        int typeId;
        int presenceFieldOffset;
        int presenceMaskShift;
        OneofInfo oneof = fi.getOneof();
        if (oneof != null) {
            typeId = fi.getType().id() + 51;
            fieldOffset = (int) UnsafeUtil.objectFieldOffset(oneof.getValueField());
            presenceFieldOffset = (int) UnsafeUtil.objectFieldOffset(oneof.getCaseField());
            presenceMaskShift = 0;
        } else {
            FieldType type = fi.getType();
            fieldOffset = (int) UnsafeUtil.objectFieldOffset(fi.getField());
            typeId = type.id();
            if (!proto32 && !type.isList() && !type.isMap()) {
                presenceFieldOffset = (int) UnsafeUtil.objectFieldOffset(fi.getPresenceField());
                presenceMaskShift = Integer.numberOfTrailingZeros(fi.getPresenceMask());
            } else if (fi.getCachedSizeField() == null) {
                presenceFieldOffset = 0;
                presenceMaskShift = 0;
            } else {
                presenceFieldOffset = (int) UnsafeUtil.objectFieldOffset(fi.getCachedSizeField());
                presenceMaskShift = 0;
            }
        }
        buffer2[bufferIndex] = fi.getFieldNumber();
        buffer2[bufferIndex + 1] = (fi.isEnforceUtf8() ? 536870912 : 0) | (fi.isRequired() ? 268435456 : 0) | (typeId << 20) | fieldOffset;
        buffer2[bufferIndex + 2] = (presenceMaskShift << 20) | presenceFieldOffset;
        Class messageFieldClass = fi.getMessageFieldClass();
        if (fi.getMapDefaultEntry() != null) {
            objects2[(bufferIndex / 3) * 2] = fi.getMapDefaultEntry();
            if (messageFieldClass != null) {
                objects2[((bufferIndex / 3) * 2) + 1] = messageFieldClass;
            } else if (fi.getEnumVerifier() != null) {
                objects2[((bufferIndex / 3) * 2) + 1] = fi.getEnumVerifier();
            }
        } else if (messageFieldClass != null) {
            objects2[((bufferIndex / 3) * 2) + 1] = messageFieldClass;
        } else if (fi.getEnumVerifier() != null) {
            objects2[((bufferIndex / 3) * 2) + 1] = fi.getEnumVerifier();
        }
    }

    public T newInstance() {
        return this.newInstanceSchema.newInstance(this.defaultInstance);
    }

    public boolean equals(T message, T other) {
        int bufferLength = this.buffer.length;
        for (int pos = 0; pos < bufferLength; pos += 3) {
            if (!equals(message, other, pos)) {
                return false;
            }
        }
        if (!this.unknownFieldSchema.getFromMessage(message).equals(this.unknownFieldSchema.getFromMessage(other))) {
            return false;
        }
        if (this.hasExtensions) {
            return this.extensionSchema.getExtensions(message).equals(this.extensionSchema.getExtensions(other));
        }
        return true;
    }

    private boolean equals(T message, T other, int pos) {
        int typeAndOffset = typeAndOffsetAt(pos);
        long offset = offset(typeAndOffset);
        switch (type(typeAndOffset)) {
            case 0:
                if (!arePresentForEquals(message, other, pos) || Double.doubleToLongBits(UnsafeUtil.getDouble(message, offset)) != Double.doubleToLongBits(UnsafeUtil.getDouble(other, offset))) {
                    return false;
                }
                return true;
            case 1:
                if (!arePresentForEquals(message, other, pos) || Float.floatToIntBits(UnsafeUtil.getFloat(message, offset)) != Float.floatToIntBits(UnsafeUtil.getFloat(other, offset))) {
                    return false;
                }
                return true;
            case 2:
                if (!arePresentForEquals(message, other, pos) || UnsafeUtil.getLong(message, offset) != UnsafeUtil.getLong(other, offset)) {
                    return false;
                }
                return true;
            case 3:
                if (!arePresentForEquals(message, other, pos) || UnsafeUtil.getLong(message, offset) != UnsafeUtil.getLong(other, offset)) {
                    return false;
                }
                return true;
            case 4:
                if (!arePresentForEquals(message, other, pos) || UnsafeUtil.getInt(message, offset) != UnsafeUtil.getInt(other, offset)) {
                    return false;
                }
                return true;
            case 5:
                if (!arePresentForEquals(message, other, pos) || UnsafeUtil.getLong(message, offset) != UnsafeUtil.getLong(other, offset)) {
                    return false;
                }
                return true;
            case 6:
                if (!arePresentForEquals(message, other, pos) || UnsafeUtil.getInt(message, offset) != UnsafeUtil.getInt(other, offset)) {
                    return false;
                }
                return true;
            case 7:
                if (!arePresentForEquals(message, other, pos) || UnsafeUtil.getBoolean(message, offset) != UnsafeUtil.getBoolean(other, offset)) {
                    return false;
                }
                return true;
            case 8:
                if (!arePresentForEquals(message, other, pos) || !SchemaUtil.safeEquals(UnsafeUtil.getObject(message, offset), UnsafeUtil.getObject(other, offset))) {
                    return false;
                }
                return true;
            case 9:
                if (!arePresentForEquals(message, other, pos) || !SchemaUtil.safeEquals(UnsafeUtil.getObject(message, offset), UnsafeUtil.getObject(other, offset))) {
                    return false;
                }
                return true;
            case 10:
                if (!arePresentForEquals(message, other, pos) || !SchemaUtil.safeEquals(UnsafeUtil.getObject(message, offset), UnsafeUtil.getObject(other, offset))) {
                    return false;
                }
                return true;
            case 11:
                if (!arePresentForEquals(message, other, pos) || UnsafeUtil.getInt(message, offset) != UnsafeUtil.getInt(other, offset)) {
                    return false;
                }
                return true;
            case 12:
                if (!arePresentForEquals(message, other, pos) || UnsafeUtil.getInt(message, offset) != UnsafeUtil.getInt(other, offset)) {
                    return false;
                }
                return true;
            case 13:
                if (!arePresentForEquals(message, other, pos) || UnsafeUtil.getInt(message, offset) != UnsafeUtil.getInt(other, offset)) {
                    return false;
                }
                return true;
            case 14:
                if (!arePresentForEquals(message, other, pos) || UnsafeUtil.getLong(message, offset) != UnsafeUtil.getLong(other, offset)) {
                    return false;
                }
                return true;
            case 15:
                if (!arePresentForEquals(message, other, pos) || UnsafeUtil.getInt(message, offset) != UnsafeUtil.getInt(other, offset)) {
                    return false;
                }
                return true;
            case 16:
                if (!arePresentForEquals(message, other, pos) || UnsafeUtil.getLong(message, offset) != UnsafeUtil.getLong(other, offset)) {
                    return false;
                }
                return true;
            case 17:
                if (!arePresentForEquals(message, other, pos) || !SchemaUtil.safeEquals(UnsafeUtil.getObject(message, offset), UnsafeUtil.getObject(other, offset))) {
                    return false;
                }
                return true;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
                return SchemaUtil.safeEquals(UnsafeUtil.getObject(message, offset), UnsafeUtil.getObject(other, offset));
            case 50:
                return SchemaUtil.safeEquals(UnsafeUtil.getObject(message, offset), UnsafeUtil.getObject(other, offset));
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
                if (!isOneofCaseEqual(message, other, pos) || !SchemaUtil.safeEquals(UnsafeUtil.getObject(message, offset), UnsafeUtil.getObject(other, offset))) {
                    return false;
                }
                return true;
            default:
                return true;
        }
    }

    public int hashCode(T message) {
        int hashCode = 0;
        int bufferLength = this.buffer.length;
        for (int pos = 0; pos < bufferLength; pos += 3) {
            int typeAndOffset = typeAndOffsetAt(pos);
            int entryNumber = numberAt(pos);
            long offset = offset(typeAndOffset);
            switch (type(typeAndOffset)) {
                case 0:
                    hashCode = (hashCode * 53) + Internal.hashLong(Double.doubleToLongBits(UnsafeUtil.getDouble(message, offset)));
                    break;
                case 1:
                    hashCode = (hashCode * 53) + Float.floatToIntBits(UnsafeUtil.getFloat(message, offset));
                    break;
                case 2:
                    hashCode = (hashCode * 53) + Internal.hashLong(UnsafeUtil.getLong(message, offset));
                    break;
                case 3:
                    hashCode = (hashCode * 53) + Internal.hashLong(UnsafeUtil.getLong(message, offset));
                    break;
                case 4:
                    hashCode = (hashCode * 53) + UnsafeUtil.getInt(message, offset);
                    break;
                case 5:
                    hashCode = (hashCode * 53) + Internal.hashLong(UnsafeUtil.getLong(message, offset));
                    break;
                case 6:
                    hashCode = (hashCode * 53) + UnsafeUtil.getInt(message, offset);
                    break;
                case 7:
                    hashCode = (hashCode * 53) + Internal.hashBoolean(UnsafeUtil.getBoolean(message, offset));
                    break;
                case 8:
                    hashCode = (hashCode * 53) + ((String) UnsafeUtil.getObject(message, offset)).hashCode();
                    break;
                case 9:
                    int protoHash = 37;
                    Object submessage = UnsafeUtil.getObject(message, offset);
                    if (submessage != null) {
                        protoHash = submessage.hashCode();
                    }
                    hashCode = (hashCode * 53) + protoHash;
                    break;
                case 10:
                    hashCode = (hashCode * 53) + UnsafeUtil.getObject(message, offset).hashCode();
                    break;
                case 11:
                    hashCode = (hashCode * 53) + UnsafeUtil.getInt(message, offset);
                    break;
                case 12:
                    hashCode = (hashCode * 53) + UnsafeUtil.getInt(message, offset);
                    break;
                case 13:
                    hashCode = (hashCode * 53) + UnsafeUtil.getInt(message, offset);
                    break;
                case 14:
                    hashCode = (hashCode * 53) + Internal.hashLong(UnsafeUtil.getLong(message, offset));
                    break;
                case 15:
                    hashCode = (hashCode * 53) + UnsafeUtil.getInt(message, offset);
                    break;
                case 16:
                    hashCode = (hashCode * 53) + Internal.hashLong(UnsafeUtil.getLong(message, offset));
                    break;
                case 17:
                    int protoHash2 = 37;
                    Object submessage2 = UnsafeUtil.getObject(message, offset);
                    if (submessage2 != null) {
                        protoHash2 = submessage2.hashCode();
                    }
                    hashCode = (hashCode * 53) + protoHash2;
                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                    hashCode = (hashCode * 53) + UnsafeUtil.getObject(message, offset).hashCode();
                    break;
                case 50:
                    hashCode = (hashCode * 53) + UnsafeUtil.getObject(message, offset).hashCode();
                    break;
                case 51:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + Internal.hashLong(Double.doubleToLongBits(oneofDoubleAt(message, offset)));
                        break;
                    }
                case 52:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + Float.floatToIntBits(oneofFloatAt(message, offset));
                        break;
                    }
                case 53:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + Internal.hashLong(oneofLongAt(message, offset));
                        break;
                    }
                case 54:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + Internal.hashLong(oneofLongAt(message, offset));
                        break;
                    }
                case 55:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + oneofIntAt(message, offset);
                        break;
                    }
                case 56:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + Internal.hashLong(oneofLongAt(message, offset));
                        break;
                    }
                case 57:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + oneofIntAt(message, offset);
                        break;
                    }
                case 58:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + Internal.hashBoolean(oneofBooleanAt(message, offset));
                        break;
                    }
                case 59:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + ((String) UnsafeUtil.getObject(message, offset)).hashCode();
                        break;
                    }
                case 60:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + UnsafeUtil.getObject(message, offset).hashCode();
                        break;
                    }
                case 61:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + UnsafeUtil.getObject(message, offset).hashCode();
                        break;
                    }
                case 62:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + oneofIntAt(message, offset);
                        break;
                    }
                case 63:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + oneofIntAt(message, offset);
                        break;
                    }
                case 64:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + oneofIntAt(message, offset);
                        break;
                    }
                case 65:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + Internal.hashLong(oneofLongAt(message, offset));
                        break;
                    }
                case 66:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + oneofIntAt(message, offset);
                        break;
                    }
                case 67:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + Internal.hashLong(oneofLongAt(message, offset));
                        break;
                    }
                case 68:
                    if (!isOneofPresent(message, entryNumber, pos)) {
                        break;
                    } else {
                        hashCode = (hashCode * 53) + UnsafeUtil.getObject(message, offset).hashCode();
                        break;
                    }
            }
        }
        int hashCode2 = (hashCode * 53) + this.unknownFieldSchema.getFromMessage(message).hashCode();
        if (this.hasExtensions) {
            return (hashCode2 * 53) + this.extensionSchema.getExtensions(message).hashCode();
        }
        return hashCode2;
    }

    public void mergeFrom(T message, T other) {
        if (other == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < this.buffer.length; i += 3) {
            mergeSingleField(message, other, i);
        }
        if (!this.proto3) {
            SchemaUtil.mergeUnknownFields(this.unknownFieldSchema, message, other);
            if (this.hasExtensions) {
                SchemaUtil.mergeExtensions(this.extensionSchema, message, other);
            }
        }
    }

    private void mergeSingleField(T message, T other, int pos) {
        int typeAndOffset = typeAndOffsetAt(pos);
        long offset = offset(typeAndOffset);
        int number = numberAt(pos);
        switch (type(typeAndOffset)) {
            case 0:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putDouble(message, offset, UnsafeUtil.getDouble(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 1:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putFloat(message, offset, UnsafeUtil.getFloat(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 2:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putLong(message, offset, UnsafeUtil.getLong(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 3:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putLong(message, offset, UnsafeUtil.getLong(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 4:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putInt(message, offset, UnsafeUtil.getInt(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 5:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putLong(message, offset, UnsafeUtil.getLong(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 6:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putInt(message, offset, UnsafeUtil.getInt(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 7:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putBoolean(message, offset, UnsafeUtil.getBoolean(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 8:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putObject(message, offset, UnsafeUtil.getObject(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 9:
                mergeMessage(message, other, pos);
                return;
            case 10:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putObject(message, offset, UnsafeUtil.getObject(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 11:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putInt(message, offset, UnsafeUtil.getInt(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 12:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putInt(message, offset, UnsafeUtil.getInt(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 13:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putInt(message, offset, UnsafeUtil.getInt(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 14:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putLong(message, offset, UnsafeUtil.getLong(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 15:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putInt(message, offset, UnsafeUtil.getInt(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 16:
                if (isFieldPresent(other, pos)) {
                    UnsafeUtil.putLong(message, offset, UnsafeUtil.getLong(other, offset));
                    setFieldPresent(message, pos);
                    return;
                }
                return;
            case 17:
                mergeMessage(message, other, pos);
                return;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
                this.listFieldSchema.mergeListsAt(message, other, offset);
                return;
            case 50:
                SchemaUtil.mergeMap(this.mapFieldSchema, message, other, offset);
                return;
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
                if (isOneofPresent(other, number, pos)) {
                    UnsafeUtil.putObject(message, offset, UnsafeUtil.getObject(other, offset));
                    setOneofPresent(message, number, pos);
                    return;
                }
                return;
            case 60:
                mergeOneofMessage(message, other, pos);
                return;
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
                if (isOneofPresent(other, number, pos)) {
                    UnsafeUtil.putObject(message, offset, UnsafeUtil.getObject(other, offset));
                    setOneofPresent(message, number, pos);
                    return;
                }
                return;
            case 68:
                mergeOneofMessage(message, other, pos);
                return;
            default:
                return;
        }
    }

    private void mergeMessage(T message, T other, int pos) {
        long offset = offset(typeAndOffsetAt(pos));
        if (isFieldPresent(other, pos)) {
            Object mine = UnsafeUtil.getObject(message, offset);
            Object theirs = UnsafeUtil.getObject(other, offset);
            if (mine != null && theirs != null) {
                UnsafeUtil.putObject(message, offset, Internal.mergeMessage(mine, theirs));
                setFieldPresent(message, pos);
            } else if (theirs != null) {
                UnsafeUtil.putObject(message, offset, theirs);
                setFieldPresent(message, pos);
            }
        }
    }

    private void mergeOneofMessage(T message, T other, int pos) {
        int typeAndOffset = typeAndOffsetAt(pos);
        int number = numberAt(pos);
        long offset = offset(typeAndOffset);
        if (isOneofPresent(other, number, pos)) {
            Object mine = UnsafeUtil.getObject(message, offset);
            Object theirs = UnsafeUtil.getObject(other, offset);
            if (mine != null && theirs != null) {
                UnsafeUtil.putObject(message, offset, Internal.mergeMessage(mine, theirs));
                setOneofPresent(message, number, pos);
            } else if (theirs != null) {
                UnsafeUtil.putObject(message, offset, theirs);
                setOneofPresent(message, number, pos);
            }
        }
    }

    public int getSerializedSize(T message) {
        return this.proto3 ? getSerializedSizeProto3(message) : getSerializedSizeProto2(message);
    }

    private int getSerializedSizeProto2(T message) {
        int size = 0;
        Unsafe unsafe = UNSAFE;
        int currentPresenceFieldOffset = -1;
        int currentPresenceField = 0;
        for (int i = 0; i < this.buffer.length; i += 3) {
            int typeAndOffset = typeAndOffsetAt(i);
            int number = numberAt(i);
            int fieldType = type(typeAndOffset);
            int presenceMaskAndOffset = 0;
            int presenceMask = 0;
            if (fieldType <= 17) {
                presenceMaskAndOffset = this.buffer[i + 2];
                int presenceFieldOffset = presenceMaskAndOffset & OFFSET_MASK;
                presenceMask = 1 << (presenceMaskAndOffset >>> 20);
                if (presenceFieldOffset != currentPresenceFieldOffset) {
                    currentPresenceFieldOffset = presenceFieldOffset;
                    currentPresenceField = unsafe.getInt(message, (long) presenceFieldOffset);
                }
            } else if (this.useCachedSizeField && fieldType >= FieldType.DOUBLE_LIST_PACKED.id() && fieldType <= FieldType.SINT64_LIST_PACKED.id()) {
                presenceMaskAndOffset = this.buffer[i + 2] & OFFSET_MASK;
            }
            long offset = offset(typeAndOffset);
            switch (fieldType) {
                case 0:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeDoubleSize(number, 0.0d);
                        break;
                    }
                case 1:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeFloatSize(number, 0.0f);
                        break;
                    }
                case 2:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeInt64Size(number, unsafe.getLong(message, offset));
                        break;
                    }
                case 3:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeUInt64Size(number, unsafe.getLong(message, offset));
                        break;
                    }
                case 4:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeInt32Size(number, unsafe.getInt(message, offset));
                        break;
                    }
                case 5:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeFixed64Size(number, 0);
                        break;
                    }
                case 6:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeFixed32Size(number, 0);
                        break;
                    }
                case 7:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeBoolSize(number, true);
                        break;
                    }
                case 8:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        Object value = unsafe.getObject(message, offset);
                        if (!(value instanceof ByteString)) {
                            size += CodedOutputStream.computeStringSize(number, (String) value);
                            break;
                        } else {
                            size += CodedOutputStream.computeBytesSize(number, (ByteString) value);
                            break;
                        }
                    }
                case 9:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += SchemaUtil.computeSizeMessage(number, unsafe.getObject(message, offset), getMessageFieldSchema(i));
                        break;
                    }
                case 10:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeBytesSize(number, (ByteString) unsafe.getObject(message, offset));
                        break;
                    }
                case 11:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeUInt32Size(number, unsafe.getInt(message, offset));
                        break;
                    }
                case 12:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeEnumSize(number, unsafe.getInt(message, offset));
                        break;
                    }
                case 13:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSFixed32Size(number, 0);
                        break;
                    }
                case 14:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSFixed64Size(number, 0);
                        break;
                    }
                case 15:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSInt32Size(number, unsafe.getInt(message, offset));
                        break;
                    }
                case 16:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSInt64Size(number, unsafe.getLong(message, offset));
                        break;
                    }
                case 17:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        size += CodedOutputStream.computeGroupSize(number, (MessageLite) unsafe.getObject(message, offset), getMessageFieldSchema(i));
                        break;
                    }
                case 18:
                    size += SchemaUtil.computeSizeFixed64List(number, (List) unsafe.getObject(message, offset), false);
                    break;
                case 19:
                    size += SchemaUtil.computeSizeFixed32List(number, (List) unsafe.getObject(message, offset), false);
                    break;
                case 20:
                    size += SchemaUtil.computeSizeInt64List(number, (List) unsafe.getObject(message, offset), false);
                    break;
                case 21:
                    size += SchemaUtil.computeSizeUInt64List(number, (List) unsafe.getObject(message, offset), false);
                    break;
                case 22:
                    size += SchemaUtil.computeSizeInt32List(number, (List) unsafe.getObject(message, offset), false);
                    break;
                case 23:
                    size += SchemaUtil.computeSizeFixed64List(number, (List) unsafe.getObject(message, offset), false);
                    break;
                case 24:
                    size += SchemaUtil.computeSizeFixed32List(number, (List) unsafe.getObject(message, offset), false);
                    break;
                case 25:
                    size += SchemaUtil.computeSizeBoolList(number, (List) unsafe.getObject(message, offset), false);
                    break;
                case 26:
                    size += SchemaUtil.computeSizeStringList(number, (List) unsafe.getObject(message, offset));
                    break;
                case 27:
                    size += SchemaUtil.computeSizeMessageList(number, (List) unsafe.getObject(message, offset), getMessageFieldSchema(i));
                    break;
                case 28:
                    size += SchemaUtil.computeSizeByteStringList(number, (List) unsafe.getObject(message, offset));
                    break;
                case 29:
                    size += SchemaUtil.computeSizeUInt32List(number, (List) unsafe.getObject(message, offset), false);
                    break;
                case 30:
                    size += SchemaUtil.computeSizeEnumList(number, (List) unsafe.getObject(message, offset), false);
                    break;
                case 31:
                    size += SchemaUtil.computeSizeFixed32List(number, (List) unsafe.getObject(message, offset), false);
                    break;
                case 32:
                    size += SchemaUtil.computeSizeFixed64List(number, (List) unsafe.getObject(message, offset), false);
                    break;
                case 33:
                    size += SchemaUtil.computeSizeSInt32List(number, (List) unsafe.getObject(message, offset), false);
                    break;
                case 34:
                    size += SchemaUtil.computeSizeSInt64List(number, (List) unsafe.getObject(message, offset), false);
                    break;
                case 35:
                    int fieldSize = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) presenceMaskAndOffset, fieldSize);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize) + fieldSize;
                        break;
                    } else {
                        break;
                    }
                case 36:
                    int fieldSize2 = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize2 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) presenceMaskAndOffset, fieldSize2);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize2) + fieldSize2;
                        break;
                    } else {
                        break;
                    }
                case 37:
                    int fieldSize3 = SchemaUtil.computeSizeInt64ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize3 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) presenceMaskAndOffset, fieldSize3);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize3) + fieldSize3;
                        break;
                    } else {
                        break;
                    }
                case 38:
                    int fieldSize4 = SchemaUtil.computeSizeUInt64ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize4 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) presenceMaskAndOffset, fieldSize4);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize4) + fieldSize4;
                        break;
                    } else {
                        break;
                    }
                case 39:
                    int fieldSize5 = SchemaUtil.computeSizeInt32ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize5 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) presenceMaskAndOffset, fieldSize5);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize5) + fieldSize5;
                        break;
                    } else {
                        break;
                    }
                case 40:
                    int fieldSize6 = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize6 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) presenceMaskAndOffset, fieldSize6);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize6) + fieldSize6;
                        break;
                    } else {
                        break;
                    }
                case 41:
                    int fieldSize7 = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize7 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) presenceMaskAndOffset, fieldSize7);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize7) + fieldSize7;
                        break;
                    } else {
                        break;
                    }
                case 42:
                    int fieldSize8 = SchemaUtil.computeSizeBoolListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize8 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) presenceMaskAndOffset, fieldSize8);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize8) + fieldSize8;
                        break;
                    } else {
                        break;
                    }
                case 43:
                    int fieldSize9 = SchemaUtil.computeSizeUInt32ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize9 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) presenceMaskAndOffset, fieldSize9);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize9) + fieldSize9;
                        break;
                    } else {
                        break;
                    }
                case 44:
                    int fieldSize10 = SchemaUtil.computeSizeEnumListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize10 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) presenceMaskAndOffset, fieldSize10);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize10) + fieldSize10;
                        break;
                    } else {
                        break;
                    }
                case 45:
                    int fieldSize11 = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize11 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) presenceMaskAndOffset, fieldSize11);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize11) + fieldSize11;
                        break;
                    } else {
                        break;
                    }
                case 46:
                    int fieldSize12 = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize12 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) presenceMaskAndOffset, fieldSize12);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize12) + fieldSize12;
                        break;
                    } else {
                        break;
                    }
                case 47:
                    int fieldSize13 = SchemaUtil.computeSizeSInt32ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize13 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) presenceMaskAndOffset, fieldSize13);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize13) + fieldSize13;
                        break;
                    } else {
                        break;
                    }
                case 48:
                    int fieldSize14 = SchemaUtil.computeSizeSInt64ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize14 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) presenceMaskAndOffset, fieldSize14);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize14) + fieldSize14;
                        break;
                    } else {
                        break;
                    }
                case 49:
                    size += SchemaUtil.computeSizeGroupList(number, (List) unsafe.getObject(message, offset), getMessageFieldSchema(i));
                    break;
                case 50:
                    size += this.mapFieldSchema.getSerializedSize(number, unsafe.getObject(message, offset), getMapFieldDefaultEntry(i));
                    break;
                case 51:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeDoubleSize(number, 0.0d);
                        break;
                    }
                case 52:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeFloatSize(number, 0.0f);
                        break;
                    }
                case 53:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeInt64Size(number, oneofLongAt(message, offset));
                        break;
                    }
                case 54:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeUInt64Size(number, oneofLongAt(message, offset));
                        break;
                    }
                case 55:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeInt32Size(number, oneofIntAt(message, offset));
                        break;
                    }
                case 56:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeFixed64Size(number, 0);
                        break;
                    }
                case 57:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeFixed32Size(number, 0);
                        break;
                    }
                case 58:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeBoolSize(number, true);
                        break;
                    }
                case 59:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        Object value2 = unsafe.getObject(message, offset);
                        if (!(value2 instanceof ByteString)) {
                            size += CodedOutputStream.computeStringSize(number, (String) value2);
                            break;
                        } else {
                            size += CodedOutputStream.computeBytesSize(number, (ByteString) value2);
                            break;
                        }
                    }
                case 60:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += SchemaUtil.computeSizeMessage(number, unsafe.getObject(message, offset), getMessageFieldSchema(i));
                        break;
                    }
                case 61:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeBytesSize(number, (ByteString) unsafe.getObject(message, offset));
                        break;
                    }
                case 62:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeUInt32Size(number, oneofIntAt(message, offset));
                        break;
                    }
                case 63:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeEnumSize(number, oneofIntAt(message, offset));
                        break;
                    }
                case 64:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSFixed32Size(number, 0);
                        break;
                    }
                case 65:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSFixed64Size(number, 0);
                        break;
                    }
                case 66:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSInt32Size(number, oneofIntAt(message, offset));
                        break;
                    }
                case 67:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSInt64Size(number, oneofLongAt(message, offset));
                        break;
                    }
                case 68:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeGroupSize(number, (MessageLite) unsafe.getObject(message, offset), getMessageFieldSchema(i));
                        break;
                    }
            }
        }
        int size2 = size + getUnknownFieldsSerializedSize(this.unknownFieldSchema, message);
        if (this.hasExtensions) {
            return size2 + this.extensionSchema.getExtensions(message).getSerializedSize();
        }
        return size2;
    }

    private int getSerializedSizeProto3(T message) {
        Unsafe unsafe = UNSAFE;
        int size = 0;
        for (int i = 0; i < this.buffer.length; i += 3) {
            int typeAndOffset = typeAndOffsetAt(i);
            int fieldType = type(typeAndOffset);
            int number = numberAt(i);
            long offset = offset(typeAndOffset);
            int cachedSizeOffset = (fieldType < FieldType.DOUBLE_LIST_PACKED.id() || fieldType > FieldType.SINT64_LIST_PACKED.id()) ? 0 : this.buffer[i + 2] & OFFSET_MASK;
            switch (fieldType) {
                case 0:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeDoubleSize(number, 0.0d);
                        break;
                    }
                case 1:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeFloatSize(number, 0.0f);
                        break;
                    }
                case 2:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeInt64Size(number, UnsafeUtil.getLong(message, offset));
                        break;
                    }
                case 3:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeUInt64Size(number, UnsafeUtil.getLong(message, offset));
                        break;
                    }
                case 4:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeInt32Size(number, UnsafeUtil.getInt(message, offset));
                        break;
                    }
                case 5:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeFixed64Size(number, 0);
                        break;
                    }
                case 6:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeFixed32Size(number, 0);
                        break;
                    }
                case 7:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeBoolSize(number, true);
                        break;
                    }
                case 8:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        Object value = UnsafeUtil.getObject(message, offset);
                        if (!(value instanceof ByteString)) {
                            size += CodedOutputStream.computeStringSize(number, (String) value);
                            break;
                        } else {
                            size += CodedOutputStream.computeBytesSize(number, (ByteString) value);
                            break;
                        }
                    }
                case 9:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += SchemaUtil.computeSizeMessage(number, UnsafeUtil.getObject(message, offset), getMessageFieldSchema(i));
                        break;
                    }
                case 10:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeBytesSize(number, (ByteString) UnsafeUtil.getObject(message, offset));
                        break;
                    }
                case 11:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeUInt32Size(number, UnsafeUtil.getInt(message, offset));
                        break;
                    }
                case 12:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeEnumSize(number, UnsafeUtil.getInt(message, offset));
                        break;
                    }
                case 13:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSFixed32Size(number, 0);
                        break;
                    }
                case 14:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSFixed64Size(number, 0);
                        break;
                    }
                case 15:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSInt32Size(number, UnsafeUtil.getInt(message, offset));
                        break;
                    }
                case 16:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSInt64Size(number, UnsafeUtil.getLong(message, offset));
                        break;
                    }
                case 17:
                    if (!isFieldPresent(message, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeGroupSize(number, (MessageLite) UnsafeUtil.getObject(message, offset), getMessageFieldSchema(i));
                        break;
                    }
                case 18:
                    size += SchemaUtil.computeSizeFixed64List(number, listAt(message, offset), false);
                    break;
                case 19:
                    size += SchemaUtil.computeSizeFixed32List(number, listAt(message, offset), false);
                    break;
                case 20:
                    size += SchemaUtil.computeSizeInt64List(number, listAt(message, offset), false);
                    break;
                case 21:
                    size += SchemaUtil.computeSizeUInt64List(number, listAt(message, offset), false);
                    break;
                case 22:
                    size += SchemaUtil.computeSizeInt32List(number, listAt(message, offset), false);
                    break;
                case 23:
                    size += SchemaUtil.computeSizeFixed64List(number, listAt(message, offset), false);
                    break;
                case 24:
                    size += SchemaUtil.computeSizeFixed32List(number, listAt(message, offset), false);
                    break;
                case 25:
                    size += SchemaUtil.computeSizeBoolList(number, listAt(message, offset), false);
                    break;
                case 26:
                    size += SchemaUtil.computeSizeStringList(number, listAt(message, offset));
                    break;
                case 27:
                    size += SchemaUtil.computeSizeMessageList(number, listAt(message, offset), getMessageFieldSchema(i));
                    break;
                case 28:
                    size += SchemaUtil.computeSizeByteStringList(number, listAt(message, offset));
                    break;
                case 29:
                    size += SchemaUtil.computeSizeUInt32List(number, listAt(message, offset), false);
                    break;
                case 30:
                    size += SchemaUtil.computeSizeEnumList(number, listAt(message, offset), false);
                    break;
                case 31:
                    size += SchemaUtil.computeSizeFixed32List(number, listAt(message, offset), false);
                    break;
                case 32:
                    size += SchemaUtil.computeSizeFixed64List(number, listAt(message, offset), false);
                    break;
                case 33:
                    size += SchemaUtil.computeSizeSInt32List(number, listAt(message, offset), false);
                    break;
                case 34:
                    size += SchemaUtil.computeSizeSInt64List(number, listAt(message, offset), false);
                    break;
                case 35:
                    int fieldSize = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) cachedSizeOffset, fieldSize);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize) + fieldSize;
                        break;
                    } else {
                        break;
                    }
                case 36:
                    int fieldSize2 = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize2 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) cachedSizeOffset, fieldSize2);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize2) + fieldSize2;
                        break;
                    } else {
                        break;
                    }
                case 37:
                    int fieldSize3 = SchemaUtil.computeSizeInt64ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize3 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) cachedSizeOffset, fieldSize3);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize3) + fieldSize3;
                        break;
                    } else {
                        break;
                    }
                case 38:
                    int fieldSize4 = SchemaUtil.computeSizeUInt64ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize4 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) cachedSizeOffset, fieldSize4);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize4) + fieldSize4;
                        break;
                    } else {
                        break;
                    }
                case 39:
                    int fieldSize5 = SchemaUtil.computeSizeInt32ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize5 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) cachedSizeOffset, fieldSize5);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize5) + fieldSize5;
                        break;
                    } else {
                        break;
                    }
                case 40:
                    int fieldSize6 = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize6 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) cachedSizeOffset, fieldSize6);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize6) + fieldSize6;
                        break;
                    } else {
                        break;
                    }
                case 41:
                    int fieldSize7 = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize7 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) cachedSizeOffset, fieldSize7);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize7) + fieldSize7;
                        break;
                    } else {
                        break;
                    }
                case 42:
                    int fieldSize8 = SchemaUtil.computeSizeBoolListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize8 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) cachedSizeOffset, fieldSize8);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize8) + fieldSize8;
                        break;
                    } else {
                        break;
                    }
                case 43:
                    int fieldSize9 = SchemaUtil.computeSizeUInt32ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize9 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) cachedSizeOffset, fieldSize9);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize9) + fieldSize9;
                        break;
                    } else {
                        break;
                    }
                case 44:
                    int fieldSize10 = SchemaUtil.computeSizeEnumListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize10 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) cachedSizeOffset, fieldSize10);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize10) + fieldSize10;
                        break;
                    } else {
                        break;
                    }
                case 45:
                    int fieldSize11 = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize11 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) cachedSizeOffset, fieldSize11);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize11) + fieldSize11;
                        break;
                    } else {
                        break;
                    }
                case 46:
                    int fieldSize12 = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize12 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) cachedSizeOffset, fieldSize12);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize12) + fieldSize12;
                        break;
                    } else {
                        break;
                    }
                case 47:
                    int fieldSize13 = SchemaUtil.computeSizeSInt32ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize13 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) cachedSizeOffset, fieldSize13);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize13) + fieldSize13;
                        break;
                    } else {
                        break;
                    }
                case 48:
                    int fieldSize14 = SchemaUtil.computeSizeSInt64ListNoTag((List) unsafe.getObject(message, offset));
                    if (fieldSize14 > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(message, (long) cachedSizeOffset, fieldSize14);
                        }
                        size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeUInt32SizeNoTag(fieldSize14) + fieldSize14;
                        break;
                    } else {
                        break;
                    }
                case 49:
                    size += SchemaUtil.computeSizeGroupList(number, listAt(message, offset), getMessageFieldSchema(i));
                    break;
                case 50:
                    size += this.mapFieldSchema.getSerializedSize(number, UnsafeUtil.getObject(message, offset), getMapFieldDefaultEntry(i));
                    break;
                case 51:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeDoubleSize(number, 0.0d);
                        break;
                    }
                case 52:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeFloatSize(number, 0.0f);
                        break;
                    }
                case 53:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeInt64Size(number, oneofLongAt(message, offset));
                        break;
                    }
                case 54:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeUInt64Size(number, oneofLongAt(message, offset));
                        break;
                    }
                case 55:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeInt32Size(number, oneofIntAt(message, offset));
                        break;
                    }
                case 56:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeFixed64Size(number, 0);
                        break;
                    }
                case 57:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeFixed32Size(number, 0);
                        break;
                    }
                case 58:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeBoolSize(number, true);
                        break;
                    }
                case 59:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        Object value2 = UnsafeUtil.getObject(message, offset);
                        if (!(value2 instanceof ByteString)) {
                            size += CodedOutputStream.computeStringSize(number, (String) value2);
                            break;
                        } else {
                            size += CodedOutputStream.computeBytesSize(number, (ByteString) value2);
                            break;
                        }
                    }
                case 60:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += SchemaUtil.computeSizeMessage(number, UnsafeUtil.getObject(message, offset), getMessageFieldSchema(i));
                        break;
                    }
                case 61:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeBytesSize(number, (ByteString) UnsafeUtil.getObject(message, offset));
                        break;
                    }
                case 62:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeUInt32Size(number, oneofIntAt(message, offset));
                        break;
                    }
                case 63:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeEnumSize(number, oneofIntAt(message, offset));
                        break;
                    }
                case 64:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSFixed32Size(number, 0);
                        break;
                    }
                case 65:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSFixed64Size(number, 0);
                        break;
                    }
                case 66:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSInt32Size(number, oneofIntAt(message, offset));
                        break;
                    }
                case 67:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeSInt64Size(number, oneofLongAt(message, offset));
                        break;
                    }
                case 68:
                    if (!isOneofPresent(message, number, i)) {
                        break;
                    } else {
                        size += CodedOutputStream.computeGroupSize(number, (MessageLite) UnsafeUtil.getObject(message, offset), getMessageFieldSchema(i));
                        break;
                    }
            }
        }
        return size + getUnknownFieldsSerializedSize(this.unknownFieldSchema, message);
    }

    private <UT, UB> int getUnknownFieldsSerializedSize(UnknownFieldSchema<UT, UB> schema, T message) {
        return schema.getSerializedSize(schema.getFromMessage(message));
    }

    private static List<?> listAt(Object message, long offset) {
        return (List) UnsafeUtil.getObject(message, offset);
    }

    public void writeTo(T message, Writer writer) throws IOException {
        if (writer.fieldOrder() == Writer.FieldOrder.DESCENDING) {
            writeFieldsInDescendingOrder(message, writer);
        } else if (this.proto3) {
            writeFieldsInAscendingOrderProto3(message, writer);
        } else {
            writeFieldsInAscendingOrderProto2(message, writer);
        }
    }

    private void writeFieldsInAscendingOrderProto2(T message, Writer writer) throws IOException {
        Map.Entry nextExtension;
        Iterator<? extends Map.Entry<?, ?>> extensionIterator = null;
        Map.Entry nextExtension2 = null;
        if (this.hasExtensions) {
            FieldSet<?> extensions = this.extensionSchema.getExtensions(message);
            if (!extensions.isEmpty()) {
                extensionIterator = extensions.iterator();
                nextExtension2 = extensionIterator.next();
            }
        }
        int currentPresenceFieldOffset = -1;
        int currentPresenceField = 0;
        int bufferLength = this.buffer.length;
        Unsafe unsafe = UNSAFE;
        for (int pos = 0; pos < bufferLength; pos += 3) {
            int typeAndOffset = typeAndOffsetAt(pos);
            int number = numberAt(pos);
            int fieldType = type(typeAndOffset);
            int presenceMask = 0;
            if (!this.proto3 && fieldType <= 17) {
                int presenceMaskAndOffset = this.buffer[pos + 2];
                int presenceFieldOffset = presenceMaskAndOffset & OFFSET_MASK;
                if (presenceFieldOffset != currentPresenceFieldOffset) {
                    currentPresenceFieldOffset = presenceFieldOffset;
                    currentPresenceField = unsafe.getInt(message, (long) presenceFieldOffset);
                }
                presenceMask = 1 << (presenceMaskAndOffset >>> 20);
            }
            while (nextExtension != null && this.extensionSchema.extensionNumber(nextExtension) <= number) {
                this.extensionSchema.serializeExtension(writer, nextExtension);
                nextExtension = extensionIterator.hasNext() ? (Map.Entry) extensionIterator.next() : null;
            }
            long offset = offset(typeAndOffset);
            switch (fieldType) {
                case 0:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeDouble(number, doubleAt(message, offset));
                        break;
                    }
                case 1:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeFloat(number, floatAt(message, offset));
                        break;
                    }
                case 2:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeInt64(number, unsafe.getLong(message, offset));
                        break;
                    }
                case 3:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeUInt64(number, unsafe.getLong(message, offset));
                        break;
                    }
                case 4:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeInt32(number, unsafe.getInt(message, offset));
                        break;
                    }
                case 5:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeFixed64(number, unsafe.getLong(message, offset));
                        break;
                    }
                case 6:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeFixed32(number, unsafe.getInt(message, offset));
                        break;
                    }
                case 7:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeBool(number, booleanAt(message, offset));
                        break;
                    }
                case 8:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writeString(number, unsafe.getObject(message, offset), writer);
                        break;
                    }
                case 9:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeMessage(number, unsafe.getObject(message, offset), getMessageFieldSchema(pos));
                        break;
                    }
                case 10:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeBytes(number, (ByteString) unsafe.getObject(message, offset));
                        break;
                    }
                case 11:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeUInt32(number, unsafe.getInt(message, offset));
                        break;
                    }
                case 12:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeEnum(number, unsafe.getInt(message, offset));
                        break;
                    }
                case 13:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeSFixed32(number, unsafe.getInt(message, offset));
                        break;
                    }
                case 14:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeSFixed64(number, unsafe.getLong(message, offset));
                        break;
                    }
                case 15:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeSInt32(number, unsafe.getInt(message, offset));
                        break;
                    }
                case 16:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeSInt64(number, unsafe.getLong(message, offset));
                        break;
                    }
                case 17:
                    if ((currentPresenceField & presenceMask) == 0) {
                        break;
                    } else {
                        writer.writeGroup(number, unsafe.getObject(message, offset), getMessageFieldSchema(pos));
                        break;
                    }
                case 18:
                    SchemaUtil.writeDoubleList(numberAt(pos), (List) unsafe.getObject(message, offset), writer, false);
                    break;
                case 19:
                    SchemaUtil.writeFloatList(numberAt(pos), (List) unsafe.getObject(message, offset), writer, false);
                    break;
                case 20:
                    SchemaUtil.writeInt64List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, false);
                    break;
                case 21:
                    SchemaUtil.writeUInt64List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, false);
                    break;
                case 22:
                    SchemaUtil.writeInt32List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, false);
                    break;
                case 23:
                    SchemaUtil.writeFixed64List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, false);
                    break;
                case 24:
                    SchemaUtil.writeFixed32List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, false);
                    break;
                case 25:
                    SchemaUtil.writeBoolList(numberAt(pos), (List) unsafe.getObject(message, offset), writer, false);
                    break;
                case 26:
                    SchemaUtil.writeStringList(numberAt(pos), (List) unsafe.getObject(message, offset), writer);
                    break;
                case 27:
                    SchemaUtil.writeMessageList(numberAt(pos), (List) unsafe.getObject(message, offset), writer, getMessageFieldSchema(pos));
                    break;
                case 28:
                    SchemaUtil.writeBytesList(numberAt(pos), (List) unsafe.getObject(message, offset), writer);
                    break;
                case 29:
                    SchemaUtil.writeUInt32List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, false);
                    break;
                case 30:
                    SchemaUtil.writeEnumList(numberAt(pos), (List) unsafe.getObject(message, offset), writer, false);
                    break;
                case 31:
                    SchemaUtil.writeSFixed32List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, false);
                    break;
                case 32:
                    SchemaUtil.writeSFixed64List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, false);
                    break;
                case 33:
                    SchemaUtil.writeSInt32List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, false);
                    break;
                case 34:
                    SchemaUtil.writeSInt64List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, false);
                    break;
                case 35:
                    SchemaUtil.writeDoubleList(numberAt(pos), (List) unsafe.getObject(message, offset), writer, true);
                    break;
                case 36:
                    SchemaUtil.writeFloatList(numberAt(pos), (List) unsafe.getObject(message, offset), writer, true);
                    break;
                case 37:
                    SchemaUtil.writeInt64List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, true);
                    break;
                case 38:
                    SchemaUtil.writeUInt64List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, true);
                    break;
                case 39:
                    SchemaUtil.writeInt32List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, true);
                    break;
                case 40:
                    SchemaUtil.writeFixed64List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, true);
                    break;
                case 41:
                    SchemaUtil.writeFixed32List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, true);
                    break;
                case 42:
                    SchemaUtil.writeBoolList(numberAt(pos), (List) unsafe.getObject(message, offset), writer, true);
                    break;
                case 43:
                    SchemaUtil.writeUInt32List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, true);
                    break;
                case 44:
                    SchemaUtil.writeEnumList(numberAt(pos), (List) unsafe.getObject(message, offset), writer, true);
                    break;
                case 45:
                    SchemaUtil.writeSFixed32List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, true);
                    break;
                case 46:
                    SchemaUtil.writeSFixed64List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, true);
                    break;
                case 47:
                    SchemaUtil.writeSInt32List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, true);
                    break;
                case 48:
                    SchemaUtil.writeSInt64List(numberAt(pos), (List) unsafe.getObject(message, offset), writer, true);
                    break;
                case 49:
                    SchemaUtil.writeGroupList(numberAt(pos), (List) unsafe.getObject(message, offset), writer, getMessageFieldSchema(pos));
                    break;
                case 50:
                    writeMapHelper(writer, number, unsafe.getObject(message, offset), pos);
                    break;
                case 51:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeDouble(number, oneofDoubleAt(message, offset));
                        break;
                    }
                case 52:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeFloat(number, oneofFloatAt(message, offset));
                        break;
                    }
                case 53:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeInt64(number, oneofLongAt(message, offset));
                        break;
                    }
                case 54:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeUInt64(number, oneofLongAt(message, offset));
                        break;
                    }
                case 55:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeInt32(number, oneofIntAt(message, offset));
                        break;
                    }
                case 56:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeFixed64(number, oneofLongAt(message, offset));
                        break;
                    }
                case 57:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeFixed32(number, oneofIntAt(message, offset));
                        break;
                    }
                case 58:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeBool(number, oneofBooleanAt(message, offset));
                        break;
                    }
                case 59:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writeString(number, unsafe.getObject(message, offset), writer);
                        break;
                    }
                case 60:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeMessage(number, unsafe.getObject(message, offset), getMessageFieldSchema(pos));
                        break;
                    }
                case 61:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeBytes(number, (ByteString) unsafe.getObject(message, offset));
                        break;
                    }
                case 62:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeUInt32(number, oneofIntAt(message, offset));
                        break;
                    }
                case 63:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeEnum(number, oneofIntAt(message, offset));
                        break;
                    }
                case 64:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeSFixed32(number, oneofIntAt(message, offset));
                        break;
                    }
                case 65:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeSFixed64(number, oneofLongAt(message, offset));
                        break;
                    }
                case 66:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeSInt32(number, oneofIntAt(message, offset));
                        break;
                    }
                case 67:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeSInt64(number, oneofLongAt(message, offset));
                        break;
                    }
                case 68:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeGroup(number, unsafe.getObject(message, offset), getMessageFieldSchema(pos));
                        break;
                    }
            }
        }
        while (nextExtension != null) {
            this.extensionSchema.serializeExtension(writer, nextExtension);
            nextExtension = extensionIterator.hasNext() ? (Map.Entry) extensionIterator.next() : null;
        }
        writeUnknownInMessageTo(this.unknownFieldSchema, message, writer);
    }

    private void writeFieldsInAscendingOrderProto3(T message, Writer writer) throws IOException {
        Map.Entry nextExtension;
        Iterator<? extends Map.Entry<?, ?>> extensionIterator = null;
        Map.Entry nextExtension2 = null;
        if (this.hasExtensions) {
            FieldSet<?> extensions = this.extensionSchema.getExtensions(message);
            if (!extensions.isEmpty()) {
                extensionIterator = extensions.iterator();
                nextExtension2 = extensionIterator.next();
            }
        }
        int bufferLength = this.buffer.length;
        for (int pos = 0; pos < bufferLength; pos += 3) {
            int typeAndOffset = typeAndOffsetAt(pos);
            int number = numberAt(pos);
            while (nextExtension != null && this.extensionSchema.extensionNumber(nextExtension) <= number) {
                this.extensionSchema.serializeExtension(writer, nextExtension);
                nextExtension = extensionIterator.hasNext() ? (Map.Entry) extensionIterator.next() : null;
            }
            switch (type(typeAndOffset)) {
                case 0:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeDouble(number, doubleAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 1:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeFloat(number, floatAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 2:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeInt64(number, longAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 3:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeUInt64(number, longAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 4:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeInt32(number, intAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 5:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeFixed64(number, longAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 6:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeFixed32(number, intAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 7:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeBool(number, booleanAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 8:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writeString(number, UnsafeUtil.getObject(message, offset(typeAndOffset)), writer);
                        break;
                    }
                case 9:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeMessage(number, UnsafeUtil.getObject(message, offset(typeAndOffset)), getMessageFieldSchema(pos));
                        break;
                    }
                case 10:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeBytes(number, (ByteString) UnsafeUtil.getObject(message, offset(typeAndOffset)));
                        break;
                    }
                case 11:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeUInt32(number, intAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 12:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeEnum(number, intAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 13:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeSFixed32(number, intAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 14:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeSFixed64(number, longAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 15:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeSInt32(number, intAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 16:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeSInt64(number, longAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 17:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeGroup(number, UnsafeUtil.getObject(message, offset(typeAndOffset)), getMessageFieldSchema(pos));
                        break;
                    }
                case 18:
                    SchemaUtil.writeDoubleList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 19:
                    SchemaUtil.writeFloatList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 20:
                    SchemaUtil.writeInt64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 21:
                    SchemaUtil.writeUInt64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 22:
                    SchemaUtil.writeInt32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 23:
                    SchemaUtil.writeFixed64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 24:
                    SchemaUtil.writeFixed32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 25:
                    SchemaUtil.writeBoolList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 26:
                    SchemaUtil.writeStringList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer);
                    break;
                case 27:
                    SchemaUtil.writeMessageList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, getMessageFieldSchema(pos));
                    break;
                case 28:
                    SchemaUtil.writeBytesList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer);
                    break;
                case 29:
                    SchemaUtil.writeUInt32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 30:
                    SchemaUtil.writeEnumList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 31:
                    SchemaUtil.writeSFixed32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 32:
                    SchemaUtil.writeSFixed64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 33:
                    SchemaUtil.writeSInt32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 34:
                    SchemaUtil.writeSInt64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 35:
                    SchemaUtil.writeDoubleList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 36:
                    SchemaUtil.writeFloatList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 37:
                    SchemaUtil.writeInt64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 38:
                    SchemaUtil.writeUInt64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 39:
                    SchemaUtil.writeInt32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 40:
                    SchemaUtil.writeFixed64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 41:
                    SchemaUtil.writeFixed32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 42:
                    SchemaUtil.writeBoolList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 43:
                    SchemaUtil.writeUInt32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 44:
                    SchemaUtil.writeEnumList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 45:
                    SchemaUtil.writeSFixed32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 46:
                    SchemaUtil.writeSFixed64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 47:
                    SchemaUtil.writeSInt32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 48:
                    SchemaUtil.writeSInt64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 49:
                    SchemaUtil.writeGroupList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, getMessageFieldSchema(pos));
                    break;
                case 50:
                    writeMapHelper(writer, number, UnsafeUtil.getObject(message, offset(typeAndOffset)), pos);
                    break;
                case 51:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeDouble(number, oneofDoubleAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 52:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeFloat(number, oneofFloatAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 53:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeInt64(number, oneofLongAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 54:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeUInt64(number, oneofLongAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 55:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeInt32(number, oneofIntAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 56:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeFixed64(number, oneofLongAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 57:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeFixed32(number, oneofIntAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 58:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeBool(number, oneofBooleanAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 59:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writeString(number, UnsafeUtil.getObject(message, offset(typeAndOffset)), writer);
                        break;
                    }
                case 60:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeMessage(number, UnsafeUtil.getObject(message, offset(typeAndOffset)), getMessageFieldSchema(pos));
                        break;
                    }
                case 61:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeBytes(number, (ByteString) UnsafeUtil.getObject(message, offset(typeAndOffset)));
                        break;
                    }
                case 62:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeUInt32(number, oneofIntAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 63:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeEnum(number, oneofIntAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 64:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeSFixed32(number, oneofIntAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 65:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeSFixed64(number, oneofLongAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 66:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeSInt32(number, oneofIntAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 67:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeSInt64(number, oneofLongAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 68:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeGroup(number, UnsafeUtil.getObject(message, offset(typeAndOffset)), getMessageFieldSchema(pos));
                        break;
                    }
            }
        }
        while (nextExtension != null) {
            this.extensionSchema.serializeExtension(writer, nextExtension);
            nextExtension = extensionIterator.hasNext() ? (Map.Entry) extensionIterator.next() : null;
        }
        writeUnknownInMessageTo(this.unknownFieldSchema, message, writer);
    }

    private void writeFieldsInDescendingOrder(T message, Writer writer) throws IOException {
        Map.Entry nextExtension;
        writeUnknownInMessageTo(this.unknownFieldSchema, message, writer);
        Iterator<? extends Map.Entry<?, ?>> extensionIterator = null;
        Map.Entry nextExtension2 = null;
        if (this.hasExtensions) {
            FieldSet<?> extensions = this.extensionSchema.getExtensions(message);
            if (!extensions.isEmpty()) {
                extensionIterator = extensions.descendingIterator();
                nextExtension2 = extensionIterator.next();
            }
        }
        for (int pos = this.buffer.length - 3; pos >= 0; pos -= 3) {
            int typeAndOffset = typeAndOffsetAt(pos);
            int number = numberAt(pos);
            while (nextExtension != null && this.extensionSchema.extensionNumber(nextExtension) > number) {
                this.extensionSchema.serializeExtension(writer, nextExtension);
                nextExtension = extensionIterator.hasNext() ? (Map.Entry) extensionIterator.next() : null;
            }
            switch (type(typeAndOffset)) {
                case 0:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeDouble(number, doubleAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 1:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeFloat(number, floatAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 2:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeInt64(number, longAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 3:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeUInt64(number, longAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 4:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeInt32(number, intAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 5:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeFixed64(number, longAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 6:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeFixed32(number, intAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 7:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeBool(number, booleanAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 8:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writeString(number, UnsafeUtil.getObject(message, offset(typeAndOffset)), writer);
                        break;
                    }
                case 9:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeMessage(number, UnsafeUtil.getObject(message, offset(typeAndOffset)), getMessageFieldSchema(pos));
                        break;
                    }
                case 10:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeBytes(number, (ByteString) UnsafeUtil.getObject(message, offset(typeAndOffset)));
                        break;
                    }
                case 11:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeUInt32(number, intAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 12:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeEnum(number, intAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 13:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeSFixed32(number, intAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 14:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeSFixed64(number, longAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 15:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeSInt32(number, intAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 16:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeSInt64(number, longAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 17:
                    if (!isFieldPresent(message, pos)) {
                        break;
                    } else {
                        writer.writeGroup(number, UnsafeUtil.getObject(message, offset(typeAndOffset)), getMessageFieldSchema(pos));
                        break;
                    }
                case 18:
                    SchemaUtil.writeDoubleList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 19:
                    SchemaUtil.writeFloatList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 20:
                    SchemaUtil.writeInt64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 21:
                    SchemaUtil.writeUInt64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 22:
                    SchemaUtil.writeInt32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 23:
                    SchemaUtil.writeFixed64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 24:
                    SchemaUtil.writeFixed32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 25:
                    SchemaUtil.writeBoolList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 26:
                    SchemaUtil.writeStringList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer);
                    break;
                case 27:
                    SchemaUtil.writeMessageList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, getMessageFieldSchema(pos));
                    break;
                case 28:
                    SchemaUtil.writeBytesList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer);
                    break;
                case 29:
                    SchemaUtil.writeUInt32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 30:
                    SchemaUtil.writeEnumList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 31:
                    SchemaUtil.writeSFixed32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 32:
                    SchemaUtil.writeSFixed64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 33:
                    SchemaUtil.writeSInt32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 34:
                    SchemaUtil.writeSInt64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, false);
                    break;
                case 35:
                    SchemaUtil.writeDoubleList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 36:
                    SchemaUtil.writeFloatList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 37:
                    SchemaUtil.writeInt64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 38:
                    SchemaUtil.writeUInt64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 39:
                    SchemaUtil.writeInt32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 40:
                    SchemaUtil.writeFixed64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 41:
                    SchemaUtil.writeFixed32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 42:
                    SchemaUtil.writeBoolList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 43:
                    SchemaUtil.writeUInt32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 44:
                    SchemaUtil.writeEnumList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 45:
                    SchemaUtil.writeSFixed32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 46:
                    SchemaUtil.writeSFixed64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 47:
                    SchemaUtil.writeSInt32List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 48:
                    SchemaUtil.writeSInt64List(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, true);
                    break;
                case 49:
                    SchemaUtil.writeGroupList(numberAt(pos), (List) UnsafeUtil.getObject(message, offset(typeAndOffset)), writer, getMessageFieldSchema(pos));
                    break;
                case 50:
                    writeMapHelper(writer, number, UnsafeUtil.getObject(message, offset(typeAndOffset)), pos);
                    break;
                case 51:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeDouble(number, oneofDoubleAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 52:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeFloat(number, oneofFloatAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 53:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeInt64(number, oneofLongAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 54:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeUInt64(number, oneofLongAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 55:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeInt32(number, oneofIntAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 56:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeFixed64(number, oneofLongAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 57:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeFixed32(number, oneofIntAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 58:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeBool(number, oneofBooleanAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 59:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writeString(number, UnsafeUtil.getObject(message, offset(typeAndOffset)), writer);
                        break;
                    }
                case 60:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeMessage(number, UnsafeUtil.getObject(message, offset(typeAndOffset)), getMessageFieldSchema(pos));
                        break;
                    }
                case 61:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeBytes(number, (ByteString) UnsafeUtil.getObject(message, offset(typeAndOffset)));
                        break;
                    }
                case 62:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeUInt32(number, oneofIntAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 63:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeEnum(number, oneofIntAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 64:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeSFixed32(number, oneofIntAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 65:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeSFixed64(number, oneofLongAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 66:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeSInt32(number, oneofIntAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 67:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeSInt64(number, oneofLongAt(message, offset(typeAndOffset)));
                        break;
                    }
                case 68:
                    if (!isOneofPresent(message, number, pos)) {
                        break;
                    } else {
                        writer.writeGroup(number, UnsafeUtil.getObject(message, offset(typeAndOffset)), getMessageFieldSchema(pos));
                        break;
                    }
            }
        }
        while (nextExtension != null) {
            this.extensionSchema.serializeExtension(writer, nextExtension);
            nextExtension = extensionIterator.hasNext() ? (Map.Entry) extensionIterator.next() : null;
        }
    }

    private <K, V> void writeMapHelper(Writer writer, int number, Object mapField, int pos) throws IOException {
        if (mapField != null) {
            writer.writeMap(number, this.mapFieldSchema.forMapMetadata(getMapFieldDefaultEntry(pos)), this.mapFieldSchema.forMapData(mapField));
        }
    }

    private <UT, UB> void writeUnknownInMessageTo(UnknownFieldSchema<UT, UB> schema, T message, Writer writer) throws IOException {
        schema.writeTo(schema.getFromMessage(message), writer);
    }

    public void mergeFrom(T message, Reader reader, ExtensionRegistryLite extensionRegistry) throws IOException {
        if (extensionRegistry == null) {
            throw new NullPointerException();
        }
        mergeFromHelper(this.unknownFieldSchema, this.extensionSchema, message, reader, extensionRegistry);
    }

    private <UT, UB, ET extends FieldSet.FieldDescriptorLite<ET>> void mergeFromHelper(UnknownFieldSchema<UT, UB> unknownFieldSchema2, ExtensionSchema<ET> extensionSchema2, T message, Reader reader, ExtensionRegistryLite extensionRegistry) throws IOException {
        UB unknownFields;
        Object extension;
        FieldSet<ET> extensions = null;
        UB ub = null;
        while (true) {
            try {
                int number = reader.getFieldNumber();
                int pos = positionForFieldNumber(number);
                if (pos >= 0) {
                    int typeAndOffset = typeAndOffsetAt(pos);
                    try {
                        switch (type(typeAndOffset)) {
                            case 0:
                                UnsafeUtil.putDouble(message, offset(typeAndOffset), reader.readDouble());
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 1:
                                UnsafeUtil.putFloat(message, offset(typeAndOffset), reader.readFloat());
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 2:
                                UnsafeUtil.putLong(message, offset(typeAndOffset), reader.readInt64());
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 3:
                                UnsafeUtil.putLong(message, offset(typeAndOffset), reader.readUInt64());
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 4:
                                UnsafeUtil.putInt(message, offset(typeAndOffset), reader.readInt32());
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 5:
                                UnsafeUtil.putLong(message, offset(typeAndOffset), reader.readFixed64());
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 6:
                                UnsafeUtil.putInt(message, offset(typeAndOffset), reader.readFixed32());
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 7:
                                UnsafeUtil.putBoolean(message, offset(typeAndOffset), reader.readBool());
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 8:
                                readString(message, typeAndOffset, reader);
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 9:
                                if (isFieldPresent(message, pos)) {
                                    UnsafeUtil.putObject(message, offset(typeAndOffset), Internal.mergeMessage(UnsafeUtil.getObject(message, offset(typeAndOffset)), reader.readMessageBySchemaWithCheck(getMessageFieldSchema(pos), extensionRegistry)));
                                } else {
                                    UnsafeUtil.putObject(message, offset(typeAndOffset), reader.readMessageBySchemaWithCheck(getMessageFieldSchema(pos), extensionRegistry));
                                    setFieldPresent(message, pos);
                                }
                                unknownFields = ub;
                                break;
                            case 10:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), reader.readBytes());
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 11:
                                UnsafeUtil.putInt(message, offset(typeAndOffset), reader.readUInt32());
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 12:
                                int enumValue = reader.readEnum();
                                Internal.EnumVerifier enumVerifier = getEnumFieldVerifier(pos);
                                if (enumVerifier == null || enumVerifier.isInRange(enumValue)) {
                                    UnsafeUtil.putInt(message, offset(typeAndOffset), enumValue);
                                    setFieldPresent(message, pos);
                                } else {
                                    ub = SchemaUtil.storeUnknownEnum(number, enumValue, ub, unknownFieldSchema2);
                                }
                                unknownFields = ub;
                                break;
                            case 13:
                                UnsafeUtil.putInt(message, offset(typeAndOffset), reader.readSFixed32());
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 14:
                                UnsafeUtil.putLong(message, offset(typeAndOffset), reader.readSFixed64());
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 15:
                                UnsafeUtil.putInt(message, offset(typeAndOffset), reader.readSInt32());
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 16:
                                UnsafeUtil.putLong(message, offset(typeAndOffset), reader.readSInt64());
                                setFieldPresent(message, pos);
                                unknownFields = ub;
                                break;
                            case 17:
                                if (isFieldPresent(message, pos)) {
                                    UnsafeUtil.putObject(message, offset(typeAndOffset), Internal.mergeMessage(UnsafeUtil.getObject(message, offset(typeAndOffset)), reader.readGroupBySchemaWithCheck(getMessageFieldSchema(pos), extensionRegistry)));
                                } else {
                                    UnsafeUtil.putObject(message, offset(typeAndOffset), reader.readGroupBySchemaWithCheck(getMessageFieldSchema(pos), extensionRegistry));
                                    setFieldPresent(message, pos);
                                }
                                unknownFields = ub;
                                break;
                            case 18:
                                reader.readDoubleList(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 19:
                                reader.readFloatList(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 20:
                                reader.readInt64List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 21:
                                reader.readUInt64List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 22:
                                reader.readInt32List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 23:
                                reader.readFixed64List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 24:
                                reader.readFixed32List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 25:
                                reader.readBoolList(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 26:
                                readStringList(message, typeAndOffset, reader);
                                unknownFields = ub;
                                break;
                            case 27:
                                readMessageList(message, typeAndOffset, reader, getMessageFieldSchema(pos), extensionRegistry);
                                unknownFields = ub;
                                break;
                            case 28:
                                reader.readBytesList(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 29:
                                reader.readUInt32List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 30:
                                List<Integer> enumList = this.listFieldSchema.mutableListAt(message, offset(typeAndOffset));
                                reader.readEnumList(enumList);
                                ub = SchemaUtil.filterUnknownEnumList(number, enumList, getEnumFieldVerifier(pos), ub, unknownFieldSchema2);
                                unknownFields = ub;
                                break;
                            case 31:
                                reader.readSFixed32List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 32:
                                reader.readSFixed64List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 33:
                                reader.readSInt32List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 34:
                                reader.readSInt64List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 35:
                                reader.readDoubleList(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 36:
                                reader.readFloatList(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 37:
                                reader.readInt64List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 38:
                                reader.readUInt64List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 39:
                                reader.readInt32List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 40:
                                reader.readFixed64List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 41:
                                reader.readFixed32List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 42:
                                reader.readBoolList(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 43:
                                reader.readUInt32List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 44:
                                List<Integer> enumList2 = this.listFieldSchema.mutableListAt(message, offset(typeAndOffset));
                                reader.readEnumList(enumList2);
                                ub = SchemaUtil.filterUnknownEnumList(number, enumList2, getEnumFieldVerifier(pos), ub, unknownFieldSchema2);
                                unknownFields = ub;
                                break;
                            case 45:
                                reader.readSFixed32List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 46:
                                reader.readSFixed64List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 47:
                                reader.readSInt32List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 48:
                                reader.readSInt64List(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
                                unknownFields = ub;
                                break;
                            case 49:
                                readGroupList(message, offset(typeAndOffset), reader, getMessageFieldSchema(pos), extensionRegistry);
                                unknownFields = ub;
                                break;
                            case 50:
                                mergeMap(message, pos, getMapFieldDefaultEntry(pos), extensionRegistry, reader);
                                unknownFields = ub;
                                break;
                            case 51:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), Double.valueOf(reader.readDouble()));
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 52:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), Float.valueOf(reader.readFloat()));
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 53:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), Long.valueOf(reader.readInt64()));
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 54:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), Long.valueOf(reader.readUInt64()));
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 55:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), Integer.valueOf(reader.readInt32()));
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 56:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), Long.valueOf(reader.readFixed64()));
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 57:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), Integer.valueOf(reader.readFixed32()));
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 58:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), Boolean.valueOf(reader.readBool()));
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 59:
                                readString(message, typeAndOffset, reader);
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 60:
                                if (isOneofPresent(message, number, pos)) {
                                    UnsafeUtil.putObject(message, offset(typeAndOffset), Internal.mergeMessage(UnsafeUtil.getObject(message, offset(typeAndOffset)), reader.readMessageBySchemaWithCheck(getMessageFieldSchema(pos), extensionRegistry)));
                                } else {
                                    UnsafeUtil.putObject(message, offset(typeAndOffset), reader.readMessageBySchemaWithCheck(getMessageFieldSchema(pos), extensionRegistry));
                                    setFieldPresent(message, pos);
                                }
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 61:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), reader.readBytes());
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 62:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), Integer.valueOf(reader.readUInt32()));
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 63:
                                int enumValue2 = reader.readEnum();
                                Internal.EnumVerifier enumVerifier2 = getEnumFieldVerifier(pos);
                                if (enumVerifier2 == null || enumVerifier2.isInRange(enumValue2)) {
                                    UnsafeUtil.putObject(message, offset(typeAndOffset), Integer.valueOf(enumValue2));
                                    setOneofPresent(message, number, pos);
                                } else {
                                    ub = SchemaUtil.storeUnknownEnum(number, enumValue2, ub, unknownFieldSchema2);
                                }
                                unknownFields = ub;
                                break;
                            case 64:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), Integer.valueOf(reader.readSFixed32()));
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 65:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), Long.valueOf(reader.readSFixed64()));
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 66:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), Integer.valueOf(reader.readSInt32()));
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 67:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), Long.valueOf(reader.readSInt64()));
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            case 68:
                                UnsafeUtil.putObject(message, offset(typeAndOffset), reader.readGroupBySchemaWithCheck(getMessageFieldSchema(pos), extensionRegistry));
                                setOneofPresent(message, number, pos);
                                unknownFields = ub;
                                break;
                            default:
                                if (ub == null) {
                                    unknownFields = unknownFieldSchema2.newBuilder();
                                } else {
                                    unknownFields = ub;
                                }
                                try {
                                    if (unknownFieldSchema2.mergeOneFieldFrom(unknownFields, reader)) {
                                        ub = unknownFields;
                                        unknownFields = ub;
                                        break;
                                    } else {
                                        for (int i = this.checkInitializedCount; i < this.repeatedFieldOffsetStart; i++) {
                                            unknownFields = filterMapUnknownEnumValues(message, this.intArray[i], unknownFields, unknownFieldSchema2);
                                        }
                                        if (unknownFields != null) {
                                            unknownFieldSchema2.setBuilderToMessage(message, unknownFields);
                                            return;
                                        }
                                        return;
                                    }
                                } catch (InvalidProtocolBufferException.InvalidWireTypeException e) {
                                    break;
                                }
                        }
                    } catch (InvalidProtocolBufferException.InvalidWireTypeException e2) {
                        unknownFields = ub;
                    }
                    ub = unknownFields;
                } else if (number == Integer.MAX_VALUE) {
                    int i2 = this.checkInitializedCount;
                    while (i2 < this.repeatedFieldOffsetStart) {
                        i2++;
                        ub = filterMapUnknownEnumValues(message, this.intArray[i2], ub, unknownFieldSchema2);
                    }
                    if (ub != null) {
                        unknownFieldSchema2.setBuilderToMessage(message, ub);
                        return;
                    }
                    return;
                } else {
                    if (!this.hasExtensions) {
                        extension = null;
                    } else {
                        extension = extensionSchema2.findExtensionByNumber(extensionRegistry, this.defaultInstance, number);
                    }
                    if (extension != null) {
                        if (extensions == null) {
                            extensions = extensionSchema2.getMutableExtensions(message);
                        }
                        ub = extensionSchema2.parseExtension(reader, extension, extensionRegistry, extensions, ub, unknownFieldSchema2);
                    } else if (!unknownFieldSchema2.shouldDiscardUnknownFields(reader)) {
                        if (ub == null) {
                            ub = unknownFieldSchema2.getBuilderFromMessage(message);
                        }
                        if (unknownFieldSchema2.mergeOneFieldFrom(ub, reader)) {
                        }
                    } else if (!reader.skipField()) {
                    }
                }
            } catch (Throwable th) {
                th = th;
                unknownFields = ub;
            }
        }
        int i3 = this.checkInitializedCount;
        while (i3 < this.repeatedFieldOffsetStart) {
            i3++;
            ub = filterMapUnknownEnumValues(message, this.intArray[i3], ub, unknownFieldSchema2);
        }
        if (ub != null) {
            unknownFieldSchema2.setBuilderToMessage(message, ub);
            return;
        }
        return;
        try {
            if (!unknownFieldSchema2.shouldDiscardUnknownFields(reader)) {
                if (unknownFields == null) {
                    unknownFields = unknownFieldSchema2.getBuilderFromMessage(message);
                }
                if (!unknownFieldSchema2.mergeOneFieldFrom(unknownFields, reader)) {
                    for (int i4 = this.checkInitializedCount; i4 < this.repeatedFieldOffsetStart; i4++) {
                        unknownFields = filterMapUnknownEnumValues(message, this.intArray[i4], unknownFields, unknownFieldSchema2);
                    }
                    if (unknownFields != null) {
                        unknownFieldSchema2.setBuilderToMessage(message, unknownFields);
                        return;
                    }
                    return;
                }
            } else if (!reader.skipField()) {
                for (int i5 = this.checkInitializedCount; i5 < this.repeatedFieldOffsetStart; i5++) {
                    unknownFields = filterMapUnknownEnumValues(message, this.intArray[i5], unknownFields, unknownFieldSchema2);
                }
                if (unknownFields != null) {
                    unknownFieldSchema2.setBuilderToMessage(message, unknownFields);
                    return;
                }
                return;
            }
            ub = unknownFields;
        } catch (Throwable th2) {
            th = th2;
            for (int i6 = this.checkInitializedCount; i6 < this.repeatedFieldOffsetStart; i6++) {
                unknownFields = filterMapUnknownEnumValues(message, this.intArray[i6], unknownFields, unknownFieldSchema2);
            }
            if (unknownFields != null) {
                unknownFieldSchema2.setBuilderToMessage(message, unknownFields);
            }
            throw th;
        }
    }

    static UnknownFieldSetLite getMutableUnknownFields(Object message) {
        UnknownFieldSetLite unknownFields = ((GeneratedMessageLite) message).unknownFields;
        if (unknownFields != UnknownFieldSetLite.getDefaultInstance()) {
            return unknownFields;
        }
        UnknownFieldSetLite unknownFields2 = UnknownFieldSetLite.newInstance();
        ((GeneratedMessageLite) message).unknownFields = unknownFields2;
        return unknownFields2;
    }

    private int decodeMapEntryValue(byte[] data, int position, int limit, WireFormat.FieldType fieldType, Class<?> messageType, ArrayDecoders.Registers registers) throws IOException {
        boolean z;
        switch (fieldType) {
            case BOOL:
                int position2 = ArrayDecoders.decodeVarint64(data, position, registers);
                if (registers.long1 != 0) {
                    z = true;
                } else {
                    z = false;
                }
                registers.object1 = Boolean.valueOf(z);
                return position2;
            case BYTES:
                return ArrayDecoders.decodeBytes(data, position, registers);
            case DOUBLE:
                registers.object1 = Double.valueOf(ArrayDecoders.decodeDouble(data, position));
                return position + 8;
            case FIXED32:
            case SFIXED32:
                registers.object1 = Integer.valueOf(ArrayDecoders.decodeFixed32(data, position));
                return position + 4;
            case FIXED64:
            case SFIXED64:
                registers.object1 = Long.valueOf(ArrayDecoders.decodeFixed64(data, position));
                return position + 8;
            case FLOAT:
                registers.object1 = Float.valueOf(ArrayDecoders.decodeFloat(data, position));
                return position + 4;
            case ENUM:
            case INT32:
            case UINT32:
                int position3 = ArrayDecoders.decodeVarint32(data, position, registers);
                registers.object1 = Integer.valueOf(registers.int1);
                return position3;
            case INT64:
            case UINT64:
                int position4 = ArrayDecoders.decodeVarint64(data, position, registers);
                registers.object1 = Long.valueOf(registers.long1);
                return position4;
            case MESSAGE:
                return ArrayDecoders.decodeMessageField(Protobuf.getInstance().schemaFor((Class) messageType), data, position, limit, registers);
            case SINT32:
                int position5 = ArrayDecoders.decodeVarint32(data, position, registers);
                registers.object1 = Integer.valueOf(CodedInputStream.decodeZigZag32(registers.int1));
                return position5;
            case SINT64:
                int position6 = ArrayDecoders.decodeVarint64(data, position, registers);
                registers.object1 = Long.valueOf(CodedInputStream.decodeZigZag64(registers.long1));
                return position6;
            case STRING:
                return ArrayDecoders.decodeStringRequireUtf8(data, position, registers);
            default:
                throw new RuntimeException("unsupported field type.");
        }
    }

    /* JADX WARN: Type inference failed for: r0v21, types: [int], assign insn: 0x003f: IGET  (r0v21 ? I:int) = (r0v20 com.google.protobuf.ArrayDecoders$Registers) com.google.protobuf.ArrayDecoders.Registers.int1 int */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private <K, V> int decodeMapEntry(byte[] r21, int r22, int r23, com.google.protobuf.MapEntryLite.Metadata<K, V> r24, java.util.Map<K, V> r25, com.google.protobuf.ArrayDecoders.Registers r26) throws java.io.IOException {
        /*
            r20 = this;
            r0 = r21
            r1 = r22
            r2 = r26
            int r22 = com.google.protobuf.ArrayDecoders.decodeVarint32(r0, r1, r2)
            r0 = r26
            int r15 = r0.int1
            if (r15 < 0) goto L_0x0014
            int r5 = r23 - r22
            if (r15 <= r5) goto L_0x0019
        L_0x0014:
            com.google.protobuf.InvalidProtocolBufferException r5 = com.google.protobuf.InvalidProtocolBufferException.truncatedMessage()
            throw r5
        L_0x0019:
            int r12 = r22 + r15
            r0 = r24
            K r14 = r0.defaultKey
            r0 = r24
            V r0 = r0.defaultValue
            r18 = r0
            r16 = r22
        L_0x0027:
            r0 = r16
            if (r0 >= r12) goto L_0x00b1
            int r22 = r16 + 1
            byte r17 = r21[r16]
            if (r17 >= 0) goto L_0x0043
            r0 = r17
            r1 = r21
            r2 = r22
            r3 = r26
            int r22 = com.google.protobuf.ArrayDecoders.decodeVarint32(r0, r1, r2, r3)
            r0 = r26
            int r0 = r0.int1
            r17 = r0
        L_0x0043:
            int r13 = r17 >>> 3
            r19 = r17 & 7
            switch(r13) {
                case 1: goto L_0x005b;
                case 2: goto L_0x0081;
                default: goto L_0x004a;
            }
        L_0x004a:
            r0 = r17
            r1 = r21
            r2 = r22
            r3 = r23
            r4 = r26
            int r22 = com.google.protobuf.ArrayDecoders.skipField(r0, r1, r2, r3, r4)
            r16 = r22
            goto L_0x0027
        L_0x005b:
            r0 = r24
            com.google.protobuf.WireFormat$FieldType r5 = r0.keyType
            int r5 = r5.getWireType()
            r0 = r19
            if (r0 != r5) goto L_0x004a
            r0 = r24
            com.google.protobuf.WireFormat$FieldType r9 = r0.keyType
            r10 = 0
            r5 = r20
            r6 = r21
            r7 = r22
            r8 = r23
            r11 = r26
            int r22 = r5.decodeMapEntryValue(r6, r7, r8, r9, r10, r11)
            r0 = r26
            java.lang.Object r14 = r0.object1
            r16 = r22
            goto L_0x0027
        L_0x0081:
            r0 = r24
            com.google.protobuf.WireFormat$FieldType r5 = r0.valueType
            int r5 = r5.getWireType()
            r0 = r19
            if (r0 != r5) goto L_0x004a
            r0 = r24
            com.google.protobuf.WireFormat$FieldType r9 = r0.valueType
            r0 = r24
            V r5 = r0.defaultValue
            java.lang.Class r10 = r5.getClass()
            r5 = r20
            r6 = r21
            r7 = r22
            r8 = r23
            r11 = r26
            int r22 = r5.decodeMapEntryValue(r6, r7, r8, r9, r10, r11)
            r0 = r26
            java.lang.Object r0 = r0.object1
            r18 = r0
            r16 = r22
            goto L_0x0027
        L_0x00b1:
            r0 = r16
            if (r0 == r12) goto L_0x00ba
            com.google.protobuf.InvalidProtocolBufferException r5 = com.google.protobuf.InvalidProtocolBufferException.parseFailure()
            throw r5
        L_0x00ba:
            r0 = r25
            r1 = r18
            r0.put(r14, r1)
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageSchema.decodeMapEntry(byte[], int, int, com.google.protobuf.MapEntryLite$Metadata, java.util.Map, com.google.protobuf.ArrayDecoders$Registers):int");
    }

    private int parseRepeatedField(T message, byte[] data, int position, int limit, int tag, int number, int wireType, int bufferPosition, long typeAndOffset, int fieldType, long fieldOffset, ArrayDecoders.Registers registers) throws IOException {
        int position2;
        Internal.ProtobufList<?> list = (Internal.ProtobufList) UNSAFE.getObject(message, fieldOffset);
        if (!list.isModifiable()) {
            int size = list.size();
            list = list.mutableCopyWithCapacity(size == 0 ? 10 : size * 2);
            UNSAFE.putObject(message, fieldOffset, list);
        }
        switch (fieldType) {
            case 18:
            case 35:
                if (wireType == 2) {
                    return ArrayDecoders.decodePackedDoubleList(data, position, list, registers);
                }
                if (wireType == 1) {
                    return ArrayDecoders.decodeDoubleList(tag, data, position, limit, list, registers);
                }
                return position;
            case 19:
            case 36:
                if (wireType == 2) {
                    return ArrayDecoders.decodePackedFloatList(data, position, list, registers);
                }
                if (wireType == 5) {
                    return ArrayDecoders.decodeFloatList(tag, data, position, limit, list, registers);
                }
                return position;
            case 20:
            case 21:
            case 37:
            case 38:
                if (wireType == 2) {
                    return ArrayDecoders.decodePackedVarint64List(data, position, list, registers);
                }
                if (wireType == 0) {
                    return ArrayDecoders.decodeVarint64List(tag, data, position, limit, list, registers);
                }
                return position;
            case 22:
            case 29:
            case 39:
            case 43:
                if (wireType == 2) {
                    return ArrayDecoders.decodePackedVarint32List(data, position, list, registers);
                }
                if (wireType == 0) {
                    return ArrayDecoders.decodeVarint32List(tag, data, position, limit, list, registers);
                }
                return position;
            case 23:
            case 32:
            case 40:
            case 46:
                if (wireType == 2) {
                    return ArrayDecoders.decodePackedFixed64List(data, position, list, registers);
                }
                if (wireType == 1) {
                    return ArrayDecoders.decodeFixed64List(tag, data, position, limit, list, registers);
                }
                return position;
            case 24:
            case 31:
            case 41:
            case 45:
                if (wireType == 2) {
                    return ArrayDecoders.decodePackedFixed32List(data, position, list, registers);
                }
                if (wireType == 5) {
                    return ArrayDecoders.decodeFixed32List(tag, data, position, limit, list, registers);
                }
                return position;
            case 25:
            case 42:
                if (wireType == 2) {
                    return ArrayDecoders.decodePackedBoolList(data, position, list, registers);
                }
                if (wireType == 0) {
                    return ArrayDecoders.decodeBoolList(tag, data, position, limit, list, registers);
                }
                return position;
            case 26:
                if (wireType != 2) {
                    return position;
                }
                if ((536870912 & typeAndOffset) == 0) {
                    return ArrayDecoders.decodeStringList(tag, data, position, limit, list, registers);
                }
                return ArrayDecoders.decodeStringListRequireUtf8(tag, data, position, limit, list, registers);
            case 27:
                if (wireType == 2) {
                    return ArrayDecoders.decodeMessageList(getMessageFieldSchema(bufferPosition), tag, data, position, limit, list, registers);
                }
                return position;
            case 28:
                if (wireType == 2) {
                    return ArrayDecoders.decodeBytesList(tag, data, position, limit, list, registers);
                }
                return position;
            case 30:
            case 44:
                if (wireType == 2) {
                    position2 = ArrayDecoders.decodePackedVarint32List(data, position, list, registers);
                } else if (wireType != 0) {
                    return position;
                } else {
                    position2 = ArrayDecoders.decodeVarint32List(tag, data, position, limit, list, registers);
                }
                UnknownFieldSetLite unknownFields = ((GeneratedMessageLite) message).unknownFields;
                if (unknownFields == UnknownFieldSetLite.getDefaultInstance()) {
                    unknownFields = null;
                }
                UnknownFieldSetLite unknownFields2 = (UnknownFieldSetLite) SchemaUtil.filterUnknownEnumList(number, list, getEnumFieldVerifier(bufferPosition), unknownFields, this.unknownFieldSchema);
                if (unknownFields2 == null) {
                    return position2;
                }
                ((GeneratedMessageLite) message).unknownFields = unknownFields2;
                return position2;
            case 33:
            case 47:
                if (wireType == 2) {
                    return ArrayDecoders.decodePackedSInt32List(data, position, list, registers);
                }
                if (wireType == 0) {
                    return ArrayDecoders.decodeSInt32List(tag, data, position, limit, list, registers);
                }
                return position;
            case 34:
            case 48:
                if (wireType == 2) {
                    return ArrayDecoders.decodePackedSInt64List(data, position, list, registers);
                }
                if (wireType == 0) {
                    return ArrayDecoders.decodeSInt64List(tag, data, position, limit, list, registers);
                }
                return position;
            case 49:
                if (wireType == 3) {
                    return ArrayDecoders.decodeGroupList(getMessageFieldSchema(bufferPosition), tag, data, position, limit, list, registers);
                }
                return position;
            default:
                return position;
        }
    }

    private <K, V> int parseMapField(T message, byte[] data, int position, int limit, int bufferPosition, long fieldOffset, ArrayDecoders.Registers registers) throws IOException {
        Unsafe unsafe = UNSAFE;
        Object mapDefaultEntry = getMapFieldDefaultEntry(bufferPosition);
        Object mapField = unsafe.getObject(message, fieldOffset);
        if (this.mapFieldSchema.isImmutable(mapField)) {
            Object oldMapField = mapField;
            mapField = this.mapFieldSchema.newMapField(mapDefaultEntry);
            this.mapFieldSchema.mergeFrom(mapField, oldMapField);
            unsafe.putObject(message, fieldOffset, mapField);
        }
        return decodeMapEntry(data, position, limit, this.mapFieldSchema.forMapMetadata(mapDefaultEntry), this.mapFieldSchema.forMutableMapData(mapField), registers);
    }

    private int parseOneofField(T message, byte[] data, int position, int limit, int tag, int number, int wireType, int typeAndOffset, int fieldType, long fieldOffset, int bufferPosition, ArrayDecoders.Registers registers) throws IOException {
        Unsafe unsafe = UNSAFE;
        long oneofCaseOffset = (long) (this.buffer[bufferPosition + 2] & OFFSET_MASK);
        switch (fieldType) {
            case 51:
                if (wireType != 1) {
                    return position;
                }
                unsafe.putObject(message, fieldOffset, Double.valueOf(ArrayDecoders.decodeDouble(data, position)));
                int position2 = position + 8;
                unsafe.putInt(message, oneofCaseOffset, number);
                return position2;
            case 52:
                if (wireType != 5) {
                    return position;
                }
                unsafe.putObject(message, fieldOffset, Float.valueOf(ArrayDecoders.decodeFloat(data, position)));
                int position3 = position + 4;
                unsafe.putInt(message, oneofCaseOffset, number);
                return position3;
            case 53:
            case 54:
                if (wireType != 0) {
                    return position;
                }
                int position4 = ArrayDecoders.decodeVarint64(data, position, registers);
                unsafe.putObject(message, fieldOffset, Long.valueOf(registers.long1));
                unsafe.putInt(message, oneofCaseOffset, number);
                return position4;
            case 55:
            case 62:
                if (wireType != 0) {
                    return position;
                }
                int position5 = ArrayDecoders.decodeVarint32(data, position, registers);
                unsafe.putObject(message, fieldOffset, Integer.valueOf(registers.int1));
                unsafe.putInt(message, oneofCaseOffset, number);
                return position5;
            case 56:
            case 65:
                if (wireType != 1) {
                    return position;
                }
                unsafe.putObject(message, fieldOffset, Long.valueOf(ArrayDecoders.decodeFixed64(data, position)));
                int position6 = position + 8;
                unsafe.putInt(message, oneofCaseOffset, number);
                return position6;
            case 57:
            case 64:
                if (wireType != 5) {
                    return position;
                }
                unsafe.putObject(message, fieldOffset, Integer.valueOf(ArrayDecoders.decodeFixed32(data, position)));
                int position7 = position + 4;
                unsafe.putInt(message, oneofCaseOffset, number);
                return position7;
            case 58:
                if (wireType != 0) {
                    return position;
                }
                int position8 = ArrayDecoders.decodeVarint64(data, position, registers);
                unsafe.putObject(message, fieldOffset, Boolean.valueOf(registers.long1 != 0));
                unsafe.putInt(message, oneofCaseOffset, number);
                return position8;
            case 59:
                if (wireType != 2) {
                    return position;
                }
                int position9 = ArrayDecoders.decodeVarint32(data, position, registers);
                int length = registers.int1;
                if (length == 0) {
                    unsafe.putObject(message, fieldOffset, "");
                } else {
                    if ((536870912 & typeAndOffset) != 0) {
                        if (!Utf8.isValidUtf8(data, position9, position9 + length)) {
                            throw InvalidProtocolBufferException.invalidUtf8();
                        }
                    }
                    unsafe.putObject(message, fieldOffset, new String(data, position9, length, Internal.UTF_8));
                    position9 += length;
                }
                unsafe.putInt(message, oneofCaseOffset, number);
                return position9;
            case 60:
                if (wireType != 2) {
                    return position;
                }
                int position10 = ArrayDecoders.decodeMessageField(getMessageFieldSchema(bufferPosition), data, position, limit, registers);
                Object oldValue = unsafe.getInt(message, oneofCaseOffset) == number ? unsafe.getObject(message, fieldOffset) : null;
                if (oldValue == null) {
                    unsafe.putObject(message, fieldOffset, registers.object1);
                } else {
                    unsafe.putObject(message, fieldOffset, Internal.mergeMessage(oldValue, registers.object1));
                }
                unsafe.putInt(message, oneofCaseOffset, number);
                return position10;
            case 61:
                if (wireType != 2) {
                    return position;
                }
                int position11 = ArrayDecoders.decodeBytes(data, position, registers);
                unsafe.putObject(message, fieldOffset, registers.object1);
                unsafe.putInt(message, oneofCaseOffset, number);
                return position11;
            case 63:
                if (wireType != 0) {
                    return position;
                }
                int position12 = ArrayDecoders.decodeVarint32(data, position, registers);
                int enumValue = registers.int1;
                Internal.EnumVerifier enumVerifier = getEnumFieldVerifier(bufferPosition);
                if (enumVerifier == null || enumVerifier.isInRange(enumValue)) {
                    unsafe.putObject(message, fieldOffset, Integer.valueOf(enumValue));
                    unsafe.putInt(message, oneofCaseOffset, number);
                    return position12;
                }
                getMutableUnknownFields(message).storeField(tag, Long.valueOf((long) enumValue));
                return position12;
            case 66:
                if (wireType != 0) {
                    return position;
                }
                int position13 = ArrayDecoders.decodeVarint32(data, position, registers);
                unsafe.putObject(message, fieldOffset, Integer.valueOf(CodedInputStream.decodeZigZag32(registers.int1)));
                unsafe.putInt(message, oneofCaseOffset, number);
                return position13;
            case 67:
                if (wireType != 0) {
                    return position;
                }
                int position14 = ArrayDecoders.decodeVarint64(data, position, registers);
                unsafe.putObject(message, fieldOffset, Long.valueOf(CodedInputStream.decodeZigZag64(registers.long1)));
                unsafe.putInt(message, oneofCaseOffset, number);
                return position14;
            case 68:
                if (wireType != 3) {
                    return position;
                }
                int position15 = ArrayDecoders.decodeGroupField(getMessageFieldSchema(bufferPosition), data, position, limit, (tag & -8) | 4, registers);
                Object oldValue2 = unsafe.getInt(message, oneofCaseOffset) == number ? unsafe.getObject(message, fieldOffset) : null;
                if (oldValue2 == null) {
                    unsafe.putObject(message, fieldOffset, registers.object1);
                } else {
                    unsafe.putObject(message, fieldOffset, Internal.mergeMessage(oldValue2, registers.object1));
                }
                unsafe.putInt(message, oneofCaseOffset, number);
                return position15;
            default:
                return position;
        }
    }

    private Schema getMessageFieldSchema(int pos) {
        int index = (pos / 3) * 2;
        Schema schema = (Schema) this.objects[index];
        if (schema != null) {
            return schema;
        }
        Schema schema2 = Protobuf.getInstance().schemaFor((Class) this.objects[index + 1]);
        this.objects[index] = schema2;
        return schema2;
    }

    private Object getMapFieldDefaultEntry(int pos) {
        return this.objects[(pos / 3) * 2];
    }

    private Internal.EnumVerifier getEnumFieldVerifier(int pos) {
        return (Internal.EnumVerifier) this.objects[((pos / 3) * 2) + 1];
    }

    /* JADX INFO: additional move instructions added (1) to help type inference */
    /* JADX WARN: Failed to insert an additional move for type inference into block B:138:0x000d */
    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* access modifiers changed from: package-private */
    public int parseProto2Message(T message, byte[] data, int position, int limit, int endGroup, ArrayDecoders.Registers registers) throws IOException {
        Unsafe unsafe = UNSAFE;
        int currentPresenceFieldOffset = -1;
        int currentPresenceField = 0;
        int tag = 0;
        int oldNumber = -1;
        int pos = 0;
        while (true) {
            int position2 = position;
            if (position2 < limit) {
                position = position2 + 1;
                byte b = data[position2];
                tag = b;
                if (b < 0) {
                    position = ArrayDecoders.decodeVarint32(b, data, position, registers);
                    tag = registers.int1;
                }
                int number = tag >>> 3;
                int wireType = tag & true;
                if (number > oldNumber) {
                    pos = positionForFieldNumber(number, pos / 3);
                } else {
                    pos = positionForFieldNumber(number);
                }
                oldNumber = number;
                if (pos == -1) {
                    pos = 0;
                } else {
                    int typeAndOffset = this.buffer[pos + 1];
                    int fieldType = type(typeAndOffset);
                    long fieldOffset = offset(typeAndOffset);
                    if (fieldType <= 17) {
                        int presenceMaskAndOffset = this.buffer[pos + 2];
                        int presenceMask = 1 << (presenceMaskAndOffset >>> 20);
                        int presenceFieldOffset = presenceMaskAndOffset & OFFSET_MASK;
                        if (presenceFieldOffset != currentPresenceFieldOffset) {
                            if (currentPresenceFieldOffset != -1) {
                                unsafe.putInt(message, (long) currentPresenceFieldOffset, currentPresenceField);
                            }
                            currentPresenceFieldOffset = presenceFieldOffset;
                            currentPresenceField = unsafe.getInt(message, (long) presenceFieldOffset);
                        }
                        switch (fieldType) {
                            case 0:
                                if (wireType == true) {
                                    UnsafeUtil.putDouble(message, fieldOffset, ArrayDecoders.decodeDouble(data, position));
                                    position += 8;
                                    currentPresenceField |= presenceMask;
                                    break;
                                }
                                break;
                            case 1:
                                if (wireType == true) {
                                    UnsafeUtil.putFloat(message, fieldOffset, ArrayDecoders.decodeFloat(data, position));
                                    position += 4;
                                    currentPresenceField |= presenceMask;
                                    break;
                                }
                                break;
                            case 2:
                            case 3:
                                if (wireType == false) {
                                    position = ArrayDecoders.decodeVarint64(data, position, registers);
                                    unsafe.putLong(message, fieldOffset, registers.long1);
                                    currentPresenceField |= presenceMask;
                                    break;
                                }
                                break;
                            case 4:
                            case 11:
                                if (wireType == false) {
                                    position = ArrayDecoders.decodeVarint32(data, position, registers);
                                    unsafe.putInt(message, fieldOffset, registers.int1);
                                    currentPresenceField |= presenceMask;
                                    break;
                                }
                                break;
                            case 5:
                            case 14:
                                if (wireType == true) {
                                    unsafe.putLong(message, fieldOffset, ArrayDecoders.decodeFixed64(data, position));
                                    position += 8;
                                    currentPresenceField |= presenceMask;
                                    break;
                                }
                                break;
                            case 6:
                            case 13:
                                if (wireType == true) {
                                    unsafe.putInt(message, fieldOffset, ArrayDecoders.decodeFixed32(data, position));
                                    position += 4;
                                    currentPresenceField |= presenceMask;
                                    break;
                                }
                                break;
                            case 7:
                                if (wireType == false) {
                                    position = ArrayDecoders.decodeVarint64(data, position, registers);
                                    UnsafeUtil.putBoolean(message, fieldOffset, registers.long1 != 0);
                                    currentPresenceField |= presenceMask;
                                    break;
                                }
                                break;
                            case 8:
                                if (wireType == true) {
                                    if ((536870912 & typeAndOffset) == 0) {
                                        position = ArrayDecoders.decodeString(data, position, registers);
                                    } else {
                                        position = ArrayDecoders.decodeStringRequireUtf8(data, position, registers);
                                    }
                                    unsafe.putObject(message, fieldOffset, registers.object1);
                                    currentPresenceField |= presenceMask;
                                    break;
                                }
                                break;
                            case 9:
                                if (wireType == true) {
                                    position = ArrayDecoders.decodeMessageField(getMessageFieldSchema(pos), data, position, limit, registers);
                                    if ((currentPresenceField & presenceMask) == 0) {
                                        unsafe.putObject(message, fieldOffset, registers.object1);
                                    } else {
                                        unsafe.putObject(message, fieldOffset, Internal.mergeMessage(unsafe.getObject(message, fieldOffset), registers.object1));
                                    }
                                    currentPresenceField |= presenceMask;
                                    break;
                                }
                                break;
                            case 10:
                                if (wireType == true) {
                                    position = ArrayDecoders.decodeBytes(data, position, registers);
                                    unsafe.putObject(message, fieldOffset, registers.object1);
                                    currentPresenceField |= presenceMask;
                                    break;
                                }
                                break;
                            case 12:
                                if (wireType == false) {
                                    position = ArrayDecoders.decodeVarint32(data, position, registers);
                                    int enumValue = registers.int1;
                                    Internal.EnumVerifier enumVerifier = getEnumFieldVerifier(pos);
                                    if (enumVerifier != null && !enumVerifier.isInRange(enumValue)) {
                                        getMutableUnknownFields(message).storeField(tag, Long.valueOf((long) enumValue));
                                        break;
                                    } else {
                                        unsafe.putInt(message, fieldOffset, enumValue);
                                        currentPresenceField |= presenceMask;
                                        break;
                                    }
                                }
                                break;
                            case 15:
                                if (wireType == false) {
                                    position = ArrayDecoders.decodeVarint32(data, position, registers);
                                    unsafe.putInt(message, fieldOffset, CodedInputStream.decodeZigZag32(registers.int1));
                                    currentPresenceField |= presenceMask;
                                    break;
                                }
                                break;
                            case 16:
                                if (wireType == false) {
                                    position = ArrayDecoders.decodeVarint64(data, position, registers);
                                    unsafe.putLong(message, fieldOffset, CodedInputStream.decodeZigZag64(registers.long1));
                                    currentPresenceField |= presenceMask;
                                    break;
                                }
                                break;
                            case 17:
                                if (wireType == true) {
                                    position = ArrayDecoders.decodeGroupField(getMessageFieldSchema(pos), data, position, limit, (number << 3) | 4, registers);
                                    if ((currentPresenceField & presenceMask) == 0) {
                                        unsafe.putObject(message, fieldOffset, registers.object1);
                                    } else {
                                        unsafe.putObject(message, fieldOffset, Internal.mergeMessage(unsafe.getObject(message, fieldOffset), registers.object1));
                                    }
                                    currentPresenceField |= presenceMask;
                                    break;
                                }
                                break;
                        }
                    } else if (fieldType == 27) {
                        if (wireType == true) {
                            Internal.ProtobufList<?> list = (Internal.ProtobufList) unsafe.getObject(message, fieldOffset);
                            if (!list.isModifiable()) {
                                int size = list.size();
                                list = list.mutableCopyWithCapacity(size == 0 ? 10 : size * 2);
                                unsafe.putObject(message, fieldOffset, list);
                            }
                            position = ArrayDecoders.decodeMessageList(getMessageFieldSchema(pos), tag, data, position, limit, list, registers);
                        }
                    } else if (fieldType <= 49) {
                        int oldPosition = position;
                        position = parseRepeatedField(message, data, position, limit, tag, number, wireType, pos, (long) typeAndOffset, fieldType, fieldOffset, registers);
                        if (position != oldPosition) {
                        }
                    } else if (fieldType != 50) {
                        int oldPosition2 = position;
                        position = parseOneofField(message, data, position, limit, tag, number, wireType, typeAndOffset, fieldType, fieldOffset, pos, registers);
                        if (position != oldPosition2) {
                        }
                    } else if (wireType == true) {
                        int oldPosition3 = position;
                        position = parseMapField(message, data, position, limit, pos, fieldOffset, registers);
                        if (position != oldPosition3) {
                        }
                    }
                }
                if (tag != endGroup || endGroup == 0) {
                    if (!this.hasExtensions || registers.extensionRegistry == ExtensionRegistryLite.getEmptyRegistry()) {
                        position = ArrayDecoders.decodeUnknownField(tag, data, position, limit, getMutableUnknownFields(message), registers);
                    } else {
                        position = ArrayDecoders.decodeExtensionOrUnknownField(tag, data, position, limit, message, this.defaultInstance, this.unknownFieldSchema, registers);
                    }
                }
            } else {
                position = position2;
            }
        }
        if (currentPresenceFieldOffset != -1) {
            unsafe.putInt(message, (long) currentPresenceFieldOffset, currentPresenceField);
        }
        UnknownFieldSetLite unknownFields = null;
        for (int i = this.checkInitializedCount; i < this.repeatedFieldOffsetStart; i++) {
            unknownFields = (UnknownFieldSetLite) filterMapUnknownEnumValues(message, this.intArray[i], unknownFields, this.unknownFieldSchema);
        }
        if (unknownFields != null) {
            this.unknownFieldSchema.setBuilderToMessage(message, unknownFields);
        }
        if (endGroup == 0) {
            if (position != limit) {
                throw InvalidProtocolBufferException.parseFailure();
            }
        } else if (position > limit || tag != endGroup) {
            throw InvalidProtocolBufferException.parseFailure();
        }
        return position;
    }

    /* JADX WARN: Type inference failed for: r0v64, types: [int], assign insn: 0x0024: IGET  (r0v64 ? I:int) = (r0v63 com.google.protobuf.ArrayDecoders$Registers) com.google.protobuf.ArrayDecoders.Registers.int1 int */
    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int parseProto3Message(T r46, byte[] r47, int r48, int r49, com.google.protobuf.ArrayDecoders.Registers r50) throws java.io.IOException {
        /*
            r45 = this;
            sun.misc.Unsafe r4 = com.google.protobuf.MessageSchema.UNSAFE
            r19 = 0
            r40 = -1
            r22 = 0
            r43 = r48
        L_0x000a:
            r0 = r43
            r1 = r49
            if (r0 >= r1) goto L_0x02c1
            int r48 = r43 + 1
            byte r19 = r47[r43]
            if (r19 >= 0) goto L_0x0028
            r0 = r19
            r1 = r47
            r2 = r48
            r3 = r50
            int r48 = com.google.protobuf.ArrayDecoders.decodeVarint32(r0, r1, r2, r3)
            r0 = r50
            int r0 = r0.int1
            r19 = r0
        L_0x0028:
            int r20 = r19 >>> 3
            r21 = r19 & 7
            r0 = r20
            r1 = r40
            if (r0 <= r1) goto L_0x005a
            int r5 = r22 / 3
            r0 = r45
            r1 = r20
            int r22 = r0.positionForFieldNumber(r1, r5)
        L_0x003c:
            r40 = r20
            r5 = -1
            r0 = r22
            if (r0 != r5) goto L_0x0063
            r22 = 0
        L_0x0045:
            com.google.protobuf.UnknownFieldSetLite r30 = getMutableUnknownFields(r46)
            r26 = r19
            r27 = r47
            r28 = r48
            r29 = r49
            r31 = r50
            int r48 = com.google.protobuf.ArrayDecoders.decodeUnknownField(r26, r27, r28, r29, r30, r31)
            r43 = r48
            goto L_0x000a
        L_0x005a:
            r0 = r45
            r1 = r20
            int r22 = r0.positionForFieldNumber(r1)
            goto L_0x003c
        L_0x0063:
            r0 = r45
            int[] r5 = r0.buffer
            int r8 = r22 + 1
            r34 = r5[r8]
            int r25 = type(r34)
            long r6 = offset(r34)
            r5 = 17
            r0 = r25
            if (r0 > r5) goto L_0x01fe
            switch(r25) {
                case 0: goto L_0x007d;
                case 1: goto L_0x0091;
                case 2: goto L_0x00a5;
                case 3: goto L_0x00a5;
                case 4: goto L_0x00be;
                case 5: goto L_0x00d7;
                case 6: goto L_0x00eb;
                case 7: goto L_0x00ff;
                case 8: goto L_0x0121;
                case 9: goto L_0x014e;
                case 10: goto L_0x018f;
                case 11: goto L_0x00be;
                case 12: goto L_0x01ab;
                case 13: goto L_0x00eb;
                case 14: goto L_0x00d7;
                case 15: goto L_0x01c4;
                case 16: goto L_0x01e1;
                default: goto L_0x007c;
            }
        L_0x007c:
            goto L_0x0045
        L_0x007d:
            r5 = 1
            r0 = r21
            if (r0 != r5) goto L_0x0045
            double r8 = com.google.protobuf.ArrayDecoders.decodeDouble(r47, r48)
            r0 = r46
            com.google.protobuf.UnsafeUtil.putDouble(r0, r6, r8)
            int r48 = r48 + 8
            r43 = r48
            goto L_0x000a
        L_0x0091:
            r5 = 5
            r0 = r21
            if (r0 != r5) goto L_0x0045
            float r5 = com.google.protobuf.ArrayDecoders.decodeFloat(r47, r48)
            r0 = r46
            com.google.protobuf.UnsafeUtil.putFloat(r0, r6, r5)
            int r48 = r48 + 4
            r43 = r48
            goto L_0x000a
        L_0x00a5:
            if (r21 != 0) goto L_0x0045
            r0 = r47
            r1 = r48
            r2 = r50
            int r48 = com.google.protobuf.ArrayDecoders.decodeVarint64(r0, r1, r2)
            r0 = r50
            long r8 = r0.long1
            r5 = r46
            r4.putLong(r5, r6, r8)
            r43 = r48
            goto L_0x000a
        L_0x00be:
            if (r21 != 0) goto L_0x0045
            r0 = r47
            r1 = r48
            r2 = r50
            int r48 = com.google.protobuf.ArrayDecoders.decodeVarint32(r0, r1, r2)
            r0 = r50
            int r5 = r0.int1
            r0 = r46
            r4.putInt(r0, r6, r5)
            r43 = r48
            goto L_0x000a
        L_0x00d7:
            r5 = 1
            r0 = r21
            if (r0 != r5) goto L_0x0045
            long r8 = com.google.protobuf.ArrayDecoders.decodeFixed64(r47, r48)
            r5 = r46
            r4.putLong(r5, r6, r8)
            int r48 = r48 + 8
            r43 = r48
            goto L_0x000a
        L_0x00eb:
            r5 = 5
            r0 = r21
            if (r0 != r5) goto L_0x0045
            int r5 = com.google.protobuf.ArrayDecoders.decodeFixed32(r47, r48)
            r0 = r46
            r4.putInt(r0, r6, r5)
            int r48 = r48 + 4
            r43 = r48
            goto L_0x000a
        L_0x00ff:
            if (r21 != 0) goto L_0x0045
            r0 = r47
            r1 = r48
            r2 = r50
            int r48 = com.google.protobuf.ArrayDecoders.decodeVarint64(r0, r1, r2)
            r0 = r50
            long r8 = r0.long1
            r10 = 0
            int r5 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r5 == 0) goto L_0x011f
            r5 = 1
        L_0x0116:
            r0 = r46
            com.google.protobuf.UnsafeUtil.putBoolean(r0, r6, r5)
            r43 = r48
            goto L_0x000a
        L_0x011f:
            r5 = 0
            goto L_0x0116
        L_0x0121:
            r5 = 2
            r0 = r21
            if (r0 != r5) goto L_0x0045
            r5 = 536870912(0x20000000, float:1.0842022E-19)
            r5 = r5 & r34
            if (r5 != 0) goto L_0x0143
            r0 = r47
            r1 = r48
            r2 = r50
            int r48 = com.google.protobuf.ArrayDecoders.decodeString(r0, r1, r2)
        L_0x0136:
            r0 = r50
            java.lang.Object r5 = r0.object1
            r0 = r46
            r4.putObject(r0, r6, r5)
            r43 = r48
            goto L_0x000a
        L_0x0143:
            r0 = r47
            r1 = r48
            r2 = r50
            int r48 = com.google.protobuf.ArrayDecoders.decodeStringRequireUtf8(r0, r1, r2)
            goto L_0x0136
        L_0x014e:
            r5 = 2
            r0 = r21
            if (r0 != r5) goto L_0x0045
            r0 = r45
            r1 = r22
            com.google.protobuf.Schema r5 = r0.getMessageFieldSchema(r1)
            r0 = r47
            r1 = r48
            r2 = r49
            r3 = r50
            int r48 = com.google.protobuf.ArrayDecoders.decodeMessageField(r5, r0, r1, r2, r3)
            r0 = r46
            java.lang.Object r42 = r4.getObject(r0, r6)
            if (r42 != 0) goto L_0x017c
            r0 = r50
            java.lang.Object r5 = r0.object1
            r0 = r46
            r4.putObject(r0, r6, r5)
            r43 = r48
            goto L_0x000a
        L_0x017c:
            r0 = r50
            java.lang.Object r5 = r0.object1
            r0 = r42
            java.lang.Object r5 = com.google.protobuf.Internal.mergeMessage(r0, r5)
            r0 = r46
            r4.putObject(r0, r6, r5)
            r43 = r48
            goto L_0x000a
        L_0x018f:
            r5 = 2
            r0 = r21
            if (r0 != r5) goto L_0x0045
            r0 = r47
            r1 = r48
            r2 = r50
            int r48 = com.google.protobuf.ArrayDecoders.decodeBytes(r0, r1, r2)
            r0 = r50
            java.lang.Object r5 = r0.object1
            r0 = r46
            r4.putObject(r0, r6, r5)
            r43 = r48
            goto L_0x000a
        L_0x01ab:
            if (r21 != 0) goto L_0x0045
            r0 = r47
            r1 = r48
            r2 = r50
            int r48 = com.google.protobuf.ArrayDecoders.decodeVarint32(r0, r1, r2)
            r0 = r50
            int r5 = r0.int1
            r0 = r46
            r4.putInt(r0, r6, r5)
            r43 = r48
            goto L_0x000a
        L_0x01c4:
            if (r21 != 0) goto L_0x0045
            r0 = r47
            r1 = r48
            r2 = r50
            int r48 = com.google.protobuf.ArrayDecoders.decodeVarint32(r0, r1, r2)
            r0 = r50
            int r5 = r0.int1
            int r5 = com.google.protobuf.CodedInputStream.decodeZigZag32(r5)
            r0 = r46
            r4.putInt(r0, r6, r5)
            r43 = r48
            goto L_0x000a
        L_0x01e1:
            if (r21 != 0) goto L_0x0045
            r0 = r47
            r1 = r48
            r2 = r50
            int r48 = com.google.protobuf.ArrayDecoders.decodeVarint64(r0, r1, r2)
            r0 = r50
            long r8 = r0.long1
            long r8 = com.google.protobuf.CodedInputStream.decodeZigZag64(r8)
            r5 = r46
            r4.putLong(r5, r6, r8)
            r43 = r48
            goto L_0x000a
        L_0x01fe:
            r5 = 27
            r0 = r25
            if (r0 != r5) goto L_0x0245
            r5 = 2
            r0 = r21
            if (r0 != r5) goto L_0x0045
            r0 = r46
            java.lang.Object r13 = r4.getObject(r0, r6)
            com.google.protobuf.Internal$ProtobufList r13 = (com.google.protobuf.Internal.ProtobufList) r13
            boolean r5 = r13.isModifiable()
            if (r5 != 0) goto L_0x0228
            int r44 = r13.size()
            if (r44 != 0) goto L_0x0242
            r5 = 10
        L_0x021f:
            com.google.protobuf.Internal$ProtobufList r13 = r13.mutableCopyWithCapacity(r5)
            r0 = r46
            r4.putObject(r0, r6, r13)
        L_0x0228:
            r0 = r45
            r1 = r22
            com.google.protobuf.Schema r8 = r0.getMessageFieldSchema(r1)
            r9 = r19
            r10 = r47
            r11 = r48
            r12 = r49
            r14 = r50
            int r48 = com.google.protobuf.ArrayDecoders.decodeMessageList(r8, r9, r10, r11, r12, r13, r14)
            r43 = r48
            goto L_0x000a
        L_0x0242:
            int r5 = r44 * 2
            goto L_0x021f
        L_0x0245:
            r5 = 49
            r0 = r25
            if (r0 > r5) goto L_0x026e
            r41 = r48
            r0 = r34
            long r0 = (long) r0
            r23 = r0
            r14 = r45
            r15 = r46
            r16 = r47
            r17 = r48
            r18 = r49
            r26 = r6
            r28 = r50
            int r48 = r14.parseRepeatedField(r15, r16, r17, r18, r19, r20, r21, r22, r23, r25, r26, r28)
            r0 = r48
            r1 = r41
            if (r0 == r1) goto L_0x0045
            r43 = r48
            goto L_0x000a
        L_0x026e:
            r5 = 50
            r0 = r25
            if (r0 != r5) goto L_0x0299
            r5 = 2
            r0 = r21
            if (r0 != r5) goto L_0x0045
            r41 = r48
            r26 = r45
            r27 = r46
            r28 = r47
            r29 = r48
            r30 = r49
            r31 = r22
            r32 = r6
            r34 = r50
            int r48 = r26.parseMapField(r27, r28, r29, r30, r31, r32, r34)
            r0 = r48
            r1 = r41
            if (r0 == r1) goto L_0x0045
            r43 = r48
            goto L_0x000a
        L_0x0299:
            r41 = r48
            r26 = r45
            r27 = r46
            r28 = r47
            r29 = r48
            r30 = r49
            r31 = r19
            r32 = r20
            r33 = r21
            r35 = r25
            r36 = r6
            r38 = r22
            r39 = r50
            int r48 = r26.parseOneofField(r27, r28, r29, r30, r31, r32, r33, r34, r35, r36, r38, r39)
            r0 = r48
            r1 = r41
            if (r0 == r1) goto L_0x0045
            r43 = r48
            goto L_0x000a
        L_0x02c1:
            r0 = r43
            r1 = r49
            if (r0 == r1) goto L_0x02cc
            com.google.protobuf.InvalidProtocolBufferException r5 = com.google.protobuf.InvalidProtocolBufferException.parseFailure()
            throw r5
        L_0x02cc:
            return r43
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageSchema.parseProto3Message(java.lang.Object, byte[], int, int, com.google.protobuf.ArrayDecoders$Registers):int");
    }

    public void mergeFrom(T message, byte[] data, int position, int limit, ArrayDecoders.Registers registers) throws IOException {
        if (this.proto3) {
            parseProto3Message(message, data, position, limit, registers);
        } else {
            parseProto2Message(message, data, position, limit, 0, registers);
        }
    }

    public void makeImmutable(T message) {
        for (int i = this.checkInitializedCount; i < this.repeatedFieldOffsetStart; i++) {
            long offset = offset(typeAndOffsetAt(this.intArray[i]));
            Object mapField = UnsafeUtil.getObject(message, offset);
            if (mapField != null) {
                UnsafeUtil.putObject(message, offset, this.mapFieldSchema.toImmutable(mapField));
            }
        }
        int length = this.intArray.length;
        for (int i2 = this.repeatedFieldOffsetStart; i2 < length; i2++) {
            this.listFieldSchema.makeImmutableListAt(message, (long) this.intArray[i2]);
        }
        this.unknownFieldSchema.makeImmutable(message);
        if (this.hasExtensions) {
            this.extensionSchema.makeImmutable(message);
        }
    }

    private final <K, V> void mergeMap(Object message, int pos, Object mapDefaultEntry, ExtensionRegistryLite extensionRegistry, Reader reader) throws IOException {
        long offset = offset(typeAndOffsetAt(pos));
        Object mapField = UnsafeUtil.getObject(message, offset);
        if (mapField == null) {
            mapField = this.mapFieldSchema.newMapField(mapDefaultEntry);
            UnsafeUtil.putObject(message, offset, mapField);
        } else if (this.mapFieldSchema.isImmutable(mapField)) {
            Object oldMapField = mapField;
            mapField = this.mapFieldSchema.newMapField(mapDefaultEntry);
            this.mapFieldSchema.mergeFrom(mapField, oldMapField);
            UnsafeUtil.putObject(message, offset, mapField);
        }
        reader.readMap(this.mapFieldSchema.forMutableMapData(mapField), this.mapFieldSchema.forMapMetadata(mapDefaultEntry), extensionRegistry);
    }

    private final <UT, UB> UB filterMapUnknownEnumValues(Object message, int pos, UB unknownFields, UnknownFieldSchema<UT, UB> unknownFieldSchema2) {
        int fieldNumber = numberAt(pos);
        Object mapField = UnsafeUtil.getObject(message, offset(typeAndOffsetAt(pos)));
        if (mapField == null) {
            return unknownFields;
        }
        Internal.EnumVerifier enumVerifier = getEnumFieldVerifier(pos);
        if (enumVerifier == null) {
            return unknownFields;
        }
        return filterUnknownEnumMap(pos, fieldNumber, this.mapFieldSchema.forMutableMapData(mapField), enumVerifier, unknownFields, unknownFieldSchema2);
    }

    private final <K, V, UT, UB> UB filterUnknownEnumMap(int pos, int number, Map<K, V> mapData, Internal.EnumVerifier enumVerifier, UB unknownFields, UnknownFieldSchema<UT, UB> unknownFieldSchema2) {
        MapEntryLite.Metadata<?, ?> forMapMetadata = this.mapFieldSchema.forMapMetadata(getMapFieldDefaultEntry(pos));
        Iterator<Map.Entry<K, V>> it2 = mapData.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry<K, V> entry = it2.next();
            if (!enumVerifier.isInRange(((Integer) entry.getValue()).intValue())) {
                if (unknownFields == null) {
                    unknownFields = unknownFieldSchema2.newBuilder();
                }
                ByteString.CodedBuilder codedBuilder = ByteString.newCodedBuilder(MapEntryLite.computeSerializedSize(forMapMetadata, entry.getKey(), entry.getValue()));
                try {
                    MapEntryLite.writeTo(codedBuilder.getCodedOutput(), forMapMetadata, entry.getKey(), entry.getValue());
                    unknownFieldSchema2.addLengthDelimited(unknownFields, number, codedBuilder.build());
                    it2.remove();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return unknownFields;
    }

    public final boolean isInitialized(T message) {
        int currentPresenceFieldOffset = -1;
        int currentPresenceField = 0;
        for (int i = 0; i < this.checkInitializedCount; i++) {
            int pos = this.intArray[i];
            int number = numberAt(pos);
            int typeAndOffset = typeAndOffsetAt(pos);
            int presenceMask = 0;
            if (!this.proto3) {
                int presenceMaskAndOffset = this.buffer[pos + 2];
                int presenceFieldOffset = presenceMaskAndOffset & OFFSET_MASK;
                presenceMask = 1 << (presenceMaskAndOffset >>> 20);
                if (presenceFieldOffset != currentPresenceFieldOffset) {
                    currentPresenceFieldOffset = presenceFieldOffset;
                    currentPresenceField = UNSAFE.getInt(message, (long) presenceFieldOffset);
                }
            }
            if (isRequired(typeAndOffset) && !isFieldPresent(message, pos, currentPresenceField, presenceMask)) {
                return false;
            }
            switch (type(typeAndOffset)) {
                case 9:
                case 17:
                    if (isFieldPresent(message, pos, currentPresenceField, presenceMask) && !isInitialized(message, typeAndOffset, getMessageFieldSchema(pos))) {
                        return false;
                    }
                case 27:
                case 49:
                    if (isListInitialized(message, typeAndOffset, pos)) {
                        break;
                    } else {
                        return false;
                    }
                case 50:
                    if (isMapInitialized(message, typeAndOffset, pos)) {
                        break;
                    } else {
                        return false;
                    }
                case 60:
                case 68:
                    if (isOneofPresent(message, number, pos) && !isInitialized(message, typeAndOffset, getMessageFieldSchema(pos))) {
                        return false;
                    }
            }
        }
        if (!this.hasExtensions || this.extensionSchema.getExtensions(message).isInitialized()) {
            return true;
        }
        return false;
    }

    private static boolean isInitialized(Object message, int typeAndOffset, Schema schema) {
        return schema.isInitialized(UnsafeUtil.getObject(message, offset(typeAndOffset)));
    }

    private <N> boolean isListInitialized(Object message, int typeAndOffset, int pos) {
        List<N> list = (List) UnsafeUtil.getObject(message, offset(typeAndOffset));
        if (list.isEmpty()) {
            return true;
        }
        Schema schema = getMessageFieldSchema(pos);
        for (int i = 0; i < list.size(); i++) {
            if (!schema.isInitialized(list.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isMapInitialized(T message, int typeAndOffset, int pos) {
        Map<?, ?> map = this.mapFieldSchema.forMapData(UnsafeUtil.getObject(message, offset(typeAndOffset)));
        if (map.isEmpty()) {
            return true;
        }
        if (this.mapFieldSchema.forMapMetadata(getMapFieldDefaultEntry(pos)).valueType.getJavaType() != WireFormat.JavaType.MESSAGE) {
            return true;
        }
        Schema schema = null;
        for (Object nested : map.values()) {
            if (schema == null) {
                schema = Protobuf.getInstance().schemaFor((Class) nested.getClass());
            }
            if (!schema.isInitialized(nested)) {
                return false;
            }
        }
        return true;
    }

    private void writeString(int fieldNumber, Object value, Writer writer) throws IOException {
        if (value instanceof String) {
            writer.writeString(fieldNumber, (String) value);
        } else {
            writer.writeBytes(fieldNumber, (ByteString) value);
        }
    }

    private void readString(Object message, int typeAndOffset, Reader reader) throws IOException {
        if (isEnforceUtf8(typeAndOffset)) {
            UnsafeUtil.putObject(message, offset(typeAndOffset), reader.readStringRequireUtf8());
        } else if (this.lite) {
            UnsafeUtil.putObject(message, offset(typeAndOffset), reader.readString());
        } else {
            UnsafeUtil.putObject(message, offset(typeAndOffset), reader.readBytes());
        }
    }

    private void readStringList(Object message, int typeAndOffset, Reader reader) throws IOException {
        if (isEnforceUtf8(typeAndOffset)) {
            reader.readStringListRequireUtf8(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
        } else {
            reader.readStringList(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)));
        }
    }

    private <E> void readMessageList(Object message, int typeAndOffset, Reader reader, Schema<E> schema, ExtensionRegistryLite extensionRegistry) throws IOException {
        reader.readMessageList(this.listFieldSchema.mutableListAt(message, offset(typeAndOffset)), schema, extensionRegistry);
    }

    private <E> void readGroupList(Object message, long offset, Reader reader, Schema<E> schema, ExtensionRegistryLite extensionRegistry) throws IOException {
        reader.readGroupList(this.listFieldSchema.mutableListAt(message, offset), schema, extensionRegistry);
    }

    private int numberAt(int pos) {
        return this.buffer[pos];
    }

    private int typeAndOffsetAt(int pos) {
        return this.buffer[pos + 1];
    }

    private int presenceMaskAndOffsetAt(int pos) {
        return this.buffer[pos + 2];
    }

    private static int type(int value) {
        return (FIELD_TYPE_MASK & value) >>> 20;
    }

    private static boolean isRequired(int value) {
        return (268435456 & value) != 0;
    }

    private static boolean isEnforceUtf8(int value) {
        return (536870912 & value) != 0;
    }

    private static long offset(int value) {
        return (long) (OFFSET_MASK & value);
    }

    private static <T> double doubleAt(T message, long offset) {
        return UnsafeUtil.getDouble(message, offset);
    }

    private static <T> float floatAt(T message, long offset) {
        return UnsafeUtil.getFloat(message, offset);
    }

    private static <T> int intAt(T message, long offset) {
        return UnsafeUtil.getInt(message, offset);
    }

    private static <T> long longAt(T message, long offset) {
        return UnsafeUtil.getLong(message, offset);
    }

    private static <T> boolean booleanAt(T message, long offset) {
        return UnsafeUtil.getBoolean(message, offset);
    }

    private static <T> double oneofDoubleAt(T message, long offset) {
        return ((Double) UnsafeUtil.getObject(message, offset)).doubleValue();
    }

    private static <T> float oneofFloatAt(T message, long offset) {
        return ((Float) UnsafeUtil.getObject(message, offset)).floatValue();
    }

    private static <T> int oneofIntAt(T message, long offset) {
        return ((Integer) UnsafeUtil.getObject(message, offset)).intValue();
    }

    private static <T> long oneofLongAt(T message, long offset) {
        return ((Long) UnsafeUtil.getObject(message, offset)).longValue();
    }

    private static <T> boolean oneofBooleanAt(T message, long offset) {
        return ((Boolean) UnsafeUtil.getObject(message, offset)).booleanValue();
    }

    private boolean arePresentForEquals(T message, T other, int pos) {
        return isFieldPresent(message, pos) == isFieldPresent(other, pos);
    }

    private boolean isFieldPresent(T message, int pos, int presenceField, int presenceMask) {
        if (this.proto3) {
            return isFieldPresent(message, pos);
        }
        return (presenceField & presenceMask) != 0;
    }

    private boolean isFieldPresent(T message, int pos) {
        if (this.proto3) {
            int typeAndOffset = typeAndOffsetAt(pos);
            long offset = offset(typeAndOffset);
            switch (type(typeAndOffset)) {
                case 0:
                    if (UnsafeUtil.getDouble(message, offset) != 0.0d) {
                        return true;
                    }
                    return false;
                case 1:
                    if (UnsafeUtil.getFloat(message, offset) == 0.0f) {
                        return false;
                    }
                    return true;
                case 2:
                    if (UnsafeUtil.getLong(message, offset) == 0) {
                        return false;
                    }
                    return true;
                case 3:
                    if (UnsafeUtil.getLong(message, offset) == 0) {
                        return false;
                    }
                    return true;
                case 4:
                    if (UnsafeUtil.getInt(message, offset) == 0) {
                        return false;
                    }
                    return true;
                case 5:
                    if (UnsafeUtil.getLong(message, offset) == 0) {
                        return false;
                    }
                    return true;
                case 6:
                    if (UnsafeUtil.getInt(message, offset) == 0) {
                        return false;
                    }
                    return true;
                case 7:
                    return UnsafeUtil.getBoolean(message, offset);
                case 8:
                    Object value = UnsafeUtil.getObject(message, offset);
                    if (value instanceof String) {
                        if (((String) value).isEmpty()) {
                            return false;
                        }
                        return true;
                    } else if (!(value instanceof ByteString)) {
                        throw new IllegalArgumentException();
                    } else if (ByteString.EMPTY.equals(value)) {
                        return false;
                    } else {
                        return true;
                    }
                case 9:
                    if (UnsafeUtil.getObject(message, offset) == null) {
                        return false;
                    }
                    return true;
                case 10:
                    if (ByteString.EMPTY.equals(UnsafeUtil.getObject(message, offset))) {
                        return false;
                    }
                    return true;
                case 11:
                    if (UnsafeUtil.getInt(message, offset) == 0) {
                        return false;
                    }
                    return true;
                case 12:
                    if (UnsafeUtil.getInt(message, offset) == 0) {
                        return false;
                    }
                    return true;
                case 13:
                    if (UnsafeUtil.getInt(message, offset) == 0) {
                        return false;
                    }
                    return true;
                case 14:
                    if (UnsafeUtil.getLong(message, offset) == 0) {
                        return false;
                    }
                    return true;
                case 15:
                    if (UnsafeUtil.getInt(message, offset) == 0) {
                        return false;
                    }
                    return true;
                case 16:
                    if (UnsafeUtil.getLong(message, offset) == 0) {
                        return false;
                    }
                    return true;
                case 17:
                    if (UnsafeUtil.getObject(message, offset) == null) {
                        return false;
                    }
                    return true;
                default:
                    throw new IllegalArgumentException();
            }
        } else {
            int presenceMaskAndOffset = presenceMaskAndOffsetAt(pos);
            if ((UnsafeUtil.getInt(message, (long) (OFFSET_MASK & presenceMaskAndOffset)) & (1 << (presenceMaskAndOffset >>> 20))) == 0) {
                return false;
            }
            return true;
        }
    }

    private void setFieldPresent(T message, int pos) {
        if (!this.proto3) {
            int presenceMaskAndOffset = presenceMaskAndOffsetAt(pos);
            long presenceFieldOffset = (long) (OFFSET_MASK & presenceMaskAndOffset);
            UnsafeUtil.putInt(message, presenceFieldOffset, UnsafeUtil.getInt(message, presenceFieldOffset) | (1 << (presenceMaskAndOffset >>> 20)));
        }
    }

    private boolean isOneofPresent(T message, int fieldNumber, int pos) {
        return UnsafeUtil.getInt(message, (long) (OFFSET_MASK & presenceMaskAndOffsetAt(pos))) == fieldNumber;
    }

    private boolean isOneofCaseEqual(T message, T other, int pos) {
        int presenceMaskAndOffset = presenceMaskAndOffsetAt(pos);
        return UnsafeUtil.getInt(message, (long) (presenceMaskAndOffset & OFFSET_MASK)) == UnsafeUtil.getInt(other, (long) (presenceMaskAndOffset & OFFSET_MASK));
    }

    private void setOneofPresent(T message, int fieldNumber, int pos) {
        UnsafeUtil.putInt(message, (long) (OFFSET_MASK & presenceMaskAndOffsetAt(pos)), fieldNumber);
    }

    private int positionForFieldNumber(int number) {
        if (number < this.minFieldNumber || number > this.maxFieldNumber) {
            return -1;
        }
        return slowPositionForFieldNumber(number, 0);
    }

    private int positionForFieldNumber(int number, int min) {
        if (number < this.minFieldNumber || number > this.maxFieldNumber) {
            return -1;
        }
        return slowPositionForFieldNumber(number, min);
    }

    private int slowPositionForFieldNumber(int number, int min) {
        int max = (this.buffer.length / 3) - 1;
        while (min <= max) {
            int mid = (max + min) >>> 1;
            int pos = mid * 3;
            int midFieldNumber = numberAt(pos);
            if (number == midFieldNumber) {
                return pos;
            }
            if (number < midFieldNumber) {
                max = mid - 1;
            } else {
                min = mid + 1;
            }
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public int getSchemaSize() {
        return this.buffer.length * 3;
    }
}
