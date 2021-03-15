package com.gw.mvvm.wan.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.gw.mvvm.framework.base.viewmodel.EmptyViewModel
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.base.BaseActivity
import com.gw.mvvm.wan.data.model.NormalWebInfo
import com.gw.mvvm.wan.databinding.ActivityMainBinding
import com.gw.mvvm.wan.ext.loge
import com.gw.mvvm.wan.ui.fragment.web.WebviewFragment
import splitties.toast.toast

class MainActivity : BaseActivity<EmptyViewModel, ActivityMainBinding>() {

    companion object {
        private const val EXIT_TIME: Int = 2000

        const val KEY_OPEN_WEBVIEW: String = "openWebView"
        const val ACTION_OPEN_WEBVIEW: String = "openWebView"
    }

    var lastBackTime: Long = 0L

    override fun layoutId(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nav: NavController =
                    Navigation.findNavController(this@MainActivity, R.id.fragment_host)
                if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.mainFragment) {
                    nav.navigateUp()
                } else {
                    if (System.currentTimeMillis() - lastBackTime > EXIT_TIME) {
                        lastBackTime = System.currentTimeMillis()
                        toast(R.string.text_repress_to_exit)
                    } else {
                        finish()
                    }
                }
            }
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == ACTION_OPEN_WEBVIEW) {
            val data: NormalWebInfo? = intent.getParcelableExtra(KEY_OPEN_WEBVIEW)
            data.run {
                nav(R.id.fragment_host).navigateAction(
                    R.id.action_global_webviewFragment,
                    WebviewFragment.makeData(
                        type = WebviewFragment.TYPE_NORMAL_WEB,
                        data = data!!,
                        showCollect = false,
                        showReload = false,
                        showShare = false,
                    )
                )
            }
        }
    }

    override fun initObserver() {
    }

    override fun initData() {

    }

    override fun isObserverNetworkStateEvent(): Boolean {
        return true
    }

    override fun onNetworkAvailable(boolean: Boolean) {
        super.onNetworkAvailable(boolean)
        if (boolean) {
            toast(R.string.text_network_available)
        } else {
            toast(R.string.text_network_lost)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loge("Activity lifecycle onCreate,${this@MainActivity}")
    }

    override fun onStart() {
        super.onStart()
        loge("Activity lifecycle onStart,${this@MainActivity}")
    }

    override fun onResume() {
        super.onResume()
        loge("Activity lifecycle onResume,${this@MainActivity}")
    }

    override fun onPause() {
        super.onPause()
        loge("Activity lifecycle onPause,${this@MainActivity}")
    }

    override fun onStop() {
        super.onStop()
        loge("Activity lifecycle onPause,${this@MainActivity}")
    }

    override fun onDestroy() {
        super.onDestroy()
        loge("Activity lifecycle onDestroy,${this@MainActivity}")
    }

    override fun onRestart() {
        super.onRestart()
        loge("Activity lifecycle onRestart,${this@MainActivity}")
    }
}