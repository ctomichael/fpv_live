package io.reactivex.internal.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.concurrent.atomic.AtomicInteger;

public final class VolatileSizeArrayList<T> extends AtomicInteger implements List<T>, RandomAccess {
    private static final long serialVersionUID = 3972397474470203923L;
    final ArrayList<T> list;

    public VolatileSizeArrayList() {
        this.list = new ArrayList<>();
    }

    public VolatileSizeArrayList(int initialCapacity) {
        this.list = new ArrayList<>(initialCapacity);
    }

    public int size() {
        return get();
    }

    public boolean isEmpty() {
        return get() == 0;
    }

    public boolean contains(Object o) {
        return this.list.contains(o);
    }

    public Iterator<T> iterator() {
        return this.list.iterator();
    }

    public Object[] toArray() {
        return this.list.toArray();
    }

    public <E> E[] toArray(E[] a) {
        return this.list.toArray(a);
    }

    public boolean add(T e) {
        boolean b = this.list.add(e);
        lazySet(this.list.size());
        return b;
    }

    public boolean remove(Object o) {
        boolean b = this.list.remove(o);
        lazySet(this.list.size());
        return b;
    }

    public boolean containsAll(Collection<?> c) {
        return this.list.containsAll(c);
    }

    public boolean addAll(Collection<? extends T> c) {
        boolean b = this.list.addAll(c);
        lazySet(this.list.size());
        return b;
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        boolean b = this.list.addAll(index, c);
        lazySet(this.list.size());
        return b;
    }

    public boolean removeAll(Collection<?> c) {
        boolean b = this.list.removeAll(c);
        lazySet(this.list.size());
        return b;
    }

    public boolean retainAll(Collection<?> c) {
        boolean b = this.list.retainAll(c);
        lazySet(this.list.size());
        return b;
    }

    public void clear() {
        this.list.clear();
        lazySet(0);
    }

    public T get(int index) {
        return this.list.get(index);
    }

    public T set(int index, T element) {
        return this.list.set(index, element);
    }

    public void add(int index, T element) {
        this.list.add(index, element);
        lazySet(this.list.size());
    }

    public T remove(int index) {
        T v = this.list.remove(index);
        lazySet(this.list.size());
        return v;
    }

    public int indexOf(Object o) {
        return this.list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return this.list.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return this.list.listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return this.list.listIterator(index);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return this.list.subList(fromIndex, toIndex);
    }

    public boolean equals(Object obj) {
        if (obj instanceof VolatileSizeArrayList) {
            return this.list.equals(((VolatileSizeArrayList) obj).list);
        }
        return this.list.equals(obj);
    }

    public int hashCode() {
        return this.list.hashCode();
    }

    public String toString() {
        return this.list.toString();
    }
}
