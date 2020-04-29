package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.tls.TlsProtocol;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.Arrays;

public class TlsServerProtocol extends TlsProtocol {
    protected CertificateRequest certificateRequest = null;
    protected short clientCertificateType = -1;
    protected TlsKeyExchange keyExchange = null;
    protected TlsHandshakeHash prepareFinishHash = null;
    protected TlsCredentials serverCredentials = null;
    protected TlsServer tlsServer = null;
    TlsServerContextImpl tlsServerContext = null;

    public TlsServerProtocol(InputStream inputStream, OutputStream outputStream, SecureRandom secureRandom) {
        super(inputStream, outputStream, secureRandom);
    }

    public TlsServerProtocol(SecureRandom secureRandom) {
        super(secureRandom);
    }

    public void accept(TlsServer tlsServer2) throws IOException {
        if (tlsServer2 == null) {
            throw new IllegalArgumentException("'tlsServer' cannot be null");
        } else if (this.tlsServer != null) {
            throw new IllegalStateException("'accept' can only be called once");
        } else {
            this.tlsServer = tlsServer2;
            this.securityParameters = new SecurityParameters();
            this.securityParameters.entity = 0;
            this.tlsServerContext = new TlsServerContextImpl(this.secureRandom, this.securityParameters);
            this.securityParameters.serverRandom = createRandomBlock(tlsServer2.shouldUseGMTUnixTime(), this.tlsServerContext.getNonceRandomGenerator());
            this.tlsServer.init(this.tlsServerContext);
            this.recordStream.init(this.tlsServerContext);
            this.recordStream.setRestrictReadVersion(false);
            blockForHandshake();
        }
    }

    /* access modifiers changed from: protected */
    public void cleanupHandshake() {
        super.cleanupHandshake();
        this.keyExchange = null;
        this.serverCredentials = null;
        this.certificateRequest = null;
        this.prepareFinishHash = null;
    }

    /* access modifiers changed from: protected */
    public boolean expectCertificateVerifyMessage() {
        return this.clientCertificateType >= 0 && TlsUtils.hasSigningCapability(this.clientCertificateType);
    }

    /* access modifiers changed from: protected */
    public TlsContext getContext() {
        return this.tlsServerContext;
    }

    /* access modifiers changed from: package-private */
    public AbstractTlsContext getContextAdmin() {
        return this.tlsServerContext;
    }

    /* access modifiers changed from: protected */
    public TlsPeer getPeer() {
        return this.tlsServer;
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0144  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0151  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleHandshakeMessage(short r6, java.io.ByteArrayInputStream r7) throws java.io.IOException {
        /*
            r5 = this;
            r1 = 1
            r2 = 0
            r0 = 0
            r4 = 10
            switch(r6) {
                case 1: goto L_0x000e;
                case 11: goto L_0x0110;
                case 15: goto L_0x017b;
                case 16: goto L_0x0130;
                case 20: goto L_0x019b;
                case 23: goto L_0x00f7;
                default: goto L_0x0008;
            }
        L_0x0008:
            org.bouncycastle.crypto.tls.TlsFatalAlert r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert
            r0.<init>(r4)
            throw r0
        L_0x000e:
            short r3 = r5.connection_state
            switch(r3) {
                case 0: goto L_0x0019;
                case 16: goto L_0x00f3;
                default: goto L_0x0013;
            }
        L_0x0013:
            org.bouncycastle.crypto.tls.TlsFatalAlert r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert
            r0.<init>(r4)
            throw r0
        L_0x0019:
            r5.receiveClientHelloMessage(r7)
            r5.connection_state = r1
            r5.sendServerHelloMessage()
            r3 = 2
            r5.connection_state = r3
            org.bouncycastle.crypto.tls.RecordStream r3 = r5.recordStream
            r3.notifyHelloComplete()
            org.bouncycastle.crypto.tls.TlsServer r3 = r5.tlsServer
            java.util.Vector r3 = r3.getServerSupplementalData()
            if (r3 == 0) goto L_0x0034
            r5.sendSupplementalDataMessage(r3)
        L_0x0034:
            r3 = 3
            r5.connection_state = r3
            org.bouncycastle.crypto.tls.TlsServer r3 = r5.tlsServer
            org.bouncycastle.crypto.tls.TlsKeyExchange r3 = r3.getKeyExchange()
            r5.keyExchange = r3
            org.bouncycastle.crypto.tls.TlsKeyExchange r3 = r5.keyExchange
            org.bouncycastle.crypto.tls.TlsContext r4 = r5.getContext()
            r3.init(r4)
            org.bouncycastle.crypto.tls.TlsServer r3 = r5.tlsServer
            org.bouncycastle.crypto.tls.TlsCredentials r3 = r3.getCredentials()
            r5.serverCredentials = r3
            org.bouncycastle.crypto.tls.TlsCredentials r3 = r5.serverCredentials
            if (r3 != 0) goto L_0x00b1
            org.bouncycastle.crypto.tls.TlsKeyExchange r3 = r5.keyExchange
            r3.skipServerCredentials()
        L_0x0059:
            r3 = 4
            r5.connection_state = r3
            if (r0 == 0) goto L_0x0064
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L_0x0066
        L_0x0064:
            r5.allowCertificateStatus = r2
        L_0x0066:
            boolean r0 = r5.allowCertificateStatus
            if (r0 == 0) goto L_0x0075
            org.bouncycastle.crypto.tls.TlsServer r0 = r5.tlsServer
            org.bouncycastle.crypto.tls.CertificateStatus r0 = r0.getCertificateStatus()
            if (r0 == 0) goto L_0x0075
            r5.sendCertificateStatusMessage(r0)
        L_0x0075:
            r0 = 5
            r5.connection_state = r0
            org.bouncycastle.crypto.tls.TlsKeyExchange r0 = r5.keyExchange
            byte[] r0 = r0.generateServerKeyExchange()
            if (r0 == 0) goto L_0x0083
            r5.sendServerKeyExchangeMessage(r0)
        L_0x0083:
            r0 = 6
            r5.connection_state = r0
            org.bouncycastle.crypto.tls.TlsCredentials r0 = r5.serverCredentials
            if (r0 == 0) goto L_0x00df
            org.bouncycastle.crypto.tls.TlsServer r0 = r5.tlsServer
            org.bouncycastle.crypto.tls.CertificateRequest r0 = r0.getCertificateRequest()
            r5.certificateRequest = r0
            org.bouncycastle.crypto.tls.CertificateRequest r0 = r5.certificateRequest
            if (r0 == 0) goto L_0x00df
            org.bouncycastle.crypto.tls.TlsContext r0 = r5.getContext()
            boolean r3 = org.bouncycastle.crypto.tls.TlsUtils.isTLSv12(r0)
            org.bouncycastle.crypto.tls.CertificateRequest r0 = r5.certificateRequest
            java.util.Vector r0 = r0.getSupportedSignatureAlgorithms()
            if (r0 == 0) goto L_0x00c2
            r0 = r1
        L_0x00a7:
            if (r3 == r0) goto L_0x00c4
            org.bouncycastle.crypto.tls.TlsFatalAlert r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert
            r1 = 80
            r0.<init>(r1)
            throw r0
        L_0x00b1:
            org.bouncycastle.crypto.tls.TlsKeyExchange r0 = r5.keyExchange
            org.bouncycastle.crypto.tls.TlsCredentials r3 = r5.serverCredentials
            r0.processServerCredentials(r3)
            org.bouncycastle.crypto.tls.TlsCredentials r0 = r5.serverCredentials
            org.bouncycastle.crypto.tls.Certificate r0 = r0.getCertificate()
            r5.sendCertificateMessage(r0)
            goto L_0x0059
        L_0x00c2:
            r0 = r2
            goto L_0x00a7
        L_0x00c4:
            org.bouncycastle.crypto.tls.TlsKeyExchange r0 = r5.keyExchange
            org.bouncycastle.crypto.tls.CertificateRequest r1 = r5.certificateRequest
            r0.validateCertificateRequest(r1)
            org.bouncycastle.crypto.tls.CertificateRequest r0 = r5.certificateRequest
            r5.sendCertificateRequestMessage(r0)
            org.bouncycastle.crypto.tls.RecordStream r0 = r5.recordStream
            org.bouncycastle.crypto.tls.TlsHandshakeHash r0 = r0.getHandshakeHash()
            org.bouncycastle.crypto.tls.CertificateRequest r1 = r5.certificateRequest
            java.util.Vector r1 = r1.getSupportedSignatureAlgorithms()
            org.bouncycastle.crypto.tls.TlsUtils.trackHashAlgorithms(r0, r1)
        L_0x00df:
            r0 = 7
            r5.connection_state = r0
            r5.sendServerHelloDoneMessage()
            r0 = 8
            r5.connection_state = r0
            org.bouncycastle.crypto.tls.RecordStream r0 = r5.recordStream
            org.bouncycastle.crypto.tls.TlsHandshakeHash r0 = r0.getHandshakeHash()
            r0.sealHashAlgorithms()
        L_0x00f2:
            return
        L_0x00f3:
            r5.refuseRenegotiation()
            goto L_0x00f2
        L_0x00f7:
            short r0 = r5.connection_state
            switch(r0) {
                case 8: goto L_0x0102;
                default: goto L_0x00fc;
            }
        L_0x00fc:
            org.bouncycastle.crypto.tls.TlsFatalAlert r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert
            r0.<init>(r4)
            throw r0
        L_0x0102:
            org.bouncycastle.crypto.tls.TlsServer r0 = r5.tlsServer
            java.util.Vector r1 = readSupplementalDataMessage(r7)
            r0.processClientSupplementalData(r1)
            r0 = 9
            r5.connection_state = r0
            goto L_0x00f2
        L_0x0110:
            short r1 = r5.connection_state
            switch(r1) {
                case 8: goto L_0x011b;
                case 9: goto L_0x0120;
                default: goto L_0x0115;
            }
        L_0x0115:
            org.bouncycastle.crypto.tls.TlsFatalAlert r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert
            r0.<init>(r4)
            throw r0
        L_0x011b:
            org.bouncycastle.crypto.tls.TlsServer r1 = r5.tlsServer
            r1.processClientSupplementalData(r0)
        L_0x0120:
            org.bouncycastle.crypto.tls.CertificateRequest r0 = r5.certificateRequest
            if (r0 != 0) goto L_0x012a
            org.bouncycastle.crypto.tls.TlsFatalAlert r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert
            r0.<init>(r4)
            throw r0
        L_0x012a:
            r5.receiveCertificateMessage(r7)
            r5.connection_state = r4
            goto L_0x00f2
        L_0x0130:
            short r1 = r5.connection_state
            switch(r1) {
                case 8: goto L_0x013b;
                case 9: goto L_0x0140;
                case 10: goto L_0x0149;
                default: goto L_0x0135;
            }
        L_0x0135:
            org.bouncycastle.crypto.tls.TlsFatalAlert r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert
            r0.<init>(r4)
            throw r0
        L_0x013b:
            org.bouncycastle.crypto.tls.TlsServer r1 = r5.tlsServer
            r1.processClientSupplementalData(r0)
        L_0x0140:
            org.bouncycastle.crypto.tls.CertificateRequest r0 = r5.certificateRequest
            if (r0 != 0) goto L_0x0151
            org.bouncycastle.crypto.tls.TlsKeyExchange r0 = r5.keyExchange
            r0.skipClientCredentials()
        L_0x0149:
            r5.receiveClientKeyExchangeMessage(r7)
            r0 = 11
            r5.connection_state = r0
            goto L_0x00f2
        L_0x0151:
            org.bouncycastle.crypto.tls.TlsContext r0 = r5.getContext()
            boolean r0 = org.bouncycastle.crypto.tls.TlsUtils.isTLSv12(r0)
            if (r0 == 0) goto L_0x0161
            org.bouncycastle.crypto.tls.TlsFatalAlert r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert
            r0.<init>(r4)
            throw r0
        L_0x0161:
            org.bouncycastle.crypto.tls.TlsContext r0 = r5.getContext()
            boolean r0 = org.bouncycastle.crypto.tls.TlsUtils.isSSL(r0)
            if (r0 == 0) goto L_0x0175
            org.bouncycastle.crypto.tls.Certificate r0 = r5.peerCertificate
            if (r0 != 0) goto L_0x0149
            org.bouncycastle.crypto.tls.TlsFatalAlert r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert
            r0.<init>(r4)
            throw r0
        L_0x0175:
            org.bouncycastle.crypto.tls.Certificate r0 = org.bouncycastle.crypto.tls.Certificate.EMPTY_CHAIN
            r5.notifyClientCertificate(r0)
            goto L_0x0149
        L_0x017b:
            short r0 = r5.connection_state
            switch(r0) {
                case 11: goto L_0x0186;
                default: goto L_0x0180;
            }
        L_0x0180:
            org.bouncycastle.crypto.tls.TlsFatalAlert r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert
            r0.<init>(r4)
            throw r0
        L_0x0186:
            boolean r0 = r5.expectCertificateVerifyMessage()
            if (r0 != 0) goto L_0x0192
            org.bouncycastle.crypto.tls.TlsFatalAlert r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert
            r0.<init>(r4)
            throw r0
        L_0x0192:
            r5.receiveCertificateVerifyMessage(r7)
            r0 = 12
            r5.connection_state = r0
            goto L_0x00f2
        L_0x019b:
            short r0 = r5.connection_state
            switch(r0) {
                case 11: goto L_0x01a6;
                case 12: goto L_0x01b2;
                default: goto L_0x01a0;
            }
        L_0x01a0:
            org.bouncycastle.crypto.tls.TlsFatalAlert r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert
            r0.<init>(r4)
            throw r0
        L_0x01a6:
            boolean r0 = r5.expectCertificateVerifyMessage()
            if (r0 == 0) goto L_0x01b2
            org.bouncycastle.crypto.tls.TlsFatalAlert r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert
            r0.<init>(r4)
            throw r0
        L_0x01b2:
            r5.processFinishedMessage(r7)
            r0 = 13
            r5.connection_state = r0
            boolean r0 = r5.expectSessionTicket
            if (r0 == 0) goto L_0x01c9
            org.bouncycastle.crypto.tls.TlsServer r0 = r5.tlsServer
            org.bouncycastle.crypto.tls.NewSessionTicket r0 = r0.getNewSessionTicket()
            r5.sendNewSessionTicketMessage(r0)
            r5.sendChangeCipherSpecMessage()
        L_0x01c9:
            r0 = 14
            r5.connection_state = r0
            r5.sendFinishedMessage()
            r0 = 15
            r5.connection_state = r0
            r5.completeHandshake()
            goto L_0x00f2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.crypto.tls.TlsServerProtocol.handleHandshakeMessage(short, java.io.ByteArrayInputStream):void");
    }

    /* access modifiers changed from: protected */
    public void handleWarningMessage(short s) throws IOException {
        switch (s) {
            case 41:
                if (TlsUtils.isSSL(getContext()) && this.certificateRequest != null) {
                    notifyClientCertificate(Certificate.EMPTY_CHAIN);
                    return;
                }
                return;
            default:
                super.handleWarningMessage(s);
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void notifyClientCertificate(Certificate certificate) throws IOException {
        if (this.certificateRequest == null) {
            throw new IllegalStateException();
        } else if (this.peerCertificate != null) {
            throw new TlsFatalAlert(10);
        } else {
            this.peerCertificate = certificate;
            if (certificate.isEmpty()) {
                this.keyExchange.skipClientCredentials();
            } else {
                this.clientCertificateType = TlsUtils.getClientCertificateType(certificate, this.serverCredentials.getCertificate());
                this.keyExchange.processClientCertificate(certificate);
            }
            this.tlsServer.notifyClientCertificate(certificate);
        }
    }

    /* access modifiers changed from: protected */
    public void receiveCertificateMessage(ByteArrayInputStream byteArrayInputStream) throws IOException {
        Certificate parse = Certificate.parse(byteArrayInputStream);
        assertEmpty(byteArrayInputStream);
        notifyClientCertificate(parse);
    }

    /* access modifiers changed from: protected */
    public void receiveCertificateVerifyMessage(ByteArrayInputStream byteArrayInputStream) throws IOException {
        byte[] sessionHash;
        if (this.certificateRequest == null) {
            throw new IllegalStateException();
        }
        DigitallySigned parse = DigitallySigned.parse(getContext(), byteArrayInputStream);
        assertEmpty(byteArrayInputStream);
        try {
            SignatureAndHashAlgorithm algorithm = parse.getAlgorithm();
            if (TlsUtils.isTLSv12(getContext())) {
                TlsUtils.verifySupportedSignatureAlgorithm(this.certificateRequest.getSupportedSignatureAlgorithms(), algorithm);
                sessionHash = this.prepareFinishHash.getFinalHash(algorithm.getHash());
            } else {
                sessionHash = this.securityParameters.getSessionHash();
            }
            AsymmetricKeyParameter createKey = PublicKeyFactory.createKey(this.peerCertificate.getCertificateAt(0).getSubjectPublicKeyInfo());
            TlsSigner createTlsSigner = TlsUtils.createTlsSigner(this.clientCertificateType);
            createTlsSigner.init(getContext());
            if (!createTlsSigner.verifyRawSignature(algorithm, parse.getSignature(), createKey, sessionHash)) {
                throw new TlsFatalAlert(51);
            }
        } catch (TlsFatalAlert e) {
            throw e;
        } catch (Exception e2) {
            throw new TlsFatalAlert(51, e2);
        }
    }

    /* access modifiers changed from: protected */
    public void receiveClientHelloMessage(ByteArrayInputStream byteArrayInputStream) throws IOException {
        ProtocolVersion readVersion = TlsUtils.readVersion(byteArrayInputStream);
        this.recordStream.setWriteVersion(readVersion);
        if (readVersion.isDTLS()) {
            throw new TlsFatalAlert(47);
        }
        byte[] readFully = TlsUtils.readFully(32, byteArrayInputStream);
        if (TlsUtils.readOpaque8(byteArrayInputStream).length > 32) {
            throw new TlsFatalAlert(47);
        }
        int readUint16 = TlsUtils.readUint16(byteArrayInputStream);
        if (readUint16 < 2 || (readUint16 & 1) != 0) {
            throw new TlsFatalAlert(50);
        }
        this.offeredCipherSuites = TlsUtils.readUint16Array(readUint16 / 2, byteArrayInputStream);
        short readUint8 = TlsUtils.readUint8(byteArrayInputStream);
        if (readUint8 < 1) {
            throw new TlsFatalAlert(47);
        }
        this.offeredCompressionMethods = TlsUtils.readUint8Array(readUint8, byteArrayInputStream);
        this.clientExtensions = readExtensions(byteArrayInputStream);
        this.securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(this.clientExtensions);
        getContextAdmin().setClientVersion(readVersion);
        this.tlsServer.notifyClientVersion(readVersion);
        this.tlsServer.notifyFallback(Arrays.contains(this.offeredCipherSuites, (int) CipherSuite.TLS_FALLBACK_SCSV));
        this.securityParameters.clientRandom = readFully;
        this.tlsServer.notifyOfferedCipherSuites(this.offeredCipherSuites);
        this.tlsServer.notifyOfferedCompressionMethods(this.offeredCompressionMethods);
        if (Arrays.contains(this.offeredCipherSuites, 255)) {
            this.secure_renegotiation = true;
        }
        byte[] extensionData = TlsUtils.getExtensionData(this.clientExtensions, EXT_RenegotiationInfo);
        if (extensionData != null) {
            this.secure_renegotiation = true;
            if (!Arrays.constantTimeAreEqual(extensionData, createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                throw new TlsFatalAlert(40);
            }
        }
        this.tlsServer.notifySecureRenegotiation(this.secure_renegotiation);
        if (this.clientExtensions != null) {
            TlsExtensionsUtils.getPaddingExtension(this.clientExtensions);
            this.tlsServer.processClientExtensions(this.clientExtensions);
        }
    }

    /* access modifiers changed from: protected */
    public void receiveClientKeyExchangeMessage(ByteArrayInputStream byteArrayInputStream) throws IOException {
        this.keyExchange.processClientKeyExchange(byteArrayInputStream);
        assertEmpty(byteArrayInputStream);
        if (TlsUtils.isSSL(getContext())) {
            establishMasterSecret(getContext(), this.keyExchange);
        }
        this.prepareFinishHash = this.recordStream.prepareToFinish();
        this.securityParameters.sessionHash = getCurrentPRFHash(getContext(), this.prepareFinishHash, null);
        if (!TlsUtils.isSSL(getContext())) {
            establishMasterSecret(getContext(), this.keyExchange);
        }
        this.recordStream.setPendingConnectionState(getPeer().getCompression(), getPeer().getCipher());
        if (!this.expectSessionTicket) {
            sendChangeCipherSpecMessage();
        }
    }

    /* access modifiers changed from: protected */
    public void sendCertificateRequestMessage(CertificateRequest certificateRequest2) throws IOException {
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(this, 13);
        certificateRequest2.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    /* access modifiers changed from: protected */
    public void sendCertificateStatusMessage(CertificateStatus certificateStatus) throws IOException {
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(this, 22);
        certificateStatus.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    /* access modifiers changed from: protected */
    public void sendNewSessionTicketMessage(NewSessionTicket newSessionTicket) throws IOException {
        if (newSessionTicket == null) {
            throw new TlsFatalAlert(80);
        }
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(this, 4);
        newSessionTicket.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: org.bouncycastle.crypto.tls.TlsUtils.writeUint8(short, byte[], int):void
     arg types: [int, byte[], int]
     candidates:
      org.bouncycastle.crypto.tls.TlsUtils.writeUint8(int, byte[], int):void
      org.bouncycastle.crypto.tls.TlsUtils.writeUint8(short, byte[], int):void */
    /* access modifiers changed from: protected */
    public void sendServerHelloDoneMessage() throws IOException {
        byte[] bArr = new byte[4];
        TlsUtils.writeUint8((short) 14, bArr, 0);
        TlsUtils.writeUint24(0, bArr, 1);
        writeHandshakeMessage(bArr, 0, bArr.length);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: org.bouncycastle.crypto.tls.TlsUtils.writeUint8(short, java.io.OutputStream):void
     arg types: [short, org.bouncycastle.crypto.tls.TlsProtocol$HandshakeMessage]
     candidates:
      org.bouncycastle.crypto.tls.TlsUtils.writeUint8(int, java.io.OutputStream):void
      org.bouncycastle.crypto.tls.TlsUtils.writeUint8(short, java.io.OutputStream):void */
    /* access modifiers changed from: protected */
    public void sendServerHelloMessage() throws IOException {
        boolean z = true;
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(this, 2);
        ProtocolVersion serverVersion = this.tlsServer.getServerVersion();
        if (!serverVersion.isEqualOrEarlierVersionOf(getContext().getClientVersion())) {
            throw new TlsFatalAlert(80);
        }
        this.recordStream.setReadVersion(serverVersion);
        this.recordStream.setWriteVersion(serverVersion);
        this.recordStream.setRestrictReadVersion(true);
        getContextAdmin().setServerVersion(serverVersion);
        TlsUtils.writeVersion(serverVersion, handshakeMessage);
        handshakeMessage.write(this.securityParameters.serverRandom);
        TlsUtils.writeOpaque8(TlsUtils.EMPTY_BYTES, handshakeMessage);
        int selectedCipherSuite = this.tlsServer.getSelectedCipherSuite();
        if (!Arrays.contains(this.offeredCipherSuites, selectedCipherSuite) || selectedCipherSuite == 0 || CipherSuite.isSCSV(selectedCipherSuite) || !TlsUtils.isValidCipherSuiteForVersion(selectedCipherSuite, getContext().getServerVersion())) {
            throw new TlsFatalAlert(80);
        }
        this.securityParameters.cipherSuite = selectedCipherSuite;
        short selectedCompressionMethod = this.tlsServer.getSelectedCompressionMethod();
        if (!Arrays.contains(this.offeredCompressionMethods, selectedCompressionMethod)) {
            throw new TlsFatalAlert(80);
        }
        this.securityParameters.compressionAlgorithm = selectedCompressionMethod;
        TlsUtils.writeUint16(selectedCipherSuite, handshakeMessage);
        TlsUtils.writeUint8(selectedCompressionMethod, (OutputStream) handshakeMessage);
        this.serverExtensions = this.tlsServer.getServerExtensions();
        if (this.secure_renegotiation) {
            if (TlsUtils.getExtensionData(this.serverExtensions, EXT_RenegotiationInfo) == null) {
                this.serverExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(this.serverExtensions);
                this.serverExtensions.put(EXT_RenegotiationInfo, createRenegotiationInfo(TlsUtils.EMPTY_BYTES));
            }
        }
        if (this.securityParameters.extendedMasterSecret) {
            this.serverExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(this.serverExtensions);
            TlsExtensionsUtils.addExtendedMasterSecretExtension(this.serverExtensions);
        }
        if (this.serverExtensions != null) {
            this.securityParameters.encryptThenMAC = TlsExtensionsUtils.hasEncryptThenMACExtension(this.serverExtensions);
            this.securityParameters.maxFragmentLength = processMaxFragmentLengthExtension(this.clientExtensions, this.serverExtensions, 80);
            this.securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(this.serverExtensions);
            this.allowCertificateStatus = !this.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(this.serverExtensions, TlsExtensionsUtils.EXT_status_request, 80);
            if (this.resumedSession || !TlsUtils.hasExpectedEmptyExtensionData(this.serverExtensions, TlsProtocol.EXT_SessionTicket, 80)) {
                z = false;
            }
            this.expectSessionTicket = z;
            writeExtensions(handshakeMessage, this.serverExtensions);
        }
        this.securityParameters.prfAlgorithm = getPRFAlgorithm(getContext(), this.securityParameters.getCipherSuite());
        this.securityParameters.verifyDataLength = 12;
        applyMaxFragmentLengthExtension();
        handshakeMessage.writeToRecordStream();
    }

    /* access modifiers changed from: protected */
    public void sendServerKeyExchangeMessage(byte[] bArr) throws IOException {
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(12, bArr.length);
        handshakeMessage.write(bArr);
        handshakeMessage.writeToRecordStream();
    }
}
