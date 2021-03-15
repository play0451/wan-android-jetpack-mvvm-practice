package com.gw.mvvm.wan.ui.adapter.collect

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gw.mvvm.framework.ext.utils.toHtml
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.CollectAriticleInfo
import com.gw.mvvm.wan.data.ui.databinding.ItemAriticleBindingData
import com.gw.mvvm.wan.databinding.ItemAriticleBinding
import com.gw.mvvm.wan.databinding.ItemProjectBinding

/**
 * 收藏文章Adapter
 * @author play0451
 */
class CollectAriticleAdapter(datas: MutableList<CollectAriticleInfo>) :
    BaseDelegateMultiAdapter<CollectAriticleInfo, BaseViewHolder>(datas) {

    companion object {
        private const val ITEM_TYPE_ARITICLE: Int = 0
        private const val ITEM_TYPE_PROJECT: Int = 1
    }

    init {
        val delegate: BaseMultiTypeDelegate<CollectAriticleInfo> =
            object : BaseMultiTypeDelegate<CollectAriticleInfo>() {
                override fun getItemType(data: List<CollectAriticleInfo>, position: Int): Int {
                    //根据envelopePic是否为空来判断是文章还是项目
                    return if (data[position].envelopePic.isBlank()) ITEM_TYPE_ARITICLE else ITEM_TYPE_PROJECT
                }
            }
        delegate.addItemType(ITEM_TYPE_ARITICLE, R.layout.item_ariticle)
        delegate.addItemType(ITEM_TYPE_PROJECT, R.layout.item_project)

        setMultiTypeDelegate(delegate)

        setAnimationWithDefault(AnimationType.AlphaIn)
        isAnimationFirstOnly = true
    }

    override fun convert(holder: BaseViewHolder, item: CollectAriticleInfo) {
        item.run {
            val bindingData: ItemAriticleBindingData = ItemAriticleBindingData(
                author = if (author.isBlank()) holder.itemView.context.getString(R.string.collect_anonymous) else author,
                content = title.toHtml(),
                type = "$chapterName".toHtml(),
                date = niceDate,
                isFresh = false,
                isTop = false,
                hasTag = false,
                isCollected = MutableLiveData(collected)
            )
            when (holder.itemViewType) {
                ITEM_TYPE_PROJECT -> {
                    val binding: ItemProjectBinding? = DataBindingUtil.bind(holder.itemView)
                    bindingData.title = desc.toHtml()
                    bindingData.imgUlr = envelopePic
                    binding?.ariticleData = bindingData
                    binding?.executePendingBindings()
                }
                else -> {
                    val binding: ItemAriticleBinding? = DataBindingUtil.bind(holder.itemView)
                    binding?.ariticleData = bindingData
                    binding?.executePendingBindings()
                }
            }
        }
    }
}