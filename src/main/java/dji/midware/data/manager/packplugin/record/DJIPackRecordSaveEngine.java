package dji.midware.data.manager.packplugin.record;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import dji.midware.data.manager.packplugin.record.CmdPackPlugin;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ThreadFactory;

public class DJIPackRecordSaveEngine implements EventHandler<PluginPackRecordEvent> {
    ThreadFactory disruptorThreadFactory = DJIPackRecordSaveEngine$$Lambda$0.$instance;
    private FileOutputStream mFileOutputStream;
    private Disruptor<PluginPackRecordEvent> mRecvDisruptor;

    static final /* synthetic */ Thread lambda$new$0$DJIPackRecordSaveEngine(Runnable r) {
        Thread ret = new Thread(r, "pack-record-disruptor");
        ret.setPriority(8);
        return ret;
    }

    DJIPackRecordSaveEngine(String saveFilePath) {
        if (saveFilePath != null) {
            try {
                this.mFileOutputStream = new FileOutputStream(saveFilePath);
            } catch (FileNotFoundException e) {
            }
        }
        this.mRecvDisruptor = new Disruptor<>(new PackRecordEventFactory(), 1024, this.disruptorThreadFactory, ProducerType.MULTI, new YieldingWaitStrategy());
        this.mRecvDisruptor.handleEventsWith(this);
        this.mRecvDisruptor.start();
    }

    public void onPackComing(CmdPackPlugin.DJIV1Pack4Save pack4Save) {
        RingBuffer<PluginPackRecordEvent> ringBuffer = this.mRecvDisruptor.getRingBuffer();
        long sequence = ringBuffer.next();
        try {
            ringBuffer.get(sequence).setPack4Save(pack4Save);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    public void onEvent(PluginPackRecordEvent event, long sequence, boolean endOfBatch) throws Exception {
        if (this.mFileOutputStream != null) {
            event.getPack4Save().writeDelimitedTo(this.mFileOutputStream);
        }
    }

    public void onDestroy() {
        this.mRecvDisruptor.shutdown();
        try {
            if (this.mFileOutputStream != null) {
                this.mFileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final class PackRecordEventFactory implements EventFactory<PluginPackRecordEvent> {
        private PackRecordEventFactory() {
        }

        public PluginPackRecordEvent newInstance() {
            return new PluginPackRecordEvent();
        }
    }
}
