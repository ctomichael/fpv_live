package dji.component.productrgnz;

import io.reactivex.Observable;

public interface IProductRecognizeService {
    public static final String NAME = "ProductRecognizeService";

    void configForDebugApp();

    Observable<Boolean> getSupportDJIFlyObservable();

    boolean isSupportDJIFly();
}
