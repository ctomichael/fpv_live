package dji.midware.media;

import android.media.AudioRecord;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.manager.Dpad.DpadBoardManager;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.util.save.StreamDataObserver;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

@EXClassNullAway
public class DJIAudioRecordWrapper {
    private static final String AudioName = "/sdcard/rawpcm.raw";
    static final int BIT_RATE = 64000;
    private static final String NewAudioName = "/sdcard/new.wav";
    public static final int SAMPLE_RATE = 44100;
    private static final String TAG = "DJIAudioRecordWrapper";
    static final int VOLUME_REFRESH_INTERVAL = 200;
    private static DJIAudioRecordWrapper instance;
    /* access modifiers changed from: private */
    public volatile boolean cancel = true;
    private ByteBuffer dataBuffer;
    private int dataSize;
    FileOutputStream fos = null;
    private int frequency;
    private List<DJIAudioRecordListenter> listenerList = new LinkedList();
    private Object lock = new Object();
    private DpadBoardManager mBoardManager;
    private AudioRecord recorder;
    private Thread thread;
    /* access modifiers changed from: private */
    public volatile double volume;
    private Thread volumeThread;
    private int writeTestFileCount = 0;

    public interface DJIAudioRecordListenter {
        void onAudioDataRead(ByteBuffer byteBuffer, int i);

        void onVolumeRefresh(double d);
    }

    public static synchronized DJIAudioRecordWrapper getInstance() {
        DJIAudioRecordWrapper dJIAudioRecordWrapper;
        synchronized (DJIAudioRecordWrapper.class) {
            if (instance == null) {
                instance = new DJIAudioRecordWrapper();
            }
            dJIAudioRecordWrapper = instance;
        }
        return dJIAudioRecordWrapper;
    }

    public boolean addListener(DJIAudioRecordListenter listener) {
        if (listener == null) {
            return false;
        }
        synchronized (this.listenerList) {
            this.listenerList.add(listener);
        }
        if (!isRunning() && listener != null) {
            if (!start()) {
                this.listenerList.clear();
                return false;
            } else if (DpadProductManager.getInstance().isCrystalSky()) {
                if (this.mBoardManager == null) {
                    this.mBoardManager = new DpadBoardManager();
                }
                this.mBoardManager.handleRecordStart();
            }
        }
        return true;
    }

    public void clearListeners() {
        synchronized (this.listenerList) {
            this.listenerList.clear();
        }
        if (isRunning()) {
            stop();
        }
    }

    public void removeListener(DJIAudioRecordListenter listener) {
        synchronized (this.listenerList) {
            this.listenerList.remove(listener);
        }
        if (this.listenerList.isEmpty() && isRunning()) {
            stop();
        }
    }

    private void invokeOnAudioDataRead(ByteBuffer buf, int length) {
        synchronized (this.listenerList) {
            for (DJIAudioRecordListenter listener : this.listenerList) {
                buf.position(length);
                buf.flip();
                listener.onAudioDataRead(buf, length);
            }
        }
    }

    /* access modifiers changed from: private */
    public void invokeOnVolumeRefresh(double volumn) {
        synchronized (this.listenerList) {
            for (DJIAudioRecordListenter listener : this.listenerList) {
                listener.onVolumeRefresh(volumn);
            }
        }
    }

    private boolean init(int frequency2) {
        this.frequency = frequency2;
        int bufferSize = AudioRecord.getMinBufferSize(frequency2, 16, 2);
        this.dataBuffer = ByteBuffer.allocateDirect(bufferSize);
        this.recorder = new AudioRecord(1, frequency2, 16, 2, bufferSize);
        if (this.recorder.getState() != 1) {
            return false;
        }
        this.dataSize = bufferSize / 2;
        return true;
    }

    public boolean isRunning() {
        return !this.cancel;
    }

    public boolean isRecorderInitiated() {
        return this.recorder != null && this.recorder.getState() == 1;
    }

    public boolean isRecorderRecording() {
        return this.recorder != null && this.recorder.getRecordingState() == 3;
    }

    private boolean start() {
        boolean z = false;
        synchronized (this.lock) {
            if (!init(44100)) {
                this.recorder.release();
            } else {
                this.recorder.startRecording();
                this.cancel = false;
                this.thread = new Thread(new Runnable() {
                    /* class dji.midware.media.DJIAudioRecordWrapper.AnonymousClass1 */

                    public void run() {
                        DJIAudioRecordWrapper.this.recordThread();
                    }
                }, TAG);
                this.thread.start();
                this.volumeThread = new Thread(new Runnable() {
                    /* class dji.midware.media.DJIAudioRecordWrapper.AnonymousClass2 */

                    public void run() {
                        while (!DJIAudioRecordWrapper.this.cancel) {
                            try {
                                DJIAudioRecordWrapper.this.invokeOnVolumeRefresh(DJIAudioRecordWrapper.this.volume);
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, "DJIAudioRecordWrapper2");
                this.volumeThread.start();
                z = true;
            }
        }
        return z;
    }

    public boolean isRecorderPermissionGet() {
        if (isRunning()) {
            return true;
        }
        boolean init = init(44100);
        this.recorder.release();
        return init;
    }

    public void recordThread() {
        while (!this.cancel) {
            synchronized (this.lock) {
                if (this.recorder != null) {
                    this.dataBuffer.clear();
                    this.dataBuffer.position(0);
                    this.dataBuffer.limit(this.dataBuffer.capacity());
                    int bufferReadResult = this.recorder.read(this.dataBuffer, this.dataBuffer.capacity());
                    if (bufferReadResult > 0) {
                        StreamDataObserver.getInstance(StreamDataObserver.ObservingPoint.AudioRecord).onDataRecv(StreamDataObserver.ObservingContext.ByteRate, (long) bufferReadResult).onDataRecv(StreamDataObserver.ObservingContext.FrameRate, 1);
                        invokeOnAudioDataRead(this.dataBuffer, bufferReadResult);
                        this.dataBuffer.position(bufferReadResult);
                        this.dataBuffer.flip();
                        long v = 0;
                        for (int i = 0; i < bufferReadResult / 2; i++) {
                            int sample = (short) ((this.dataBuffer.get() << 8) | (this.dataBuffer.get() & 255));
                            v += (long) (sample * sample);
                        }
                        this.volume = 10.0d * Math.log10(((double) v) / ((double) bufferReadResult));
                    } else if (bufferReadResult < 0) {
                        Log.w(TAG, "Error calling recorder.read: " + bufferReadResult);
                        if (bufferReadResult == -3) {
                            stop();
                            if (!this.listenerList.isEmpty()) {
                            }
                        }
                    }
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    DJILogHelper.getInstance().LOGE(TAG, "recordThread: the recorder is null!");
                }
            }
        }
    }

    public void stop() {
        synchronized (this.lock) {
            this.cancel = true;
            if (this.recorder != null) {
                if (this.recorder.getRecordingState() == 3) {
                    this.recorder.stop();
                    this.recorder.release();
                }
                this.recorder = null;
            }
            if (this.thread != null) {
                this.thread = null;
            }
            if (this.fos != null) {
                try {
                    this.fos.flush();
                    this.fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (this.mBoardManager != null) {
                this.mBoardManager.handleRecordStop();
            }
        }
    }

    private DJIAudioRecordWrapper() {
    }

    public byte[] ShortArray2ByteArray(short[] buffer, int bufferReadResult) {
        byte[] bufByte = new byte[(bufferReadResult * 2)];
        for (int i = bufferReadResult - 1; i >= 0; i--) {
            short j = (short) (buffer[i] >> 2);
            bufByte[i] = (byte) (j & 255);
            bufByte[i + 1] = (byte) ((65280 & j) >> 8);
        }
        return bufByte;
    }

    private void copyWaveFile(String inFilename, String outFilename) {
        FileOutputStream out;
        long j = 0 + 44;
        long byteRate = (long) 88200;
        byte[] data = new byte[(this.dataSize * 2)];
        try {
            FileInputStream fileInputStream = new FileInputStream(inFilename);
            try {
                out = new FileOutputStream(outFilename);
            } catch (FileNotFoundException e) {
                e = e;
                e.printStackTrace();
            } catch (IOException e2) {
                e = e2;
                e.printStackTrace();
            }
            try {
                long totalAudioLen = fileInputStream.getChannel().size();
                WriteWaveFileHeader(out, totalAudioLen, totalAudioLen + 44, 44100, 1, byteRate);
                while (fileInputStream.read(data) != -1) {
                    out.write(data);
                }
                fileInputStream.close();
                out.close();
            } catch (FileNotFoundException e3) {
                e = e3;
                e.printStackTrace();
            } catch (IOException e4) {
                e = e4;
                e.printStackTrace();
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
        } catch (IOException e6) {
            e = e6;
            e.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate, int channels, long byteRate) throws IOException {
        out.write(new byte[]{82, 73, 70, 70, (byte) ((int) (255 & totalDataLen)), (byte) ((int) ((totalDataLen >> 8) & 255)), (byte) ((int) ((totalDataLen >> 16) & 255)), (byte) ((int) ((totalDataLen >> 24) & 255)), 87, 65, 86, 69, 102, 109, 116, 32, Tnaf.POW_2_WIDTH, 0, 0, 0, 1, 0, (byte) channels, 0, (byte) ((int) (255 & longSampleRate)), (byte) ((int) ((longSampleRate >> 8) & 255)), (byte) ((int) ((longSampleRate >> 16) & 255)), (byte) ((int) ((longSampleRate >> 24) & 255)), (byte) ((int) (255 & byteRate)), (byte) ((int) ((byteRate >> 8) & 255)), (byte) ((int) ((byteRate >> 16) & 255)), (byte) ((int) ((byteRate >> 24) & 255)), 4, 0, Tnaf.POW_2_WIDTH, 0, 100, 97, 116, 97, (byte) ((int) (255 & totalAudioLen)), (byte) ((int) ((totalAudioLen >> 8) & 255)), (byte) ((int) ((totalAudioLen >> 16) & 255)), (byte) ((int) ((totalAudioLen >> 24) & 255))}, 0, 44);
    }
}
