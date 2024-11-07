package com.lxt.framework.ui.main

import android.os.Bundle
import com.lxt.framework.common.utils.ApplicationUtils
import com.lxt.framework.databinding.ActivityMainBinding
import com.lxt.framework.ui.base.activity.BaseMVVMActivity
import com.lxt.framework.ui.splash.SplashVM

class MainActivity : BaseMVVMActivity<ActivityMainBinding,MainVM>(true) {
    override fun setLayout(): Any? {return null}
    override fun beforeSetLayout() {}
    override suspend fun initDataSuspend() {}
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {
        super.initData()
        viewModel.text.observe(this) {
            viewBinding.text.text = it
        }
    }

    override fun onDestroy() {
        ApplicationUtils.globalRelease()
        super.onDestroy()
    }
}