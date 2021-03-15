package com.gw.mvvm.wan.viewModel.login

import androidx.lifecycle.MutableLiveData
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.request
import com.gw.mvvm.framework.network.state.ResultState
import com.gw.mvvm.wan.data.model.UserInfo
import com.gw.mvvm.wan.data.repository.LoginRepository

/**
 * 登录,注册,登出ViewModel
 * @author play0451
 */
class LoginViewModel : BaseViewModel() {
    var userName: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()
    val confirmPassword: MutableLiveData<String> = MutableLiveData()

    val loginResult: MutableLiveData<ResultState<UserInfo>> = MutableLiveData()
    val logoutResult: MutableLiveData<Boolean> = MutableLiveData()

    private val repository: LoginRepository by lazy { LoginRepository() }

    /**
     * 登录
     */
    fun login() {
        if (userName.value.isNullOrBlank() || password.value.isNullOrEmpty()) {
            return
        }
        request({ repository.login(userName.value!!, password.value!!) }, loginResult)
    }

    /**
     * 注册
     */
    fun register() {
        if (userName.value.isNullOrBlank() || password.value.isNullOrEmpty() || confirmPassword.value.isNullOrEmpty()) {
            return
        }
        request({
            repository.register(
                userName.value!!,
                password.value!!,
                confirmPassword.value!!
            )
        }, loginResult)
    }

    /**
     * 登出
     */
    fun logout() {
        request({ repository.logout() }, {
            logoutResult.value = true
        }, {
            logoutResult.value = false
        })
    }
}