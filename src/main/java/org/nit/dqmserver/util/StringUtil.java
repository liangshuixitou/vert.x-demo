package org.nit.dqmserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.nit.dqmserver.constant.GlobalConsts.*;


/**
 * @author sensordb
 * @date 2018/5/21
 */

public class StringUtil {

    /**
     * 将文件转为字符串
     *
     * @param fileName 文件目录
     * @return 文件中的字符串
     */

    public static String readFileToString(final String fileName) {

        final File file = new File(fileName);
        final byte[] fileContent = new byte[(int) file.length()];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return new String(fileContent, UTF_8);
    }

    public static String boolToString(final boolean b) {
        if (b) {
            return TRUE_STR;
        }
        return FALSE_STR;
    }

    public static String objectToString(final Object o) {
        if (o == null) {
            return NULL_STR;
        }
        return o.toString();
    }

    public static String stringToObject(final String string) {
        if (string == null || NULL_STR.equals(string)) {
            return null;
        }
        return string;
    }

    public static String stringToHexString(final String str) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); ++i) {
            final char c = str.charAt(i);
            String hex = Integer.toHexString(c);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            if (i != str.length() - 1) {
                sb.append("0x" + hex + ",");
            } else {
                sb.append("0x" + hex);
            }
        }
        return sb.toString();
    }

    private static byte charToByte(final char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String intWithZeroPadding(final int number, final int length) throws Exception {
        final String numberSring = String.valueOf(number);
        if (numberSring.length() > length) {
            throw new Exception("");
        }
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length - numberSring.length(); ++i) {
            sb.append("0");
        }
        sb.append(numberSring);
        return sb.toString();
    }


    /**
     * 利用java原生的摘要实现SHA256加密
     *
     * @param str 加密后的报文
     * @return encodeSt
     */
    public static String encodeSHA256(String str) {
        if (str == null) {
            str = NULL_CHARACTER;
        }
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return stringBuffer.toString()
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    /**
     * MD5，将字符串映射为32位字符串
     *
     * @param str
     * @return
     */
    public static String stringToMD5(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            //字符串变比特串
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;

    }

    public static boolean isBlank(Object str) {
        int strLen;
        if (str != null && (strLen = str.toString().length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.toString().charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(Object str) {
        return !isBlank(str);
    }
}
