package com.lxt.framework.data.local.manager

import com.lxt.framework.data.local.database.MyDataBase
import com.lxt.framework.data.model.common.Message
import com.lxt.framework.data.model.common.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * @date   2023/4/11 16:57
 * @desc   视频缓存管理类
 */
object DatabaseManager {
    private val messageDao by lazy { MyDataBase.getInstance().messageDao() }
    private val userDao by lazy { MyDataBase.getInstance().userDao() }
    fun insertMessage(message: Message){
        MainScope().launch {
            messageDao.insert(message)
        }
    }
    fun getMessages(): MutableList<Message>? {
        return messageDao.queryAll()
    }
    fun insertUser(user: User){
        MainScope().launch(Dispatchers.IO) {
            userDao.insert(user)
        }
    }
    fun insertUsers(users: MutableList<User>){
        MainScope().launch(Dispatchers.IO) {
            userDao.insertAll(users)
        }
    }
    fun getUsers(): MutableList<User>? {
        return userDao.queryAll()
    }
}