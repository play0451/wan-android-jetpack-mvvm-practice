package com.gw.mvvm.wan.ui.fragment.web

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.utils.toHtml
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.data.enum.CollectType
import com.gw.mvvm.wan.data.model.*
import com.gw.mvvm.wan.data.ui.CollectUiData
import com.gw.mvvm.wan.databinding.FragmentWebviewBinding
import com.gw.mvvm.wan.ext.dealCollectEvent
import com.gw.mvvm.wan.ext.initClose
import com.gw.mvvm.wan.ext.initWithNavIcon
import com.gw.mvvm.wan.viewModel.collect.CollectViewModel
import com.gw.mvvm.wan.viewModel.web.WebviewViewModel
import com.just.agentweb.AgentWeb
import splitties.resources.drawable
import splitties.resources.str
import splitties.toast.toast

class WebviewFragment : BaseFragment<WebviewViewModel, FragmentWebviewBinding>() {

    private var agentWeb: AgentWeb? = null
    private var preAgentWeb: AgentWeb.PreAgentWeb? = null
    private var isShowCollect: Boolean = true
    private var isShowReload: Boolean = true
    private var isShowShare: Boolean = true
    private var isShowOpenInBrowser: Boolean = true
    private val collectViewModel: CollectViewModel by viewModels()

    companion object {
        /**
         * 类型文章
         */
        const val TYPE_ARITICLE: String = "typeAriticle"

        /**
         * 类型Banner
         */
        const val TYPE_BANNER: String = "typeBanner"

        /**
         * 类型收藏
         */
        const val TYPE_COLLECT: String = "typeCollect"

        /**
         * 类型收藏网址
         */
        const val TYPE_COLLECT_URL: String = "typeCollectUrl"

        /**
         * 类型普通网页
         */
        const val TYPE_NORMAL_WEB: String = "typeNormalWeb"

        const val KEY_SHOW_COLLECT: String = "showCollect"
        const val KEY_SHOW_RELOAD: String = "showReload"
        const val KEY_SHOW_SHARE: String = "showShare"
        const val KEY_SHOW_OPEN_IN_BROWSER: String = "showOpenInBrowser"

        fun makeData(
            type: String,
            data: Parcelable,
            showCollect: Boolean = true,
            showReload: Boolean = true,
            showShare: Boolean = true,
            showOpenInBrowser: Boolean = true
        ): Bundle {
            return bundleOf(
                type to data,
                KEY_SHOW_COLLECT to showCollect,
                KEY_SHOW_RELOAD to showReload,
                KEY_SHOW_SHARE to showShare,
                KEY_SHOW_OPEN_IN_BROWSER to showOpenInBrowser
            )
        }
    }

    override fun layoutId(): Int = R.layout.fragment_webview

    override fun initView(savedInstanceState: Bundle?) {
        //获取数据
        arguments?.run {
            getParcelable<AriticleInfo>(TYPE_ARITICLE)?.let {
                mViewModel.apply {
                    ariticleId = it.id
                    title = it.title
                    collect = it.collect
                    url = it.link
                    collectType = CollectType.Ariticle
                }
            }
            getParcelable<BannerInfo>(TYPE_BANNER)?.let {
                mViewModel.apply {
                    ariticleId = it.id
                    title = it.title
                    collect = false //Banner过来的无法确定是否收藏了,所以默认否
                    url = it.url
                    collectType = CollectType.Url
                }
            }
            getParcelable<CollectAriticleInfo>(TYPE_COLLECT)?.let {
                mViewModel.apply {
                    ariticleId = it.originId
                    title = it.title
                    collect = it.collected
                    url = it.link
                    collectType = CollectType.Ariticle
                }
            }
            getParcelable<CollectUrlInfo>(TYPE_COLLECT_URL)?.let {
                mViewModel.apply {
                    ariticleId = it.id
                    title = it.name
                    collect = it.collected
                    url = it.link
                    collectType = CollectType.Url
                }
            }
            getParcelable<NormalWebInfo>(TYPE_NORMAL_WEB)?.let {
                mViewModel.apply {
                    title = it.title
                    url = it.url
                }
            }
            isShowCollect = getBoolean(KEY_SHOW_COLLECT, true)
            isShowReload = getBoolean(KEY_SHOW_RELOAD, true)
            isShowShare = getBoolean(KEY_SHOW_SHARE, true)
            isShowOpenInBrowser = getBoolean(KEY_SHOW_OPEN_IN_BROWSER, true)
        }
        //初始化Toolbar
        mDataBinding.includeToolbar.toolbar.run {
            //判断是否显示收藏,刷新,分享,用浏览器打开
            if (isShowCollect || isShowReload || isShowShare || isShowOpenInBrowser) {
                initWithNavIcon(
                    title = mViewModel.title.toHtml(),
                    navCallback = {
                        nav().navigateUp()
                    },
                    menuId = R.menu.menu_webview,
                    menuClickListener = this@WebviewFragment::onMenuClick
                )
                setMenuItemVisible(menu, R.id.item_collect, isShowCollect)
                setMenuItemVisible(menu, R.id.item_refresh, isShowReload)
                setMenuItemVisible(menu, R.id.item_share, isShowShare)
                setMenuItemVisible(menu, R.id.item_open_browser, isShowOpenInBrowser)
            } else {
                initClose(mViewModel.title) {
                    nav().navigateUp()
                }
            }
        }

        initPreAgentWeb()
    }

    private fun setMenuItemVisible(menu: Menu, itemId: Int, visible: Boolean) {
        menu.findItem(itemId)?.isVisible = visible
    }

    private fun initPreAgentWeb() {
        preAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(
                mDataBinding.llWebContainer,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            )
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
    }

    override fun initObserver() {
        //收藏结果
        when (mViewModel.collectType) {
            CollectType.Ariticle -> collectViewModel.collectEvent.observe(
                viewLifecycleOwner,
                this::dealCollect
            )
            CollectType.Url -> collectViewModel.collectUrlEvent.observe(
                viewLifecycleOwner,
                this::dealCollect
            )
        }
        //收藏事件
        mGlobalEventViewModel.collectAriticleEvent.observeInFragment(this) {
            updateMenuCollected(it.data!!.isCollected)
        }
    }

    private fun dealCollect(data: CollectUiData) {
        data.dealCollectEvent(mGlobalEventViewModel)
    }

    override fun initData() {

        updateMenuCollected(mViewModel.collect)

        agentWeb = preAgentWeb?.go(mViewModel.url)
        requireActivity().onBackPressedDispatcher.addCallback(this, true) {
            agentWeb?.let {
                if (it.webCreator.webView.canGoBack()) {
                    it.webCreator.webView.goBack()
                } else {
                    nav().navigateUp()
                }
            }
        }
    }

    private fun onMenuClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_collect -> {
                when (mViewModel.collectType) {
                    CollectType.Ariticle -> {
                        if (mViewModel.ariticleId <= 0) {
                            return true
                        }
                        if (mViewModel.collect) {
                            collectViewModel.uncollect(mViewModel.ariticleId)
                        } else {
                            collectViewModel.collect(mViewModel.ariticleId)
                        }
                    }
                    CollectType.Url -> {
                        if (mViewModel.collect) {
                            if (mViewModel.ariticleId <= 0) {
                                return true
                            }
                            collectViewModel.uncollectUrl(mViewModel.ariticleId)
                        } else {
                            collectViewModel.collectUrl(mViewModel.title, mViewModel.url)
                        }
                    }
                }

            }
            R.id.item_share -> {
                //分享
                startActivity(Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "{${mViewModel.title}}:${mViewModel.url}")
                    type = "text/plain"
                }, str(R.string.text_share_to)))
            }
            R.id.item_refresh -> {
                agentWeb?.urlLoader?.reload()
            }
            R.id.item_open_browser -> {
                //用浏览器打开
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mViewModel.url)))
            }
        }

        return true
    }

    private fun updateMenuCollected(bo: Boolean) {
        mViewModel.collect = bo
        mDataBinding.includeToolbar.toolbar.menu.findItem(R.id.item_collect)?.icon =
            drawable(if (bo) R.drawable.icon_collected else R.drawable.icon_collect)
    }

    override fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }
}