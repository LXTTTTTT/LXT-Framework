package com.lxt.framework.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.lxt.framework.common.global.Path
import com.lxt.framework.common.global.Permissions
import com.lxt.framework.common.utils.ApplicationUtils
import com.lxt.framework.data.model.common.Status
import com.lxt.framework.databinding.ActivityMainBinding
import com.lxt.framework.ui.base.activity.BaseMVVMActivity
import com.tbruyelle.rxpermissions3.RxPermissions
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Arrays

class MainActivity : BaseMVVMActivity<ActivityMainBinding,MainVM>(false) {

    lateinit var rxPermissions : RxPermissions
    override fun setLayout(): Any? {return null}
    override fun beforeSetLayout() {}
    override suspend fun initDataSuspend() {}
    override fun initView(savedInstanceState: Bundle?) {
        viewBinding.test1.setOnClickListener {
            viewModel.combineData()
        }
    }

    override fun initData() {
        super.initData()
        check_permission()

        initViewModel()
        viewModel.getHotkey()
    }

    fun initViewModel(){
        viewModel.text.observe(this) {
            viewBinding.text.text = it
        }
        viewModel.total.observe(this) {
            viewBinding.text2.text = it.toString()
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

    fun check_permission(){
        rxPermissions = RxPermissions(this)
        rxPermissions.request(*Permissions.PERMISSION_LIST)
            .observeOn(Schedulers.io())
            .subscribe({
                loge("权限申请结果$it")
            })
        check_file_permission()
    }

    fun check_file_permission(){
        val haveFilePermission = Permissions.checkFilePermission()
        if(haveFilePermission){
            check_install_permission()
        }else{
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            startActivityForResult(intent, Permissions.PERMISSION_CODE_FILE)
        }
    }
    fun check_install_permission(){
        val haveInstallPermission = Permissions.checkInstallPermission(this)
        if(haveInstallPermission){
            Path.init()  // 初始化文件路径
        }else{
            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            intent.data = Uri.parse("package:$packageName")
            startActivityForResult(intent, Permissions.PERMISSION_CODE_INSTALL)
        }
    }

// double click -----------------------------------------------------
    private var lastClickTime: Long = 0L
    override fun onBackPressed() {
        super.onBackPressed()
        if (System.currentTimeMillis() - lastClickTime > 2000) {
            viewModel.showToast("再按一次返回桌面",0)
            lastClickTime = System.currentTimeMillis()
        } else {
            moveTaskToBack(true)
        }
    }

    override fun onDestroy() {
        ApplicationUtils.globalRelease()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 文件页面返回
        if(requestCode==Permissions.PERMISSION_CODE_FILE){
            val haveFilePermission = Permissions.checkFilePermission()
            if(haveFilePermission){
                loge("授予文件权限成功")
                check_install_permission()
            }else{
                loge("授予文件权限失败")
                viewModel.showToast("没有文件权限，APP无法正常使用!",0)
                finish()
            }
        }
        // 安装页面返回
        else if(requestCode==Permissions.PERMISSION_CODE_INSTALL){
            if(resultCode== RESULT_OK){
                loge("授予安装权限成功")
            }else{
                loge("授予安装权限失败")
                viewModel.showToast("没有安装权限，APP无法正常使用!",0)
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}