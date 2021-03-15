package com.gw.mvvm.wan.core.network

import com.gw.mvvm.framework.network.BaseResponse

/**
 * 服务器返回数据基类,通常服务器返回数据有固定的格式
 * 通过固定字段加泛型的方式灵活处理数据
 * @author play0451
 */
data class ApiResponse<T>(private val errorCode: Int, private val errorMsg: String, private val data: T) :
    BaseResponse<T>() {
    override fun isSucces(): Boolean = errorCode == 0

    override fun data(): T = data

    override fun code(): Int = errorCode

    override fun msg(): String = errorMsg
}