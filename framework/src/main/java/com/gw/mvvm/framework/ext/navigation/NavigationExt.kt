package com.gw.mvvm.framework.ext.navigation

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

fun Fragment.nav(): NavController {
    return NavHostFragment.findNavController(this)
}

fun Fragment.navigateUp() {
    nav().navigateUp()
}

fun View.nav(): NavController {
    return this.findNavController()
}

fun View.navigateUp() {
    nav().navigateUp()
}

fun Activity.nav(viewId: Int): NavController {
    return this.findNavController(viewId)
}

fun Activity.navigateUp(viewId: Int) {
    nav(viewId).navigateUp()
}

var lastNavTime = 0L

/**
 * 防止短时间内多次快速跳转Fragment出现的bug
 * @param actId 跳转的action Id
 * @param bundle 传递的参数
 * @param interval 多少毫秒内不可重复点击 默认0.5秒
 */
fun NavController.navigateAction(actId: Int, bundle: Bundle? = null, interval: Long = 500) {
    val currentTime = System.currentTimeMillis()
    if (currentTime >= lastNavTime + interval) {
        lastNavTime = currentTime
        navigate(actId, bundle)
    }
}

