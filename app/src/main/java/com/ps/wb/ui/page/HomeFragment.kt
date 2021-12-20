package com.ps.wb.ui.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.utils.DistanceUtil
import com.github.gzuliyujiang.wheelpicker.LinkagePicker
import com.ps.wb.R
import com.ps.wb.base.data.*
import com.ps.wb.base.utils.Arith
import com.ps.wb.databinding.FragmentHomeBinding
import com.ps.wb.state.HomeViewModel
import com.ps.wb.ui.base.BaseFragment
import com.ps.wb.ui.dialog.InputDialog
import com.ps.wb.ui.dialog.SetInfoDialog
import com.ps.wb.ui.view.TimeProvider
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : BaseFragment() {
    private lateinit var dao: OrderDao
    private lateinit var vm: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private var distance = 0.0
    private var money = 0.0
    private var weight = 0
    private var type = ""
    private var timeStr = "立即发单"
    private var time = 0L

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dao = AppDatabase.getInstance(context).orderDao()
        vm = getFragmentViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _binding?.click = ClickProxy()
        _binding?.lifecycleOwner = this
        _binding?.vm = vm
        _binding?.shareVm = sharedVM
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedVM.mainNavBottomVisible.value = View.VISIBLE
        if (sharedVM.onSendSetDown.value == true) {
            vm.sendAddress.value = sharedVM.sendAddress.value
            sharedVM.sendAddress.value = Address("", "", "", "")
            sharedVM.onSendSetDown.value = false
        }
        if (sharedVM.onReceiveSetDown.value == true) {
            vm.receiveAddress.value = sharedVM.receiveAddress.value
            sharedVM.receiveAddress.value = Address("", "", "", "")
            sharedVM.onReceiveSetDown.value = false
        }
        if (vm.sendAddress.value?.lt != 0.0 && vm.receiveAddress.value?.lt != 0.0) {
            val l1 = LatLng(vm.sendAddress.value!!.lt, vm.sendAddress.value!!.lg)
            val l2 = LatLng(vm.receiveAddress.value!!.lt, vm.receiveAddress.value!!.lg)
            distance = DistanceUtil.getDistance(l1, l2)
            vm.distance.value = "${Arith.div(distance, 1000.0, 1)}公里"
            if (weight > 0)
                initMoney()
        }
    }

    private fun refresh() {
        distance = 0.0
        money = 0.0
        weight = 0
        type = ""
        timeStr = "立即发单"
        time = 0L
        vm.info.value = ""
        vm.distance.value = ""
        vm.money.value = "￥ - - "
        vm.time.value = timeStr
        vm.remark.value = ""
        vm.sendAddress.value = Address("", "", "", "")
        vm.receiveAddress.value = Address("", "", "", "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initMoney() {
        if (distance >= 0.0)
            money = 5 + weight * 1.2 + distance / 1000 * 1.3
        money = Arith.div(money, 1.0, 2)
        vm.money.value = "￥ $money"
    }

    inner class ClickProxy {
        fun onChooseSendInfo() {
            if (!isLogin) {
                showLoginDialog()
                return
            }
            sharedVM.addressInfo.value = getString(R.string.title_address_send)
            sharedVM.sendAddress.value = vm.sendAddress.value?.copy()
            nav().navigate(R.id.action_navigation_home_to_navigation_home_address_info)
            sharedVM.mainNavBottomVisible.value = View.GONE
        }

        fun onSetInfo() {
            val dialog = SetInfoDialog(activity)
            dialog.show()
            if (weight > 0 || type.isNotEmpty())
                dialog.setInfo(weight, type)
            dialog.setListener { t, w ->
                vm.info.value = "${t}/${w}公斤"
                weight = w
                type = t
                if (distance >= 0.0)
                    initMoney()
            }
        }

        fun onAddMark() {
            val dialog = InputDialog(activity)
            dialog.show()
            if (!vm.remark.value.isNullOrEmpty())
                dialog.setContent(vm.remark.value)
            dialog.setListener { s ->
                vm.remark.value = s
            }
        }

        fun onChooseReceiveInfo() {
            if (!isLogin) {
                showLoginDialog()
                return
            }
            sharedVM.addressInfo.value = getString(R.string.title_address_receive)
            sharedVM.receiveAddress.value = vm.receiveAddress.value?.copy()
            //不能直接赋值
            nav().navigate(R.id.action_navigation_home_to_navigation_home_address_info)
            sharedVM.mainNavBottomVisible.value = View.GONE
        }

        fun onSubmit() {
            if (!isLogin) {
                showLoginDialog()
                return
            }
            AlertDialog.Builder(requireContext())
                .setMessage("确认提交订单?")
                .setPositiveButton("确认") { dialog, _ ->
                    submit()
                    dialog.dismiss()
                }
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        @SuppressLint("SimpleDateFormat")
        fun onChooseBookTime() {
            val picker = LinkagePicker(activity!!)
            picker.setTitle("取件时间")
            picker.setData(TimeProvider())
            picker.setOnDismissListener {
                vm.time.value = timeStr
            }
            picker.setOnLinkagePickedListener { d, t, _ ->
                timeStr = if (d.equals("今天")) {
                    t as String
                } else {
                    "$d $t"
                }
                if (timeStr == "现在")
                    timeStr = "立即发单"
                val ymd = SimpleDateFormat("yyyyMMdd").format(Date())
                time = if (t == "现在")
                    Date().time
                else
                    SimpleDateFormat("yyyyMMddHH:mm").parse("$ymd$t").time
                when (d) {
                    "明天" -> {
                        time += 24 * 60 * 60 * 1000L
                    }
                    "后天" -> {
                        time += 2 * 24 * 60 * 60 * 1000L
                    }
                }
            }
            picker.show()
        }
    }

    private fun submit() {
        if (vm.sendAddress.value?.addressDetail!!.isEmpty()) {
            showShortToast("请填写寄件人信息")
            return
        }
        if (vm.receiveAddress.value?.addressDetail!!.isEmpty()) {
            showShortToast("请填写收件人信息")
            return
        }
        if (weight == 0) {
            showShortToast("请选择类型和重量")
            return
        }
        if (time == 0L)
            time = Date().time
        val orderId = dao.insert(
            createOrder(
                time,
                timeStr,
                vm.sendAddress.value!!,
                vm.receiveAddress.value!!
            )
        )
        if (orderId > 0) {
            val messageDao = AppDatabase.getInstance(context).messageDao()
            messageDao.insert(
                Message(
                    Message.TYPE_ORDER_MESSAGE,
                    "订单消息",
                    orderId.toInt(),
                    "订单提交成功"
                )
            )
            showShortToast("下单成功")
            refresh()
            sharedVM.index.value = R.id.navigation_orders
        } else {
            showShortToast("下单失败")
        }
    }

    private fun createOrder(
        bookTime: Long,
        bookTimeString: String,
        sender: Address,
        receiver: Address
    ): Order {
        val order = Order()
        order.orderNumber = UUID.randomUUID().toString().replace("-".toRegex(), "")
        order.distance = vm.distance.value
        order.mark = vm.remark.value
        order.type = "${type}/${weight}公斤"
        order.bookTimeString = bookTimeString
        order.bookTime = bookTime
        order.orderTime = Date().time
        order.sendAddress = sender.addressDetail
        order.sendName = sender.name
        order.sendPhone = sender.phone
        order.receiveAddress = receiver.addressDetail
        order.receiveName = receiver.name
        order.receivePhone = receiver.phone
        order.money = money
        return order
    }
}