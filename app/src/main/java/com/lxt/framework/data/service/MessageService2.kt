package com.lxt.framework.data.service

import com.lxt.framework.data.model.common.Message

interface MessageService2 {
    suspend fun getMessages():MutableList<Message>?
}