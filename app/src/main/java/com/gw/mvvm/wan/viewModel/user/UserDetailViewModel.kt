package com.gw.mvvm.wan.viewModel.user

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ResourceUtils
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.request
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.model.CoinInfo
import com.gw.mvvm.wan.data.repository.UserDetailRepository
import com.gw.mvvm.wan.data.ui.ListUiData

/**
 * 用户详情ViewModel
 * @author play0451
 */
class UserDetailViewModel : BaseViewModel() {
    //从1开始
    private var pageIndex: Int = 1
    private val repository: UserDetailRepository by lazy { UserDetailRepository() }

    var userInfo: MutableLiveData<CoinInfo> = MutableLiveData()
    val shareList: MutableLiveData<ListUiData<AriticleInfo>> = MutableLiveData()

    var drawableBackground: Drawable

    init {
        val imgs: List<Int> = listOf(
            R.drawable.img_splash_0,
            R.drawable.img_splash_1,
            R.drawable.img_splash_2,
            R.drawable.img_splash_3,
            R.drawable.img_splash_4
        )
        drawableBackground = ResourceUtils.getDrawable(imgs.random())
    }

    fun getUserInfo(userId: Int, isRefresh: Boolean = false) {
        if (isRefresh) {
            pageIndex = 1
        }
        request({ repository.getUserInfo(userId, pageIndex) }, {
            if (pageIndex == 1) {
                userInfo.value = it.coinInfo
            }
            pageIndex++
            val list: ListUiData<AriticleInfo> = ListUiData(
                isSuccess = true,
                isRefresh = it.shareArticles.isRefresh(),
                isEmpty = it.shareArticles.isEmpty(),
                hasMore = it.shareArticles.hasMore(),
                isFirstEmpty = isRefresh && it.shareArticles.isEmpty(),
                datas = it.shareArticles.datas
            )
            shareList.value = list
        }, {
            val list: ListUiData<AriticleInfo> = ListUiData(
                isSuccess = false,
                isRefresh = isRefresh,
                isEmpty = true,
                hasMore = false,
                isFirstEmpty = true,
                error = it.errorMsg,
                datas = arrayListOf()
            )
            shareList.value = list
        })
    }
}