package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 分类信息
 * @author play0451
 */
@Parcelize
data class ClassifyInfo(
    var children: List<String> = listOf(),
    var courseId: Int = 0,
    var id: Int = 0,
    var name: String = "",
    var order: Int = 0,
    var parentChapterId: Int = 0,
    var userControlSetTop: Boolean = false,
    var visible: Int = 0
) : Parcelable