package com.ps.wb.ui.page

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.ps.wb.databinding.FragmentFeedbackBinding
import com.ps.wb.state.FeedbackViewModel
import com.ps.wb.ui.base.BaseFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity
import com.ps.wb.R
import com.ps.wb.base.utils.PermissionUtils
import com.yalantis.ucrop.UCrop
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

class FeedbackFragment : BaseFragment() {
    private var path: String? = null
    private lateinit var vm: FeedbackViewModel
    private var _binding: FragmentFeedbackBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm =
            ViewModelProvider(this).get(FeedbackViewModel::class.java)

        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        _binding!!.lifecycleOwner = this
        _binding?.vm = vm
        _binding?.click = ClickProxy()
        _binding?.shareVm = sharedVM
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.content.observe(viewLifecycleOwner) {
            vm.contentNumber.value = "${it.length}/50字"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    /**
     * 从相册中获取照片
     */
    @SuppressLint("SimpleDateFormat","ShowToast")
    private fun getImage() {
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        PermissionUtils.applicationPermissions(requireContext(), object : PermissionUtils.PermissionListener {
            @Override
            override fun onSuccess(context: Context?) {
                val file = File(requireActivity().externalCacheDir!!.path)
                val photoPickerIntent = BGAPhotoPickerActivity.IntentBuilder(context)
                    .cameraFileDir(file) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                    .maxChooseCount(1) // 图片选择张数的最大值
                    .selectedPhotos(null) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                    .build()
                startActivityForResult(photoPickerIntent, 101)
            }

            @Override
            override fun onFailed(context: Context?) {
                if (AndPermission.hasAlwaysDeniedPermission(context, Permission.Group.CAMERA.asList())
                    && AndPermission.hasAlwaysDeniedPermission(context, Permission.Group.STORAGE.asList())) {
                    AndPermission.with(context).runtime().setting().start(100)
                }
                showShortToast(R.string.permission_camera_storage)
            }
        }, Permission.Group.STORAGE, Permission.Group.CAMERA)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                101 -> {
                    // 结果回调
                    val dir = requireContext().externalCacheDir
                    val file = File(
                        dir!!.path + "/" +
                                SimpleDateFormat("yyyyMMddHHmm").format(System.currentTimeMillis()) + UUID.randomUUID()
                            .toString().substring(0, 6) + ".jpg"
                    )
                    val selectedPhotos: List<String> =
                        BGAPhotoPickerActivity.getSelectedPhotos(data)
                    UCrop.of(Uri.fromFile(File(selectedPhotos[0])), Uri.fromFile(file))
                        .start(requireContext(),this)
                }
                UCrop.REQUEST_CROP -> {
                    val resultUri = UCrop.getOutput(data!!)
                    path = resultUri.toString()
                    if (path!!.contains("file:///")) {
                        path = path!!.replace("file:///", "")
                    }
                    Glide.with(this)
                        .load(path)
                        .into(_binding!!.ivAddPic)
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
            sharedVM.mainNavBottomVisible.value = View.VISIBLE
            nav().navigateUp()
        }

        fun addPic() {
            getImage()
        }

        fun submit() {
            when {
                TextUtils.isEmpty(vm.content.value) -> {
                    showShortToast("请输入您要发布的动态")
                }
                vm.content.value!!.length < 5 -> {
                    showLongToast("说说内容不能少于5个字")
                }
                else -> {
                    showLoading("提交中")
                    Handler(Looper.getMainLooper()).postDelayed({
                        hideLoading()
                        showShortToast("反馈成功")
                        sharedVM.mainNavBottomVisible.value = View.VISIBLE
                        nav().navigateUp()
                    }, 800)
                }
            }
        }
    }
}