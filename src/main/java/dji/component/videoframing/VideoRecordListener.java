package dji.component.videoframing;

public interface VideoRecordListener {
    void onError(Throwable th);

    void onRecording(int i);

    void onSaved(String str);

    void onStart();
}
