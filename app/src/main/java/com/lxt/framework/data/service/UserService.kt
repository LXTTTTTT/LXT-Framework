package com.lxt.framework.data.service

import com.lxt.framework.data.model.common.User
import com.lxt.framework.data.model.response.BaseResponse
import com.lxt.framework.data.model.response.Hotkey

interface UserService {
    suspend fun getUsers():BaseResponse<MutableList<User>>
    suspend fun getHotkey():BaseResponse<MutableList<Hotkey>>
}