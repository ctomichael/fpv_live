package dji.thirdparty.afinal.http;

import android.os.SystemClock;
import dji.thirdparty.afinal.core.AsyncTask;
import dji.thirdparty.afinal.http.entityhandler.EntityCallBack;
import dji.thirdparty.afinal.http.entityhandler.FileEntityHandler;
import dji.thirdparty.afinal.http.entityhandler.StringEntityHandler;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

public class HttpHandler<T> extends AsyncTask<Object, Object, Object> implements EntityCallBack {
    private static final int UPDATE_FAILURE = 3;
    private static final int UPDATE_LOADING = 2;
    private static final int UPDATE_START = 1;
    private static final int UPDATE_SUCCESS = 4;
    private final AjaxCallBack<T> callback;
    private String charset;
    private boolean checkContentLength = false;
    private final AbstractHttpClient client;
    private final HttpContext context;
    private int executionCount = 0;
    private boolean isResume = false;
    private final FileEntityHandler mFileEntityHandler = new FileEntityHandler();
    private final StringEntityHandler mStrEntityHandler = new StringEntityHandler();
    private String targetUrl = null;
    private long time;

    public HttpHandler(AbstractHttpClient client2, HttpContext context2, AjaxCallBack<T> callback2, String charset2) {
        this.client = client2;
        this.context = context2;
        this.callback = callback2;
        this.charset = charset2;
    }

    private void makeRequestWithRetries(HttpUriRequest request) throws IOException {
        if (this.isResume && this.targetUrl != null) {
            File downloadFile = new File(this.targetUrl);
            long fileLen = 0;
            if (downloadFile.isFile() && downloadFile.exists()) {
                fileLen = downloadFile.length();
            }
            if (fileLen > 0) {
                request.setHeader("Range", "bytes=" + fileLen + "-");
            }
        }
        boolean retry = true;
        IOException cause = null;
        HttpRequestRetryHandler retryHandler = this.client.getHttpRequestRetryHandler();
        while (retry) {
            try {
                if (!isCancelled()) {
                    HttpResponse response = this.client.execute(request, this.context);
                    if (!isCancelled()) {
                        handleResponse(response);
                        return;
                    }
                    return;
                }
                return;
            } catch (UnknownHostException e) {
                publishProgress(3, e, 0, "unknownHostException：can't resolve host");
                return;
            } catch (IOException e2) {
                cause = e2;
                int i = this.executionCount + 1;
                this.executionCount = i;
                retry = retryHandler.retryRequest(cause, i, this.context);
            } catch (NullPointerException e3) {
                cause = new IOException("NPE in HttpClient" + e3.getMessage());
                int i2 = this.executionCount + 1;
                this.executionCount = i2;
                retry = retryHandler.retryRequest(cause, i2, this.context);
            } catch (Exception e4) {
                cause = new IOException("Exception" + e4.getMessage());
                int i3 = this.executionCount + 1;
                this.executionCount = i3;
                retry = retryHandler.retryRequest(cause, i3, this.context);
            }
        }
        if (cause != null) {
            throw cause;
        }
        throw new IOException("未知网络错误");
    }

    /* access modifiers changed from: protected */
    public Object doInBackground(Object... params) {
        if (params != null && params.length == 4) {
            this.targetUrl = String.valueOf(params[1]);
            this.isResume = ((Boolean) params[2]).booleanValue();
            this.checkContentLength = ((Boolean) params[3]).booleanValue();
        }
        try {
            publishProgress(1);
            makeRequestWithRetries((HttpUriRequest) params[0]);
            return null;
        } catch (IOException e) {
            publishProgress(3, e, 0, e.getMessage());
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Object... values) {
        switch (Integer.valueOf(String.valueOf(values[0])).intValue()) {
            case 1:
                if (this.callback != null) {
                    this.callback.onStart(this.isResume);
                    break;
                }
                break;
            case 2:
                if (this.callback != null) {
                    this.callback.onLoading(Long.valueOf(String.valueOf(values[1])).longValue(), Long.valueOf(String.valueOf(values[2])).longValue());
                    break;
                }
                break;
            case 3:
                if (this.callback != null) {
                    this.callback.onFailure((Throwable) values[1], ((Integer) values[2]).intValue(), (String) values[3]);
                    break;
                }
                break;
            case 4:
                if (this.callback != null) {
                    this.callback.onSuccess(values[1]);
                    break;
                }
                break;
        }
        super.onProgressUpdate(values);
    }

    public boolean isStop() {
        return this.mFileEntityHandler.isStop();
    }

    public void stop() {
        this.mFileEntityHandler.setStop(true);
    }

    private void handleResponse(HttpResponse response) {
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() >= 300) {
            String errorMsg = "response status error code:" + status.getStatusCode();
            if (status.getStatusCode() == 416 && this.isResume) {
                errorMsg = errorMsg + " \n maybe you have download complete.";
            }
            publishProgress(3, new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()), Integer.valueOf(status.getStatusCode()), errorMsg);
            return;
        }
        try {
            HttpEntity entity = response.getEntity();
            Object responseBody = null;
            if (entity != null) {
                this.time = SystemClock.uptimeMillis();
                if (this.targetUrl != null) {
                    responseBody = this.mFileEntityHandler.handleEntity(entity, this, this.targetUrl, this.isResume, this.checkContentLength);
                    if (responseBody instanceof String) {
                        publishProgress(3, null, 0, (String) responseBody);
                        return;
                    }
                } else {
                    responseBody = this.mStrEntityHandler.handleEntity(entity, this, this.charset);
                }
            }
            publishProgress(4, responseBody);
        } catch (IOException e) {
            publishProgress(3, e, 0, e.getMessage());
        }
    }

    public void callBack(long count, long current, boolean mustNoticeUI) {
        if (this.callback != null && this.callback.isProgress()) {
            if (mustNoticeUI) {
                publishProgress(2, Long.valueOf(count), Long.valueOf(current));
                return;
            }
            long thisTime = SystemClock.uptimeMillis();
            if (thisTime - this.time >= ((long) this.callback.getRate())) {
                this.time = thisTime;
                publishProgress(2, Long.valueOf(count), Long.valueOf(current));
            }
        }
    }
}
