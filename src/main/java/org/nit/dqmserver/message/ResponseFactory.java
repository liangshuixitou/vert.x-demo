
package org.nit.dqmserver.message;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;


/**
 * @author sensordb
 * @date 2018/5/21
 */

public class ResponseFactory {

    private RoutingContext routingContext;
    private Request request;

    public ResponseFactory(final RoutingContext routingContext, final Request request) {
        this.routingContext = routingContext;
        this.request = request;
    }

    public ResponseFactory(final RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    public ResponseFactory(final Request request) {
        this.request = request;
    }

    public void error(final int code, final String message) {
        final Error error = new Error(this.request.getID(), code, message);

        if (!this.routingContext.response().closed() && !this.routingContext.response().ended()) {
            this.routingContext.response().end(error.toString());
        }
    }

    public void success(final JsonObject result) {
        final Success success = new Success(this.request.getID(), result);

        if (!this.routingContext.response().closed() && !this.routingContext.response().ended()) {
            this.routingContext.response().end(success.toString());
        }

    }

    public static void error(final RoutingContext routingContext, final int id, final int code, final String message) {
        final Error error = new Error(id, code, message);
        if (!routingContext.response().closed() && !routingContext.response().ended()) {
            routingContext.response().end(error.toString());
        }

    }

}
