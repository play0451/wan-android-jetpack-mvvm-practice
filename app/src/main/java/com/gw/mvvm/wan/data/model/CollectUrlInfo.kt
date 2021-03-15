package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 收藏网址数据
 * @author play0451
 */
@Parcelize
data class CollectUrlInfo(
    var icon: String,
    var id: Int,
    var link: String,
    var name: String,
    var order: Int,
    var userId: Int,
    var visible: Int,
    var collected: Boolean = true
) : Parcelable