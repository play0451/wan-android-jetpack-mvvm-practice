package com.gw.mvvm.wan.viewModel.usercoin

import androidx.lifecycle.MutableLiveData
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.request
import com.gw.mvvm.wan.data.model.UserCoinHistoryInfo
import com.gw.mvvm.wan.data.model.UserCoinInfo
import com.gw.mvvm.wan.data.repository.UserCoinRepository
import com.gw.mvvm.wan.data.ui.ListUiData

/**
 * 用户积分ViewModel
 * @author play0451
 */
class UserCoinViewModel : BaseViewModel() {
    private var pageIndex: Int = 1
    private val repository: UserCoinRepository by lazy { UserCoinRepository() }

    val userCoinList: MutableLiveData<ListUiData<UserCoinInfo>> by lazy { MutableLiveData() }
    val userCoinHistoryList: MutableLiveData<ListUiData<UserCoinHistoryInfo>> by lazy { MutableLiveData() }

    /**
     * 获取用户积分列表
     * @param isRefresh Boolean 是否刷新
     */
    fun getUserCoinList(isRefresh: Boolean = false) {
        if (isRefresh) {
            pageIndex = 1
        }
        request({ repository.getUserCoinList(pageIndex) }, {
            pageIndex++
            userCoinList.value = ListUiData.parsePageInfo(it, isRefresh)
        }, {
            userCoinList.value = ListUiData.parseAppException(it, isRefresh)
        })
    }

    /**
     * 获取积分记录
     * @param isRefresh Boolean 是否刷新
     */
    fun getUserCoinHistoryList(isRefresh: Boolean = false) {
        if (isRefresh) {
            pageIndex = 1
        }
        request({ repository.getUserCoinHistoryList(pageIndex) }, {
            pageIndex++
            userCoinHistoryList.value = ListUiData.parsePageInfo(it, isRefresh)
        }, {
            userCoinHistoryList.value = ListUiData.parseAppException(it, isRefresh)
        })
    }
}