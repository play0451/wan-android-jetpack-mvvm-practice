package com.gw.mvvm.framework.core

import android.app.Activity
import java.util.*

/**
 * Activity管理单例类
 */
object ActivityManger {
    private val activityList = LinkedList<Activity>()
    private val currentActivity: Activity?
        get() =
            if (activityList.isEmpty()) null
            else activityList.last

    /**
     * activity入栈
     */
    fun pushActivity(activity: Activity) {
        if (activityList.contains(activity)) {
            if (activityList.last != activity) {
                activityList.remove(activity)
                activityList.add(activity)
            }
        } else {
            activityList.add(activity)
        }
    }

    /**
     * activity出栈
     */
    fun popActivity(activity: Activity) {
        activityList.remove(activity)
    }

    /**
     * 关闭当前activity
     */
    fun finishCurrentActivity() {
        currentActivity?.finish()
    }

    /**
     * 关闭传入的activity
     */
    fun finishActivity(activity: Activity) {
        activityList.remove(activity)
        activity.finish()
    }

    /**
     * 关闭传入的activity类名
     */
    fun finishActivity(clazz: Class<*>) {
        for (activity in activityList)
            if (activity.javaClass == clazz)
                activity.finish()
    }

    /**
     * 关闭所有的activity
     */
    fun finishAllActivity() {
        for (activity in activityList)
            activity.finish()
    }
}