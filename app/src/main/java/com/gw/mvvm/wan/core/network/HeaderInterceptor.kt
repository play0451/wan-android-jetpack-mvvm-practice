package com.gw.mvvm.wan.core.network

import com.gw.mvvm.wan.core.GlobalData
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Header拦截器
 * @author play0451
 */
class HeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            //.addHeader("token", "token123456")
            .addHeader("device", "Android")
            .addHeader("User-Agent", "Android")
            .addHeader("isLogin", GlobalData.isLogin.toString())
        return chain.proceed(builder.build())
    }

}