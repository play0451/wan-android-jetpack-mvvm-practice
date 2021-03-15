package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 广场体系信息
 * @author play0451
 */
@Parcelize
data class SquareSystemInfo(var children: ArrayList<ClassifyInfo>,
                            var courseId: Int,
                            var id: Int,
                            var name: String,
                            var order: Int,
                            var parentChapterId: Int,
                            var userControlSetTop: Boolean,
                            var visible: Int):Parcelable