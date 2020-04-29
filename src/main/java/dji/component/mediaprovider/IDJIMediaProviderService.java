package dji.component.mediaprovider;

import android.support.annotation.NonNull;

public interface IDJIMediaProviderService {
    void fastScan();

    void startScan();

    void startScan(@NonNull String str);
}
