package com.google.protobuf;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Internal;
import com.google.protobuf.StructuralMessageInfo;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

final class DescriptorMessageInfoFactory implements MessageInfoFactory {
    private static final String GET_DEFAULT_INSTANCE_METHOD_NAME = "getDefaultInstance";
    private static final DescriptorMessageInfoFactory instance = new DescriptorMessageInfoFactory();
    private static IsInitializedCheckAnalyzer isInitializedCheckAnalyzer = new IsInitializedCheckAnalyzer();
    private static final Set<String> specialFieldNames = new HashSet(Arrays.asList("cached_size", "serialized_size", "class"));

    private DescriptorMessageInfoFactory() {
    }

    public static DescriptorMessageInfoFactory getInstance() {
        return instance;
    }

    public boolean isSupported(Class<?> messageType) {
        return GeneratedMessageV3.class.isAssignableFrom(messageType);
    }

    public MessageInfo messageInfoFor(Class<?> messageType) {
        if (GeneratedMessageV3.class.isAssignableFrom(messageType)) {
            return convert(messageType, descriptorForType(messageType));
        }
        throw new IllegalArgumentException("Unsupported message type: " + messageType.getName());
    }

    private static Message getDefaultInstance(Class<?> messageType) {
        try {
            return (Message) messageType.getDeclaredMethod(GET_DEFAULT_INSTANCE_METHOD_NAME, new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to get default instance for message class " + messageType.getName(), e);
        }
    }

    private static Descriptors.Descriptor descriptorForType(Class<?> messageType) {
        return getDefaultInstance(messageType).getDescriptorForType();
    }

    private static MessageInfo convert(Class<?> messageType, Descriptors.Descriptor messageDescriptor) {
        switch (messageDescriptor.getFile().getSyntax()) {
            case PROTO2:
                return convertProto2(messageType, messageDescriptor);
            case PROTO3:
                return convertProto3(messageType, messageDescriptor);
            default:
                throw new IllegalArgumentException("Unsupported syntax: " + messageDescriptor.getFile().getSyntax());
        }
    }

    static class IsInitializedCheckAnalyzer {
        private int index = 0;
        private final Map<Descriptors.Descriptor, Node> nodeCache = new HashMap();
        private final Map<Descriptors.Descriptor, Boolean> resultCache = new ConcurrentHashMap();
        private final Stack<Node> stack = new Stack<>();

        IsInitializedCheckAnalyzer() {
        }

        public boolean needsIsInitializedCheck(Descriptors.Descriptor descriptor) {
            Boolean cachedValue = this.resultCache.get(descriptor);
            if (cachedValue != null) {
                return cachedValue.booleanValue();
            }
            synchronized (this) {
                Boolean cachedValue2 = this.resultCache.get(descriptor);
                if (cachedValue2 != null) {
                    boolean booleanValue = cachedValue2.booleanValue();
                    return booleanValue;
                }
                boolean z = dfs(descriptor).component.needsIsInitializedCheck;
                return z;
            }
        }

        private static class Node {
            StronglyConnectedComponent component = null;
            final Descriptors.Descriptor descriptor;
            final int index;
            int lowLink;

            Node(Descriptors.Descriptor descriptor2, int index2) {
                this.descriptor = descriptor2;
                this.index = index2;
                this.lowLink = index2;
            }
        }

        private static class StronglyConnectedComponent {
            final List<Descriptors.Descriptor> messages;
            boolean needsIsInitializedCheck;

            private StronglyConnectedComponent() {
                this.messages = new ArrayList();
                this.needsIsInitializedCheck = false;
            }
        }

        private Node dfs(Descriptors.Descriptor descriptor) {
            Node node;
            int i = this.index;
            this.index = i + 1;
            Node result = new Node(descriptor, i);
            this.stack.push(result);
            this.nodeCache.put(descriptor, result);
            for (Descriptors.FieldDescriptor field : descriptor.getFields()) {
                if (field.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                    Node child = this.nodeCache.get(field.getMessageType());
                    if (child == null) {
                        result.lowLink = Math.min(result.lowLink, dfs(field.getMessageType()).lowLink);
                    } else if (child.component == null) {
                        result.lowLink = Math.min(result.lowLink, child.lowLink);
                    }
                }
            }
            if (result.index == result.lowLink) {
                StronglyConnectedComponent component = new StronglyConnectedComponent();
                do {
                    node = this.stack.pop();
                    node.component = component;
                    component.messages.add(node.descriptor);
                } while (node != result);
                analyze(component);
            }
            return result;
        }

        private void analyze(StronglyConnectedComponent component) {
            boolean needsIsInitializedCheck = false;
            Iterator<Descriptors.Descriptor> it2 = component.messages.iterator();
            loop0:
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                Descriptors.Descriptor descriptor = it2.next();
                if (descriptor.isExtendable()) {
                    needsIsInitializedCheck = true;
                    break;
                }
                Iterator<Descriptors.FieldDescriptor> it3 = descriptor.getFields().iterator();
                while (true) {
                    if (it3.hasNext()) {
                        Descriptors.FieldDescriptor field = it3.next();
                        if (field.isRequired()) {
                            needsIsInitializedCheck = true;
                            break loop0;
                        } else if (field.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                            Node node = this.nodeCache.get(field.getMessageType());
                            if (node.component != component && node.component.needsIsInitializedCheck) {
                                needsIsInitializedCheck = true;
                                break loop0;
                            }
                        }
                    }
                }
            }
            component.needsIsInitializedCheck = needsIsInitializedCheck;
            for (Descriptors.Descriptor descriptor2 : component.messages) {
                this.resultCache.put(descriptor2, Boolean.valueOf(component.needsIsInitializedCheck));
            }
        }
    }

    private static boolean needsIsInitializedCheck(Descriptors.Descriptor descriptor) {
        return isInitializedCheckAnalyzer.needsIsInitializedCheck(descriptor);
    }

    private static StructuralMessageInfo convertProto2(Class<?> messageType, Descriptors.Descriptor messageDescriptor) {
        List<Descriptors.FieldDescriptor> fieldDescriptors = messageDescriptor.getFields();
        StructuralMessageInfo.Builder builder = StructuralMessageInfo.newBuilder(fieldDescriptors.size());
        builder.withDefaultInstance(getDefaultInstance(messageType));
        builder.withSyntax(ProtoSyntax.PROTO2);
        builder.withMessageSetWireFormat(messageDescriptor.getOptions().getMessageSetWireFormat());
        OneofState oneofState = new OneofState();
        int bitFieldIndex = 0;
        int presenceMask = 1;
        Field bitField = null;
        for (int i = 0; i < fieldDescriptors.size(); i++) {
            final Descriptors.FieldDescriptor fd = fieldDescriptors.get(i);
            boolean enforceUtf8 = fd.getFile().getOptions().getJavaStringCheckUtf8();
            Internal.EnumVerifier enumVerifier = null;
            if (fd.getJavaType() == Descriptors.FieldDescriptor.JavaType.ENUM) {
                enumVerifier = new Internal.EnumVerifier() {
                    /* class com.google.protobuf.DescriptorMessageInfoFactory.AnonymousClass1 */

                    public boolean isInRange(int number) {
                        return fd.getEnumType().findValueByNumber(number) != null;
                    }
                };
            }
            if (fd.getContainingOneof() != null) {
                builder.withField(buildOneofMember(messageType, fd, oneofState, enforceUtf8, enumVerifier));
            } else {
                Field field = field(messageType, fd);
                int number = fd.getNumber();
                FieldType type = getFieldType(fd);
                if (fd.isMapField()) {
                    Descriptors.FieldDescriptor valueField = fd.getMessageType().findFieldByNumber(2);
                    if (valueField.getJavaType() == Descriptors.FieldDescriptor.JavaType.ENUM) {
                        final Descriptors.FieldDescriptor fieldDescriptor = valueField;
                        enumVerifier = new Internal.EnumVerifier() {
                            /* class com.google.protobuf.DescriptorMessageInfoFactory.AnonymousClass2 */

                            public boolean isInRange(int number) {
                                return fieldDescriptor.getEnumType().findValueByNumber(number) != null;
                            }
                        };
                    }
                    builder.withField(FieldInfo.forMapField(field, number, SchemaUtil.getMapDefaultEntry(messageType, fd.getName()), enumVerifier));
                } else if (!fd.isRepeated()) {
                    if (bitField == null) {
                        bitField = bitField(messageType, bitFieldIndex);
                    }
                    if (fd.isRequired()) {
                        builder.withField(FieldInfo.forProto2RequiredField(field, number, type, bitField, presenceMask, enforceUtf8, enumVerifier));
                    } else {
                        builder.withField(FieldInfo.forProto2OptionalField(field, number, type, bitField, presenceMask, enforceUtf8, enumVerifier));
                    }
                } else if (enumVerifier == null) {
                    if (fd.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                        builder.withField(FieldInfo.forRepeatedMessageField(field, number, type, getTypeForRepeatedMessageField(messageType, fd)));
                    } else if (fd.isPacked()) {
                        builder.withField(FieldInfo.forPackedField(field, number, type, cachedSizeField(messageType, fd)));
                    } else {
                        builder.withField(FieldInfo.forField(field, number, type, enforceUtf8));
                    }
                } else if (fd.isPacked()) {
                    builder.withField(FieldInfo.forPackedFieldWithEnumVerifier(field, number, type, enumVerifier, cachedSizeField(messageType, fd)));
                } else {
                    builder.withField(FieldInfo.forFieldWithEnumVerifier(field, number, type, enumVerifier));
                }
            }
            presenceMask <<= 1;
            if (presenceMask == 0) {
                bitField = null;
                presenceMask = 1;
                bitFieldIndex++;
            }
        }
        List<Integer> fieldsToCheckIsInitialized = new ArrayList<>();
        for (int i2 = 0; i2 < fieldDescriptors.size(); i2++) {
            Descriptors.FieldDescriptor fd2 = fieldDescriptors.get(i2);
            if (fd2.isRequired() || (fd2.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE && needsIsInitializedCheck(fd2.getMessageType()))) {
                fieldsToCheckIsInitialized.add(Integer.valueOf(fd2.getNumber()));
            }
        }
        int[] numbers = new int[fieldsToCheckIsInitialized.size()];
        for (int i3 = 0; i3 < fieldsToCheckIsInitialized.size(); i3++) {
            numbers[i3] = ((Integer) fieldsToCheckIsInitialized.get(i3)).intValue();
        }
        builder.withCheckInitialized(numbers);
        return builder.build();
    }

    private static StructuralMessageInfo convertProto3(Class<?> messageType, Descriptors.Descriptor messageDescriptor) {
        List<Descriptors.FieldDescriptor> fieldDescriptors = messageDescriptor.getFields();
        StructuralMessageInfo.Builder builder = StructuralMessageInfo.newBuilder(fieldDescriptors.size());
        builder.withDefaultInstance(getDefaultInstance(messageType));
        builder.withSyntax(ProtoSyntax.PROTO3);
        OneofState oneofState = new OneofState();
        for (int i = 0; i < fieldDescriptors.size(); i++) {
            Descriptors.FieldDescriptor fd = fieldDescriptors.get(i);
            if (fd.getContainingOneof() != null) {
                builder.withField(buildOneofMember(messageType, fd, oneofState, true, null));
            } else if (fd.isMapField()) {
                builder.withField(FieldInfo.forMapField(field(messageType, fd), fd.getNumber(), SchemaUtil.getMapDefaultEntry(messageType, fd.getName()), null));
            } else if (fd.isRepeated() && fd.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                builder.withField(FieldInfo.forRepeatedMessageField(field(messageType, fd), fd.getNumber(), getFieldType(fd), getTypeForRepeatedMessageField(messageType, fd)));
            } else if (fd.isPacked()) {
                builder.withField(FieldInfo.forPackedField(field(messageType, fd), fd.getNumber(), getFieldType(fd), cachedSizeField(messageType, fd)));
            } else {
                builder.withField(FieldInfo.forField(field(messageType, fd), fd.getNumber(), getFieldType(fd), true));
            }
        }
        return builder.build();
    }

    private static FieldInfo buildOneofMember(Class<?> messageType, Descriptors.FieldDescriptor fd, OneofState oneofState, boolean enforceUtf8, Internal.EnumVerifier enumVerifier) {
        OneofInfo oneof = oneofState.getOneof(messageType, fd.getContainingOneof());
        FieldType type = getFieldType(fd);
        return FieldInfo.forOneofMemberField(fd.getNumber(), type, oneof, getOneofStoredType(messageType, fd, type), enforceUtf8, enumVerifier);
    }

    private static Class<?> getOneofStoredType(Class<?> messageType, Descriptors.FieldDescriptor fd, FieldType type) {
        switch (type.getJavaType()) {
            case BOOLEAN:
                return Boolean.class;
            case BYTE_STRING:
                return ByteString.class;
            case DOUBLE:
                return Double.class;
            case FLOAT:
                return Float.class;
            case ENUM:
            case INT:
                return Integer.class;
            case LONG:
                return Long.class;
            case STRING:
                return String.class;
            case MESSAGE:
                return getOneofStoredTypeForMessage(messageType, fd);
            default:
                throw new IllegalArgumentException("Invalid type for oneof: " + type);
        }
    }

    private static FieldType getFieldType(Descriptors.FieldDescriptor fd) {
        switch (fd.getType()) {
            case BOOL:
                if (!fd.isRepeated()) {
                    return FieldType.BOOL;
                }
                return fd.isPacked() ? FieldType.BOOL_LIST_PACKED : FieldType.BOOL_LIST;
            case BYTES:
                return fd.isRepeated() ? FieldType.BYTES_LIST : FieldType.BYTES;
            case DOUBLE:
                if (!fd.isRepeated()) {
                    return FieldType.DOUBLE;
                }
                return fd.isPacked() ? FieldType.DOUBLE_LIST_PACKED : FieldType.DOUBLE_LIST;
            case ENUM:
                if (!fd.isRepeated()) {
                    return FieldType.ENUM;
                }
                return fd.isPacked() ? FieldType.ENUM_LIST_PACKED : FieldType.ENUM_LIST;
            case FIXED32:
                if (!fd.isRepeated()) {
                    return FieldType.FIXED32;
                }
                return fd.isPacked() ? FieldType.FIXED32_LIST_PACKED : FieldType.FIXED32_LIST;
            case FIXED64:
                if (!fd.isRepeated()) {
                    return FieldType.FIXED64;
                }
                return fd.isPacked() ? FieldType.FIXED64_LIST_PACKED : FieldType.FIXED64_LIST;
            case FLOAT:
                if (!fd.isRepeated()) {
                    return FieldType.FLOAT;
                }
                return fd.isPacked() ? FieldType.FLOAT_LIST_PACKED : FieldType.FLOAT_LIST;
            case GROUP:
                return fd.isRepeated() ? FieldType.GROUP_LIST : FieldType.GROUP;
            case INT32:
                if (!fd.isRepeated()) {
                    return FieldType.INT32;
                }
                return fd.isPacked() ? FieldType.INT32_LIST_PACKED : FieldType.INT32_LIST;
            case INT64:
                if (!fd.isRepeated()) {
                    return FieldType.INT64;
                }
                return fd.isPacked() ? FieldType.INT64_LIST_PACKED : FieldType.INT64_LIST;
            case MESSAGE:
                if (fd.isMapField()) {
                    return FieldType.MAP;
                }
                return fd.isRepeated() ? FieldType.MESSAGE_LIST : FieldType.MESSAGE;
            case SFIXED32:
                if (!fd.isRepeated()) {
                    return FieldType.SFIXED32;
                }
                return fd.isPacked() ? FieldType.SFIXED32_LIST_PACKED : FieldType.SFIXED32_LIST;
            case SFIXED64:
                if (!fd.isRepeated()) {
                    return FieldType.SFIXED64;
                }
                return fd.isPacked() ? FieldType.SFIXED64_LIST_PACKED : FieldType.SFIXED64_LIST;
            case SINT32:
                if (!fd.isRepeated()) {
                    return FieldType.SINT32;
                }
                return fd.isPacked() ? FieldType.SINT32_LIST_PACKED : FieldType.SINT32_LIST;
            case SINT64:
                if (!fd.isRepeated()) {
                    return FieldType.SINT64;
                }
                return fd.isPacked() ? FieldType.SINT64_LIST_PACKED : FieldType.SINT64_LIST;
            case STRING:
                return fd.isRepeated() ? FieldType.STRING_LIST : FieldType.STRING;
            case UINT32:
                if (!fd.isRepeated()) {
                    return FieldType.UINT32;
                }
                return fd.isPacked() ? FieldType.UINT32_LIST_PACKED : FieldType.UINT32_LIST;
            case UINT64:
                if (!fd.isRepeated()) {
                    return FieldType.UINT64;
                }
                return fd.isPacked() ? FieldType.UINT64_LIST_PACKED : FieldType.UINT64_LIST;
            default:
                throw new IllegalArgumentException("Unsupported field type: " + fd.getType());
        }
    }

    private static Field bitField(Class<?> messageType, int index) {
        return field(messageType, "bitField" + index + "_");
    }

    private static Field field(Class<?> messageType, Descriptors.FieldDescriptor fd) {
        return field(messageType, getFieldName(fd));
    }

    private static Field cachedSizeField(Class<?> messageType, Descriptors.FieldDescriptor fd) {
        return field(messageType, getCachedSizeFieldName(fd));
    }

    /* access modifiers changed from: private */
    public static Field field(Class<?> messageType, String fieldName) {
        try {
            return messageType.getDeclaredField(fieldName);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to find field " + fieldName + " in message class " + messageType.getName());
        }
    }

    static String getFieldName(Descriptors.FieldDescriptor fd) {
        String name;
        if (fd.getType() == Descriptors.FieldDescriptor.Type.GROUP) {
            name = fd.getMessageType().getName();
        } else {
            name = fd.getName();
        }
        return snakeCaseToCamelCase(name) + (specialFieldNames.contains(name) ? "__" : "_");
    }

    private static String getCachedSizeFieldName(Descriptors.FieldDescriptor fd) {
        return snakeCaseToCamelCase(fd.getName()) + "MemoizedSerializedSize";
    }

    /* access modifiers changed from: private */
    public static String snakeCaseToCamelCase(String snakeCase) {
        StringBuilder sb = new StringBuilder(snakeCase.length() + 1);
        boolean capNext = false;
        for (int ctr = 0; ctr < snakeCase.length(); ctr++) {
            char next = snakeCase.charAt(ctr);
            if (next == '_') {
                capNext = true;
            } else if (Character.isDigit(next)) {
                sb.append(next);
                capNext = true;
            } else if (capNext) {
                sb.append(Character.toUpperCase(next));
                capNext = false;
            } else if (ctr == 0) {
                sb.append(Character.toLowerCase(next));
            } else {
                sb.append(next);
            }
        }
        return sb.toString();
    }

    private static Class<?> getOneofStoredTypeForMessage(Class<?> messageType, Descriptors.FieldDescriptor fd) {
        try {
            return messageType.getDeclaredMethod(getterForField(fd.getType() == Descriptors.FieldDescriptor.Type.GROUP ? fd.getMessageType().getName() : fd.getName()), new Class[0]).getReturnType();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> getTypeForRepeatedMessageField(Class<?> messageType, Descriptors.FieldDescriptor fd) {
        try {
            return messageType.getDeclaredMethod(getterForField(fd.getType() == Descriptors.FieldDescriptor.Type.GROUP ? fd.getMessageType().getName() : fd.getName()), Integer.TYPE).getReturnType();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getterForField(String snakeCase) {
        String camelCase = snakeCaseToCamelCase(snakeCase);
        return "get" + Character.toUpperCase(camelCase.charAt(0)) + camelCase.substring(1, camelCase.length());
    }

    private static final class OneofState {
        private OneofInfo[] oneofs;

        private OneofState() {
            this.oneofs = new OneofInfo[2];
        }

        /* access modifiers changed from: package-private */
        public OneofInfo getOneof(Class<?> messageType, Descriptors.OneofDescriptor desc) {
            int index = desc.getIndex();
            if (index >= this.oneofs.length) {
                this.oneofs = (OneofInfo[]) Arrays.copyOf(this.oneofs, index * 2);
            }
            OneofInfo info = this.oneofs[index];
            if (info != null) {
                return info;
            }
            OneofInfo info2 = newInfo(messageType, desc);
            this.oneofs[index] = info2;
            return info2;
        }

        private static OneofInfo newInfo(Class<?> messageType, Descriptors.OneofDescriptor desc) {
            String camelCase = DescriptorMessageInfoFactory.snakeCaseToCamelCase(desc.getName());
            return new OneofInfo(desc.getIndex(), DescriptorMessageInfoFactory.field(messageType, camelCase + "Case_"), DescriptorMessageInfoFactory.field(messageType, camelCase + "_"));
        }
    }
}
