package com.gw.mvvm.wan.ext

import com.chad.library.adapter.base.BaseQuickAdapter
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.ui.CollectUiData
import com.gw.mvvm.wan.viewModel.app.GlobalEventViewModel
import splitties.toast.toast

/**
 * 数据扩展
 * @author play0451
 */

/**
 * 处理收藏结果
 * @receiver CollectUiData  收藏数据
 * @param ariticleAdapter BaseQuickAdapter<AriticleInfo, *> ariticleAdapter
 */
fun CollectUiData.dealCollectEvent(
    ariticleAdapter: BaseQuickAdapter<AriticleInfo, *>,
    showToast: Boolean = true
) {
    if (!this.isSuccess) {
        if (this.errorMsg.isNotBlank() && showToast) {
            toast(this.errorMsg)
        }
    } else {
        val info: AriticleInfo = ariticleAdapter.data.firstOrNull { i ->
            i.id == this.id
        } ?: return

        info.collect = this.isCollect
        ariticleAdapter.setData(ariticleAdapter.data.indexOf(info), info)
        if (showToast) {
            toast(if (this.isCollect) R.string.hint_collect_success else R.string.hint_uncollect_success)
        }
    }
}

/**
 * 处理收藏结果,发送收藏事件
 * @receiver CollectUiData
 * @param eventViewModel GlobalEventViewModel
 */
fun CollectUiData.dealCollectEvent(
    eventViewModel: GlobalEventViewModel,
    showToast: Boolean = true,
    onSuccess: (() -> Unit)? = null,
    onFailed: (() -> Unit)? = null
) {
    if (!this.isSuccess) {
        if (this.errorMsg.isNotBlank() && showToast) {
            toast(this.errorMsg)
        }
        onFailed?.invoke()
    } else {
        if (showToast) {
            toast(if (this.isCollect) R.string.hint_collect_success else R.string.hint_uncollect_success)
        }
        onSuccess?.invoke()
        eventViewModel.sendCollectAriticle(this.id, this.isCollect)
    }
}