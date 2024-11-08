package com.lxt.framework.data.local.manager

import com.lxt.framework.data.local.database.MyDataBase
import com.lxt.framework.data.model.common.Message
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * @date   2023/4/11 16:57
 * @desc   视频缓存管理类
 */
object DatabaseManager {
    private val messageDao by lazy { MyDataBase.getInstance().messageDao() }
    fun insertMessage(message: Message){
        MainScope().launch {
            messageDao.insert(message)
        }
    }
    fun getMessages(): MutableList<Message>? {
        return messageDao.queryAll()
    }
}