package com.yann.demo.common.bean;

/**
 * Created by yayun.xia on 2017/8/30.
 */

public class NetConfig extends NetResponse<NetConfig> {

    public WirelessAPI wirelessAPI = new WirelessAPI();

    public static class WirelessAPI {

        public String tokenGetToken = ""; // 获取 TOKEN
        public String apnsSetCidDeviceToken = ""; // 个推上报CID
        public String welcomePage = ""; //欢迎页
        public String homePage = ""; //首页
        public String goodsdetail = "";//商详页
        public String popupWindow = ""; //首页弹窗
        public String sendGiftOnLoad = ""; //首页新人礼包
        public String merchandiseSearchByKey = ""; //关键词搜索
        public String merchandiseSearchByCategory = ""; //类目搜索
        public String accountDetails = ""; // 结算页详细信息
        public String accountCreateOrder = ""; // 下单
        public String accountPaymentList = ""; // 支付方式列表
        public String accountPay = ""; // 支付
        public String accountPaySuc = ""; // 支付成功页
        public String accountGetVoucherList = ""; // 结算页卡券列表
        public String accountVoucherSelect = ""; // 结算页卡券选择
        public String miscMemberIndex = ""; //个人中心首页
        public String getPhoneCode = ""; //获取短信验证码
        public String getPicCode = ""; //获取图片验证码
        public String verificationCodeGetMailCode = ""; //获取邮箱验证码
        public String login = ""; //登录
        public String register = ""; //注册
        public String thirdLogin = "";  // 第三方账户登入检查
        public String thirdLoginBind = "";  // 第三方登入并绑定
        public String thirdRegisterBind = "";   // 第三方注册并绑定
        public String thirdCheckUser = "";  // 第三方用户检查
        public String checkUser = ""; //检查用户是否存在
        public String verificationCodeCheckPhoneCode = ""; //校验短信验证码
        public String firstCategory = "";//一级分类列表
        public String childCategory = "";//二、三级分类列表
        public String feedbackaddFeedback = ""; //意见反馈
        public String feedbackFeedbackType = ""; //意见反馈类型
        public String addressAddUpdate = ""; //新增和更新收货地址
        public String addressIsDistribution = ""; //是否在配送范围内
        public String addressIsDistributionList = ""; //列表是否在配送范围内
        public String addressGetAddressList = ""; //收货地址列表页
        public String addressGpsHome = ""; //定位地址
        public String addressDistributionArea = ""; //是否在配送范围内
        public String addressDefaultAddress = ""; //是否在配送范围内
        public String addressDelete = ""; //删除地址接口
        public String setIndex = ""; //设置首页
        public String adminshopcart = "";//购物车
        public String cartget = "";//查询购物车
        public String cartinsert = "";//加入购物车
        public String cartupdate = "";//更新购物车
        public String allselect = "";//购物车全选接口
        public String passwordSubmitLoginPassword = "";//设置、修改登录密码
        public String passwordSubmitPayPassword = "";//设置、修改支付密码
        public String passwordCheckPayPassword = "";//验证支付密码
        public String setGetShare = "";// 关于页面
        public String voucherGetMyVoucherlist = "";//获取优惠券列表
        public String passwordResetLoginPassword = "";//重置密码
        public String passwordUserInfo = "";//检查用户信息
        public String passwordBindPhone = "";//绑定手机
        public String toolUpimg = "";//上传单张图片
        public String toolUpimgs = "";//上传多张图片
        public String cardGetCardDetail = "";//获取购物卡列表
        public String orderList = ""; // 订单列表
        public String orderDetail = ""; // 订单详情
        public String orderCancel = ""; //取消订单
        public String orderCancellationRequest = ""; // 请求退订页面信息展示
        public String orderCancellationApplywhole = ""; // 整单退订，出参返回退订详情实体
        public String orderCancellationApply = ""; // 退订单个商品，出参返回退订详情实体
        public String refundList = ""; // 退订单列表
        public String refundDetail = ""; // 退款详情
        public String refundDetailGood = ""; // 退款详情
        public String remainderList = ""; // 飞牛余额明细
        public String remainderDetail = ""; // 提现进度查询
        public String checkActivation = ""; // 检查账户是否激活
        public String merchandiseGetVoucherMerchandise = ""; // 优惠券适用商品列表
        public String addAppLog = ""; //埋点上报接口
        public String miscVersionUpdate = ""; //版本更新
        public String loginRegistMsg = ""; //登录注册文案信息
        public String loginOut = ""; //登出
        public String merchandiseSearchCampList = ""; //行销活动列表页
        public String merchandiseGetCampInfo = ""; //行销活动小计接口
        public String serviceCenterIndex = "";//服务中心
        public String cardCardRecharge = "";//卡券充值
        public String cardValidateCard = "";//卡券验证
        public String getCardHistory = "";//获取购物卡明细
        public String goodsoftenbuy = "";//常买清单
        public String categoryType = "";//API配置
    }
}
