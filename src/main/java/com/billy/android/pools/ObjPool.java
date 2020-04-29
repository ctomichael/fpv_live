package com.billy.android.pools;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class ObjPool<T, R> {
    protected ConcurrentLinkedQueue<T> list = new ConcurrentLinkedQueue<>();

    public interface Initable<R> {
        void init(Object obj);
    }

    public interface Resetable {
        void reset();
    }

    /* access modifiers changed from: protected */
    public abstract T newInstance(Object obj);

    public T get(R r) {
        T t = this.list.poll();
        if (t == null) {
            t = newInstance(r);
        }
        if (t != null && (t instanceof Initable)) {
            ((Initable) t).init(r);
        }
        return t;
    }

    public void put(T t) {
        if (t != null) {
            if (t instanceof Resetable) {
                ((Resetable) t).reset();
            }
            this.list.offer(t);
        }
    }

    public void clear() {
        this.list.clear();
    }
}
