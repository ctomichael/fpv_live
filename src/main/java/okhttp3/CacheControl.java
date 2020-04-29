package okhttp3;

import dji.publics.protocol.ResponseBase;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.internal.http.HttpHeaders;

public final class CacheControl {
    public static final CacheControl FORCE_CACHE = new Builder().onlyIfCached().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS).build();
    public static final CacheControl FORCE_NETWORK = new Builder().noCache().build();
    @Nullable
    String headerValue;
    private final boolean immutable;
    private final boolean isPrivate;
    private final boolean isPublic;
    private final int maxAgeSeconds;
    private final int maxStaleSeconds;
    private final int minFreshSeconds;
    private final boolean mustRevalidate;
    private final boolean noCache;
    private final boolean noStore;
    private final boolean noTransform;
    private final boolean onlyIfCached;
    private final int sMaxAgeSeconds;

    private CacheControl(boolean noCache2, boolean noStore2, int maxAgeSeconds2, int sMaxAgeSeconds2, boolean isPrivate2, boolean isPublic2, boolean mustRevalidate2, int maxStaleSeconds2, int minFreshSeconds2, boolean onlyIfCached2, boolean noTransform2, boolean immutable2, @Nullable String headerValue2) {
        this.noCache = noCache2;
        this.noStore = noStore2;
        this.maxAgeSeconds = maxAgeSeconds2;
        this.sMaxAgeSeconds = sMaxAgeSeconds2;
        this.isPrivate = isPrivate2;
        this.isPublic = isPublic2;
        this.mustRevalidate = mustRevalidate2;
        this.maxStaleSeconds = maxStaleSeconds2;
        this.minFreshSeconds = minFreshSeconds2;
        this.onlyIfCached = onlyIfCached2;
        this.noTransform = noTransform2;
        this.immutable = immutable2;
        this.headerValue = headerValue2;
    }

    CacheControl(Builder builder) {
        this.noCache = builder.noCache;
        this.noStore = builder.noStore;
        this.maxAgeSeconds = builder.maxAgeSeconds;
        this.sMaxAgeSeconds = -1;
        this.isPrivate = false;
        this.isPublic = false;
        this.mustRevalidate = false;
        this.maxStaleSeconds = builder.maxStaleSeconds;
        this.minFreshSeconds = builder.minFreshSeconds;
        this.onlyIfCached = builder.onlyIfCached;
        this.noTransform = builder.noTransform;
        this.immutable = builder.immutable;
    }

    public boolean noCache() {
        return this.noCache;
    }

    public boolean noStore() {
        return this.noStore;
    }

    public int maxAgeSeconds() {
        return this.maxAgeSeconds;
    }

    public int sMaxAgeSeconds() {
        return this.sMaxAgeSeconds;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public boolean mustRevalidate() {
        return this.mustRevalidate;
    }

    public int maxStaleSeconds() {
        return this.maxStaleSeconds;
    }

    public int minFreshSeconds() {
        return this.minFreshSeconds;
    }

    public boolean onlyIfCached() {
        return this.onlyIfCached;
    }

    public boolean noTransform() {
        return this.noTransform;
    }

    public boolean immutable() {
        return this.immutable;
    }

    public static CacheControl parse(Headers headers) {
        String parameter;
        boolean noCache2 = false;
        boolean noStore2 = false;
        int maxAgeSeconds2 = -1;
        int sMaxAgeSeconds2 = -1;
        boolean isPrivate2 = false;
        boolean isPublic2 = false;
        boolean mustRevalidate2 = false;
        int maxStaleSeconds2 = -1;
        int minFreshSeconds2 = -1;
        boolean onlyIfCached2 = false;
        boolean noTransform2 = false;
        boolean immutable2 = false;
        boolean canUseHeaderValue = true;
        String headerValue2 = null;
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            String name = headers.name(i);
            String value = headers.value(i);
            if (name.equalsIgnoreCase("Cache-Control")) {
                if (headerValue2 != null) {
                    canUseHeaderValue = false;
                } else {
                    headerValue2 = value;
                }
            } else if (name.equalsIgnoreCase("Pragma")) {
                canUseHeaderValue = false;
            }
            int pos = 0;
            while (pos < value.length()) {
                int tokenStart = pos;
                int pos2 = HttpHeaders.skipUntil(value, pos, "=,;");
                String directive = value.substring(tokenStart, pos2).trim();
                if (pos2 == value.length() || value.charAt(pos2) == ',' || value.charAt(pos2) == ';') {
                    pos = pos2 + 1;
                    parameter = null;
                } else {
                    int pos3 = HttpHeaders.skipWhitespace(value, pos2 + 1);
                    if (pos3 >= value.length() || value.charAt(pos3) != '\"') {
                        int parameterStart = pos3;
                        pos = HttpHeaders.skipUntil(value, pos3, ",;");
                        parameter = value.substring(parameterStart, pos).trim();
                    } else {
                        int pos4 = pos3 + 1;
                        int parameterStart2 = pos4;
                        int pos5 = HttpHeaders.skipUntil(value, pos4, "\"");
                        parameter = value.substring(parameterStart2, pos5);
                        pos = pos5 + 1;
                    }
                }
                if ("no-cache".equalsIgnoreCase(directive)) {
                    noCache2 = true;
                } else if ("no-store".equalsIgnoreCase(directive)) {
                    noStore2 = true;
                } else if ("max-age".equalsIgnoreCase(directive)) {
                    maxAgeSeconds2 = HttpHeaders.parseSeconds(parameter, -1);
                } else if ("s-maxage".equalsIgnoreCase(directive)) {
                    sMaxAgeSeconds2 = HttpHeaders.parseSeconds(parameter, -1);
                } else if ("private".equalsIgnoreCase(directive)) {
                    isPrivate2 = true;
                } else if (ResponseBase.STRING_PUBLIC.equalsIgnoreCase(directive)) {
                    isPublic2 = true;
                } else if ("must-revalidate".equalsIgnoreCase(directive)) {
                    mustRevalidate2 = true;
                } else if ("max-stale".equalsIgnoreCase(directive)) {
                    maxStaleSeconds2 = HttpHeaders.parseSeconds(parameter, Integer.MAX_VALUE);
                } else if ("min-fresh".equalsIgnoreCase(directive)) {
                    minFreshSeconds2 = HttpHeaders.parseSeconds(parameter, -1);
                } else if ("only-if-cached".equalsIgnoreCase(directive)) {
                    onlyIfCached2 = true;
                } else if ("no-transform".equalsIgnoreCase(directive)) {
                    noTransform2 = true;
                } else if ("immutable".equalsIgnoreCase(directive)) {
                    immutable2 = true;
                }
            }
        }
        if (!canUseHeaderValue) {
            headerValue2 = null;
        }
        return new CacheControl(noCache2, noStore2, maxAgeSeconds2, sMaxAgeSeconds2, isPrivate2, isPublic2, mustRevalidate2, maxStaleSeconds2, minFreshSeconds2, onlyIfCached2, noTransform2, immutable2, headerValue2);
    }

    public String toString() {
        String result = this.headerValue;
        if (result != null) {
            return result;
        }
        String result2 = headerValue();
        this.headerValue = result2;
        return result2;
    }

    private String headerValue() {
        StringBuilder result = new StringBuilder();
        if (this.noCache) {
            result.append("no-cache, ");
        }
        if (this.noStore) {
            result.append("no-store, ");
        }
        if (this.maxAgeSeconds != -1) {
            result.append("max-age=").append(this.maxAgeSeconds).append(", ");
        }
        if (this.sMaxAgeSeconds != -1) {
            result.append("s-maxage=").append(this.sMaxAgeSeconds).append(", ");
        }
        if (this.isPrivate) {
            result.append("private, ");
        }
        if (this.isPublic) {
            result.append("public, ");
        }
        if (this.mustRevalidate) {
            result.append("must-revalidate, ");
        }
        if (this.maxStaleSeconds != -1) {
            result.append("max-stale=").append(this.maxStaleSeconds).append(", ");
        }
        if (this.minFreshSeconds != -1) {
            result.append("min-fresh=").append(this.minFreshSeconds).append(", ");
        }
        if (this.onlyIfCached) {
            result.append("only-if-cached, ");
        }
        if (this.noTransform) {
            result.append("no-transform, ");
        }
        if (this.immutable) {
            result.append("immutable, ");
        }
        if (result.length() == 0) {
            return "";
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }

    public static final class Builder {
        boolean immutable;
        int maxAgeSeconds = -1;
        int maxStaleSeconds = -1;
        int minFreshSeconds = -1;
        boolean noCache;
        boolean noStore;
        boolean noTransform;
        boolean onlyIfCached;

        public Builder noCache() {
            this.noCache = true;
            return this;
        }

        public Builder noStore() {
            this.noStore = true;
            return this;
        }

        public Builder maxAge(int maxAge, TimeUnit timeUnit) {
            int i;
            if (maxAge < 0) {
                throw new IllegalArgumentException("maxAge < 0: " + maxAge);
            }
            long maxAgeSecondsLong = timeUnit.toSeconds((long) maxAge);
            if (maxAgeSecondsLong > 2147483647L) {
                i = Integer.MAX_VALUE;
            } else {
                i = (int) maxAgeSecondsLong;
            }
            this.maxAgeSeconds = i;
            return this;
        }

        public Builder maxStale(int maxStale, TimeUnit timeUnit) {
            int i;
            if (maxStale < 0) {
                throw new IllegalArgumentException("maxStale < 0: " + maxStale);
            }
            long maxStaleSecondsLong = timeUnit.toSeconds((long) maxStale);
            if (maxStaleSecondsLong > 2147483647L) {
                i = Integer.MAX_VALUE;
            } else {
                i = (int) maxStaleSecondsLong;
            }
            this.maxStaleSeconds = i;
            return this;
        }

        public Builder minFresh(int minFresh, TimeUnit timeUnit) {
            int i;
            if (minFresh < 0) {
                throw new IllegalArgumentException("minFresh < 0: " + minFresh);
            }
            long minFreshSecondsLong = timeUnit.toSeconds((long) minFresh);
            if (minFreshSecondsLong > 2147483647L) {
                i = Integer.MAX_VALUE;
            } else {
                i = (int) minFreshSecondsLong;
            }
            this.minFreshSeconds = i;
            return this;
        }

        public Builder onlyIfCached() {
            this.onlyIfCached = true;
            return this;
        }

        public Builder noTransform() {
            this.noTransform = true;
            return this;
        }

        public Builder immutable() {
            this.immutable = true;
            return this;
        }

        public CacheControl build() {
            return new CacheControl(this);
        }
    }
}
