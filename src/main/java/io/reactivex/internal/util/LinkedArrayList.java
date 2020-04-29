package io.reactivex.internal.util;

import java.util.ArrayList;
import java.util.List;

public class LinkedArrayList {
    final int capacityHint;
    Object[] head;
    int indexInTail;
    volatile int size;
    Object[] tail;

    public LinkedArrayList(int capacityHint2) {
        this.capacityHint = capacityHint2;
    }

    public void add(Object o) {
        if (this.size == 0) {
            this.head = new Object[(this.capacityHint + 1)];
            this.tail = this.head;
            this.head[0] = o;
            this.indexInTail = 1;
            this.size = 1;
        } else if (this.indexInTail == this.capacityHint) {
            Object[] t = new Object[(this.capacityHint + 1)];
            t[0] = o;
            this.tail[this.capacityHint] = t;
            this.tail = t;
            this.indexInTail = 1;
            this.size++;
        } else {
            this.tail[this.indexInTail] = o;
            this.indexInTail++;
            this.size++;
        }
    }

    public Object[] head() {
        return this.head;
    }

    public int size() {
        return this.size;
    }

    public String toString() {
        int cap = this.capacityHint;
        int s = this.size;
        List<Object> list = new ArrayList<>(s + 1);
        Object[] h = head();
        int j = 0;
        int k = 0;
        while (j < s) {
            list.add(h[k]);
            j++;
            k++;
            if (k == cap) {
                k = 0;
                h = (Object[]) h[cap];
            }
        }
        return list.toString();
    }
}
