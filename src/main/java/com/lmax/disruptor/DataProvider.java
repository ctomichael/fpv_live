package com.lmax.disruptor;

public interface DataProvider<T> {
    T get(long j);
}
