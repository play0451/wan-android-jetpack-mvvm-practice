package com.gw.mvvm.wan.ui.fragment.login

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.KeyboardUtils
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.framework.ext.view.onClick
import com.gw.mvvm.framework.ext.viewmodel.parseState
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.databinding.FragmentLoginBinding
import com.gw.mvvm.wan.ext.initClose
import com.gw.mvvm.wan.ext.loge
import com.gw.mvvm.wan.ui.fragment.register.RegisterFragment
import com.gw.mvvm.wan.viewModel.login.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import splitties.resources.str
import splitties.toast.toast

/**
 * 登录Fragment
 */
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override fun layoutId(): Int = R.layout.fragment_login

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.let {
            it.viewModel = mViewModel
            it.includeToolbar.toolbar.initClose(str(R.string.text_login)) {
                nav().navigateUp()
                KeyboardUtils.hideSoftInput(requireActivity())
            }
            it.btnLogin.isEnabled = false
            it.btnLogin.onClick {
                login()
                KeyboardUtils.hideSoftInput(requireActivity())
            }
            it.btnRegister.onClick {
                register()
                KeyboardUtils.hideSoftInput(requireActivity())
            }
        }
    }

    override fun initObserver() {
        mViewModel.userName.observe(viewLifecycleOwner, {
            mDataBinding.tilName.error =
                if (it.isNullOrBlank()) str(R.string.hint_input_user_name) else ""
            updateLoginButtonState()
        })
        mViewModel.password.observe(viewLifecycleOwner, {
            mDataBinding.tilPassword.error =
                if (it.isNullOrEmpty()) str(R.string.hint_input_password) else ""
            updateLoginButtonState()
        })
        mViewModel.loginResult.observe(viewLifecycleOwner, {
            hideLoading()
            parseState(it, { info ->
                GlobalData.userInfo = info
                mGlobalEventViewModel.sendLoginOut(true)
                toast(R.string.hint_login_success)
                nav().navigateUp()
            }, { e ->
                loge(e)
                toast(e.errorMsg)
            })
        })
    }

    override fun initData() {

    }

    /**
     * 更新登录按钮的可用状态
     * 当帐号密码不为空,并且不超长时才可用
     */
    private fun updateLoginButtonState() {
        mDataBinding.btnLogin.isEnabled =
            !mViewModel.userName.value.isNullOrBlank()
                    && !mViewModel.password.value.isNullOrEmpty()
                    && mViewModel.userName.value!!.length <= 12
                    && mViewModel.password.value!!.length <= 16
    }

    override fun showLoading(msg: CharSequence) {
        super.showLoading(str(R.string.hint_login_loading))
    }

    private fun login() {
        showLoading()
        //延迟请求登录,以显示一会登录提示框
        lifecycleScope.launch {
            delay(1000)
            mViewModel.login()
        }
    }

    private fun register() {
        KeyboardUtils.hideSoftInput(requireActivity())
        nav().navigateAction(R.id.action_loginFragment_to_registerFragment, Bundle().apply {
            putString(RegisterFragment.KEY_USER_NAME, mViewModel.userName.value)
        })
    }
}