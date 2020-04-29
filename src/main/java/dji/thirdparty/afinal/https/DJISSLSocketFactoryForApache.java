package dji.thirdparty.afinal.https;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class DJISSLSocketFactoryForApache extends SSLSocketFactory {
    private SSLContext mSSLContext = SSLContext.getInstance("TLS");

    public DJISSLSocketFactoryForApache(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);
    }
}
