package dji.component.privacy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckGDPRPermissions {
    public static final int FIND_MY_DRONE = 1;
    public static final int FLIGHT_RECORD = 6;
    public static final int FLYSAFE_SEARCH = 2;
    public static final int FPV_MAP = 0;
    public static final int RECOMMEND_POINTS = 3;
    public static final int RETURN_POINT_LIST = 4;
    public static final int SKYPIXEL_SHARE = 5;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AffectedFunction {
    }

    int affected() default -1;

    PrivacyType[] type() default {};
}
