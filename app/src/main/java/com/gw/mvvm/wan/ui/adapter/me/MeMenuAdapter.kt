package com.gw.mvvm.wan.ui.adapter.me

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.enum.MeMenuType
import com.gw.mvvm.wan.data.model.MeMenuInfo
import com.gw.mvvm.wan.databinding.ItemMeMenuCoinBinding
import com.gw.mvvm.wan.databinding.ItemMeMenuNormalBinding

/**
 * 我的菜单Adapter
 * @author play0451
 */
class MeMenuAdapter(datas: MutableList<MeMenuInfo>) :
    BaseDelegateMultiAdapter<MeMenuInfo, BaseViewHolder>(datas) {

    init {
        val delegate: BaseMultiTypeDelegate<MeMenuInfo> =
            object : BaseMultiTypeDelegate<MeMenuInfo>() {
                override fun getItemType(data: List<MeMenuInfo>, position: Int): Int {
                    return data[position].type.ordinal
                }
            }
        delegate.addItemType(MeMenuType.Normal.ordinal, R.layout.item_me_menu_normal)
        delegate.addItemType(MeMenuType.Coin.ordinal, R.layout.item_me_menu_coin)
        delegate.addItemType(MeMenuType.Decoration.ordinal, R.layout.item_me_menu_decoration)

        setMultiTypeDelegate(delegate)
    }

    override fun convert(holder: BaseViewHolder, item: MeMenuInfo) {
        when (holder.itemViewType) {
            MeMenuType.Normal.ordinal -> {
                val binding: ItemMeMenuNormalBinding? = DataBindingUtil.bind(holder.itemView)
                binding?.info = item
            }
            MeMenuType.Coin.ordinal -> {
                val binding: ItemMeMenuCoinBinding? = DataBindingUtil.bind(holder.itemView)
                binding?.info = item
            }
            MeMenuType.Decoration.ordinal -> {

            }
        }
    }
}