package com.lxt.framework.ui.base.activity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.lxt.framework.common.extension.saveAs
import com.lxt.framework.common.extension.saveAsUnChecked
import com.lxt.framework.common.utils.ApplicationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.ParameterizedType
import java.util.*
import kotlin.coroutines.CoroutineContext

// 基类
abstract class BaseActivity<VB:ViewBinding> : AppCompatActivity(){

    val TAG : String = this::class.java.simpleName
    val APP : Application by lazy { ApplicationUtils.getApplication() }
    val mainDispatcher : CoroutineContext by lazy { Dispatchers.Main }
    val ioDispatcher : CoroutineContext by lazy { Dispatchers.IO }
    val cpuDispatcher : CoroutineContext by lazy { Dispatchers.Default }
    lateinit var activity_window : Window
    lateinit var my_context : Context
    lateinit var viewBinding:VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity_window = window
        my_context = this
        beforeSetLayout()
        supportActionBar?.let {it.hide()}  // 隐藏标题栏
        setActivityLayout()  // 绑定布局
        if(lockPortrait()){setOrientationPortrait()}  // 锁定垂直布局
        initView(savedInstanceState);  // 初始化页面
        CoroutineScope(Dispatchers.Main).launch{
            initData();  // 初始化数据
            withContext(Dispatchers.IO){
                initDataSuspend()
            }
        }
    }

    abstract fun setLayout(): Any?
    abstract fun beforeSetLayout()
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initData()
    abstract suspend fun initDataSuspend()
    open fun lockPortrait():Boolean=true
    // 绑定布局
    open fun setActivityLayout(){
        setLayout()?.let {
            loge("手动绑定布局")
            if (setLayout() is Int) {
                setContentView((setLayout() as Int)) // 手动绑定 R.layout.id
            } else if (setLayout() is View) {
                setContentView(setLayout() as View) // 手动绑定 ViewBinding
            } else {
                throw IllegalArgumentException()
            }
            return
        }
        try {
            // 通过反射获取到对应的 Binding 对象并拿到他的 Binding.inflate(layoutInflater) 方法执行
            val type = javaClass.genericSuperclass  // getClass().getGenericSuperclass();
            // 拿到 ViewBinding 类对象
            val vbClass: Class<VB> = type!!.saveAs<ParameterizedType>().actualTypeArguments[0].saveAs()  // genericSuperclass 强转为 ParameterizedType
            // 拿到 ViewBinding 类的inflate方法
            val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
            // 执行 ViewBinding.inflate(getLayoutInflater()); 前面的变量已声明类型VB所以不需要再指定<VB>
            viewBinding = method.invoke(this, layoutInflater)!!.saveAsUnChecked()
            setContentView(viewBinding.root)  // 设置布局
        } catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun launchInCoroutineScope(dispatcher: CoroutineContext=ioDispatcher,custom: Boolean = false, run:suspend ()->Unit):Job?{
        return if(custom){
            // 自行管理生命周期
            CoroutineScope(dispatcher).launch {
                run.invoke()
            }
        }else{
            lifecycleScope.launchWhenCreated {
                withContext(dispatcher){
                    run.invoke()
                }
            }
            return null;
        }
    }

    fun trySomething(run:()->Unit){
        try {
            run.invoke()
        } catch (e:Exception){
            e.printStackTrace()
        }
    }

    // 打印 loge
    fun loge(log: String?) {
        Log.e(TAG, log!!)
    }

    fun logi(log: String?) {
        Log.i(TAG, log!!)
    }


    // 锁定页面垂直
    private fun setOrientationPortrait() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }


    // 页面返回响应
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loge("页面返回，请求码是：$requestCode 响应码是：$resultCode")
    }


    // 权限申请响应
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        loge("请求权限码是：" + requestCode + " / 权限列表：" + Arrays.toString(permissions) + " / 结果列表：" + Arrays.toString(grantResults))
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}