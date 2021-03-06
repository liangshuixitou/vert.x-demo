package org.nit.dqmserver;

import io.vertx.core.Vertx;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.nit.dqmserver.util.DateUtil;
import org.nit.dqmserver.util.Tools;

import java.util.Date;

import static org.nit.dqmserver.constant.GlobalConsts.YZ_HOME;

/**
 * @author eda
 * @date 2018/5/21
 */

public class Startup {
    private static String LOG_FILE = ((System.getenv(YZ_HOME) == null) ? "./conf/log4j.properties" :
            (System.getenv(YZ_HOME) + "/conf/log4j.properties"));
    protected static final Logger logger = Logger.getLogger(Startup.class);

    public static void main(final String[] args) {
        Tools.initSystemProperties();
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4jLogDelegateFactory");
        PropertyConfigurator.configure(LOG_FILE);

        logger.info(("@@@@@@@@@@@@@@@" + DateUtil.dateToStr(new Date()) + " Data quality manager system Server startup"));
        try {
            //获取 vertx 实例
            final Vertx vertx = Vertx.vertx();
            VertxInstance.getInstance().setVertx(vertx);
            //实例化 Server 对象
            final Server server = new Server();
            vertx.deployVerticle(server);
        } catch (Throwable e) {
            final String es = Tools.getTrace(e);
            logger.error(es);
            logger.info(("@@@@@@@@@@@@@@@" + DateUtil.dateToStr(new Date()) + " Data quality manager system Server exit with exception:" + es));
        }
    }
}
