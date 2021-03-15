package com.gw.mvvm.wan.ui.fragment.project

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gw.mvvm.framework.ext.navigation.navigateUp
import com.gw.mvvm.framework.ext.utils.toHtml
import com.gw.mvvm.framework.ext.viewmodel.parseState
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.databinding.FragmentSpinnerRefreshListBinding
import com.gw.mvvm.wan.ext.*
import com.gw.mvvm.wan.ui.adapter.common.AriticleAdapter
import com.gw.mvvm.wan.util.MmkvUtil
import com.gw.mvvm.wan.viewModel.collect.CollectViewModel
import com.gw.mvvm.wan.viewModel.project.ProjectViewModel
import com.kingja.loadsir.core.LoadService
import splitties.resources.str
import splitties.toast.toast

/**
 * 项目Fragment
 */
class ProjectFragment : BaseFragment<ProjectViewModel, FragmentSpinnerRefreshListBinding>(),
    AdapterView.OnItemSelectedListener {

    //loading界面管理
    private lateinit var loadsir: LoadService<Any>
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var ariticleAdapter: AriticleAdapter
    private var classifyId: Int = 0
    private var titleList: MutableList<Pair<Int, String>> = mutableListOf()
    private val collectViewModel: CollectViewModel by viewModels()

    companion object {
        private const val KEY_TITLE_ID: String = "projectTitleId"
    }

    override fun layoutId(): Int = R.layout.fragment_spinner_refresh_list

    override fun initView(savedInstanceState: Bundle?) {

        spinnerAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item,
            mutableListOf()
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        ariticleAdapter = AriticleAdapter(mutableListOf(), true)

        mDataBinding.let {

            it.includeToolbar.toolbar.initClose(str(R.string.menu_main_project)) {
                navigateUp()
            }

            loadsir = initLoadServiceWithReloadCallback(it.llContent) {
                initData()
            }

            it.spinner.apply {
                adapter = spinnerAdapter
                onItemSelectedListener = this@ProjectFragment
            }

            it.includeRefreshList.layoutRefresh.init({
                mViewModel.getProjectList(classifyId, classifyId == 0, true)
            }, {
                mViewModel.getProjectList(classifyId, classifyId == 0, false)
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
            this@ProjectFragment,
            null,
            intArrayOf(R.id.btn_collect, R.id.tv_author),
            null,
            collectViewModel
        )
    }

    override fun initObserver() {
        //标题列表
        mViewModel.titleList.observe(viewLifecycleOwner, { it ->
            parseState(it, { list ->
                titleList.add(0, 0 to str(R.string.project_new))
                titleList.addAll(list.map { info ->
                    info.id to info.name
                })
                val l: MutableList<String> = titleList.map { p ->
                    p.second.toHtml().toString()
                }.toMutableList()
                spinnerAdapter.addAll(l)

                //检查记录的标题并设置
                val id: Int = MmkvUtil.get(KEY_TITLE_ID, 0)
                titleList.forEachIndexed { index, pair ->
                    if (pair.first == id) {
                        mDataBinding.spinner.setSelection(index)
                        return@forEachIndexed
                    }
                }
                loadsir.showSuccess()
            }, {
                toast(it.errorMsg)
                loadsir.showError()
            })
        })
        //项目列表
        mViewModel.projectDatas.observe(viewLifecycleOwner, {
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
        loadsir.showLoading()
        mViewModel.getTitleList()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val p: Pair<Int, String> = titleList[position]
        classifyId = p.first
        mDataBinding.includeRefreshList.layoutRefresh.autoRefresh()
        //记录选择的标题
        MmkvUtil.set(KEY_TITLE_ID, p.first)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}