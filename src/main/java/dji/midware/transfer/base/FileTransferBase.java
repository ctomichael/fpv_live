package dji.midware.transfer.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.transfer.FileTransferLog;
import dji.midware.transfer.base.ITransferFileObject;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public abstract class FileTransferBase implements FileTransferListener {
    private static final String LOCK = "LOCK";
    private static final String UNLOCK = "UNLOCK";
    /* access modifiers changed from: protected */
    public final String TAG;
    /* access modifiers changed from: private */
    public FileTransferTask mCurrentTask;
    protected final DeviceType mDeviceType;
    private final String[] mLock;
    protected volatile FileTransferListener mOuterListener;
    protected final int mReceiveId;
    private ExecutorService mTaskExecutor;
    private HashMap<FileTransferTask, Future> mTaskMap;

    public abstract void startTransfer(@NonNull File file, @NonNull ITransferFileObject.CommonTransferFileType commonTransferFileType, @Nullable byte[] bArr, @Nullable FileTransferListener fileTransferListener);

    public abstract void stopTransfer();

    protected class TaskRunnable implements Runnable {
        private FileTransferListener mListener;
        private final FileTransferTask mTask;

        public TaskRunnable(FileTransferTask task, FileTransferListener listener) {
            this.mTask = task;
            this.mListener = listener;
        }

        public void run() {
            FileTransferBase.this.mOuterListener = this.mListener;
            FileTransferTask unused = FileTransferBase.this.mCurrentTask = this.mTask;
            this.mTask.start();
            FileTransferBase.this.lock();
            FileTransferLog.d("Task Finish");
        }
    }

    protected FileTransferBase(DeviceType type) {
        this(type, 0);
    }

    protected FileTransferBase(DeviceType type, int receiveId) {
        this.TAG = getClass().getSimpleName();
        this.mLock = new String[]{LOCK};
        this.mDeviceType = type;
        this.mReceiveId = receiveId;
        this.mTaskExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(1024));
        this.mTaskMap = new HashMap<>();
    }

    /* access modifiers changed from: protected */
    public final synchronized void addTask(FileTransferTask task, FileTransferListener listener) {
        if (this.mTaskExecutor.isTerminated() || this.mTaskExecutor.isShutdown()) {
            FileTransferLog.d("transfer task executor state invalid");
        } else {
            this.mTaskMap.put(task, this.mTaskExecutor.submit(new TaskRunnable(task, listener)));
        }
    }

    /* access modifiers changed from: protected */
    public final synchronized void stopAllTask() {
        FileTransferLog.d("stop all task size=" + this.mTaskMap.size());
        if (this.mTaskMap.size() == 0 || this.mTaskExecutor.isTerminated() || this.mTaskExecutor.isShutdown()) {
            FileTransferLog.d("stop all task state invalid");
        } else {
            Iterator<Future> it2 = this.mTaskMap.values().iterator();
            while (it2.hasNext()) {
                FileTransferLog.d("stop a task result=" + it2.next().cancel(false));
            }
            if (this.mCurrentTask != null && this.mTaskMap.containsKey(this.mCurrentTask)) {
                FileTransferLog.d("stop current task");
                this.mCurrentTask.stop("stop reason:cancel all task");
            }
            this.mTaskMap.clear();
            FileTransferLog.d("clear all task size=" + this.mTaskMap.size());
        }
    }

    /* access modifiers changed from: private */
    public void lock() {
        synchronized (this.mLock) {
            FileTransferLog.d("task execute lock...");
            this.mLock[0] = LOCK;
            while (this.mLock[0].equals(LOCK)) {
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void unlock() {
        synchronized (this.mLock) {
            FileTransferLog.d("task execute unlock...");
            if (this.mLock[0].equals(UNLOCK)) {
                FileTransferLog.e("unlock error");
                return;
            }
            this.mLock[0] = UNLOCK;
            this.mLock.notifyAll();
        }
    }

    public void onStart() {
        if (this.mOuterListener != null) {
            this.mOuterListener.onStart();
        }
    }

    public void onRateUpdate(float rate) {
        if (this.mOuterListener != null) {
            this.mOuterListener.onRateUpdate(rate);
        }
    }

    public void onProgress(int progress, int total) {
        if (this.mOuterListener != null) {
            this.mOuterListener.onProgress(progress, total);
        }
    }

    public void onSuccess(FileTransferTask task) {
        task.destroy();
        this.mTaskMap.remove(task);
        this.mCurrentTask = null;
        if (this.mOuterListener != null) {
            this.mOuterListener.onSuccess(task);
            this.mOuterListener = null;
        }
        unlock();
    }

    public void onFailure(FileTransferTask task, String info, Ccode code) {
        task.destroy();
        this.mTaskMap.remove(task);
        this.mCurrentTask = null;
        if (this.mOuterListener != null) {
            this.mOuterListener.onFailure(task, info, code);
            this.mOuterListener = null;
        }
        unlock();
    }
}
