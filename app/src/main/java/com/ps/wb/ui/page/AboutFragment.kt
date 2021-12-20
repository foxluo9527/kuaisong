package com.ps.wb.ui.page

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ps.wb.R
import com.ps.wb.databinding.FragmentAboutBinding
import com.ps.wb.state.AboutViewModel
import com.ps.wb.ui.PrivacyActivity
import com.ps.wb.ui.base.BaseFragment

class AboutFragment : BaseFragment() {

    private lateinit var vm: AboutViewModel
    private var _binding: FragmentAboutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm =
            ViewModelProvider(this).get(AboutViewModel::class.java)

        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        _binding!!.lifecycleOwner = this
        _binding?.vm = vm
        _binding?.click = ClickProxy()
        _binding?.shareVm = sharedVM
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.version.value = "${getString(R.string.app_name)} V ${
            requireContext().packageManager?.getPackageInfo(
                requireContext().packageName,
                0
            )?.versionName
        }"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class ClickProxy {
        fun privacyPress() {
            PrivacyActivity.startActivity(context!!, PrivacyActivity.TYPE_PRIVACY)
        }

        fun agreePress() {
            PrivacyActivity.startActivity(context!!, PrivacyActivity.TYPE_AGREEMENT)
        }

        fun onBackPress() {
            sharedVM.mainNavBottomVisible.value = View.VISIBLE
            nav().navigateUp()
        }

        fun checkVersion() {
            showLoading("检查更新中")
            Handler(Looper.getMainLooper()).postDelayed({
                hideLoading()
                showShortToast("暂无更新")
            }, 1000)
        }
    }
}