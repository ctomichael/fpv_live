package dji.midware.wsbridge;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.thirdparty.org.java_websocket.client.WebSocketClient;
import dji.thirdparty.org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingDeque;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class DJIWebSocketClient extends WebSocketClient {
    private static final int MAX_BUFFER_SIZE = 100;
    private ByteBuffer mLast;
    private LinkedBlockingDeque<ByteBuffer> mQueue = new LinkedBlockingDeque<>(100);

    public DJIWebSocketClient(URI uri) {
        super(uri);
        DJILog.d("WSOCKET", "Starting...", new Object[0]);
    }

    public ByteBuffer read() {
        if (this.mLast == null || (this.mLast.remaining() <= 0 && !this.mQueue.isEmpty())) {
            try {
                this.mLast = this.mQueue.take();
            } catch (InterruptedException e) {
                DJILog.d("WSOCKET", "onError " + e, new Object[0]);
            }
        }
        return this.mLast;
    }

    public void onOpen(ServerHandshake serverHandshake) {
        DJILog.d("WSOCKET", "onOpen ", new Object[0]);
        send("OPEN!");
        UsbAccessoryService.getInstance().startThreads();
        EventBus.getDefault().post(DataEvent.ConnectOK);
    }

    public void onMessage(String s) {
    }

    public void onClose(int i, String s, boolean b) {
        DJILog.d("WSOCKET", "onClose " + s, new Object[0]);
        BridgeWSConnectionManager.getInstance().reconnect();
        DJILog.saveConnectDebug("dji web socket post connect lose!");
        EventBus.getDefault().post(DataEvent.ConnectLose);
    }

    public void onError(Exception e) {
        DJILog.d("WSOCKET", "onError " + e, new Object[0]);
    }

    public void onMessage(ByteBuffer byteBuffer) {
        if (this.mQueue != null) {
            this.mQueue.add(byteBuffer);
        }
    }
}
