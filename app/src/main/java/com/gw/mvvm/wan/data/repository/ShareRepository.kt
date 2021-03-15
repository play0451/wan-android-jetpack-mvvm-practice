package com.gw.mvvm.wan.data.repository

import com.gw.mvvm.wan.core.network.ApiResponse
import com.gw.mvvm.wan.core.network.apiService
import com.gw.mvvm.wan.data.model.ShareInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 分享数据仓库
 * @author play0451
 */
class ShareRepository {

    /**
     * 添加文章
     * @param title String  标题
     * @param link String   链接
     */
    suspend fun addAriticle(title: String, link: String): ApiResponse<Any?> {
        return withContext(Dispatchers.IO) {
            apiService.addAriticle(title, link)
        }
    }

    /**
     * 获取自己分享的文章数据
     * @param pageIndex Int 页码
     * @return ApiResponse<ShareInfo>
     */
    suspend fun getAriticleList(pageIndex: Int): ApiResponse<ShareInfo> {
        return withContext(Dispatchers.IO) {
            apiService.getShareAriticleList(pageIndex)
        }
    }

    /**
     * 删除自己分享的文章
     * @param id Int    文章ID
     * @return ApiResponse<Any?>
     */
    suspend fun deleteAriticle(id: Int): ApiResponse<Any?> {
        return withContext(Dispatchers.IO) {
            apiService.deleteShareAriticle(id)
        }
    }
}