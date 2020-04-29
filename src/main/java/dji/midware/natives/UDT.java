package dji.midware.natives;

import android.annotation.TargetApi;
import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.wifi.DJIMultiNetworkMgr;

@Keep
@EXClassNullAway
public class UDT {
    public static native void SwUdpClose();

    public static native int SwUdpConnect(String str, int i, int i2, boolean z);

    public static native boolean SwUdpIsConnected();

    public static native boolean SwUdpIsPortOccupied();

    public static native int SwUdpJoyStickSend(byte[] bArr, int i, int i2);

    public static native int SwUdpSend(byte[] bArr, int i, int i2);

    public static native void SwUdpStop();

    public static native synchronized int cleanup();

    public static native int close(int i);

    public static native int connect(int i, String str, String str2);

    public static native int connectWithLocal(int i, String str, String str2, String str3, String str4);

    public static native int recv(int i, byte[] bArr, int i2, int i3, int i4);

    public static native int recvmsg(int i, byte[] bArr, int i2, int i3);

    public static native int send(int i, byte[] bArr, int i2, int i3, int i4);

    public static native int sendmsg(int i, byte[] bArr, int i2, int i3);

    public static native void setSwRecver(Object obj);

    public static native int socket();

    public static native int socketDgram();

    public static native synchronized int startup();

    static {
        try {
            DJILog.d("FPVController", "try to load udt.so", new Object[0]);
            System.loadLibrary("udt");
            System.loadLibrary("udtjni");
        } catch (UnsatisfiedLinkError e) {
            DJILog.e("UDT", "Couldn't load lib", new Object[0]);
        }
    }

    public static void loadLibrary() {
    }

    @TargetApi(23)
    public static void bindSocketToNetwork(int socketfd) {
        DJIMultiNetworkMgr.getInstance().bindSocketWithWIFI(socketfd);
    }
}
