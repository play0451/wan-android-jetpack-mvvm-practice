package com.gw.mvvm.wan.core.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.gw.mvvm.framework.base.activity.BaseViewModelDataBindingActivity
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.getAppViewModel
import com.gw.mvvm.wan.ext.hideLoadingDialog
import com.gw.mvvm.wan.ext.showLoadingDialog
import com.gw.mvvm.wan.network.NetworkManager
import com.gw.mvvm.wan.network.NetworkState
import com.gw.mvvm.wan.viewModel.app.AppViewModel

/**
 * Activity基类
 * @author play0451
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> :
    BaseViewModelDataBindingActivity<VM, DB>() {

    /**
     * App级别的ViewModel,存放一些全局信息
     */
    val appViewModel: AppViewModel by lazy { getAppViewModel<AppViewModel>() }

    override fun innerInit(savedInstanceState: Bundle?) {
        super.innerInit(savedInstanceState)
        observerNetwork()
    }

    /**
     * 是否监听网络状态事件
     * @return Boolean
     */
    open fun isObserverNetworkStateEvent(): Boolean {
        return false
    }

    /**
     * 监听网络状态
     */
    private fun observerNetwork() {
        if (!isObserverNetworkStateEvent()) {
            return
        }
        NetworkManager.instance.networkStateEvent.observeInActivity(
            this
        ) {
            if (it != null) {
                when (it) {
                    NetworkState.Lost -> {
                        onNetworkAvailable(false)
                    }
                    NetworkState.Available -> {
                        onNetworkAvailable(true)
                    }
                }
            }
        }
    }

    /**
     * 当网络可用状态变化
     * @param boolean Boolean   是否可用
     */
    protected open fun onNetworkAvailable(boolean: Boolean = true) {}

    override fun showLoading(msg: String) {
        showLoadingDialog(msg)
    }

    override fun hideLoading() {
        hideLoadingDialog()
    }
}