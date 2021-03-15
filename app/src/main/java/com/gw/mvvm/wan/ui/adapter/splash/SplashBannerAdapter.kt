package com.gw.mvvm.wan.ui.adapter.splash

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gw.mvvm.framework.core.appContext
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.databinding.ItemSplashBannerBinding
import com.youth.banner.adapter.BannerAdapter

/**
 * @author play0451
 */
class SplashBannerAdapter(datas: List<Int>) :
    BannerAdapter<Int, SplashBannerAdapter.ViewHolder>(datas) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(appContext).inflate(R.layout.item_splash_banner, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun onBindView(holder: ViewHolder?, data: Int?, position: Int, size: Int) {
        if (holder != null && data != null) {
            holder.binding?.ivImage?.setImageResource(data)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: ItemSplashBannerBinding? = DataBindingUtil.bind(view)
            private set
    }
}