package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import com.gw.mvvm.wan.data.enum.MeMenuType
import kotlinx.parcelize.Parcelize

/**
 * 我的菜单条目数据
 * @author play0451
 */
@Parcelize
data class MeMenuInfo(
    val id: Int = 0,
    val type: MeMenuType = MeMenuType.Normal,
    var title: String = "",
    var icon: Int = 0,
    var coin: Int = 0,
    var clickable: Boolean = true,
    var action: String = ACTION_NONE
) : Parcelable {
    companion object {
        /**
         * 动作无
         */
        const val ACTION_NONE: String = "none"

        /**
         * 动作积分
         */

        const val ACTION_COIN: String = "coin"

        /**
         * 动作收藏
         */
        const val ACTION_COLLETION: String = "colletion"

        /**
         * 动作文章
         */
        const val ACTION_ARITICLE: String = "ariticle"

        /**
         * 动作TODO
         */
        const val ACTION_TODO: String = "todo"

        /**
         * 动作网站
         */
        const val ACTION_SITE: String = "site"

        /**
         * 动作设置
         */
        const val ACTION_SETTINGS: String = "settings"
    }
}