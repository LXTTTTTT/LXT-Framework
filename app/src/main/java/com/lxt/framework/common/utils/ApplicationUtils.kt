package com.lxt.framework.common.utils

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

// 应用程序工具
object ApplicationUtils {
    private lateinit var app: Application
    private var isDebug = false

    fun init(application: Application, isDebug: Boolean) {
        app = application
        ApplicationUtils.isDebug = isDebug
    }
    // 当前是否为debug环境
    fun isDebug() = isDebug

    // 获取全局应用
    fun getApplication() = app
    // 全局使用 ViewModelStore
    var globalViewModelStore : ViewModelStore? = null  // 一般是 MainApplication 的
        set(store){
            if(globalViewModelStore==null){ field =store }else{ Log.e("globalViewModelStore", "已存在")}
        }

    fun <T : ViewModel> getGlobalViewModel(viewModelClass: Class<T>) : T?{
        var viewModel: T? = null
        globalViewModelStore?.let {
            viewModel =
                ViewModelLazy(
                    viewModelClass.kotlin,
                    { it },
                    { ViewModelProvider.AndroidViewModelFactory(app) }
                ).value
        }
        return viewModel
    }

    // 全局释放资源，程序结束时调用
    fun globalRelease(){
        // 清除所有全局使用的 viewModel
        globalViewModelStore?.clear()
    }
}