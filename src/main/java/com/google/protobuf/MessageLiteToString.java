package com.google.protobuf;

import com.google.protobuf.GeneratedMessageLite;
import dji.component.accountcenter.IMemberProtocol;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import kotlin.text.Typography;

final class MessageLiteToString {
    private static final String BUILDER_LIST_SUFFIX = "OrBuilderList";
    private static final String BYTES_SUFFIX = "Bytes";
    private static final String LIST_SUFFIX = "List";
    private static final String MAP_SUFFIX = "Map";

    MessageLiteToString() {
    }

    static String toString(MessageLite messageLite, String commentString) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("# ").append(commentString);
        reflectivePrintWithIndent(messageLite, buffer, 0);
        return buffer.toString();
    }

    private static void reflectivePrintWithIndent(MessageLite messageLite, StringBuilder buffer, int indent) {
        boolean hasValue;
        HashMap hashMap = new HashMap();
        Map<String, Method> nameToMethod = new HashMap<>();
        Set<String> getters = new TreeSet<>();
        Method[] declaredMethods = messageLite.getClass().getDeclaredMethods();
        int length = declaredMethods.length;
        for (int i = 0; i < length; i++) {
            Method method = declaredMethods[i];
            nameToMethod.put(method.getName(), method);
            if (method.getParameterTypes().length == 0) {
                hashMap.put(method.getName(), method);
                if (method.getName().startsWith("get")) {
                    getters.add(method.getName());
                }
            }
        }
        for (String getter : getters) {
            String suffix = getter.replaceFirst("get", "");
            if (suffix.endsWith(LIST_SUFFIX) && !suffix.endsWith(BUILDER_LIST_SUFFIX) && !suffix.equals(LIST_SUFFIX)) {
                String camelCase = suffix.substring(0, 1).toLowerCase() + suffix.substring(1, suffix.length() - LIST_SUFFIX.length());
                Method listMethod = (Method) hashMap.get(getter);
                if (listMethod != null && listMethod.getReturnType().equals(List.class)) {
                    printField(buffer, indent, camelCaseToSnakeCase(camelCase), GeneratedMessageLite.invokeOrDie(listMethod, messageLite, new Object[0]));
                }
            }
            if (suffix.endsWith(MAP_SUFFIX) && !suffix.equals(MAP_SUFFIX)) {
                String camelCase2 = suffix.substring(0, 1).toLowerCase() + suffix.substring(1, suffix.length() - MAP_SUFFIX.length());
                Method mapMethod = (Method) hashMap.get(getter);
                if (mapMethod != null && mapMethod.getReturnType().equals(Map.class) && !mapMethod.isAnnotationPresent(Deprecated.class) && Modifier.isPublic(mapMethod.getModifiers())) {
                    printField(buffer, indent, camelCaseToSnakeCase(camelCase2), GeneratedMessageLite.invokeOrDie(mapMethod, messageLite, new Object[0]));
                }
            }
            if (((Method) nameToMethod.get("set" + suffix)) != null) {
                if (suffix.endsWith(BYTES_SUFFIX)) {
                    if (hashMap.containsKey("get" + suffix.substring(0, suffix.length() - BYTES_SUFFIX.length()))) {
                    }
                }
                String camelCase3 = suffix.substring(0, 1).toLowerCase() + suffix.substring(1);
                Method getMethod = (Method) hashMap.get("get" + suffix);
                Method hasMethod = (Method) hashMap.get("has" + suffix);
                if (getMethod != null) {
                    Object value = GeneratedMessageLite.invokeOrDie(getMethod, messageLite, new Object[0]);
                    if (hasMethod == null) {
                        hasValue = !isDefaultValue(value);
                    } else {
                        hasValue = ((Boolean) GeneratedMessageLite.invokeOrDie(hasMethod, messageLite, new Object[0])).booleanValue();
                    }
                    if (hasValue) {
                        printField(buffer, indent, camelCaseToSnakeCase(camelCase3), value);
                    }
                }
            }
        }
        if (messageLite instanceof GeneratedMessageLite.ExtendableMessage) {
            Iterator<Map.Entry<GeneratedMessageLite.ExtensionDescriptor, Object>> iter = ((GeneratedMessageLite.ExtendableMessage) messageLite).extensions.iterator();
            while (iter.hasNext()) {
                Map.Entry<GeneratedMessageLite.ExtensionDescriptor, Object> entry = iter.next();
                printField(buffer, indent, IMemberProtocol.STRING_SEPERATOR_LEFT + ((GeneratedMessageLite.ExtensionDescriptor) entry.getKey()).getNumber() + IMemberProtocol.STRING_SEPERATOR_RIGHT, entry.getValue());
            }
        }
        if (((GeneratedMessageLite) messageLite).unknownFields != null) {
            ((GeneratedMessageLite) messageLite).unknownFields.printWithIndent(buffer, indent);
        }
    }

    private static boolean isDefaultValue(Object o) {
        boolean z;
        if (o instanceof Boolean) {
            if (!((Boolean) o).booleanValue()) {
                z = true;
            } else {
                z = false;
            }
            return z;
        } else if (o instanceof Integer) {
            if (((Integer) o).intValue() != 0) {
                return false;
            }
            return true;
        } else if (o instanceof Float) {
            if (((Float) o).floatValue() != 0.0f) {
                return false;
            }
            return true;
        } else if (o instanceof Double) {
            if (((Double) o).doubleValue() != 0.0d) {
                return false;
            }
            return true;
        } else if (o instanceof String) {
            return o.equals("");
        } else {
            if (o instanceof ByteString) {
                return o.equals(ByteString.EMPTY);
            }
            if (o instanceof MessageLite) {
                if (o != ((MessageLite) o).getDefaultInstanceForType()) {
                    return false;
                }
                return true;
            } else if (!(o instanceof Enum)) {
                return false;
            } else {
                if (((Enum) o).ordinal() != 0) {
                    return false;
                }
                return true;
            }
        }
    }

    static final void printField(StringBuilder buffer, int indent, String name, Object object) {
        if (object instanceof List) {
            for (Object entry : (List) object) {
                printField(buffer, indent, name, entry);
            }
        } else if (object instanceof Map) {
            for (Map.Entry<?, ?> entry2 : ((Map) object).entrySet()) {
                printField(buffer, indent, name, entry2);
            }
        } else {
            buffer.append(10);
            for (int i = 0; i < indent; i++) {
                buffer.append(' ');
            }
            buffer.append(name);
            if (object instanceof String) {
                buffer.append(": \"").append(TextFormatEscaper.escapeText((String) object)).append((char) Typography.quote);
            } else if (object instanceof ByteString) {
                buffer.append(": \"").append(TextFormatEscaper.escapeBytes((ByteString) object)).append((char) Typography.quote);
            } else if (object instanceof GeneratedMessageLite) {
                buffer.append(" {");
                reflectivePrintWithIndent((GeneratedMessageLite) object, buffer, indent + 2);
                buffer.append("\n");
                for (int i2 = 0; i2 < indent; i2++) {
                    buffer.append(' ');
                }
                buffer.append("}");
            } else if (object instanceof Map.Entry) {
                buffer.append(" {");
                Map.Entry<?, ?> entry3 = (Map.Entry) object;
                printField(buffer, indent + 2, IMemberProtocol.STRING_KEY, entry3.getKey());
                printField(buffer, indent + 2, "value", entry3.getValue());
                buffer.append("\n");
                for (int i3 = 0; i3 < indent; i3++) {
                    buffer.append(' ');
                }
                buffer.append("}");
            } else {
                buffer.append(": ").append(object.toString());
            }
        }
    }

    private static final String camelCaseToSnakeCase(String camelCase) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char ch = camelCase.charAt(i);
            if (Character.isUpperCase(ch)) {
                builder.append("_");
            }
            builder.append(Character.toLowerCase(ch));
        }
        return builder.toString();
    }
}
