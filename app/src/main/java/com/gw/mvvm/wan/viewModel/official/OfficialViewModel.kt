package com.gw.mvvm.wan.viewModel.official

import androidx.lifecycle.MutableLiveData
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.request
import com.gw.mvvm.framework.network.state.ResultState
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.model.ClassifyInfo
import com.gw.mvvm.wan.data.repository.OfficialRepository
import com.gw.mvvm.wan.data.ui.ListUiData

/**
 * 公众号ViewModel
 * @author play0451
 */
class OfficialViewModel : BaseViewModel() {

    private var pageIndex: Int = 0
    private val repository: OfficialRepository by lazy { OfficialRepository() }
    private val allAriticles: MutableList<AriticleInfo> = mutableListOf()

    val titleList: MutableLiveData<ResultState<ArrayList<ClassifyInfo>>> by lazy { MutableLiveData() }
    val ariticleList: MutableLiveData<ListUiData<AriticleInfo>> by lazy { MutableLiveData() }

    /**
     * 获取标题列表
     */
    fun getTitleList() {
        request({ repository.getTitleList() }, titleList)
    }

    /**
     * 获取文章列表
     * @param id Int    分类ID
     * @param isRefresh Boolean 是否刷新
     */
    fun getAriticleList(id: Int, isRefresh: Boolean = false) {
        if (isRefresh) {
            pageIndex = 0
            allAriticles.clear()
        }
        request({ repository.getAriticleList(id, pageIndex) }, {
            pageIndex++
            allAriticles.addAll(it.datas)
            ariticleList.value =
                ListUiData.parsePageInfo(it, isRefresh, allAriticles.toMutableList())
        }, {
            ariticleList.value = ListUiData.parseAppException(it, isRefresh)
        })
    }
}