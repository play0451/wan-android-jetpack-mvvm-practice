package com.gw.mvvm.wan.data.repository

import com.gw.mvvm.wan.core.network.ApiResponse
import com.gw.mvvm.wan.core.network.apiService
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.model.PageInfo
import com.gw.mvvm.wan.data.model.SearchHotKeyInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 搜索数据仓库
 * @author play0451
 */
class SearchRepository {
    /**
     * 获取搜索热词
     * @return ApiResponse<ArrayList<SearchHotKeyInfo>>
     */
    suspend fun getHotKeys(): ApiResponse<ArrayList<SearchHotKeyInfo>> {
        return withContext(Dispatchers.IO) {
            apiService.getSearchHotKeys()
        }
    }

    /**
     * 获取搜索结果
     * @param pageIndex Int 页码
     * @param key String    关键词
     * @return ApiResponse<PageInfo<ArrayList<AriticleInfo>>>
     */
    suspend fun getSearchResult(
        pageIndex: Int,
        key: String
    ): ApiResponse<PageInfo<ArrayList<AriticleInfo>>> {
        return withContext(Dispatchers.IO) {
            apiService.getSearchResult(pageIndex, key)
        }
    }
}