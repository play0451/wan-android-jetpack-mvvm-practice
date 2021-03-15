package com.gw.mvvm.wan.data.ui

/**
 * 收藏UI数据
 * @property isSuccess Boolean  是否成功
 * @property isCollect Boolean  是否收藏
 * @property id Int 收藏ID
 * @property errorMsg String    错误信息
 * @constructor
 */
data class CollectUiData(
    /**
     * 是否成功
     */
    var isSuccess: Boolean = true,
    /**
     * 是否收藏
     */
    var isCollect: Boolean = false,
    /**
     * 收藏Id
     */
    var id: Int = -1,
    /**
     * 错误信息
     */
    var errorMsg: String = ""
)