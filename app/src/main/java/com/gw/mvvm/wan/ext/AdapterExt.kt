package com.gw.mvvm.wan.ext

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.ui.ListUiData
import com.gw.mvvm.wan.ui.fragment.user.UserDetailFragment
import com.gw.mvvm.wan.ui.fragment.web.WebviewFragment
import com.gw.mvvm.wan.viewModel.collect.CollectViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import me.jessyan.autosize.AutoSize
import splitties.toast.toast

/**
 * Adapter扩展库
 * @author play0451
 */


/**
 * BaseQuickAdapter更新数据
 * @receiver BaseQuickAdapter<T, *>
 * @param data ListUiData<T>    数据
 * @param refreshLayout SmartRefreshLayout? 刷新容器
 */
fun <T> BaseQuickAdapter<T, *>.updateData(
    data: ListUiData<T>,
    refreshLayout: RefreshLayout? = null,
) {
    if (data.isSuccess) {
        val useAll: Boolean = data.allDatas.isNotEmpty()
        if (data.isRefresh) {
            refreshLayout?.finishRefresh()
            if (!useAll) {
                this.setList(data.datas)
            }
        } else {
            refreshLayout?.finishLoadMore()
            if (!useAll) {
                this.addData(data.datas)
            }
        }
        if (useAll) {
            this.setDiffNewData(data.allDatas)
        }
        refreshLayout?.setNoMoreData(!data.hasMore)
    } else {
        if (data.isRefresh) {
            refreshLayout?.finishRefresh(false)
        } else {
            refreshLayout?.finishLoadMore(false)
            refreshLayout?.setNoMoreData(true)
        }
    }
}

/**
 * AriticleAdapter更新收藏情况
 * @receiver BaseQuickAdapter<AriticleInfo, *>
 * @param ids List<String>? 已收藏的文章ID集合
 */
fun BaseQuickAdapter<AriticleInfo, *>.updateCollect(ids: List<String>?) {
    val datas: MutableList<AriticleInfo> = this.data.toMutableList()
    if (ids != null && ids.isNotEmpty()) {
        ids.forEach { id ->
            val info: AriticleInfo? = datas.firstOrNull {
                it.id == id.toInt()
            }
            info?.collect = true
        }
    } else {
        datas.forEach {
            it.collect = false
        }
    }
    this.setDiffNewData(datas)
}

/**
 * AriticleAdapter更新收藏情况
 * @receiver BaseQuickAdapter<AriticleInfo, *>
 * @param id Int    文章ID
 * @param isCollected Boolean   是否收藏
 */
fun BaseQuickAdapter<AriticleInfo, *>.updateCollect(id: Int, isCollected: Boolean) {
    val info: AriticleInfo = this.data.firstOrNull {
        it.id == id
    } ?: return
    info.collect = isCollected
    this.setData(this.data.indexOf(info), info)
}

/**
 * AriticleAdapter初始化
 * @receiver BaseQuickAdapter<AriticleInfo, *>
 * @param fragment Fragment
 * @param onItemClick Function3<[@kotlin.ParameterName] BaseQuickAdapter<*, *>, [@kotlin.ParameterName] View, [@kotlin.ParameterName] Int, Unit>? 条目点击回调,如果为空,则默认跳转WebviewFragment
 * @param childClickIds List<Int>?  要点击的子view的ID合集
 * @param onItemChildClick Function3<[@kotlin.ParameterName] BaseQuickAdapter<*, *>, [@kotlin.ParameterName] View, [@kotlin.ParameterName] Int, Unit>? 条目子view点击回调,如果为空,则默认收藏或取消收藏
 * @param collectViewModel CollectViewModel?    收藏的ViewModel
 */
fun BaseQuickAdapter<AriticleInfo, *>.init(
    fragment: Fragment,
    onItemClick: ((adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit)? = null,
    childClickIds: IntArray? = null,
    onItemChildClick: ((adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit)? = null,
    collectViewModel: CollectViewModel? = null
) {
    this.setOnItemClickListener { adapter, view, position ->
        if (onItemClick != null) {
            onItemClick(adapter, view, position)
        } else {
            val info: AriticleInfo = this.getItem(position)
            fragment.nav().navigateAction(
                R.id.action_global_webviewFragment,
                WebviewFragment.makeData(WebviewFragment.TYPE_ARITICLE, info)
            )
        }
    }
    childClickIds?.let {
        if (childClickIds.isEmpty()) {
            return
        }
        this.addChildClickViewIds(*childClickIds)
        this.setOnItemChildClickListener { adapter, view, position ->
            if (onItemChildClick != null) {
                onItemChildClick(adapter, view, position)
            } else {
                when (view.id) {
                    //收藏
                    R.id.btn_collect -> {
                        fragment.checkToLogin({
                            val info: AriticleInfo = this.getItem(position)
                            if (info.collect) {
                                collectViewModel?.uncollect(info.id)
                            } else {
                                collectViewModel?.collect(info.id)
                            }
                        }, {
                            toast(R.string.hint_login_please)
                        })
                    }
                    //用户详情
                    R.id.tv_author -> {
                        fragment.nav()
                            .navigateAction(
                                R.id.action_global_userDetailFragment,
                                UserDetailFragment.makeData(this.getItem(position).userId)
                            )
                    }
                }
            }
        }
    }
}

/**
 * 更新AutoSize的转换,用于修正item大小会变的问题
 * @receiver BaseQuickAdapter<*, *>
 */
fun BaseQuickAdapter<*, *>.updateAutoSizeConvert() {
    val a: Activity? = this.recyclerView.context as? Activity
    a?.run {
        AutoSize.autoConvertDensityOfGlobal(a)
    }
}