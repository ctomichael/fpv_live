package com.lmax.disruptor.dsl;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public class BasicExecutor implements Executor {
    private final ThreadFactory factory;
    private final Queue<Thread> threads = new ConcurrentLinkedQueue();

    public BasicExecutor(ThreadFactory factory2) {
        this.factory = factory2;
    }

    public void execute(Runnable command) {
        Thread thread = this.factory.newThread(command);
        if (thread == null) {
            throw new RuntimeException("Failed to create thread to run: " + command);
        }
        thread.start();
        this.threads.add(thread);
    }

    public String toString() {
        return "BasicExecutor{threads=" + dumpThreadInfo() + '}';
    }

    private String dumpThreadInfo() {
        StringBuilder sb = new StringBuilder();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        for (Thread t : this.threads) {
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(t.getId());
            sb.append("{");
            sb.append("name=").append(t.getName()).append(",");
            sb.append("id=").append(t.getId()).append(",");
            sb.append("state=").append(threadInfo.getThreadState()).append(",");
            sb.append("lockInfo=").append(threadInfo.getLockInfo());
            sb.append("}");
        }
        return sb.toString();
    }
}
