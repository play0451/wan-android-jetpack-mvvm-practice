package com.gw.mvvm.framework.network.interceptor

import android.content.Context
import android.net.NetworkCapabilities
import android.os.Build
import com.gw.mvvm.framework.core.appContext
import com.gw.mvvm.framework.ext.utils.connectivityManager
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 缓存拦截器
 * @property day Int    缓存时长,默认7天
 * @constructor
 */
class CacheInterceptor(var day: Int = 7) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!isNetworkAvailable(appContext)) {
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }
        val response = chain.proceed(request)
        if (!isNetworkAvailable(appContext)) {
            val maxAge = 60 * 60
            response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=$maxAge")
                .build()
        } else {
            val maxStale = 60 * 60 * 24 * day // tolerate 4-weeks stale
            response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
        }
        return response
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val manager = context.connectivityManager ?: return false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities =
                manager.getNetworkCapabilities(manager.activeNetwork)
            if (networkCapabilities != null) {
                return (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
            }
        } else {
            val networkInfo = manager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
        return false
    }
}