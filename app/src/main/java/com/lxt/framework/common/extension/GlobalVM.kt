package com.lxt.framework.common.extension

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

// 创建全局 ViewModel ，第二种方式
val globalViewModelStore = ViewModelStore()

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.globalViewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return ViewModelLazy(VM::class, { globalViewModelStore }, factoryPromise)
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.globalViewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return ViewModelLazy(VM::class, { globalViewModelStore }, factoryPromise)
}

@MainThread
inline fun <reified VM : ViewModel> Application.globalViewModels(): Lazy<VM> {
    val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
    return ViewModelLazy(VM::class, { globalViewModelStore }, { factory })
}

