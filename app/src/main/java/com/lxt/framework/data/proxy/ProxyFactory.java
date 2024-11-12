package com.lxt.framework.data.proxy;

import com.lxt.framework.data.service.LoginService;
import com.lxt.framework.data.service.MessageService;
import com.lxt.framework.data.service.UserService;

public class ProxyFactory {
    public static MessageService createMessageService(boolean isMock) {
        return isMock ? new MockDataProxy() : new DataServiceProxy();
    }
    public static LoginService createLoginService(boolean isMock) {
        return isMock ? new MockDataProxy() : new DataServiceProxy();
    }
    public static UserService createUserService(boolean isMock) {
        return isMock ? new MockDataProxy() : new DataServiceProxy();
    }
}
