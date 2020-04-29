package com.dji.lifecycle.core.annotation;

import com.dji.lifecycle.core.LifecycleEvent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicConfig {
    LifecycleEvent end() default LifecycleEvent.ON_APP_STOP;

    LifecycleEvent start() default LifecycleEvent.ON_APP_START;
}
