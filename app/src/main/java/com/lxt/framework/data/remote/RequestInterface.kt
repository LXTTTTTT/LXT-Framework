package com.lxt.framework.data.remote

import com.lxt.framework.common.global.Constant
import com.lxt.framework.data.model.common.User
import com.lxt.framework.data.model.response.BaseResponse
import com.lxt.framework.data.model.response.Hotkey
import retrofit2.http.*

interface RequestInterface {
    @GET("/tree/json")
    suspend fun testWan(): String

    @GET("")
    suspend fun getUsers(): BaseResponse<MutableList<User>>

    @GET(Constant.PATH_HOTKEY)
    suspend fun getHotkey(): BaseResponse<MutableList<Hotkey>>

    @POST("")
    suspend fun pwdLogin(@Body hashMap: HashMap<String, String>): BaseResponse<User>

    @POST("")
    suspend fun sendPhoneCode(@Query("mode") mode: String, @Query("to") phone: String): BaseResponse<String>
}