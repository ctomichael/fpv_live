package com.lmax.disruptor;

public interface EventProcessor extends Runnable {
    Sequence getSequence();

    void halt();

    boolean isRunning();
}
