package com.ps.wb.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.ps.wb.R
import com.ps.wb.base.utils.BarUtils
import com.ps.wb.databinding.ActivityResetBinding
import com.ps.wb.repository.APIRepository
import com.ps.wb.state.ResetViewModel
import com.ps.wb.ui.base.BaseActivity
import com.scrb.baselib.net.ExceptionHandle
import com.scrb.baselib.util.VerificationUtils
import kotlinx.android.synthetic.main.activity_reset.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ResetActivity : BaseActivity() {
    private var binding: ActivityResetBinding? = null
    var vm: ResetViewModel? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm =
            getActivityViewModelProvider(this).get(ResetViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset)
        binding?.lifecycleOwner = this
        binding?.vm = vm
        binding?.click = ClickProxy()
        binding?.shareVm = mSharedViewModel
        val params = binding?.root?.layoutParams as FrameLayout.LayoutParams
        params.topMargin = BarUtils.statusBarHeight

        BarUtils.setStatusBarColor(this, Color.parseColor("#2196F3"))
        BarUtils.setStatusBarLightMode(this, false)
    }

    inner class ClickProxy {
        fun register() {
            if (!ck_privacy.isChecked) {
                showShortToast("请先勾选页面下方的\"同意《用户协议》和《隐私政策》\"")
                return
            } else if (!VerificationUtils.matcherAccount(vm!!.phone.value)) {
                showShortToast("请正确填写手机号码")
                return
            } else if (vm!!.pwd.value.isNullOrEmpty()
                || vm!!.pwdAgain.value.isNullOrEmpty()
                || !vm!!.pwd.value.equals(vm!!.pwdAgain.value)
            ) {
                showShortToast("请正确填写并确认密码")
                return
            }

            showLoading("修改密码中")
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val response = APIRepository().resetPassword(vm!!.phone.value,vm!!.pwdAgain.value)
                    hideLoading()
                    showShortToast(response)
                    if (response?.contains("修改密码成功") == true) {
                        finish()
                    }
                } catch (e: Exception) {
                    ExceptionHandle.handleException(e)
                    hideLoading()
                    showShortToast("修改密码失败")
                }
            }
        }

        fun privacy() {
            PrivacyActivity.startActivity(this@ResetActivity, PrivacyActivity.TYPE_PRIVACY)
        }

        fun agree() {
            PrivacyActivity.startActivity(this@ResetActivity, PrivacyActivity.TYPE_AGREEMENT)
        }

        fun back() {
            finish()
        }
    }
}