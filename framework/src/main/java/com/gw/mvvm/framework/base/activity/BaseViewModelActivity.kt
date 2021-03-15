package com.gw.mvvm.framework.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.getViewModelClazz

/**
 * 具有ViewModel的Activity基类
 */
abstract class BaseViewModelActivity<VM : BaseViewModel> : AppCompatActivity() {

    lateinit var mViewModel: VM

    /**
     * 获取布局ID
     */
    abstract fun layoutId(): Int

    /**
     * 初始化View
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化观察者
     */
    abstract fun initObserver()

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 显示loading
     */
    open fun showLoading(msg: String = "Loading") {}

    /**
     * 隐藏loading
     */
    open fun hideLoading() {}

    /**
     * 是否使用LoadingEvent,默认Ture,禁用的话不会接受BaseViewModel的LoadingEvent事件
     * @return Boolean
     */
    open fun useLoadingEvent(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createView()
        innerInit(savedInstanceState)
    }

    protected open fun createView() {
        val id: Int = layoutId()
        if (id <= 0) {
            return
        }
        setContentView(id)
    }

    protected open fun innerInit(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        if (useLoadingEvent()) {
            registerLoadingEvent(mViewModel)
        }
        initView(savedInstanceState)
        initObserver()
        initData()
    }

    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getViewModelClazz(this))
    }

    /**
     * 注册Loading事件,如果不是此activity绑定的viewModel,则需要调用此方法传入相应的viewModel
     * 否则BaseViewModel提供的loading事件将不会生效
     */
    protected open fun registerLoadingEvent(vararg viewModels: VM) {
        viewModels.forEach { vm ->
            vm.loadingEvent.showLoading.observeInActivity(this) { t -> showLoading(t) }
            vm.loadingEvent.hideLoading.observeInActivity(this) { hideLoading() }
        }
    }
}