package dji.thirdparty.afinal;

import android.os.Build;
import android.util.Log;
import com.dji.frame.util.V_AppUtils;
import dji.component.accountcenter.IMemberProtocol;
import dji.midware.media.DJIVideoDecoder;
import dji.publics.protocol.ResponseBase;
import dji.thirdparty.afinal.http.AjaxCallBack;
import dji.thirdparty.afinal.http.AjaxParams;
import dji.thirdparty.afinal.http.HttpHandler;
import dji.thirdparty.afinal.http.RetryHandler;
import dji.thirdparty.afinal.http.SyncRequestHandler;
import dji.thirdparty.afinal.utils.HttpsHelper;
import dji.thirdparty.org.java_websocket.WebSocket;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

@Deprecated
public class FinalHttp {
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    private static final String ENCODING_GZIP = "gzip";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    /* access modifiers changed from: private */
    public static final String TAG = FinalHttp.class.getSimpleName();
    private static final Executor executor = Executors.newFixedThreadPool(httpThreadCount, sThreadFactory);
    private static int httpThreadCount = 6;
    private static int maxConnections = 10;
    private static int maxRetries = 3;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        /* class dji.thirdparty.afinal.FinalHttp.AnonymousClass1 */
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            Thread tread = new Thread(r, "FinalHttp #" + this.mCount.getAndIncrement());
            tread.setPriority(4);
            return tread;
        }
    };
    private static int socketConnectTimeout = 4000;
    private static int socketSoTimeout = 30000;
    private static int socketTimeout = DJIVideoDecoder.connectLosedelay;
    private String charset = "utf-8";
    /* access modifiers changed from: private */
    public final Map<String, String> clientHeaderMap;
    private final DefaultHttpClient httpClient;
    private final HttpContext httpContext;

    @Deprecated
    public FinalHttp() {
        BasicHttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter("http.protocol.allow-circular-redirects", true);
        ConnManagerParams.setTimeout(httpParams, (long) socketTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(maxConnections));
        ConnManagerParams.setMaxTotalConnections(httpParams, 10);
        HttpConnectionParams.setSoTimeout(httpParams, socketSoTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, socketConnectTimeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), (int) WebSocket.DEFAULT_WSS_PORT));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        this.httpContext = new SyncBasicHttpContext(new BasicHttpContext());
        this.httpClient = new DefaultHttpClient(cm, httpParams);
        this.httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            /* class dji.thirdparty.afinal.FinalHttp.AnonymousClass2 */

            public void process(HttpRequest request, HttpContext context) {
                if (!request.containsHeader(FinalHttp.HEADER_ACCEPT_ENCODING)) {
                    request.addHeader(FinalHttp.HEADER_ACCEPT_ENCODING, FinalHttp.ENCODING_GZIP);
                }
                for (String header : FinalHttp.this.clientHeaderMap.keySet()) {
                    request.addHeader(header, (String) FinalHttp.this.clientHeaderMap.get(header));
                }
            }
        });
        this.httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            /* class dji.thirdparty.afinal.FinalHttp.AnonymousClass3 */

            public void process(HttpResponse response, HttpContext context) {
                Header encoding;
                HttpEntity entity = response.getEntity();
                if (entity != null && (encoding = entity.getContentEncoding()) != null) {
                    for (HeaderElement element : encoding.getElements()) {
                        if (element.getName().equalsIgnoreCase(FinalHttp.ENCODING_GZIP)) {
                            response.setEntity(new InflatingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }
        });
        this.httpClient.setRedirectHandler(new SpaceRedirectHandler());
        this.httpClient.setHttpRequestRetryHandler(new RetryHandler(maxRetries));
        this.clientHeaderMap = new HashMap();
        configUserAgent("DJI/Android/" + Build.PRODUCT + IMemberProtocol.PARAM_SEPERATOR + Build.MODEL + IMemberProtocol.PARAM_SEPERATOR + Build.BRAND + IMemberProtocol.PARAM_SEPERATOR + Build.VERSION.SDK_INT + IMemberProtocol.PARAM_SEPERATOR);
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public HttpContext getHttpContext() {
        return this.httpContext;
    }

    public void configCharset(String charSet) {
        if (charSet != null && charSet.trim().length() != 0) {
            this.charset = charSet;
        }
    }

    public void configCookieStore(CookieStore cookieStore) {
        this.httpContext.setAttribute("http.cookie-store", cookieStore);
    }

    private void configUserAgent(String userAgent) {
        HttpProtocolParams.setUserAgent(this.httpClient.getParams(), userAgent);
    }

    public void configTimeout(int timeout) {
        HttpParams httpParams = this.httpClient.getParams();
        ConnManagerParams.setTimeout(httpParams, (long) timeout);
        HttpConnectionParams.setSoTimeout(httpParams, timeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
    }

    public void configSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        if (V_AppUtils.sSSLSwitch) {
            this.httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sslSocketFactory, (int) WebSocket.DEFAULT_WSS_PORT));
        }
    }

    public void configRequestExecutionRetryCount(int count) {
        this.httpClient.setHttpRequestRetryHandler(new RetryHandler(count));
    }

    public void addHeader(String header, String value) {
        this.clientHeaderMap.put(header, value);
    }

    public void get(String url, AjaxCallBack<? extends Object> callBack) {
        get(url, null, callBack);
    }

    public void get(String url, AjaxParams params, AjaxCallBack<? extends Object> callBack) {
        sendRequest(this.httpClient, this.httpContext, new HttpGet(getUrlWithQueryString(url, params)), null, callBack);
    }

    public void get(String url, Header[] headers, AjaxParams params, AjaxCallBack<? extends Object> callBack) {
        HttpUriRequest request = new HttpGet(getUrlWithQueryString(url, params));
        if (headers != null) {
            request.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, request, null, callBack);
    }

    public Object getSync(String url) {
        return getSync(url, null);
    }

    public Object getSync(String url, AjaxParams params) {
        return sendSyncRequest(this.httpClient, this.httpContext, new HttpGet(getUrlWithQueryString(url, params)), null);
    }

    public Object getSync(String url, Header[] headers, AjaxParams params) {
        HttpUriRequest request = new HttpGet(getUrlWithQueryString(url, params));
        if (headers != null) {
            request.setHeaders(headers);
        }
        return sendSyncRequest(this.httpClient, this.httpContext, request, null);
    }

    public void post(String url, AjaxCallBack<? extends Object> callBack) {
        post(url, null, callBack);
    }

    public void post(String url, AjaxParams params, AjaxCallBack<? extends Object> callBack) {
        post(url, paramsToEntity(params), null, callBack);
    }

    public void post(String url, HttpEntity entity, String contentType, AjaxCallBack<? extends Object> callBack) {
        sendRequest(this.httpClient, this.httpContext, addEntityToRequestBase(new HttpPost(url), entity), contentType, callBack);
    }

    public <T> void post(String url, Header[] headers, AjaxParams params, String contentType, AjaxCallBack<T> callBack) {
        HttpEntityEnclosingRequestBase request = new HttpPost(url);
        if (params != null) {
            request.setEntity(paramsToEntity(params));
        }
        if (headers != null) {
            request.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, request, contentType, callBack);
    }

    public void post(String url, Header[] headers, HttpEntity entity, String contentType, AjaxCallBack<? extends Object> callBack) {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPost(url), entity);
        if (headers != null) {
            request.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, request, contentType, callBack);
    }

    public Object postSync(String url) {
        return postSync(url, null);
    }

    public Object postSync(String url, AjaxParams params) {
        return postSync(url, paramsToEntity(params), null);
    }

    public Object postSync(String url, HttpEntity entity, String contentType) {
        return sendSyncRequest(this.httpClient, this.httpContext, addEntityToRequestBase(new HttpPost(url), entity), contentType);
    }

    public Object postSync(String url, Header[] headers, AjaxParams params, String contentType) {
        HttpEntityEnclosingRequestBase request = new HttpPost(url);
        if (params != null) {
            request.setEntity(paramsToEntity(params));
        }
        if (headers != null) {
            request.setHeaders(headers);
        }
        return sendSyncRequest(this.httpClient, this.httpContext, request, contentType);
    }

    public Object postSync(String url, Header[] headers, HttpEntity entity, String contentType) {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPost(url), entity);
        if (headers != null) {
            request.setHeaders(headers);
        }
        return sendSyncRequest(this.httpClient, this.httpContext, request, contentType);
    }

    public void put(String url, AjaxCallBack<? extends Object> callBack) {
        put(url, null, callBack);
    }

    public void put(String url, AjaxParams params, AjaxCallBack<? extends Object> callBack) {
        put(url, paramsToEntity(params), null, callBack);
    }

    public void put(String url, HttpEntity entity, String contentType, AjaxCallBack<? extends Object> callBack) {
        sendRequest(this.httpClient, this.httpContext, addEntityToRequestBase(new HttpPut(url), entity), contentType, callBack);
    }

    public void put(String url, Header[] headers, HttpEntity entity, String contentType, AjaxCallBack<? extends Object> callBack) {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPut(url), entity);
        if (headers != null) {
            request.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, request, contentType, callBack);
    }

    public Object putSync(String url) {
        return putSync(url, null);
    }

    public Object putSync(String url, AjaxParams params) {
        return putSync(url, paramsToEntity(params), null);
    }

    public Object putSync(String url, HttpEntity entity, String contentType) {
        return putSync(url, null, entity, contentType);
    }

    public Object putSync(String url, Header[] headers, HttpEntity entity, String contentType) {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPut(url), entity);
        if (headers != null) {
            request.setHeaders(headers);
        }
        return sendSyncRequest(this.httpClient, this.httpContext, request, contentType);
    }

    public void delete(String url, AjaxCallBack<? extends Object> callBack) {
        sendRequest(this.httpClient, this.httpContext, new HttpDelete(url), null, callBack);
    }

    public void delete(String url, AjaxParams params, AjaxCallBack<? extends Object> callBack) {
        sendRequest(this.httpClient, this.httpContext, new HttpDelete(getUrlWithQueryString(url, params)), null, callBack);
    }

    public void delete(String url, Header[] headers, AjaxCallBack<? extends Object> callBack) {
        HttpDelete delete = new HttpDelete(url);
        if (headers != null) {
            delete.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, delete, null, callBack);
    }

    public Object deleteSync(String url) {
        return deleteSync(url, null);
    }

    public Object deleteSync(String url, Header[] headers) {
        HttpDelete delete = new HttpDelete(url);
        if (headers != null) {
            delete.setHeaders(headers);
        }
        return sendSyncRequest(this.httpClient, this.httpContext, delete, null);
    }

    public HttpHandler<File> download(String url, String target, AjaxCallBack<File> callback) {
        return download(url, null, target, false, true, callback);
    }

    public HttpHandler<File> downloadCheck(String url, String target, boolean isResume, boolean checkContentLength, AjaxCallBack<File> callback) {
        return download(url, null, target, isResume, checkContentLength, callback);
    }

    public HttpHandler<File> download(String url, String target, boolean isResume, AjaxCallBack<File> callback) {
        return download(url, null, target, isResume, true, callback);
    }

    public HttpHandler<File> download(String url, AjaxParams params, String target, AjaxCallBack<File> callback) {
        return download(url, params, target, false, true, callback);
    }

    public HttpHandler<File> download(String url, AjaxParams params, String target, boolean isResume, boolean checkContentLength, AjaxCallBack<File> callback) {
        HttpGet get = new HttpGet(getUrlWithQueryString(url.trim(), params));
        HttpHandler<File> handler = new HttpHandler<>(this.httpClient, this.httpContext, callback, this.charset);
        handler.executeOnExecutor(executor, get, target, Boolean.valueOf(isResume), Boolean.valueOf(checkContentLength));
        return handler;
    }

    public HttpHandler<File> postDownload(String url, String target, HttpEntity entity, String contentType, boolean isResume, AjaxCallBack<File> callback) {
        return postDownload(url, target, entity, contentType, isResume, true, callback);
    }

    public HttpHandler<File> postDownload(String url, String target, HttpEntity entity, String contentType, boolean isResume, boolean checkContentLength, AjaxCallBack<File> callback) {
        HttpHandler<File> handler = new HttpHandler<>(this.httpClient, this.httpContext, callback, this.charset);
        HttpUriRequest uriRequest = addEntityToRequestBase(new HttpPost(url), entity);
        if (contentType != null) {
            uriRequest.addHeader("Content-Type", contentType);
        }
        handler.executeOnExecutor(executor, uriRequest, target, Boolean.valueOf(isResume), Boolean.valueOf(checkContentLength));
        return handler;
    }

    /* access modifiers changed from: protected */
    public <T> void sendRequest(DefaultHttpClient client, HttpContext httpContext2, HttpUriRequest uriRequest, String contentType, AjaxCallBack<T> ajaxCallBack) {
        if (contentType != null) {
            uriRequest.addHeader("Content-Type", contentType);
        }
        new HttpHandler(client, httpContext2, ajaxCallBack, this.charset).executeOnExecutor(executor, uriRequest);
    }

    /* access modifiers changed from: protected */
    public Object sendSyncRequest(DefaultHttpClient client, HttpContext httpContext2, HttpUriRequest uriRequest, String contentType) {
        if (contentType != null) {
            uriRequest.addHeader("Content-Type", contentType);
        }
        return new SyncRequestHandler(client, httpContext2, this.charset).sendRequest(uriRequest);
    }

    public static String getUrlWithQueryString(String url, AjaxParams params) {
        if (params == null) {
            return url;
        }
        return url + "?" + params.getParamString();
    }

    private HttpEntity paramsToEntity(AjaxParams params) {
        if (params != null) {
            return params.getEntity();
        }
        return null;
    }

    private HttpEntityEnclosingRequestBase addEntityToRequestBase(HttpEntityEnclosingRequestBase requestBase, HttpEntity entity) {
        if (entity != null) {
            requestBase.setEntity(entity);
        }
        return requestBase;
    }

    private static class InflatingEntity extends HttpEntityWrapper {
        public InflatingEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        public InputStream getContent() throws IOException {
            return new GZIPInputStream(this.wrappedEntity.getContent());
        }

        public long getContentLength() {
            return -1;
        }
    }

    private static final class SpaceRedirectHandler extends DefaultRedirectHandler {
        private SpaceRedirectHandler() {
        }

        public boolean isRedirectRequested(HttpResponse httpResponse, HttpContext httpContext) {
            return FinalHttp.super.isRedirectRequested(httpResponse, httpContext);
        }

        public URI getLocationURI(HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
            return sanitizeUrl(httpResponse.getFirstHeader(ResponseBase.STRING_LOCATION).getValue());
        }

        private URI sanitizeUrl(String sanitizeURL) throws ProtocolException {
            try {
                URL url = new URL(URLDecoder.decode(sanitizeURL, "UTF-8"));
                return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            } catch (Exception e) {
                throw new ProtocolException(e.getMessage(), e);
            }
        }
    }

    private static final class EasyX509TrustManager implements X509TrustManager {
        private X509TrustManager defaultTM = null;

        public EasyX509TrustManager(KeyStore trustStore) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init(trustStore);
            TrustManager[] tms = factory.getTrustManagers();
            if (tms == null || tms.length <= 0) {
                throw new NoSuchAlgorithmException("no trust manager found");
            }
            this.defaultTM = findTrustManager(tms);
        }

        private X509TrustManager findTrustManager(TrustManager[] tms) {
            for (TrustManager tm : tms) {
                if (tm instanceof X509TrustManager) {
                    return (X509TrustManager) tm;
                }
            }
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            if (this.defaultTM != null) {
                this.defaultTM.checkClientTrusted(chain, authType);
            }
        }

        private boolean searchRootCert(X509Certificate cert) {
            for (X509Certificate rootCert : HttpsHelper.getRootCerts()) {
                if (cert.getSubjectDN().equals(rootCert.getSubjectDN())) {
                    return true;
                }
            }
            return false;
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            if (chain == null || chain.length <= 0) {
                if (this.defaultTM != null) {
                    Log.e(FinalHttp.TAG, "checkServerTrusted: using defaultTM");
                    this.defaultTM.checkServerTrusted(chain, authType);
                }
            } else if (chain == null) {
                throw new IllegalArgumentException("Check Server x509Certificates is null");
            } else if (chain.length < 0) {
                throw new IllegalArgumentException("Check Server x509Certificates is empty");
            } else {
                boolean verified = false;
                try {
                    int chainLen = chain.length;
                    for (int i = 0; i < chainLen - 1 && !verified; i++) {
                        Log.d(FinalHttp.TAG, "cert: " + i + "\n" + chain[i]);
                        chain[i].checkValidity();
                        if (chain[i + 1] != null) {
                            chain[i].verify(chain[i + 1].getPublicKey());
                            verified = searchRootCert(chain[i + 1]);
                        }
                    }
                    Log.d(FinalHttp.TAG, "DN: " + chain[chainLen - 1].getIssuerDN().getName());
                    if (verified) {
                        Log.d(FinalHttp.TAG, "verified, return");
                        return;
                    }
                    Iterator<X509Certificate> it2 = HttpsHelper.getRootCerts().iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        X509Certificate rootCert = it2.next();
                        Log.d(FinalHttp.TAG, "ROOT DN: " + rootCert.getSubjectDN().getName());
                        if (chain[chainLen - 1].getIssuerDN().equals(rootCert.getSubjectDN())) {
                            chain[chainLen - 1].verify(rootCert.getPublicKey());
                            verified = true;
                            break;
                        }
                    }
                    if (!verified) {
                        throw new CertificateException("not verified!");
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (SignatureException e2) {
                    e2.printStackTrace();
                } catch (NoSuchProviderException e3) {
                    e3.printStackTrace();
                } catch (InvalidKeyException e4) {
                    e4.printStackTrace();
                }
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return this.defaultTM != null ? this.defaultTM.getAcceptedIssuers() : new X509Certificate[0];
        }
    }

    private static final class MySSLSocketFactory extends SSLSocketFactory {
        private SSLContext mSslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);
            this.mSslContext.init(null, new TrustManager[]{new EasyX509TrustManager(truststore)}, new SecureRandom());
        }

        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return this.mSslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        public Socket createSocket() throws IOException {
            return this.mSslContext.getSocketFactory().createSocket();
        }
    }
}
