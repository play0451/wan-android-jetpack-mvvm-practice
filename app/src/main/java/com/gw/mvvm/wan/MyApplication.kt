package com.gw.mvvm.wan

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import cat.ereza.customactivityoncrash.config.CaocConfig
import coil.Coil
import coil.ImageLoader
import coil.util.CoilUtils
import com.gw.mvvm.framework.base.BaseApplication
import com.gw.mvvm.framework.ext.utils.frameWorkLog
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.ext.logd
import com.gw.mvvm.wan.network.NetworkManager
import com.gw.mvvm.wan.ui.activity.CrashActivity
import com.gw.mvvm.wan.core.view.loading.EmptyCallback
import com.gw.mvvm.wan.core.view.loading.ErrorCallback
import com.gw.mvvm.wan.core.view.loading.LoadingCallback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.tencent.mmkv.MMKV
import okhttp3.OkHttpClient
import timber.log.Timber
import java.io.File

class MyApplication : BaseApplication() {
    override fun initApp() {
        MultiDex.install(this)
        initLog()
        initMmkv()
        initNightMode()
        initCrashActivity()
        initNetwork()
        initLoadSir()
        initCoil()
    }

    /**
     * 初始化日志
     */
    private fun initLog() {
        frameWorkLog = BuildConfig.DEBUG
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    /**
     * 初始化MMKV
     */
    private fun initMmkv() {
        val path: String = MMKV.initialize("${this.filesDir.absolutePath + File.separator}mmkv")
        logd("MMKV path=$path")
    }

    /**
     * 初始化夜间模式
     */
    private fun initNightMode() {
        if (GlobalData.isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    /**
     * 初始化崩溃页面
     */
    private fun initCrashActivity() {
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            .enabled(true) //default: true
            .showErrorDetails(true) //default: true
            .showRestartButton(true) //default: true
            .logErrorOnRestart(true) //default: true
            .trackActivities(true) //default: false
            .minTimeBetweenCrashesMs(3000) //default: 3000
            //.errorDrawable(R.drawable.ic_custom_drawable) //default: bug image
            //.restartActivity(YourCustomActivity::class.java) //default: null (your app's launch activity)
            .errorActivity(CrashActivity::class.java) //default: null (default error activity)
            //.eventListener(YourCustomEventListener()) //default: null
            .apply()
    }

    /**
     * 初始化网络
     */
    private fun initNetwork() {
        NetworkManager.instance.init(this)
    }

    /**
     * 初始化加载页面管理
     */
    private fun initLoadSir() {
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback()) //Loading
            .addCallback(EmptyCallback())   //空
            .addCallback(ErrorCallback())   //错误
            .setDefaultCallback(SuccessCallback::class.java)    //设置默认加载状态页
            .commit()
    }

    /**
     * 初始化图片加载库
     */
    private fun initCoil() {
        val imageLoader = ImageLoader.Builder(this)
            .crossfade(true)
            .placeholder(R.drawable.icon_img_placeholder)
            .error(R.drawable.icon_img_error)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(this))
                    .build()
            }
            .build()
        Coil.setImageLoader(imageLoader)
    }
}