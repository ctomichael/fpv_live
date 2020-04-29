package dji.midware.util;

import java.security.SecureRandom;

public class RandomUtils {
    public static int nextInt(int size) {
        return createRandom().nextInt(size);
    }

    public static SecureRandom createRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (Exception e) {
            return null;
        }
    }
}
