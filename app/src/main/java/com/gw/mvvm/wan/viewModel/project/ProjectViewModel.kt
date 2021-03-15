package com.gw.mvvm.wan.viewModel.project

import androidx.lifecycle.MutableLiveData
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.request
import com.gw.mvvm.framework.network.state.ResultState
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.model.ClassifyInfo
import com.gw.mvvm.wan.data.repository.ProjectRepository
import com.gw.mvvm.wan.data.ui.ListUiData

/**
 * 项目ViewModel
 * @author play0451
 */
class ProjectViewModel : BaseViewModel() {

    var titleList: MutableLiveData<ResultState<ArrayList<ClassifyInfo>>> = MutableLiveData()

    var projectDatas: MutableLiveData<ListUiData<AriticleInfo>> = MutableLiveData()

    private val repository: ProjectRepository by lazy { ProjectRepository() }
    private var pageIndex: Int = 0
    private var allDatas: MutableList<AriticleInfo> = mutableListOf()

    /**
     * 获取标题列表
     */
    fun getTitleList() {
        request({ repository.getTitleList() }, titleList)
    }

    /**
     * 获取项目列表
     * @param classifyId Int    分类ID
     * @param isNewProject Boolean  是否最新项目
     * @param isRefresh Boolean 是否刷新
     */
    fun getProjectList(classifyId: Int, isNewProject: Boolean = false, isRefresh: Boolean = false) {
        if (isRefresh) {
            pageIndex = if (isNewProject) 0 else 1
            allDatas.clear()
        }
        request({ repository.getProjectList(pageIndex, classifyId, isNewProject) }, {
            pageIndex++
            allDatas.addAll(it.datas)
            projectDatas.value = ListUiData.parsePageInfo(it, isRefresh, allDatas.toMutableList())
        }, {
            projectDatas.value = ListUiData.parseAppException(it, isRefresh)
        })
    }
}