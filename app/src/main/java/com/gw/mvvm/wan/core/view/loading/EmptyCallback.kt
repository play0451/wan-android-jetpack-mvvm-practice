package com.gw.mvvm.wan.core.view.loading

import com.gw.mvvm.wan.R
import com.kingja.loadsir.callback.Callback

/**
 * LoadSir的EmptyCallback
 * @author play0451
 */
class EmptyCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_callback_empty
    }
}