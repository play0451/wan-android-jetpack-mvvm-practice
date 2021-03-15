package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 标签信息
 * @author play0451
 */
@Parcelize
data class TagInfo(var name:String, var url:String):Parcelable