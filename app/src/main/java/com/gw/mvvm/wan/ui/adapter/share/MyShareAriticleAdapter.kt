package com.gw.mvvm.wan.ui.adapter.share

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.databinding.ItemMyShareAriticleBinding

/**
 * 我的分享Adapter
 * @author play0451
 */
class MyShareAriticleAdapter(datas: MutableList<AriticleInfo>) :
    BaseQuickAdapter<AriticleInfo, BaseDataBindingHolder<ItemMyShareAriticleBinding>>(
        R.layout.item_my_share_ariticle, datas
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemMyShareAriticleBinding>,
        item: AriticleInfo
    ) {
        holder.dataBinding?.info = item
        holder.dataBinding?.executePendingBindings()
    }
}