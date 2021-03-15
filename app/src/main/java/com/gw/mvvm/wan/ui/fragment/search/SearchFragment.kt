package com.gw.mvvm.wan.ui.fragment.search

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.KeyboardUtils
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.framework.ext.view.onClick
import com.gw.mvvm.framework.ext.viewmodel.parseState
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.data.model.SearchHistoryInfo
import com.gw.mvvm.wan.databinding.FragmentSearchBinding
import com.gw.mvvm.wan.ext.init
import com.gw.mvvm.wan.ui.adapter.search.SearchHistoryAdapter
import com.gw.mvvm.wan.ui.adapter.search.SearchHotKeyAdapter
import com.gw.mvvm.wan.viewModel.search.SearchViewModel

/**
 * 搜索页面
 * @property hotKeyAdapter SearchHotKeyAdapter
 * @property historyAdapter SearchHistoryAdapter
 */
class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>() {

    private lateinit var hotKeyAdapter: SearchHotKeyAdapter
    private lateinit var historyAdapter: SearchHistoryAdapter

    override fun layoutId(): Int = R.layout.fragment_search

    override fun initView(savedInstanceState: Bundle?) {
        hotKeyAdapter = SearchHotKeyAdapter(mutableListOf())
        historyAdapter = SearchHistoryAdapter(mutableListOf())

        mDataBinding.let {
            it.tvClearAll.onClick {
                deleteAllHsitory()
            }
            it.svSearch.run {
                onActionViewExpanded()
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        query?.let { q ->
                            if (q.isNotBlank()) {
                                search(q, true)
                            }
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }
            it.tvCancel.onClick {
                KeyboardUtils.hideSoftInput(requireActivity())
                nav().navigateUp()
            }
            val layoutManager: FlexboxLayoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            it.rvHotSearch.init(layoutManager, hotKeyAdapter, null, false)
            hotKeyAdapter.setOnItemClickListener { _, _, position ->
                search(hotKeyAdapter.getItem(position).name)
            }

            it.rvSearchHistory.init(
                LinearLayoutManager(requireContext()),
                historyAdapter,
                null,
                false
            )
            historyAdapter.apply {
                setOnItemClickListener { _, _, position ->
                    search(historyAdapter.getItem(position).name, position != 0)
                }
                addChildClickViewIds(R.id.iv_delete)
                setOnItemChildClickListener { _, _, position ->
                    deleteHistory(historyAdapter.getItem(position))
                }
            }
        }
    }

    override fun initObserver() {
        mViewModel.hotKeys.observe(viewLifecycleOwner, {
            parseState(it, { list ->
                hotKeyAdapter.setList(list)
            })
        })
        mViewModel.searchHistories.observe(viewLifecycleOwner, {
            historyAdapter.setDiffNewData(it.toMutableList())
        })
    }

    override fun initData() {
        mViewModel.getHotKeys()
    }

    private fun updateSearchHistory(str: String) {
        mViewModel.addHistory(str)
    }

    private fun search(key: String, updateHistory: Boolean = true) {
        if (key.isBlank()) {
            return
        }
        KeyboardUtils.hideSoftInput(requireActivity())
        if (updateHistory) {
            updateSearchHistory(key)
        }
        nav().navigateAction(
            R.id.action_searchFragment_to_searchResultFragment,
            SearchResultFragment.makeData(key)
        )
    }

    private fun deleteHistory(info: SearchHistoryInfo) {
        mViewModel.deleteHistory(info.id)
    }

    private fun deleteAllHsitory() {
        MaterialDialog(requireContext()).show {
            cancelable(true)
            title(R.string.text_hint)
            message(R.string.hint_search_delete_all_history)
            positiveButton(R.string.text_sure) {
                mViewModel.deleteAllHistory()
            }
            negativeButton(R.string.text_cancel)
        }
    }
}