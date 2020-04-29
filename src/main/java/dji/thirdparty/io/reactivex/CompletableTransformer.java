package dji.thirdparty.io.reactivex;

public interface CompletableTransformer {
    CompletableSource apply(Completable completable);
}
