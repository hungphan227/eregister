package com.hungphan.studentapplication;

import io.vertx.core.Vertx;

public class VertxSingleton {

    private static Vertx vertx = Vertx.vertx();

    private VertxSingleton () {}

    public static Vertx getInstance() {
        return vertx;
    }
}
