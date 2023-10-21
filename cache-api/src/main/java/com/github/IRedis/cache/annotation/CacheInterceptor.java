package com.github.IRedis.cache.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheInterceptor {

    boolean common() default true;

    boolean refresh() default false;

    boolean aof() default false;

    boolean evict() default false;
}
