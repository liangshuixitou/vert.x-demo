package org.nit.dqmserver.util;

/**
 * @author eda
 * @date 2018/6/8
 */

public class ArrayUtil {

    /**
     *
     * @param var0 字符串数组
     * @param split 分割符
     * @return 字符串
     */

    public static String arrayToString(String[] var0, String split) {
        if (var0 == null) {
            return null;
        } else {
            StringBuffer var1 = new StringBuffer();

            for (int i = 0; i < var0.length; ++i) {
                var1.append((var0[i] != null ? var0[i].replaceAll("\\s", "") : ""));
                if (i != var0.length - 1) {
                    var1.append(split);
                }
            }

            return var1.toString();
        }
    }
}
