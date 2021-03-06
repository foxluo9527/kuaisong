package com.ps.wb.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ps.wb.R
import com.ps.wb.base.utils.BarUtils
import com.ps.wb.base.utils.SpUtils
import com.ps.wb.databinding.ActivityMainBinding
import com.ps.wb.state.MainActivityViewModel
import com.ps.wb.ui.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.system.exitProcess

class MainActivity : BaseActivity() {
    private var mainBinding: ActivityMainBinding? = null
    var mainActivityViewModel: MainActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityViewModel =
            getActivityViewModelProvider(this).get(MainActivityViewModel::class.java)

        mSharedViewModel.cityInfo.value = getSharedPreferences("data", MODE_PRIVATE)
            .getString("defaultCity", "")

        if (isLogin) {
            mSharedViewModel.loginUser.value = SpUtils.getUserInfo(this)
            val phone = mSharedViewModel.loginUser.value!!.phone
            mSharedViewModel.loginUSerPhone.value =
                phone.substring(0, 3) + "****" + phone.substring(7, phone.length)
        }
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding?.lifecycleOwner = this
        mainBinding?.vm = mainActivityViewModel
        mainBinding?.shareVm = mSharedViewModel
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment?
        val controller = navHostFragment!!.navController
        val navView: BottomNavigationView = mainBinding!!.navView
        NavigationUI.setupWithNavController(navView, controller)
        BarUtils.setStatusBarColor(this, Color.parseColor("#2196F3"))
        BarUtils.setStatusBarLightMode(this, false)
        val params = mainBinding?.root?.layoutParams as FrameLayout.LayoutParams
        params.topMargin = BarUtils.statusBarHeight

        mSharedViewModel.index.observe(this) {
            navView.selectedItemId = it
        }
        mainBinding?.navView?.setOnNavigationItemSelectedListener {
            // ??????????????????????????????
            if (it.isChecked) {
                return@setOnNavigationItemSelectedListener true
            }
            // ??????B?????????A????????????
            val popBackStack = controller.popBackStack(it.itemId, false)
            if (popBackStack) {
                // ?????????
                return@setOnNavigationItemSelectedListener popBackStack
            } else {
                // ?????????
                return@setOnNavigationItemSelectedListener NavigationUI.onNavDestinationSelected(
                    it, controller
                )
            }
        }
    }

    private var lastTime: Long = 0 //???????????????????????????
    override fun onBackPressed() {
        if (mSharedViewModel.mainNavBottomVisible.value == View.VISIBLE && System.currentTimeMillis() - lastTime > 2000) {
            showShortToast("????????????????????????")
            lastTime = System.currentTimeMillis()
        } else {
            mSharedViewModel.mainNavBottomVisible.value = View.VISIBLE
            if (mSharedViewModel.shouldExit.value == false)
                super.onBackPressed()
            else
                exitProcess(0)
        }
    }
}