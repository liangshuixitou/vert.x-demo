package org.nit.dqmserver;


import static org.nit.dqmserver.constant.GlobalConsts.YZ_HOME;

/**
 * @author eda
 * @date 2018/5/21
 */

public class Config {

    private static String confRoot = ((System.getenv(YZ_HOME) == null) ? "./conf/" :
            (System.getenv(YZ_HOME) + "/conf/"));

    private static String confFile = confRoot + "config.ini";
    private static String localConfFile = confRoot + "configLocalDebug.ini";
    private static String remoteConfFile = confRoot + "configProductNode.ini";

    private static String databaseConfFile = confRoot + "database.conf";
    private static String localDatabaseConfFile = confRoot + "databaseLocalDebug.conf";
    private static String remoteDatabaseConfFile = confRoot + "databaseRemoteDebug.conf";

    private static String hbaseConfFile = confRoot + "hbase.conf";


    public static void setLocalConfFile() {
        Config.confFile = localConfFile;
    }

    public static void setRemoteConfFile() {
        Config.confFile = remoteConfFile;
    }

    public static String getConfFile() {
        return Config.confFile;
    }

    public static void setDatabaseConfFile(final String databaseConfFile) {
        Config.databaseConfFile = databaseConfFile;
    }

    public static void setLocalDatabaseConfFile() {
        Config.databaseConfFile = localDatabaseConfFile;
    }

    public static void setRemoteDatabaseConfFile() {
        Config.databaseConfFile = remoteDatabaseConfFile;
    }

    public static String getDatabaseConfFile() {
        return Config.databaseConfFile;
    }

    public static String getHbaseConfFile() {
        return Config.hbaseConfFile;
    }

}
