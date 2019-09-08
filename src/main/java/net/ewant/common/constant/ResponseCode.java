package net.ewant.common.constant;

/**
 * 响应状态码常量类
 * 0 表示成功
 * 1开头表示系统全局异常
 * 其他异常从2开头启用
 * 20 表示用户相关异常
 * 21、22、23、24、25 等等需要自行定义
 *
 * 注意！！！：【请相关开发人员严格按照约定执行，避免乱用，导致失控，维护困难】
 * Created by huangzh on 2018/10/26.
 */
public enum ResponseCode implements ResponseCodeMessage{

    OK("0", "请求成功"),

    /**1开头表示系统异常*/
    SYS_UNKNOWN_ERR("1001", "未知错误"),
    SYS_SERVER_ERR("1002", "服务器异常"),

    SYS_INVALID_PARAMS("1003", "参数异常"),
    SYS_INVALID_METHOD("1004", "请求方法不支持"),
    SYS_INVALID_MEDIA("1005", "请求媒体类型不支持"),
    SYS_INVALID_OPERA("1006", "无效操作"),
    SYS_FREQUENT_OPERATION("1007", "操作频繁"),
    SYS_RESPONSE_DATA_INVALID("1008", "响应数据类型不匹配"),

    COM_AUTH_PERMISSION_DENIED("1101", "没有访问权限"),
    COM_AUTH_EXPIRE("1102", "未登录或身份认证已失效"),

    COM_ADD_ERR("1201", "添加失败"),
    COM_UPD_ERR("1202", "更新失败"),
    COM_DEL_ERR("1203", "删除失败"),
    COM_SMS_ERR("1204", "验证码错误"),

    /**20开头表示用户异常*/
    USER_NOT_EXISTS("2001", "用户不存在"),
    USER_EXISTS("2002", "用户已存在"),
    USER_NOT_MATCH("2003", "用户名或密码错误"),
    USER_DISABLE("2004", "用户被禁用"),
    USER_PASS_SAME("2005", "新旧密码一致，不需要修改"),
    USER_REG_PASS_ERR("2006", "两次密码输入不一致"),
    USER_REG_ERR("2007", "注册失败"),
    USER_LOGIN_ERR("2008", "登录失败"),
    USER_REG_OK_LOGIN_ERR("2009", "注册成功，静默登录失败"),
    USER_NICKNAME_EXISTS("2010", "用户昵称已存在"),
    USER_OLD_PASS_ERR("2011", "旧密码错误"),

    /**21开头表示邮箱相关异常*/
    MAIL_ADDRESS_NOT_MATCH("2101", "邮箱地址格式不正确"),
    MAIL_SEND_FAILED("2102", "邮件发送失败"),
    PHONE_NOT_MATCH("2106", "手机号格式不正确"),
    PHONE_SMS_SEND_FAILED("2107", "短信验证码发送失败"),
    PHONE_SMS_CHECK_FAILED("2108", "短信验证码错误"),

    FILE_NOT_SUPPORT_UPLOAD_TYPE("2201", "不支持文件类型"),
    FILE_UPLOAD_SIZE_LIMITED("2202", "文件大小超出限制"),
    FILE_UPLOAD_ERR("2203", "文件传输失败"),
    FILE_NOT_EXISTS_INFO("2204", "没有存储文件扩展信息"),
    FILE_DELETE_FAILED("2205", "文件删除失败"),
    FILE_IS_EMPTY("2206", "上传文件为空"),

    ;
    private String code;
    private String msg;

    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
