package com.gw.mvvm.wan.data.event

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 收藏事件数据
 * @author play0451
 */
@Parcelize
data class CollectAriticleEventData(var id: Int, var isCollected: Boolean) : Parcelable