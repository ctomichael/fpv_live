package dji.thirdparty.io.reactivex.internal.util;

import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.functions.BiPredicate;
import dji.thirdparty.io.reactivex.functions.Predicate;
import org.reactivestreams.Subscriber;

public class AppendOnlyLinkedArrayList<T> {
    final int capacity;
    final Object[] head;
    int offset;
    Object[] tail = this.head;

    public interface NonThrowingPredicate<T> extends Predicate<T> {
        boolean test(T t);
    }

    public AppendOnlyLinkedArrayList(int capacity2) {
        this.capacity = capacity2;
        this.head = new Object[(capacity2 + 1)];
    }

    public void add(T value) {
        int c = this.capacity;
        int o = this.offset;
        if (o == c) {
            Object[] next = new Object[(c + 1)];
            this.tail[c] = next;
            this.tail = next;
            o = 0;
        }
        this.tail[o] = value;
        this.offset = o + 1;
    }

    public void setFirst(T value) {
        this.head[0] = value;
    }

    public void forEachWhile(NonThrowingPredicate<? super T> consumer) {
        int c = this.capacity;
        for (Object[] a = this.head; a != null; a = (Object[]) a[c]) {
            for (int i = 0; i < c; i++) {
                Object o = a[i];
                if (o == null || consumer.test(o)) {
                    break;
                }
            }
        }
    }

    public <U> boolean accept(Subscriber subscriber) {
        int c = this.capacity;
        for (Object[] a = this.head; a != null; a = (Object[]) a[c]) {
            for (int i = 0; i < c; i++) {
                Object o = a[i];
                if (o == null || NotificationLite.acceptFull(o, subscriber)) {
                    break;
                }
            }
        }
        return false;
    }

    public <U> boolean accept(Observer observer) {
        int c = this.capacity;
        for (Object[] a = this.head; a != null; a = (Object[]) a[c]) {
            for (int i = 0; i < c; i++) {
                Object o = a[i];
                if (o == null || NotificationLite.acceptFull(o, observer)) {
                    break;
                }
            }
        }
        return false;
    }

    public <S> void forEachWhile(S state, BiPredicate<? super S, ? super T> consumer) throws Exception {
        Object[] a = this.head;
        int c = this.capacity;
        while (true) {
            int i = 0;
            while (i < c) {
                Object o = a[i];
                if (o != null && !consumer.test(state, o)) {
                    i++;
                } else {
                    return;
                }
            }
            a = (Object[]) a[c];
        }
    }
}
