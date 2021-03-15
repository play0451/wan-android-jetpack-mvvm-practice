package com.gw.mvvm.framework.base

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.gw.mvvm.framework.core.AppManager

/**
 * Application基类,提供获取ViewModelProvider功能,方便获取App级别的ViewMoel
 */
abstract class BaseApplication : Application(), ViewModelStoreOwner {
    private lateinit var appViewModelStore: ViewModelStore

    private var factory: ViewModelProvider.Factory? = null

    override fun getViewModelStore(): ViewModelStore {
        return appViewModelStore
    }

    override fun onCreate() {
        super.onCreate()
        appViewModelStore = ViewModelStore()
        AppManager.instance.init(this)
        initApp()
    }

    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (factory == null) {
            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return factory as ViewModelProvider.Factory
    }

    abstract fun initApp()
}