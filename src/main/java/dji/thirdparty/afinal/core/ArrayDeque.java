package dji.thirdparty.afinal.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<E> extends AbstractCollection<E> implements Deque<E>, Cloneable, Serializable {
    static final /* synthetic */ boolean $assertionsDisabled = (!ArrayDeque.class.desiredAssertionStatus());
    private static final int MIN_INITIAL_CAPACITY = 8;
    private static final long serialVersionUID = 2340985798034038923L;
    /* access modifiers changed from: private */
    public transient E[] elements;
    /* access modifiers changed from: private */
    public transient int head;
    /* access modifiers changed from: private */
    public transient int tail;

    private void allocateElements(int numElements) {
        int initialCapacity = 8;
        if (numElements >= 8) {
            int initialCapacity2 = numElements;
            int initialCapacity3 = initialCapacity2 | (initialCapacity2 >>> 1);
            int initialCapacity4 = initialCapacity3 | (initialCapacity3 >>> 2);
            int initialCapacity5 = initialCapacity4 | (initialCapacity4 >>> 4);
            int initialCapacity6 = initialCapacity5 | (initialCapacity5 >>> 8);
            initialCapacity = (initialCapacity6 | (initialCapacity6 >>> 16)) + 1;
            if (initialCapacity < 0) {
                initialCapacity >>>= 1;
            }
        }
        this.elements = (Object[]) new Object[initialCapacity];
    }

    private void doubleCapacity() {
        if ($assertionsDisabled || this.head == this.tail) {
            int p = this.head;
            int n = this.elements.length;
            int r = n - p;
            int newCapacity = n << 1;
            if (newCapacity < 0) {
                throw new IllegalStateException("Sorry, deque too big");
            }
            Object[] a = new Object[newCapacity];
            System.arraycopy(this.elements, p, a, 0, r);
            System.arraycopy(this.elements, 0, a, r, p);
            this.elements = a;
            this.head = 0;
            this.tail = n;
            return;
        }
        throw new AssertionError();
    }

    private <T> T[] copyElements(T[] a) {
        if (this.head < this.tail) {
            System.arraycopy(this.elements, this.head, a, 0, size());
        } else if (this.head > this.tail) {
            int headPortionLen = this.elements.length - this.head;
            System.arraycopy(this.elements, this.head, a, 0, headPortionLen);
            System.arraycopy(this.elements, 0, a, headPortionLen, this.tail);
        }
        return a;
    }

    public ArrayDeque() {
        this.elements = (Object[]) new Object[16];
    }

    public ArrayDeque(int numElements) {
        allocateElements(numElements);
    }

    public ArrayDeque(Collection<? extends E> c) {
        allocateElements(c.size());
        addAll(c);
    }

    public void addFirst(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        E[] eArr = this.elements;
        int length = (this.head - 1) & (this.elements.length - 1);
        this.head = length;
        eArr[length] = e;
        if (this.head == this.tail) {
            doubleCapacity();
        }
    }

    public void addLast(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        this.elements[this.tail] = e;
        int length = (this.tail + 1) & (this.elements.length - 1);
        this.tail = length;
        if (length == this.head) {
            doubleCapacity();
        }
    }

    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    public E removeFirst() {
        E x = pollFirst();
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }

    public E removeLast() {
        E x = pollLast();
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }

    public E pollFirst() {
        int h = this.head;
        E result = this.elements[h];
        if (result == null) {
            return null;
        }
        this.elements[h] = null;
        this.head = (h + 1) & (this.elements.length - 1);
        return result;
    }

    public E pollLast() {
        int t = (this.tail - 1) & (this.elements.length - 1);
        E result = this.elements[t];
        if (result == null) {
            return null;
        }
        this.elements[t] = null;
        this.tail = t;
        return result;
    }

    public E getFirst() {
        E x = this.elements[this.head];
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }

    public E getLast() {
        E x = this.elements[(this.tail - 1) & (this.elements.length - 1)];
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }

    public E peekFirst() {
        return this.elements[this.head];
    }

    public E peekLast() {
        return this.elements[(this.tail - 1) & (this.elements.length - 1)];
    }

    public boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            return false;
        }
        int mask = this.elements.length - 1;
        int i = this.head;
        while (true) {
            E x = this.elements[i];
            if (x == null) {
                return false;
            }
            if (o.equals(x)) {
                delete(i);
                return true;
            }
            i = (i + 1) & mask;
        }
    }

    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            return false;
        }
        int mask = this.elements.length - 1;
        int i = this.tail - 1;
        while (true) {
            int i2 = i & mask;
            E x = this.elements[i2];
            if (x == null) {
                return false;
            }
            if (o.equals(x)) {
                delete(i2);
                return true;
            }
            i = i2 - 1;
        }
    }

    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public boolean offer(E e) {
        return offerLast(e);
    }

    public E remove() {
        return removeFirst();
    }

    public E poll() {
        return pollFirst();
    }

    public E element() {
        return getFirst();
    }

    public E peek() {
        return peekFirst();
    }

    public void push(E e) {
        addFirst(e);
    }

    public E pop() {
        return removeFirst();
    }

    private void checkInvariants() {
        if (!$assertionsDisabled && this.elements[this.tail] != null) {
            throw new AssertionError();
        } else if (!$assertionsDisabled && (this.head != this.tail ? this.elements[this.head] == null || this.elements[(this.tail - 1) & (this.elements.length - 1)] == null : this.elements[this.head] != null)) {
            throw new AssertionError();
        } else if (!$assertionsDisabled && this.elements[(this.head - 1) & (this.elements.length - 1)] != null) {
            throw new AssertionError();
        }
    }

    /* access modifiers changed from: private */
    public boolean delete(int i) {
        checkInvariants();
        E[] elements2 = this.elements;
        int mask = elements2.length - 1;
        int h = this.head;
        int t = this.tail;
        int front = (i - h) & mask;
        int back = (t - i) & mask;
        if (front >= ((t - h) & mask)) {
            throw new ConcurrentModificationException();
        } else if (front < back) {
            if (h <= i) {
                System.arraycopy(elements2, h, elements2, h + 1, front);
            } else {
                System.arraycopy(elements2, 0, elements2, 1, i);
                elements2[0] = elements2[mask];
                System.arraycopy(elements2, h, elements2, h + 1, mask - h);
            }
            elements2[h] = null;
            this.head = (h + 1) & mask;
            return false;
        } else {
            if (i < t) {
                System.arraycopy(elements2, i + 1, elements2, i, back);
                this.tail = t - 1;
            } else {
                System.arraycopy(elements2, i + 1, elements2, i, mask - i);
                elements2[mask] = elements2[0];
                System.arraycopy(elements2, 1, elements2, 0, t);
                this.tail = (t - 1) & mask;
            }
            return true;
        }
    }

    public int size() {
        return (this.tail - this.head) & (this.elements.length - 1);
    }

    public boolean isEmpty() {
        return this.head == this.tail;
    }

    public Iterator<E> iterator() {
        return new DeqIterator();
    }

    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    private class DeqIterator implements Iterator<E> {
        private int cursor;
        private int fence;
        private int lastRet;

        private DeqIterator() {
            this.cursor = ArrayDeque.this.head;
            this.fence = ArrayDeque.this.tail;
            this.lastRet = -1;
        }

        public boolean hasNext() {
            return this.cursor != this.fence;
        }

        public E next() {
            if (this.cursor == this.fence) {
                throw new NoSuchElementException();
            }
            E result = ArrayDeque.this.elements[this.cursor];
            if (ArrayDeque.this.tail != this.fence || result == null) {
                throw new ConcurrentModificationException();
            }
            this.lastRet = this.cursor;
            this.cursor = (this.cursor + 1) & (ArrayDeque.this.elements.length - 1);
            return result;
        }

        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            if (ArrayDeque.this.delete(this.lastRet)) {
                this.cursor = (this.cursor - 1) & (ArrayDeque.this.elements.length - 1);
                this.fence = ArrayDeque.this.tail;
            }
            this.lastRet = -1;
        }
    }

    private class DescendingIterator implements Iterator<E> {
        private int cursor;
        private int fence;
        private int lastRet;

        private DescendingIterator() {
            this.cursor = ArrayDeque.this.tail;
            this.fence = ArrayDeque.this.head;
            this.lastRet = -1;
        }

        public boolean hasNext() {
            return this.cursor != this.fence;
        }

        public E next() {
            if (this.cursor == this.fence) {
                throw new NoSuchElementException();
            }
            this.cursor = (this.cursor - 1) & (ArrayDeque.this.elements.length - 1);
            E result = ArrayDeque.this.elements[this.cursor];
            if (ArrayDeque.this.head != this.fence || result == null) {
                throw new ConcurrentModificationException();
            }
            this.lastRet = this.cursor;
            return result;
        }

        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            if (!ArrayDeque.this.delete(this.lastRet)) {
                this.cursor = (this.cursor + 1) & (ArrayDeque.this.elements.length - 1);
                this.fence = ArrayDeque.this.head;
            }
            this.lastRet = -1;
        }
    }

    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        int mask = this.elements.length - 1;
        int i = this.head;
        while (true) {
            E x = this.elements[i];
            if (x == null) {
                return false;
            }
            if (o.equals(x)) {
                return true;
            }
            i = (i + 1) & mask;
        }
    }

    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    public void clear() {
        int h = this.head;
        int t = this.tail;
        if (h != t) {
            this.tail = 0;
            this.head = 0;
            int i = h;
            int mask = this.elements.length - 1;
            do {
                this.elements[i] = null;
                i = (i + 1) & mask;
            } while (i != t);
        }
    }

    public Object[] toArray() {
        return copyElements(new Object[size()]);
    }

    public <T> T[] toArray(T[] a) {
        int size = size();
        if (a.length < size) {
            a = (Object[]) ((Object[]) Array.newInstance(a.getClass().getComponentType(), size));
        }
        copyElements(a);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    public ArrayDeque<E> clone() {
        try {
            ArrayDeque<E> result = (ArrayDeque) super.clone();
            result.elements = Arrays.copyOf(this.elements, this.elements.length);
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(size());
        int mask = this.elements.length - 1;
        for (int i = this.head; i != this.tail; i = (i + 1) & mask) {
            s.writeObject(this.elements[i]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int size = s.readInt();
        allocateElements(size);
        this.head = 0;
        this.tail = size;
        for (int i = 0; i < size; i++) {
            this.elements[i] = s.readObject();
        }
    }
}
