package com.gw.mvvm.wan.data.repository

import com.gw.mvvm.wan.core.network.ApiResponse
import com.gw.mvvm.wan.core.network.apiService
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.model.PageInfo
import com.gw.mvvm.wan.data.model.SquareNavigationInfo
import com.gw.mvvm.wan.data.model.SquareSystemInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 广场数据仓库
 * @author play0451
 */
class SquareRepository {
    /**
     * 获取广场数据
     * @param pageIndex Int 页码
     * @return ApiResponse<PageInfo<ArrayList<AriticleInfo>>>
     */
    suspend fun getPlazaList(pageIndex: Int): ApiResponse<PageInfo<ArrayList<AriticleInfo>>> {
        return withContext(Dispatchers.IO) {
            apiService.getSquarePlazaList(pageIndex)
        }
    }

    /**
     * 获取每日一问数据
     * @param pageIndex Int 页码
     * @return ApiResponse<PageInfo<ArrayList<AriticleInfo>>>
     */
    suspend fun getAskList(pageIndex: Int): ApiResponse<PageInfo<ArrayList<AriticleInfo>>> {
        return withContext(Dispatchers.IO) {
            apiService.getSquareAskList(pageIndex)
        }
    }

    /**
     * 获取广场体系数据
     * @return ApiResponse<ArrayList<SquareSystemInfo>>
     */
    suspend fun getSystemList(): ApiResponse<ArrayList<SquareSystemInfo>> {
        return withContext(Dispatchers.IO) {
            apiService.getSquareSystemList()
        }
    }

    /**
     * 获取广场体系下的文章数据
     * @param pageIndex Int 页码
     * @param classifyId Int    分类ID
     * @return ApiResponse<PageInfo<ArrayList<AriticleInfo>>>
     */
    suspend fun getSystemSubChildList(
        pageIndex: Int,
        classifyId: Int
    ): ApiResponse<PageInfo<ArrayList<AriticleInfo>>> {
        return withContext(Dispatchers.IO) {
            apiService.getSquareSystemSubChildList(pageIndex, classifyId)
        }
    }

    /**
     * 获取广场导航数据
     * @return ApiResponse<ArrayList<SquareNavigationInfo>>
     */
    suspend fun getNavigationList(): ApiResponse<ArrayList<SquareNavigationInfo>> {
        return withContext(Dispatchers.IO) {
            apiService.getSquareNavigationList()
        }
    }
}