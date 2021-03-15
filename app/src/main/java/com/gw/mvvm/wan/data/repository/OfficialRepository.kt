package com.gw.mvvm.wan.data.repository

import com.gw.mvvm.wan.core.network.ApiResponse
import com.gw.mvvm.wan.core.network.apiService
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.model.ClassifyInfo
import com.gw.mvvm.wan.data.model.PageInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 公众号数据仓库
 * @author play0451
 */
class OfficialRepository {
    /**
     * 获取公众号标题列表
     * @return ApiResponse<ArrayList<ClassifyInfo>>
     */
    suspend fun getTitleList(): ApiResponse<ArrayList<ClassifyInfo>> {
        return withContext(Dispatchers.IO) {
            apiService.getOfficialTitleList()
        }
    }

    /**
     * 获取公众号文章列表
     * @param id Int    分类ID
     * @param pageIndex Int 页码
     * @return ApiResponse<PageInfo<ArrayList<AriticleInfo>>>
     */
    suspend fun getAriticleList(
        id: Int,
        pageIndex: Int
    ): ApiResponse<PageInfo<ArrayList<AriticleInfo>>> {
        return withContext(Dispatchers.IO) {
            apiService.getOfficialAriticleList(id, pageIndex)
        }
    }
}