package dji.midware.media.player;

import android.os.Process;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.logic.album.manager.DJIAlbumInterface;
import dji.logic.album.manager.litchis.DJIFileType;
import dji.logic.album.model.DJIAlbumFile;
import dji.logic.album.model.DJIAlbumPullErrorType;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.DJIVideoUtil;
import dji.midware.media.player.DJIMediaPlayerInterface;
import dji.midware.natives.FPVController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.TimerTask;

@EXClassNullAway
public abstract class DJIMediaPlayer {
    public static final int PLAY_STATUS_FAST_FORWARD = 3;
    public static final int PLAY_STATUS_PAUSE = 2;
    public static final int PLAY_STATUS_PLAYING = 10;
    public static final int PLAY_STATUS_PREPARE_PLAY = 1;
    public static final int PLAY_STATUS_STOP = 0;
    protected static boolean isDebug = false;
    protected static final int persize = 262144;
    protected String TAG = getClass().getSimpleName();
    protected RandomAccessFile accessFile;
    protected final Object accessFileLock = new Object();
    protected byte[] buffer = new byte[1048576];
    protected int cachedPos;
    protected Timer durTimer;
    protected long fileLen;
    protected boolean isBuffered = false;
    protected boolean isBuffering = false;
    protected volatile int isPlaying = 0;
    protected DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener = new DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile>() {
        /* class dji.midware.media.player.DJIMediaPlayer.AnonymousClass2 */

        public void onStart() {
            DJIMediaPlayer.this.isBuffered = false;
        }

        public void onProgress(long total, long current) {
        }

        public void onRateUpdate(long total, long current, long bufferSize) {
            DJIMediaPlayer.this.onRateUpdate(total, current, bufferSize);
        }

        public void onSuccess(DJIAlbumFile data) {
            if (DJIMediaPlayer.this.mOnBufferingUpdateListener != null) {
                DJIMediaPlayer.this.mOnBufferingUpdateListener.onBufferingUpdate(DJIMediaPlayer.this, 100);
            }
            DJIMediaPlayer.this.isBuffering = false;
            DJIMediaPlayer.this.isBuffered = true;
            if (DJIMediaPlayer.isDebug) {
                DJILogHelper.getInstance().LOGD("mediaPlayer", "*****isBuffered*****", true, true);
            }
        }

        public void onFailure(DJIAlbumPullErrorType error) {
            if (error == DJIAlbumPullErrorType.TIMEOUT) {
                DJIMediaPlayer.this.restartFromCurrent();
            } else if (DJIMediaPlayer.this.mOnErrorListener != null) {
                DJIMediaPlayer.this.mOnErrorListener.onError(DJIMediaPlayer.this, error);
            }
        }
    };
    protected int mDuration;
    protected int mFileIndex;
    protected long mFileLength;
    protected String mFileName;
    protected DJIFileType mFileType;
    protected int mFrameRate;
    protected DJIMediaPlayerInterface.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private DJIMediaPlayerInterface.OnCompletionListener mOnCompletionListener;
    /* access modifiers changed from: private */
    public DJIMediaPlayerInterface.OnErrorListener mOnErrorListener;
    protected DJIMediaPlayerInterface.OnIframeRecvListener mOnIframeRecvListener;
    protected DJIMediaPlayerInterface.OnPreparedListener mOnPreparedListener;
    protected DJIMediaPlayerInterface.OnSeekCompleteListener mOnSeekCompleteListener;
    /* access modifiers changed from: private */
    public DJIMediaPlayerInterface.OnTimeUpdateListener mOnTimeUpdateListener;
    protected int mVideoScale;
    protected boolean needRebuffer = false;
    protected long playedOffset = 0;
    protected int position;
    protected int qsize = 0;
    protected int receiverIdInProtocol = -1;
    protected long seekToOffset;
    protected long toBufferLen;
    protected long toBufferPosition;

    /* access modifiers changed from: protected */
    public abstract void abortStream();

    public abstract void config();

    /* access modifiers changed from: protected */
    public abstract String getStreamCachePath();

    /* access modifiers changed from: protected */
    public abstract void onRateUpdate(long j, long j2, long j3);

    /* access modifiers changed from: protected */
    public abstract void seekStream(int i);

    /* access modifiers changed from: protected */
    public abstract void startStream();

    public void onDestroy() {
        if (this.durTimer != null) {
            this.durTimer.cancel();
            this.durTimer = null;
        }
    }

    public void setFileInfo(int fileIndex, DJIFileType fileType, long fileLength, String fileName, int duration) {
        this.mFileIndex = fileIndex;
        this.mFileType = fileType;
        this.mFileLength = fileLength;
        this.mFileName = fileName;
        this.mDuration = duration;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public int getCurrentPosition() {
        return this.position;
    }

    /* access modifiers changed from: protected */
    public void openCacheFileStream() {
        long tmpPosition = 0;
        if (this.accessFile != null) {
            try {
                tmpPosition = this.accessFile.getFilePointer();
                this.accessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String path = getStreamCachePath();
        DJILogHelper.getInstance().LOGD("mediaPlayer", "path=" + path);
        try {
            this.accessFile = new RandomAccessFile(new File(path), "rws");
            this.accessFile.seek(tmpPosition);
        } catch (FileNotFoundException e2) {
            if (isDebug) {
                DJILogHelper.getInstance().LOGD("mediaPlayer", e2.getMessage(), true, true);
            }
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public void start() {
        reset();
        this.position = 0;
        ServiceManager.getInstance().setIsFix(true);
        ServiceManager.getInstance().resumeParseThread();
        FPVController.native_setFrameRate(this.mFrameRate);
        this.isPlaying = 1;
        startStream();
        new PlayThread().start();
        startDurTimer();
    }

    /* access modifiers changed from: protected */
    public void startDurTimer() {
        if (this.durTimer != null) {
            this.durTimer.cancel();
        }
        this.durTimer = new Timer();
        this.durTimer.schedule(new TimerTask() {
            /* class dji.midware.media.player.DJIMediaPlayer.AnonymousClass1 */

            public void run() {
                if (DJIMediaPlayer.this.qsize == 0 || DJIMediaPlayer.this.isPlaying != 10 || DJIMediaPlayer.this.isBuffering) {
                    if (DJIMediaPlayer.this.isPlaying == 2) {
                    }
                    return;
                }
                DJIMediaPlayer.this.position++;
                if (DJIMediaPlayer.this.mOnTimeUpdateListener != null) {
                    DJIMediaPlayer.this.mOnTimeUpdateListener.onUpdate(DJIMediaPlayer.this, DJIMediaPlayer.this.position, DJIMediaPlayer.this.cachedPos);
                }
                if (DJIMediaPlayer.this.position == DJIMediaPlayer.this.mDuration) {
                    DJIMediaPlayer.this.onPlayOver();
                    if (DJIMediaPlayer.isDebug) {
                        DJILogHelper.getInstance().LOGD("", "*********play time over**********", true, true);
                    }
                }
            }
        }, 1000, 1000);
    }

    public void pause() {
        if (this.isPlaying != 0) {
            this.isPlaying = 2;
            ServiceManager.getInstance().pauseParseThread();
        }
    }

    public void resume() {
        if (this.isPlaying != 0) {
            this.isPlaying = 10;
            this.needRebuffer = true;
            ServiceManager.getInstance().resumeParseThread();
        }
    }

    public void stop() {
        ServiceManager.getInstance().resumeParseThread();
        if (isDebug) {
            DJILogHelper.getInstance().LOGD("", "*********play do stop**********", true, true);
        }
        this.position = 0;
        if (this.isPlaying != 0) {
            this.isPlaying = 0;
            if (this.durTimer != null) {
                this.durTimer.cancel();
                this.durTimer = null;
            }
            synchronized (this.accessFileLock) {
                try {
                    if (this.accessFile != null) {
                        this.accessFile.close();
                        this.accessFile = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ServiceManager.getInstance().setIsFix(false);
            abortStream();
            reset();
        }
    }

    public boolean canSeek() {
        return this.mDuration != 0;
    }

    public void seekTo(int sec) {
        if (canSeek()) {
            PlayThread thread = null;
            if (this.isPlaying == 0) {
                this.fileLen = 0;
                ServiceManager.getInstance().setIsFix(true);
                openCacheFileStream();
                thread = new PlayThread();
                startDurTimer();
            }
            this.isPlaying = 3;
            if (thread != null) {
                thread.start();
            }
            this.position = sec;
            seekStream(sec);
        }
    }

    /* access modifiers changed from: protected */
    public void onPlayOver() {
        stop();
        if (this.mOnCompletionListener != null) {
            this.mOnCompletionListener.onCompletion(this);
        }
        this.position = this.mDuration;
        if (this.mOnTimeUpdateListener != null) {
            this.mOnTimeUpdateListener.onUpdate(this, this.position, this.cachedPos);
        }
    }

    /* access modifiers changed from: protected */
    public void reset() {
        this.isBuffering = false;
        this.seekToOffset = 0;
        this.playedOffset = 0;
        this.fileLen = 0;
        FPVController.native_clear();
    }

    public int isPlaying() {
        return this.isPlaying;
    }

    public boolean isBuffering() {
        return this.isBuffering;
    }

    /* access modifiers changed from: protected */
    public void myParser(byte[] buffer2, int size) {
        FPVController.native_transferVideoData(DJIVideoUtil.extraMemForParsing(DJIVideoUtil.ExtraMemInvokePoint.DJIMediaPlayer, buffer2, 0, size), size);
    }

    /* access modifiers changed from: protected */
    public synchronized void readFileToPlay() {
        synchronized (this.accessFileLock) {
            try {
                if (this.isPlaying != 0 && ((this.isBuffered || this.fileLen - this.playedOffset >= ((long) this.buffer.length)) && this.accessFile != null)) {
                    DJILogHelper.getInstance().LOGD(getClass().getSimpleName(), "*******************localfile read start " + this.accessFile.length() + " " + this.accessFile.getFilePointer());
                    int size = this.accessFile.read(this.buffer, 0, this.buffer.length);
                    if (size > 0) {
                        myParser(this.buffer, size);
                        this.playedOffset += (long) size;
                        if (isDebug) {
                            DJILogHelper.getInstance().LOGD(getClass().getSimpleName(), "*******************localfile read size=" + size + " qsize=" + FPVController.native_getQueueSize() + " fileLen=" + this.fileLen + " remain size=" + (this.fileLen - this.playedOffset));
                        }
                    } else if (isDebug) {
                        DJILogHelper.getInstance().LOGD(getClass().getSimpleName(), "*******************localfile read size error=" + size + " qsize=" + FPVController.native_getQueueSize());
                    }
                } else if (isDebug) {
                    DJILogHelper.getInstance().LOGD(getClass().getSimpleName(), "*******************localfile remain size=" + (this.fileLen - this.playedOffset) + ",isPlaying:" + this.isPlaying);
                }
            } catch (IOException e) {
                if (isDebug) {
                    DJILogHelper.getInstance().LOGD(getClass().getSimpleName(), e.getMessage(), true, true);
                    e.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void restartFromCurrent() {
    }

    private class PlayThread extends Thread {
        private PlayThread() {
        }

        public void run() {
            Process.setThreadPriority(-16);
            while (DJIMediaPlayer.this.isPlaying != 0) {
                DJIMediaPlayer.this.qsize = FPVController.native_getQueueSize();
                if (DJIMediaPlayer.this.isPlaying == 10 && DJIMediaPlayer.this.qsize == 0 && DJIMediaPlayer.this.fileLen == DJIMediaPlayer.this.playedOffset && DJIMediaPlayer.this.isBuffered) {
                    DJIMediaPlayer.this.onPlayOver();
                    if (DJIMediaPlayer.isDebug) {
                        DJILogHelper.getInstance().LOGD(getName(), "*********localfile stop**********");
                    }
                } else if (DJIMediaPlayer.this.qsize < 800 && (!DJIMediaPlayer.this.isBuffering || DJIMediaPlayer.this.isBuffered)) {
                    DJIMediaPlayer.this.readFileToPlay();
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            FPVController.native_clear();
            if (DJIMediaPlayer.isDebug) {
                DJILogHelper.getInstance().LOGD(getName(), "*********playthread over**********");
            }
        }
    }

    public DJIMediaPlayer(int... protocolIdOfCamera) {
        if (protocolIdOfCamera != null && protocolIdOfCamera.length > 0) {
            this.receiverIdInProtocol = protocolIdOfCamera[0];
        }
    }

    public void setOnPreparedListener(DJIMediaPlayerInterface.OnPreparedListener listener2) {
        this.mOnPreparedListener = listener2;
    }

    public void setOnCompletionListener(DJIMediaPlayerInterface.OnCompletionListener listener2) {
        this.mOnCompletionListener = listener2;
    }

    public void setOnBufferingUpdateListener(DJIMediaPlayerInterface.OnBufferingUpdateListener listener2) {
        this.mOnBufferingUpdateListener = listener2;
    }

    public void setOnIframeRecvListener(DJIMediaPlayerInterface.OnIframeRecvListener listener2) {
        this.mOnIframeRecvListener = listener2;
    }

    public void setOnSeekCompleteListener(DJIMediaPlayerInterface.OnSeekCompleteListener listener2) {
        this.mOnSeekCompleteListener = listener2;
    }

    public void setOnErrorListener(DJIMediaPlayerInterface.OnErrorListener listener2) {
        this.mOnErrorListener = listener2;
    }

    public void setOnTimeUpdateListener(DJIMediaPlayerInterface.OnTimeUpdateListener listener2) {
        this.mOnTimeUpdateListener = listener2;
    }
}
