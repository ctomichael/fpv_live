package dji.midware.media.record;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.record.RecorderManager;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public interface RecorderInterface {
    String getRecordingFileName();

    boolean isRecordingToExternalSd();

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    void onEvent3BackgroundThread(RecorderManager.Service_Action service_Action);
}
