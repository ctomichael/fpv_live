package dji.midware.data.manager.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.packages.P3.RecvPack;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.data.queue.P3.QueueBase;

@EXClassNullAway
public class BlockUtils {
    @Deprecated
    public static RecvPack synSendCmd(SendPack sendPack) {
        if (!ServiceManager.getInstance().isConnected()) {
            sendPack.bufferObject.willRepeat(false);
            sendPack.bufferObject.noUsed();
            RecvPack recvPack = RecvPack.obtain(null);
            recvPack.ccode = Ccode.NOCONNECT.value();
            return recvPack;
        }
        sendPack.bufferObject.willRepeat(true);
        RecvPack recvPack2 = block_GetResponse(ServiceManager.getInstance().getQueue(sendPack.cmdSet).addMsg(sendPack), sendPack, sendPack.timeOut);
        if (recvPack2 != null) {
            sendPack.bufferObject.willRepeat(false);
            sendPack.bufferObject.noUsed();
            return recvPack2;
        }
        sendPack.repeatTimes--;
        if (sendPack.repeatTimes > 0) {
            return synSendCmd(sendPack);
        }
        sendPack.bufferObject.willRepeat(false);
        sendPack.bufferObject.noUsed();
        RecvPack recvPack3 = RecvPack.obtain(null);
        recvPack3.ccode = Ccode.TIMEOUT.value();
        return recvPack3;
    }

    public static void asynSendCmd(SendPack sendPack) {
        ServiceManager.getInstance().sendmessage(sendPack);
    }

    private static RecvPack block_GetResponse(QueueBase.Msg msg, SendPack sendPack, int timeOut) {
        synchronized (msg) {
            try {
                ServiceManager.getInstance().sendmessage(sendPack);
                msg.wait((long) timeOut);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!msg.isResponse) {
            return null;
        }
        return (RecvPack) msg.pack;
    }
}
