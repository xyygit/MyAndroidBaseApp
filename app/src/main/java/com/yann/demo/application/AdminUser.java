package com.yann.demo.application;

import lib.core.http.ResponseCallback;

/**
 * 个人账户相关管理类
 * Created by yayun.xia on 2017/8/30.
 */

public class AdminUser {
    private AdminUser() {
    }

    public void getToken(ResponseCallback callback) {

    }

    private static class AdminUserHolder {
        private static final AdminUser adminUser = new AdminUser();
    }

    public static final AdminUser getInstance() {
        return AdminUserHolder.adminUser;
    }
}
