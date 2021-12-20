package com.ps.wb.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.ps.wb.R
import com.ps.wb.base.data.AppDatabase
import com.ps.wb.base.data.Message
import com.ps.wb.base.utils.BarUtils
import com.ps.wb.base.utils.SpUtils
import com.ps.wb.databinding.ActivityLoginBinding
import com.ps.wb.state.LoginViewModel
import com.ps.wb.ui.base.BaseActivity
import com.scrb.baselib.entity.User
import com.scrb.baselib.util.VerificationUtils
import kotlinx.android.synthetic.main.activity_login.*

open class LoginActivity : BaseActivity() {
    private var binding: ActivityLoginBinding? = null
    var vm: LoginViewModel? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm =
            getActivityViewModelProvider(this).get(LoginViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding?.lifecycleOwner = this
        binding?.vm = vm
        binding?.click = ClickProxy()
        binding?.shareVm = mSharedViewModel
        val params = binding?.root?.layoutParams as FrameLayout.LayoutParams
        params.topMargin = BarUtils.statusBarHeight
        vm!!.loginResult.observe(this) {
            if (it.isNullOrEmpty()) {
                hideLoading()
                showShortToast("登录失败")
            } else {
                requestSuccess(it)
            }
        }
        vm!!.stateManager.observe(this) {
            showShortToast(it)
        }
        BarUtils.setStatusBarColor(this, Color.parseColor("#2196F3"))
        BarUtils.setStatusBarLightMode(this, false)
    }

    private fun requestSuccess(response: String) {
        hideLoading()
        if (response.contains("S") || response.contains("s")) {
            val userList = SpUtils.getAccount(this@LoginActivity)
            var user = User()
            for (u in userList) {
                if (u.phone == vm!!.phone.value) {
                    //有用户信息，直接使用
                    user = u
                    SpUtils.saveUserInfo(this@LoginActivity, u)
                    break
                }
            }
            if (SpUtils.getUserInfo(this@LoginActivity) == null) {
                //没有用户信息，新建用户信息
                val phone = vm!!.phone.value
                user.phone = phone
                user.nickName = phone?.substring(0, 3) + "****" + phone?.substring(
                    7,
                    phone.length
                )
                user.password = vm!!.pwd.value
                SpUtils.saveAccount(this@LoginActivity, user)
                SpUtils.saveUserInfo(this@LoginActivity, user)
            } else if (!userList.contains(user)) {
                user = SpUtils.getUserInfo(this@LoginActivity)
                SpUtils.saveAccount(this@LoginActivity, user)
            }
            val phone = user.phone
            mSharedViewModel.loginUSerPhone.value =
                phone.substring(0, 3) + "****" + phone.substring(7, phone.length)
            mSharedViewModel.loginUser.value = user
            showShortToast("登录成功")
            SpUtils.saveLoginState(true, this@LoginActivity)
            if (AppDatabase.getInstance(this@LoginActivity).messageDao()
                    .searchById(1) == null
            )
                AppDatabase.getInstance(this@LoginActivity).messageDao().insert(
                    Message(
                        Message.TYPE_SYS,
                        "系统消息",
                        0,
                        "欢迎使用${getString(R.string.app_name)}，赶快试试我们的配送服务吧"
                    )
                )
            finish()
        } else {
            showShortToast(response)
        }
    }

    inner class ClickProxy {
        fun login() {
            if (!ck_privacy.isChecked) {
                showShortToast("请先勾选页面下方的\"同意《用户协议》和《隐私政策》\"")
                return
            } else if (!VerificationUtils.matcherAccount(vm!!.phone.value)) {
                showShortToast("请正确填写手机号码")
                return
            } else if (vm!!.pwd.value.isNullOrEmpty()) {
                showShortToast("请填写密码")
                return
            }
            showLoading("登录中")
            vm!!.login()
        }

        fun back() {
            finish()
        }

        fun privacy() {
            PrivacyActivity.startActivity(this@LoginActivity, PrivacyActivity.TYPE_PRIVACY)
        }

        fun agree() {
            PrivacyActivity.startActivity(this@LoginActivity, PrivacyActivity.TYPE_AGREEMENT)
        }

        fun reset() {
            gotoActivity(ResetActivity::class.java)
        }

        fun register() {
            gotoActivity(RegisterActivity::class.java)
        }
    }
}