package com.ps.wb.ui.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ps.wb.R
import com.ps.wb.base.data.Address
import com.ps.wb.base.data.AddressDao
import com.ps.wb.base.data.AppDatabase
import com.ps.wb.base.ui.adapter.SimpleBaseBindingAdapter
import com.ps.wb.databinding.FragmentAddressManageBinding
import com.ps.wb.databinding.ItemAddressBinding
import com.ps.wb.state.AddressManageViewModel
import com.ps.wb.ui.base.BaseFragment


class AddressManageFragment : BaseFragment() {
    private lateinit var dao: AddressDao
    private lateinit var vm: AddressManageViewModel
    private var _binding: FragmentAddressManageBinding? = null
    private var adapter: SimpleBaseBindingAdapter<Address?, ItemAddressBinding?>? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dao = AppDatabase.getInstance(context).addressDao()
        vm =
            getFragmentViewModelProvider(this).get(AddressManageViewModel::class.java)
        _binding = FragmentAddressManageBinding.inflate(inflater, container, false)
        _binding?.lifecycleOwner = this
        _binding?.vm = vm
        _binding?.shareVm = sharedVM
        _binding?.click = ClickProxy()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.rvAddressInfo?.layoutManager = LinearLayoutManager(context)
        adapter = object : SimpleBaseBindingAdapter<Address?, ItemAddressBinding?>
            (context, R.layout.item_address) {
            @SuppressLint("SetTextI18n")
            override fun onSimpleBindItem(
                binding: ItemAddressBinding?,
                item: Address?,
                holder: RecyclerView.ViewHolder?
            ) {
                binding!!.root.setOnClickListener {
                }
                binding.ivDel.setOnClickListener {
                    if (item != null) {
                        delItem(item)
                    }
                }
                binding.tvAddress.text = item!!.addressDetail
                if (!TextUtils.isEmpty(item.name))
                    binding.tvNamePhone.text = "${item.name}   ${item.phone}"
                else
                    binding.tvNamePhone.text = item.phone
            }
        }
        _binding?.rvAddressInfo?.adapter = adapter
        vm.addresses = dao.all as LiveData<List<Address>>
        vm.addresses?.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty())
                vm.emptyVisible.value = View.VISIBLE
            adapter?.list = it
            adapter?.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun delItem(item: Address) {
        AlertDialog.Builder(requireContext())
            .setMessage("确认删除此地址信息?")
            .setPositiveButton("确认") { dialog, _ ->
                dao.delete(item)
                adapter?.list?.remove(item)
                adapter?.notifyDataSetChanged()
                dialog.dismiss()
                if (adapter?.list?.size == 0)
                    vm.emptyVisible.value = View.VISIBLE
            }
            .setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class ClickProxy {
        fun onBackPress() {
            nav().navigateUp()
            sharedVM.mainNavBottomVisible.value=View.VISIBLE
        }
    }
}