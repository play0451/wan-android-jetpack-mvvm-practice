package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 普通网页信息
 * @author play0451
 */
@Parcelize
data class NormalWebInfo(
    val url: String,
    val title: String = ""
) : Parcelable