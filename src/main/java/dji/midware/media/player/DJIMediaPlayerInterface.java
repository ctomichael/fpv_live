package dji.midware.media.player;

import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.album.model.DJIAlbumPullErrorType;

@EXClassNullAway
public interface DJIMediaPlayerInterface {

    public interface OnPreparedListener {
        void onPrepared(DJIMediaPlayer dJIMediaPlayer);
    }

    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(DJIMediaPlayer dJIMediaPlayer, int i);
    }

    public interface OnIframeRecvListener {
        void onIFrameReceive();
    }

    public interface OnErrorListener {
        void onError(DJIMediaPlayer dJIMediaPlayer, DJIAlbumPullErrorType dJIAlbumPullErrorType);
    }

    public interface OnCompletionListener {
        void onCompletion(DJIMediaPlayer dJIMediaPlayer);
    }

    public interface OnTimeUpdateListener {
        void onUpdate(DJIMediaPlayer dJIMediaPlayer, int i, int i2);
    }

    public interface OnSeekCompleteListener {
        void onSeekComplete(DJIMediaPlayer dJIMediaPlayer);
    }

    public enum STATE {
        READY,
        STOP,
        PAUSE,
        PLAY
    }

    int getCurrentPosition();

    int getDuration();

    boolean isPaused();

    boolean isPlaying();

    void pause();

    void prepare();

    void release();

    void reset();

    void seekTo(long j);

    void setAudioStreamType(int i);

    void setDataSource(String str);

    void setDisplay(Object obj);

    void start();

    void stop();
}
