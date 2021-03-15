package com.gw.mvvm.wan.viewModel.me

import androidx.lifecycle.MutableLiveData
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.core.appContext
import com.gw.mvvm.framework.ext.viewmodel.request
import com.gw.mvvm.framework.ext.viewmodel.requestNoParse
import com.gw.mvvm.framework.network.state.ResultState
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.data.model.MeMenuInfo
import com.gw.mvvm.wan.data.model.UserCoinInfo
import com.gw.mvvm.wan.data.repository.MeRepository

/**
 * 我的ViewModel
 * @author play0451
 */
class MeViewModel : BaseViewModel() {
    val menuList: MutableLiveData<MutableList<MeMenuInfo>> = MutableLiveData()
    val userCoin: MutableLiveData<ResultState<UserCoinInfo>> = MutableLiveData()

    val userName: MutableLiveData<String> = MutableLiveData()

    private val repository: MeRepository by lazy { MeRepository() }

    init {
        updateUserName()
    }

    fun updateUserName()
    {
        if (GlobalData.userInfo != null) {
            userName.value =
                if (GlobalData.userInfo!!.nickname.isBlank()) GlobalData.userInfo!!.username else GlobalData.userInfo!!.nickname
        } else {
            userName.value = appContext.getString(R.string.me_login_please)
        }
    }

            /**
     * 获取菜单列表
     */
    fun getMenuList() {
        requestNoParse({ repository.getMenuList() }, {
            menuList.value = it
        })
    }

    /**
     * 获取当前用户积分
     */
    fun getUserCoin() {
        request({ repository.getUserCoin() }, userCoin)
    }
}