package dji.thirdparty.retrofit2.adapter.rxjava2;

import dji.thirdparty.retrofit2.Response;

@Deprecated
public final class HttpException extends dji.thirdparty.retrofit2.HttpException {
    public HttpException(Response<?> response) {
        super(response);
    }
}
