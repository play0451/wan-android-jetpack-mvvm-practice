package com.gw.mvvm.wan.ui.fragment.share

import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.RegexUtils
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.view.clickNoRepeat
import com.gw.mvvm.framework.ext.viewmodel.parseState
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.databinding.FragmentShareAriticleBinding
import com.gw.mvvm.wan.ext.initWithNavIcon
import com.gw.mvvm.wan.ext.resolveThemeColor
import com.gw.mvvm.wan.ext.showDialog
import com.gw.mvvm.wan.viewModel.share.ShareViewModel
import splitties.resources.str
import splitties.toast.toast

/**
 * 分享文章Fragment
 */
class ShareAriticleFragment : BaseFragment<ShareViewModel, FragmentShareAriticleBinding>() {
    override fun layoutId(): Int = R.layout.fragment_share_ariticle

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.let {
            it.includeToolbar.toolbar.initWithNavIcon(
                title = str(R.string.share_title),
                navCallback = {
                    KeyboardUtils.hideSoftInput(requireActivity())
                    nav().navigateUp()
                },
                menuId = R.menu.menu_share_ariticle
            ) { item ->
                if (item.itemId == R.id.item_rule) {
                    showRule()
                }
                true
            }
            it.buttonShare.clickNoRepeat {
                submitShare()
            }
            it.viewModel = mViewModel
        }
    }

    override fun initObserver() {
        mViewModel.addAriticleResult.observe(viewLifecycleOwner, {
            parseState(it, {
                hideLoading()
                toast(R.string.share_ariticle_success)
                //通知分享了文章
                mGlobalEventViewModel.sendShareAriticle()
                nav().navigateUp()
            }, { e ->
                hideLoading()
                enableUIs(true)
                toast(e.errorMsg)
            }, {
                showLoading(str(R.string.share_ariticle_requesting_tip))
            })
        })
    }

    override fun initData() {
    }

    private fun showRule() {
        MaterialDialog(requireActivity(), BottomSheet())
            .lifecycleOwner(viewLifecycleOwner)
            .show {
                title(text = str(R.string.text_hint))
                customView(
                    R.layout.layout_share_ariticle_rule,
                    scrollable = true,
                    horizontalPadding = true
                )
                positiveButton(text = str(R.string.text_sure))
                cornerRadius(16f)
                getActionButton(WhichButton.POSITIVE).updateTextColor(resolveThemeColor(R.attr.colorDialogButton))
            }
    }

    private fun submitShare() {
        if (mViewModel.title.value.isNullOrBlank()
            || mViewModel.title.value!!.length > mDataBinding.tilTitle.counterMaxLength
        ) {
            mDataBinding.tilTitle.error = String.format(
                str(R.string.share_ariticle_title_error),
                mDataBinding.tilTitle.counterMaxLength
            )
            return
        }
        if (mViewModel.url.value.isNullOrBlank()
            || mViewModel.url.value!!.length > mDataBinding.tilUrl.counterMaxLength
            || !RegexUtils.isURL(mViewModel.url.value)
        ) {
            mDataBinding.tilUrl.error = String.format(
                str(R.string.share_ariticle_url_error),
                mDataBinding.tilUrl.counterMaxLength
            )
            return
        }
        showDialog(message = str(R.string.share_ariticle_hint),
            negativeButtonText = str(R.string.text_cancel),
            positiveAction = {
                enableUIs(false)
                mViewModel.addAriticle()
            })
    }

    private fun enableUIs(bo: Boolean = true) {
        mDataBinding.let {
            it.buttonShare.isEnabled = bo
            it.etvTitle.isEnabled = bo
            it.etvUrl.isEnabled = bo
        }
    }
}