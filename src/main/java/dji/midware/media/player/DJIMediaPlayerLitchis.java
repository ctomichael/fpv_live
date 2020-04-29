package dji.midware.media.player;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.logic.album.manager.litchis.DJIFileStreamLoader;
import dji.logic.album.model.DJIAlbumFile;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.natives.FPVController;
import java.io.IOException;

@EXClassNullAway
public class DJIMediaPlayerLitchis extends DJIMediaPlayer {
    /* access modifiers changed from: private */
    public DJIFileStreamLoader streamLoader = new DJIFileStreamLoader();

    public DJIMediaPlayerLitchis(int... protocolIdOfCamera) {
        super(protocolIdOfCamera);
        this.streamLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.streamLoader.setPreparedListener(new DJIFileStreamLoader.OnPreparedListener() {
            /* class dji.midware.media.player.DJIMediaPlayerLitchis.AnonymousClass1 */

            public void onPrepared(DJIAlbumFile alburmFile) {
                DJIMediaPlayerLitchis.this.isPlaying = 10;
                if (DJIMediaPlayerLitchis.this.mOnPreparedListener != null) {
                    DJIMediaPlayerLitchis.this.mOnPreparedListener.onPrepared(DJIMediaPlayerLitchis.this);
                }
            }
        });
        this.streamLoader.setOnSeekCompleteListener(new DJIFileStreamLoader.OnSeekCompleteListener() {
            /* class dji.midware.media.player.DJIMediaPlayerLitchis.AnonymousClass2 */

            public void onSeekComplete(DJIAlbumFile alburmFile) {
                DJIMediaPlayerLitchis.this.resume();
                if (DJIMediaPlayerLitchis.this.mOnSeekCompleteListener != null) {
                    DJIMediaPlayerLitchis.this.mOnSeekCompleteListener.onSeekComplete(DJIMediaPlayerLitchis.this);
                }
            }
        });
        this.streamLoader.setOnCacheFileChangeListener(new DJIFileStreamLoader.OnCacheFileChangeListener() {
            /* class dji.midware.media.player.DJIMediaPlayerLitchis.AnonymousClass3 */

            public void onChange(long size) {
                DJIMediaPlayerLitchis.this.fileLen = size;
                if (DJIMediaPlayerLitchis.this.streamLoader.isCached()) {
                    DJIMediaPlayerLitchis.this.cachedPos = DJIMediaPlayerLitchis.this.mDuration;
                    return;
                }
                long cachedLen = DJIMediaPlayerLitchis.this.streamLoader.getSeekOffset() + DJIMediaPlayerLitchis.this.fileLen;
                if (DJIMediaPlayerLitchis.this.mFileLength > 0) {
                    float percent = (((float) cachedLen) * 1.0f) / ((float) DJIMediaPlayerLitchis.this.mFileLength);
                    DJIMediaPlayerLitchis.this.cachedPos = (int) (((float) DJIMediaPlayerLitchis.this.mDuration) * percent);
                    return;
                }
                DJIMediaPlayerLitchis.this.cachedPos = (int) ((((float) cachedLen) * 1.0f) / 262144.0f);
            }
        });
        this.streamLoader.setOnCacheRenameListener(new DJIFileStreamLoader.OnCacheRenameListener() {
            /* class dji.midware.media.player.DJIMediaPlayerLitchis.AnonymousClass4 */

            public void onChange() {
                DJILogHelper.getInstance().LOGD("mediaPlayer", "OnCacheRename reopen");
                DJIMediaPlayerLitchis.this.cachedPos = DJIMediaPlayerLitchis.this.mDuration;
                DJIMediaPlayerLitchis.this.openCacheFileStream();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        this.buffer = null;
        if (this.streamLoader != null) {
            this.streamLoader.destroy();
            this.streamLoader = null;
        }
    }

    public void config() {
        this.streamLoader.setListener(this.mFileIndex, this.mFileType, this.mFileName, this.mDuration, this.listener);
    }

    /* access modifiers changed from: protected */
    public String getStreamCachePath() {
        return this.streamLoader.getCachePath();
    }

    /* access modifiers changed from: protected */
    public void restartFromCurrent() {
        if (this.streamLoader != null) {
            this.streamLoader.restart();
        }
        super.restartFromCurrent();
    }

    /* access modifiers changed from: protected */
    public void startStream() {
        this.streamLoader.start();
        openCacheFileStream();
    }

    /* access modifiers changed from: protected */
    public void abortStream() {
        this.streamLoader.abort(false);
    }

    /* access modifiers changed from: protected */
    public void seekStream(int sec) {
        long seekTo = (long) (((float) this.mFileLength) * ((((float) sec) * 1.0f) / ((float) this.mDuration)));
        DJILogHelper.getInstance().LOGD("", "fileInfo.length=" + this.mFileLength + " fileLen=" + this.fileLen, false, true);
        if (this.mFileLength == this.fileLen || (seekTo > this.streamLoader.getSeekOffset() && seekTo < this.streamLoader.getSeekOffset() + this.fileLen)) {
            DJILogHelper.getInstance().LOGD(this.TAG, "local file seekTo " + seekTo, true, true);
            this.playedOffset = seekTo - this.streamLoader.getSeekOffset();
            this.seekToOffset = seekTo;
            try {
                this.accessFile.seek(this.playedOffset);
                FPVController.native_clear();
                readFileToPlay();
            } catch (IOException e) {
                e.printStackTrace();
            }
            resume();
            if (this.mOnSeekCompleteListener != null) {
                this.mOnSeekCompleteListener.onSeekComplete(this);
                return;
            }
            return;
        }
        synchronized (this.accessFileLock) {
            DJILogHelper.getInstance().LOGD("", "remote file seekTo " + seekTo, true, true);
            this.seekToOffset = seekTo;
            this.fileLen = 0;
            this.playedOffset = 0;
            abortStream();
            ServiceManager.getInstance().pauseParseThread();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            FPVController.native_clear();
            ServiceManager.getInstance().resumeParseThread();
            DJILogHelper.getInstance().LOGD("", "remote file isCached " + this.streamLoader.isCached(), true, true);
            if (!this.streamLoader.isCached()) {
                this.streamLoader.start(this.seekToOffset);
                if (this.accessFile != null) {
                    try {
                        this.accessFile.close();
                        this.accessFile = null;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                openCacheFileStream();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onRateUpdate(long total, long current, long bufferSize) {
        if (current > 0 && this.buffer != null && FPVController.native_getQueueSize() <= 0) {
            if (this.needRebuffer || (!this.isBuffering && this.fileLen - this.playedOffset < ((long) this.buffer.length))) {
                this.toBufferLen = ((long) this.buffer.length) - (this.fileLen - this.playedOffset);
                DJILogHelper.getInstance().LOGD(this.TAG, "toBufferLen " + this.toBufferLen);
                if (this.toBufferLen > 0) {
                    this.toBufferPosition = this.toBufferLen + current;
                    this.isBuffering = true;
                    this.needRebuffer = false;
                }
            }
            if (this.isBuffering && this.mOnBufferingUpdateListener != null) {
                int pgs = (int) ((1.0f - ((((float) (this.toBufferPosition - current)) * 1.0f) / ((float) this.toBufferLen))) * 100.0f);
                if (pgs > 100) {
                    pgs = 100;
                }
                this.mOnBufferingUpdateListener.onBufferingUpdate(this, pgs);
            }
            if (this.isBuffering && this.toBufferPosition <= current) {
                this.isBuffering = false;
            }
        }
    }
}
