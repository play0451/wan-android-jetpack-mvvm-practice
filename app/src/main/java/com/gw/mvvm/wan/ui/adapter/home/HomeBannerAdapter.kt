package com.gw.mvvm.wan.ui.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gw.mvvm.framework.core.appContext
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.BannerInfo
import com.gw.mvvm.wan.databinding.ItemHomeBannerBinding
import com.youth.banner.adapter.BannerAdapter

/**
 * @author play0451
 */
class HomeBannerAdapter(datas: List<BannerInfo>) :
    BannerAdapter<BannerInfo, HomeBannerAdapter.ViewHolder>(datas) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(appContext).inflate(R.layout.item_home_banner, parent, false)
        return ViewHolder(view)
    }

    override fun onBindView(holder: ViewHolder?, data: BannerInfo?, position: Int, size: Int) {
        if (holder != null && data != null) {
            holder.binding?.let {
                it.imageUrl = data.imagePath
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemHomeBannerBinding? = DataBindingUtil.bind(view)
    }
}