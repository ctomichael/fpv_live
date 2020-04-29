package dji.component.videoframing;

public interface PhotoShootListener {
    void onFailed(Throwable th);

    void onStart();

    void onSuccess(String str);
}
