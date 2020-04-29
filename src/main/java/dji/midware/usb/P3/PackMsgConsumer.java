package dji.midware.usb.P3;

import android.os.Handler;
import android.os.Message;
import com.lmax.disruptor.EventHandler;
import dji.log.DJILog;
import dji.log.DJILogUtils;
import dji.midware.data.config.P3.DJICmdSetBase;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.manager.P3.DataBaseProcessor;
import dji.midware.data.packages.P3.RecvPack;
import dji.midware.interfaces.DJIDataAsyncListener;
import dji.midware.reflect.MidwareInjectManager;
import dji.midware.reflect.MidwareToP3Injectable;
import dji.publics.DJIExecutor;
import java.lang.reflect.Method;

public class PackMsgConsumer implements EventHandler<PackMsgEvent> {
    private static final int DELAY_TIME = 100;
    private static final int MSG_EVENTBUS_POST_TOO_LONG = 1;
    private DataBaseProcessor dataBaseProcessor = DataBaseProcessor.getInstance();
    /* access modifiers changed from: private */
    public Thread mCurrentThread;
    private Handler mHandler = new Handler(DJIExecutor.getLooper(), new Handler.Callback() {
        /* class dji.midware.usb.P3.PackMsgConsumer.AnonymousClass1 */

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (PackMsgConsumer.this.mCurrentThread != null) {
                        DJILog.logWriteD(AoaReportHelper.TAG_CONNECT_DEBUG, "eventbus consume time too long, name: " + PackMsgConsumer.this.mCurrentThread.getName() + "\nstack trace: " + DJILogUtils.getThreadStack(PackMsgConsumer.this.mCurrentThread), AoaReportHelper.TAG_CONNECT_DEBUG, new Object[0]);
                        break;
                    }
                    break;
            }
            return false;
        }
    });
    private MidwareToP3Injectable mToP3Injectable = MidwareInjectManager.getMidwareToP3Injectable();

    public void onEvent(PackMsgEvent event, long sequence, boolean endOfBatch) throws Exception {
        if (this.mCurrentThread == null) {
            this.mCurrentThread = Thread.currentThread();
        }
        setMsg(event.getPack());
    }

    private void setMsg(RecvPack pack) {
        if (pack.cmdSetObj != null && pack.cmdSetObj.cmdIdClass() != null) {
            DJICmdSetBase cls = pack.cmdSetObj.cmdIdClass();
            boolean isBlock = cls.isBlock(pack.cmdId);
            boolean isMix = cls.isMix(pack.cmdId);
            if (isBlock || isMix) {
                this.dataBaseProcessor.setMsg(pack);
            }
            if (!isBlock) {
                try {
                    DataBase dataBase = cls.getDataBase(pack.cmdId);
                    if (dataBase == null) {
                        Class dataModel = cls.getDataModel(pack.cmdId);
                        if (dataModel == null) {
                            dataModel = Class.forName(cls.getDataModelName(pack.senderType, pack.senderId, pack.cmdId));
                        }
                        Method getInstance = dataModel.getMethod("getInstance", new Class[0]);
                        getInstance.setAccessible(true);
                        dataBase = (DataBase) getInstance.invoke(dataModel, new Object[0]);
                    }
                    if (dataBase instanceof DJIDataAsyncListener) {
                        ((DJIDataAsyncListener) dataBase).stop();
                        return;
                    }
                    if (isInnerOrDebug()) {
                        this.mHandler.sendEmptyMessageDelayed(1, 100);
                    }
                    dataBase.outerSetPushRecPack(pack);
                    if (isInnerOrDebug()) {
                        this.mHandler.removeMessages(1);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private boolean isInnerOrDebug() {
        return this.mToP3Injectable != null && (this.mToP3Injectable.isDevelopPackage() || this.mToP3Injectable.isInnerPackage());
    }
}
