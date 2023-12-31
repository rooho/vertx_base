package com.scaffolding.core.util;

import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ServiceProxyBuilder;

public final class AsyncServiceUtil {

    public static <T> T getAsyncServiceInstance(Class<T> asClazz, Vertx vertx) {
        String address = asClazz.getName();
        return new ServiceProxyBuilder(vertx).setAddress(address).build(asClazz);
    }

    public static <T> T getAsyncServiceInstance(Class<T> asClazz) {
        String address = asClazz.getName();
        return new ServiceProxyBuilder(VertxHolder.getVertxInstance()).setAddress(address).build(asClazz);
    }
}
