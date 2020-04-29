package com.google.protobuf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapField<K, V> implements MutabilityOracle {
    private final Converter<K, V> converter;
    private volatile boolean isMutable;
    private List<Message> listData;
    private MutatabilityAwareMap<K, V> mapData;
    private volatile StorageMode mode;

    private interface Converter<K, V> {
        Message convertKeyAndValueToMessage(K k, V v);

        void convertMessageToKeyAndValue(Message message, Map<K, V> map);

        Message getMessageDefaultInstance();
    }

    private enum StorageMode {
        MAP,
        LIST,
        BOTH
    }

    private static class ImmutableMessageConverter<K, V> implements Converter<K, V> {
        private final MapEntry<K, V> defaultEntry;

        public ImmutableMessageConverter(MapEntry<K, V> defaultEntry2) {
            this.defaultEntry = defaultEntry2;
        }

        public Message convertKeyAndValueToMessage(K key, V value) {
            return this.defaultEntry.newBuilderForType().setKey(key).setValue(value).buildPartial();
        }

        public void convertMessageToKeyAndValue(Message message, Map<K, V> map) {
            MapEntry<K, V> entry = (MapEntry) message;
            map.put(entry.getKey(), entry.getValue());
        }

        public Message getMessageDefaultInstance() {
            return this.defaultEntry;
        }
    }

    private MapField(Converter<K, V> converter2, StorageMode mode2, Map<K, V> mapData2) {
        this.converter = converter2;
        this.isMutable = true;
        this.mode = mode2;
        this.mapData = new MutatabilityAwareMap<>(this, mapData2);
        this.listData = null;
    }

    private MapField(MapEntry<K, V> defaultEntry, StorageMode mode2, Map<K, V> mapData2) {
        this(new ImmutableMessageConverter(defaultEntry), mode2, mapData2);
    }

    public static <K, V> MapField<K, V> emptyMapField(MapEntry<K, V> defaultEntry) {
        return new MapField<>(defaultEntry, StorageMode.MAP, Collections.emptyMap());
    }

    public static <K, V> MapField<K, V> newMapField(MapEntry<K, V> defaultEntry) {
        return new MapField<>(defaultEntry, StorageMode.MAP, new LinkedHashMap());
    }

    private Message convertKeyAndValueToMessage(K key, V value) {
        return this.converter.convertKeyAndValueToMessage(key, value);
    }

    private void convertMessageToKeyAndValue(Message message, Map<K, V> map) {
        this.converter.convertMessageToKeyAndValue(message, map);
    }

    private List<Message> convertMapToList(MutatabilityAwareMap<K, V> mapData2) {
        List<Message> listData2 = new ArrayList<>();
        for (Map.Entry<K, V> entry : mapData2.entrySet()) {
            listData2.add(convertKeyAndValueToMessage(entry.getKey(), entry.getValue()));
        }
        return listData2;
    }

    private MutatabilityAwareMap<K, V> convertListToMap(List<Message> listData2) {
        Map<K, V> mapData2 = new LinkedHashMap<>();
        for (Message item : listData2) {
            convertMessageToKeyAndValue(item, mapData2);
        }
        return new MutatabilityAwareMap<>(this, mapData2);
    }

    public Map<K, V> getMap() {
        if (this.mode == StorageMode.LIST) {
            synchronized (this) {
                if (this.mode == StorageMode.LIST) {
                    this.mapData = convertListToMap(this.listData);
                    this.mode = StorageMode.BOTH;
                }
            }
        }
        return Collections.unmodifiableMap(this.mapData);
    }

    public Map<K, V> getMutableMap() {
        if (this.mode != StorageMode.MAP) {
            if (this.mode == StorageMode.LIST) {
                this.mapData = convertListToMap(this.listData);
            }
            this.listData = null;
            this.mode = StorageMode.MAP;
        }
        return this.mapData;
    }

    public void mergeFrom(MapField<K, V> other) {
        getMutableMap().putAll(MapFieldLite.copy((Map) other.getMap()));
    }

    public void clear() {
        this.mapData = new MutatabilityAwareMap<>(this, new LinkedHashMap());
        this.mode = StorageMode.MAP;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.MapFieldLite.equals(java.util.Map, java.util.Map):boolean
     arg types: [java.util.Map, java.util.Map<K, V>]
     candidates:
      com.google.protobuf.MapFieldLite.equals(java.lang.Object, java.lang.Object):boolean
      com.google.protobuf.MapFieldLite.equals(java.util.Map, java.util.Map):boolean */
    public boolean equals(Object object) {
        if (!(object instanceof MapField)) {
            return false;
        }
        return MapFieldLite.equals(getMap(), (Map) ((MapField) object).getMap());
    }

    public int hashCode() {
        return MapFieldLite.calculateHashCodeForMap(getMap());
    }

    public MapField<K, V> copy() {
        return new MapField<>(this.converter, StorageMode.MAP, MapFieldLite.copy(getMap()));
    }

    /* access modifiers changed from: package-private */
    public List<Message> getList() {
        if (this.mode == StorageMode.MAP) {
            synchronized (this) {
                if (this.mode == StorageMode.MAP) {
                    this.listData = convertMapToList(this.mapData);
                    this.mode = StorageMode.BOTH;
                }
            }
        }
        return Collections.unmodifiableList(this.listData);
    }

    /* access modifiers changed from: package-private */
    public List<Message> getMutableList() {
        if (this.mode != StorageMode.LIST) {
            if (this.mode == StorageMode.MAP) {
                this.listData = convertMapToList(this.mapData);
            }
            this.mapData = null;
            this.mode = StorageMode.LIST;
        }
        return this.listData;
    }

    /* access modifiers changed from: package-private */
    public Message getMapEntryMessageDefaultInstance() {
        return this.converter.getMessageDefaultInstance();
    }

    public void makeImmutable() {
        this.isMutable = false;
    }

    public boolean isMutable() {
        return this.isMutable;
    }

    public void ensureMutable() {
        if (!isMutable()) {
            throw new UnsupportedOperationException();
        }
    }

    private static class MutatabilityAwareMap<K, V> implements Map<K, V> {
        private final Map<K, V> delegate;
        private final MutabilityOracle mutabilityOracle;

        MutatabilityAwareMap(MutabilityOracle mutabilityOracle2, Map<K, V> delegate2) {
            this.mutabilityOracle = mutabilityOracle2;
            this.delegate = delegate2;
        }

        public int size() {
            return this.delegate.size();
        }

        public boolean isEmpty() {
            return this.delegate.isEmpty();
        }

        public boolean containsKey(Object key) {
            return this.delegate.containsKey(key);
        }

        public boolean containsValue(Object value) {
            return this.delegate.containsValue(value);
        }

        public V get(Object key) {
            return this.delegate.get(key);
        }

        public V put(K key, V value) {
            this.mutabilityOracle.ensureMutable();
            Internal.checkNotNull(key);
            Internal.checkNotNull(value);
            return this.delegate.put(key, value);
        }

        public V remove(Object key) {
            this.mutabilityOracle.ensureMutable();
            return this.delegate.remove(key);
        }

        public void putAll(Map<? extends K, ? extends V> m) {
            this.mutabilityOracle.ensureMutable();
            for (K key : m.keySet()) {
                Internal.checkNotNull(key);
                Internal.checkNotNull(m.get(key));
            }
            this.delegate.putAll(m);
        }

        public void clear() {
            this.mutabilityOracle.ensureMutable();
            this.delegate.clear();
        }

        public Set<K> keySet() {
            return new MutatabilityAwareSet(this.mutabilityOracle, this.delegate.keySet());
        }

        public Collection<V> values() {
            return new MutatabilityAwareCollection(this.mutabilityOracle, this.delegate.values());
        }

        public Set<Map.Entry<K, V>> entrySet() {
            return new MutatabilityAwareSet(this.mutabilityOracle, this.delegate.entrySet());
        }

        public boolean equals(Object o) {
            return this.delegate.equals(o);
        }

        public int hashCode() {
            return this.delegate.hashCode();
        }

        public String toString() {
            return this.delegate.toString();
        }

        private static class MutatabilityAwareCollection<E> implements Collection<E> {
            private final Collection<E> delegate;
            private final MutabilityOracle mutabilityOracle;

            MutatabilityAwareCollection(MutabilityOracle mutabilityOracle2, Collection<E> delegate2) {
                this.mutabilityOracle = mutabilityOracle2;
                this.delegate = delegate2;
            }

            public int size() {
                return this.delegate.size();
            }

            public boolean isEmpty() {
                return this.delegate.isEmpty();
            }

            public boolean contains(Object o) {
                return this.delegate.contains(o);
            }

            public Iterator<E> iterator() {
                return new MutatabilityAwareIterator(this.mutabilityOracle, this.delegate.iterator());
            }

            public Object[] toArray() {
                return this.delegate.toArray();
            }

            public <T> T[] toArray(T[] a) {
                return this.delegate.toArray(a);
            }

            public boolean add(E e) {
                throw new UnsupportedOperationException();
            }

            public boolean remove(Object o) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.remove(o);
            }

            public boolean containsAll(Collection<?> c) {
                return this.delegate.containsAll(c);
            }

            public boolean addAll(Collection<? extends E> collection) {
                throw new UnsupportedOperationException();
            }

            public boolean removeAll(Collection<?> c) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.removeAll(c);
            }

            public boolean retainAll(Collection<?> c) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.retainAll(c);
            }

            public void clear() {
                this.mutabilityOracle.ensureMutable();
                this.delegate.clear();
            }

            public boolean equals(Object o) {
                return this.delegate.equals(o);
            }

            public int hashCode() {
                return this.delegate.hashCode();
            }

            public String toString() {
                return this.delegate.toString();
            }
        }

        private static class MutatabilityAwareSet<E> implements Set<E> {
            private final Set<E> delegate;
            private final MutabilityOracle mutabilityOracle;

            MutatabilityAwareSet(MutabilityOracle mutabilityOracle2, Set<E> delegate2) {
                this.mutabilityOracle = mutabilityOracle2;
                this.delegate = delegate2;
            }

            public int size() {
                return this.delegate.size();
            }

            public boolean isEmpty() {
                return this.delegate.isEmpty();
            }

            public boolean contains(Object o) {
                return this.delegate.contains(o);
            }

            public Iterator<E> iterator() {
                return new MutatabilityAwareIterator(this.mutabilityOracle, this.delegate.iterator());
            }

            public Object[] toArray() {
                return this.delegate.toArray();
            }

            public <T> T[] toArray(T[] a) {
                return this.delegate.toArray(a);
            }

            public boolean add(E e) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.add(e);
            }

            public boolean remove(Object o) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.remove(o);
            }

            public boolean containsAll(Collection<?> c) {
                return this.delegate.containsAll(c);
            }

            public boolean addAll(Collection<? extends E> c) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.addAll(c);
            }

            public boolean retainAll(Collection<?> c) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.retainAll(c);
            }

            public boolean removeAll(Collection<?> c) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.removeAll(c);
            }

            public void clear() {
                this.mutabilityOracle.ensureMutable();
                this.delegate.clear();
            }

            public boolean equals(Object o) {
                return this.delegate.equals(o);
            }

            public int hashCode() {
                return this.delegate.hashCode();
            }

            public String toString() {
                return this.delegate.toString();
            }
        }

        private static class MutatabilityAwareIterator<E> implements Iterator<E> {
            private final Iterator<E> delegate;
            private final MutabilityOracle mutabilityOracle;

            MutatabilityAwareIterator(MutabilityOracle mutabilityOracle2, Iterator<E> delegate2) {
                this.mutabilityOracle = mutabilityOracle2;
                this.delegate = delegate2;
            }

            public boolean hasNext() {
                return this.delegate.hasNext();
            }

            public E next() {
                return this.delegate.next();
            }

            public void remove() {
                this.mutabilityOracle.ensureMutable();
                this.delegate.remove();
            }

            public boolean equals(Object obj) {
                return this.delegate.equals(obj);
            }

            public int hashCode() {
                return this.delegate.hashCode();
            }

            public String toString() {
                return this.delegate.toString();
            }
        }
    }
}
