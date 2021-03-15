package com.gw.mvvm.wan.core.base

/**
 * 事件基类
 * @author play0451
 */
open class BaseEvent<T>(var data: T? = null) {
    var time: Long = System.currentTimeMillis()
        protected set
}