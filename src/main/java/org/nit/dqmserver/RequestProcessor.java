package org.nit.dqmserver;


import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.dqmserver.handler.user.*;
import org.nit.dqmserver.message.AbstractRequestHandler;
import org.nit.dqmserver.message.Request;
import org.nit.dqmserver.message.ResponseFactory;

import java.util.Hashtable;

import static org.nit.dqmserver.constant.ResponseError.METHOD_NOT_FOUND;


/**
 * @author sensordb
 * @date 2018/5/21
 */

public class RequestProcessor {
    protected static final Logger logger = Logger.getLogger(RequestProcessor.class);
    private static RequestProcessor instance;
    private Hashtable<String, AbstractRequestHandler> requestHandlerMap;

    private RequestProcessor() {
        this.requestHandlerMap = new Hashtable<>();
        this.initRequestHandlers();
    }

    private void initRequestHandlers() {

        this.requestHandlerMap.put("user.Login", new Login());
        this.requestHandlerMap.put("user.UpdatePassword", new UpdatePassword());


    }


    public static RequestProcessor getInstance() {
        if (RequestProcessor.instance == null) {
            RequestProcessor.instance = new RequestProcessor();
        }
        return RequestProcessor.instance;
    }

    private boolean isRequestValid(final Request request) {
        return true;
    }


    public void processRequest(final RoutingContext routingContext, final Request request) {

        final AbstractRequestHandler requestHandler = this.requestHandlerMap.get(request.getMethod());
        if (requestHandler == null) {
            RequestProcessor.logger.error(String.format("request:%s cannot find a handler", request.toString()));

            ResponseFactory response = new ResponseFactory(routingContext, request);
            response.error(METHOD_NOT_FOUND.getCode(), METHOD_NOT_FOUND.getMsg());
            return;
        }

        // 注册中间件
        Middleware mw = new Middleware(routingContext, request, requestHandler);
        // 不需要验证token的接口
        JsonArray notVerifyUserTokenArr = new JsonArray().add("user.Login");

        if (!notVerifyUserTokenArr.contains(request.getMethod())) {
            mw.verifyUserToken();
        } else {
            logger.info(String.format("request:%s find a handler", request.toString()));
            requestHandler.handle(routingContext, request);
        }

    }
}
