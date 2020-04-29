package dji.midware.data.manager.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.packages.P3.SendPack;

@EXClassNullAway
public interface DJIServiceInterface {
    void destroy();

    boolean isConnected();

    boolean isOK();

    boolean isRemoteOK();

    void onConnect();

    void onDisconnect();

    void pauseParseThread();

    void pauseRecvThread();

    void pauseService(boolean z);

    void resumeParseThread();

    void resumeRecvThread();

    void sendmessage(SendPack sendPack);

    void setDataMode(boolean z);

    void startStream();

    void stopStream();
}
