package com.gw.mvvm.wan.ui.fragment.register

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
import com.gw.mvvm.wan.databinding.FragmentRegisterBinding
import com.gw.mvvm.wan.ext.initClose
import com.gw.mvvm.wan.ext.loge
import com.gw.mvvm.wan.viewModel.login.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import splitties.resources.str
import splitties.toast.toast

/**
 * 注册Fragment
 */
class RegisterFragment : BaseFragment<LoginViewModel, FragmentRegisterBinding>() {

    companion object {
        const val KEY_USER_NAME: String = "userName"
    }

    override fun layoutId(): Int = R.layout.fragment_register

    override fun initView(savedInstanceState: Bundle?) {
        var name: String? = arguments?.getString(KEY_USER_NAME)
        mDataBinding.let {
            it.includeToolbar.toolbar.initClose(str(R.string.text_register)) {
                nav().navigateUp()
                KeyboardUtils.hideSoftInput(requireActivity())
            }
            it.btnRegister.isEnabled = false
            it.btnRegister.onClick {
                register()
                KeyboardUtils.hideSoftInput(requireActivity())
            }
            mDataBinding.viewModel = mViewModel
            if (!name.isNullOrBlank()) {
                mViewModel.userName.value = name
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
        mViewModel.confirmPassword.observe(viewLifecycleOwner, {
            mDataBinding.tilConfirmPassword.error =
                if (it.isNullOrEmpty() || it != mViewModel.password.value) str(R.string.hint_input_confirm_password) else ""
            updateLoginButtonState()
        })
        mViewModel.loginResult.observe(viewLifecycleOwner, {
            hideLoading()
            parseState(it, { info ->
                GlobalData.userInfo = info
                mGlobalEventViewModel.sendLoginOut(true)
                toast(R.string.hint_register_success)
                nav().navigateAction(R.id.action_registerFragment_to_mainFragment)
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
        mDataBinding.btnRegister.isEnabled =
            !mViewModel.userName.value.isNullOrBlank()
                    && !mViewModel.password.value.isNullOrEmpty()
                    && !mViewModel.confirmPassword.value.isNullOrEmpty()
                    && mViewModel.userName.value!!.length <= 12
                    && mViewModel.password.value!!.length <= 16
                    && mViewModel.confirmPassword.value!!.length <= 16
                    && mViewModel.password.value.equals(mViewModel.confirmPassword.value)
    }

    private fun register() {
        showLoading()
        //延迟请求登录,以显示一会注册提示框
        lifecycleScope.launch {
            delay(1000)
            mViewModel.register()
        }
    }

    override fun showLoading(msg: CharSequence) {
        super.showLoading(str(R.string.hint_login_loading))
    }
}