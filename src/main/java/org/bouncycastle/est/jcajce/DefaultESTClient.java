package org.bouncycastle.est.jcajce;

import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.makernotes.OlympusRawInfoMakernoteDirectory;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.thirdparty.org.java_websocket.drafts.Draft_75;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import org.bouncycastle.est.ESTClient;
import org.bouncycastle.est.ESTClientSourceProvider;
import org.bouncycastle.est.ESTException;
import org.bouncycastle.est.ESTRequest;
import org.bouncycastle.est.ESTRequestBuilder;
import org.bouncycastle.est.ESTResponse;
import org.bouncycastle.est.Source;
import org.bouncycastle.util.Properties;

class DefaultESTClient implements ESTClient {
    private static byte[] CRLF = {Draft_75.CR, 10};
    private static final Charset utf8 = Charset.forName("UTF-8");
    private final ESTClientSourceProvider sslSocketProvider;

    private class PrintingOutputStream extends OutputStream {
        private final OutputStream tgt;

        public PrintingOutputStream(OutputStream outputStream) {
            this.tgt = outputStream;
        }

        public void write(int i) throws IOException {
            System.out.print(String.valueOf((char) i));
            this.tgt.write(i);
        }
    }

    public DefaultESTClient(ESTClientSourceProvider eSTClientSourceProvider) {
        this.sslSocketProvider = eSTClientSourceProvider;
    }

    private static void writeLine(OutputStream outputStream, String str) throws IOException {
        outputStream.write(str.getBytes());
        outputStream.write(CRLF);
    }

    public ESTResponse doRequest(ESTRequest eSTRequest) throws IOException {
        ESTResponse performRequest;
        int i = 15;
        do {
            performRequest = performRequest(eSTRequest);
            eSTRequest = redirectURL(performRequest);
            if (eSTRequest == null) {
                break;
            }
            i--;
        } while (i > 0);
        if (i != 0) {
            return performRequest;
        }
        throw new ESTException("Too many redirects..");
    }

    public ESTResponse performRequest(ESTRequest eSTRequest) throws IOException {
        Source source;
        ESTResponse eSTResponse;
        try {
            Source makeSource = this.sslSocketProvider.makeSource(eSTRequest.getURL().getHost(), eSTRequest.getURL().getPort());
            try {
                if (eSTRequest.getListener() != null) {
                    eSTRequest = eSTRequest.getListener().onConnection(makeSource, eSTRequest);
                }
                Set<String> asKeySet = Properties.asKeySet("org.bouncycastle.debug.est");
                PrintingOutputStream printingOutputStream = (asKeySet.contains("output") || asKeySet.contains("all")) ? new PrintingOutputStream(makeSource.getOutputStream()) : makeSource.getOutputStream();
                String str = eSTRequest.getURL().getPath() + (eSTRequest.getURL().getQuery() != null ? eSTRequest.getURL().getQuery() : "");
                ESTRequestBuilder eSTRequestBuilder = new ESTRequestBuilder(eSTRequest);
                if (!eSTRequest.getHeaders().containsKey(DJISDKCacheKeys.CONNECTION)) {
                    eSTRequestBuilder.addHeader(DJISDKCacheKeys.CONNECTION, "close");
                }
                URL url = eSTRequest.getURL();
                if (url.getPort() > -1) {
                    eSTRequestBuilder.setHeader("Host", String.format("%s:%d", url.getHost(), Integer.valueOf(url.getPort())));
                } else {
                    eSTRequestBuilder.setHeader("Host", url.getHost());
                }
                ESTRequest build = eSTRequestBuilder.build();
                writeLine(printingOutputStream, build.getMethod() + " " + str + " HTTP/1.1");
                for (Map.Entry entry : build.getHeaders().entrySet()) {
                    String[] strArr = (String[]) entry.getValue();
                    for (int i = 0; i != strArr.length; i++) {
                        writeLine(printingOutputStream, ((String) entry.getKey()) + ": " + strArr[i]);
                    }
                }
                printingOutputStream.write(CRLF);
                printingOutputStream.flush();
                build.writeData(printingOutputStream);
                printingOutputStream.flush();
                if (build.getHijacker() != null) {
                    eSTResponse = build.getHijacker().hijack(build, makeSource);
                    if (makeSource != null && eSTResponse == null) {
                        makeSource.close();
                    }
                } else {
                    eSTResponse = new ESTResponse(build, makeSource);
                    if (makeSource != null && eSTResponse == null) {
                        makeSource.close();
                    }
                }
                return eSTResponse;
            } catch (Throwable th) {
                th = th;
                source = makeSource;
                if (source != null && 0 == 0) {
                    source.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            source = null;
        }
    }

    /* access modifiers changed from: protected */
    public ESTRequest redirectURL(ESTResponse eSTResponse) throws IOException {
        ESTRequest eSTRequest = null;
        if (eSTResponse.getStatusCode() >= 300 && eSTResponse.getStatusCode() <= 399) {
            switch (eSTResponse.getStatusCode()) {
                case OlympusRawInfoMakernoteDirectory.TagWbRbLevelsDaylightFluor:
                case 305:
                default:
                    throw new ESTException("Client does not handle http status code: " + eSTResponse.getStatusCode());
                case ExifDirectoryBase.TAG_TRANSFER_FUNCTION:
                case 302:
                case 303:
                case 306:
                case 307:
                    String header = eSTResponse.getHeader("Location");
                    if (!"".equals(header)) {
                        ESTRequestBuilder eSTRequestBuilder = new ESTRequestBuilder(eSTResponse.getOriginalRequest());
                        if (!header.startsWith("http")) {
                            URL url = eSTResponse.getOriginalRequest().getURL();
                            eSTRequest = eSTRequestBuilder.withURL(new URL(url.getProtocol(), url.getHost(), url.getPort(), header)).build();
                            break;
                        } else {
                            eSTRequest = eSTRequestBuilder.withURL(new URL(header)).build();
                            break;
                        }
                    } else {
                        throw new ESTException("Redirect status type: " + eSTResponse.getStatusCode() + " but no location header");
                    }
            }
        }
        if (eSTRequest != null) {
            eSTResponse.close();
        }
        return eSTRequest;
    }
}
