package org.nit.dqmserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.apache.log4j.Logger;
import org.nit.dqmserver.constant.HttpHeaderContentType;
import org.nit.dqmserver.message.Request;
import org.nit.dqmserver.message.ResponseFactory;
import org.nit.dqmserver.util.IniUtil;
import org.nit.dqmserver.util.Tools;

import java.util.HashSet;
import java.util.Set;

import static org.nit.dqmserver.constant.ResponseError.DECODE_ERROR;


/**
 * @author sensordb
 * @date 2018/5/21
 */

public class Server extends AbstractVerticle {
    protected static final Logger logger = Logger.getLogger(Server.class);
    private Router router;
    private int port = IniUtil.getInstance().getHTTPServerPort();
    private String host = IniUtil.getInstance().getServerHostName();
    private HttpServer server;
    public Server() {

    }

    private void init() {
        super.init(vertx, context);
        this.server = vertx.createHttpServer();
        this.router = Router.router(vertx);
        this.router.route().handler(BodyHandler.create().setBodyLimit(2000000L));
        final Set<String> allowHeaders = new HashSet<String>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        final Set<HttpMethod> allowMethods = new HashSet<HttpMethod>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);
        this.router.route().handler(CorsHandler.create("*").allowedMethods(allowMethods).allowedHeaders(allowHeaders));
        this.initTest();
        this.initWeb();
        this.initRequestProcessor();
    }

    @Override
    public void start(){
        this.init();
        this.server.requestHandler(this.router::accept).listen(this.port);
        logger.info(String.format("server:%s is now listening at port:%d", this.host, this.port));
        logger.info(String.format("please visit url: http://%s:%d%s", this.host, this.port, "/"));
    }

    private void initRequestProcessor() {
        final String urlPath = "/";
        logger.info(("Router:" + urlPath));
        this.router.post(urlPath).handler(routingContext -> {
            routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);

            final Buffer buffer = routingContext.getBody();
            try {
                final Request request = new Request(buffer.toString());
                logger.info(String.format("server received request:%s", request.toString()));
                RequestProcessor.getInstance().processRequest(routingContext, request);
            }
            catch (Exception e) {
                logger.error(String.format("server received:%s to json object error exception:%s", buffer.toString(), Tools.getTrace(e)));
                int errorRequestId = -1;
                ResponseFactory.error(routingContext, errorRequestId, DECODE_ERROR.getCode(), DECODE_ERROR.getMsg());
            }
        });
    }

    private void initWeb() {
        final String urlPath = "/*";
        logger.info(("Router:" + urlPath));
        router.route(urlPath).handler(StaticHandler.create("assets"));
    }

    private void initTest() {
        final String urlPath = "/test";
        logger.info(("Router:" + urlPath));
        this.router.get(urlPath).handler(routingContext -> {
            final Buffer buffer = routingContext.getBody();
            logger.info(String.format("server received:%s", buffer.toString()));
            final JsonObject result = new JsonObject();
            result.put("status", "success");
            routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.TEXT_PLAIN);
            routingContext.response().end(result.toString());
            logger.info(String.format("server send:%s", result.toString()));
        });
    }


}
