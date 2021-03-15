package com.gw.mvvm.wan.ui.fragment.square

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.framework.ext.navigation.navigateUp
import com.gw.mvvm.framework.ext.viewmodel.parseState
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.model.SquareNavigationInfo
import com.gw.mvvm.wan.databinding.FragmentSquareNavigationBinding
import com.gw.mvvm.wan.ext.*
import com.gw.mvvm.wan.ui.adapter.square.SquareNavigationAdapter
import com.gw.mvvm.wan.ui.adapter.square.SquareNavigationTitleAdapter
import com.gw.mvvm.wan.ui.fragment.web.WebviewFragment
import com.gw.mvvm.wan.viewModel.square.SquareViewModel
import com.kingja.loadsir.core.LoadService
import splitties.resources.str

/**
 * 广场导航Fragment
 */
class SquareNavigationFragment : BaseFragment<SquareViewModel, FragmentSquareNavigationBinding>() {

    //loading界面管理
    private lateinit var loadsir: LoadService<Any>
    private lateinit var contentAdapter: SquareNavigationAdapter
    private lateinit var titleAdapter: SquareNavigationTitleAdapter
    private var currentTitleIndex: Int = 0

    override fun layoutId(): Int = R.layout.fragment_square_navigation

    override fun initView(savedInstanceState: Bundle?) {
        contentAdapter = SquareNavigationAdapter(mutableListOf(), this::onChildClick)
        titleAdapter = SquareNavigationTitleAdapter(mutableListOf())
        titleAdapter.setOnItemClickListener { _, _, pos ->
            updateTitleSelected(pos)
            mDataBinding.rvContent.scrollToPosition(currentTitleIndex)
        }

        mDataBinding.let {
            it.includeToolbar.toolbar.initClose(str(R.string.square_system)) {
                navigateUp()
            }

            //初始化loading界面管理
            loadsir = initLoadServiceWithReloadCallback(it.llContainer) {
                initData()
            }

            it.rvTitle.init(
                LinearLayoutManager(requireContext()),
                titleAdapter
            )
            it.rvContent.init(
                LinearLayoutManager(requireContext()),
                contentAdapter
            )
            it.rvContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val layoutManager: LinearLayoutManager =
                            recyclerView.layoutManager as LinearLayoutManager
                        val a: Int = layoutManager.findFirstCompletelyVisibleItemPosition()
                        updateTitleSelected(a)
                        it.rvTitle.scrollToPosition(a)
                    }
                }
            })
        }
    }

    override fun initObserver() {
        mViewModel.navigationList.observe(viewLifecycleOwner, {
            parseState(it, { list ->
                if (list.isNullOrEmpty()) {
                    loadsir.showEmpty()
                    return@parseState
                }
                val titleList: MutableList<Pair<String, Boolean>> = list.mapIndexed { index, info ->
                    info.name to (index == 0)
                }.toMutableList()
                titleAdapter.setList(titleList)
                contentAdapter.setList(list)
                loadsir.showSuccess()
            }, {
                loadsir.showError()
            })
        })
    }

    override fun initData() {
        loadsir.showLoading()
        mViewModel.getNavigationList()
    }

    private fun onChildClick(item: SquareNavigationInfo, childPosition: Int) {
        val info: AriticleInfo = item.articles[childPosition]
        nav().navigateAction(
            R.id.action_global_webviewFragment,
            WebviewFragment.makeData(WebviewFragment.TYPE_ARITICLE, info, false)
        )
    }

    private fun updateTitleSelected(pos: Int) {
        if (pos < 0 || pos > titleAdapter.itemCount) {
            return
        }
        var pair: Pair<String, Boolean> = titleAdapter.getItem(currentTitleIndex)
        var np: Pair<String, Boolean> = pair.first to false
        titleAdapter.setData(currentTitleIndex, np)
        pair = titleAdapter.getItem(pos)
        np = pair.first to true
        currentTitleIndex = pos
        titleAdapter.setData(currentTitleIndex, np)
    }
}