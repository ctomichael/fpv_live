package dji.thirdparty.afinal.http;

import dji.thirdparty.afinal.http.entityhandler.StringEntityHandler;
import java.io.IOException;
import java.net.UnknownHostException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

public class SyncRequestHandler {
    private String charset;
    private final AbstractHttpClient client;
    private final HttpContext context;
    private final StringEntityHandler entityHandler = new StringEntityHandler();
    private int executionCount = 0;

    public SyncRequestHandler(AbstractHttpClient client2, HttpContext context2, String charset2) {
        this.client = client2;
        this.context = context2;
        this.charset = charset2;
    }

    private Object makeRequestWithRetries(HttpUriRequest request) throws IOException {
        boolean retry = true;
        IOException cause = null;
        HttpRequestRetryHandler retryHandler = this.client.getHttpRequestRetryHandler();
        while (retry) {
            try {
                return this.entityHandler.handleEntity(this.client.execute(request, this.context).getEntity(), null, this.charset);
            } catch (UnknownHostException e) {
                cause = e;
                int i = this.executionCount + 1;
                this.executionCount = i;
                retry = retryHandler.retryRequest(cause, i, this.context);
            } catch (IOException e2) {
                cause = e2;
                int i2 = this.executionCount + 1;
                this.executionCount = i2;
                retry = retryHandler.retryRequest(cause, i2, this.context);
            } catch (NullPointerException e3) {
                cause = new IOException("NPE in HttpClient" + e3.getMessage());
                int i3 = this.executionCount + 1;
                this.executionCount = i3;
                retry = retryHandler.retryRequest(cause, i3, this.context);
            } catch (Exception e4) {
                cause = new IOException("Exception" + e4.getMessage());
                int i4 = this.executionCount + 1;
                this.executionCount = i4;
                retry = retryHandler.retryRequest(cause, i4, this.context);
            }
        }
        if (cause != null) {
            throw cause;
        }
        throw new IOException("未知网络错误");
    }

    public Object sendRequest(HttpUriRequest... params) {
        try {
            return makeRequestWithRetries(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
