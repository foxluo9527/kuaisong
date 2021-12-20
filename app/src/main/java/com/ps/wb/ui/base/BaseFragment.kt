package com.ps.wb.ui.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.ps.wb.App
import com.ps.wb.base.androidx.navigation.fragment.NavHostFragment
import com.ps.wb.base.bridge.callback.SharedViewModel
import com.ps.wb.base.utils.SpUtils
import com.ps.wb.ui.LoginActivity
import com.scrb.baselib.entity.DialogType
import com.scrb.baselib.view.MyDialog
import com.scrb.baselib.view.MyLoading

/**
 * 所有Fragment的父类
 */
open class BaseFragment : Fragment() {

    protected var mActivity: AppCompatActivity? = null // 为了 让所有的子类 持有 Activity
    private var dialog: Dialog? = null

    // private var _sharedViewModel: SharedViewModel
    // 贯穿整个项目的（只会让App(Application)初始化一次）
    protected lateinit var sharedVM: SharedViewModel // 共享区域的ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedVM = getAppViewModelProvider().get(SharedViewModel::class.java)
    }

    /**
     * 登陆弹窗
     */
    fun showLoginDialog() {
        val dialog = MyDialog(mActivity, "您还未登录，是否去登录", DialogType.NORMAL_DIALOG)
        dialog.setDialogListener { gotoActivity(LoginActivity::class.java) }
        dialog.show()
    }
    /**
     * 携带数据的页面跳转
     */
    /**
     * 跳转页面
     *
     * @param clz
     */
    @JvmOverloads
    fun gotoActivity(clz: Class<*>, bundle: Bundle? = null, isJudge: Boolean? = null) {
        if (isJudge != null && isJudge && !isLogin) { //判断登录是否为空&&是否判断登录&是否登录
            showLoginDialog()
            return
        }
        val intent = Intent()
        intent.setClass(requireActivity(), clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    val isLogin: Boolean
        get() = SpUtils.getLoginState(context)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    // 同学们，只是提示而已
    fun showLongToast(text: String?) {
        Toast.makeText(mActivity!!.applicationContext, text, Toast.LENGTH_LONG).show()
    }

    // 同学们，只是提示而已
    fun showShortToast(text: String?) {
        Toast.makeText(mActivity!!.applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    // 同学们，只是提示而已
    fun showLongToast(stringRes: Int) {
        showLongToast(mActivity!!.applicationContext.getString(stringRes))
    }

    // 同学们，只是提示而已
    fun showShortToast(stringRes: Int) {
        showShortToast(mActivity!!.applicationContext.getString(stringRes))
    }

    // 给当前BaseFragment用的【共享区域的ViewModel】
    protected fun getAppViewModelProvider(): ViewModelProvider {
        return (mActivity!!.applicationContext as App).getAppViewModelProvider(mActivity!!)
    }

    // 给所有的 子fragment提供的函数，可以顺利的拿到 ViewModel 【非共享区域的ViewModel】
    protected fun getFragmentViewModelProvider(fragment: Fragment): ViewModelProvider {
        return ViewModelProvider(fragment, fragment.defaultViewModelProviderFactory)
    }

    // 备用的
    // 给所有的 子fragment提供的函数，可以顺利的拿到 ViewModel 【非共享区域的ViewModel】
    protected fun getActivityViewModelProvider(activity: AppCompatActivity): ViewModelProvider {
        return ViewModelProvider(activity, activity.defaultViewModelProviderFactory)
    }

    /**
     * 为了给所有的 子fragment，导航跳转fragment的
     * @return
     */
    protected fun nav(): NavController {
        return NavHostFragment.findNavController(this)
    }

    /**
     * 显示加载框
     */
    fun showLoading(message: String?) {
        if (dialog == null) {
            dialog = MyLoading.createLoadingDialog(mActivity, message!!)
        }
    }

    /**
     * 隐藏加载框
     */
    fun hideLoading() {
        if (dialog != null) {
            MyLoading.closeDialog(dialog)
            dialog = null
        }
    }
    /**
     * 对外暴露 SharedViewModel，所有的 子fragment都可以使用 共享的SharedViewModel
     * @return
     */
    /*fun getSharedViewModel(): SharedViewModel {
        return sharedViewModel
    }*/
}