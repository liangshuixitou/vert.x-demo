package org.nit.dqmserver.handler.user;


import io.vertx.core.Future;
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
import org.nit.dqmserver.util.FormValidator;
import org.nit.dqmserver.util.StringUtil;
import org.nit.dqmserver.util.Tools;

import java.util.Date;

import static org.nit.dqmserver.constant.GlobalConsts.*;
import static org.nit.dqmserver.constant.ResponseError.*;
import static org.nit.dqmserver.constant.UserConsts.*;

/**
 * 用户登录
 *
 * @author eda
 * @date 2018/5/21
 */

public class Login extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(Login.class);
    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();

    @Override
    public void handle(final RoutingContext routingContext, final Request request) {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        // 获取参数
        Object usernameObj = request.getParamWithKey(USERNAME_STR);
        Object passwordObj = request.getParamWithKey(PASSWORD_STR);

        // 验证非空
        if (usernameObj == null) {
            logger.info(String.format("exception:%s", "username为必填参数"));
            response.error(USERNAME_IS_REQUIRED.getCode(), USERNAME_IS_REQUIRED.getMsg());
            return;
        }
        if (passwordObj == null) {
            logger.info(String.format("exception:%s", "password为必填参数"));
            response.error(PASSWORD_IS_REQUIRED.getCode(), PASSWORD_IS_REQUIRED.getMsg());
            return;
        }

        // 验证表单数据格式
        if (!FormValidator.isString(usernameObj)
                || !FormValidator.lengthBetween(usernameObj.toString(), USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH)) {
            logger.info(String.format("exception:%s", "username格式非法"));
            response.error(USERNAME_FORMAT_ERROR.getCode(), USERNAME_FORMAT_ERROR.getMsg());
            return;
        }

        if (!FormValidator.isString(passwordObj)
                || !FormValidator.lengthBetween(passwordObj.toString(), PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH)) {
            logger.info(String.format("exception:%s", "password格式非法"));
            response.error(PASSWORD_FORMAT_ERROR.getCode(), PASSWORD_FORMAT_ERROR.getMsg());
            return;
        }

        String username = usernameObj.toString();
        //sha256加密存储
        String password = StringUtil.encodeSHA256(passwordObj.toString());
        String query = "SELECT id, role FROM tbl_user " +
                "WHERE username = ? " +
                "AND password = ? " +
                "AND deleted_at IS NULL;";
        JsonArray params = new JsonArray().add(username).add(password);

        Future<JsonObject> loginFuture = Future.future();
        mySQLClient.queryWithParams(query,
                params, ar -> {
                    if (ar.failed()) {
                        logger.error(String.format("query:%s exception:%s", query, Tools.getTrace(ar.cause())));
                        response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
                        return;
                    }
                    if (ar.result().getNumRows() == 0) {
                        logger.info(String.format("exception:%s", "用户名或密码错误"));
                        response.error(USERNAME_PASSWORD_ERROR.getCode(), USERNAME_PASSWORD_ERROR.getMsg());
                        return;
                    }
                    loginFuture.complete(ar.result().getRows().get(0));

                });

        // 更新token
        loginFuture.setHandler(loginAr -> {

            JsonObject userInfo = loginAr.result();
            long current = System.currentTimeMillis();
            current += 2 * 60 * 60 * 1000;
            Date date = new Date(current);
            String tokenExpirationTime = DateUtil.dateToStr(date);
            String token = StringUtil.stringToMD5(tokenExpirationTime + username);
            String updateTokenSQL = "UPDATE tbl_user " +
                    "SET token = ? , token_expiration_time = ? WHERE id = ?";

            JsonArray updateParams = new JsonArray().add(token).add(tokenExpirationTime).add(userInfo.getInteger(ID_UNDERLINE));
            mySQLClient.updateWithParams(updateTokenSQL,
                    updateParams, ur -> {
                        if (ur.succeeded() && ur.result().getUpdated() > 0) {
                            //token 更新成功即登录成功，返回token
                            JsonObject result = new JsonObject();
                            result.put(TOKEN_STR, token);
                            result.put(ROLE_STR, userInfo.getInteger(ROLE_UNDERLINE));
                            result.put(USER_ID_STR, userInfo.getInteger(ID_UNDERLINE));
                            logger.info(String.format("用户%s登录成功", username));
                            response.success(result);
                        } else {
                            logger.error(String.format("query:%s exception:%s", updateTokenSQL, Tools.getTrace(ur.cause())));
                            response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
                        }
                    });
        });

    }


}
