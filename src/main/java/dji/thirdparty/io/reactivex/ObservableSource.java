package dji.thirdparty.io.reactivex;

public interface ObservableSource<T> {
    void subscribe(Observer<? super Long> observer);
}
