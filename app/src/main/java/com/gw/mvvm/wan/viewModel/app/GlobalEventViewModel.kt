package com.gw.mvvm.wan.viewModel.app

import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.wan.core.base.BaseEvent
import com.gw.mvvm.wan.data.event.CollectAriticleEventData
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 * 全局事件ViewModel
 * @author play0451
 */
class GlobalEventViewModel : BaseViewModel() {

    /**
     * 分享文章事件
     */
    val shareAriticleEvent: UnPeekLiveData<BaseEvent<Any>> by lazy { UnPeekLiveData() }

    /**
     * 收藏文章事件
     */
    val collectAriticleEvent: UnPeekLiveData<BaseEvent<CollectAriticleEventData>> by lazy { UnPeekLiveData() }

    /**
     * To do刷新事件
     */
    val todoRefreshEvent: UnPeekLiveData<BaseEvent<Any>> by lazy { UnPeekLiveData() }

    /**
     * 登录登出事件
     */
    val loginOutEvent: UnPeekLiveData<BaseEvent<Boolean>> by lazy { UnPeekLiveData() }

    /**
     * 通知分享分章
     */
    fun sendShareAriticle() {
        shareAriticleEvent.value = BaseEvent()
    }

    /**
     * 通知收藏文章
     * @param id Int    文章ID
     * @param isCollect Boolean 是否收藏
     */
    fun sendCollectAriticle(id: Int, isCollect: Boolean) {
        collectAriticleEvent.value = BaseEvent(CollectAriticleEventData(id, isCollect))
    }

    /**
     * 通知To do刷新
     */
    fun sendTodoRefresh() {
        todoRefreshEvent.value = BaseEvent()
    }

    /**
     * 发送登录登出事件
     * @param isLogin Boolean
     */
    fun sendLoginOut(isLogin: Boolean) {
        loginOutEvent.value = BaseEvent(isLogin)
    }
}