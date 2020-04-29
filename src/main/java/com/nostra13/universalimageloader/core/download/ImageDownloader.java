package com.nostra13.universalimageloader.core.download;

import com.amap.location.common.model.AmapLoc;
import dji.publics.protocol.ResponseBase;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public interface ImageDownloader {
    InputStream getStream(String str, Object obj) throws IOException;

    public enum Scheme {
        HTTP("http"),
        HTTPS("https"),
        FILE(AmapLoc.TYPE_OFFLINE_CELL),
        CONTENT(ResponseBase.STRING_CONTENT),
        ASSETS("assets"),
        DRAWABLE("drawable"),
        UNKNOWN("");
        
        private String scheme;
        private String uriPrefix;

        private Scheme(String scheme2) {
            this.scheme = scheme2;
            this.uriPrefix = scheme2 + "://";
        }

        public static Scheme ofUri(String uri) {
            if (uri != null) {
                Scheme[] arr$ = values();
                for (Scheme s : arr$) {
                    if (s.belongsTo(uri)) {
                        return s;
                    }
                }
            }
            return UNKNOWN;
        }

        private boolean belongsTo(String uri) {
            return uri.toLowerCase(Locale.US).startsWith(this.uriPrefix);
        }

        public String wrap(String path) {
            return this.uriPrefix + path;
        }

        public String crop(String uri) {
            if (belongsTo(uri)) {
                return uri.substring(this.uriPrefix.length());
            }
            throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", uri, this.scheme));
        }
    }
}
