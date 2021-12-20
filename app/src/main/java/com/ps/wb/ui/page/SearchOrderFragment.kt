package com.ps.wb.ui.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ps.wb.R
import com.ps.wb.base.data.AppDatabase
import com.ps.wb.base.data.Order
import com.ps.wb.base.data.OrderDao
import com.ps.wb.base.ui.adapter.SimpleBaseBindingAdapter
import com.ps.wb.databinding.FragmentSearchOrderBinding
import com.ps.wb.databinding.ItemOrderBinding
import com.ps.wb.state.SearchOrderModel
import com.ps.wb.ui.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

class SearchOrderFragment : BaseFragment() {
    private lateinit var dao: OrderDao
    private var adapter: SimpleBaseBindingAdapter<Order?, ItemOrderBinding?>? =
        null
    private lateinit var vm: SearchOrderModel
    private var _binding: FragmentSearchOrderBinding? = null

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
            ViewModelProvider(this).get(SearchOrderModel::class.java)

        _binding = FragmentSearchOrderBinding.inflate(inflater, container, false)
        _binding!!.lifecycleOwner = this
        _binding?.vm = vm
        _binding?.click = ClickProxy()
        _binding?.shareVm = sharedVM
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.rvOrder?.layoutManager = LinearLayoutManager(context)
        adapter = object : SimpleBaseBindingAdapter<Order?, ItemOrderBinding?>
            (context, R.layout.item_order) {
            @SuppressLint("SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged")
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
                    sharedVM.showOrder.value=item
                    nav().navigate(R.id.action_searchOrdeFragment_to_detailFragment)
                }
                binding.btnCancel.setOnClickListener {
                    AlertDialog.Builder(requireContext())
                        .setMessage("确认取消此订单?")
                        .setPositiveButton("确认") { dialog, _ ->
                            item.orderState = -1
                            dao.update(item)
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
        search(vm.key.value!!)
        vm.key.observe(viewLifecycleOwner) {
            search(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun search(key: String) {
        if (key.isEmpty())
            adapter?.list = dao.all
        else
            adapter?.list = dao.search("%$key%")
        adapter?.notifyDataSetChanged()
        if (adapter!!.list != null && adapter!!.list!!.size > 0) {
            _binding?.rvOrder?.visibility = View.VISIBLE
            vm.emptyVisible.value = View.GONE
        } else {
            vm.emptyVisible.value = View.VISIBLE
            _binding?.rvOrder?.visibility = View.GONE
        }
    }

    inner class ClickProxy {
        fun onBackPress() {
            nav().navigateUp()
            sharedVM.mainNavBottomVisible.value = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}