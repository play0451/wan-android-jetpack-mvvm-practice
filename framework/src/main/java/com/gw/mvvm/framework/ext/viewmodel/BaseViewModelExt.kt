package com.gw.mvvm.framework.ext.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gw.mvvm.framework.base.activity.BaseViewModelActivity
import com.gw.mvvm.framework.base.fragment.BaseViewModelFragment
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.utils.loge
import com.gw.mvvm.framework.network.AppException
import com.gw.mvvm.framework.network.BaseResponse
import com.gw.mvvm.framework.network.ExceptionHandle
import com.gw.mvvm.framework.network.state.ResultState
import com.gw.mvvm.framework.network.state.parseException
import com.gw.mvvm.framework.network.state.parseResult
import kotlinx.coroutines.*

/**
 * 显示页面状态
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseViewModelActivity<*>.parseState(
    resultState: ResultState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.invoke()
        }
        is ResultState.Success -> {
            hideLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            hideLoading()
            onError?.invoke(resultState.error)
        }
    }
}

/**
 * 显示页面状态
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseViewModelFragment<*>.parseState(
    resultState: ResultState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.invoke()
        }
        is ResultState.Success -> {
            hideLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            hideLoading()
            onError?.invoke(resultState.error)
        }
    }
}


/**
 * 解析请求结果,区分成功失败
 * @param block 请求体方法
 * @param resultState 请求回调的ResultState数据
 * @param isShowLoading 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    resultState: MutableLiveData<ResultState<T>>,
    isShowLoading: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return viewModelScope.launch {
        //使用runCatching扩展方法,执行方法块,如果成功了就调用onSuccess,失败了调用onFailure
        runCatching {
            //如果显示loading,就设置传入的MutableLiveData<ResultState<T>>的值为密封类ResultState的Loading子类
            if (isShowLoading) resultState.value = ResultState.onLoading(loadingMessage)
            //执行方法块
            block()
        }.onSuccess {
            //使用MutableLiveData<ResultState<T>>的扩展方法parseResult解析请求结果
            //如果请求成功,就设置传入的MutableLiveData<ResultState<T>>的值为密封类ResultState的Success子类,携带请求返回的数据
            //如果请求失败,就设置传入的MutableLiveData<ResultState<T>>的值为密封类ResultState的Error子类,携带错误信息
            resultState.parseResult(it)
        }.onFailure {
            it.message?.loge()
            //使用MutableLiveData<ResultState<T>>的扩展方法parseResult解析错误信息
            //设置传入的MutableLiveData<ResultState<T>>的值为密封类ResultState的Error子类,携带错误信息
            resultState.parseException(it)
        }
        //在设置了传入的MutableLiveData<ResultState<T>>的值后,其观察者就会观察到值的改变
        //就可以调用BaseViewModelActivity或BaseViewModelFragment的parseState来解析对应的ResultState
    }
}

/**
 * 不解析请求结果,直接返回成功
 * @param block 请求体方法
 * @param resultState 请求回调的ResultState数据
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.requestNoParse(
    block: suspend () -> T,
    resultState: MutableLiveData<ResultState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return viewModelScope.launch {
        //使用runCatching扩展方法,执行方法块,如果成功了就调用onSuccess,失败了调用onFailure
        runCatching {
            //如果显示loading,就设置传入的MutableLiveData<ResultState<T>>的值为密封类ResultState的Loading子类
            if (isShowDialog) resultState.value = ResultState.onLoading(loadingMessage)
            //执行方法块
            block()
        }.onSuccess {
            //不对结果进行解析,直接设置传入的MutableLiveData<ResultState<T>>值为ResultState的Success子类,携带请求结果
            resultState.parseResult(it)
        }.onFailure {
            it.message?.loge()
            //使用MutableLiveData<ResultState<T>>的扩展方法parseResult解析错误信息
            //设置传入的MutableLiveData<ResultState<T>>的值为密封类ResultState的Error子类,携带错误信息
            resultState.parseException(it)
        }
        ////在设置了传入的MutableLiveData<ResultState<T>>的值后,其观察者就会观察到值的改变
    }
}

/**
 * 解析请求结果，区分成功失败
 * @param block 请求体方法，必须要用suspend关键字修饰
 * @param success 成功回调
 * @param error 失败回调 可不传
 * @param isShowLoading 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
    isShowLoading: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return viewModelScope.launch {
        //使用runCatching扩展方法,执行方法块,如果成功了就调用onSuccess,失败了调用onFailure
        runCatching {
            //如果显示loading,就发送BaseViewModel的loadingEvent的showLoading事件
            if (isShowLoading) loadingEvent.showLoading.postValue(loadingMessage)
            //执行方法块
            block()
        }.onSuccess {
            //如果成功,就发送BaseViewModel的loadingEvent的hideLoading事件
            loadingEvent.hideLoading.postValue(false)
            /*runCatching {
                //校验请求结果码是否正确，不正确会抛出异常走下面的onFailure
                executeResponse(it) { t ->
                    success(t)
                }
            }.onFailure { e ->
                //打印错误消息
                e.message?.loge()
                //失败回调
                error(ExceptionHandle.handleException(e))
            }*/
            //执行请求结果,根据请求成功与否,分别调用success和error回调
            executeResponse(it, { t -> success(t) }, { e -> error(e) })
        }.onFailure {
            //请求失败,发送BaseViewModel的loadingEvent的hideLoading事件
            loadingEvent.hideLoading.postValue(false)
            //打印错误消息
            it.message?.loge()
            //失败回调
            error(ExceptionHandle.handleException(it))
        }
    }
}

/**
 *  不解析请求结果,直接返回成功
 * @param block 请求体 必须要用suspend关键字修饰
 * @param success 成功回调
 * @param error 失败回调 可不给
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.requestNoParse(
    block: suspend () -> T,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return viewModelScope.launch {
        //使用runCatching扩展方法,执行方法块,如果成功了就调用onSuccess,失败了调用onFailure
        runCatching {
            //如果显示loading,就发送BaseViewModel的loadingEvent的showLoading事件
            if (isShowDialog) loadingEvent.showLoading.postValue(loadingMessage)
            //执行方法块
            block()
        }.onSuccess {
            //如果成功,就发送BaseViewModel的loadingEvent的hideLoading事件
            loadingEvent.hideLoading.postValue(false)
            //请求成功直接调用success回调,不判断请求内部是否成功
            success(it)
        }.onFailure {
            //如果失败,就发送BaseViewModel的loadingEvent的hideLoading事件
            loadingEvent.hideLoading.postValue(false)
            //打印错误消息
            it.message?.loge()
            //失败回调
            error(ExceptionHandle.handleException(it))
        }
    }
}

/**
 * 请求结果过滤，判断请求服务器请求结果是否成功，不成功则会抛出异常
 */
suspend fun <T> executeResponse(
    response: BaseResponse<T>,
    success: suspend CoroutineScope.(T) -> Unit
) {
    coroutineScope {
        when {
            response.isSucces() -> {
                success(response.data())
            }
            else -> {
                throw AppException(
                    response.code(),
                    response.msg(),
                    response.msg()
                )
            }
        }
    }
}

/**
 * 执行请求结果,如果请求成功,就执行success方法,传入请求结果数据,如果失败就调用error方法,传入错误信息
 * @param response BaseResponse<T>  请求结果
 * @param success [@kotlin.ExtensionFunctionType] SuspendFunction2<CoroutineScope, T, Unit> 成功回调
 * @param error [@kotlin.ExtensionFunctionType] SuspendFunction2<CoroutineScope, AppException, Unit>    失败回调
 */
suspend fun <T> executeResponse(
    response: BaseResponse<T>,
    success: suspend CoroutineScope.(T) -> Unit,
    error: suspend CoroutineScope.(AppException) -> Unit
) {
    coroutineScope {
        when {
            response.isSucces() -> {
                success(response.data())
            }
            else -> {
                error(AppException(response.code(), response.msg(), response.msg()))
            }
        }
    }
}

/**
 *  调用携程
 * @param block 操作耗时操作任务
 * @param success 成功回调
 * @param error 失败回调 可不给
 */
fun <T> BaseViewModel.launch(
    block: () -> T,
    success: (T) -> Unit,
    error: (Throwable) -> Unit = {}
) {
    viewModelScope.launch {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                block()
            }
        }.onSuccess {
            success(it)
        }.onFailure {
            error(it)
        }
    }
}
