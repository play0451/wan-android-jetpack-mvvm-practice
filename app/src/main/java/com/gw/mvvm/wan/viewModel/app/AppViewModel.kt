package com.gw.mvvm.wan.viewModel.app

import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.data.model.UserInfo
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 * App级别的ViewModel,存放一些全局信息
 * @author play0451
 */
class AppViewModel : BaseViewModel() {
    /*val userInfo: UnPeekLiveData<UserInfo> =
        UnPeekLiveData.Builder<UserInfo>().setAllowNullValue(true).create()

    init {
        userInfo.value = GlobalData.userInfo
    }*/
}