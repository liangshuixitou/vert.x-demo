package org.nit.dqmserver.database;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import org.apache.log4j.Logger;
import org.nit.dqmserver.Config;
import org.nit.dqmserver.VertxInstance;
import org.nit.dqmserver.util.StringUtil;

/**
 * MySQL
 * @author sensordb
 * @date 2018/5/21
 */

public class MysqlConnection {
    private static Logger logger = Logger.getLogger(MysqlConnection.class);
    private Vertx vertx;
    private JDBCClient mySQLClient;

    public MysqlConnection() {
        //读取MySQL配置文件
        String databaseConfFile = Config.getDatabaseConfFile();
        //将配置转换为json
        final JsonObject config = new JsonObject(StringUtil.readFileToString(databaseConfFile));
        this.vertx = VertxInstance.getInstance().getVertx();
        this.mySQLClient = JDBCClient.createShared(vertx, config);
    }

    public SQLClient getMySQLClient() {
        return this.mySQLClient;
    }

}
