package com.lxt.framework.ui.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lxt.framework.data.model.common.Status
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext


abstract class BaseViewModel<D> : ViewModel() {
    val mainDispatcher : CoroutineContext by lazy { Dispatchers.Main }
    val ioDispatcher : CoroutineContext by lazy { Dispatchers.IO }
    val cpuDispatcher : CoroutineContext by lazy { Dispatchers.Default }
    val dataStatus : MutableLiveData<Status<D>> = MutableLiveData()  // 数据状态


    fun <T> launchUIWithResult(
        responseBlock: suspend () -> T?,  // 挂起函数拿到数据：无输入 → 输出T
        successBlock: (T?) -> Unit  // 处理函数
    ) {
        // 在主线程调用
        viewModelScope.launch(Dispatchers.Main) {
            val result = safeApiCallWithResult(responseBlock)  // 子线程获取数据
            result?.let { successBlock(it) }  // 主线程处理结果
        }
    }

    suspend fun <T> safeApiCallWithResult(
        responseBlock: suspend () -> T?
    ): T? {
        try {
            // 子线程执行请求函数获取数据 10秒超时
            val response = withContext(Dispatchers.IO) {
                withTimeout(10 * 1000) {
                    responseBlock()
                }
            } ?: return null  // 为空返回 null

            return response  // 返回数据
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // 倒计时任务
    fun countDownCoroutines(
        total: Int,
        scope: CoroutineScope,
        onTick: (Int) -> Unit,
        onStart: (() -> Unit)? = null,
        onFinish: (() -> Unit)? = null,
    ): Job {
        return flow {
            for (i in total downTo 0) {
                emit(i)
                delay(1000)
            }
        }
            .flowOn(Dispatchers.Main)
            .onStart { onStart?.invoke() }
            .onCompletion { onFinish?.invoke() }
            .onEach { onTick.invoke(it) }  // 每次倒计时时执行
            .launchIn(scope)
    }

    // 如果这个viewmodel使用了强引用来持有某些对象则需要手动释放资源
    abstract fun release()
    override fun onCleared() {
        release()
        super.onCleared()
    }
}