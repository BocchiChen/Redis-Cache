package com.github.IRedis.cache.core.util;

public class ArgUtil {
    public static void notNull(Object arg, String argName) {
        if (arg == null) {
            throw new IllegalArgumentException(argName + " cannot be null");
        }
    }

    public static void notNegative(Object arg, String argName) {
        if (arg == null) {
            throw new IllegalArgumentException(argName + " cannot be null");
        }
        if (!(arg instanceof Integer) || (Integer) arg < 0) {
            throw new IllegalArgumentException(argName + " cannot be negative");
        }
    }
}
