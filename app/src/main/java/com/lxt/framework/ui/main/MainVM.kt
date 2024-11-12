package com.lxt.framework.ui.main

import androidx.lifecycle.MutableLiveData
import com.lxt.framework.common.extension.*
import com.lxt.framework.data.model.common.Status
import com.lxt.framework.data.model.common.User
import com.lxt.framework.data.model.response.BaseResponse
import com.lxt.framework.data.model.response.Hotkey
import com.lxt.framework.data.proxy.ProxyFactory
import com.lxt.framework.data.service.MessageService
import com.lxt.framework.data.service.UserService
import com.lxt.framework.ui.base.viewmodel.BaseViewModel

// 全局使用 ViewModel
class MainVM : BaseViewModel<MutableList<Hotkey>>() {
    val text : MutableLiveData<String?> = MutableLiveData()
    val total : MutableLiveData<Int?> = MutableLiveData()
    private var loginService:MessageService
    private var userService:UserService
    init {
        loginService = ProxyFactory.createMessageService(false)
        userService = ProxyFactory.createUserService(false)
    }

    fun getHotkey(before: (() -> Unit)? = null, after: (() -> Unit)? = null){
        launchInScope {
            val data = requestFlow(
                before = {
                    before?.invoke()
                },
                after = {
                    showToast("请求完成",0)
                    after?.invoke()
                },
                requestCall = {
                    userService.getHotkey()
                },
                errorHandler = { code, error ->
                    showToast("请求失败: $error",0)
                }
            )
            data?.let {
                dataStatus.postValue(Status.Success(it))
                // 数据处理(存储)
                val user = User().apply {
                    name = it[0].name
                    hotkeyCount = it.size
                    userService.saveUser(this)
                }

            }
        }
    }

    fun combineData(){
        launchInScope {
            val data = requestFlowConcurrent4<MutableList<User>?, BaseResponse<MutableList<Hotkey>>,String?,Int>(
                request1 = {
                    userService.getLocalUsers()
                },
                request2 = {
                    userService.getHotkey()
                },
                parser = { l,x,t ->
                    var a = 0;var b = 0;
                    l?.let { a=it.size }
                    x?.let {
                        if(it.isSuccessful){
                            b=it.data.size
                        }
                    }
                    return@requestFlowConcurrent4 a+b
                }
            )
            data?.let { total.postValue(it) }
        }
    }

    fun test1(){
        launchInScope {
            val result = requestFlowConcurrent1(
                requests = listOf({userService.getHotkey()},{userService.getHotkey()}),
                parser = {it
                    var result1 = "";var result2 = 0;
                    it[0]?.let {
                        result1 = (it.data as MutableList<Hotkey>).toString()
                    }
                    it[1]?.let {
                        result2 = (it.data as MutableList<Hotkey>).size
                    }
                    return@requestFlowConcurrent1 "beans:$result2 / $result1"
                },
                errorHandler = {
                    it.printStackTrace()
                }
            )
            result?.let { text.postValue(it as String) }
        }
    }

    fun test2(){
        launchInScope {
            val result = requestFlowConcurrent2(
                requests = listOf({userService.getHotkey()},{userService.getHotkey()}),
                parser = {it
                    var result1 = "";var result2 = 0;
                    it[0]?.let {
                        result1 = it.data.toString()
                    }
                    it[1]?.let {
                        result2 = it.data.size
                    }
                    return@requestFlowConcurrent2 "beans:$result2 / $result1"
                },
                errorHandler = {
                    it.printStackTrace()
                }
            )
            result?.let { text.postValue(it as String) }
        }
    }

    fun test3(){
        launchInScope {
            val result = requestFlowConcurrent3(
                request1 = {userService.getHotkey()},
                request2 = {userService.getHotkey()},
                parser = {a,b,c->
                    var result1 = "";var result2 = 0;
                    a?.let { result1=(it.data as MutableList<Hotkey>).toString() }
                    b?.let { result2=(it.data as MutableList<Hotkey>).size }
                    return@requestFlowConcurrent3 "beans:$result2 / $result1"
                },
                errorHandler = {
                    it.printStackTrace()
                }
            )
            result?.let { text.postValue(it as String) }
        }
    }


    override fun release() {
        loge("release")
    }
}