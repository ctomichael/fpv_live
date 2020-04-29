package org.bouncycastle.jcajce;

import java.io.OutputStream;
import java.security.KeyStore;

public class PKCS12StoreParameter implements KeyStore.LoadStoreParameter {
    private final boolean forDEREncoding;
    private final OutputStream out;
    private final KeyStore.ProtectionParameter protectionParameter;

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: org.bouncycastle.jcajce.PKCS12StoreParameter.<init>(java.io.OutputStream, java.security.KeyStore$ProtectionParameter, boolean):void
     arg types: [java.io.OutputStream, java.security.KeyStore$ProtectionParameter, int]
     candidates:
      org.bouncycastle.jcajce.PKCS12StoreParameter.<init>(java.io.OutputStream, char[], boolean):void
      org.bouncycastle.jcajce.PKCS12StoreParameter.<init>(java.io.OutputStream, java.security.KeyStore$ProtectionParameter, boolean):void */
    public PKCS12StoreParameter(OutputStream outputStream, KeyStore.ProtectionParameter protectionParameter2) {
        this(outputStream, protectionParameter2, false);
    }

    public PKCS12StoreParameter(OutputStream outputStream, KeyStore.ProtectionParameter protectionParameter2, boolean z) {
        this.out = outputStream;
        this.protectionParameter = protectionParameter2;
        this.forDEREncoding = z;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: org.bouncycastle.jcajce.PKCS12StoreParameter.<init>(java.io.OutputStream, char[], boolean):void
     arg types: [java.io.OutputStream, char[], int]
     candidates:
      org.bouncycastle.jcajce.PKCS12StoreParameter.<init>(java.io.OutputStream, java.security.KeyStore$ProtectionParameter, boolean):void
      org.bouncycastle.jcajce.PKCS12StoreParameter.<init>(java.io.OutputStream, char[], boolean):void */
    public PKCS12StoreParameter(OutputStream outputStream, char[] cArr) {
        this(outputStream, cArr, false);
    }

    public PKCS12StoreParameter(OutputStream outputStream, char[] cArr, boolean z) {
        this(outputStream, new KeyStore.PasswordProtection(cArr), z);
    }

    public OutputStream getOutputStream() {
        return this.out;
    }

    public KeyStore.ProtectionParameter getProtectionParameter() {
        return this.protectionParameter;
    }

    public boolean isForDEREncoding() {
        return this.forDEREncoding;
    }
}
