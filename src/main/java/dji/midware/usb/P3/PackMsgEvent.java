package dji.midware.usb.P3;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import dji.midware.data.packages.P3.RecvPack;

public class PackMsgEvent {
    RecvPack mPack;

    public void setPack(RecvPack pack) {
        this.mPack = pack;
    }

    public RecvPack getPack() {
        return this.mPack;
    }

    public static class PackMsgEventFactory implements EventFactory<PackMsgEvent> {
        public PackMsgEvent newInstance() {
            return new PackMsgEvent();
        }
    }

    public static class PackMsgEventProducerWithTranslator {
        private static final EventTranslatorOneArg<PackMsgEvent, RecvPack> TRANSLATOR = new EventTranslatorOneArg<PackMsgEvent, RecvPack>() {
            /* class dji.midware.usb.P3.PackMsgEvent.PackMsgEventProducerWithTranslator.AnonymousClass1 */

            public void translateTo(PackMsgEvent event, long sequence, RecvPack recvPack) {
                event.setPack(recvPack);
            }
        };
        private final RingBuffer<PackMsgEvent> ringBuffer;

        public PackMsgEventProducerWithTranslator(RingBuffer<PackMsgEvent> ringBuffer2) {
            this.ringBuffer = ringBuffer2;
        }

        public void onData(RecvPack bb) {
            this.ringBuffer.publishEvent(TRANSLATOR, bb);
        }
    }
}
