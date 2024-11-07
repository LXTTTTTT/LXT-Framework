package com.lxt.framework.common.taskDispatch.task

import android.app.Application
import android.util.Log
import com.lxt.framework.BuildConfig
import com.lxt.framework.MainApplication
import com.lxt.framework.common.utils.ApplicationUtils
import com.lxt.framework.common.utils.DispatcherExecutor
import com.lxt.framework.common.utils.SystemInfoUtils
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import java.util.concurrent.ExecutorService

// 任务
val TAG = "MyTask"
// 初始化全局APP工具
class InitAppUtilTask(val application: Application) : Task() {

    // 异步线程执行的Task在被调用await的时候等待
    override fun needWait(): Boolean {
        return true
    }

    override fun run() {
        ApplicationUtils.init(application, BuildConfig.DEBUG)
        ApplicationUtils.globalViewModelStore = MainApplication.getInstance().getViewModelStore()
        Log.e(TAG, "初始化APP工具" )
    }
}


// 初始化 MMKV
class InitMmkvTask() : Task() {

    // 异步线程执行的Task在被调用await的时候等待
    override fun needWait(): Boolean {
        return true
    }

    // 依赖某些任务，在某些任务完成后才能执行（初始化 ApplicationUtil 之后）
    override fun dependsOn(): MutableList<Class<out Task>> {
        val tasks = mutableListOf<Class<out Task?>>()
        tasks.add(InitAppUtilTask::class.java)  // MMKV初始化需要用到主程序
        return tasks
    }

    // 指定需要使用的线程池
    override fun runOn(): ExecutorService? {
        return DispatcherExecutor.IOExecutor
    }

    // 执行任务
    override fun run() {
        val rootDir: String = MMKV.initialize(ApplicationUtils.getApplication())
        MMKV.setLogLevel(
            if (BuildConfig.DEBUG) {
                MMKVLogLevel.LevelDebug
            } else {
                MMKVLogLevel.LevelError
            }
        )
        Log.e(TAG, "MMKV 初始化根目录: $rootDir" )
    }
}

// 初始化AppManager
class InitSystemInfoTask() : Task() {
    // 异步线程执行的Task在被调用await的时候等待
    override fun needWait(): Boolean {
        return true
    }

    //依赖某些任务，在某些任务完成后才能执行
    override fun dependsOn(): MutableList<Class<out Task>> {
        val tasks = mutableListOf<Class<out Task?>>()
        tasks.add(InitAppUtilTask::class.java)  // 需要用到主程序
        return tasks
    }

    override fun run() {
        SystemInfoUtils.init(ApplicationUtils.getApplication())
        Log.e(TAG, "初始化系统信息" )
    }
}

// 初始化 数据库
class InitDatabaseTask() : Task() {
    // 异步线程执行的Task在被调用await的时候等待
    override fun needWait(): Boolean {
        return true
    }

    //依赖某些任务，在某些任务完成后才能执行
    override fun dependsOn(): MutableList<Class<out Task>> {
        val tasks = mutableListOf<Class<out Task?>>()
        tasks.add(InitAppUtilTask::class.java)  // 需要用到主程序
        return tasks
    }

    override fun run() {
//        MyDataBase.getInstance()
        Log.e(TAG, "初始化数据库" )
    }
}


// 捕获异常注册
class InitCatchException() : Task() {
    // 异步线程执行的Task在被调用await的时候等待
    override fun needWait(): Boolean {
        return true
    }

    //依赖某些任务，在某些任务完成后才能执行
    override fun dependsOn(): MutableList<Class<out Task>> {
        val tasks = mutableListOf<Class<out Task?>>()
        tasks.add(InitAppUtilTask::class.java)
        return tasks
    }

    override fun run() {
//        CatchExceptionUtils.getInstance().init(ApplicationUtils.getApplication())
        Log.e(TAG, "初始化捕获异常" )
    }
}


// 快捷创建其他任务 -------------------------------

// 初始化A
class InitTaskA() : Task() {
    override fun run() {
        //...
    }
}

// 初始化B
class InitTaskB() : Task() {
    override fun run() {
        //...
    }
}