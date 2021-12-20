package com.ps.wb.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.crush.skipupdate.SplashRequest
import com.ps.wb.R
import com.ps.wb.base.utils.BarUtils
import com.ps.wb.base.utils.SpUtils
import com.ps.wb.repository.APIRepository
import com.ps.wb.ui.base.BaseActivity
import com.scrb.baselib.net.ExceptionHandle
import com.scrb.baselib.view.FirstDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.exitProcess


class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this, Color.WHITE)
        BarUtils.setStatusBarLightMode(this, true)
        if (SpUtils.getFirst(this)) {
            showFirstDialog()
        } else {
            checkWebUrl()
        }
    }

    private fun checkWebUrl() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = APIRepository().webURL()
                SplashRequest.init(this@SplashActivity, response) { //跳转到自己的首页
                    setContentView(R.layout.activity_splash)
                    toMain()
                }
            } catch (e: Exception) {
                ExceptionHandle.handleException(e)
                setContentView(R.layout.activity_splash)
                toMain()
            }
        }
    }

    private fun showFirstDialog() {
        val firstDialog = FirstDialog(this)
        firstDialog.setOnDialogListener(object : FirstDialog.OnDialogListener {
            override fun onNotAgree() {
                exitProcess(0)
            }

            override fun onAgree() {
                checkWebUrl()
                SpUtils.saveIsFirst(false, this@SplashActivity)
            }

            override fun onUserAgreement() {
                PrivacyActivity.startActivity(this@SplashActivity, PrivacyActivity.TYPE_AGREEMENT)
            }

            override fun onPrivacy() {
                PrivacyActivity.startActivity(this@SplashActivity, PrivacyActivity.TYPE_PRIVACY)
            }
        })
        firstDialog.show()
    }

    private fun toMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}