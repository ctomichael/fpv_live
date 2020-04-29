package com.lmax.disruptor;

public interface TimeoutHandler {
    void onTimeout(long j) throws Exception;
}
