package com.lxt.framework.ui.main

import android.os.Bundle
import com.lxt.framework.common.utils.ApplicationUtils
import com.lxt.framework.databinding.ActivityMainBinding
import com.lxt.framework.ui.base.activity.BaseMVVMActivity
import com.lxt.framework.ui.splash.SplashVM

class MainActivity : BaseMVVMActivity<ActivityMainBinding,MainVM>() {
    override fun setLayout(): Any? {return null}
    override fun beforeSetLayout() {}
    override suspend fun initDataSuspend() {}
    private var splashVM: SplashVM? = null
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {
        super.initData()
        splashVM = ApplicationUtils.getGlobalViewModel(SplashVM::class.java)
        viewModel.text.observe(this) {
            viewBinding.text.text = it
        }
        splashVM?.text?.observe(this) {
            viewBinding.text2.text = it
        }
    }

    override fun onDestroy() {
        ApplicationUtils.globalRelease()
        super.onDestroy()
    }
}