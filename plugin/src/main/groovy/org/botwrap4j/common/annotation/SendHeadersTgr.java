package org.botwrap4j.common.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface SendHeadersTgr {
    boolean disabled() default false;

    String key();

    String clusterId() default "";
}
