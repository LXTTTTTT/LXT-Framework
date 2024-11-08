package com.lxt.framework.ui.base.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.viewbinding.ViewBinding
import com.lxt.framework.common.utils.ApplicationUtils
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseMVVMFragment<VB : ViewBinding, VM : ViewModel>(global_model:Boolean = false) : BaseFragment<VB>() {

    val global = global_model  // 是否全局使用的 ViewModel
    lateinit var viewModel: VM

    override fun initData() {
        // 反射创建 ViewModel 对象
        if(global){loge("使用全局ViewModel")}else{loge("使用临时ViewModel")}
        viewModel = if(global){
            // 使用全局 ViewModel
            val superClass = this::class.supertypes
            loge("构造函数：${superClass[0]}")
            val kArgument = superClass[0].arguments
            loge("ViewModel对象：${kArgument[1]}")
            val classVM = kArgument[1].type?.classifier
            val globalViewModelStore : ViewModelStore =
                if(ApplicationUtils.globalViewModelStore==null){
                    ApplicationUtils.globalViewModelStore = viewModelStore
                    viewModelStore
                }else{
                    loge("已有 globalViewModelStore ")
                    ApplicationUtils.globalViewModelStore!!
                }
            ViewModelLazy(
                classVM as KClass<VM>,
                { globalViewModelStore },
                { defaultViewModelProviderFactory }
            ).value
        }else{
            val argument = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
            ViewModelProvider(this).get(argument[1] as Class<VM>)
        }
    }

}