package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;

public class DHKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private DHKeyGenerationParameters param;

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: org.bouncycastle.crypto.AsymmetricCipherKeyPair.<init>(org.bouncycastle.crypto.params.AsymmetricKeyParameter, org.bouncycastle.crypto.params.AsymmetricKeyParameter):void
     arg types: [org.bouncycastle.crypto.params.DHPublicKeyParameters, org.bouncycastle.crypto.params.DHPrivateKeyParameters]
     candidates:
      org.bouncycastle.crypto.AsymmetricCipherKeyPair.<init>(org.bouncycastle.crypto.CipherParameters, org.bouncycastle.crypto.CipherParameters):void
      org.bouncycastle.crypto.AsymmetricCipherKeyPair.<init>(org.bouncycastle.crypto.params.AsymmetricKeyParameter, org.bouncycastle.crypto.params.AsymmetricKeyParameter):void */
    public AsymmetricCipherKeyPair generateKeyPair() {
        DHKeyGeneratorHelper dHKeyGeneratorHelper = DHKeyGeneratorHelper.INSTANCE;
        DHParameters parameters = this.param.getParameters();
        BigInteger calculatePrivate = dHKeyGeneratorHelper.calculatePrivate(parameters, this.param.getRandom());
        return new AsymmetricCipherKeyPair((AsymmetricKeyParameter) new DHPublicKeyParameters(dHKeyGeneratorHelper.calculatePublic(parameters, calculatePrivate), parameters), (AsymmetricKeyParameter) new DHPrivateKeyParameters(calculatePrivate, parameters));
    }

    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (DHKeyGenerationParameters) keyGenerationParameters;
    }
}
