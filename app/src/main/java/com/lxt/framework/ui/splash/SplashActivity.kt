package com.lxt.framework.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.lxt.framework.common.extension.countDownCoroutines
import com.lxt.framework.common.extension.globalViewModels
import com.lxt.framework.common.utils.ApplicationUtils
import com.lxt.framework.databinding.ActivitySplashBinding
import com.lxt.framework.ui.base.activity.BaseActivity
import com.lxt.framework.ui.base.activity.BaseMVVMActivity
import com.lxt.framework.ui.main.MainActivity
import com.lxt.framework.ui.main.MainVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun setLayout(): Any? { return null }
    override fun beforeSetLayout() {}
    override suspend fun initDataSuspend() {}
    var countDownJob:Job?=null
    override fun initView(savedInstanceState: Bundle?) {
        viewBinding.countDown.setOnClickListener {
            countDownJob?.let { it.cancel() }
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        countDownJob=countDownCoroutines(
            Dispatchers.Main,
            2,
            lifecycleScope,
            onTick = {
                viewBinding.countDown.text = "${it.plus(1).toString()} S"
            },
            onFinish = {
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        )
    }

    override fun initData() {}
}