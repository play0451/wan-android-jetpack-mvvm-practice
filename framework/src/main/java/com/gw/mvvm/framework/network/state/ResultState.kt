package com.gw.mvvm.framework.network.state

import androidx.lifecycle.MutableLiveData
import com.gw.mvvm.framework.network.AppException
import com.gw.mvvm.framework.network.BaseResponse
import com.gw.mvvm.framework.network.ExceptionHandle

/**
 * 自定义结果集封装类
 */
sealed class ResultState<out T> {
    companion object {
        fun <T> onSuccess(data: T): ResultState<T> = Success(data)
        fun <T> onLoading(loadingMessage: String): ResultState<T> = Loading(loadingMessage)
        fun <T> onError(error: AppException): ResultState<T> = Error(error)
    }

    data class Loading(val loadingMessage: String) : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val error: AppException) : ResultState<Nothing>()
}

/**
 * 处理返回值
 * @param response 请求结果
 */
fun <T> MutableLiveData<ResultState<T>>.parseResult(response: BaseResponse<T>) {
    value = when {
        //如果请求结果是成功的,就设置MutableLiveData<ResultState<T>>的值为ResultState的Success子类
        response.isSucces() -> {
            ResultState.onSuccess(response.data())
        }
        //如果失败了就设置为Error
        else -> {
            ResultState.onError(AppException(response.code(), response.msg()))
        }
    }
}

/**
 * 不处理返回值 直接返回请求结果为成功
 * @param result 请求结果成功
 */
fun <T> MutableLiveData<ResultState<T>>.parseResult(result: T) {
    //不进行判断,直接设置设置MutableLiveData<ResultState<T>>的值为ResultState的Success子类
    value = ResultState.onSuccess(result)
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<ResultState<T>>.parseException(e: Throwable) {
    this.value = ResultState.onError(ExceptionHandle.handleException(e))
}