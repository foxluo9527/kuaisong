package com.ps.wb.ui.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.ps.wb.base.data.AppDatabase
import com.ps.wb.base.data.Message
import com.ps.wb.base.data.OrderDao
import com.ps.wb.databinding.FragmentDetailBinding
import com.ps.wb.state.DetailViewModel
import com.ps.wb.ui.base.BaseFragment

class DetailFragment : BaseFragment() {
    private lateinit var dao: OrderDao
    private lateinit var vm: DetailViewModel
    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dao = AppDatabase.getInstance(context).orderDao()
        vm =
            ViewModelProvider(this).get(DetailViewModel::class.java)

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        _binding!!.lifecycleOwner = this
        _binding?.vm = vm
        _binding?.click = ClickProxy()
        _binding?.shareVm = sharedVM
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.initOrder(sharedVM.showOrder.value!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class ClickProxy {
        fun onBackPress() {
            nav().navigateUp()
            sharedVM.mainNavBottomVisible.value = View.VISIBLE
        }

        fun delete() {
            AlertDialog.Builder(requireContext())
                .setMessage("确认删除此订单?")
                .setPositiveButton("确认") { dialog, _ ->
                    val messageDao = AppDatabase.getInstance(context).messageDao()
                    val list = messageDao.searchByOrderId(vm.order.value!!.id)
                    for (message in list) {
                        messageDao.delete(message)
                    }
                    dao.delete(vm.order.value)
                    dialog.dismiss()
                    nav().navigateUp()
                    sharedVM.mainNavBottomVisible.value = View.VISIBLE
                }
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        fun onCancel() {
            AlertDialog.Builder(requireContext())
                .setMessage("确认取消此订单?")
                .setPositiveButton("确认") { dialog, _ ->
                    vm.order.value?.orderState = -1
                    dao.update(vm.order.value)
                    val messageDao = AppDatabase.getInstance(context).messageDao()
                    messageDao.insert(
                        Message(
                            Message.TYPE_ORDER_MESSAGE,
                            "订单消息",
                            vm.order.value!!.id,
                            "订单已取消"
                        )
                    )

                    dialog.dismiss()
                    vm.initData()
                }
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}