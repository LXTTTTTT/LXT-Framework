package com.lxt.framework.data.proxy

import com.lxt.framework.data.local.manager.DatabaseManager
import com.lxt.framework.data.model.common.Message
import com.lxt.framework.data.model.common.User
import com.lxt.framework.data.model.response.BaseResponse
import com.lxt.framework.data.model.response.Hotkey
import com.lxt.framework.data.service.LoginService
import com.lxt.framework.data.remote.RequestInterface
import com.lxt.framework.data.remote.RetrofitClient
import com.lxt.framework.data.service.MessageService
import com.lxt.framework.data.service.UserService

class DataServiceProxy : MessageService, LoginService, UserService {
    val api : RequestInterface
    val db : DatabaseManager

    init {
        api = RetrofitClient.createAPI(RequestInterface::class.java)
        db = DatabaseManager
    }
    override suspend fun getMessages(): MutableList<Message>? {
        return db.getMessages()
    }

    override fun login(account: String?, pwd: String?, appCode: String?): User {
        return User()
    }


    override fun directAuth(authUrl: String?) {

    }

    override fun testWan(): String {
        return ""
    }

    override suspend fun getUsers(): BaseResponse<MutableList<User>> {
        return api.getUsers()
    }

    override suspend fun getHotkey(): BaseResponse<MutableList<Hotkey>> {
        return api.getHotkey()
    }

    override suspend fun getLocalUsers(): MutableList<User>? {
        return db.getUsers()
    }

    override fun saveUser(user: User) {
        db.insertUser(user)
    }

    override fun saveUsers(users: MutableList<User>) {
        db.insertUsers(users)
    }

}