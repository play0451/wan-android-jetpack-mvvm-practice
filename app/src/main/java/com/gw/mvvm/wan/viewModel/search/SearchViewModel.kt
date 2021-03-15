package com.gw.mvvm.wan.viewModel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.launch
import com.gw.mvvm.framework.ext.viewmodel.request
import com.gw.mvvm.framework.network.state.ResultState
import com.gw.mvvm.wan.data.database.AppDatabase
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.data.model.SearchHistoryInfo
import com.gw.mvvm.wan.data.model.SearchHotKeyInfo
import com.gw.mvvm.wan.data.repository.SearchRepository
import com.gw.mvvm.wan.data.ui.ListUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 搜索的ViewModel
 * @author play0451
 */
class SearchViewModel : BaseViewModel() {

    private var pageIndex: Int = 0
    private var allResults: MutableList<AriticleInfo> = mutableListOf()

    private val repository: SearchRepository by lazy { SearchRepository() }

    val hotKeys: MutableLiveData<ResultState<ArrayList<SearchHotKeyInfo>>> = MutableLiveData()
    val searchHistories: LiveData<List<SearchHistoryInfo>> by lazy {
        AppDatabase.instance.searchHistoryDato().getAllHistories()
    }
    val searchResult: MutableLiveData<ListUiData<AriticleInfo>> = MutableLiveData()

    /**
     * 获取搜索关键词
     */
    fun getHotKeys() {
        request({ repository.getHotKeys() }, hotKeys)
    }

    /**
     * 新增搜索记录
     * @param name String
     */
    fun addHistory(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var info: SearchHistoryInfo? =
                AppDatabase.instance.searchHistoryDato().getHistoryByName(name)
            if (info == null) {
                info = SearchHistoryInfo(name = name, date = System.currentTimeMillis())
                AppDatabase.instance.searchHistoryDato().insertHistories(info)
            } else {
                info.date = System.currentTimeMillis()
                AppDatabase.instance.searchHistoryDato().updateHistories(info)
            }
        }
    }

    /**
     * 根据ID删除搜索记录
     * @param id Long
     */
    fun deleteHistory(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.instance.searchHistoryDato().deleteHistoryById(id)
        }
    }

    /**
     * 清空搜索记录
     */
    fun deleteAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.instance.searchHistoryDato().deleteAllHistories()
        }
    }

    /**
     * 获取搜索结果
     * @param key String    关键词
     */
    fun getSearchResult(key: String, isRefresh: Boolean = false) {
        //刷新就把页码设为0
        if (isRefresh) {
            pageIndex = 0
            allResults.clear()
        }
        request({ repository.getSearchResult(pageIndex, key) }, {
            pageIndex++
            allResults.addAll(it.datas)
            searchResult.value = ListUiData.parsePageInfo(it, isRefresh, allResults.toMutableList())
        }, {
            searchResult.value = ListUiData.parseAppException(it, isRefresh)
        })
    }
}