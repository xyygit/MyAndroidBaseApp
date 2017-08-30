package com.yann.demo.common.bean;

/**
 * 网络请求码
 * Created by yayun.xia on 2017/8/30.
 */

public class NetCode {
    public static final int SUCCESS = 0;
    public static final int ERROR_CODE_NORMAL = 1000; //一般性错误
    public static final int ERROR_CODE_INVALIDE_TOKEN = 2001; //无效Token
    public static final int ERROR_CODE_INVALIDE_CAPTCHA = 2002; //无效验证码
    public static final int ERROR_CODE_PARAM_ERROR = 2003; //请求参数错误
    public static final int ERROR_CODE_USER_EXSIST = 4001; //用户已经存在，请去登录
    public static final int ERROR_CODE_SEND_VOICE_CAPTCHA = 4002; //手机验证码通过语音发送
    public static final int ERROR_CODE_MORE_ERROR_CAPTCHA = 4003; //本手机校验短信验证码错误次数太多不能再发送
    public static final int ERROR_CODE_CAPTCHA_ONE_PHONE_ERROR = 4004; //同一台手机1分钟内可以最多发送5个不同的号码,同一个手机一分钟内只能发送一次
    public static final int ERROR_CODE_LOGIN_PIC = 9003;// 登录失败超过指定次数,返回此code前端则显示图片验证码
    public static final int ERROR_CODE_LOGIN_WARN_REMOTE = 9004;// 异地登录需要验证短信或邮箱验证码,返回此code前端则显示验证码,
    public static final int ERROR_CODE_PHONE_ALREADY_REGISTER = 9005;//当前手机号码已经注册
    public static final int ERROR_CODE_BLACK_ACCOUNT = 9006;//当前账户已在黑名单内
    public static final int ERROR_CODE_LOGIN_EXPIRED = 9001;//账户登陆态过期
    public static final int ERROR_CORD_SYSTEM_MAINTENANCE = 9999;//系统维护
    public static final int ERROR_CORD_PASSWORD_CHANGE = 9000;//账户密码已变更，请重新登录
}
