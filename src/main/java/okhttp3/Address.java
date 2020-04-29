package okhttp3;

import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.HttpUrl;
import okhttp3.internal.Util;

public final class Address {
    @Nullable
    final CertificatePinner certificatePinner;
    final List<ConnectionSpec> connectionSpecs;
    final Dns dns;
    @Nullable
    final HostnameVerifier hostnameVerifier;
    final List<Protocol> protocols;
    @Nullable
    final Proxy proxy;
    final Authenticator proxyAuthenticator;
    final ProxySelector proxySelector;
    final SocketFactory socketFactory;
    @Nullable
    final SSLSocketFactory sslSocketFactory;
    final HttpUrl url;

    public Address(String uriHost, int uriPort, Dns dns2, SocketFactory socketFactory2, @Nullable SSLSocketFactory sslSocketFactory2, @Nullable HostnameVerifier hostnameVerifier2, @Nullable CertificatePinner certificatePinner2, Authenticator proxyAuthenticator2, @Nullable Proxy proxy2, List<Protocol> protocols2, List<ConnectionSpec> connectionSpecs2, ProxySelector proxySelector2) {
        this.url = new HttpUrl.Builder().scheme(sslSocketFactory2 != null ? "https" : "http").host(uriHost).port(uriPort).build();
        if (dns2 == null) {
            throw new NullPointerException("dns == null");
        }
        this.dns = dns2;
        if (socketFactory2 == null) {
            throw new NullPointerException("socketFactory == null");
        }
        this.socketFactory = socketFactory2;
        if (proxyAuthenticator2 == null) {
            throw new NullPointerException("proxyAuthenticator == null");
        }
        this.proxyAuthenticator = proxyAuthenticator2;
        if (protocols2 == null) {
            throw new NullPointerException("protocols == null");
        }
        this.protocols = Util.immutableList(protocols2);
        if (connectionSpecs2 == null) {
            throw new NullPointerException("connectionSpecs == null");
        }
        this.connectionSpecs = Util.immutableList(connectionSpecs2);
        if (proxySelector2 == null) {
            throw new NullPointerException("proxySelector == null");
        }
        this.proxySelector = proxySelector2;
        this.proxy = proxy2;
        this.sslSocketFactory = sslSocketFactory2;
        this.hostnameVerifier = hostnameVerifier2;
        this.certificatePinner = certificatePinner2;
    }

    public HttpUrl url() {
        return this.url;
    }

    public Dns dns() {
        return this.dns;
    }

    public SocketFactory socketFactory() {
        return this.socketFactory;
    }

    public Authenticator proxyAuthenticator() {
        return this.proxyAuthenticator;
    }

    public List<Protocol> protocols() {
        return this.protocols;
    }

    public List<ConnectionSpec> connectionSpecs() {
        return this.connectionSpecs;
    }

    public ProxySelector proxySelector() {
        return this.proxySelector;
    }

    @Nullable
    public Proxy proxy() {
        return this.proxy;
    }

    @Nullable
    public SSLSocketFactory sslSocketFactory() {
        return this.sslSocketFactory;
    }

    @Nullable
    public HostnameVerifier hostnameVerifier() {
        return this.hostnameVerifier;
    }

    @Nullable
    public CertificatePinner certificatePinner() {
        return this.certificatePinner;
    }

    public boolean equals(@Nullable Object other) {
        return (other instanceof Address) && this.url.equals(((Address) other).url) && equalsNonHost((Address) other);
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4 = 0;
        int hashCode = (((((((((((this.url.hashCode() + 527) * 31) + this.dns.hashCode()) * 31) + this.proxyAuthenticator.hashCode()) * 31) + this.protocols.hashCode()) * 31) + this.connectionSpecs.hashCode()) * 31) + this.proxySelector.hashCode()) * 31;
        if (this.proxy != null) {
            i = this.proxy.hashCode();
        } else {
            i = 0;
        }
        int i5 = (hashCode + i) * 31;
        if (this.sslSocketFactory != null) {
            i2 = this.sslSocketFactory.hashCode();
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 31;
        if (this.hostnameVerifier != null) {
            i3 = this.hostnameVerifier.hashCode();
        } else {
            i3 = 0;
        }
        int i7 = (i6 + i3) * 31;
        if (this.certificatePinner != null) {
            i4 = this.certificatePinner.hashCode();
        }
        return i7 + i4;
    }

    /* access modifiers changed from: package-private */
    public boolean equalsNonHost(Address that) {
        if (!this.dns.equals(that.dns) || !this.proxyAuthenticator.equals(that.proxyAuthenticator) || !this.protocols.equals(that.protocols) || !this.connectionSpecs.equals(that.connectionSpecs) || !this.proxySelector.equals(that.proxySelector) || !Util.equal(this.proxy, that.proxy) || !Util.equal(this.sslSocketFactory, that.sslSocketFactory) || !Util.equal(this.hostnameVerifier, that.hostnameVerifier) || !Util.equal(this.certificatePinner, that.certificatePinner) || url().port() != that.url().port()) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder result = new StringBuilder().append("Address{").append(this.url.host()).append(":").append(this.url.port());
        if (this.proxy != null) {
            result.append(", proxy=").append(this.proxy);
        } else {
            result.append(", proxySelector=").append(this.proxySelector);
        }
        result.append("}");
        return result.toString();
    }
}
