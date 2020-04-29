package dji.midware.transfer.base;

import java.util.List;

public interface IPacketHandler {
    void destroy();

    void handleFullQueue();

    void handlePackAckReceive(FileTransferTask fileTransferTask, int i, int i2, List<Integer> list);

    void sendPack(FileTransferTask fileTransferTask, int i, byte[] bArr, boolean z);
}
