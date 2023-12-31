package com.scaffolding.core.util;

import io.vertx.core.Vertx;
import java.util.Objects;

/**
 * 保存vertx实例
 */
public final class VertxHolder {

    private static Vertx singletonVertx;

    public static void init(Vertx vertx) {
        Objects.requireNonNull(vertx, "未初始化Vertx");
        singletonVertx = vertx;
    }

    public static Vertx getVertxInstance() {
        Objects.requireNonNull(singletonVertx, "未初始化Vertx");
        return singletonVertx;
    }
}
