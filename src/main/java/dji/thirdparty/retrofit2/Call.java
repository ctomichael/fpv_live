package dji.thirdparty.retrofit2;

import dji.thirdparty.okhttp3.Request;
import java.io.IOException;

public interface Call<T> extends Cloneable {
    void cancel();

    Call<T> clone();

    void enqueue(Callback<T> callback);

    Response<T> execute() throws IOException;

    boolean isCanceled();

    boolean isExecuted();

    Request request();
}
