package com.gw.mvvm.wan.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.blankj.utilcode.util.NetworkUtils
import com.gw.mvvm.framework.ext.utils.connectivityManager
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import java.lang.RuntimeException

/**
 * 网络管理类
 * @author play0451
 */
class NetworkManager private constructor() : ConnectivityManager.NetworkCallback() {
    val networkStateEvent: UnPeekLiveData<NetworkState> = UnPeekLiveData()

    companion object {
        val instance: NetworkManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { NetworkManager() }
    }

    fun init(context: Context) {
        context.connectivityManager?.let {
            val request: NetworkRequest = NetworkRequest.Builder().build()
            it.registerNetworkCallback(request, this@NetworkManager)
        }
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        networkStateEvent.postValue(NetworkState.Lost)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        networkStateEvent.postValue(NetworkState.Available)
    }
}