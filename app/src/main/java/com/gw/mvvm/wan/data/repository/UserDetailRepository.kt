package com.gw.mvvm.wan.data.repository

import com.gw.mvvm.wan.core.network.ApiResponse
import com.gw.mvvm.wan.core.network.apiService
import com.gw.mvvm.wan.data.model.ShareInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 用户详情数据仓库
 * @author play0451
 */
class UserDetailRepository {
    /**
     * 获取用户信息
     * @param userId Int    用户ID
     * @param pageIndex Int 页码
     * @return ApiResponse<ShareInfo>
     */
    suspend fun getUserInfo(userId: Int, pageIndex: Int): ApiResponse<ShareInfo> {
        return withContext(Dispatchers.IO) {
            apiService.getShareDataById(userId, pageIndex)
        }
    }
}