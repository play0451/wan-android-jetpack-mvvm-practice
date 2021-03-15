package com.gw.mvvm.wan.viewModel.web

import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.wan.data.enum.CollectType

/**
 * WebviewViewModel
 * @author play0451
 */
class WebviewViewModel : BaseViewModel() {
    /**
     * 是否收藏,默认false
     */
    var collect: Boolean = false

    /**
     * 收藏的Id
     */
    var ariticleId: Int = 0

    /**
     * 标题
     */
    var title: String = ""

    /**
     * 文章的网址
     */
    var url: String = ""

    /**
     * 收藏类型,默认CollectType.Ariticle
     */
    var collectType: CollectType = CollectType.Ariticle
}