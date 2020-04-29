package dji.midware.transfer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.transfer.base.FileTransferBase;
import dji.midware.transfer.base.FileTransferListener;
import dji.midware.transfer.base.FileTransferTask;
import dji.midware.transfer.base.ITransferFileObject;
import dji.midware.util.BytesUtil;
import java.io.File;

@EXClassNullAway
public class FileTransferCamera extends FileTransferBase {
    private static volatile FileTransferCamera mInstance;

    private FileTransferCamera() {
        super(DeviceType.CAMERA);
    }

    public static synchronized FileTransferCamera getInstance() {
        FileTransferCamera fileTransferCamera;
        synchronized (FileTransferCamera.class) {
            if (mInstance == null) {
                mInstance = new FileTransferCamera();
            }
            fileTransferCamera = mInstance;
        }
        return fileTransferCamera;
    }

    public void startTransfer(@NonNull File file, @NonNull ITransferFileObject.CommonTransferFileType fileType, int fileIndex, int groupType, @Nullable FileTransferListener listener) {
        startTransfer(file, fileType, getParameterBytes(fileIndex, groupType), listener);
    }

    public void startTransfer(@NonNull File file, @NonNull ITransferFileObject.CommonTransferFileType fileType, @Nullable byte[] parameter, @Nullable FileTransferListener listener) {
        FileTransferTask task = new FileTransferTask();
        task.setReceiver(this.mDeviceType, this.mReceiveId).setTransferListener(this).setTransferData(file, fileType, parameter);
        addTask(task, listener);
    }

    public void stopTransfer() {
        stopAllTask();
    }

    private byte[] getParameterBytes(int fileIndex, int groupType) {
        byte[] parameter = new byte[9];
        parameter[0] = (byte) ParameterType.INDEX.value();
        int index = 0 + 1;
        parameter[index] = 4;
        int index2 = index + 1;
        System.arraycopy(BytesUtil.getBytes(fileIndex), 0, parameter, index2, 4);
        int index3 = index2 + 4;
        parameter[index3] = (byte) ParameterType.SUBTYPE.value();
        int index4 = index3 + 1;
        parameter[index4] = 1;
        parameter[index4 + 1] = (byte) groupType;
        return parameter;
    }

    private enum ParameterType {
        INDEX(1),
        SUBTYPE(2),
        OTHER(255);
        
        final int mValue;

        private ParameterType(int value) {
            this.mValue = value;
        }

        /* access modifiers changed from: package-private */
        public int value() {
            return this.mValue;
        }
    }
}
