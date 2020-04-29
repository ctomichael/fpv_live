package dji.component.playback.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface IDeviceMediaPlayer {

    public interface StateListener {
        void onError(int i);

        void onPause();

        void onPlay();

        void onProgressChange(float f, float f2);

        void onStop();

        void onWait(float f);
    }

    void changeSurfaceSize(int i, int i2);

    void config(int i, int i2, @NonNull String str, long j, int i3);

    void destroy();

    int getDuration();

    float getFrameRate();

    float getProgressCache();

    float getProgressPlay();

    float getScale();

    boolean isConfig();

    boolean isPause();

    boolean isPlay();

    void pause();

    void play();

    void releaseSurface();

    void seek(float f);

    void setStateListener(@Nullable StateListener stateListener);

    void setSurface(@NonNull Object obj, int i, int i2);

    void stop();
}
