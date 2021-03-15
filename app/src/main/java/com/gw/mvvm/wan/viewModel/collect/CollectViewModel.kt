package com.gw.mvvm.wan.viewModel.collect

import androidx.lifecycle.MutableLiveData
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.request
import com.gw.mvvm.framework.network.state.ResultState
import com.gw.mvvm.wan.data.model.CollectAriticleInfo
import com.gw.mvvm.wan.data.model.CollectUrlInfo
import com.gw.mvvm.wan.data.repository.CollectRepository
import com.gw.mvvm.wan.data.ui.CollectUiData
import com.gw.mvvm.wan.data.ui.ListUiData

/**
 * 收藏ViewModel
 * @author play0451
 */
class CollectViewModel : BaseViewModel() {
    private var pageIndex: Int = 0
    private val repository: CollectRepository by lazy { CollectRepository() }

    /**
     * 收藏状态
     */
    val collectEvent: MutableLiveData<CollectUiData> by lazy { MutableLiveData() }

    /**
     * 收藏URL状态
     */
    val collectUrlEvent: MutableLiveData<CollectUiData> by lazy { MutableLiveData() }

    /**
     * 收藏文章数据
     */
    val collectAriticleList: MutableLiveData<ListUiData<CollectAriticleInfo>> by lazy { MutableLiveData() }

    /**
     * 收藏网址数据
     */
    val collectUrlList: MutableLiveData<ListUiData<CollectUrlInfo>> by lazy { MutableLiveData() }

    /**
     * 收藏文章
     * @param id Int    文章ID
     */
    fun collect(id: Int) {
        request({ repository.collect(id) }, {
            collectEvent.value = CollectUiData(isSuccess = true, isCollect = true, id = id)
        }, {
            collectEvent.value =
                CollectUiData(isSuccess = true, isCollect = false, id = id, errorMsg = it.errorMsg)
        })
    }

    /**
     * 取消收藏文章
     * @param id Int    文章ID
     */
    fun uncollect(id: Int) {
        request({ repository.uncollect(id) }, {
            collectEvent.value = CollectUiData(isSuccess = true, isCollect = false, id = id)
        }, {
            CollectUiData(isSuccess = true, isCollect = true, id = id, errorMsg = it.errorMsg)
        })
    }

    /**
     * 收藏网址
     * @param name String
     * @param url String
     */
    fun collectUrl(name: String, url: String) {
        request({ repository.collectUrl(name, url) }, {
            collectUrlEvent.value = (CollectUiData(isSuccess = true, isCollect = true, id = it.id))
        }, {
            collectUrlEvent.value =
                (CollectUiData(isSuccess = false, isCollect = false, errorMsg = it.errorMsg))
        })
    }

    /**
     * 取消收藏网址
     * @param id Int
     */
    fun uncollectUrl(id: Int) {
        request({ repository.uncollect(id) }, {
            collectUrlEvent.value = (CollectUiData(isSuccess = true, isCollect = false, id = id))
        }, {
            collectUrlEvent.value =
                (CollectUiData(
                    isSuccess = false,
                    isCollect = true,
                    errorMsg = it.errorMsg,
                    id = id
                ))
        })
    }

    /**
     * 获取收藏文章数据
     * @param isRefresh Boolean 是否刷新
     */
    fun getCollectAriticleList(isRefresh: Boolean = false) {
        if (isRefresh) {
            pageIndex = 0
        }
        request({ repository.getCollectAriticleList(pageIndex) }, {
            pageIndex++
            val data: ListUiData<CollectAriticleInfo> = ListUiData.parsePageInfo(it, isRefresh)
            data.datas.forEach { info ->
                info.collected = true
            }
            collectAriticleList.value = data
        }, {
            collectAriticleList.value = ListUiData.parseAppException(it, isRefresh)
        })
    }

    /**
     * 获取收藏网址数据
     */
    fun getCollectUrlList() {
        request({ repository.getCollectUrlList() }, {
            it.forEach { info ->
                info.collected = true
            }
            collectUrlList.value = ListUiData(
                isSuccess = true,
                isRefresh = true,
                isEmpty = it.size <= 0,
                isFirstEmpty = it.size <= 0,
                hasMore = false,
                datas = it
            )
        }, {
            collectUrlList.value = ListUiData.parseAppException(it, true)
        })
    }
}