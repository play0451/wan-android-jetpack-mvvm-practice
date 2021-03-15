package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 用户积分数据
 * @author play0451
 */
@Parcelize
data class UserCoinInfo(
    var coinCount: Int,//当前积分
    var rank: Int,
    var userId: Int,
    var username: String
) : Parcelable