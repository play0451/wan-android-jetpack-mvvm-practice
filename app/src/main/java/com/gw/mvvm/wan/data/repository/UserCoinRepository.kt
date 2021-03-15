package com.gw.mvvm.wan.data.repository

import com.gw.mvvm.wan.core.network.ApiResponse
import com.gw.mvvm.wan.core.network.apiService
import com.gw.mvvm.wan.data.model.PageInfo
import com.gw.mvvm.wan.data.model.UserCoinHistoryInfo
import com.gw.mvvm.wan.data.model.UserCoinInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 用户积分数据仓库
 * @author play0451
 */
class UserCoinRepository {

    /**
     * 获取用户积分列表
     * @param pageIndex Int 页码
     * @return ApiResponse<PageInfo<ArrayList<UserCoinInfo>>>
     */
    suspend fun getUserCoinList(pageIndex: Int): ApiResponse<PageInfo<ArrayList<UserCoinInfo>>> {
        return withContext(Dispatchers.IO) {
            apiService.getUserCoinList(pageIndex)
        }
    }

    /**
     * 获取积分历史
     * @param pageIndex Int 页码
     * @return ApiResponse<PageInfo<ArrayList<UserCoinHistoryInfo>>>
     */
    suspend fun getUserCoinHistoryList(pageIndex: Int): ApiResponse<PageInfo<ArrayList<UserCoinHistoryInfo>>> {
        return withContext(Dispatchers.IO) {
            apiService.getUserCoinHistoryList(pageIndex)
        }
    }
}