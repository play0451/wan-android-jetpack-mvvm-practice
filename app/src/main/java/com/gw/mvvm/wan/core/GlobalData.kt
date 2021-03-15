package com.gw.mvvm.wan.core

import com.google.gson.Gson
import com.gw.mvvm.wan.data.model.UserInfo
import com.gw.mvvm.wan.util.MmkvUtil

/**
 * 全局 数据
 * @author play0451
 */
object GlobalData {
    private const val KEY_IS_FIRST_START: String = "isFirstStart"
    private const val KEY_IS_LOGIN: String = "isLogin"
    private const val KEY_USER_INFO: String = "userInfo"
    private const val KEY_HOME_SHOW_TOP: String = "homeShowTop"
    private const val KEY_NIGHT_MODE: String = "nightMode"

    /**
     * 是否是第一次启动
     */
    var isFirstStart: Boolean
        get() {
            return MmkvUtil.get(KEY_IS_FIRST_START, true)
        }
        set(value) {
            MmkvUtil.set(KEY_IS_FIRST_START, value)
        }

    /**
     * 是否已登录
     */
    var isLogin: Boolean
        get() {
            return MmkvUtil.get(KEY_IS_LOGIN, false)
        }
        set(value) {
            MmkvUtil.set(KEY_IS_LOGIN, value)
        }

    /**
     * 用户信息
     */
    var userInfo: UserInfo?
        get() {
            val str: String = MmkvUtil.get(KEY_USER_INFO, "")
            if (str.isBlank()) {
                return null
            }
            return Gson().fromJson(str, UserInfo::class.java)
        }
        set(value) {
            isLogin = if (value == null) {
                MmkvUtil.remove(KEY_USER_INFO)
                false
            } else {
                val str: String = Gson().toJson(value)
                MmkvUtil.set(KEY_USER_INFO, str)
                true
            }
        }

    /**
     * 主页是否显示置顶内容
     */
    var isHomeShowTop: Boolean
        get() {
            return MmkvUtil.get(KEY_HOME_SHOW_TOP, true)
        }
        set(value) {
            MmkvUtil.set(KEY_HOME_SHOW_TOP, value)
        }

    /**
     * 是否是夜间模式
     */
    var isNightMode:Boolean
        get() {
            return MmkvUtil.get(KEY_NIGHT_MODE, false)
        }
        set(value) {
            MmkvUtil.set(KEY_NIGHT_MODE, value)
        }
}