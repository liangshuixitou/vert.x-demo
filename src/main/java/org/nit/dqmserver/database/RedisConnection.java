package org.nit.dqmserver.database;

import io.vertx.core.Vertx;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import org.apache.log4j.Logger;
import org.nit.dqmserver.VertxInstance;
import org.nit.dqmserver.util.IniUtil;

/**
 * Redis
 * @author eda
 * @date 2018/6/6
 */

public class RedisConnection {
    private static Logger logger = Logger.getLogger(RedisConnection.class);

    private RedisClient redisClient;

    public RedisConnection() {
        //读取MySQL配置文件
        String host = IniUtil.getInstance().getRedisHost();

        Vertx vertx = VertxInstance.getInstance().getVertx();
        this.redisClient = RedisClient.create(vertx, new RedisOptions().setHost(host));
    }
    public RedisClient getRedisClient() {
        return this.redisClient;
    }
}
