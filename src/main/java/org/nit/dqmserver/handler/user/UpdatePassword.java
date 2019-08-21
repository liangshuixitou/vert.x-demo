package org.nit.dqmserver.handler.user;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.dqmserver.constant.HttpHeaderContentType;
import org.nit.dqmserver.database.MysqlConnection;
import org.nit.dqmserver.message.AbstractRequestHandler;
import org.nit.dqmserver.message.Request;
import org.nit.dqmserver.message.ResponseFactory;
import org.nit.dqmserver.util.FormValidator;
import org.nit.dqmserver.util.StringUtil;
import org.nit.dqmserver.util.Tools;

import static org.nit.dqmserver.constant.GlobalConsts.USERNAME_STR;
import static org.nit.dqmserver.constant.ResponseError.*;
import static org.nit.dqmserver.constant.UserConsts.*;

/**
 * 删除规范性细粒度规则
 *
 * @author eda
 * @date 2018/5/21
 */

public class UpdatePassword extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(UpdatePassword.class);
    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();

    @Override
    public void handle(final RoutingContext routingContext, final Request request) {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);
        String user = request.getString(USERNAME_STR);

        Object userIdObj = request.getParamWithKey(USER_ID_STR);
        Object newPasswordObj = request.getParamWithKey(NEW_PASSWORD_STR);


        // 验证非空
        if (userIdObj == null) {
            logger.info(String.format("user: %s exception: %s", user, "userId为必填参数"));
            response.error(USER_ID_IS_REQUIRED.getCode(), USER_ID_IS_REQUIRED.getMsg());
            return;
        }

        // 验证整型
        if (!FormValidator.isInteger(userIdObj)) {
            logger.info(String.format("user: %s exception: %s", user, "userId格式非法"));
            response.error(USER_ID_FORMAT_ERROR.getCode(), USER_ID_FORMAT_ERROR.getMsg());
            return;
        }

        if (newPasswordObj == null) {
            logger.info(String.format("exception:%s", "newPassword为必填参数"));
            response.error(NEW_PASSWORD_IS_REQUIRED.getCode(), NEW_PASSWORD_IS_REQUIRED.getMsg());
            return;
        }

        if (!FormValidator.isString(newPasswordObj)
                || !FormValidator.lengthBetween(newPasswordObj.toString(), PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH)) {
            logger.info(String.format("exception:%s", "newPassword格式非法"));
            response.error(NEW_PASSWORD_FORMAT_ERROR.getCode(), NEW_PASSWORD_FORMAT_ERROR.getMsg());
            return;
        }

        String sql = "UPDATE `tbl_user` SET `password` = ? WHERE `id` = ? AND deleted_at IS NULL;";

        //sha256加密存储
        String newPassword = StringUtil.encodeSHA256(newPasswordObj.toString());

        JsonArray params = new JsonArray().add(newPassword).add((Integer) userIdObj);

        mySQLClient.updateWithParams(sql, params, ar -> {
            if (ar.failed()) {
                logger.error(String.format("user: %s query: %s exception: %s", user, sql, Tools.getTrace(ar.cause())));
                response.error(SERVER_ERROR.getCode(), SERVER_ERROR.getMsg());
                return;
            }
            logger.info("user: " + user + " update" + ar.result().getUpdated() + "rules");
            response.success(null);

        });


    }

}
