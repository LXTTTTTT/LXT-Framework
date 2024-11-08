package com.lxt.framework.ui.base.fragment

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.lxt.framework.common.extension.saveAs
import com.lxt.framework.common.extension.saveAsUnChecked
import com.lxt.framework.common.utils.ApplicationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VB : ViewBinding>:Fragment() {
    private var TAG:String = javaClass.simpleName
    val APP : Application by lazy { ApplicationUtils.getApplication() }
    lateinit var my_context: Context;
    var layoutView:View? = null
    lateinit var viewBinding:VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logi("Fragment Created")
        my_context = requireActivity()
//        setHasOptionsMenu(true)  // 开启菜单项
        beforeSetLayout()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutView = getFragmentLayout(container)
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getView()?.let { initView(it,savedInstanceState) }  // 初始化控件事件
        CoroutineScope(Dispatchers.Main).launch{
            initData();  // 初始化数据
            withContext(Dispatchers.IO){
                initDataSuspend()
            }
        }
    }

    abstract fun beforeSetLayout()
    abstract fun setLayout():Any?
    abstract fun initView(view: View, savedInstanceState: Bundle?)
    abstract fun initData()
    abstract suspend fun initDataSuspend()

    fun getFragmentLayout(container: ViewGroup?):View?{
        val layout = setLayout()
        var view : View
        layout?.let {
            loge("手动绑定布局")
            if (it is Int) {
                view = layoutInflater.inflate(it,container,false)
            } else if (it is View) {
                view = it
            } else {
                throw IllegalArgumentException()
            }
            return view
        }
        try {
            val type = javaClass.genericSuperclass
            val vbClass: Class<VB> = type!!.saveAs<ParameterizedType>().actualTypeArguments[0].saveAs()
            val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
            viewBinding = method.invoke(this, layoutInflater)!!.saveAsUnChecked()
            layoutView = viewBinding.root
            return viewBinding.root
        } catch (e:Exception){
            return null
            e.printStackTrace()
        }
    }

    fun loge(log:String){
        Log.e(TAG, log )
    }
    fun logi(log:String){
        Log.i(TAG, log )
    }

    override fun onDestroy() {
        logi("Fragment Destroy")
        super.onDestroy()
    }
}