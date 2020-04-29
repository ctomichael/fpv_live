package dji.thirdparty.retrofit2.adapter.rxjava2;

import dji.thirdparty.retrofit2.Response;

public final class Result<T> {
    private final Throwable error;
    private final Response<T> response;

    public static <T> Result<T> error(Throwable error2) {
        if (error2 != null) {
            return new Result<>(null, error2);
        }
        throw new NullPointerException("error == null");
    }

    public static <T> Result<T> response(Response<T> response2) {
        if (response2 != null) {
            return new Result<>(response2, null);
        }
        throw new NullPointerException("response == null");
    }

    private Result(Response<T> response2, Throwable error2) {
        this.response = response2;
        this.error = error2;
    }

    public Response<T> response() {
        return this.response;
    }

    public Throwable error() {
        return this.error;
    }

    public boolean isError() {
        return this.error != null;
    }
}
