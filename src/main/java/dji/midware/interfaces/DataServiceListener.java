package dji.midware.interfaces;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.queue.P3.Queue;
import dji.midware.data.queue.P3.QueueBase;

@EXClassNullAway
public interface DataServiceListener {
    Queue getQueue(int i);

    QueueBase getQueue(CmdSet cmdSet);
}
