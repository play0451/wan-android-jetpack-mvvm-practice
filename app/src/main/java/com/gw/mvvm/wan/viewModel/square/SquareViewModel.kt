package com.gw.mvvm.wan.viewModel.square

import androidx.lifecycle.MutableLiveData
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.request
import com.gw.mvvm.framework.network.state.ResultState
import com.gw.mvvm.wan.data.enum.SquareAriticleType
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.model.SquareNavigationInfo
import com.gw.mvvm.wan.data.model.SquareSystemInfo
import com.gw.mvvm.wan.data.repository.SquareRepository
import com.gw.mvvm.wan.data.ui.ListUiData

/**
 * 广场ViewModel
 * @author play0451
 */
class SquareViewModel : BaseViewModel() {

    private var pageIndex: Int = 0
    private val repository: SquareRepository by lazy { SquareRepository() }

    //存储广场,每日一问以及体系子页面的文章数据
    private val allAriticles: MutableList<AriticleInfo> by lazy { mutableListOf() }

    val ariticleList: MutableLiveData<ListUiData<AriticleInfo>> by lazy { MutableLiveData() }

    val systemList: MutableLiveData<ResultState<ArrayList<SquareSystemInfo>>> by lazy { MutableLiveData() }

    val systemSubChildList: MutableLiveData<ListUiData<AriticleInfo>> by lazy { MutableLiveData() }

    val navigationList: MutableLiveData<ResultState<ArrayList<SquareNavigationInfo>>> by lazy { MutableLiveData() }

    /**
     * 获取广场数据
     * @param isRefresh Boolean 是否刷新
     */
    fun getAriticleList(type: SquareAriticleType, isRefresh: Boolean = false) {
        if (isRefresh) {
            pageIndex = 0
            allAriticles.clear()
        }
        request({
            when (type) {
                SquareAriticleType.Plaza -> repository.getPlazaList(pageIndex)
                SquareAriticleType.Ask -> repository.getAskList(pageIndex)
            }
        }, {
            pageIndex++
            allAriticles.addAll(it.datas)
            ariticleList.value =
                ListUiData.parsePageInfo(it, isRefresh, allAriticles.toMutableList())
        }, {
            ariticleList.value = ListUiData.parseAppException(it, isRefresh)
        })
    }

    /**
     * 获取体系数据
     */
    fun getSystemList() {
        request({ repository.getSystemList() }, systemList)
    }

    /**
     * 获取体系下的文章数据
     * @param classifyId Int    分类ID
     * @param isRefresh Boolean 是否刷新
     */
    fun getSystemSubChildList(classifyId: Int, isRefresh: Boolean = false) {
        if (isRefresh) {
            pageIndex = 0
            allAriticles.clear()
        }
        request({ repository.getSystemSubChildList(pageIndex, classifyId) }, {
            allAriticles.addAll(it.datas)
            systemSubChildList.value =
                ListUiData.parsePageInfo(it, isRefresh, allAriticles.toMutableList())
        }, {
            systemSubChildList.value = ListUiData.parseAppException(it, isRefresh)
        })
    }

    /**
     * 获取导航数据
     */
    fun getNavigationList() {
        request({ repository.getNavigationList() }, navigationList)
    }
}