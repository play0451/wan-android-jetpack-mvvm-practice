package com.gw.mvvm.framework.base.viewmodel

import androidx.lifecycle.ViewModel
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 * ViewModel基类,提供loading显示隐藏事件
 */
open class BaseViewModel : ViewModel() {

    val loadingEvent: LoadingEvent by lazy { LoadingEvent() }

    inner class LoadingEvent {
        val showLoading by lazy { UnPeekLiveData<String>() }
        val hideLoading by lazy { UnPeekLiveData<Boolean>() }
    }
}