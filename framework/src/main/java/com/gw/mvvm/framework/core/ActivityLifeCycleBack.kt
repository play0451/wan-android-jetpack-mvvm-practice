package com.gw.mvvm.framework.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

class ActivityLifeCycleBack : Application.ActivityLifecycleCallbacks {
    companion object {
        const val TAG: String = "ActivityLifeCycleBack";
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d(TAG, "onActivityPaused : ${activity.localClassName}")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(TAG, "onActivityStarted : ${activity.localClassName}")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(TAG, "onActivityDestroyed : ${activity.localClassName}")
        ActivityManger.popActivity(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(TAG, "onActivityStopped : ${activity.localClassName}")
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        ActivityManger.pushActivity(activity)
        Log.d(TAG, "onActivityCreated : ${activity.localClassName}")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(TAG, "onActivityResumed : ${activity.localClassName}")
    }
}