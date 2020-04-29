package dji.sdksharedlib.keycatalog.extension;

import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Key {
    int accessType();

    int autoGetInterval() default 1000;

    Class[] excludedAbstractions() default {};

    int expirationDuration() default 100;

    Class[] includedAbstractions() default {};

    int protectDuration() default 500;

    Class type() default Object.class;

    Class[] types() default {};

    DJISDKCacheUpdateType updateType() default DJISDKCacheUpdateType.DYNAMIC;
}
