package dji.midware.media.player;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.logic.album.manager.DJIAlbumInterface;
import dji.logic.album.manager.h1.DJIMp4StreamLoader;
import dji.logic.album.manager.litchis.DJIFileStreamLoader;
import dji.logic.album.model.DJIAlbumFile;
import dji.logic.album.model.DJIAlbumPullErrorType;
import dji.midware.media.mp4.MP4BoxType;
import dji.midware.media.mp4.MP4BytesUtil;
import dji.midware.media.mp4.MP4Info;
import dji.midware.media.mp4.MP4Parser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

@EXClassNullAway
public class DJIMediaPlayerH1 extends DJIMediaPlayer implements DJIMp4StreamLoader.Mp4PreReadListener {
    /* access modifiers changed from: private */
    public int curMp4Index = 0;
    /* access modifiers changed from: private */
    public long curWriteOffset = 0;
    /* access modifiers changed from: private */
    public long delay = 0;
    protected DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile> listener = new DJIAlbumInterface.DJIAlbumPullListener<DJIAlbumFile>() {
        /* class dji.midware.media.player.DJIMediaPlayerH1.AnonymousClass4 */

        public void onStart() {
        }

        public void onProgress(long total, long current) {
        }

        public void onRateUpdate(long total, long current, long bufferSize) {
        }

        public void onSuccess(DJIAlbumFile data) {
        }

        public void onFailure(DJIAlbumPullErrorType error) {
        }
    };
    /* access modifiers changed from: private */
    public MP4Info mp4Info;
    private DJIMp4StreamLoader streamLoader = new DJIMp4StreamLoader();

    static /* synthetic */ int access$308(DJIMediaPlayerH1 x0) {
        int i = x0.curMp4Index;
        x0.curMp4Index = i + 1;
        return i;
    }

    public DJIMediaPlayerH1(int... protocolIdOfCamera) {
        super(protocolIdOfCamera);
        this.streamLoader.setReceiverIdInProtocol(this.receiverIdInProtocol);
        this.streamLoader.setPreReadListener(this);
        this.streamLoader.setOnCacheFileChangeListener(new DJIFileStreamLoader.OnCacheFileChangeListener() {
            /* class dji.midware.media.player.DJIMediaPlayerH1.AnonymousClass1 */

            public void onChange(long size) {
                try {
                    synchronized (DJIMediaPlayerH1.this.accessFileLock) {
                        DJIMediaPlayerH1.this.accessFile.close();
                        DJIMediaPlayerH1.this.accessFile = new RandomAccessFile(new File(DJIMediaPlayerH1.this.getStreamCachePath()), "rws");
                    }
                    long unused = DJIMediaPlayerH1.this.curWriteOffset = size;
                    long totalFrameOffset = (long) (DJIMediaPlayerH1.this.mp4Info.sample_offset[DJIMediaPlayerH1.this.mp4Info.sample_offset.length - 1] + DJIMediaPlayerH1.this.mp4Info.sample_size[DJIMediaPlayerH1.this.mp4Info.sample_size.length - 1]);
                    DJIMediaPlayerH1.this.cachedPos = (int) (((float) DJIMediaPlayerH1.this.mDuration) * ((1.0f * ((float) DJIMediaPlayerH1.this.curWriteOffset)) / ((float) totalFrameOffset)));
                } catch (IOException e) {
                    DJILog.exceptionToString(e);
                }
            }
        });
        this.streamLoader.setOnCacheRenameListener(new DJIFileStreamLoader.OnCacheRenameListener() {
            /* class dji.midware.media.player.DJIMediaPlayerH1.AnonymousClass2 */

            public void onChange() {
                DJILogHelper.getInstance().LOGD("mediaPlayer", "OnCacheRename reopen");
                DJIMediaPlayerH1.this.cachedPos = DJIMediaPlayerH1.this.mDuration;
                DJIMediaPlayerH1.this.openCacheFileStream();
            }
        });
        this.streamLoader.setPreparedListener(new DJIFileStreamLoader.OnPreparedListener() {
            /* class dji.midware.media.player.DJIMediaPlayerH1.AnonymousClass3 */

            public void onPrepared(DJIAlbumFile alburmFile) {
                DJIMediaPlayerH1.this.isPlaying = 10;
                if (DJIMediaPlayerH1.this.mOnPreparedListener != null) {
                    DJIMediaPlayerH1.this.mOnPreparedListener.onPrepared(DJIMediaPlayerH1.this);
                }
            }
        });
    }

    public void config() {
        this.streamLoader.setListener(this.mFileIndex, this.mFileType, this.mFileName, this.mDuration, this.listener);
    }

    public void start() {
        Log.e(this.TAG, "start preload");
        this.mOnBufferingUpdateListener.onBufferingUpdate(this, 0);
        preLoad();
    }

    public void pause() {
        if (this.isPlaying != 0) {
            this.isPlaying = 2;
        }
    }

    public void resume() {
        if (this.isPlaying != 0) {
            this.isPlaying = 10;
            this.needRebuffer = true;
        }
    }

    /* access modifiers changed from: protected */
    public String getStreamCachePath() {
        return this.streamLoader.getCachePath();
    }

    /* access modifiers changed from: protected */
    public void preLoad() {
        openCacheFileStream();
        if (this.streamLoader.isCached()) {
            byte[] buffer = new byte[100];
            try {
                this.accessFile.read(buffer);
                int moov_offset = MP4Parser.getInstance().findMoovOffset(buffer);
                this.accessFile.seek((long) moov_offset);
                byte[] moovHeader = new byte[8];
                this.accessFile.read(moovHeader);
                int box_size = MP4BytesUtil.getInt(moovHeader, 0);
                if (!MP4BoxType.moov._equals(MP4BytesUtil.getString(moovHeader, 0 + 4, 4))) {
                    Log.e(this.TAG, "not find box moov");
                    return;
                }
                byte[] buffer2 = new byte[box_size];
                this.accessFile.seek((long) moov_offset);
                this.accessFile.read(buffer2);
                MP4Parser.getInstance().parseMoov(buffer2);
                onPreReadComplete();
            } catch (IOException e) {
                DJILog.exceptionToString(e);
            }
        } else {
            this.streamLoader.startLoadHeader();
        }
    }

    /* access modifiers changed from: protected */
    public void startStream() {
        this.mp4Info = MP4Parser.getInstance().videoInfo;
        if (this.mp4Info != null && this.mp4Info.pos_iframe != null) {
            this.curMp4Index = 0;
            this.position = 0;
            this.streamLoader.start(0);
            new VideoThread().start();
            startDurTimer();
        }
    }

    /* access modifiers changed from: protected */
    public void abortStream() {
        this.streamLoader.abort(false);
    }

    /* access modifiers changed from: protected */
    public void seekStream(int seekTo) {
        if (this.isPlaying == 0) {
            start();
            this.position = seekTo;
            pause();
        }
        float progress = (1.0f * ((float) seekTo)) / ((float) this.mDuration);
        Log.d(this.TAG, "seek to progress:" + progress);
        int index = (int) (((float) this.mp4Info.media_length) * progress);
        if (index == this.mp4Info.media_length) {
            index--;
        }
        if (this.curWriteOffset < ((long) this.mp4Info.sample_offset[index]) || this.streamLoader.hasSeek) {
            this.seekToOffset = (long) seekTo;
            this.fileLen = 0;
            this.playedOffset = 0;
            this.curWriteOffset = (long) this.mp4Info.sample_offset[index];
            this.streamLoader.abort(false);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                DJILog.exceptionToString(e);
            }
            this.streamLoader.onSeek();
            if (!this.streamLoader.isCached()) {
                Log.e(this.TAG, "seek cmd start: " + this.mp4Info.chunk_offset[index]);
                this.streamLoader.start((long) this.mp4Info.chunk_offset[index]);
                synchronized (this.accessFileLock) {
                    if (this.accessFile != null) {
                        try {
                            this.accessFile.close();
                            this.accessFile = null;
                        } catch (IOException e2) {
                            DJILog.exceptionToString(e2);
                        }
                    }
                    String path = getStreamCachePath();
                    DJILogHelper.getInstance().LOGD("mediaPlayer", "path=" + path);
                    try {
                        this.accessFile = new RandomAccessFile(new File(path), "rws");
                    } catch (FileNotFoundException e3) {
                        DJILog.exceptionToString(e3);
                    }
                }
            }
        }
        this.curMp4Index = index;
        Log.e(this.TAG, "isPlaying: " + this.isPlaying + " curMp4Index: " + this.curMp4Index + " mp4Info.media_length: " + this.mp4Info.media_length);
        this.isPlaying = 1;
        return;
    }

    /* access modifiers changed from: protected */
    public void onRateUpdate(long total, long current, long bufferSize) {
    }

    public void seekTo(int sec) {
        this.position = sec;
        if (this.mp4Info != null) {
            seekStream(sec);
        }
    }

    public void onDestroy() {
        if (this.streamLoader != null) {
            this.streamLoader.destroy();
            this.streamLoader = null;
            this.curWriteOffset = 0;
        }
    }

    public void onPreReadComplete() {
        this.isPlaying = 1;
        startStream();
    }

    private class VideoThread extends Thread {
        private VideoThread() {
        }

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: dji.midware.media.DJIVideoDataRecver.onVideoRecv(byte[], int, int, int, boolean, int, int, int, int, int, int, boolean):void
         arg types: [byte[], int, int, int, int, int, int, int, int, int, int, int]
         candidates:
          dji.midware.media.DJIVideoDataRecver.onVideoRecv(byte[], int, int, boolean, int, int, int, int, int, int, boolean, int):void
          dji.midware.media.DJIVideoDataRecver.onVideoRecv(byte[], int, int, int, boolean, int, int, int, int, int, int, boolean):void */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0059, code lost:
            if ((dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).media_length - dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0)) <= (dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).fps * 5)) goto L_0x00de;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x005b, code lost:
            r17 = dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).fps * 5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0088, code lost:
            if (((long) dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).sample_offset[dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0) + r17]) >= dji.midware.media.player.DJIMediaPlayerH1.access$000(r24.this$0)) goto L_0x0327;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x008a, code lost:
            r18 = dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0) + r17;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x009e, code lost:
            if (dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0) >= r18) goto L_0x0000;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x00bf, code lost:
            if (((long) dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).sample_offset[dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0)]) >= dji.midware.media.player.DJIMediaPlayerH1.access$000(r24.this$0)) goto L_0x0000;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x00c8, code lost:
            if (r24.this$0.isPlaying != 2) goto L_0x00f5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
            java.lang.Thread.sleep(200);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x00d0, code lost:
            r19 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
            dji.log.DJILog.exceptionToString(r19);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x00de, code lost:
            r17 = (dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).media_length - dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0)) - 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x010a, code lost:
            if (dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0) <= dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).pos_iframe[0]) goto L_0x0115;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x010c, code lost:
            r24.this$0.mOnIframeRecvListener.onIFrameReceive();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0115, code lost:
            android.util.Log.e(r24.this$0.TAG, "curMp4Index: " + dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0) + "isPlaying:" + r24.this$0.isPlaying);
            r24.this$0.mOnBufferingUpdateListener.onBufferingUpdate(r24.this$0, 100);
            dji.midware.media.player.DJIMediaPlayerH1.access$402(r24.this$0, java.lang.System.currentTimeMillis());
            r24.this$0.qsize = 100;
            r24.this$0.isBuffered = true;
            r24.this$0.isBuffering = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x0182, code lost:
            if (r24.this$0.isPlaying == 2) goto L_0x0194;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x018a, code lost:
            if (r24.this$0.isPlaying == 0) goto L_0x0194;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x018c, code lost:
            r24.this$0.isPlaying = 10;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x0194, code lost:
            r5 = new byte[dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).sample_size[dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0)]];
            r4 = r24.this$0.accessFileLock;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x01b0, code lost:
            monitor-enter(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x01b7, code lost:
            if (r24.this$0.accessFile != null) goto L_0x01bf;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x01b9, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:?, code lost:
            monitor-exit(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
            r24.this$0.accessFile.seek((long) dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).sample_offset[dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0)]);
            r22 = r24.this$0.accessFile.read(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:0x01e8, code lost:
            if (r22 >= 0) goto L_0x020b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:72:0x01ea, code lost:
            android.util.Log.e(r24.this$0.TAG, "read size:" + r22);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:0x020b, code lost:
            dji.midware.media.player.DJIMediaPlayerH1.access$500(r24.this$0, r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:74:0x0227, code lost:
            if (dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0) >= dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).pos_iframe[0]) goto L_0x02e6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:0x0229, code lost:
            r23 = r24.this$0.insertSPSPPS();
            r3 = new byte[(r23.length + r5.length)];
            java.lang.System.arraycopy(r23, 0, r3, 0, r23.length);
            java.lang.System.arraycopy(r5, 0, r3, r23.length, r5.length);
            dji.midware.media.DJIVideoDataRecver.getInstance().onVideoRecv(r3, 0, r3.length, 1, true, -1, 0, -1, 0, dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).width, dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).height, false);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:77:0x0275, code lost:
            if (r24.this$0.isPlaying == 2) goto L_0x027e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:78:0x0277, code lost:
            dji.midware.media.player.DJIMediaPlayerH1.access$308(r24.this$0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:?, code lost:
            dji.midware.media.player.DJIMediaPlayerH1.access$402(r24.this$0, dji.midware.media.player.DJIMediaPlayerH1.access$400(r24.this$0) + ((long) (1000 / dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).fps)));
            dji.midware.media.player.DJIMediaPlayerH1.access$402(r24.this$0, dji.midware.media.player.DJIMediaPlayerH1.access$400(r24.this$0) - java.lang.System.currentTimeMillis());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:82:0x02bc, code lost:
            if (dji.midware.media.player.DJIMediaPlayerH1.access$400(r24.this$0) >= 0) goto L_0x02d3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:83:0x02be, code lost:
            dji.midware.media.player.DJIMediaPlayerH1.access$402(r24.this$0, (long) (1000 / dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).fps));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:84:0x02d3, code lost:
            java.lang.Thread.sleep(dji.midware.media.player.DJIMediaPlayerH1.access$400(r24.this$0));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:85:0x02e0, code lost:
            r19 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:87:?, code lost:
            dji.log.DJILog.exceptionToString(r19);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:88:0x02e6, code lost:
            dji.midware.media.DJIVideoDataRecver.getInstance().onVideoRecv(r5, 0, dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).sample_size[dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0)], dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0), false, -1, 0, -1, 0, dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).width, dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).height, false);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:89:0x0327, code lost:
            r20 = (100.0d * ((double) (dji.midware.media.player.DJIMediaPlayerH1.access$000(r24.this$0) - ((long) dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).sample_offset[dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0)])))) / ((double) (dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).sample_offset[dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0) + r17] - dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).sample_offset[dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0)]));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:90:0x037b, code lost:
            if (r20 >= 0.0d) goto L_0x037f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:91:0x037d, code lost:
            r20 = 0.0d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:92:0x037f, code lost:
            r24.this$0.mOnBufferingUpdateListener.onBufferingUpdate(r24.this$0, (int) r20);
            r24.this$0.isBuffered = false;
            r24.this$0.isBuffering = true;
            android.util.Log.e(r24.this$0.TAG, "waiting buffer read: " + dji.midware.media.player.DJIMediaPlayerH1.access$100(r24.this$0).sample_offset[dji.midware.media.player.DJIMediaPlayerH1.access$300(r24.this$0) + r17] + " write: " + dji.midware.media.player.DJIMediaPlayerH1.access$000(r24.this$0));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:95:?, code lost:
            java.lang.Thread.sleep(100);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:96:0x03ea, code lost:
            r19 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:98:?, code lost:
            dji.log.DJILog.exceptionToString(r19);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r24 = this;
            L_0x0000:
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this
                int r2 = r2.isPlaying
                if (r2 == 0) goto L_0x002c
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this
                int r2 = r2.curMp4Index
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this
                dji.midware.media.mp4.MP4Info r4 = r4.mp4Info
                int r4 = r4.media_length
                if (r2 >= r4) goto L_0x002c
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                java.lang.Object r4 = r2.accessFileLock     // Catch:{ IOException -> 0x00d5 }
                monitor-enter(r4)     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ all -> 0x00db }
                java.io.RandomAccessFile r2 = r2.accessFile     // Catch:{ all -> 0x00db }
                if (r2 != 0) goto L_0x0039
                monitor-exit(r4)     // Catch:{ all -> 0x00db }
            L_0x002c:
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this
                java.lang.String r2 = r2.TAG
                java.lang.String r4 = "video Thread Over!!"
                android.util.Log.e(r2, r4)
                return
            L_0x0039:
                monitor-exit(r4)     // Catch:{ all -> 0x00db }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r2 = r2.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2.media_length     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r4 = r4.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2 - r4
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r4 = r4.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int r4 = r4.fps     // Catch:{ IOException -> 0x00d5 }
                int r4 = r4 * 5
                if (r2 <= r4) goto L_0x00de
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r2 = r2.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2.fps     // Catch:{ IOException -> 0x00d5 }
                int r17 = r2 * 5
            L_0x0067:
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r2 = r2.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int[] r2 = r2.sample_offset     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r4 = r4.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                int r4 = r4 + r17
                r2 = r2[r4]     // Catch:{ IOException -> 0x00d5 }
                long r6 = (long) r2     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                long r8 = r2.curWriteOffset     // Catch:{ IOException -> 0x00d5 }
                int r2 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r2 >= 0) goto L_0x0327
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                int r18 = r2 + r17
            L_0x0094:
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                r0 = r18
                if (r2 >= r0) goto L_0x0000
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r2 = r2.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int[] r2 = r2.sample_offset     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r4 = r4.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                r2 = r2[r4]     // Catch:{ IOException -> 0x00d5 }
                long r6 = (long) r2     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                long r8 = r2.curWriteOffset     // Catch:{ IOException -> 0x00d5 }
                int r2 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r2 >= 0) goto L_0x0000
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2.isPlaying     // Catch:{ IOException -> 0x00d5 }
                r4 = 2
                if (r2 != r4) goto L_0x00f5
                r6 = 200(0xc8, double:9.9E-322)
                java.lang.Thread.sleep(r6)     // Catch:{ InterruptedException -> 0x00d0 }
                goto L_0x0094
            L_0x00d0:
                r19 = move-exception
                dji.log.DJILog.exceptionToString(r19)     // Catch:{ IOException -> 0x00d5 }
                goto L_0x0094
            L_0x00d5:
                r19 = move-exception
                dji.log.DJILog.exceptionToString(r19)
                goto L_0x0000
            L_0x00db:
                r2 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x00db }
                throw r2     // Catch:{ IOException -> 0x00d5 }
            L_0x00de:
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r2 = r2.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2.media_length     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r4 = r4.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2 - r4
                int r17 = r2 + -1
                goto L_0x0067
            L_0x00f5:
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r4 = r4.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int[] r4 = r4.pos_iframe     // Catch:{ IOException -> 0x00d5 }
                r6 = 0
                r4 = r4[r6]     // Catch:{ IOException -> 0x00d5 }
                if (r2 <= r4) goto L_0x0115
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.player.DJIMediaPlayerInterface$OnIframeRecvListener r2 = r2.mOnIframeRecvListener     // Catch:{ IOException -> 0x00d5 }
                r2.onIFrameReceive()     // Catch:{ IOException -> 0x00d5 }
            L_0x0115:
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                java.lang.String r2 = r2.TAG     // Catch:{ IOException -> 0x00d5 }
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00d5 }
                r4.<init>()     // Catch:{ IOException -> 0x00d5 }
                java.lang.String r6 = "curMp4Index: "
                java.lang.StringBuilder r4 = r4.append(r6)     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r6 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r6 = r6.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                java.lang.StringBuilder r4 = r4.append(r6)     // Catch:{ IOException -> 0x00d5 }
                java.lang.String r6 = "isPlaying:"
                java.lang.StringBuilder r4 = r4.append(r6)     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r6 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r6 = r6.isPlaying     // Catch:{ IOException -> 0x00d5 }
                java.lang.StringBuilder r4 = r4.append(r6)     // Catch:{ IOException -> 0x00d5 }
                java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x00d5 }
                android.util.Log.e(r2, r4)     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.player.DJIMediaPlayerInterface$OnBufferingUpdateListener r2 = r2.mOnBufferingUpdateListener     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                r6 = 100
                r2.onBufferingUpdate(r4, r6)     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                long r6 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x00d5 }
                long unused = r2.delay = r6     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                r4 = 100
                r2.qsize = r4     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                r4 = 1
                r2.isBuffered = r4     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                r4 = 0
                r2.isBuffering = r4     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2.isPlaying     // Catch:{ IOException -> 0x00d5 }
                r4 = 2
                if (r2 == r4) goto L_0x0194
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2.isPlaying     // Catch:{ IOException -> 0x00d5 }
                if (r2 == 0) goto L_0x0194
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                r4 = 10
                r2.isPlaying = r4     // Catch:{ IOException -> 0x00d5 }
            L_0x0194:
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r2 = r2.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int[] r2 = r2.sample_size     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r4 = r4.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                r2 = r2[r4]     // Catch:{ IOException -> 0x00d5 }
                byte[] r5 = new byte[r2]     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                java.lang.Object r4 = r2.accessFileLock     // Catch:{ IOException -> 0x00d5 }
                monitor-enter(r4)     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ all -> 0x01bc }
                java.io.RandomAccessFile r2 = r2.accessFile     // Catch:{ all -> 0x01bc }
                if (r2 != 0) goto L_0x01bf
                monitor-exit(r4)     // Catch:{ all -> 0x01bc }
                goto L_0x0000
            L_0x01bc:
                r2 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x01bc }
                throw r2     // Catch:{ IOException -> 0x00d5 }
            L_0x01bf:
                monitor-exit(r4)     // Catch:{ all -> 0x01bc }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                java.io.RandomAccessFile r2 = r2.accessFile     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r4 = r4.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int[] r4 = r4.sample_offset     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r6 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r6 = r6.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                r4 = r4[r6]     // Catch:{ IOException -> 0x00d5 }
                long r6 = (long) r4     // Catch:{ IOException -> 0x00d5 }
                r2.seek(r6)     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                java.io.RandomAccessFile r2 = r2.accessFile     // Catch:{ IOException -> 0x00d5 }
                int r22 = r2.read(r5)     // Catch:{ IOException -> 0x00d5 }
                if (r22 >= 0) goto L_0x020b
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                java.lang.String r2 = r2.TAG     // Catch:{ IOException -> 0x00d5 }
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00d5 }
                r4.<init>()     // Catch:{ IOException -> 0x00d5 }
                java.lang.String r6 = "read size:"
                java.lang.StringBuilder r4 = r4.append(r6)     // Catch:{ IOException -> 0x00d5 }
                r0 = r22
                java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ IOException -> 0x00d5 }
                java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x00d5 }
                android.util.Log.e(r2, r4)     // Catch:{ IOException -> 0x00d5 }
                goto L_0x0094
            L_0x020b:
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                r2.replaceSliceHeader(r5)     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r4 = r4.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int[] r4 = r4.pos_iframe     // Catch:{ IOException -> 0x00d5 }
                r6 = 0
                r4 = r4[r6]     // Catch:{ IOException -> 0x00d5 }
                if (r2 >= r4) goto L_0x02e6
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                byte[] r23 = r2.insertSPSPPS()     // Catch:{ IOException -> 0x00d5 }
                r0 = r23
                int r2 = r0.length     // Catch:{ IOException -> 0x00d5 }
                int r4 = r5.length     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2 + r4
                byte[] r3 = new byte[r2]     // Catch:{ IOException -> 0x00d5 }
                r2 = 0
                r4 = 0
                r0 = r23
                int r6 = r0.length     // Catch:{ IOException -> 0x00d5 }
                r0 = r23
                java.lang.System.arraycopy(r0, r2, r3, r4, r6)     // Catch:{ IOException -> 0x00d5 }
                r2 = 0
                r0 = r23
                int r4 = r0.length     // Catch:{ IOException -> 0x00d5 }
                int r6 = r5.length     // Catch:{ IOException -> 0x00d5 }
                java.lang.System.arraycopy(r5, r2, r3, r4, r6)     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.DJIVideoDataRecver r2 = dji.midware.media.DJIVideoDataRecver.getInstance()     // Catch:{ IOException -> 0x00d5 }
                r4 = 0
                int r5 = r3.length     // Catch:{ IOException -> 0x00d5 }
                r6 = 1
                r7 = 1
                r8 = -1
                r9 = 0
                r10 = -1
                r11 = 0
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r12 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r12 = r12.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int r12 = r12.width     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r13 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r13 = r13.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int r13 = r13.height     // Catch:{ IOException -> 0x00d5 }
                r14 = 0
                r2.onVideoRecv(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14)     // Catch:{ IOException -> 0x00d5 }
            L_0x026e:
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2.isPlaying     // Catch:{ IOException -> 0x00d5 }
                r4 = 2
                if (r2 == r4) goto L_0x027e
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.player.DJIMediaPlayerH1.access$308(r2)     // Catch:{ IOException -> 0x00d5 }
            L_0x027e:
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ InterruptedException -> 0x02e0 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ InterruptedException -> 0x02e0 }
                long r6 = r4.delay     // Catch:{ InterruptedException -> 0x02e0 }
                r4 = 1000(0x3e8, float:1.401E-42)
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r8 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ InterruptedException -> 0x02e0 }
                dji.midware.media.mp4.MP4Info r8 = r8.mp4Info     // Catch:{ InterruptedException -> 0x02e0 }
                int r8 = r8.fps     // Catch:{ InterruptedException -> 0x02e0 }
                int r4 = r4 / r8
                long r8 = (long) r4     // Catch:{ InterruptedException -> 0x02e0 }
                long r6 = r6 + r8
                long unused = r2.delay = r6     // Catch:{ InterruptedException -> 0x02e0 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ InterruptedException -> 0x02e0 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ InterruptedException -> 0x02e0 }
                long r6 = r4.delay     // Catch:{ InterruptedException -> 0x02e0 }
                long r8 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedException -> 0x02e0 }
                long r6 = r6 - r8
                long unused = r2.delay = r6     // Catch:{ InterruptedException -> 0x02e0 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ InterruptedException -> 0x02e0 }
                long r6 = r2.delay     // Catch:{ InterruptedException -> 0x02e0 }
                r8 = 0
                int r2 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r2 >= 0) goto L_0x02d3
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ InterruptedException -> 0x02e0 }
                r4 = 1000(0x3e8, float:1.401E-42)
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r6 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ InterruptedException -> 0x02e0 }
                dji.midware.media.mp4.MP4Info r6 = r6.mp4Info     // Catch:{ InterruptedException -> 0x02e0 }
                int r6 = r6.fps     // Catch:{ InterruptedException -> 0x02e0 }
                int r4 = r4 / r6
                long r6 = (long) r4     // Catch:{ InterruptedException -> 0x02e0 }
                long unused = r2.delay = r6     // Catch:{ InterruptedException -> 0x02e0 }
            L_0x02d3:
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ InterruptedException -> 0x02e0 }
                long r6 = r2.delay     // Catch:{ InterruptedException -> 0x02e0 }
                java.lang.Thread.sleep(r6)     // Catch:{ InterruptedException -> 0x02e0 }
                goto L_0x0094
            L_0x02e0:
                r19 = move-exception
                dji.log.DJILog.exceptionToString(r19)     // Catch:{ IOException -> 0x00d5 }
                goto L_0x0094
            L_0x02e6:
                dji.midware.media.DJIVideoDataRecver r4 = dji.midware.media.DJIVideoDataRecver.getInstance()     // Catch:{ IOException -> 0x00d5 }
                r6 = 0
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r2 = r2.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int[] r2 = r2.sample_size     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r7 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r7 = r7.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                r7 = r2[r7]     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r8 = r2.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                r9 = 0
                r10 = -1
                r11 = 0
                r12 = -1
                r13 = 0
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r2 = r2.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int r14 = r2.width     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r2 = r2.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int r15 = r2.height     // Catch:{ IOException -> 0x00d5 }
                r16 = 0
                r4.onVideoRecv(r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16)     // Catch:{ IOException -> 0x00d5 }
                goto L_0x026e
            L_0x0327:
                r6 = 4636737291354636288(0x4059000000000000, double:100.0)
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                long r8 = r2.curWriteOffset     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r2 = r2.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int[] r2 = r2.sample_offset     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r4 = r4.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                r2 = r2[r4]     // Catch:{ IOException -> 0x00d5 }
                long r10 = (long) r2     // Catch:{ IOException -> 0x00d5 }
                long r8 = r8 - r10
                double r8 = (double) r8     // Catch:{ IOException -> 0x00d5 }
                double r6 = r6 * r8
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r2 = r2.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int[] r2 = r2.sample_offset     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r4 = r4.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                int r4 = r4 + r17
                r2 = r2[r4]     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r4 = r4.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int[] r4 = r4.sample_offset     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r8 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r8 = r8.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                r4 = r4[r8]     // Catch:{ IOException -> 0x00d5 }
                int r2 = r2 - r4
                double r8 = (double) r2     // Catch:{ IOException -> 0x00d5 }
                double r20 = r6 / r8
                r6 = 0
                int r2 = (r20 > r6 ? 1 : (r20 == r6 ? 0 : -1))
                if (r2 >= 0) goto L_0x037f
                r20 = 0
            L_0x037f:
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.player.DJIMediaPlayerInterface$OnBufferingUpdateListener r2 = r2.mOnBufferingUpdateListener     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r4 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                r0 = r20
                int r6 = (int) r0     // Catch:{ IOException -> 0x00d5 }
                r2.onBufferingUpdate(r4, r6)     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                r4 = 0
                r2.isBuffered = r4     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                r4 = 1
                r2.isBuffering = r4     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r2 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                java.lang.String r2 = r2.TAG     // Catch:{ IOException -> 0x00d5 }
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00d5 }
                r4.<init>()     // Catch:{ IOException -> 0x00d5 }
                java.lang.String r6 = "waiting buffer read: "
                java.lang.StringBuilder r4 = r4.append(r6)     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r6 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                dji.midware.media.mp4.MP4Info r6 = r6.mp4Info     // Catch:{ IOException -> 0x00d5 }
                int[] r6 = r6.sample_offset     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r7 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                int r7 = r7.curMp4Index     // Catch:{ IOException -> 0x00d5 }
                int r7 = r7 + r17
                r6 = r6[r7]     // Catch:{ IOException -> 0x00d5 }
                java.lang.StringBuilder r4 = r4.append(r6)     // Catch:{ IOException -> 0x00d5 }
                java.lang.String r6 = " write: "
                java.lang.StringBuilder r4 = r4.append(r6)     // Catch:{ IOException -> 0x00d5 }
                r0 = r24
                dji.midware.media.player.DJIMediaPlayerH1 r6 = dji.midware.media.player.DJIMediaPlayerH1.this     // Catch:{ IOException -> 0x00d5 }
                long r6 = r6.curWriteOffset     // Catch:{ IOException -> 0x00d5 }
                java.lang.StringBuilder r4 = r4.append(r6)     // Catch:{ IOException -> 0x00d5 }
                java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x00d5 }
                android.util.Log.e(r2, r4)     // Catch:{ IOException -> 0x00d5 }
                r6 = 100
                java.lang.Thread.sleep(r6)     // Catch:{ InterruptedException -> 0x03ea }
                goto L_0x0000
            L_0x03ea:
                r19 = move-exception
                dji.log.DJILog.exceptionToString(r19)     // Catch:{ IOException -> 0x00d5 }
                goto L_0x0000
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.midware.media.player.DJIMediaPlayerH1.VideoThread.run():void");
        }
    }

    /* access modifiers changed from: private */
    public void replaceSliceHeader(byte[] frame) {
        int index = 0;
        while (index < this.mp4Info.sample_size[this.curMp4Index] && index >= 0) {
            if (index > frame.length - 4) {
                Log.e(this.TAG, "transform 264 error: index=" + index);
                return;
            }
            int slice_length = MP4BytesUtil.getInt(frame, index);
            if (slice_length < 0) {
                Log.e(this.TAG, "transform 264 error: index=" + index);
                return;
            }
            frame[index] = 0;
            int index2 = index + 1;
            frame[index2] = 0;
            int index3 = index2 + 1;
            frame[index3] = 0;
            int index4 = index3 + 1;
            frame[index4] = 1;
            index = index4 + 1 + slice_length;
        }
    }

    public byte[] insertSPSPPS() {
        byte[] spspps = new byte[(this.mp4Info.sps.length + this.mp4Info.pps.length + 8)];
        spspps[0] = 0;
        int index = 0 + 1;
        spspps[index] = 0;
        int index2 = index + 1;
        spspps[index2] = 0;
        int index3 = index2 + 1;
        spspps[index3] = 1;
        System.arraycopy(this.mp4Info.sps, 0, spspps, index3 + 1, this.mp4Info.sps.length);
        int index4 = this.mp4Info.sps.length + 4;
        spspps[index4] = 0;
        int index5 = index4 + 1;
        spspps[index5] = 0;
        int index6 = index5 + 1;
        spspps[index6] = 0;
        int index7 = index6 + 1;
        spspps[index7] = 1;
        int index8 = index7 + 1;
        System.arraycopy(this.mp4Info.pps, 0, spspps, index8, this.mp4Info.pps.length);
        int index9 = index8 + this.mp4Info.pps.length;
        return spspps;
    }
}
