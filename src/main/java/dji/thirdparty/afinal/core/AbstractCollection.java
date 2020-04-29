package dji.thirdparty.afinal.core;

import com.adobe.xmp.XMPConst;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractCollection<E> implements Collection<E> {
    public abstract Iterator<E> iterator();

    public abstract int size();

    protected AbstractCollection() {
    }

    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends E> collection) {
        boolean result = false;
        for (Object obj : collection) {
            if (add(obj)) {
                result = true;
            }
        }
        return result;
    }

    public void clear() {
        Iterator<E> it2 = iterator();
        while (it2.hasNext()) {
            it2.next();
            it2.remove();
        }
    }

    public boolean contains(Object object) {
        Iterator<E> it2 = iterator();
        if (object != null) {
            while (it2.hasNext()) {
                if (object.equals(it2.next())) {
                    return true;
                }
            }
        } else {
            while (it2.hasNext()) {
                if (it2.next() == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsAll(Collection<?> collection) {
        for (Object obj : collection) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean remove(Object object) {
        Iterator<?> it2 = iterator();
        if (object != null) {
            while (it2.hasNext()) {
                if (object.equals(it2.next())) {
                    it2.remove();
                    return true;
                }
            }
        } else {
            while (it2.hasNext()) {
                if (it2.next() == null) {
                    it2.remove();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeAll(Collection<?> collection) {
        boolean result = false;
        Iterator<?> it2 = iterator();
        while (it2.hasNext()) {
            if (collection.contains(it2.next())) {
                it2.remove();
                result = true;
            }
        }
        return result;
    }

    public boolean retainAll(Collection<?> collection) {
        boolean result = false;
        Iterator<?> it2 = iterator();
        while (it2.hasNext()) {
            if (!collection.contains(it2.next())) {
                it2.remove();
                result = true;
            }
        }
        return result;
    }

    public Object[] toArray() {
        int size = size();
        Iterator<?> it2 = iterator();
        Object[] array = new Object[size];
        for (int index = 0; index < size; index++) {
            array[index] = it2.next();
        }
        return array;
    }

    public <T> T[] toArray(T[] contents) {
        int size = size();
        int index = 0;
        if (size > contents.length) {
            contents = (Object[]) ((Object[]) Array.newInstance(contents.getClass().getComponentType(), size));
        }
        Iterator it2 = iterator();
        while (it2.hasNext()) {
            contents[index] = it2.next();
            index++;
        }
        if (index < contents.length) {
            contents[index] = null;
        }
        return contents;
    }

    public String toString() {
        if (isEmpty()) {
            return XMPConst.ARRAY_ITEM_NAME;
        }
        StringBuilder buffer = new StringBuilder(size() * 16);
        buffer.append('[');
        Iterator<?> it2 = iterator();
        while (it2.hasNext()) {
            Object next = it2.next();
            if (next != this) {
                buffer.append(next);
            } else {
                buffer.append("(this Collection)");
            }
            if (it2.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append(']');
        return buffer.toString();
    }
}
