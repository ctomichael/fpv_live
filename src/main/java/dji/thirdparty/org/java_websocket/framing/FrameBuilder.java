package dji.thirdparty.org.java_websocket.framing;

import dji.thirdparty.org.java_websocket.exceptions.InvalidDataException;
import dji.thirdparty.org.java_websocket.framing.Framedata;
import java.nio.ByteBuffer;

public interface FrameBuilder extends Framedata {
    void setFin(boolean z);

    void setOptcode(Framedata.Opcode opcode);

    void setPayload(ByteBuffer byteBuffer) throws InvalidDataException;

    void setTransferemasked(boolean z);
}
