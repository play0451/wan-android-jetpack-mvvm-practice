package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 搜索热词信息
 * @author play0451
 */
@Parcelize
data class SearchHotKeyInfo(
    var id: Int,
    var link: String,
    var name: String,
    var order: Int,
    var visible: Int
) : Parcelable