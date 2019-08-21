package org.nit.dqmserver.constant;


/**
 * 错误码常量
 *
 * @author sensordb
 * @date 2018/5/21
 */

public enum ResponseError {

    // 0.全局错误

    DECODE_ERROR(1, "请求解析错误"),
    METHOD_NOT_FOUND(2, "方法不存在或者不可见"),
    SERVER_ERROR(3, "服务器内部错误"),
    CLIENT_NOT_LOGIN(4, "客户端未登录"),
    LOGIN_TIMEOUT(5, "用户登录超时"),
    PAGE_FORMAT_ERROR(6, "页码格式非法"),
    CHECK_TYPE_IS_REQUIRED(7, "checkType为必填参数"),
    CHECK_TYPE_FORMAT_ERROR(8, "checkType格式非法"),
    START_TIME_H_IS_REQUIRED(9, "startTime为必填参数"),
    END_TIME_H_IS_REQUIRED(10, "endTime为必填参数"),
    START_TIME_H_FORMAT_ERROR(11, "startTime格式非法"),
    END_TIME_H_FORMAT_ERROR(12, "startTime格式非法"),

    // 1.用户模块
    USERNAME_IS_REQUIRED(10001, "username为必填参数"),
    PASSWORD_IS_REQUIRED(10002, "password为必填参数"),
    USERNAME_FORMAT_ERROR(10003, "username格式非法"),
    PASSWORD_FORMAT_ERROR(10004, "password格式非法"),
    USERNAME_PASSWORD_ERROR(10005, "用户名或密码错误"),
    USERNAME_EXISTED_ERROR(10006, "该用户已经存在"),
    USER_ID_IS_REQUIRED(10007, "userId为必填参数"),
    USER_ID_FORMAT_ERROR(10008, "userId格式非法"),
    NEW_PASSWORD_IS_REQUIRED(10009, "newPassword为必填参数"),
    NEW_PASSWORD_FORMAT_ERROR(10010, "newPassword格式非法");

    private String msg;
    private int code;

    ResponseError(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getCode() {
        return this.code;
    }


}
