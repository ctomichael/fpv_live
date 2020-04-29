package dji.thirdparty.okhttp3.internal.cache;

import dji.thirdparty.okhttp3.Request;
import dji.thirdparty.okhttp3.Response;
import java.io.IOException;

public interface InternalCache {
    Response get(Request request) throws IOException;

    CacheRequest put(Response response) throws IOException;

    void remove(Request request) throws IOException;

    void trackConditionalCacheHit();

    void trackResponse(CacheStrategy cacheStrategy);

    void update(Response response, Response response2);
}
