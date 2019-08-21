package org.nit.dqmserver.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.regex.Pattern;

/**
 * 表单验证
 * @author eda
 * @date 2018/7/4
 */

public class FormValidator {
    /**
     * 正则验证
     *
     * @param value 表单值
     * @param regex 正则表达式
     * @return boolean
     */
    public static boolean regex(final String value, final String regex) {
        boolean res = false;
        if (value != null && regex != null) {
            res = Pattern.matches(regex, value);
        }
        return res;
    }

    public static boolean lengthBetween(final String value, final int minLength, final int maxLength) {
        boolean res = false;
        if (value == null) {
            return false;
        }
        if (value.length() >= minLength && value.length() <= maxLength) {
            res = true;
        }
        return res;
    }

    public static boolean in(final int value, final int ... range) {
        boolean res = false;

        for (int i: range) {
            if(value == i) {
                res = true;
            }
        }
        return res;
    }

    public static boolean in(final String value, final String ... range) {
        boolean res = false;

        if (value == null) {
            return false;
        }

        for (String i: range) {
            if(value.equals(i)) {
                res = true;
            }
        }
        return res;
    }

    public static boolean between(final int value, final int minValue, final int maxValue) {
        return value >= minValue && value <= maxValue;
    }

    public static boolean isString(final Object obj) {
        return obj instanceof String;

    }

    public static boolean isInteger(final Object obj) {
        return obj instanceof Integer;
    }

    public static boolean isLong(final Object obj) {
        return obj instanceof Long;
    }

    public static boolean isBoolean(final Object obj) {
       return obj instanceof Boolean;
    }


    public static boolean isFloat(final Object obj) {
        return obj instanceof Float;
    }

    public static boolean isDouble(final Object obj) {
        return obj instanceof Double;
    }


    public static boolean isJsonArray(final Object obj) {
        return obj instanceof JsonArray;
    }


    public static boolean isJsonObject(final Object obj) {
        return obj instanceof JsonObject;
    }

    public static boolean isValidYYYYMMDDHH(final Object obj) {
        String dateRegex = "^[1-9]\\d{3}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])+(20|21|22|23|[0-1]\\d)$";

        boolean res = false;
        if (obj == null) {
            return false;
        }

        if(Pattern.matches(dateRegex, obj.toString()))
        {
            res = true;
        }

        return res;
    }

}
