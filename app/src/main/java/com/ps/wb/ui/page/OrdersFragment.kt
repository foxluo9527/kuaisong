package com.ps.wb.ui.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ps.wb.R
import com.ps.wb.base.data.AppDatabase
import com.ps.wb.base.data.Message
import com.ps.wb.base.data.Order
import com.ps.wb.base.data.OrderDao
import com.ps.wb.base.ui.adapter.SimpleBaseBindingAdapter
import com.ps.wb.databinding.FragmentOrdersBinding
import com.ps.wb.databinding.ItemOrderBinding
import com.ps.wb.state.OrdersViewModel
import com.ps.wb.ui.LoginActivity
import com.ps.wb.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StaticFieldLeak", "SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged")
class OrdersFragment : BaseFragment() {
    private lateinit var dao: OrderDao
    private var adapter: SimpleBaseBindingAdapter<Order?, ItemOrderBinding?>? =
        null
    private lateinit var vm: OrdersViewModel
    private var _binding: FragmentOrdersBinding? = null

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
            ViewModelProvider(this).get(OrdersViewModel::class.java)

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        _binding!!.lifecycleOwner = this
        _binding?.vm = vm
        _binding?.click = ClickProxy()
        _binding?.shareVm = sharedVM
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedVM.mainNavBottomVisible.value = View.VISIBLE
        _binding?.rvOrder?.layoutManager = LinearLayoutManager(context)
        adapter = object : SimpleBaseBindingAdapter<Order?, ItemOrderBinding?>
            (context, R.layout.item_order) {
            override fun onSimpleBindItem(
                binding: ItemOrderBinding?,
                item: Order?,
                holder: RecyclerView.ViewHolder?
            ) {
                binding!!.tvType.text = item!!.type
                binding.sendAddress.text = item.sendAddress
                binding.sendPhone.text = item.sendPhone
                binding.receiveAddress.text = item.receiveAddress
                binding.receivePhone.text = item.receivePhone
                binding.tvTime.text = SimpleDateFormat("yyyy-MM-dd").format(item.orderTime)
                if (item.orderState == 0) {
                    if (item.bookTime <= Date().time) {
                        binding.btnCancel.text = "取消订单"
                        binding.tvState.text = "待接单"//已到预定时间
                    } else {
                        binding.btnCancel.text = "取消预定"
                        binding.tvState.text = "已预定 ${item.bookTimeString}"
                    }
                    binding.btnCancel.visibility = View.VISIBLE
                } else {
                    binding.btnCancel.visibility = View.GONE
                    when (item.orderState) {
                        -1 -> binding.tvState.text = "订单已取消"
                        1 -> binding.tvState.text = "订单待取货"
                        2 -> binding.tvState.text = "配送中"
                        3 -> binding.tvState.text = "订单已完成"
                    }
                }
                binding.root.setOnClickListener {
                    sharedVM.showOrder.value = item
                    nav().navigate(R.id.action_navigation_orders_to_detailFragment)
                    sharedVM.mainNavBottomVisible.value = View.GONE
                    sharedVM.shouldExit.value = false
                }
                binding.btnCancel.setOnClickListener {
                    AlertDialog.Builder(requireContext())
                        .setMessage("确认取消此订单?")
                        .setPositiveButton("确认") { dialog, _ ->
                            item.orderState = -1
                            dao.update(item)
                            val messageDao = AppDatabase.getInstance(context).messageDao()
                            messageDao.insert(
                                Message(
                                    Message.TYPE_ORDER_MESSAGE,
                                    "订单消息",
                                    item.id,
                                    "订单已取消"
                                )
                            )
                            adapter?.notifyDataSetChanged()
                            dialog.dismiss()
                            if (adapter?.list?.size == 0) {
                                vm.emptyVisible.value = View.VISIBLE
                                _binding?.rvOrder?.visibility = View.GONE
                            }
                        }
                        .setNegativeButton("取消") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
        _binding?.rvOrder?.adapter = adapter
        getData()
        refresh.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    delay(500)
                }
                getData()
                refresh.isRefreshing = false
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        adapter?.list = dao.all
        adapter?.notifyDataSetChanged()
        if (adapter!!.list != null && adapter!!.list!!.size > 0) {
            _binding?.rvOrder?.visibility = View.VISIBLE
            vm.emptyVisible.value = View.GONE
        } else {
            vm.emptyVisible.value = View.VISIBLE
            _binding?.rvOrder?.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isLogin)
            vm.unLoginVisible.value = View.VISIBLE
        else
            vm.unLoginVisible.value = View.GONE
        sharedVM.shouldExit.value = true
    }

    inner class ClickProxy {
        fun search() {
            if (isLogin) {
                nav().navigate(R.id.action_navigation_orders_to_searchOrderFragment)
                sharedVM.mainNavBottomVisible.value = View.GONE
                sharedVM.shouldExit.value = false
            } else
                showLoginDialog()
        }

        fun login() {
            gotoActivity(LoginActivity::class.java)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}