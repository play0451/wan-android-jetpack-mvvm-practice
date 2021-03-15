package com.gw.mvvm.wan.ui.adapter.common

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gw.mvvm.framework.ext.utils.toHtml
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.ui.databinding.ItemAriticleBindingData
import com.gw.mvvm.wan.databinding.ItemAriticleBinding
import com.gw.mvvm.wan.databinding.ItemProjectBinding
import com.gw.mvvm.wan.ext.updateAutoSizeConvert

/**
 * 文章Adapter
 * @author play0451
 */
class AriticleAdapter(datas: MutableList<AriticleInfo>, private val isShowTag: Boolean = true) :
    BaseDelegateMultiAdapter<AriticleInfo, BaseViewHolder>(datas) {

    companion object {
        private const val ITEM_TYPE_ARITICLE: Int = 0
        private const val ITEM_TYPE_PROJECT: Int = 1
    }

    init {
        val delegate: BaseMultiTypeDelegate<AriticleInfo> =
            object : BaseMultiTypeDelegate<AriticleInfo>() {
                override fun getItemType(data: List<AriticleInfo>, position: Int): Int {
                    //根据projectLink是否为空来判断是文章还是项目
                    return if (data[position].projectLink.isBlank()) ITEM_TYPE_ARITICLE else ITEM_TYPE_PROJECT
                }
            }
        delegate.addItemType(ITEM_TYPE_ARITICLE, R.layout.item_ariticle)
        delegate.addItemType(ITEM_TYPE_PROJECT, R.layout.item_project)

        setMultiTypeDelegate(delegate)

        setAnimationWithDefault(AnimationType.AlphaIn)
        isAnimationFirstOnly = true

        setDiffCallback(AriticleDiffCallback())
    }

    override fun convert(holder: BaseViewHolder, item: AriticleInfo) {
        item.run {
            val bindingData: ItemAriticleBindingData = ItemAriticleBindingData(
                author = if (author.isBlank()) shareUser else author,
                content = title.toHtml(),
                type = "$superChapterName·$chapterName".toHtml(),
                date = niceDate,
                tag = if (tags.isEmpty()) "" else tags[0].name,
                isFresh = isShowTag && fresh,
                isTop = isShowTag && type == 1,
                hasTag = isShowTag && tags.isNotEmpty(),
                isCollected = MutableLiveData(collect)
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
        updateAutoSizeConvert()
    }

    override fun convert(holder: BaseViewHolder, item: AriticleInfo, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            return;
        }
        val bundle: Bundle = payloads[0] as Bundle
        when (holder.itemViewType) {
            ITEM_TYPE_PROJECT -> {
                val binding: ItemProjectBinding? = DataBindingUtil.bind(holder.itemView)
                binding?.ariticleData?.isCollected?.value =
                    bundle.getBoolean(AriticleDiffCallback.KEY_COLLECTED)
                binding?.executePendingBindings()
            }
            else -> {
                val binding: ItemAriticleBinding? = DataBindingUtil.bind(holder.itemView)
                binding?.ariticleData?.isCollected?.value =
                    bundle.getBoolean(AriticleDiffCallback.KEY_COLLECTED)
                binding?.executePendingBindings()
            }
        }
        updateAutoSizeConvert()
    }
}