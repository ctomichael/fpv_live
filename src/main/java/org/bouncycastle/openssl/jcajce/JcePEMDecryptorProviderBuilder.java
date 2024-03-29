package org.bouncycastle.openssl.jcajce;

import java.security.Provider;
import org.bouncycastle.jcajce.util.DefaultJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jcajce.util.NamedJcaJceHelper;
import org.bouncycastle.jcajce.util.ProviderJcaJceHelper;
import org.bouncycastle.openssl.PEMDecryptor;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PasswordException;

public class JcePEMDecryptorProviderBuilder {
    /* access modifiers changed from: private */
    public JcaJceHelper helper = new DefaultJcaJceHelper();

    public PEMDecryptorProvider build(final char[] cArr) {
        return new PEMDecryptorProvider() {
            /* class org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder.AnonymousClass1 */

            public PEMDecryptor get(final String str) {
                return new PEMDecryptor() {
                    /* class org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder.AnonymousClass1.AnonymousClass1 */

                    public byte[] decrypt(byte[] bArr, byte[] bArr2) throws PEMException {
                        if (cArr == null) {
                            throw new PasswordException("Password is null, but a password is required");
                        }
                        return PEMUtilities.crypt(false, JcePEMDecryptorProviderBuilder.this.helper, bArr, cArr, str, bArr2);
                    }
                };
            }
        };
    }

    public JcePEMDecryptorProviderBuilder setProvider(String str) {
        this.helper = new NamedJcaJceHelper(str);
        return this;
    }

    public JcePEMDecryptorProviderBuilder setProvider(Provider provider) {
        this.helper = new ProviderJcaJceHelper(provider);
        return this;
    }
}
