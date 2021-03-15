package com.gw.mvvm.wan.data.repository

import com.gw.mvvm.wan.core.network.ApiResponse
import com.gw.mvvm.wan.core.network.apiService
import com.gw.mvvm.wan.data.model.CollectAriticleInfo
import com.gw.mvvm.wan.data.model.CollectUrlInfo
import com.gw.mvvm.wan.data.model.PageInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 收藏Repository
 * @author play0451
 */
class CollectRepository {

    /**
     * 收藏文章
     * @param id Int 文章ID
     * @return ApiResponse<Any?>
     */
    suspend fun collect(id: Int): ApiResponse<Any?> {
        return withContext(Dispatchers.IO) {
            apiService.collect(id)
        }
    }

    /**
     * 取消收藏
     * @param id Int    文章ID
     * @return ApiResponse<Any?>
     */
    suspend fun uncollect(id: Int): ApiResponse<Any?> {
        return withContext(Dispatchers.IO) {
            apiService.uncollect(id)
        }
    }

    /**
     * 收藏网址
     * @param name String
     * @param url String
     * @return ApiResponse<CollectUrlInfo>
     */
    suspend fun collectUrl(name: String, url: String): ApiResponse<CollectUrlInfo> {
        return withContext(Dispatchers.IO) {
            apiService.collectUrl(name, url)
        }
    }

    /**
     * 获取收藏文章数据
     * @param pageIndex Int 页码
     * @return ApiResponse<PageInfo<ArrayList<CollectAriticleInfo>>>
     */
    suspend fun getCollectAriticleList(pageIndex: Int): ApiResponse<PageInfo<ArrayList<CollectAriticleInfo>>> {
        return withContext(Dispatchers.IO) {
            apiService.getCollectAriticleList(pageIndex)
        }
    }

    /**
     * 获取收藏网址数据
     * @return ApiResponse<ArrayList<CollectUrlInfo>>
     */
    suspend fun getCollectUrlList(): ApiResponse<ArrayList<CollectUrlInfo>> {
        return withContext(Dispatchers.IO) {
            apiService.getCollectUrlList()
        }
    }
}