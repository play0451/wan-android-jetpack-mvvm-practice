package com.gw.mvvm.wan.viewModel.share

import androidx.lifecycle.MutableLiveData
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.request
import com.gw.mvvm.framework.network.state.ResultState
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.repository.ShareRepository
import com.gw.mvvm.wan.data.ui.ListUiData
import com.gw.mvvm.wan.data.ui.UpdateUiData

/**
 * 分享ViewModel
 * @author play0451
 */
class ShareViewModel : BaseViewModel() {
    val title: MutableLiveData<String> by lazy { MutableLiveData() }
    val url: MutableLiveData<String> by lazy { MutableLiveData() }

    val addAriticleResult: MutableLiveData<ResultState<Any?>> by lazy { MutableLiveData() }

    val ariticleList: MutableLiveData<ListUiData<AriticleInfo>> by lazy { MutableLiveData() }

    val deleteAriticleResult: MutableLiveData<UpdateUiData<Int>> by lazy { MutableLiveData() }

    private var pageIndex: Int = 0

    private val repository: ShareRepository by lazy { ShareRepository() }

    /**
     * 分享文章
     * @param title String  标题
     * @param link String   链接
     */
    fun addAriticle() {
        if (title.value.isNullOrBlank() || url.value.isNullOrBlank()) {
            return
        }
        request({ repository.addAriticle(title.value!!, url.value!!) }, addAriticleResult, true)
    }

    /**
     * 获取文章数据
     * @param isRefresh Boolean 是否刷新
     */
    fun getAriticleList(isRefresh: Boolean = false) {
        if (isRefresh) {
            pageIndex = 0
        }
        request({ repository.getAriticleList(pageIndex) }, {
            ariticleList.value = ListUiData(
                isSuccess = true,
                isRefresh = isRefresh,
                isEmpty = it.shareArticles.isEmpty(),
                isFirstEmpty = isRefresh && it.shareArticles.isEmpty(),
                hasMore = it.shareArticles.hasMore(),
                datas = it.shareArticles.datas
            )
        }, {
            ariticleList.value = ListUiData.parseAppException(it, isRefresh)
        })
    }

    /**
     * 删除文章
     * @param id Int    文章ID
     * @param position Int  文章位置
     */
    fun deleteAriticle(id: Int, position: Int) {
        request({ repository.deleteAriticle(id) }, {
            deleteAriticleResult.value = UpdateUiData(isSuccess = true, data = position)
        }, {
            deleteAriticleResult.value =
                UpdateUiData(isSuccess = false, data = position, errorMsg = it.errorMsg)
        })
    }
}