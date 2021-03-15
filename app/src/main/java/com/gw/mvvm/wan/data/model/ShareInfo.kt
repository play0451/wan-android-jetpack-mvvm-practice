package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *用户分享信息
 * @author play0451
 */
@Parcelize
data class ShareInfo(
    var coinInfo: CoinInfo,
    var shareArticles: PageInfo<ArrayList<AriticleInfo>>
) : Parcelable