package com.gw.mvvm.wan.ui.activity

import android.content.ClipData
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.gw.mvvm.framework.base.viewmodel.EmptyViewModel
import com.gw.mvvm.framework.ext.utils.clipboardManager
import com.gw.mvvm.framework.ext.view.gone
import com.gw.mvvm.framework.ext.view.onClick
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.databinding.ActivityCrashBinding
import com.gw.mvvm.wan.core.base.BaseActivity
import splitties.resources.str
import splitties.toast.toast

/**
 * 崩溃页面
 */
class CrashActivity : BaseActivity<EmptyViewModel, ActivityCrashBinding>() {
    override fun layoutId(): Int {
        return R.layout.activity_crash
    }

    override fun initView(savedInstanceState: Bundle?) {
        val config: CaocConfig? = CustomActivityOnCrash.getConfigFromIntent(intent)
        if (config == null) {
            finish()
            return
        }
        config.run {
            if (this.isShowRestartButton && this.restartActivityClass != null) {
                mDataBinding.btnClose.text = getString(R.string.crash_restart_app)
                mDataBinding.btnClose.onClick {
                    CustomActivityOnCrash.restartApplication(this@CrashActivity, this)
                }
            } else {
                mDataBinding.btnClose.onClick {
                    CustomActivityOnCrash.closeApplication(this@CrashActivity, this)
                }
            }
            if (this.isShowErrorDetails) {
                mDataBinding.btnDetail.onClick {
                    val dialog: AlertDialog = AlertDialog.Builder(this@CrashActivity)
                        .setTitle(R.string.crash_error_detail)
                        .setMessage(
                            CustomActivityOnCrash.getAllErrorDetailsFromIntent(
                                this@CrashActivity,
                                intent
                            )
                        )
                        .setPositiveButton(R.string.text_close, null)
                        .setNeutralButton(R.string.text_copy_to_clipboard) { _, _ -> copyErrorToClipboard() }
                        .create()
                    dialog.show()
                }
            } else {
                mDataBinding.btnDetail.gone()
            }
        }
    }

    override fun initObserver() {

    }

    private fun copyErrorToClipboard() {
        val errorInformation =
            CustomActivityOnCrash.getAllErrorDetailsFromIntent(this@CrashActivity, intent)
        val clipboard = clipboardManager

        //Are there any devices without clipboard...?
        if (clipboard != null) {
            val clip = ClipData.newPlainText(
                str(R.string.crash_error_detail),
                errorInformation
            )
            clipboard.setPrimaryClip(clip)
            toast(R.string.crash_error_detail_copyed)
        }
    }

    override fun initData() {
    }
}