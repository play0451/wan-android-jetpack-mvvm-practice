package com.gw.mvvm.wan.ui.adapter.common

import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil
import com.gw.mvvm.wan.data.model.AriticleInfo

/**
 * 文章数据的DiffUtil.ItemCallback
 * @author play0451
 */
class AriticleDiffCallback : DiffUtil.ItemCallback<AriticleInfo>() {

    companion object {
        const val KEY_COLLECTED: String = "collected"
    }

    override fun areItemsTheSame(oldItem: AriticleInfo, newItem: AriticleInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AriticleInfo, newItem: AriticleInfo): Boolean {
        return oldItem.collect == newItem.collect
    }

    override fun getChangePayload(oldItem: AriticleInfo, newItem: AriticleInfo): Any? {
        return if (oldItem.collect != newItem.collect) bundleOf(KEY_COLLECTED to newItem.collect) else null
    }
}