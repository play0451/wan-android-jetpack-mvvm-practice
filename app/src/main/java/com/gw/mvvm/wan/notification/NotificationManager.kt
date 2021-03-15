package com.gw.mvvm.wan.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import com.gw.mvvm.framework.ext.utils.notificationManager
import com.gw.mvvm.wan.R

/**
 * 通知管理类
 * @author play0451
 */
object NotificationManager {

    /**
     * 创建
     * @param context Context
     * @param channel NotificationChannels
     * @return NotificationChannel?
     */
    @JvmStatic
    fun createNotificationChannel(
        context: Context,
        channel: NotificationChannels
    ): NotificationChannel? {
        //只有在8以上才使用channel
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nc: NotificationChannel = NotificationChannel(
                channel.id,
                context.getString(channel.channelName),
                channel.importance
            ).apply {
                description = context.getString(channel.desc)
                enableVibration(channel.enableVibrate)
                lockscreenVisibility = channel.lockscreenVisibility
            }
            context.notificationManager?.createNotificationChannel(nc)
            nc
        } else {
            null
        }
    }

    /**
     * 发送通知
     * @param context Context
     * @param channel NotificationChannels
     * @param id Int
     * @param title CharSequence
     * @param content CharSequence
     * @param summary CharSequence
     * @param contentIntent PendingIntent?
     * @param smallIcon Int
     * @param largeIcon Bitmap?
     * @param actions Array<out Action>
     */
    @JvmStatic
    fun sendNotification(
        context: Context,
        channel: NotificationChannels,
        id: Int,
        title: CharSequence = "",
        content: CharSequence = "",
        summary: CharSequence = "",
        autoCancel: Boolean = true,
        contentIntent: PendingIntent? = null,
        smallIcon: Int = R.mipmap.ic_launcher,
        largeIcon: Bitmap? = null,
        vararg actions: NotificationCompat.Action
    ) {
        val builder: NotificationCompat.Builder = makeBuilder(
            context = context,
            channel = channel,
            title = title,
            content = content,
            summary = summary,
            autoCancel = autoCancel,
            contentIntent = contentIntent,
            smallIcon = smallIcon,
            largeIcon = largeIcon,
            actions = actions
        )
        val notification: Notification = builder.build()
        context.notificationManager?.notify(id, notification)
    }

    /**
     * 创建Builder
     * @param context Context
     * @param channel NotificationChannels
     * @param title CharSequence
     * @param content CharSequence
     * @param summary CharSequence
     * @param contentIntent PendingIntent?
     * @param smallIcon Int
     * @param largeIcon Bitmap?
     * @param actions Array<out Action>
     * @return NotificationCompat.Builder
     */
    @JvmStatic
    fun makeBuilder(
        context: Context,
        channel: NotificationChannels,
        title: CharSequence = "",
        content: CharSequence = "",
        summary: CharSequence = "",
        autoCancel: Boolean = true,
        contentIntent: PendingIntent? = null,
        smallIcon: Int = R.mipmap.ic_launcher,
        largeIcon: Bitmap? = null,
        vararg actions: NotificationCompat.Action
    ): NotificationCompat.Builder {
        val nc: NotificationChannel? = createNotificationChannel(context, channel)
        val channelId: String =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) nc!!.id else channel.id
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
        builder.apply {
            val bigTextStyle =
                NotificationCompat.BigTextStyle()
                    .bigText(content)
                    .setBigContentTitle(title)
                    .setSummaryText(summary)
            setStyle(bigTextStyle)
            setContentTitle(title)
            setContentText(content)
            setSmallIcon(smallIcon)
            setAutoCancel(autoCancel)
            if (largeIcon != null) {
                setLargeIcon(largeIcon)
            }
            if (contentIntent != null) {
                setContentIntent(contentIntent)
            }
            setDefaults(NotificationCompat.DEFAULT_ALL)
            priority = when (channel) {
                NotificationChannels.None -> NotificationCompat.PRIORITY_MIN
                NotificationChannels.Min -> NotificationCompat.PRIORITY_MIN
                NotificationChannels.Low -> NotificationCompat.PRIORITY_LOW
                NotificationChannels.Default -> NotificationCompat.PRIORITY_DEFAULT
                NotificationChannels.High -> NotificationCompat.PRIORITY_HIGH
                NotificationChannels.Max -> NotificationCompat.PRIORITY_HIGH
            }
            setVisibility(channel.lockscreenVisibility)
            if (channel.enableVibrate) {
                setVibrate(longArrayOf(100, 100, 100))
            }
            actions.forEach {
                addAction(it)
            }
        }
        return builder
    }
}