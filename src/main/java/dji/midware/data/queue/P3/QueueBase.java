package dji.midware.data.queue.P3;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public abstract class QueueBase {

    public class Msg {
        public boolean isResponse = false;
        public Object pack;

        public Msg() {
        }
    }
}
