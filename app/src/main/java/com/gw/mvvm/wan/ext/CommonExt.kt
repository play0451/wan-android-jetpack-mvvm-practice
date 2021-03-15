package com.gw.mvvm.wan.ext

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.model.NormalWebInfo
import com.gw.mvvm.wan.notification.NotificationChannels
import com.gw.mvvm.wan.notification.NotificationManager
import com.gw.mvvm.wan.ui.activity.MainActivity
import splitties.resources.color
import splitties.resources.resolveThemeAttribute
import kotlin.random.Random

/**
 * 一些通用的扩展集合
 * @author play0451
 */

/**
 * 检查是否已登录,没登录就跳转到Login
 * @receiver Fragment
 * @return Boolean  是否已登录
 */
fun Fragment.checkToLogin(): Boolean {
    val bo: Boolean = GlobalData.isLogin
    if (bo) {
        return true
    }
    nav().navigateAction(R.id.action_global_loginFragment)
    return false
}

/**
 * 检查是否登录,并执行相应回调
 * @receiver Fragment
 * @param yesCallback Function0<Unit>?  已登录回调
 * @param noCallBack Function0<Unit>?   未登录回调
 */
fun Fragment.checkToLogin(yesCallback: (() -> Unit)? = null, noCallBack: (() -> Unit)? = null) {
    if (GlobalData.isLogin) {
        yesCallback?.invoke()
    } else {
        noCallBack?.invoke()
    }
}

/**
 * 检查是否登录,未登录就跳转到登录页面,已登录并执行相应回调
 * @receiver Fragment
 * @param yesCallBack Function0<Unit>?  已登录回调
 */
fun Fragment.checkToLogin(yesCallBack: (() -> Unit)? = null) {
    if (checkToLogin()) {
        yesCallBack?.invoke()
    }
}

/**
 * 检查是否已登录,没登录就跳转到Login
 * @receiver Activity
 * @param viewId Int 要查询的NavController的View的ID
 * @return Boolean  是否已登录
 */
fun Activity.checkToLogin(viewId: Int): Boolean {
    val bo: Boolean = GlobalData.isLogin
    if (bo) {
        return true
    }
    nav(viewId).navigateAction(R.id.action_global_loginFragment)
    return false
}

/**
 * 检查是否登录,并执行相应回调
 * @receiver Activity
 * @param yesCallback Function0<Unit>?  已登录回调
 * @param noCallBack Function0<Unit>?   未登录回调
 */
fun Activity.checkToLogin(yesCallback: (() -> Unit)? = null, noCallBack: (() -> Unit)? = null) {
    if (GlobalData.isLogin) {
        yesCallback?.invoke()
    } else {
        noCallBack?.invoke()
    }
}

/**
 * 检查是否登录,未登录就跳转到登录页面,已登录并执行相应回调
 * @receiver Activity
 * @param viewId Int    要查询的NavController的View的ID
 * @param yesCallBack Function0<Unit>?  已登录回调
 */
fun Activity.checkToLogin(viewId: Int, yesCallBack: (() -> Unit)? = null) {
    if (checkToLogin(viewId)) {
        yesCallBack?.invoke()
    }
}

/**
 * 发送收藏取消收藏通知,点击后会打开对应网址
 * @receiver Context
 * @param title String  标题
 * @param url String    网址
 * @param isCollect Boolean 是否收藏
 */

/**
 * 发送收藏取消收藏通知,点击后会打开对应网址
 * @receiver Context
 * @param title String  标题
 * @param url String    网址
 * @param isCollect Boolean 是否收藏
 * @param activityClass Class<*>    目标Activity的class,默认MainActivity::class.java
 */
fun Context.sendCollectNotification(
    title: String,
    url: String,
    isCollect: Boolean = true,
    activityClass: Class<*> = MainActivity::class.java
) {
    //如果使用相同的requestCode
    //对于PendingIntent.FLAG_ONE_SHOT,Activity的onNewIntent方法收到的数据始终是第一次发送的数据
    //对于PendingIntent.FLAG_UPDATE_CURRENT,每次数据都会被更新,导致Activity的onNewIntent方法收到的数据都是最后一次发送的数据
    //所以这里对requestCode使用了随机数
    val data = PendingIntent.getActivity(
        this,
        Random.nextInt(),
        Intent(this, activityClass).apply {
            putExtra(
                MainActivity.KEY_OPEN_WEBVIEW,
                NormalWebInfo(url, title)
            )
            action = MainActivity.ACTION_OPEN_WEBVIEW
        }, PendingIntent.FLAG_ONE_SHOT
    )
    NotificationManager.sendNotification(
        context = this,
        channel = NotificationChannels.Low,
        id = Random.nextInt(),
        title = this.getString(if (isCollect) R.string.notify_collect_title_collect else R.string.notify_collect_title_uncollect),
        content = title,
        summary = this.getString(R.string.notify_collect_summary),
        contentIntent = data
    )
}

/**
 *
 * 发送文章收藏取消收藏通知,点击后会打开对应网址
 * @receiver Context
 * @param datas List<AriticleInfo>  数据集合,用来筛选收藏的文章
 * @param id Int    文章ID
 * @param isCollected Boolean   是否收藏
 */
fun Context.sendAriticleCollectNotification(
    datas: List<AriticleInfo>,
    id: Int,
    isCollected: Boolean
) {
    val data: AriticleInfo? = datas.firstOrNull {
        it.id == id
    }
    data?.let {
        sendAriticleCollectNotification(it, isCollected)
    }
}

/**
 * 发送文章收藏取消收藏通知,点击后会打开对应网址
 * @receiver Context
 * @param data AriticleInfo 数据,用来筛选收藏的文章
 * @param isCollected Boolean   是否收藏
 */
fun Context.sendAriticleCollectNotification(data: AriticleInfo, isCollected: Boolean) {
    sendCollectNotification(data.title, data.link, isCollected)
}

/**
 * 根据attr属性的ID获取对应资源的ID
 * @receiver Fragment
 * @param attrRes Int
 * @param resolveRefs Boolean
 * @return Int
 */
@AnyRes
fun Fragment.resolveThemeAttribute(@AttrRes attrRes: Int, resolveRefs: Boolean = true): Int {
    return requireContext().resolveThemeAttribute(attrRes, resolveRefs)
}

/**
 * 根据attr属性的ID获取对应颜色
 * @receiver Fragment
 * @param attrRes Int
 * @param resolveRefs Boolean
 * @return Int
 */
@ColorInt
fun Fragment.resolveThemeColor(@AttrRes attrRes: Int, resolveRefs: Boolean = true): Int {
    return color(resolveThemeAttribute(attrRes, resolveRefs))
}