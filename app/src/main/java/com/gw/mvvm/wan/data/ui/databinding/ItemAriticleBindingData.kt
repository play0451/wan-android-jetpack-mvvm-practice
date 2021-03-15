package com.gw.mvvm.wan.data.ui.databinding

import androidx.lifecycle.MutableLiveData

/**
 * 文章条目数据
 * @author play0451
 */
data class ItemAriticleBindingData(
    /**
     * 作者
     */
    var author: String = "",
    /**
     * 内容
     */
    var content: CharSequence = "",
    /**
     * 类型
     */
    var type: CharSequence = "",
    /**
     * 日期
     */
    var date: String = "",
    /**
     * TAG
     */
    var tag: String = "",
    /**
     * 是否最新
     */
    var isFresh: Boolean = false,
    /**
     * 是否置顶
     */
    var isTop: Boolean = false,
    /**
     * 是否有TAG
     */
    var hasTag: Boolean = false,
    /**
     * 是否已收藏
     */
    var isCollected: MutableLiveData<Boolean> = MutableLiveData(false),
    /**
     * 项目标题
     */
    var title: CharSequence = "",
    /**
     * 项目图片
     */
    var imgUlr: String = ""
)