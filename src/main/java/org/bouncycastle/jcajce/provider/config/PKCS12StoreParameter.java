package org.bouncycastle.jcajce.provider.config;

import java.io.OutputStream;
import java.security.KeyStore;

public class PKCS12StoreParameter extends org.bouncycastle.jcajce.PKCS12StoreParameter {
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: org.bouncycastle.jcajce.PKCS12StoreParameter.<init>(java.io.OutputStream, java.security.KeyStore$ProtectionParameter, boolean):void
     arg types: [java.io.OutputStream, java.security.KeyStore$ProtectionParameter, int]
     candidates:
      org.bouncycastle.jcajce.PKCS12StoreParameter.<init>(java.io.OutputStream, char[], boolean):void
      org.bouncycastle.jcajce.PKCS12StoreParameter.<init>(java.io.OutputStream, java.security.KeyStore$ProtectionParameter, boolean):void */
    public PKCS12StoreParameter(OutputStream outputStream, KeyStore.ProtectionParameter protectionParameter) {
        super(outputStream, protectionParameter, false);
    }

    public PKCS12StoreParameter(OutputStream outputStream, KeyStore.ProtectionParameter protectionParameter, boolean z) {
        super(outputStream, protectionParameter, z);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: org.bouncycastle.jcajce.PKCS12StoreParameter.<init>(java.io.OutputStream, char[], boolean):void
     arg types: [java.io.OutputStream, char[], int]
     candidates:
      org.bouncycastle.jcajce.PKCS12StoreParameter.<init>(java.io.OutputStream, java.security.KeyStore$ProtectionParameter, boolean):void
      org.bouncycastle.jcajce.PKCS12StoreParameter.<init>(java.io.OutputStream, char[], boolean):void */
    public PKCS12StoreParameter(OutputStream outputStream, char[] cArr) {
        super(outputStream, cArr, false);
    }

    public PKCS12StoreParameter(OutputStream outputStream, char[] cArr, boolean z) {
        super(outputStream, new KeyStore.PasswordProtection(cArr), z);
    }
}
