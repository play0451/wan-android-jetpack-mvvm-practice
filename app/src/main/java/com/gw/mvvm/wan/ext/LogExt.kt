package com.gw.mvvm.wan.ext

import timber.log.Timber

/**
 * debug日志
 * @param message 内容
 * @param objs 参数
 */
fun logd(message: String, vararg objs: Any) {
    Timber.d(message, objs)
}

/**
 * debug日志
 * @param t 错误信息
 */
fun logd(t: Throwable) {
    Timber.d(t)
}

/**
 * debug日志
 * @param t 错误信息
 * @param message 内容
 * @param objs 参数
 */
fun logd(t: Throwable, message: String, vararg objs: Any) {
    Timber.d(t, message, objs)
}

/**
 * error日志
 * @param message 内容
 * @param objs 参数
 */
fun loge(message: String, vararg objs: Any) {
    Timber.e(message, objs)
}

/**
 * error日志
 * @param t 错误信息
 */
fun loge(t: Throwable) {
    Timber.e(t)
}

/**
 * error日志
 * @param t 错误信息
 * @param message 内容
 * @param objs 参数
 */
fun loge(t: Throwable, message: String, vararg objs: Any) {
    Timber.e(t, message, objs)
}

/**
 * warn日志
 * @param message 内容
 * @param objs 参数
 */
fun logw(message: String, vararg objs: Any) {
    Timber.w(message, objs)
}

/**
 * warn日志
 * @param t 错误信息
 */
fun logw(t: Throwable) {
    Timber.w(t)
}

/**
 * warn日志
 * @param t 错误信息
 * @param message 内容
 * @param objs 参数
 */
fun logw(t: Throwable, message: String, vararg objs: Any) {
    Timber.w(t, message, objs)
}

/**
 * info日志
 * @param message 内容
 * @param objs 参数
 */
fun logi(message: String, vararg objs: Any) {
    Timber.i(message, objs)
}

/**
 * info日志
 * @param t 错误信息
 */
fun logi(t: Throwable) {
    Timber.i(t)
}

/**
 * info日志
 * @param t 错误信息
 * @param message 内容
 * @param objs 参数
 */
fun logi(t: Throwable, message: String, vararg objs: Any) {
    Timber.i(t, message, objs)
}

/**
 * verbose日志
 * @param message 内容
 * @param objs 参数
 */
fun logv(message: String, vararg objs: Any) {
    Timber.v(message, objs)
}

/**
 * verbose日志
 * @param t 错误信息
 */
fun logv(t: Throwable) {
    Timber.v(t)
}

/**
 * verbose日志
 * @param t 错误信息
 * @param message 内容
 * @param objs 参数
 */
fun logv(t: Throwable, message: String, vararg objs: Any) {
    Timber.v(t, message, objs)
}

