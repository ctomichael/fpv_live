package dji.midware.transfer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseLongArray;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.transfer.base.FileTransferBase;
import dji.midware.transfer.base.FileTransferListener;
import dji.midware.transfer.base.FileTransferStrategies;
import dji.midware.transfer.base.FileTransferTask;
import dji.midware.transfer.base.ITransferFileObject;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EXClassNullAway
public class FileTransferFirmware extends FileTransferBase {
    /* access modifiers changed from: private */
    public volatile int firmwareIndex;
    /* access modifiers changed from: private */
    public final SparseLongArray firmwareLengthMap = new SparseLongArray();
    /* access modifiers changed from: private */
    public int firmwareNum;
    /* access modifiers changed from: private */
    public long mAckCount;
    /* access modifiers changed from: private */
    public FirmTransferListener mCallback;
    /* access modifiers changed from: private */
    public long mExceptionAckCount;
    private FileTransferListener mFileTransferListener = new FileTransferListener() {
        /* class dji.midware.transfer.FileTransferFirmware.AnonymousClass1 */

        public void onStart() {
            FileTransferFirmware.access$008(FileTransferFirmware.this);
        }

        public void onProgress(int progress, int total) {
            if (FileTransferFirmware.this.firmwareIndex < 0 || FileTransferFirmware.this.firmwareIndex >= FileTransferFirmware.this.firmwareLengthMap.size()) {
                new IndexOutOfBoundsException(FileTransferFirmware.this.TAG + " IndexOutOfBoundsException:firmwareIndex " + FileTransferFirmware.this.firmwareIndex + ",size " + FileTransferFirmware.this.firmwareNum);
                return;
            }
            int currentProgress = FileTransferFirmware.this.calculateProgress(progress, total);
            if (currentProgress > FileTransferFirmware.this.totalProgress) {
                int unused = FileTransferFirmware.this.totalProgress = currentProgress;
                if (FileTransferFirmware.this.checkCallback()) {
                    FileTransferFirmware.this.mCallback.onUploadProgress(FileTransferFirmware.this.totalProgress);
                }
            }
        }

        public void onRateUpdate(float rate) {
            if (FileTransferFirmware.this.checkCallback()) {
                FileTransferFirmware.this.mCallback.onUploadRate(rate);
            }
        }

        public void onSuccess(FileTransferTask task) {
            if (FileTransferFirmware.this.checkCallback()) {
                FileTransferFirmware.this.mCallback.onUploadSuccess(FileTransferFirmware.this.firmwareIndex);
            }
            long unused = FileTransferFirmware.this.mSendCount = FileTransferFirmware.this.mSendCount + task.getSendCount();
            long unused2 = FileTransferFirmware.this.mReSendCount = FileTransferFirmware.this.mReSendCount + task.getResendCount();
            long unused3 = FileTransferFirmware.this.mAckCount = FileTransferFirmware.this.mAckCount + task.getAckCount();
            long unused4 = FileTransferFirmware.this.mExceptionAckCount = FileTransferFirmware.this.mExceptionAckCount + task.getExceptionAckCount();
            if (FileTransferFirmware.this.firmwareIndex + 1 >= FileTransferFirmware.this.firmwareNum) {
                int seconds = (int) ((System.currentTimeMillis() - FileTransferFirmware.this.startTime) / 1000);
                int speed = (int) (FileTransferFirmware.this.getTotalLength() / ((long) (seconds * 1024)));
                float lossRate = FileTransferFirmware.this.mSendCount == 0 ? 0.0f : (((float) FileTransferFirmware.this.mReSendCount) / ((float) FileTransferFirmware.this.mSendCount)) * 100.0f;
                float exceptionAckRate = FileTransferFirmware.this.mAckCount == 0 ? 0.0f : (((float) FileTransferFirmware.this.mExceptionAckCount) / ((float) FileTransferFirmware.this.mAckCount)) * 100.0f;
                if (FileTransferFirmware.this.checkCallback()) {
                    int unused5 = FileTransferFirmware.this.totalProgress = 100;
                    FileTransferFirmware.this.mCallback.onUploadProgress(FileTransferFirmware.this.totalProgress);
                    FileTransferFirmware.this.mCallback.onUploadComplete(seconds, speed, lossRate, exceptionAckRate);
                }
            }
        }

        public void onFailure(FileTransferTask task, String info, Ccode code) {
            FileTransferFirmware.this.stopTransfer();
            if (FileTransferFirmware.this.checkCallback()) {
                FileTransferFirmware.this.mCallback.onUploadFailed(info, code);
            }
        }
    };
    private List<String> mFirmwares;
    /* access modifiers changed from: private */
    public long mReSendCount;
    /* access modifiers changed from: private */
    public long mSendCount;
    private FileTransferStrategies mStrategy = FileTransferStrategies.APP_AOA_RC_WIFI_UAV;
    private byte[] parameter = new byte[1];
    /* access modifiers changed from: private */
    public long startTime;
    private long totalLength;
    /* access modifiers changed from: private */
    public int totalProgress;

    static /* synthetic */ int access$008(FileTransferFirmware x0) {
        int i = x0.firmwareIndex;
        x0.firmwareIndex = i + 1;
        return i;
    }

    public FileTransferFirmware(DeviceType type, int receiveId) {
        super(type, receiveId);
    }

    public void setFileTransferStrategies(FileTransferStrategies strategy) {
        this.mStrategy = strategy;
    }

    public boolean initFirmwareList(List<String> firmwares, @NonNull FirmTransferListener callback) {
        this.mFirmwares = new ArrayList();
        this.mCallback = callback;
        if (firmwares != null) {
            int index = 0;
            while (index < firmwares.size()) {
                String filePath = firmwares.get(index);
                File file = new File(filePath);
                if (checkCallback()) {
                    this.mCallback.initFirmware(index, file);
                }
                if (file.exists()) {
                    long firmwareLength = file.length();
                    this.totalLength += firmwareLength;
                    this.mFirmwares.add(filePath);
                    this.firmwareLengthMap.put(index, firmwareLength);
                    index++;
                } else if (!checkCallback()) {
                    return false;
                } else {
                    this.mCallback.onUploadFailed(file.getName() + " not exists ", Ccode.PARAM_NOT_AVAILABLE);
                    return false;
                }
            }
            return true;
        } else if (!checkCallback()) {
            return false;
        } else {
            this.mCallback.onUploadFailed("firmwares list is empty", Ccode.PARAM_NOT_AVAILABLE);
            return false;
        }
    }

    public long getTotalLength() {
        return this.totalLength;
    }

    public void startUpload() {
        this.startTime = System.currentTimeMillis();
        Arrays.fill(this.parameter, (byte) 1);
        if (this.mFirmwares != null && this.mFirmwares.size() != 0) {
            this.firmwareNum = this.mFirmwares.size();
            this.firmwareIndex = -1;
            this.mSendCount = 0;
            this.mReSendCount = 0;
            this.mAckCount = 0;
            this.mExceptionAckCount = 0;
            for (String firmware : this.mFirmwares) {
                File file = new File(firmware);
                if (this.mFirmwares.indexOf(firmware) == 0 && checkCallback()) {
                    this.mCallback.onUploadStart(this.firmwareNum, (int) (this.totalLength / 1048576));
                    this.mCallback.onUploadProgress(0);
                }
                startTransfer(file, this.mFileTransferListener);
            }
        }
    }

    private void startTransfer(@NonNull File file, @Nullable FileTransferListener listener) {
        startTransfer(file, ITransferFileObject.CommonTransferFileType.FIRMWARE, this.parameter, listener);
    }

    public void startTransfer(@NonNull File file, @NonNull ITransferFileObject.CommonTransferFileType fileType, @Nullable byte[] parameter2, @Nullable FileTransferListener listener) {
        FileTransferTask task = new FileTransferTask();
        task.setReceiver(this.mDeviceType, this.mReceiveId).setTransferListener(this).setFileTransferStrategies(this.mStrategy).setTransferData(file, fileType, parameter2);
        addTask(task, listener);
    }

    public void stopTransfer() {
        stopAllTask();
    }

    public int getTotalProgress() {
        return this.totalProgress;
    }

    /* access modifiers changed from: private */
    public int calculateProgress(int progress, int total) {
        long totalOffset = (long) (((float) this.firmwareLengthMap.get(this.firmwareIndex)) * (((float) progress) / ((float) total)));
        int tempIndex = this.firmwareIndex;
        while (tempIndex > 0) {
            tempIndex--;
            totalOffset += this.firmwareLengthMap.get(tempIndex);
        }
        return (int) ((((float) totalOffset) * 100.0f) / ((float) this.totalLength));
    }

    /* access modifiers changed from: private */
    public boolean checkCallback() {
        return this.mCallback != null;
    }
}
