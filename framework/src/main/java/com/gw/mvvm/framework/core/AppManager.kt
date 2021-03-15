package com.gw.mvvm.framework.core

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 * Application引用
 */
val appContext: Application by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { AppManager.app }

/**
 * APP管理类
 */
class AppManager : LifecycleObserver {
    companion object {

        const val TAG: String = "AppManager";

        val instance: AppManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { AppManager() }
        lateinit var app: Application
            private set
    }

    var isAppForeground: Boolean = false
        private set

    val appForeBackgroundEvent = UnPeekLiveData<Boolean>()

    private var isInited: Boolean = false

    /**
     * 初始化,如果需要使用相应功能需要调用此方法初始化,如果继承了BaseApplication则不需要调用
     */
    fun init(application: Application) {
        if (isInited) {
            return
        }
        app = application
        application.registerActivityLifecycleCallbacks(ActivityLifeCycleBack())
        ProcessLifecycleOwner.get().lifecycle.addObserver(this@AppManager)
        isInited = true
    }

    /**
     * app在前台
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForeground() {
        appForeBackgroundEvent.value = true
        isAppForeground = true
        Log.d(TAG, "app to foreground")
    }

    /**
     * app在后台
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onAppBackground() {
        appForeBackgroundEvent.value = false
        isAppForeground = false
        Log.d(TAG, "app to background")
    }
}