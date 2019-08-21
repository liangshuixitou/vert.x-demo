package org.nit.dqmserver;

import io.vertx.core.Vertx;

/**
 * @author sensordb
 * @date 2018/5/21
 */

public class VertxInstance {
    private Vertx vertx;
    private static VertxInstance instance;

    private VertxInstance() {
    }

    public static VertxInstance getInstance() {
        if (instance == null) {
            instance = new VertxInstance();
        }
        return instance;
    }

    public void setVertx(Vertx vertx) {
        this.vertx = vertx;
    }

    public Vertx getVertx() {
        return vertx;
    }
}
