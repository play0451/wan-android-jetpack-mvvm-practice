package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *分享人信息
 * @author play0451
 */
@Parcelize
data class CoinInfo(
    var coinCount: Int,
    var rank: Int,
    var userId: Int,
    var username: String
) : Parcelable