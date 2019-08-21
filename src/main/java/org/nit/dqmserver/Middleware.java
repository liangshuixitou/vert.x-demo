package org.nit.dqmserver;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.dqmserver.constant.HttpHeaderContentType;
import org.nit.dqmserver.database.MysqlConnection;
import org.nit.dqmserver.message.AbstractRequestHandler;
import org.nit.dqmserver.message.Request;
import org.nit.dqmserver.message.ResponseFactory;
import org.nit.dqmserver.util.DateUtil;
import org.nit.dqmserver.util.Tools;

import java.util.Date;
import java.util.List;

import static org.nit.dqmserver.constant.GlobalConsts.TOKEN_STR;
import static org.nit.dqmserver.constant.GlobalConsts.USERNAME_STR;
import static org.nit.dqmserver.constant.ResponseError.*;
import static org.nit.dqmserver.constant.UserConsts.TOKEN_EXPIRATION_TIME_UNDERLINE;


/**
 * 请求中间件
 *
 * @author eda
 * @date 2018/7/6
 */

public class Middleware {

    private RoutingContext routingContext;
    private Request request;
    private AbstractRequestHandler requestHandler;
    protected static final Logger logger = Logger.getLogger(Middleware.class);
    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();

    public Middleware(final RoutingContext routingContext, final Request request, final AbstractRequestHandler requestHandler) {
        this.routingContext = routingContext;
        this.request = request;
        this.requestHandler = requestHandler;
    }

    public void verifyUserToken() {

        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        String token = request.getValue(TOKEN_STR).toString();

        String query = "SELECT token_expiration_time, username " +
                "FROM tbl_user " +
                "WHERE token = ?;";

        JsonArray params = new JsonArray().add(token);
        mySQLClient.queryWithParams(query,
                params, ar -> {

                    if (ar.failed()) {
                        logger.error(String.format("[token verify] query:%s exception:%s", query, Tools.getTrace(ar.cause())));
                        response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
                        return;
                    }

                    if (ar.result().getNumRows() == 0) {
                        logger.info(String.format("[token verify] exception:%s", "用户未登录"));
                        response.error(CLIENT_NOT_LOGIN.getCode(), CLIENT_NOT_LOGIN.getMsg());
                        return;
                    }

                    String tokenExpirationTimeStr = ar.result().getRows().get(0).getString(TOKEN_EXPIRATION_TIME_UNDERLINE);
                    Date tokenExpirationTime = DateUtil.utcStringToDate(tokenExpirationTimeStr);

                    if (tokenExpirationTime == null || new Date().after(tokenExpirationTime)) {
                        logger.info(String.format("[token verify] exception:%s", "用户登录超时"));
                        response.error(LOGIN_TIMEOUT.getCode(), LOGIN_TIMEOUT.getMsg());
                        return;
                    }
                    // 获取用户名, 追加到request中
                    List<JsonObject> res = ar.result().getRows();
                    String username = res.isEmpty() ? "anonymity" : res.get(0).getString(USERNAME_STR);
                    request.put(USERNAME_STR, username);

                    logger.info(String.format("user: %s request:%s find a handler", username, request.toString()));
                    requestHandler.handle(routingContext, request);
                });

    }


}
