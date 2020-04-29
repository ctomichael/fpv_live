package dji.thirdparty.retrofit2.adapter.rxjava2;

import dji.thirdparty.io.reactivex.BackpressureStrategy;
import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.Scheduler;
import dji.thirdparty.retrofit2.Call;
import dji.thirdparty.retrofit2.CallAdapter;
import dji.thirdparty.retrofit2.Response;
import java.lang.reflect.Type;

final class RxJava2CallAdapter<R> implements CallAdapter<R, Object> {
    private final boolean isAsync;
    private final boolean isBody;
    private final boolean isCompletable;
    private final boolean isFlowable;
    private final boolean isMaybe;
    private final boolean isResult;
    private final boolean isSingle;
    private final Type responseType;
    private final Scheduler scheduler;

    RxJava2CallAdapter(Type responseType2, Scheduler scheduler2, boolean isAsync2, boolean isResult2, boolean isBody2, boolean isFlowable2, boolean isSingle2, boolean isMaybe2, boolean isCompletable2) {
        this.responseType = responseType2;
        this.scheduler = scheduler2;
        this.isAsync = isAsync2;
        this.isResult = isResult2;
        this.isBody = isBody2;
        this.isFlowable = isFlowable2;
        this.isSingle = isSingle2;
        this.isMaybe = isMaybe2;
        this.isCompletable = isCompletable2;
    }

    public Type responseType() {
        return this.responseType;
    }

    public Object adapt(Call<R> call) {
        Observable<Response<R>> responseObservable;
        Observable<Response<R>> observable;
        if (this.isAsync) {
            responseObservable = new CallEnqueueObservable<>(call);
        } else {
            responseObservable = new CallExecuteObservable<>(call);
        }
        if (this.isResult) {
            observable = new ResultObservable<>(responseObservable);
        } else if (this.isBody) {
            observable = new BodyObservable<>(responseObservable);
        } else {
            observable = responseObservable;
        }
        if (this.scheduler != null) {
            observable = observable.subscribeOn(this.scheduler);
        }
        if (this.isFlowable) {
            return observable.toFlowable(BackpressureStrategy.LATEST);
        }
        if (this.isSingle) {
            return observable.singleOrError();
        }
        if (this.isMaybe) {
            return observable.singleElement();
        }
        if (this.isCompletable) {
            return observable.ignoreElements();
        }
        return observable;
    }
}
