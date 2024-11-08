package com.lxt.framework.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lxt.framework.common.extension.requestFlow
import com.lxt.framework.data.model.common.Status
import com.lxt.framework.data.model.response.Hotkey
import com.lxt.framework.data.proxy.ProxyFactory
import com.lxt.framework.data.service.MessageService2
import com.lxt.framework.data.service.UserService
import com.lxt.framework.ui.base.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

// 全局使用 ViewModel
class MainVM : BaseViewModel<MutableList<Hotkey>>() {
    val text : MutableLiveData<String?> = MutableLiveData()
    private var loginService:MessageService2
    private var userService:UserService
    init {
        loginService = ProxyFactory.createMessageService(false)
        userService = ProxyFactory.createUserService(false)
    }

    fun getHotkey(){
        viewModelScope.launch {
            val data = requestFlow(
                before = {

                },
                after = {

                },
                requestCall = {
                    return@requestFlow userService.getHotkey()
                },
                errorHandler = { code, error ->

                }
            )
            data?.let { dataStatus.postValue(Status.Success(it)) }
        }
    }

    override fun release() {
        loge("release")
    }
}