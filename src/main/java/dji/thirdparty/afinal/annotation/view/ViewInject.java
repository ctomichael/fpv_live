package dji.thirdparty.afinal.annotation.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {
    String click() default "";

    int id() default 0;

    String itemClick() default "";

    String itemLongClick() default "";

    String longClick() default "";

    String name() default "";

    Select select() default @Select(selected = "");
}
