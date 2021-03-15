package com.gw.mvvm.framework.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.getViewModelClazz

abstract class BaseViewModelFragment<VM : BaseViewModel> : Fragment() {

    /**
     * 是否第一次可见,只读
     */
    var isFirstVisible: Boolean = true
        private set

    protected lateinit var mViewModel: VM
        private set

    protected lateinit var mActivity: AppCompatActivity
        private set

    /**
     * 获取布局ID
     */
    protected abstract fun layoutId(): Int

    /**
     * 初始化View,在onViewCreated后调用,用来初始化view
     * 可能会被多次调用,例如在Fragment初始化,或重建自身或者重建view的时候会调用
     * 调用顺序onViewCreated(系统)->initView->registerLoadingEvent->initObserver->onCreateInitData
     */
    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化观察者,在onViewCreated后调用,用来初始化对数据的观察
     * 可能会被多次调用,例如在Fragment初始化,或重建自身或者重建view的时候会调用
     * 调用顺序onViewCreated(系统)->initView->registerLoadingEvent->initObserver->onCreateInitData
     */
    protected abstract fun initObserver()

    /**
     * 初始化数据,在fragment第一次可见后调用,只会调用一次
     */
    protected abstract fun initData()

    /**
     * 显示loading
     * @param msg String    loading内容,默认空字符串
     */
    open fun showLoading(msg: CharSequence = "") {}

    /**
     * 隐藏loading
     */
    open fun hideLoading() {}

    /**
     * 是否使用LoadingEvent,默认Ture,禁用的话不会接受BaseViewModel的LoadingEvent事件
     * @return Boolean
     */
    open fun useLoadingEvent(): Boolean = true

    /**
     * 初始化观察者,在onViewCreated后调用,用来初始化数据,推荐使用initData方法进行数据的延迟加载
     * 可能会被多次调用,例如在Fragment初始化,或重建自身或者重建view的时候会调用
     * 调用顺序onViewCreated(系统)->initView->initObserver->onCreateInitData
     */
    protected open fun onCreateInitData() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(inflater, container, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = requireActivity() as AppCompatActivity
    }

    /**
     * 创建View
     * @param inflater LayoutInflater
     * @param container ViewGroup?
     * @param savedInstanceState Bundle?
     * @return View?
     */
    protected open fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (layoutId() <= 0) {
            null
        } else {
            inflater.inflate(layoutId(), container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        innerInit(savedInstanceState)
    }

    protected open fun innerInit(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        initView(savedInstanceState)
        if (useLoadingEvent()) {
            registerLoadingEvent(mViewModel)
        }
        initObserver()
        onCreateInitData()
    }

    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getViewModelClazz(this))
    }

    /**
     * 注册Loading事件,如果不是此fragment绑定的viewModel,则需要调用此方法传入相应的viewModel
     * 否则BaseViewModel提供的loading事件将不会生效
     */
    protected open fun registerLoadingEvent(vararg viewModels: VM) {
        viewModels.forEach { vm ->
            vm.loadingEvent.showLoading.observeInFragment(this) { t -> showLoading(t) }
            vm.loadingEvent.hideLoading.observeInFragment(this) { hideLoading() }
        }
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirstVisible) {
            //等待view加载后触发延迟加载
            view?.post {
                initData()
                isFirstVisible = false
            }
        }
    }
}