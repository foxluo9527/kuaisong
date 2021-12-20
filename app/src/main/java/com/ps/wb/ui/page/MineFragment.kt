package com.ps.wb.ui.page

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.ps.wb.R
import com.ps.wb.databinding.FragmentMineBinding
import com.ps.wb.state.MineViewModel
import com.ps.wb.ui.LoginActivity
import com.ps.wb.ui.PrivacyActivity
import com.ps.wb.ui.base.BaseFragment
import com.scrb.baselib.util.GlideUtils
import kotlin.system.exitProcess

class MineFragment : BaseFragment() {

    private lateinit var vm: MineViewModel
    private var _binding: FragmentMineBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm =
            ViewModelProvider(this).get(MineViewModel::class.java)

        _binding = FragmentMineBinding.inflate(inflater, container, false)
        _binding!!.lifecycleOwner = this
        _binding?.vm = vm
        _binding?.click = ClickProxy()
        _binding?.shareVm = sharedVM
        sharedVM.loginUser.observe(viewLifecycleOwner) {
            if (it != null && !it.head.isNullOrEmpty()) {
                GlideUtils.intoHead(context, it.head, _binding?.headImg)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in childFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onResume() {
        super.onResume()
        vm.name.value = sharedVM.loginUser.value?.nickName
        sharedVM.shouldExit.value = true
    }

    inner class ClickProxy {
        fun showMessage() {
            if (!isLogin) {
                gotoActivity(LoginActivity::class.java)
                return
            }
            sharedVM.mainNavBottomVisible.value = View.GONE
            nav().navigate(R.id.action_navigation_dashboard_to_messageFragment)
            sharedVM.shouldExit.value = false
        }

        fun finish() {
            exitProcess(0)
        }

        fun feedback() {
            if (!isLogin) {
                showLoginDialog()
                return
            }
            sharedVM.mainNavBottomVisible.value = View.GONE
            nav().navigate(R.id.action_navigation_dashboard_to_feedbackFragment)
            sharedVM.shouldExit.value = false
        }

        fun addressPress() {
            if (!isLogin) {
                showLoginDialog()
                return
            }
            sharedVM.mainNavBottomVisible.value = View.GONE
            nav().navigate(R.id.action_navigation_dashboard_to_addressManageFragment)
            sharedVM.shouldExit.value = false
        }

        fun aboutUs() {
            sharedVM.mainNavBottomVisible.value = View.GONE
            nav().navigate(R.id.action_navigation_dashboard_to_aboutFragment)
            sharedVM.shouldExit.value = false
        }

        fun privacyPress() {
            PrivacyActivity.startActivity(context!!, PrivacyActivity.TYPE_PRIVACY)
        }

        fun agreePress() {
            PrivacyActivity.startActivity(context!!, PrivacyActivity.TYPE_AGREEMENT)
        }
    }
}