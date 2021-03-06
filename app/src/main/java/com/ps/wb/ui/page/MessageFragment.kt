package com.ps.wb.ui.page

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity
import com.bumptech.glide.Glide
import com.ps.wb.R
import com.ps.wb.base.utils.PermissionUtils
import com.ps.wb.base.utils.SpUtils
import com.ps.wb.databinding.FragmentMessageBinding
import com.ps.wb.repository.APIRepository
import com.ps.wb.state.MessageViewModel
import com.ps.wb.ui.ResetActivity
import com.ps.wb.ui.base.BaseFragment
import com.scrb.baselib.entity.DialogType
import com.scrb.baselib.net.ExceptionHandle
import com.scrb.baselib.util.GlideUtils
import com.scrb.baselib.view.MyDialog
import com.yalantis.ucrop.UCrop
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MessageFragment : BaseFragment() {
    private var path: String? = null
    private lateinit var vm: MessageViewModel
    private var _binding: FragmentMessageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm =
            ViewModelProvider(this).get(MessageViewModel::class.java)

        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        _binding!!.lifecycleOwner = this
        _binding?.vm = vm
        _binding?.click = ClickProxy()
        _binding?.shareVm = sharedVM
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedVM.loginUser.observe(viewLifecycleOwner) {
            if (it != null && !it.head.isNullOrEmpty()) {
                GlideUtils.intoHead(context, it.head, iv_head)
            }
        }
        vm.name.value = sharedVM.loginUser.value?.nickName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * ????????????????????????
     */
    @SuppressLint("SimpleDateFormat", "ShowToast")
    private fun getImage() {
        // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        PermissionUtils.applicationPermissions(
            requireContext(),
            object : PermissionUtils.PermissionListener {
                @Override
                override fun onSuccess(context: Context?) {
                    val file = File(requireActivity().externalCacheDir!!.path)
                    val photoPickerIntent = BGAPhotoPickerActivity.IntentBuilder(context)
                        .cameraFileDir(file) // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                        .maxChooseCount(1) // ??????????????????????????????
                        .selectedPhotos(null) // ????????????????????????????????????
                        .pauseOnScroll(false) // ???????????????????????????????????????
                        .build()
                    startActivityForResult(photoPickerIntent, 101)
                }

                @Override
                override fun onFailed(context: Context?) {
                    if (AndPermission.hasAlwaysDeniedPermission(
                            context,
                            Permission.Group.CAMERA.asList()
                        )
                        && AndPermission.hasAlwaysDeniedPermission(
                            context,
                            Permission.Group.STORAGE.asList()
                        )
                    ) {
                        AndPermission.with(context).runtime().setting().start(100)
                    }
                    showShortToast(R.string.permission_camera_storage)
                }
            },
            Permission.Group.STORAGE,
            Permission.Group.CAMERA
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                101 -> {
                    // ????????????
                    val dir = requireContext().externalCacheDir
                    val file = File(
                        dir!!.path + "/" +
                                SimpleDateFormat("yyyyMMddHHmm").format(System.currentTimeMillis()) + UUID.randomUUID()
                            .toString().substring(0, 6) + ".jpg"
                    )
                    val selectedPhotos: List<String> =
                        BGAPhotoPickerActivity.getSelectedPhotos(data)
                    UCrop.of(Uri.fromFile(File(selectedPhotos[0])), Uri.fromFile(file))
                        .withAspectRatio(1f, 1f)
                        .start(requireContext(), this)
                }
                UCrop.REQUEST_CROP -> {
                    val resultUri = UCrop.getOutput(data!!)
                    path = resultUri.toString()
                    if (path!!.contains("file:///")) {
                        path = path!!.replace("file:///", "")
                    }
                    Glide.with(this)
                        .load(path)
                        .into(iv_head)
                    sharedVM.loginUser.value!!.head = path
                    SpUtils.saveAccount(context, sharedVM.loginUser.value)
                    SpUtils.saveUserInfo(context, sharedVM.loginUser.value)
                }
                UCrop.RESULT_ERROR -> {
                    val cropError = UCrop.getError(data!!)
                    cropError!!.printStackTrace()
                }
                100 -> {
                    getImage()
                }
            }
        }
    }

    inner class ClickProxy {
        fun onBackPress() {
            nav().navigateUp()
            sharedVM.mainNavBottomVisible.value = View.VISIBLE
        }

        fun showHead() {
            getImage()
        }

        fun showName() {
            val dialog = MyDialog(
                mActivity,
                "???????????????",
                sharedVM.loginUser.value!!.nickName,
                DialogType.EDITTEXT_DIALOG
            )
            dialog.setDialogListener {
                if (it.isNullOrEmpty()) {
                    return@setDialogListener
                }
                vm.name.value = it
                sharedVM.loginUser.value!!.nickName = it
                SpUtils.saveAccount(context, sharedVM.loginUser.value)
                SpUtils.saveUserInfo(context, sharedVM.loginUser.value)
            }
            dialog.show()
        }

        fun changePass() {
            gotoActivity(ResetActivity::class.java)
        }

        fun unRegister() {
            AlertDialog.Builder(requireContext())
                .setMessage("??????????????????????")
                .setPositiveButton("??????") { dialog, _ ->
                    dialog.dismiss()
                    showLoading("???????????????")
                    GlobalScope.launch(Dispatchers.Main) {
                        try {
                            val response = APIRepository().logout(sharedVM.loginUser.value!!.phone)
                            hideLoading()
                            showShortToast(response)
                            if (response?.contains("??????") == true) {
                                SpUtils.saveLoginState(false, context)
                                sharedVM.loginUser.value = null
                                sharedVM.loginUSerPhone.value = ""
                                nav().navigateUp()
                                sharedVM.mainNavBottomVisible.value = View.VISIBLE
                            }
                        } catch (e: Exception) {
                            ExceptionHandle.handleException(e)
                            hideLoading()
                            showShortToast("??????????????????")
                        }
                    }
                }
                .setNegativeButton("??????") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        fun logOut() {
            AlertDialog.Builder(requireContext())
                .setMessage("???????????????????")
                .setPositiveButton("??????") { dialog, _ ->
                    showShortToast("??????????????????")
                    dialog.dismiss()
                    SpUtils.saveLoginState(false, context)
                    sharedVM.loginUser.value = null
                    sharedVM.loginUSerPhone.value = ""
                    nav().navigateUp()
                    sharedVM.mainNavBottomVisible.value = View.VISIBLE
                }.setNegativeButton("??????") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}