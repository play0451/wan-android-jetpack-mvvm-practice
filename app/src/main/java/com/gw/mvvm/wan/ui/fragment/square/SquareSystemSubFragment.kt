package com.gw.mvvm.wan.ui.fragment.square

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gw.mvvm.framework.ext.navigation.navigateUp
import com.gw.mvvm.framework.ext.utils.toHtml
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.data.model.SquareSystemInfo
import com.gw.mvvm.wan.databinding.FragmentSpinnerRefreshListBinding
import com.gw.mvvm.wan.ext.*
import com.gw.mvvm.wan.ui.adapter.common.AriticleAdapter
import com.gw.mvvm.wan.viewModel.collect.CollectViewModel
import com.gw.mvvm.wan.viewModel.square.SquareViewModel
import com.kingja.loadsir.core.LoadService

/**
 * 广场项目子Fragment
 */
class SquareSystemSubFragment : BaseFragment<SquareViewModel, FragmentSpinnerRefreshListBinding>(),
    AdapterView.OnItemSelectedListener {

    private var systemInfo: SquareSystemInfo? = null
    private var childPosition: Int = 0

    //loading界面管理
    private lateinit var loadsir: LoadService<Any>
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var ariticleAdapter: AriticleAdapter
    private var titleList: MutableList<Pair<Int, String>> = mutableListOf()
    private val collectViewModel: CollectViewModel by viewModels()

    companion object {
        const val KEY_SYSTEM_INFO: String = "systemInfo"
        const val KEY_CHILD_POSITION: String = "childPosition"

        fun makeData(systemInfo: SquareSystemInfo, childPosition: Int): Bundle {
            return bundleOf(KEY_SYSTEM_INFO to systemInfo, KEY_CHILD_POSITION to childPosition)
        }
    }

    override fun layoutId(): Int = R.layout.fragment_spinner_refresh_list

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            systemInfo = it.getParcelable(KEY_SYSTEM_INFO)
            childPosition = it.getInt(KEY_CHILD_POSITION)
        }
        spinnerAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item,
            mutableListOf()
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        ariticleAdapter = AriticleAdapter(mutableListOf(), true)

        mDataBinding.let {

            it.includeToolbar.toolbar.initClose(systemInfo?.name) {
                navigateUp()
            }

            loadsir = initLoadServiceWithReloadCallback(it.llContent) {
                mDataBinding.includeRefreshList.layoutRefresh.autoRefresh()
            }

            it.spinner.apply {
                adapter = spinnerAdapter
                onItemSelectedListener = this@SquareSystemSubFragment
            }

            it.includeRefreshList.layoutRefresh.init({
                systemInfo?.let {
                    mViewModel.getSystemSubChildList(systemInfo!!.children[childPosition].id, true)
                }
            }, {
                systemInfo?.let {
                    mViewModel.getSystemSubChildList(systemInfo!!.children[childPosition].id, false)
                }
            })

            it.includeRefreshList.includeList.viewRecycler.init(
                LinearLayoutManager(requireContext()),
                ariticleAdapter,
                null,
                true,
                it.includeRefreshList.includeList.btnFloating
            )
        }

        //初始化AriticleAdapter
        ariticleAdapter.init(
            this@SquareSystemSubFragment,
            null,
            intArrayOf(R.id.btn_collect, R.id.tv_author),
            null,
            collectViewModel
        )

        //初始化Spinner
        systemInfo?.let {
            titleList = (systemInfo!!.children.map { info ->
                info.id to info.name
            }).toMutableList()
            val l: MutableList<String> = titleList.map { p ->
                p.second.toHtml().toString()
            }.toMutableList()
            spinnerAdapter.addAll(l)

            titleList.forEachIndexed { index, pair ->
                if (pair.first == systemInfo!!.children[childPosition].id) {
                    mDataBinding.spinner.setSelection(index)
                    return@forEachIndexed
                }
            }
        }
    }

    override fun initObserver() {
        //项目列表
        mViewModel.systemSubChildList.observe(viewLifecycleOwner, {
            loadsir.dealResult(it)
            ariticleAdapter.updateData(it, mDataBinding.includeRefreshList.layoutRefresh)
        })
        //用户数据,用户登录登出后更新收藏状态
        mGlobalEventViewModel.loginOutEvent.observeInFragment(this) {
            ariticleAdapter.updateCollect(GlobalData.userInfo?.collectIds)
        }
        //收藏结果
        collectViewModel.collectEvent.observe(viewLifecycleOwner, {
            it.dealCollectEvent(mGlobalEventViewModel, onSuccess = {
                requireContext().sendAriticleCollectNotification(
                    ariticleAdapter.data,
                    it.id,
                    it.isCollect
                )
            })
        })
        //收藏事件
        mGlobalEventViewModel.collectAriticleEvent.observeInFragment(this) {
            ariticleAdapter.updateCollect(it.data!!.id, it.data!!.isCollected)
        }
    }

    override fun initData() {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        childPosition = position
        mDataBinding.includeRefreshList.layoutRefresh.autoRefresh()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}