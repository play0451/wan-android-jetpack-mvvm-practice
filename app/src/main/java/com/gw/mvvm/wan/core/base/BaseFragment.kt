package com.gw.mvvm.wan.core.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.gw.mvvm.framework.base.fragment.BaseViewModelDataBindingFragment
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.getAppViewModel
import com.gw.mvvm.wan.ext.hideLoadingDialog
import com.gw.mvvm.wan.ext.showLoadingDialog
import com.gw.mvvm.wan.network.NetworkManager
import com.gw.mvvm.wan.network.NetworkState
import com.gw.mvvm.wan.viewModel.app.AppViewModel
import com.gw.mvvm.wan.viewModel.app.GlobalEventViewModel

/**
 * @author play0451
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> :
    BaseViewModelDataBindingFragment<VM, DB>() {
    /**
     * App级别的ViewModel,存放一些全局信息
     */
    val mAppViewModel: AppViewModel by lazy { getAppViewModel() }

    /**
     * 全局事件ViewModel
     */
    val mGlobalEventViewModel: GlobalEventViewModel by lazy { getAppViewModel() }

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
        NetworkManager.instance.networkStateEvent.observeInFragment(
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

    override fun showLoading(msg: CharSequence) {
        showLoadingDialog(msg)
    }

    override fun hideLoading() {
        hideLoadingDialog()
    }
}