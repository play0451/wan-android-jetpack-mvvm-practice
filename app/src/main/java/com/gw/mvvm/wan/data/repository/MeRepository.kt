package com.gw.mvvm.wan.data.repository

import com.gw.mvvm.framework.core.appContext
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.network.ApiResponse
import com.gw.mvvm.wan.core.network.apiService
import com.gw.mvvm.wan.data.enum.MeMenuType
import com.gw.mvvm.wan.data.model.MeMenuInfo
import com.gw.mvvm.wan.data.model.UserCoinInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 我的数据仓库
 * @author play0451
 */
class MeRepository {
    /**
     * 获取当前用户积分
     * @return ApiResponse<UserCoinInfo>
     */
    suspend fun getUserCoin(): ApiResponse<UserCoinInfo> {
        return withContext(Dispatchers.IO) {
            apiService.getUserCoin()
        }
    }

    /**
     * 获取菜单列表
     * @return MutableList<MeMenuInfo>
     */
    suspend fun getMenuList(): MutableList<MeMenuInfo> {
        return mutableListOf(
            MeMenuInfo(
                id = 0,
                type = MeMenuType.Coin,
                title = appContext.getString(R.string.me_menu_coin),
                icon = R.drawable.icon_coin,
                action = MeMenuInfo.ACTION_COIN
            ),
            MeMenuInfo(
                id = 1,
                type = MeMenuType.Normal,
                title = appContext.getString(R.string.me_menu_collection),
                icon = R.drawable.icon_collected,
                action = MeMenuInfo.ACTION_COLLETION
            ),
            MeMenuInfo(
                id = 2,
                type = MeMenuType.Normal,
                title = appContext.getString(R.string.me_menu_ariticle),
                icon = R.drawable.icon_ariticle,
                action = MeMenuInfo.ACTION_ARITICLE
            ),
            MeMenuInfo(
                id = 3,
                type = MeMenuType.Normal,
                title = appContext.getString(R.string.me_menu_todo),
                icon = R.drawable.icon_todo,
                action = MeMenuInfo.ACTION_TODO
            ),
            MeMenuInfo(
                id = 4,
                type = MeMenuType.Decoration,
                clickable = false
            ),
            MeMenuInfo(
                id = 5,
                type = MeMenuType.Normal,
                title = appContext.getString(R.string.me_menu_site),
                icon = R.drawable.icon_site,
                action = MeMenuInfo.ACTION_SITE
            ),
            MeMenuInfo(
                id = 6,
                type = MeMenuType.Normal,
                title = appContext.getString(R.string.me_menu_settings),
                icon = R.drawable.icon_settings,
                action = MeMenuInfo.ACTION_SETTINGS
            )
        )
    }
}