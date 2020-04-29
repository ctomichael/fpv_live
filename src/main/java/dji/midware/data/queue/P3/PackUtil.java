package dji.midware.data.queue.P3;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class PackUtil {
    private static int seq = 0;
    private static int sessionId = 0;

    public static synchronized int getSeq() {
        int i;
        synchronized (PackUtil.class) {
            seq++;
            if (seq == 85) {
                seq++;
            } else if (seq >= 65535) {
                seq = 0;
            }
            i = seq;
        }
        return i;
    }

    public static synchronized int getSessionId() {
        int i;
        synchronized (PackUtil.class) {
            sessionId++;
            if (sessionId == 85) {
                sessionId++;
            } else if (sessionId >= 65535) {
                sessionId = 0;
            }
            i = sessionId;
        }
        return i;
    }

    public static synchronized int sessionId() {
        int i;
        synchronized (PackUtil.class) {
            i = sessionId;
        }
        return i;
    }
}
