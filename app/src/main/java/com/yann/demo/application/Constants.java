package com.yann.demo.application;

import com.yann.demo.common.bean.NetConfig;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import lib.core.utils.ExCommonUtil;

/**
 * Created by yayun.xia on 2017/8/30.
 */

public class Constants {
    private static NetConfig netConfig;

    /** 全局DecimalFormat用，一定要在构造方法中加入 */
    public static DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.CHINA);

    public static NetConfig getNetConfig() {
        if(ExCommonUtil.isEmpty(netConfig) || ExCommonUtil.isEmpty(netConfig.wirelessAPI)) {
            Environment.getInstance().loadNativeConfig();
            //拉取更新API接口
            Environment.getInstance().loadAPIConfig();
        }
        return netConfig;
    }

    public static boolean hasNetConfig() {
        return !ExCommonUtil.isEmpty(netConfig);
    }

    public static void setNetConfig(NetConfig netConfig) {
        Constants.netConfig = netConfig;
    }

    /**
     * 定义项目中全局变量标示
     */
    public static class SharePreferences {
        public static final String APP_SHARE_PRE_TOKEN = "APP_SHARE_PRE_TOKEN"; //token
        public static final String APP_SHARE_PRE_GUID = "APP_SHARE_PRE_GUID"; //guid
        public static final String APP_SHARE_PRE_LAST_LOGIN_MODE = "app_account_last_login_mode";//最后一次登录模式
        public static final String APP_SHARE_PRE_LAST_LOGIN_USERNAME = "app_account_last_login_username";//最后一次登录账户
        public static final String APP_SHARE_PRE_BIND_PHONE = "app_account_bind_phone";//用户绑定的手机
        public static final String APP_SHARE_PRE_IS_SET_LOGIN_PWD = "app_account_is_set_login_pwd";//是否设置登录密码

        public static final String APP_SHARE_PRE_SHOW_GUIDE = "app_show_guide"; // 首页是否显示引导气泡
        public static final String APP_SHARE_PRE_GIFT_BOMB_TIME = "app_gift_bomb_time"; // 首页新人礼包弹窗时间
        public static final String APP_SHARE_PRE_ADVERT_BOMB_TIME = "app_advert_bomb_time"; // 首页广告弹窗时间
        public static final String APP_SHARE_PRE_ADVERT_BOMB_PERIOD = "app_advert_bomb_period"; // 首页广告弹窗档期
        public static final String APP_SHARE_PRE_COUPON_BOMB_TIME = "app_coupon_bomb_time"; // 首页普通优惠券弹窗时间
        public static final String APP_SHARE_PRE_COUPON_BOMB_PERIOD = "app_coupon_bomb_period"; // 首页普通优惠券弹窗档期

        public static final String APP_SHARE_PRE_CLIENT_ID = "app_client_id";   // app的clientId
        public static final String APP_SHARE_PRE_LAST_UPDATE_VERSION = "LAST_UPDATE_VERSION";   // 上次取消更新的版本号
        public static final String APP_SHARE_PRE_LAST_UPDATE_TIME = "LAST_UPDATE_TIME";   // 上次取消更新的时间
        public static final String APP_SHARE_PRE_CURRENT_EVN = "CURRENT_EVN";   // 当前环境
        public static final String APP_SHARE_PRE_TAG_REF_CONTENT = "TAG_REF_CONTENT";   // ref
        public static final String APP_SHARE_PRE_TAG_REF_TIME = "TAG_REF_TIME";   // ref存储时间
        public static final String APP_SHARE_PRE_CLOSE_FIRST_LAUNCH = "CLOSE_FIRST_LAUNCH";   //是否开启引导页
        public static final String APP_SHARE_PRE_CATEGORY_TYPE = "CATEGORY_TYPE";   //新旧分类列表区分
    }

    /**
     * 定义项目中全局变量标示
     */
    public static class APP {
        public static final String APP_WEB_JS_ANDROID_API = "FNJSBridge";
        public static final String APP_WEB_INTENT_URL = "URL";
        public static final String APP_PAYLOAD = "PAYLOAD";

    }

    /**
     * 定义活动跳转常量
     */
    public static class ActivityType {

        public static final String ACTIVITY_OPENURL = "openurl"; //自定义URL
        public static final String ACTIVITY_HOMEPAGE = "homepage"; // 首页
        public static final String ACTIVITY_SEARCHLIST = "categorylist"; // 一级列表页
        public static final String ACTIVITY_CATEGORYLIST = "subcategorylist"; //二三级分类列表
        public static final String ACTIVITY_SEARCHLIST_OLD = "firstlevellist"; //TODO 旧一级分类
        public static final String ACTIVITY_ORDERDETAIL = "orderdetail"; // 订单详情
        public static final String ACTIVITY_MARKETING = "marketingcampaign"; // 行销活动
        public static final String ACTIVITY_SHOPCART = "shoppingcart"; // 购物车
        public static final String ACTIVITY_CATEGORY = "category"; // 分类页
        public static final String ACTIVITY_DETAIL = "productdetail"; // 商详页
        public static final String ACTIVITY_SEARCH = "search"; // 搜索关键字
        public static final String ACTIVITY_CENTER = "individualcenter"; // 个人中心
        public static final String ACTIVITY_ORDERLIST = "orderlist"; // 订单列表
        public static final String ACTIVITY_MYCOUPON = "mycoupon"; // 我的优惠券
        public static final String ACTIVITY_LOGIN = "login"; //自定义URL
        public static final String ACTIVITY_MODIFY_PAY_PASSWORD = "modifypaypassword"; //自定义URL
        public static final String ACTIVITY_BALANCE_DETAIL = "balancedetail"; //余额详情
        public static final String ACTIVITY_DISTRIBUTION_SCOPE = "storemap"; //门店配送范围
        public static final String ACTIVITY_OFTENBUYLIST= "oftenbuylist"; //常买清单

    }
}
