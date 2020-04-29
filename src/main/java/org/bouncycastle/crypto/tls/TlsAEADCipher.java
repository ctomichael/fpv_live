package org.bouncycastle.crypto.tls;

import java.io.IOException;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;

public class TlsAEADCipher implements TlsCipher {
    static final int NONCE_DRAFT_CHACHA20_POLY1305 = 2;
    public static final int NONCE_RFC5288 = 1;
    protected TlsContext context;
    protected AEADBlockCipher decryptCipher;
    protected byte[] decryptImplicitNonce;
    protected AEADBlockCipher encryptCipher;
    protected byte[] encryptImplicitNonce;
    protected int macSize;
    protected int nonceMode;
    protected int record_iv_length;

    public TlsAEADCipher(TlsContext tlsContext, AEADBlockCipher aEADBlockCipher, AEADBlockCipher aEADBlockCipher2, int i, int i2) throws IOException {
        this(tlsContext, aEADBlockCipher, aEADBlockCipher2, i, i2, 1);
    }

    TlsAEADCipher(TlsContext tlsContext, AEADBlockCipher aEADBlockCipher, AEADBlockCipher aEADBlockCipher2, int i, int i2, int i3) throws IOException {
        int i4;
        KeyParameter keyParameter;
        KeyParameter keyParameter2;
        if (!TlsUtils.isTLSv12(tlsContext)) {
            throw new TlsFatalAlert(80);
        }
        this.nonceMode = i3;
        switch (i3) {
            case 1:
                i4 = 4;
                this.record_iv_length = 8;
                break;
            case 2:
                i4 = 12;
                this.record_iv_length = 0;
                break;
            default:
                throw new TlsFatalAlert(80);
        }
        this.context = tlsContext;
        this.macSize = i2;
        int i5 = (i * 2) + (i4 * 2);
        byte[] calculateKeyBlock = TlsUtils.calculateKeyBlock(tlsContext, i5);
        KeyParameter keyParameter3 = new KeyParameter(calculateKeyBlock, 0, i);
        int i6 = 0 + i;
        KeyParameter keyParameter4 = new KeyParameter(calculateKeyBlock, i6, i);
        int i7 = i6 + i;
        byte[] copyOfRange = Arrays.copyOfRange(calculateKeyBlock, i7, i7 + i4);
        int i8 = i7 + i4;
        byte[] copyOfRange2 = Arrays.copyOfRange(calculateKeyBlock, i8, i8 + i4);
        if (i8 + i4 != i5) {
            throw new TlsFatalAlert(80);
        }
        if (tlsContext.isServer()) {
            this.encryptCipher = aEADBlockCipher2;
            this.decryptCipher = aEADBlockCipher;
            this.encryptImplicitNonce = copyOfRange2;
            this.decryptImplicitNonce = copyOfRange;
            keyParameter = keyParameter3;
            keyParameter2 = keyParameter4;
        } else {
            this.encryptCipher = aEADBlockCipher;
            this.decryptCipher = aEADBlockCipher2;
            this.encryptImplicitNonce = copyOfRange;
            this.decryptImplicitNonce = copyOfRange2;
            keyParameter = keyParameter4;
            keyParameter2 = keyParameter3;
        }
        byte[] bArr = new byte[(i4 + this.record_iv_length)];
        this.encryptCipher.init(true, new AEADParameters(keyParameter2, i2 * 8, bArr));
        this.decryptCipher.init(false, new AEADParameters(keyParameter, i2 * 8, bArr));
    }

    public byte[] decodeCiphertext(long j, short s, byte[] bArr, int i, int i2) throws IOException {
        if (getPlaintextLimit(i2) < 0) {
            throw new TlsFatalAlert(50);
        }
        byte[] bArr2 = new byte[(this.decryptImplicitNonce.length + this.record_iv_length)];
        switch (this.nonceMode) {
            case 1:
                System.arraycopy(this.decryptImplicitNonce, 0, bArr2, 0, this.decryptImplicitNonce.length);
                System.arraycopy(bArr, i, bArr2, bArr2.length - this.record_iv_length, this.record_iv_length);
                break;
            case 2:
                TlsUtils.writeUint64(j, bArr2, bArr2.length - 8);
                for (int i3 = 0; i3 < this.decryptImplicitNonce.length; i3++) {
                    bArr2[i3] = (byte) (bArr2[i3] ^ this.decryptImplicitNonce[i3]);
                }
                break;
            default:
                throw new TlsFatalAlert(80);
        }
        int i4 = i + this.record_iv_length;
        int i5 = i2 - this.record_iv_length;
        int outputSize = this.decryptCipher.getOutputSize(i5);
        byte[] bArr3 = new byte[outputSize];
        try {
            this.decryptCipher.init(false, new AEADParameters(null, this.macSize * 8, bArr2, getAdditionalData(j, s, outputSize)));
            int processBytes = this.decryptCipher.processBytes(bArr, i4, i5, bArr3, 0) + 0;
            if (processBytes + this.decryptCipher.doFinal(bArr3, processBytes) == bArr3.length) {
                return bArr3;
            }
            throw new TlsFatalAlert(80);
        } catch (Exception e) {
            throw new TlsFatalAlert(20, e);
        }
    }

    public byte[] encodePlaintext(long j, short s, byte[] bArr, int i, int i2) throws IOException {
        byte[] bArr2 = new byte[(this.encryptImplicitNonce.length + this.record_iv_length)];
        switch (this.nonceMode) {
            case 1:
                System.arraycopy(this.encryptImplicitNonce, 0, bArr2, 0, this.encryptImplicitNonce.length);
                TlsUtils.writeUint64(j, bArr2, this.encryptImplicitNonce.length);
                break;
            case 2:
                TlsUtils.writeUint64(j, bArr2, bArr2.length - 8);
                for (int i3 = 0; i3 < this.encryptImplicitNonce.length; i3++) {
                    bArr2[i3] = (byte) (bArr2[i3] ^ this.encryptImplicitNonce[i3]);
                }
                break;
            default:
                throw new TlsFatalAlert(80);
        }
        byte[] bArr3 = new byte[(this.encryptCipher.getOutputSize(i2) + this.record_iv_length)];
        if (this.record_iv_length != 0) {
            System.arraycopy(bArr2, bArr2.length - this.record_iv_length, bArr3, 0, this.record_iv_length);
        }
        int i4 = this.record_iv_length;
        try {
            this.encryptCipher.init(true, new AEADParameters(null, this.macSize * 8, bArr2, getAdditionalData(j, s, i2)));
            int processBytes = this.encryptCipher.processBytes(bArr, i, i2, bArr3, i4) + i4;
            if (processBytes + this.encryptCipher.doFinal(bArr3, processBytes) == bArr3.length) {
                return bArr3;
            }
            throw new TlsFatalAlert(80);
        } catch (Exception e) {
            throw new TlsFatalAlert(80, e);
        }
    }

    /* access modifiers changed from: protected */
    public byte[] getAdditionalData(long j, short s, int i) throws IOException {
        byte[] bArr = new byte[13];
        TlsUtils.writeUint64(j, bArr, 0);
        TlsUtils.writeUint8(s, bArr, 8);
        TlsUtils.writeVersion(this.context.getServerVersion(), bArr, 9);
        TlsUtils.writeUint16(i, bArr, 11);
        return bArr;
    }

    public int getPlaintextLimit(int i) {
        return (i - this.macSize) - this.record_iv_length;
    }
}
