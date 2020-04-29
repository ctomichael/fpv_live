package dji.midware.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.provider.DocumentFile;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.external.DJIExternalStorageController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

@EXClassNullAway
public class FileStreamCopyController {
    private static final int CACHE_NUM = 5;
    private static final int MSG_OBSERVE = 1;
    private static final int MSG_START = 0;
    private static final int MSG_STOP = 2;
    private static final String TAG = "FileStreamCopyControl";
    private byte[] cache = new byte[1024];
    /* access modifiers changed from: private */
    public File inputFile;
    /* access modifiers changed from: private */
    public FileInputStream inputStream;
    /* access modifiers changed from: private */
    public boolean isPaused = false;
    /* access modifiers changed from: private */
    public boolean needRemoveSrcFile = false;
    /* access modifiers changed from: private */
    public Handler observingHandler;
    private HandlerThread observingThread;
    private OutputStream outputStream;
    private int writeNum = 0;

    public FileStreamCopyController(String filePath) {
        this.inputFile = new File(filePath);
    }

    private void setOutputStream(String outputPath) throws IOException {
        File file = new File(outputPath);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        this.outputStream = new FileOutputStream(file);
    }

    private void setOutputStream(FileOutputStream outputStream2) {
        this.outputStream = outputStream2;
    }

    @SuppressLint({"LongLogTag"})
    public void stopThread() {
        if (this.observingHandler != null) {
            this.observingHandler.removeCallbacksAndMessages(null);
            this.observingHandler = null;
        }
        if (this.observingThread != null) {
            if (Build.VERSION.SDK_INT >= 18) {
                this.observingThread.quitSafely();
            } else {
                this.observingThread.quit();
            }
            try {
                this.observingThread.join(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "stopThread: ", e);
            }
            this.observingThread = null;
        }
    }

    public void startThread() {
        stopThread();
        this.observingThread = new HandlerThread("FileStreamCopyController_observingThread");
        this.observingThread.start();
        this.observingHandler = new Handler(this.observingThread.getLooper()) {
            /* class dji.midware.util.FileStreamCopyController.AnonymousClass1 */

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (FileStreamCopyController.this.inputFile.exists()) {
                            try {
                                FileInputStream unused = FileStreamCopyController.this.inputStream = new FileInputStream(FileStreamCopyController.this.inputFile);
                                FileStreamCopyController.this.observingHandler.sendEmptyMessage(1);
                                return;
                            } catch (FileNotFoundException e) {
                                Log.e(FileStreamCopyController.TAG, "start: ", e);
                                e.printStackTrace();
                                return;
                            }
                        } else {
                            sendEmptyMessageDelayed(0, 100);
                            return;
                        }
                    case 1:
                        if (!FileStreamCopyController.this.isPaused) {
                            int copyRst = -1;
                            try {
                                copyRst = FileStreamCopyController.this.observe(false);
                            } catch (IOException e2) {
                                Log.e(FileStreamCopyController.TAG, "handleMessage: MSG_OBSERVE");
                                FileStreamCopyController.this.forceStop();
                            }
                            if (hasMessages(1)) {
                                return;
                            }
                            if (copyRst > 0) {
                                sendEmptyMessage(1);
                                return;
                            } else {
                                sendEmptyMessageDelayed(1, 200);
                                return;
                            }
                        } else {
                            return;
                        }
                    case 2:
                        break;
                    default:
                        return;
                }
                do {
                    try {
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                } while (FileStreamCopyController.this.observe(true) > 0);
                int checkLen = msg.arg1;
                Object checkObj = msg.obj;
                try {
                    if (FileStreamCopyController.this.inputStream == null || FileStreamCopyController.this.inputStream.available() <= 0) {
                        new StopStreamCopyTask(checkObj, checkLen).start();
                    } else {
                        sendMessage(obtainMessage(2, checkLen, 0, checkObj));
                    }
                } catch (IOException e4) {
                    Log.e(FileStreamCopyController.TAG, "handleMessage: MSG_STOP", e4);
                    e4.printStackTrace();
                }
            }
        };
    }

    public void start(OutputStream outputStream2) {
        this.outputStream = outputStream2;
        this.needRemoveSrcFile = false;
        this.isPaused = false;
        this.writeNum = 0;
        startThread();
        this.observingHandler.sendEmptyMessage(0);
    }

    /* access modifiers changed from: private */
    public int observe(boolean needForceFlush) throws IOException {
        if (this.inputStream == null || this.outputStream == null) {
            return -1;
        }
        int rst = this.inputStream.read(this.cache);
        if (rst <= 0) {
            return rst;
        }
        this.outputStream.write(this.cache, 0, rst);
        this.writeNum++;
        if (!needForceFlush && this.writeNum < 5) {
            return rst;
        }
        this.writeNum = 0;
        this.outputStream.flush();
        return rst;
    }

    public void pause() {
        this.isPaused = true;
        if (this.observingHandler != null && this.observingHandler.hasMessages(1)) {
            this.observingHandler.removeMessages(1);
        }
    }

    public void resume() {
        this.isPaused = false;
        if (this.observingHandler != null && !this.observingHandler.hasMessages(1)) {
            this.observingHandler.sendEmptyMessage(1);
        }
    }

    public void stop(DocumentFile df, int checkLen, boolean needRemoveSrcFile2) throws IOException {
        this.needRemoveSrcFile = needRemoveSrcFile2;
        _stop(df, checkLen);
    }

    public void stopAndDelete(DocumentFile df) {
        forceStop();
        if (df != null) {
            df.delete();
        }
    }

    public void stop(RandomAccessFile raf, int checkLen) throws IOException {
        _stop(raf, checkLen);
    }

    public void forceStop() {
        _stop();
    }

    public boolean isRunning() {
        return this.observingHandler != null;
    }

    private void _stop(Object checkFileObj, int checkLen) throws IOException {
        if (this.observingHandler != null) {
            this.observingHandler.sendMessage(this.observingHandler.obtainMessage(2, checkLen, 0, checkFileObj));
        }
    }

    private class StopStreamCopyTask extends Thread {
        private Object checkFileObj;
        private int checkLen;

        public StopStreamCopyTask(Object checkFileObj2, int checkLen2) {
            this.checkFileObj = checkFileObj2;
            this.checkLen = checkLen2;
        }

        public void run() {
            FileStreamCopyController.this._stop();
            try {
                FileInputStream fis = new FileInputStream(FileStreamCopyController.this.inputFile);
                byte[] checkDataBuf = new byte[1024];
                int readLen = 0;
                RandomAccessFile raf = null;
                OutputStream os = null;
                if (this.checkFileObj instanceof RandomAccessFile) {
                    raf = (RandomAccessFile) this.checkFileObj;
                    raf.seek(0);
                } else if (this.checkFileObj instanceof DocumentFile) {
                    os = ServiceManager.getContext().getContentResolver().openOutputStream(((DocumentFile) this.checkFileObj).getUri(), "rw");
                }
                while (readLen < this.checkLen) {
                    int rst = fis.read(checkDataBuf);
                    readLen += rst;
                    if (raf != null) {
                        raf.write(checkDataBuf, 0, rst);
                        raf.seek((long) readLen);
                    }
                    if (os != null) {
                        os.write(checkDataBuf, 0, rst);
                        os.flush();
                    }
                }
                if (raf != null) {
                    raf.close();
                }
                if (os != null) {
                    os.flush();
                    os.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (this.checkFileObj instanceof DocumentFile) {
                    DocumentFile df = (DocumentFile) this.checkFileObj;
                    String name = DJIExternalStorageController.getSimpleName(df);
                    String extension = DJIExternalStorageController.getExtension(FileStreamCopyController.this.inputFile);
                    String srcName = name + "." + extension;
                    if (!srcName.equals(df.getName())) {
                        df.renameTo(srcName);
                    }
                }
                if (FileStreamCopyController.this.needRemoveSrcFile && FileStreamCopyController.this.inputFile.exists()) {
                    FileStreamCopyController.this.inputFile.delete();
                }
            } catch (IOException e) {
                Log.e(FileStreamCopyController.TAG, "run: after stop: stop and check", e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void _stop() {
        stopThread();
        try {
            if (this.inputStream != null) {
                this.inputStream.close();
                this.inputStream = null;
            }
            if (this.outputStream != null) {
                this.outputStream.flush();
                this.outputStream.close();
                this.outputStream = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
