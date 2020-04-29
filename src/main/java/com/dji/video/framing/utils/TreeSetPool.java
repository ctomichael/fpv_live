package com.dji.video.framing.utils;

import java.util.Comparator;
import java.util.TreeSet;

public class TreeSetPool<T> {
    private int capacity;
    private TreeSet<T> mTreeSet;

    public TreeSetPool(int capacity2, Comparator<T> comparator) {
        this.capacity = capacity2;
        this.mTreeSet = new TreeSet<>(comparator);
    }

    public synchronized T obtain(T t) {
        T rst;
        rst = this.mTreeSet.ceiling(t);
        if (rst != null) {
            this.mTreeSet.remove(rst);
        }
        return rst;
    }

    public synchronized void recycle(T t) {
        if (t != null) {
            while (this.capacity > 0 && this.mTreeSet.size() > this.capacity) {
                this.mTreeSet.remove(this.mTreeSet.first());
            }
            if (this.capacity <= 0 || this.mTreeSet.size() != this.capacity) {
                this.mTreeSet.add(t);
            } else {
                T firstBuf = this.mTreeSet.first();
                if (firstBuf == null || this.mTreeSet.comparator().compare(t, firstBuf) > 0) {
                    this.mTreeSet.remove(firstBuf);
                    this.mTreeSet.add(t);
                }
            }
        }
    }

    public synchronized void clear() {
        this.mTreeSet.clear();
    }
}
