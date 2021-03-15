package com.gw.mvvm.wan.ui.fragment.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.blankj.utilcode.util.AppUtils
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.framework.ext.navigation.navigateUp
import com.gw.mvvm.framework.ext.viewmodel.getAppViewModel
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.core.network.NetworkApi
import com.gw.mvvm.wan.data.model.NormalWebInfo
import com.gw.mvvm.wan.databinding.IncludeToolbarBinding
import com.gw.mvvm.wan.ext.hideLoadingDialog
import com.gw.mvvm.wan.ext.initClose
import com.gw.mvvm.wan.ext.showDialog
import com.gw.mvvm.wan.ext.showLoadingDialog
import com.gw.mvvm.wan.ui.fragment.web.WebviewFragment
import com.gw.mvvm.wan.util.CacheUtils
import com.gw.mvvm.wan.viewModel.app.GlobalEventViewModel
import com.gw.mvvm.wan.viewModel.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import splitties.resources.str
import splitties.toast.toast

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val loginViewModel: LoginViewModel by viewModels()
    private val globalEventViewModel: GlobalEventViewModel by lazy { getAppViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //这里重写根据PreferenceFragmentCompat 的布局 ，在其根布局插入了一个toolbar
        val containerView = view.findViewById<FrameLayout>(android.R.id.list_container)
        containerView.let {
            //转为线性布局
            val linearLayout = it.parent as? LinearLayout
            linearLayout?.run {
                val binding: IncludeToolbarBinding? = DataBindingUtil.inflate(
                    LayoutInflater.from(requireContext()),
                    R.layout.include_toolbar,
                    null,
                    false
                )
                binding?.let { b ->
                    b.toolbar.initClose(str(R.string.settings_title)) {
                        navigateUp()
                    }
                    addView(b.toolbar, 0)
                }
            }
        }

        initObserver()
    }

    private fun initObserver() {
        loginViewModel.logoutResult.observe(viewLifecycleOwner, {
            if (it) {
                GlobalData.userInfo = null
                NetworkApi.instance.cookieJar.clear()
                globalEventViewModel.sendLoginOut(false)
                navigateUp()
            } else {
                toast(R.string.text_log_out_failed)
            }
        })
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preference)

        //首页是否显示制定内容
        findPreference<SwitchPreference>("settings_key_home_top")?.isChecked =
            GlobalData.isHomeShowTop

        //夜间模式
        findPreference<SwitchPreference>("settings_key_night_mode")?.isChecked =
            GlobalData.isNightMode

        //清除缓存
        findPreference<Preference>("settings_key_clear_cache")?.run {
            setOnPreferenceClickListener {
                //弹窗提示
                showDialog(message = str(R.string.settings_clear_cache_message), positiveAction = {
                    //启动协程
                    lifecycleScope.launch {
                        //显示loading
                        showLoadingDialog(str(R.string.text_please_wait))
                        var result: Boolean = false
                        //切换到IO线程进行清理操作
                        withContext(Dispatchers.IO)
                        {
                            result = CacheUtils.clearCache(requireContext())
                        }
                        //多显示一会loading
                        delay(1000)
                        //隐藏loading
                        hideLoadingDialog()
                        //重设文本
                        summary = CacheUtils.getCacheSize(requireContext())
                        //结果提示
                        toast(if (result) R.string.settings_clear_cache_success else R.string.settings_clear_cache_failed)
                    }
                }, negativeButtonText = str(R.string.button_cancel))
                false
            }
            //文本设置
            summary = CacheUtils.getCacheSize(requireContext())
        }

        //登出
        findPreference<Preference>("settings_key_log_out")?.run {
            isVisible = GlobalData.isLogin
            setOnPreferenceClickListener {
                showDialog(str(R.string.settings_log_out_message), positiveAction = {
                    loginViewModel.logout()
                }, negativeButtonText = str(R.string.button_cancel))
                false
            }
        }

        //开源组件
        findPreference<Preference>("settings_key_open_source")?.setOnPreferenceClickListener {
            nav().navigateAction(
                R.id.action_global_webviewFragment,
                WebviewFragment.makeData(
                    WebviewFragment.TYPE_NORMAL_WEB,
                    NormalWebInfo(
                        "file:///android_asset/www/openSource.html",
                        str(R.string.settings_title_open_source)
                    ),
                    false, false, false, false
                )
            )
            false
        }

        //版本号
        findPreference<Preference>("settings_key_version")?.summary = AppUtils.getAppVersionName()
        //关于
        findPreference<Preference>("settings_key_about")?.setOnPreferenceClickListener {
            showDialog(str(R.string.settings_about_message))
            false
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sp: SharedPreferences?, key: String?) {
        when (key) {
            //主页是否显示置顶内容
            "settings_key_home_top" -> GlobalData.isHomeShowTop =
                sp?.getBoolean("settings_key_home_top", true) ?: true
            //夜间模式
            "settings_key_night_mode" -> {
                val bo: Boolean = sp?.getBoolean("settings_key_night_mode", false) ?: false
                GlobalData.isNightMode = bo
                lifecycleScope.launch {
                    //延迟调用,等待界面动画播放完毕
                    delay(300)
                    AppCompatDelegate.setDefaultNightMode(if (bo) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
}