package dji.midware.sockets.pub;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class SysRcvBufferBean {
    private static SysRcvBufferBean instance = null;
    private byte[] fullBuffer = new byte[0];

    public static synchronized SysRcvBufferBean getInstance() {
        SysRcvBufferBean sysRcvBufferBean;
        synchronized (SysRcvBufferBean.class) {
            if (instance == null) {
                instance = new SysRcvBufferBean();
            }
            sysRcvBufferBean = instance;
        }
        return sysRcvBufferBean;
    }

    public synchronized void put(byte[] buffer) {
        if (this.fullBuffer.length + buffer.length > 3145728) {
            DJILogHelper.getInstance().LOGD("", "buffer 长度太大了 " + this.fullBuffer.length, true, false);
            this.fullBuffer = buffer;
        } else {
            this.fullBuffer = BytesUtil.arrayComb(this.fullBuffer, buffer);
        }
    }

    public synchronized byte[] get() {
        return this.fullBuffer;
    }

    public synchronized void remove(int len) {
        if (len > this.fullBuffer.length) {
            DJILogHelper.getInstance().LOGD("", "len大过了buffer的长度 ");
        } else {
            this.fullBuffer = BytesUtil.arrayRemove(this.fullBuffer, len);
        }
    }

    public synchronized void clear() {
        this.fullBuffer = new byte[0];
    }
}
