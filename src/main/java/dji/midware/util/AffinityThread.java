package dji.midware.util;

public class AffinityThread extends Thread {
    private static final String TAG = "AffinityThread";

    public AffinityThread(Runnable r, String s) {
        super(r, s);
    }

    public synchronized void start() {
        super.start();
        Affinity.bindThreadToCpu(ContextUtil.getContext(), this);
    }
}
