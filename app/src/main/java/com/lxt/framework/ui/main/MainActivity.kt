package com.lxt.framework.ui.main

import android.os.Bundle
import com.lxt.framework.common.utils.ApplicationUtils
import com.lxt.framework.data.model.common.Status
import com.lxt.framework.databinding.ActivityMainBinding
import com.lxt.framework.ui.base.activity.BaseMVVMActivity
import com.lxt.framework.ui.splash.SplashVM

class MainActivity : BaseMVVMActivity<ActivityMainBinding,MainVM>(false) {
    override fun setLayout(): Any? {return null}
    override fun beforeSetLayout() {}
    override suspend fun initDataSuspend() {}
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {
        super.initData()
        initViewModel()

        viewModel.getHotkey()
    }

    fun initViewModel(){
        viewModel.text.observe(this) {
            viewBinding.text.text = it
        }
        viewModel.dataStatus.observe(this){
            if(it is Status.Success){
                viewBinding.text.text = it.data?.size.toString()
            }else if(it is Status.Error){
                viewModel.showToast("请求失败",0)
            }else if(it is Status.Loading){
                viewModel.showToast("请求中",0)
            }
        }
    }

    override fun onDestroy() {
        ApplicationUtils.globalRelease()
        super.onDestroy()
    }
}