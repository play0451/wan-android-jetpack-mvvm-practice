package com.gw.mvvm.wan.data.repository

import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.core.network.ApiResponse
import com.gw.mvvm.wan.core.network.apiService
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.model.BannerInfo
import com.gw.mvvm.wan.data.model.PageInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * Home数据仓库
 * @author play0451
 */
class HomeRepository {
    /**
     * 获取Bnner列表
     * @return ApiResponse<ArrayList<BannerInfo>>
     */
    suspend fun getBannerList(): ApiResponse<ArrayList<BannerInfo>> {
        return withContext(Dispatchers.IO) {
            apiService.getBannerList()
        }
    }

    /**
     * 获取首页文章列表,
     * @param pageIndex Int 页码
     * @return ApiResponse<PageInfo<ArrayList<AriticleInfo>>>
     */
    suspend fun getAriticleList(pageIndex: Int): ApiResponse<PageInfo<ArrayList<AriticleInfo>>> {
        val showTop: Boolean = GlobalData.isHomeShowTop
        return withContext(Dispatchers.IO) {
            val list = async { apiService.getHomeAriticleList(pageIndex) }
            if (showTop && pageIndex == 0) {
                val topList = async { apiService.getHomeTopAriticleList() }
                list.await().data().datas.addAll(0, topList.await().data())
            }
            list.await()
        }
    }
}