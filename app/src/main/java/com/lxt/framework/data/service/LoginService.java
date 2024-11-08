package com.lxt.framework.data.service;
import com.lxt.framework.data.model.common.User;

public interface LoginService {
    User login(String account, String pwd, String appCode);
    void directAuth(String authUrl);

    // test api
    String testWan();
}
