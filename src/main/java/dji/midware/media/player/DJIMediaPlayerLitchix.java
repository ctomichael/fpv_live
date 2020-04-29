package dji.midware.media.player;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.logic.album.manager.litchis.DJIStreamLoader;
import dji.logic.album.model.DJIAlbumFile;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.litchis.DataRequestAbort;
import dji.midware.media.DJIAudioDecoder;
import dji.midware.media.DJIVideoUtil;
import dji.midware.natives.FPVController;
import dji.midware.parser.plugins.DJIPlaybackChanneParser;
import dji.midware.util.BytesUtil;
import java.io.IOException;

@EXClassNullAway
public class DJIMediaPlayerLitchix extends DJIMediaPlayer {
    private float cacheTime;
    boolean isGettedMixType = false;
    boolean isMixStream = false;
    private volatile boolean isSeeking = false;
    long magic;
    /* access modifiers changed from: private */
    public long myFileLen;
    private DJIPlaybackChanneParser playbackChanneParser;
    /* access modifiers changed from: private */
    public DJIStreamLoader streamLoader;

    public DJIMediaPlayerLitchix() {
        super(new int[0]);
        this.buffer = new byte[DJIStreamLoader.bufferSize];
        this.cacheTime = (((float) this.buffer.length) * 1.0f) / 262144.0f;
        this.magic = FPVController.native_getDJIAVPaserHeaderMagic();
        DJILogHelper.getInstance().LOGD("", "magic = " + this.magic);
        this.streamLoader = new DJIStreamLoader();
        this.streamLoader.setPreparedListener(new DJIStreamLoader.OnPreparedListener() {
            /* class dji.midware.media.player.DJIMediaPlayerLitchix.AnonymousClass1 */

            public void onPrepared(DJIAlbumFile alburmFile) {
                DJIMediaPlayerLitchix.this.isPlaying = 10;
                if (DJIMediaPlayerLitchix.this.mDuration == 0) {
                    DJIMediaPlayerLitchix.this.mDuration = (int) alburmFile.duration;
                }
                if (DJIMediaPlayerLitchix.this.mOnPreparedListener != null) {
                    DJIMediaPlayerLitchix.this.mOnPreparedListener.onPrepared(DJIMediaPlayerLitchix.this);
                }
            }
        });
        this.streamLoader.setOnSeekCompleteListener(new DJIStreamLoader.OnSeekCompleteListener() {
            /* class dji.midware.media.player.DJIMediaPlayerLitchix.AnonymousClass2 */

            public void onSeekComplete(DJIAlbumFile alburmFile) {
                DJIMediaPlayerLitchix.this.resume();
                if (DJIMediaPlayerLitchix.this.mOnSeekCompleteListener != null) {
                    DJIMediaPlayerLitchix.this.mOnSeekCompleteListener.onSeekComplete(DJIMediaPlayerLitchix.this);
                }
            }
        });
        this.streamLoader.setOnCacheFileChangeListener(new DJIStreamLoader.OnCacheFileChangeListener() {
            /* class dji.midware.media.player.DJIMediaPlayerLitchix.AnonymousClass3 */

            public void onChange(long size) {
                DJIMediaPlayerLitchix.this.fileLen = size;
                long unused = DJIMediaPlayerLitchix.this.myFileLen = size;
                DJILogHelper.getInstance().LOGD("", "fileLen=" + DJIMediaPlayerLitchix.this.fileLen, false, true);
                if (DJIMediaPlayerLitchix.this.streamLoader.isCached()) {
                    DJIMediaPlayerLitchix.this.cachedPos = DJIMediaPlayerLitchix.this.mDuration;
                }
            }
        });
        this.streamLoader.setOnCacheRenameListener(new DJIStreamLoader.OnCacheRenameListener() {
            /* class dji.midware.media.player.DJIMediaPlayerLitchix.AnonymousClass4 */

            public void onChange() {
                DJILogHelper.getInstance().LOGD("mediaPlayer", "OnCacheRename reopen");
                DJIMediaPlayerLitchix.this.cachedPos = DJIMediaPlayerLitchix.this.mDuration;
                DJIMediaPlayerLitchix.this.openCacheFileStream();
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
        DJIAudioDecoder audioDecoder = ServiceManager.getInstance().getAudioDecoder();
        if (audioDecoder != null) {
            audioDecoder.stop();
        }
        this.isSeeking = true;
        float percet = (((float) sec) * 1.0f) / ((float) this.mDuration);
        this.seekToOffset = (long) (262144 * sec);
        DJILogHelper.getInstance().LOGD("", "seekToOffset=" + this.seekToOffset + " fileLen=" + this.myFileLen, false, true);
        long mseekTo = (long) (((float) this.mFileLength) * percet);
        if (this.streamLoader.isCached()) {
            this.myFileLen = this.mFileLength;
            this.fileLen = this.mFileLength;
        }
        if (this.isBuffered || (sec < this.cachedPos && this.myFileLen + mseekTo > this.mFileLength + ((long) this.buffer.length))) {
            DJILogHelper.getInstance().LOGD("", "local file seekTo=" + mseekTo + " fileLen=" + this.myFileLen + " fileInfo.length=" + this.mFileLength, true, true);
            this.playedOffset = (this.myFileLen + mseekTo) - this.mFileLength;
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
            }
        } else {
            synchronized (this.accessFileLock) {
                DJILogHelper.getInstance().LOGD("", "remote file seekTo " + sec, true, true);
                long seekToOffset = (long) (sec * 1000);
                this.fileLen = 0;
                this.playedOffset = 0;
                abortStream();
                ServiceManager.getInstance().pauseParseThread();
                this.streamLoader.abort(DataRequestAbort.AbortReason.Seek);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                FPVController.native_clear();
                ServiceManager.getInstance().resumeParseThread();
                DJILogHelper.getInstance().LOGD("", "remote file isCached " + this.streamLoader.isCached(), true, true);
                if (!this.streamLoader.isCached()) {
                    this.streamLoader.seek(seekToOffset);
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
        if (audioDecoder != null) {
            audioDecoder.play();
        }
        this.isSeeking = false;
    }

    public void pause() {
        super.pause();
        DJIAudioDecoder audioDecoder = ServiceManager.getInstance().getAudioDecoder();
        if (audioDecoder != null) {
            audioDecoder.pause();
        }
    }

    public void resume() {
        super.resume();
        DJIAudioDecoder audioDecoder = ServiceManager.getInstance().getAudioDecoder();
        if (audioDecoder != null) {
            audioDecoder.play();
        }
    }

    public void stop() {
        super.stop();
        DJIAudioDecoder audioDecoder = ServiceManager.getInstance().getAudioDecoder();
        if (audioDecoder != null) {
            audioDecoder.stop();
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void readFileToPlay() {
        if (!this.isSeeking) {
            super.readFileToPlay();
        }
    }

    /* access modifiers changed from: protected */
    public void onRateUpdate(long total, long current, long bufferSize) {
        if (current > 0 && this.buffer != null) {
            this.cachedPos = (int) ((((float) current) * 1.0f) / 262144.0f);
            this.cachedPos = this.cachedPos < this.position ? this.position : this.cachedPos;
            if (FPVController.native_getQueueSize() <= 0) {
                if (this.needRebuffer || (!this.isBuffering && this.cachedPos < this.mDuration && ((float) (this.cachedPos - this.position)) < this.cacheTime)) {
                    this.isBuffering = true;
                    this.needRebuffer = false;
                }
                DJILogHelper.getInstance().LOGD("mediaPlayer", "cachedPos=" + this.cachedPos + " position=" + this.position, false, false);
                DJILogHelper.getInstance().LOGD("mediaPlayer", " cacheTime=" + this.cacheTime + " current=" + current, false, false);
                if (this.isBuffering && this.mOnBufferingUpdateListener != null) {
                    float remain = (((float) (this.cachedPos - this.position)) * 1.0f) / (this.cacheTime > ((float) this.mDuration) ? (float) this.mDuration : this.cacheTime);
                    if (remain > 1.0f) {
                        remain = 1.0f;
                    }
                    DJILogHelper.getInstance().LOGD("mediaPlayer", "remain=" + remain, false, false);
                    int pgs = (int) (100.0f * remain);
                    if (pgs > 100) {
                        pgs = 100;
                    }
                    this.mOnBufferingUpdateListener.onBufferingUpdate(this, pgs);
                }
                if (!this.isBuffering) {
                    return;
                }
                if (this.cachedPos >= this.mDuration || ((float) (this.cachedPos - this.position)) >= this.cacheTime) {
                    this.isBuffering = false;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void myParser(byte[] buffer, int size) {
        boolean z = true;
        if (!this.isGettedMixType) {
            this.isGettedMixType = true;
            long preMagic = BytesUtil.getUInt(buffer, 0);
            if (preMagic != this.magic) {
                z = false;
            }
            this.isMixStream = z;
            DJILogHelper.getInstance().LOGD(this.TAG, "preMagic=" + preMagic + " isMixStream=" + this.isMixStream);
        }
        if (this.isMixStream) {
            if (this.playbackChanneParser == null) {
                this.playbackChanneParser = new DJIPlaybackChanneParser();
            }
            this.playbackChanneParser.parse(buffer, 0, size);
            return;
        }
        FPVController.native_transferVideoData(DJIVideoUtil.extraMemForParsing(DJIVideoUtil.ExtraMemInvokePoint.DJIMediaPlayerLitchix, buffer, 0, size), size);
    }
}
