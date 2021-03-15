package com.gw.mvvm.wan.data.repository

import com.gw.mvvm.wan.core.network.ApiResponse
import com.gw.mvvm.wan.core.network.apiService
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.model.ClassifyInfo
import com.gw.mvvm.wan.data.model.PageInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 项目数据仓库
 * @author play0451
 */
class ProjectRepository {
    /**
     * 获取项目标题列表
     * @return ApiResponse<ArrayList<ClassifyInfo>>
     */
    suspend fun getTitleList(): ApiResponse<ArrayList<ClassifyInfo>> {
        return withContext(Dispatchers.IO) {
            apiService.getProjecTitleList()
        }
    }

    /**
     * 获取项目列表
     * @param pageIndex Int 页码
     * @param classifyId Int    分类ID
     * @param isNewProject Boolean  是否最新项目
     * @return ApiResponse<PageInfo<ArrayList<AriticleInfo>>>
     */
    suspend fun getProjectList(
        pageIndex: Int,
        classifyId: Int,
        isNewProject: Boolean
    ): ApiResponse<PageInfo<ArrayList<AriticleInfo>>> {
        return withContext(Dispatchers.IO) {
            if (isNewProject)
                apiService.getNewProjecList(pageIndex)
            else
                apiService.getProjecListByType(pageIndex, classifyId)
        }
    }
}