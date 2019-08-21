package org.nit.dqmserver.message;

import io.vertx.ext.web.RoutingContext;

/**
 * @author sensordb
 * @date 2018/5/21
 */

public abstract class AbstractRequestHandler {
    private String name;

    public AbstractRequestHandler(final String name) {
        this.name = name;
    }

    public AbstractRequestHandler() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * 接受请求
     *
     * @param p0 Request
     * @param p1 RoutingContext
     */

    public abstract void handle(final RoutingContext p0, final Request p1);
}
