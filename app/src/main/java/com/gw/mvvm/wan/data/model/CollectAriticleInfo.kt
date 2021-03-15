package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 收藏文章数据
 * @author play0451
 */
@Parcelize
data class CollectAriticleInfo(
    var chapterId: Int,
    var author: String,
    var chapterName: String,
    var courseId: Int,
    var desc: String,
    var envelopePic: String,
    var id: Int,
    var link: String,
    var niceDate: String,
    var origin: String,
    var originId: Int,
    var publishTime: Long,
    var title: String,
    var userId: Int,
    var visible: Int,
    var zan: Int,
    var collected: Boolean = true
) : Parcelable