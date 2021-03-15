package com.gw.mvvm.wan.data.ui

import com.gw.mvvm.framework.network.AppException
import com.gw.mvvm.wan.data.model.PageInfo

/**
 * 列表UI数据
 * @author play0451
 */
data class ListUiData<T>(
    /**
     * 是否请求成功
     */
    var isSuccess: Boolean,
    /**
     * 错误消息 isSuccess为false才会有
     */
    var error: String = "",
    /**
     * 是否为刷新操作
     */
    var isRefresh: Boolean = false,
    /**
     * 是否为空列表
     */
    var isEmpty: Boolean = false,
    /**
     * 是否还有更多数据
     */
    var hasMore: Boolean = false,
    /**
     * 是第一页且没有数据
     */
    var isFirstEmpty: Boolean = false,
    /**
     * 新的数据
     */
    var datas: MutableList<T> = arrayListOf(),

    /**
     * 所有的数据
     */
    var allDatas: MutableList<T> = arrayListOf()
) {
    companion object {
        /**
         * 解析PageInfo
         * @param pageInfo PageInfo<L>
         * @param isRefresh Boolean
         * @return ListUiData<T>
         */
        fun <T, L : MutableList<T>> parsePageInfo(
            pageInfo: PageInfo<L>,
            isRefresh: Boolean = false,
            allDatas: MutableList<T> = mutableListOf()
        ): ListUiData<T> {
            return ListUiData(
                true,
                isRefresh = isRefresh,
                isEmpty = pageInfo.isEmpty(),
                hasMore = pageInfo.hasMore(),
                isFirstEmpty = isRefresh && pageInfo.isEmpty(),
                datas = pageInfo.datas,
                allDatas = allDatas
            )
        }

        /**
         * 解析错误
         * @param e AppException
         * @param isRefresh Boolean
         * @return ListUiData<T>
         */
        fun <T> parseAppException(e: AppException, isRefresh: Boolean = false): ListUiData<T> {
            return ListUiData(
                isSuccess = false,
                error = e.errorMsg,
                isRefresh = isRefresh,
                datas = arrayListOf()
            )
        }
    }
}