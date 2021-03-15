package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 广场导航信息
 * @author play0451
 */
@Parcelize
data class SquareNavigationInfo(
    var articles: ArrayList<AriticleInfo>,
    var cid: Int,
    var name: String
) : Parcelable