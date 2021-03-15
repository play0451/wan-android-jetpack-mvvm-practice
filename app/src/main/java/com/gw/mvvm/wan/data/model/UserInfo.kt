package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 用户信息
 * @author play0451
 */
@Parcelize
data class UserInfo(
    var admin: Boolean = false,
    var chapterTops: List<String> = listOf(),
    var collectIds: MutableList<String> = mutableListOf(),
    var email: String = "",
    var icon: String = "",
    var id: String = "",
    var nickname: String = "",
    var password: String = "",
    var token: String = "",
    var type: Int = 0,
    var username: String = ""
) : Parcelable