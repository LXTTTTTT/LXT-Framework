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


    override fun release() {
        loge("release")
    }
}