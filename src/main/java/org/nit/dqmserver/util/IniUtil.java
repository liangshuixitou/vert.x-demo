package org.nit.dqmserver.util;

import org.ini4j.Wini;
import org.nit.dqmserver.Config;

import java.io.File;
import java.io.IOException;

import static org.nit.dqmserver.constant.GlobalConsts.*;

/**
 * @author sensordb
 * @date 2018/5/21
 */

public class IniUtil {

    private static IniUtil instance;
    private static String initFilePath = Config.getConfFile();

    private Wini ini;

    public static final int TEST_MODE = 0;
    public static final int LOCAL_DEBUG_MODE = 1;
    public static final int PRODUCT_MODE = 2;

    private IniUtil() {
        try {
            ini = new Wini(new File(initFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IniUtil(String initFilePath) {
        try {
            ini = new Wini(new File(initFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setInstance(IniUtil iniUtil) {
        IniUtil.instance = iniUtil;
    }


    private static void changeIniUtil(String initFilePath) {
        IniUtil iniUtil = new IniUtil(initFilePath);
        IniUtil.setInstance(iniUtil);
    }

    private static void changeToTestServer() {
        IniUtil.changeIniUtil("./conf/configTestNode.ini");
    }

    private static void changeToProductServer() {
        IniUtil.changeIniUtil("./conf/configProductNode.ini");
    }


    private static void changeToLocalDebugServer() {
        IniUtil.changeIniUtil("./conf/configLocalDebug.ini");
    }

    public static void changeMode(int mode) {
        if (mode == IniUtil.TEST_MODE) {
            IniUtil.changeToTestServer();
        } else if (mode == IniUtil.LOCAL_DEBUG_MODE) {
            IniUtil.changeToLocalDebugServer();
        } else if (mode == IniUtil.PRODUCT_MODE) {
            IniUtil.changeToProductServer();
        }
    }

    public static IniUtil getInstance() {
        if (instance == null) {
            instance = new IniUtil();
        }
        return IniUtil.instance;
    }


    public int getHTTPServerPort() {
        try {
            return Integer.parseInt(ini.get(HTTP_STR, PORT_STR));

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 80;
        }
    }

    public String getServerHostName() {
        return ini.get(SERVER_STR, HOST_STR);
    }

    public String getRedisHost() {
        return ini.get(REDIS_STR, HOST_STR);

    }

    public String getRedisPort() {
        return ini.get(REDIS_STR, PORT_STR);
    }


}
