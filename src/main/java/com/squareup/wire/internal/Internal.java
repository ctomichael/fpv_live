package com.squareup.wire.internal;

import com.squareup.wire.ProtoAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Internal {
    private Internal() {
    }

    public static <T> List<T> newMutableList() {
        return new MutableOnWriteList(Collections.emptyList());
    }

    public static <K, V> Map<K, V> newMutableMap() {
        return new LinkedHashMap();
    }

    public static <T> List<T> copyOf(String name, List<T> list) {
        if (list == null) {
            throw new NullPointerException(name + " == null");
        } else if (list == Collections.emptyList() || (list instanceof ImmutableList)) {
            return new MutableOnWriteList(list);
        } else {
            return new ArrayList(list);
        }
    }

    public static <K, V> Map<K, V> copyOf(String name, Map<K, V> map) {
        if (map != null) {
            return new LinkedHashMap(map);
        }
        throw new NullPointerException(name + " == null");
    }

    public static <T> List<T> immutableCopyOf(String name, List<T> list) {
        if (list == null) {
            throw new NullPointerException(name + " == null");
        }
        if (list instanceof MutableOnWriteList) {
            list = ((MutableOnWriteList) list).mutableList;
        }
        if (list == Collections.emptyList() || (list instanceof ImmutableList)) {
            return list;
        }
        ImmutableList<T> result = new ImmutableList<>(list);
        if (!result.contains(null)) {
            return result;
        }
        throw new IllegalArgumentException(name + ".contains(null)");
    }

    public static <K, V> Map<K, V> immutableCopyOf(String name, Map<K, V> map) {
        if (map == null) {
            throw new NullPointerException(name + " == null");
        } else if (map.isEmpty()) {
            return Collections.emptyMap();
        } else {
            Map<K, V> result = new LinkedHashMap<>(map);
            if (result.containsKey(null)) {
                throw new IllegalArgumentException(name + ".containsKey(null)");
            } else if (!result.containsValue(null)) {
                return Collections.unmodifiableMap(result);
            } else {
                throw new IllegalArgumentException(name + ".containsValue(null)");
            }
        }
    }

    public static <T> void redactElements(List<T> list, ProtoAdapter<T> adapter) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            list.set(i, adapter.redact(list.get(i)));
        }
    }

    public static <T> void redactElements(Map<?, T> map, ProtoAdapter<T> adapter) {
        for (Map.Entry<?, T> entry : map.entrySet()) {
            entry.setValue(adapter.redact(entry.getValue()));
        }
    }

    public static boolean equals(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static IllegalStateException missingRequiredFields(Object... args) {
        StringBuilder sb = new StringBuilder();
        String plural = "";
        int size = args.length;
        for (int i = 0; i < size; i += 2) {
            if (args[i] == null) {
                if (sb.length() > 0) {
                    plural = "s";
                }
                sb.append("\n  ");
                sb.append(args[i + 1]);
            }
        }
        throw new IllegalStateException("Required field" + plural + " not set:" + ((Object) sb));
    }

    public static void checkElementsNotNull(List<?> list) {
        if (list == null) {
            throw new NullPointerException("list == null");
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i) == null) {
                throw new NullPointerException("Element at index " + i + " is null");
            }
        }
    }

    public static void checkElementsNotNull(Map<?, ?> map) {
        if (map == null) {
            throw new NullPointerException("map == null");
        }
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (entry.getKey() == null) {
                throw new NullPointerException("map.containsKey(null)");
            } else if (entry.getValue() == null) {
                throw new NullPointerException("Value for key " + entry.getKey() + " is null");
            }
        }
    }

    public static int countNonNull(Object a, Object b) {
        int i = 1;
        int i2 = a != null ? 1 : 0;
        if (b == null) {
            i = 0;
        }
        return i + i2;
    }

    public static int countNonNull(Object a, Object b, Object c) {
        int i = 1;
        int i2 = (b != null ? 1 : 0) + (a != null ? 1 : 0);
        if (c == null) {
            i = 0;
        }
        return i + i2;
    }

    public static int countNonNull(Object a, Object b, Object c, Object d, Object... rest) {
        int result = 0;
        if (a != null) {
            result = 0 + 1;
        }
        if (b != null) {
            result++;
        }
        if (c != null) {
            result++;
        }
        if (d != null) {
            result++;
        }
        for (Object o : rest) {
            if (o != null) {
                result++;
            }
        }
        return result;
    }
}
