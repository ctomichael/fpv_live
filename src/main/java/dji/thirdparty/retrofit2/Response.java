package dji.thirdparty.retrofit2;

import dji.thirdparty.okhttp3.Headers;
import dji.thirdparty.okhttp3.Protocol;
import dji.thirdparty.okhttp3.Request;
import dji.thirdparty.okhttp3.Response;
import dji.thirdparty.okhttp3.ResponseBody;

public final class Response<T> {
    private final T body;
    private final ResponseBody errorBody;
    private final dji.thirdparty.okhttp3.Response rawResponse;

    public static <T> Response<T> success(T body2) {
        return success(body2, new Response.Builder().code(200).message("OK").protocol(Protocol.HTTP_1_1).request(new Request.Builder().url("http://localhost/").build()).build());
    }

    public static <T> Response<T> success(T body2, Headers headers) {
        if (headers != null) {
            return success(body2, new Response.Builder().code(200).message("OK").protocol(Protocol.HTTP_1_1).headers(headers).request(new Request.Builder().url("http://localhost/").build()).build());
        }
        throw new NullPointerException("headers == null");
    }

    public static <T> Response<T> success(T body2, dji.thirdparty.okhttp3.Response rawResponse2) {
        if (rawResponse2 == null) {
            throw new NullPointerException("rawResponse == null");
        } else if (rawResponse2.isSuccessful()) {
            return new Response<>(rawResponse2, body2, null);
        } else {
            throw new IllegalArgumentException("rawResponse must be successful response");
        }
    }

    public static <T> Response<T> error(int code, ResponseBody body2) {
        if (code >= 400) {
            return error(body2, new Response.Builder().code(code).protocol(Protocol.HTTP_1_1).request(new Request.Builder().url("http://localhost/").build()).build());
        }
        throw new IllegalArgumentException("code < 400: " + code);
    }

    public static <T> Response<T> error(ResponseBody body2, dji.thirdparty.okhttp3.Response rawResponse2) {
        if (body2 == null) {
            throw new NullPointerException("body == null");
        } else if (rawResponse2 == null) {
            throw new NullPointerException("rawResponse == null");
        } else if (!rawResponse2.isSuccessful()) {
            return new Response<>(rawResponse2, null, body2);
        } else {
            throw new IllegalArgumentException("rawResponse should not be successful response");
        }
    }

    private Response(dji.thirdparty.okhttp3.Response rawResponse2, T body2, ResponseBody errorBody2) {
        this.rawResponse = rawResponse2;
        this.body = body2;
        this.errorBody = errorBody2;
    }

    public dji.thirdparty.okhttp3.Response raw() {
        return this.rawResponse;
    }

    public int code() {
        return this.rawResponse.code();
    }

    public String message() {
        return this.rawResponse.message();
    }

    public Headers headers() {
        return this.rawResponse.headers();
    }

    public boolean isSuccessful() {
        return this.rawResponse.isSuccessful();
    }

    public T body() {
        return this.body;
    }

    public ResponseBody errorBody() {
        return this.errorBody;
    }

    public String toString() {
        return this.rawResponse.toString();
    }
}
