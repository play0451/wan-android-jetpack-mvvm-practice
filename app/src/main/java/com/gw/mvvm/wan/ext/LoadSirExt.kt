package com.gw.mvvm.wan.ext

import android.view.View
import com.gw.mvvm.wan.core.view.loading.EmptyCallback
import com.gw.mvvm.wan.core.view.loading.ErrorCallback
import com.gw.mvvm.wan.core.view.loading.LoadingCallback
import com.gw.mvvm.wan.data.ui.ListUiData
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

/**
 * LoadSir加载管理器扩展库
 * @author play0451
 */


/**
 * 初始化加载管理器,带有reload的callback
 * @param view View 要注册的View
 * @param callback Function0<Unit>? 回调,可选
 * @return LoadService<Any> 加载管理器对象
 */
fun initLoadServiceWithReloadCallback(view: View, callback: (() -> Unit)?): LoadService<Any> {
    val loadSir = LoadSir.getDefault().register(view) {
        callback?.invoke()
    }
    loadSir.showSuccess()
    return loadSir
}

/**
 * 显示空
 * @receiver LoadService<*>
 */
fun LoadService<*>.showEmpty() {
    this.showCallback(EmptyCallback::class.java)
}

/**
 * 显示错误
 * @receiver LoadService<*>
 */
fun LoadService<*>.showError() {
    this.showCallback(ErrorCallback::class.java)
}

/**
 * 显示Loading
 * @receiver LoadService<*>
 */
fun LoadService<*>.showLoading() {
    this.showCallback(LoadingCallback::class.java)
}

/**
 * LoadService处理结果
 * @receiver LoadService<*>
 * @param data ListUiData<*>
 */
fun LoadService<*>.dealResult(data: ListUiData<*>) {
    if (data.isSuccess) {
        if (data.isFirstEmpty) {
            this.showEmpty()
        } else {
            this.showSuccess()
        }
    } else {
        this.showError()
    }
}
