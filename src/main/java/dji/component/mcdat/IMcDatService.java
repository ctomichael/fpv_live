package dji.component.mcdat;

import io.reactivex.Single;

public interface IMcDatService {
    public static final String NAME = "McDatService";

    void cancelUpload();

    Single<Object> getUploadTaskObservable();

    void onDestroy();

    void startUploadMcDat();
}
