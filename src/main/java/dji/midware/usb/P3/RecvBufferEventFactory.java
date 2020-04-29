package dji.midware.usb.P3;

import com.lmax.disruptor.EventFactory;

public class RecvBufferEventFactory implements EventFactory<RecvBufferEvent> {
    public RecvBufferEvent newInstance() {
        return new RecvBufferEvent();
    }
}
