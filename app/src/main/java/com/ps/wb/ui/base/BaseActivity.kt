package com.ps.wb.ui.base

import android.app.Dialog
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ps.wb.App
import com.ps.wb.base.bridge.callback.SharedViewModel
import com.ps.wb.base.data.manager.NetworkStateManager
import com.ps.wb.base.utils.AdaptScreenUtils
import com.ps.wb.base.utils.ScreenUtils
import com.ps.wb.base.utils.SpUtils
import com.scrb.baselib.view.MyLoading

/**
 * 所有Activity 的基类
 */
// open 剔除 final修饰
open class BaseActivity : AppCompatActivity() {
    //加载框
    private var dialog: Dialog? = null

    // 贯穿整个项目的（只会让App(Application)初始化一次）
    lateinit var mSharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSharedViewModel =
            getAppViewModelProvider().get(SharedViewModel::class.java)

        // 准备：lifecycle
        // 意味着 BaseActivity被观察者  -----> NetworkStateManager观察者（一双眼睛 盯着看 onResume/onPause）
        // BaseActivity就是被观察者 ---> NetworkStateManager.getInstance()
        lifecycle.addObserver(NetworkStateManager.instance)
    }

    /**
     * 显示加载框
     */
    protected fun showLoading(message: String) {
        if (dialog == null) {
            dialog = MyLoading.createLoadingDialog(this, message)
        }
    }

    /**
     * 隐藏加载框
     */
    protected fun hideLoading() {
        if (dialog != null) {
            MyLoading.closeDialog(dialog)
            dialog = null
        }
    }

    protected fun gotoActivity(
        clz: Class<*>?,
        bundle: Bundle? = null
    ) {
        val intent = Intent()
        intent.setClass(this, clz!!)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    val isLogin: Boolean
        get() = SpUtils.getLoginState(this)

    // BaseActivity的 Resource信息给 暴露给外界用
    override fun getResources(): Resources? {
        return if (ScreenUtils.isPortrait) {
            AdaptScreenUtils.adaptWidth(super.getResources(), 360)
        } else {
            AdaptScreenUtils.adaptHeight(super.getResources(), 640)
        }
    }

    // 工具函数 提示Toast而已
    fun showLongToast(text: String?) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }

    // 工具函数 提示Toast而已
    fun showShortToast(text: String?) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    // 2020 用法 ViewModelProvider 【ViewModel共享区域】
    // 此getAppViewModelProvider函数，只给 共享的ViewModel用（例如：mSharedViewModel .... 等共享的ViewModel）
    protected fun getAppViewModelProvider(): ViewModelProvider {
        return (applicationContext as App).getAppViewModelProvider(this)
    }

    // 此getActivityViewModelProvider函数，给所有的 BaseActivity 子类用的 【ViewModel非共享区域】
    protected fun getActivityViewModelProvider(activity: AppCompatActivity): ViewModelProvider {
        return ViewModelProvider(activity, activity.defaultViewModelProviderFactory)
    }
}