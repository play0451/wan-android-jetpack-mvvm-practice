package com.gw.mvvm.wan.util

import android.os.Parcelable
import com.gw.mvvm.framework.core.appContext
import com.tencent.mmkv.MMKV

/**
 * MMKV工具类
 * @author play0451
 */
object MmkvUtil {

    private var mmkv: MMKV? = null

    @JvmStatic
    fun getMmkv(): MMKV? {
        if (mmkv == null) {
            mmkv = MMKV.mmkvWithID(appContext.packageName)
        }
        return mmkv
    }

    fun get(key: String, defaultValue: String = ""): String =
        getMmkv()?.decodeString(key, defaultValue) ?: defaultValue

    fun get(key: String, defaultValue: Int = 0): Int =
        getMmkv()?.decodeInt(key, defaultValue) ?: defaultValue

    fun get(key: String, defaultValue: Boolean = false): Boolean =
        getMmkv()?.decodeBool(key, defaultValue) ?: defaultValue

    fun get(key: String, defaultValue: Long = 0L): Long =
        getMmkv()?.decodeLong(key, defaultValue) ?: defaultValue

    fun get(key: String, defaultValue: Double = 0.0): Double =
        getMmkv()?.decodeDouble(key, defaultValue) ?: defaultValue

    fun get(key: String, defaultValue: Float = 0F): Float =
        getMmkv()?.decodeFloat(key, defaultValue) ?: defaultValue

    fun get(key: String, defaultValue: ByteArray = byteArrayOf()): ByteArray =
        getMmkv()?.decodeBytes(key, defaultValue) ?: defaultValue

    fun get(
        key: String,
        defaultValue: MutableSet<String> = mutableSetOf()
    ): MutableSet<String> =
        getMmkv()?.decodeStringSet(key, defaultValue) ?: defaultValue

    fun <T : Parcelable?> get(key: String, clazz: Class<T>, defaultValue: T? = null): T? =
        getMmkv()?.decodeParcelable(key, clazz, defaultValue) ?: defaultValue

    fun set(key: String, value: String) = getMmkv()?.encode(key, value)

    fun set(key: String, value: Int) = getMmkv()?.encode(key, value)

    fun set(key: String, value: Double) = getMmkv()?.encode(key, value)

    fun set(key: String, value: Float) = getMmkv()?.encode(key, value)

    fun set(key: String, value: Long) = getMmkv()?.encode(key, value)

    fun set(key: String, value: Boolean) = getMmkv()?.encode(key, value)

    fun set(key: String, value: ByteArray) = getMmkv()?.encode(key, value)

    fun set(key: String, value: MutableSet<String>) = getMmkv()?.encode(key, value)

    fun <T : Parcelable> set(key: String, value: T) = getMmkv()?.encode(key, value)

    fun remove(key: String) = getMmkv()?.remove(key)
}