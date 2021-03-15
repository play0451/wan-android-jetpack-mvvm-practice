package com.gw.mvvm.wan.util

import android.content.Context
import android.os.Environment
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.FileUtils
import com.gw.mvvm.wan.ext.loge
import java.lang.Exception

/**
 * @author play0451
 */

object CacheUtils {
    /**
     *  获取内部缓存大小
     * @param context Context
     * @return Long
     */
    @JvmStatic
    fun getInternalCacheLength(context: Context): Long {
        return FileUtils.getLength(context.cacheDir)
    }

    /**
     * 获取外部缓存大小
     * @param context Context
     * @return Long
     */
    @JvmStatic
    fun getExternalCacheLength(context: Context): Long {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) FileUtils.getLength(
            context.externalCacheDir
        ) else 0
    }

    /**
     * 获取缓存大小,内部和外部之和
     * @param context Context
     * @return Long
     */
    @JvmStatic
    fun getCacheLength(context: Context): Long {
        return getInternalCacheLength(context) + getExternalCacheLength(context)
    }

    /**
     * 获取格式化的内部缓存大小
     * @param context Context
     * @param precision Int 小数精度
     * @return String
     */
    @JvmStatic
    fun getInternalCacheSize(context: Context, precision: Int = 3): String {
        return ConvertUtils.byte2FitMemorySize(getInternalCacheLength(context), precision)
    }

    /**
     * 获取格式化的外部缓存大小
     * @param context Context
     * @param precision Int 小数精度
     * @return String
     */
    @JvmStatic
    fun getExternalCacheSize(context: Context, precision: Int = 3): String {
        return ConvertUtils.byte2FitMemorySize(getExternalCacheLength(context), precision)
    }

    /**
     *  获取格式化的缓存大小,内部外部之和
     * @param context Context
     * @param precision Int 小数精度
     * @return String
     */
    fun getCacheSize(context: Context, precision: Int = 3): String {
        return ConvertUtils.byte2FitMemorySize(
            getInternalCacheLength(context) + getExternalCacheLength(
                context
            ), precision
        )
    }

    /**
     * 删除内部缓存
     * @param context Context
     * @return Boolean
     */
    fun clearInternalCache(context: Context): Boolean {
        return try {
            FileUtils.deleteAllInDir(context.cacheDir)
        } catch (e: Exception) {
            loge(e)
            false
        }
    }

    /**
     * 删除外部缓存
     * @param context Context
     * @return Boolean
     */
    fun clearExternalCache(context: Context): Boolean {
        if (Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()) {
            return true
        }
        return try {
            FileUtils.deleteAllInDir(context.externalCacheDir)
        } catch (e: Exception) {
            loge(e)
            false
        }
    }

    /**
     * 删除缓存,包括内部和外部缓存
     * @param context Context
     * @return Boolean
     */
    fun clearCache(context: Context):Boolean
    {
        return clearInternalCache(context) && clearExternalCache(context)
    }
}