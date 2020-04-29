package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;

public class DSTU4145KeyPairGenerator extends ECKeyPairGenerator {
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: org.bouncycastle.crypto.AsymmetricCipherKeyPair.<init>(org.bouncycastle.crypto.params.AsymmetricKeyParameter, org.bouncycastle.crypto.params.AsymmetricKeyParameter):void
     arg types: [org.bouncycastle.crypto.params.ECPublicKeyParameters, org.bouncycastle.crypto.params.ECPrivateKeyParameters]
     candidates:
      org.bouncycastle.crypto.AsymmetricCipherKeyPair.<init>(org.bouncycastle.crypto.CipherParameters, org.bouncycastle.crypto.CipherParameters):void
      org.bouncycastle.crypto.AsymmetricCipherKeyPair.<init>(org.bouncycastle.crypto.params.AsymmetricKeyParameter, org.bouncycastle.crypto.params.AsymmetricKeyParameter):void */
    public AsymmetricCipherKeyPair generateKeyPair() {
        AsymmetricCipherKeyPair generateKeyPair = super.generateKeyPair();
        ECPublicKeyParameters eCPublicKeyParameters = (ECPublicKeyParameters) generateKeyPair.getPublic();
        return new AsymmetricCipherKeyPair((AsymmetricKeyParameter) new ECPublicKeyParameters(eCPublicKeyParameters.getQ().negate(), eCPublicKeyParameters.getParameters()), (AsymmetricKeyParameter) ((ECPrivateKeyParameters) generateKeyPair.getPrivate()));
    }
}
