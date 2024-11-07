package com.lxt.framework

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.lxt.framework.common.taskDispatch.dispatcher.TaskDispatcher
import com.lxt.framework.common.taskDispatch.task.InitAppUtilTask
import com.lxt.framework.common.taskDispatch.task.InitCatchException
import com.lxt.framework.common.taskDispatch.task.InitDatabaseTask
import com.lxt.framework.common.taskDispatch.task.InitMmkvTask
import com.lxt.framework.common.taskDispatch.task.InitSystemInfoTask
import com.lxt.framework.common.utils.ActivityManagementUtils

class MainApplication:Application() {

// 常量 ------------------------------------------
    val TAG = "MainApplication"
// 变量 ------------------------------------------
    var aliveActivityCount = 0  // 已开启页面统计

// 单例（这个项目用不上） ------------------------------------------
    companion object{
        private lateinit var mainApplication: MainApplication
        fun getInstance(): MainApplication {
            return mainApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        mainApplication = this;

        // 执行程序初始化
        TaskDispatcher.init(this)  // 初始化调度器
        TaskDispatcher.createInstance().addTask(InitAppUtilTask(this))
            .addTask(InitMmkvTask())
            .addTask(InitSystemInfoTask())
            .addTask(InitDatabaseTask())
            .addTask(InitCatchException())
            .start()
        TaskDispatcher.createInstance().await()

        // Activity 生命周期注册
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks{
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                Log.d(TAG, "onActivityCreated: 打开页面 ${p0.localClassName}")
                ActivityManagementUtils.getInstance().push(p0)
            }
            override fun onActivityStarted(p0: Activity) {
                aliveActivityCount++;if (aliveActivityCount == 1) {
                    Log.e(TAG, "切换到前台")
                }
            }
            override fun onActivityResumed(p0: Activity) {}
            override fun onActivityPaused(p0: Activity) {}
            override fun onActivityStopped(p0: Activity) {
                aliveActivityCount--;if (aliveActivityCount == 0) {
                    Log.e(TAG, "切换到后台")
                }
            }
            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
            override fun onActivityDestroyed(p0: Activity) {
                Log.d(TAG, "onActivityCreated: 关闭页面 ${p0.localClassName}")
                ActivityManagementUtils.getInstance().pop(p0)
            }
        })

    }

    private val appViewModelStore: ViewModelStore by lazy { ViewModelStore() }
    fun getViewModelStore(): ViewModelStore {
        return appViewModelStore
    }
}