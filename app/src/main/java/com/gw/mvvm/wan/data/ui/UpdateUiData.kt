package com.gw.mvvm.wan.data.ui

/**
 * 更新UI数据
 * @author play0451
 */
data class UpdateUiData<T>(
    /**
     * 请求是否成功
     */
    var isSuccess: Boolean = true,
    /**
     * 数据
     */
    var data: T? = null,
    /**
     * 错误信息
     */
    var errorMsg: String = ""
) {
}