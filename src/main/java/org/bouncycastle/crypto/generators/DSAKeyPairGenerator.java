package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.math.ec.WNafUtil;
import org.bouncycastle.util.BigIntegers;

public class DSAKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private static final BigInteger ONE = BigInteger.valueOf(1);
    private DSAKeyGenerationParameters param;

    private static BigInteger calculatePublicKey(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        return bigInteger2.modPow(bigInteger3, bigInteger);
    }

    private static BigInteger generatePrivateKey(BigInteger bigInteger, SecureRandom secureRandom) {
        BigInteger createRandomInRange;
        int bitLength = bigInteger.bitLength() >>> 2;
        do {
            createRandomInRange = BigIntegers.createRandomInRange(ONE, bigInteger.subtract(ONE), secureRandom);
        } while (WNafUtil.getNafWeight(createRandomInRange) < bitLength);
        return createRandomInRange;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: org.bouncycastle.crypto.AsymmetricCipherKeyPair.<init>(org.bouncycastle.crypto.params.AsymmetricKeyParameter, org.bouncycastle.crypto.params.AsymmetricKeyParameter):void
     arg types: [org.bouncycastle.crypto.params.DSAPublicKeyParameters, org.bouncycastle.crypto.params.DSAPrivateKeyParameters]
     candidates:
      org.bouncycastle.crypto.AsymmetricCipherKeyPair.<init>(org.bouncycastle.crypto.CipherParameters, org.bouncycastle.crypto.CipherParameters):void
      org.bouncycastle.crypto.AsymmetricCipherKeyPair.<init>(org.bouncycastle.crypto.params.AsymmetricKeyParameter, org.bouncycastle.crypto.params.AsymmetricKeyParameter):void */
    public AsymmetricCipherKeyPair generateKeyPair() {
        DSAParameters parameters = this.param.getParameters();
        BigInteger generatePrivateKey = generatePrivateKey(parameters.getQ(), this.param.getRandom());
        return new AsymmetricCipherKeyPair((AsymmetricKeyParameter) new DSAPublicKeyParameters(calculatePublicKey(parameters.getP(), parameters.getG(), generatePrivateKey), parameters), (AsymmetricKeyParameter) new DSAPrivateKeyParameters(generatePrivateKey, parameters));
    }

    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (DSAKeyGenerationParameters) keyGenerationParameters;
    }
}
