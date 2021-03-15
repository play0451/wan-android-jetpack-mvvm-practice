package com.gw.mvvm.wan.notification

import android.app.NotificationManager
import com.gw.mvvm.wan.R

/**
 * 通知通道
 * @author play0451
 */
enum class NotificationChannels(
    val id: String,
    val channelName: Int,
    val desc: Int,
    val importance: Int,
    val enableVibrate: Boolean,
    val lockscreenVisibility: Int
) {
    None(
        "None",
        R.string.notification_channel_name_none,
        R.string.notification_channel_desc_none,
        NotificationManager.IMPORTANCE_NONE,
        false,
        0
    ),
    Min(
        "Min",
        R.string.notification_channel_name_min,
        R.string.notification_channel_desc_min,
        NotificationManager.IMPORTANCE_MIN,
        false,
        0
    ),
    Low(
        "Low",
        R.string.notification_channel_name_low,
        R.string.notification_channel_desc_low,
        NotificationManager.IMPORTANCE_LOW,
        false,
        0
    ),
    Default(
        "Default",
        R.string.notification_channel_name_default,
        R.string.notification_channel_desc_default,
        NotificationManager.IMPORTANCE_DEFAULT,
        false,
        0
    ),
    High(
        "High",
        R.string.notification_channel_name_high,
        R.string.notification_channel_desc_high,
        NotificationManager.IMPORTANCE_HIGH,
        true,
        1
    ),
    Max(
        "Max",
        R.string.notification_channel_name_max,
        R.string.notification_channel_desc_max,
        NotificationManager.IMPORTANCE_MAX,
        true,
        1
    )
}