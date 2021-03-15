package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * 搜索记录信息
 * @author play0451
 */
@Parcelize
@Entity
data class SearchHistoryInfo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    var date: Long = 0
) : Parcelable