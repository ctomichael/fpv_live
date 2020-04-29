package dji.midware.parser.plugins;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import dji.midware.parser.plugins.DJIPluginRingBufferParser;
import dji.midware.usb.P3.RecvBufferEvent;

public class DisruptorAoaBufferParser extends DJIPluginRingBufferParser implements EventHandler<RecvBufferEvent> {
    private RingBuffer<RecvBufferEvent> mRingBuffer;

    public DisruptorAoaBufferParser(int bufferSize, DJIRingBufferModel bufferModel, DJIPluginRingBufferParser.DJIRingBufferParserListener listener) {
        super(bufferSize, bufferModel, listener);
    }

    public void setRingBuffer(RingBuffer<RecvBufferEvent> ringBuffer) {
        this.mRingBuffer = ringBuffer;
    }

    public void onEvent(RecvBufferEvent event, long sequence, boolean endOfBatch) {
        super.parse(event.getBuffer(), 0, event.getLength());
    }

    public void parse(byte[] buffer, int offset, int count) {
        if (this.mRingBuffer != null) {
            long sequence = this.mRingBuffer.next();
            try {
                this.mRingBuffer.get(sequence).setBuffer(buffer, count);
            } finally {
                this.mRingBuffer.publish(sequence);
            }
        }
    }

    public void shutDown() {
        super.shutDown();
    }
}
