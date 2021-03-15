package com.gw.mvvm.wan.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.gw.mvvm.framework.base.viewmodel.EmptyViewModel
import com.gw.mvvm.framework.ext.view.onClick
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.databinding.ActivitySplashBinding
import com.gw.mvvm.wan.core.base.BaseActivity
import com.gw.mvvm.wan.ui.adapter.splash.SplashBannerAdapter
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnPageChangeListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 启动页面
 */
class SplashActivity : BaseActivity<EmptyViewModel, ActivitySplashBinding>() {

    companion object {
        private const val SHOW_DURATION: Long = 2000
    }

    override fun layoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initView(savedInstanceState: Bundle?) {

        val isFirstStart: Boolean = GlobalData.isFirstStart
        mDataBinding.isFirstStart = isFirstStart
        if (isFirstStart) {
            mDataBinding.let {
                val list: List<Int> = listOf(
                    R.drawable.img_splash_0,
                    R.drawable.img_splash_1,
                    R.drawable.img_splash_2,
                    R.drawable.img_splash_3,
                    R.drawable.img_splash_4
                )
                it.banner.apply {
                    adapter =
                        SplashBannerAdapter(
                            list
                        )
                    addBannerLifecycleObserver(this@SplashActivity)
                    addOnPageChangeListener(object : OnPageChangeListener {
                        override fun onPageScrollStateChanged(state: Int) {}

                        override fun onPageScrolled(
                            position: Int,
                            positionOffset: Float,
                            positionOffsetPixels: Int
                        ) {
                        }

                        override fun onPageSelected(position: Int) {
                            it.isShowEnter = position >= list.size - 1
                        }
                    })
                    indicator = CircleIndicator(this@SplashActivity)
                }
                it.btnEnter.onClick {
                    GlobalData.isFirstStart = false
                    openMainActivity()
                }
            }
        } else {
            lifecycleScope.launch {
                delay(SHOW_DURATION)
                openMainActivity()
            }
        }
    }

    private fun openMainActivity() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
        //关闭当前Activity和启动下一个Activity的动画,系统方法
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun initObserver() {

    }

    override fun initData() {
    }
}