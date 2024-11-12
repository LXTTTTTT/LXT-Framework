package com.lxt.framework.common.extension

import android.util.Log
import com.lxt.framework.data.error.*
import com.lxt.framework.data.model.response.BaseResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

val TAG = "FlowExtension"
// 倒计时任务
fun countDownCoroutines(
    dispatcher: CoroutineDispatcher,
    total: Int,
    scope: CoroutineScope,
    onTick: (Int) -> Unit,
    onStart: (() -> Unit)? = null,
    onFinish: (() -> Unit)? = null
): Job {
    return flow {
        for (i in total downTo 0) {
            emit(i)
            delay(1000)
        }
    }
        .flowOn(dispatcher)
        .onStart { onStart?.invoke() }
        .onCompletion { onFinish?.invoke() }
        .onEach { onTick.invoke(it) }
        .launchIn(scope)
}

// 无限循环任务
fun infiniteLoop(scope: CoroutineScope, dispatcher: CoroutineDispatcher=Dispatchers.IO, intervalMillis: Long, action: () -> Unit): Job {
    return scope.launch(dispatcher) {
        while (true) {
            action()
            delay(intervalMillis) // 指定间隔
        }
    }
}

/**
 * 执行请求
 * @param requestCall 请求方法
 * @return 请求结果
 */
suspend fun <T> requestFlow(
    before: (() -> Unit)? = null,  // 前置准备
    after: (() -> Unit)? = null,  // 后置工作
    requestCall: suspend () -> BaseResponse<T>?,
    errorHandler: ((Int?, String?) -> Unit)? = null
): T? {
    var data: T? = null
    val flow = requestFlowResponse1(before, after, requestCall, errorHandler)
    flow.collect {
        data = it?.data
    }
    return data
}


// 多任务
suspend fun requestFlowConcurrent1(
    requests: List<suspend () -> BaseResponse<out Any>?>,  // 任务集
    requestBefore: (() -> Unit)? = null,
    requestAfter: (() -> Unit)? = null,
    requestErrorHandler: ((Int?, String?) -> Unit)? = null,
    before: (() -> Unit)? = null,
    after: (() -> Unit)? = null,
    errorHandler:((Throwable)->Unit)? = null,
    parser:(Array<BaseResponse<out Any>?>) -> Any?
) : Any? {
    var result:Any? = null
    val flows = requests.map { request ->
        requestFlowResponse2(requestBefore, requestAfter, request, requestErrorHandler)
    }
    combine(*flows.toTypedArray()){ results ->
        Log.e(TAG, "执行数据处理1")
        for ((index, baseResponse) in results.withIndex()) {
            Log.i(TAG, "$index : $baseResponse" )
        }
        parser.invoke(results)
    }
        .flowOn(Dispatchers.IO)
        .onStart {
            Log.e(TAG, "任务启动1")
            before?.invoke()
        }
        .onCompletion {
            Log.e(TAG, "任务完成1")
            after?.invoke()
//            if(it==null){
//                Log.e(TAG, "任务完成1")
//                after?.invoke()
//            }else{
//                Log.e(TAG, "任务异常1")
//            }
        }
        .flowOn(Dispatchers.Main)
        .catch {
            Log.e(TAG, "任务异常1")
            errorHandler?.invoke(it)
        }
        .collect {
            Log.e(TAG, "输出结果1: ${it.toString()}", )
            result = it
        }
    return result
}

// 同参数多任务
suspend fun <T> requestFlowConcurrent2(
    requests: List<suspend () -> BaseResponse<T>?>,
    requestBefore: (() -> Unit)? = null,
    requestAfter: (() -> Unit)? = null,
    requestErrorHandler: ((Int?, String?) -> Unit)? = null,
    before: (() -> Unit)? = null,
    after: (() -> Unit)? = null,
    errorHandler:((Throwable)->Unit)? = null,
    parser:(Array<BaseResponse<T>?>) -> Any?
) : Any? {
    var result:Any? = null
    val flows = requests.map { request ->
        requestFlowResponse1(requestBefore, requestAfter, request, requestErrorHandler)
    }
    combine(flows){ results ->
        Log.e(TAG, "执行数据处理2")
        for ((index, baseResponse) in results.withIndex()) {
            Log.i(TAG, "$index : $baseResponse" )
        }
        parser.invoke(results)
    }
        .flowOn(Dispatchers.IO)
        .onStart {
            Log.e(TAG, "任务启动2")
            before?.invoke()
        }
        .onCompletion {
            Log.e(TAG, "任务完成2")
            after?.invoke()
//            if(it==null){
//                Log.e(TAG, "任务完成2")
//                after?.invoke()
//            }else{
//                Log.e(TAG, "任务异常2")
//            }
        }
        .flowOn(Dispatchers.Main)
        .catch {
            Log.e(TAG, "任务异常2")
            errorHandler?.invoke(it)
        }
        .collect {
            Log.e(TAG, "输出结果2: ${it.toString()}", )
            result = it
        }
    return result
}


// 简单请求多任务
suspend fun <T> requestFlowConcurrent3(
    request1: suspend () -> BaseResponse<out Any>?,
    request2: (suspend () -> BaseResponse<out Any>?)?=null,
    request3: (suspend () -> BaseResponse<out Any>?)?=null,
    requestBefore: (() -> Unit)? = null,
    requestAfter: (() -> Unit)? = null,
    requestErrorHandler: ((Int?, String?) -> Unit)? = null,
    before: (() -> Unit)? = null,
    after: (() -> Unit)? = null,
    errorHandler:((Throwable)->Unit)? = null,
    parser: (BaseResponse<out Any>?, BaseResponse<out Any>?, BaseResponse<out Any>?)->T?
) : T? {
    var result:T? = null
    val flow1 = requestFlowResponse2(requestBefore, requestAfter, request1, requestErrorHandler)
    val flow2 = if(request2!=null){
        requestFlowResponse2(requestBefore, requestAfter, request2, requestErrorHandler)
    }else{flow { emit(null) }}
    val flow3 = if(request3!=null){
        requestFlowResponse2(requestBefore, requestAfter, request3, requestErrorHandler)
    }else{flow { emit(null) }}
    combine(flow1,flow2,flow3){ a,b,c ->
        Log.i(TAG, "执行数据处理3\na:$a \nb:$b \nc:$c" )
        parser.invoke(a,b,c)
    }
        .flowOn(Dispatchers.IO)
        .onStart {
            Log.e(TAG, "任务启动3")
            before?.invoke()
        }
        .onCompletion {
            Log.e(TAG, "任务完成3")
            after?.invoke()
//            if(it==null){
//                Log.e(TAG, "任务完成3")
//                after?.invoke()
//            }else{
//                Log.e(TAG, "任务异常3")
//            }
        }
        .flowOn(Dispatchers.Main)
        .catch {
            Log.e(TAG, "任务异常3")
            errorHandler?.invoke(it)
        }
        .collect {
            Log.e(TAG, "输出结果3: ${it.toString()}", )
            result = it
        }
    return result
}

// 简单多任务
suspend fun <L:Any?, X:Any?, T:Any?, R> requestFlowConcurrent4(
    request1: suspend () -> L,
    request2: (suspend () -> X?)?=null,
    request3: (suspend () -> T?)?=null,
    requestBefore: (() -> Unit)? = null,
    requestAfter: (() -> Unit)? = null,
    requestErrorHandler: ((Int?, String?) -> Unit)? = null,
    before: (() -> Unit)? = null,
    after: (() -> Unit)? = null,
    errorHandler:((Throwable)->Unit)? = null,
    parser: (L, X?, T?)->R?
):R? {
    var result:R? = null
    val flow1 = requestFlowResponse3(requestBefore, requestAfter, request1, requestErrorHandler)
    val flow2 = if(request2!=null){
        requestFlowResponse3(requestBefore, requestAfter, request2, requestErrorHandler)
    }else{flow { emit(null) }}
    val flow3 = if(request3!=null){
        requestFlowResponse3(requestBefore, requestAfter, request3, requestErrorHandler)
    }else{flow { emit(null) }}
    combine(flow1,flow2,flow3){ l,x,t ->
        Log.i(TAG, "执行数据处理4\nl:$l \nx:$x \nt:$t" )
        parser.invoke(l,x,t)
    }
        .flowOn(Dispatchers.IO)
        .onStart {
            Log.e(TAG, "任务启动4")
            before?.invoke()
        }
        .onCompletion {
            Log.e(TAG, "任务完成4")
            after?.invoke()
//            if(it==null){
//                Log.e(TAG, "任务完成4")
//                after?.invoke()
//            }else{
//                Log.e(TAG, "任务异常4")
//            }
        }
        .flowOn(Dispatchers.Main)
        .catch {
            Log.e(TAG, "任务异常4")
            errorHandler?.invoke(it)
        }
        .collect {
            Log.e(TAG, "输出结果4: ${it.toString()}", )
            result = it
        }
    return result
}

/**
 * flow请求
 * @param request 请求方法
 * @return Flow<BaseResponse<T>?>
 */
suspend fun <T> requestFlowResponse1(
    before: (() -> Unit)? = null,  // 前置准备
    after: (() -> Unit)? = null,  // 后置工作
    request: suspend () -> BaseResponse<T>?,  // 请求
    errorHandler: ((Int?, String?) -> Unit)? = null,  // 错误处理
    timeout: Long = 10*1000
): Flow<BaseResponse<T>?> {
    val flow = flow {
        // 设置10秒超时
        val response = withTimeout(timeout) {
            request()
        }
        if(response!=null){
            if (!response.isSuccessful()) {
                throw ApiException(response.errorCode, response.errorMsg)
            }
        }else{
            throw OtherException(6666,"无法获取数据")
        }
        emit(response)
    }
        .flowOn(Dispatchers.IO)
        .onStart {
            Log.e(TAG, "请求数据1")
            before?.invoke()  // 前置准备，一般是弹出加载框
        }
        // 请求完成：成功/失败
        .onCompletion {
            Log.e(TAG, "请求完成1")
            after?.invoke()
//            if(it==null){
//                Log.e(TAG, "请求完成1")
//                after?.invoke()
//            }else{
//                Log.e(TAG, "请求异常1")
//            }
        }
        // 捕获异常
        .catch { e ->
            Log.e(TAG, "请求异常1")
            e.printStackTrace()
            val exception = ExceptionHandler.handleException(e)
            errorHandler?.invoke(exception.errCode, exception.errMsg)
        }
    return flow
}

// 多任务特制
suspend fun requestFlowResponse2(
    before: (() -> Unit)? = null,  // 前置准备
    after: (() -> Unit)? = null,  // 后置工作
    request: suspend () -> BaseResponse<out Any>?,  // 请求
    errorHandler: ((Int?, String?) -> Unit)? = null,  // 错误处理
    timeout: Long = 10*1000
): Flow<BaseResponse<out Any>?> {
    val flow = flow {
        // 设置10秒超时
        val response = withTimeout(timeout) {
            request()
        }
        if(response!=null){
            if (!response.isSuccessful()) {
                throw ApiException(response.errorCode, response.errorMsg)
            }
        }else{
            throw OtherException(6666,"无法获取数据")
        }
        emit(response)
    }
        .flowOn(Dispatchers.IO)
        .onStart {
            Log.e(TAG, "请求数据2")
            before?.invoke()  // 前置准备，一般是弹出加载框
        }
        // 请求完成：成功/失败
        .onCompletion {
            Log.e(TAG, "请求完成2")
            after?.invoke()
//            if(it==null){
//                Log.e(TAG, "请求完成2")
//                after?.invoke()
//            }else{
//                Log.e(TAG, "请求异常2")
//            }
        }
        .flowOn(Dispatchers.Main)
        // 捕获异常
        .catch { e ->
            Log.e(TAG, "请求异常2")
            e.printStackTrace()
            val exception = ExceptionHandler.handleException(e)
            errorHandler?.invoke(exception.errCode, exception.errMsg)
        }
    return flow
}

suspend fun <T:Any?> requestFlowResponse3(
    before: (() -> Unit)? = null,  // 前置准备
    after: (() -> Unit)? = null,  // 后置工作
    request: suspend () -> T,  // 请求
    errorHandler: ((Int?, String?) -> Unit)? = null,  // 错误处理
    timeout: Long = 10*1000
): Flow<T> {
    val flow = flow {
        // 设置10秒超时
        val response = withTimeout(timeout) {
            request()
        }
        if(response!=null){
            emit(response)
        }else{
            throw OtherException(6666,"无法获取数据")
        }
    }
        .flowOn(Dispatchers.IO)
        .onStart {
            Log.e(TAG, "请求数据3")
            before?.invoke()  // 前置准备，一般是弹出加载框
        }
        // 请求完成：成功/失败
        .onCompletion {
            Log.e(TAG, "请求完成3")
            after?.invoke()
//            if(it==null){
//                Log.e(TAG, "请求成功3")
//                after?.invoke()
//            }else{
//                Log.e(TAG, "请求异常3")
//            }
        }
        .flowOn(Dispatchers.Main)
        // 捕获异常
        .catch { e ->
            Log.e(TAG, "请求错误3")
            e.printStackTrace()
            val exception = ExceptionHandler.handleException(e)
            errorHandler?.invoke(exception.errCode, exception.errMsg)
        }
    return flow
}


fun <T> combineFlow(
    flows:List<Flow<BaseResponse<T>?>>,
    parser: (Array<BaseResponse<T>?>) -> Any?,
    before: (() -> Unit)?=null,
    after: (() -> Unit)?=null,
    errorHandler: ((Throwable) -> Unit)?=null
):Flow<Any?>{
    return combine(flows){ result ->
        Log.e(TAG, "执行数据处理" )
        for ((index, baseResponse) in result.withIndex()) {
            Log.i(TAG, "$index : $baseResponse" )
        }
        parser.invoke(result)
    }
        .flowOn(Dispatchers.IO)
        .onStart {
            Log.e(TAG, "启动任务")
            before?.invoke()
        }
        .onCompletion {
            if(it==null){
                Log.e(TAG, "请求完成")
                after?.invoke()
            }else{
                Log.e(TAG, "发生异常")
            }
        }
        .flowOn(Dispatchers.Main)
        .catch {
            Log.e(TAG, "任务异常")
            errorHandler?.invoke(it)
        }
}
