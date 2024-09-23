package org.botwrap4j.common.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ExecutorSinceTgr {

    boolean disabled() default false;

    String key();

    String clusterId() default "";
}
