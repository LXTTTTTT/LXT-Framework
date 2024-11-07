package com.lxt.framework.ui.main

import androidx.lifecycle.MutableLiveData
import com.lxt.framework.ui.base.viewmodel.BaseViewModel

// 全局使用 ViewModel
class MainVM : BaseViewModel<String>() {
    private val TAG = "MainVM"
    val text : MutableLiveData<String?> = MutableLiveData()
    init {
        text.postValue("mainVM")
    }
    override fun release() {}
}