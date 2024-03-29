package dji.thirdparty.okhttp3.internal.http;

import dji.thirdparty.okhttp3.Request;
import dji.thirdparty.okhttp3.Response;
import dji.thirdparty.okhttp3.ResponseBody;
import java.io.IOException;
import okio.Sink;

public interface HttpCodec {
    public static final int DISCARD_STREAM_TIMEOUT_MILLIS = 100;

    void cancel();

    Sink createRequestBody(Request request, long j);

    void finishRequest() throws IOException;

    void flushRequest() throws IOException;

    ResponseBody openResponseBody(Response response) throws IOException;

    Response.Builder readResponseHeaders(boolean z) throws IOException;

    void writeRequestHeaders(Request request) throws IOException;
}
