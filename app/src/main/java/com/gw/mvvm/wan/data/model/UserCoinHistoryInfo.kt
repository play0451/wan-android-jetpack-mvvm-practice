package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 积分历史数据
 * @author play0451
 */
@Parcelize
data class UserCoinHistoryInfo(var coinCount: Int,
                          var date: Long,
                          var desc: String,
                          var id: Int,
                          var type: Int,
                          var reason: String,
                          var userId: Int,
                          var userName: String) :Parcelable