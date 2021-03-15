package com.gw.mvvm.wan.core.view.loading

import android.content.Context
import android.view.View
import com.gw.mvvm.wan.R
import com.kingja.loadsir.callback.Callback

/**
 * LoadSir的LoadingCallback
 * @author play0451
 */
class LoadingCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_callback_loading
    }

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return true
    }
}