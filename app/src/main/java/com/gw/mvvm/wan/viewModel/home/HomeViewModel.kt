package com.gw.mvvm.wan.viewModel.home

import androidx.lifecycle.MutableLiveData
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.request
import com.gw.mvvm.framework.network.state.ResultState
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.model.BannerInfo
import com.gw.mvvm.wan.data.repository.HomeRepository
import com.gw.mvvm.wan.data.ui.ListUiData

/**
 * @author play0451
 */
class HomeViewModel : BaseViewModel() {

    private val repository: HomeRepository by lazy { HomeRepository() }
    private var pageIndex: Int = 0
    private val allDatas: ArrayList<AriticleInfo> = arrayListOf()

    val bannerList: MutableLiveData<ResultState<ArrayList<BannerInfo>>> = MutableLiveData()
    var ariticleList: MutableLiveData<ListUiData<AriticleInfo>> = MutableLiveData()

    /**
     * 获取Banner列表
     */
    fun getBannerList() {
        request({ repository.getBannerList() }, bannerList)
    }

    /**
     * 获取文章列表
     * @param isRefresh Boolean 是否是刷新
     */
    fun getAriticleList(isRefresh: Boolean = false) {
        //刷新就把页码设为0
        if (isRefresh) {
            pageIndex = 0
            allDatas.clear()
        }
        //请求服务器,不使用ResultState解析结果
        request({
            repository.getAriticleList(pageIndex)
        }, {
            pageIndex++
            allDatas.addAll(it.datas)
            ariticleList.value = ListUiData.parsePageInfo(it, isRefresh, allDatas.toMutableList())
        }, {
            ariticleList.value = ListUiData.parseAppException(it, isRefresh)
        })
    }
}