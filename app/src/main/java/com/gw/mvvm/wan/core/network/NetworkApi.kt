package com.gw.mvvm.wan.core.network

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import com.gw.mvvm.framework.core.appContext
import com.gw.mvvm.framework.network.BaseNetworkApi
import com.gw.mvvm.framework.network.interceptor.CacheInterceptor
import com.gw.mvvm.framework.network.interceptor.log.LogInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * API接口
 */
val apiService: ApiService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.instance.getApi(
        ApiService::class.java,
        ApiService.SERVICE_URL
    )
}

/**
 * 网络请求构建器
 * @author play0451
 */
class NetworkApi : BaseNetworkApi() {

    val cookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(appContext))
    }

    companion object {
        val instance: NetworkApi by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { NetworkApi() }
    }

    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            cache(Cache(File(appContext.cacheDir, "wamvvmp_cache"), 10 * 1024 * 1024))  //设置缓存文件
            cookieJar(cookieJar)
            addInterceptor(HeaderInterceptor()) //设置Header拦截器
            addInterceptor(CacheInterceptor())  //设置缓存拦截器
            addInterceptor(LogInterceptor())    //设置日志拦截器
            connectTimeout(10, TimeUnit.SECONDS)    //连接超时时间
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
        }
        return builder
    }

    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))    //设置JSON转换器
        }
    }
}