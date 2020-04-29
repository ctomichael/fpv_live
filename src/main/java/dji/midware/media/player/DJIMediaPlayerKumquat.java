package dji.midware.media.player;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetPushPlayBackParams;
import dji.midware.data.model.P3.DataCameraSingleChoice;
import dji.midware.data.model.P3.DataCameraVideoControl;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIMediaPlayerKumquat implements DJIMediaPlayerInterface {
    private static final String TAG = "DJIMediaPlayerKumquat";
    private DataCameraGetPushPlayBackParams.MODE curMode = DataCameraGetPushPlayBackParams.getInstance().getMode();
    private int currentPos = -1;
    private int duration = -1;
    private boolean isOver = false;
    private boolean isPaused = false;
    private boolean isPlaying = true;
    private DataCameraVideoControl mDataCameraVideoControl = DataCameraVideoControl.getInstance();
    private int mFileIndex;
    private DJIMediaPlayKumquatListener mPlayKumquatListener;

    public interface DJIMediaPlayKumquatListener {
        void onError(int i);

        void onOver();

        void onPause();

        void onProgress(int i, int i2);

        void onStart();

        void onStop();
    }

    public DJIMediaPlayerKumquat() {
        DJIEventBusUtil.register(this);
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        this.mPlayKumquatListener = null;
    }

    public void setMediaPlayerListener(DJIMediaPlayKumquatListener listener) {
        this.mPlayKumquatListener = listener;
    }

    public void setVideoFileIndex(int index) {
        this.mFileIndex = index;
    }

    public void setDataSource(String path) {
    }

    public void release() {
    }

    public void prepare() {
    }

    public void enterSinglePlayMode(DJIDataCallBack callBack) {
        DataCameraSingleChoice.getInstance().setKey(this.mFileIndex).start(callBack);
    }

    public void restart() {
        DJILog.logWriteI(TAG, "MediaPlayer restart, curMode=" + this.curMode, TAG, new Object[0]);
        if (this.curMode != DataCameraGetPushPlayBackParams.MODE.SingleOver) {
            logErrorStatus();
        } else {
            DataCameraSingleChoice.getInstance().setKey(this.mFileIndex).start(new DJIDataCallBack() {
                /* class dji.midware.media.player.DJIMediaPlayerKumquat.AnonymousClass1 */

                public void onSuccess(Object model) {
                    DJILog.logWriteI(DJIMediaPlayerKumquat.TAG, "MediaPlayer restart success", DJIMediaPlayerKumquat.TAG, new Object[0]);
                }

                public void onFailure(Ccode ccode) {
                    DJILog.logWriteE(DJIMediaPlayerKumquat.TAG, "MediaPlayer restart failure", DJIMediaPlayerKumquat.TAG, new Object[0]);
                }
            });
        }
    }

    public void start() {
        DJILog.logWriteI(TAG, "MediaPlayer start, curMode=" + this.curMode, TAG, new Object[0]);
        if (this.curMode == DataCameraGetPushPlayBackParams.MODE.SinglePlay) {
            logErrorStatus();
        } else {
            this.mDataCameraVideoControl.setControlType(DataCameraVideoControl.ControlType.Start).start(new DJIDataCallBack() {
                /* class dji.midware.media.player.DJIMediaPlayerKumquat.AnonymousClass2 */

                public void onSuccess(Object model) {
                    DJILog.logWriteI(DJIMediaPlayerKumquat.TAG, "MediaPlayer start success", DJIMediaPlayerKumquat.TAG, new Object[0]);
                }

                public void onFailure(Ccode ccode) {
                    DJILog.logWriteE(DJIMediaPlayerKumquat.TAG, "MediaPlayer start failure", DJIMediaPlayerKumquat.TAG, new Object[0]);
                }
            });
        }
    }

    public void stop() {
        DJILog.logWriteI(TAG, "MediaPlayer stop, curMode=" + this.curMode, TAG, new Object[0]);
        if (this.curMode == DataCameraGetPushPlayBackParams.MODE.SingleOver) {
            logErrorStatus();
        } else {
            this.mDataCameraVideoControl.setControlType(DataCameraVideoControl.ControlType.Stop).start(new DJIDataCallBack() {
                /* class dji.midware.media.player.DJIMediaPlayerKumquat.AnonymousClass3 */

                public void onSuccess(Object model) {
                    DJILog.logWriteI(DJIMediaPlayerKumquat.TAG, "MediaPlayer stop success", DJIMediaPlayerKumquat.TAG, new Object[0]);
                }

                public void onFailure(Ccode ccode) {
                    DJILog.logWriteE(DJIMediaPlayerKumquat.TAG, "MediaPlayer stop failure", DJIMediaPlayerKumquat.TAG, new Object[0]);
                }
            });
        }
    }

    public void pause() {
        DJILog.logWriteI(TAG, "MediaPlayer pause, curMode=" + this.curMode, TAG, new Object[0]);
        if (this.curMode == DataCameraGetPushPlayBackParams.MODE.SinglePause || this.curMode == DataCameraGetPushPlayBackParams.MODE.SingleOver) {
            logErrorStatus();
        } else {
            this.mDataCameraVideoControl.setControlType(DataCameraVideoControl.ControlType.Pause).start(new DJIDataCallBack() {
                /* class dji.midware.media.player.DJIMediaPlayerKumquat.AnonymousClass4 */

                public void onSuccess(Object model) {
                    DJILog.logWriteI(DJIMediaPlayerKumquat.TAG, "MediaPlayer pause success", DJIMediaPlayerKumquat.TAG, new Object[0]);
                }

                public void onFailure(Ccode ccode) {
                    DJILog.logWriteE(DJIMediaPlayerKumquat.TAG, "MediaPlayer pause failure", DJIMediaPlayerKumquat.TAG, new Object[0]);
                }
            });
        }
    }

    public int getCurrentPosition() {
        return this.currentPos;
    }

    public int getDuration() {
        return this.duration;
    }

    public void seekTo(final long msec) {
        seekTo(msec, new DJIDataCallBack() {
            /* class dji.midware.media.player.DJIMediaPlayerKumquat.AnonymousClass5 */

            public void onSuccess(Object model) {
                DJILog.logWriteI(DJIMediaPlayerKumquat.TAG, "MediaPlayer seek to " + msec + " success", DJIMediaPlayerKumquat.TAG, new Object[0]);
            }

            public void onFailure(Ccode ccode) {
                DJILog.logWriteE(DJIMediaPlayerKumquat.TAG, "MediaPlayer seek to " + msec + " failure", DJIMediaPlayerKumquat.TAG, new Object[0]);
            }
        });
    }

    public void seekTo(long msec, DJIDataCallBack callBack) {
        if (this.curMode == DataCameraGetPushPlayBackParams.MODE.SingleOver) {
            restart();
        }
        DJILog.logWriteI(TAG, "MediaPlayer seek to " + msec, TAG, new Object[0]);
        this.mDataCameraVideoControl.setControlType(DataCameraVideoControl.ControlType.StepTo).setProgress((int) msec).start(callBack);
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public boolean isOver() {
        return this.isOver;
    }

    public void reset() {
    }

    public void setAudioStreamType(int streamtype) {
    }

    private void logErrorStatus() {
        DJILog.logWriteE(TAG, "state error, current state：" + this.curMode.name(), TAG, new Object[0]);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushPlayBackParams params) {
        if (this.mPlayKumquatListener == null) {
            DJILog.logWriteE(TAG, "listener null", TAG, new Object[0]);
            return;
        }
        if (this.curMode != params.getMode()) {
            this.curMode = params.getMode();
            if (this.curMode == DataCameraGetPushPlayBackParams.MODE.SinglePlay) {
                this.isPlaying = true;
                this.isPaused = false;
                this.isOver = false;
                this.mPlayKumquatListener.onStart();
            } else if (this.curMode == DataCameraGetPushPlayBackParams.MODE.SinglePause) {
                this.isPlaying = false;
                this.isPaused = true;
                this.isOver = false;
                this.mPlayKumquatListener.onPause();
            } else if (this.curMode == DataCameraGetPushPlayBackParams.MODE.SingleOver) {
                this.isPlaying = false;
                this.isPaused = false;
                this.isOver = true;
                this.mPlayKumquatListener.onOver();
            } else if (this.curMode == DataCameraGetPushPlayBackParams.MODE.TranscodeError) {
                this.mPlayKumquatListener.onError(DataCameraGetPushPlayBackParams.MODE.TranscodeError.value());
            }
        }
        if (this.curMode != DataCameraGetPushPlayBackParams.MODE.SingleOver) {
            this.duration = params.getTotalTimeForKumquat();
        }
        if (this.curMode == DataCameraGetPushPlayBackParams.MODE.SinglePlay) {
            this.currentPos = params.getCurrentForKumquat();
            DJILog.logWriteI(TAG, "MediaPlayer progress change: current position = " + this.currentPos + " duration = " + this.duration, TAG, new Object[0]);
            this.mPlayKumquatListener.onProgress(this.currentPos, this.duration);
        }
    }

    public void setDisplay(Object display) {
    }

    @NonNull
    public String toString() {
        return "DJIMediaPlayerKumquat{curMode=" + this.curMode + ", currentPos=" + this.currentPos + ", duration=" + this.duration + ", isPlaying=" + this.isPlaying + ", isPaused=" + this.isPaused + ", isOver=" + this.isOver + '}';
    }
}
