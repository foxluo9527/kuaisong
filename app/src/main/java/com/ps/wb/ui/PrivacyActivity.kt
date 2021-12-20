package com.ps.wb.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.ps.wb.R
import com.ps.wb.base.bridge.callback.SharedViewModel
import com.ps.wb.base.utils.BarUtils
import com.ps.wb.databinding.ActivityPrivacyBinding
import com.ps.wb.state.PrivacyActivityViewModel
import com.ps.wb.ui.base.BaseActivity

class PrivacyActivity : BaseActivity() {
    private var binding: ActivityPrivacyBinding? = null
    var vm: PrivacyActivityViewModel? = null
    var shareVm: SharedViewModel? = null
    private lateinit var type: String

    companion object {
        private const val KEY_TYPE = "type"
        const val TYPE_PRIVACY = "隐私政策"
        const val TYPE_AGREEMENT = "用户协议"

        fun startActivity(context: Context, type: String) {
            val intent = Intent(context, PrivacyActivity::class.java)
            intent.putExtra(KEY_TYPE, type)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm =
            getActivityViewModelProvider(this).get(PrivacyActivityViewModel::class.java)
        shareVm =
            getAppViewModelProvider().get(SharedViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy)
        binding?.lifecycleOwner = this
        binding?.vm = vm
        binding?.shareVm = shareVm
        binding?.click=ClickProxy()

        BarUtils.setStatusBarColor(this, Color.parseColor("#2196F3"))
        BarUtils.setStatusBarLightMode(this, false)
        val params = binding?.root?.layoutParams as FrameLayout.LayoutParams
        params.topMargin = BarUtils.statusBarHeight

        val bundle = intent.extras
        if (bundle != null) {
            type = bundle.getString(KEY_TYPE).toString()
            vm!!.title.value = type
        }
        if (type == TYPE_PRIVACY) {
            vm!!.content.value = resources!!.getString(R.string.privacy_policy_txt)
        } else {
            vm!!.content.value = resources!!.getString(R.string.user_agreement_txt)
        }
    }

    inner class ClickProxy {
        fun onBackPress() {
            finish()
        }
    }
}