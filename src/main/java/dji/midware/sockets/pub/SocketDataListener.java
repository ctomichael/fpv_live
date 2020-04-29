package dji.midware.sockets.pub;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface SocketDataListener {
    void checkRecvOver();

    void parse();
}
