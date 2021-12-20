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
import com.baidu.mapapi.model.LatLng
import com.ps.wb.R
import com.ps.wb.base.data.Address
import com.ps.wb.base.data.AddressDao
import com.ps.wb.base.data.AppDatabase
import com.ps.wb.base.ui.adapter.SimpleBaseBindingAdapter
import com.ps.wb.databinding.FragmentAddressInfoBinding
import com.ps.wb.databinding.ItemAddressBinding
import com.ps.wb.state.AddressInfoViewModel
import com.ps.wb.ui.base.BaseFragment
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException


class AddressInfoFragment : BaseFragment() {
    private lateinit var dao: AddressDao
    private lateinit var vm: AddressInfoViewModel
    private var _binding: FragmentAddressInfoBinding? = null
    private var onEditSendInfo = true
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
            getFragmentViewModelProvider(this).get(AddressInfoViewModel::class.java)
        _binding = FragmentAddressInfoBinding.inflate(inflater, container, false)
        _binding?.lifecycleOwner = this
        _binding?.vm = vm
        _binding?.shareVm = sharedVM
        _binding?.click = ClickProxy()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onEditSendInfo = sharedVM.addressInfo.value.equals(getString(R.string.title_address_send))
        val address: Address = if (onEditSendInfo) {
            sharedVM.sendAddress.value!!
        } else {
            sharedVM.receiveAddress.value!!
        }
        vm.phone.value = address.phone.toString()
        vm.door.value = address.doorNumber.toString()
        vm.name.value = address.name.toString()
        vm.address.value = address.addressDetail.toString()

        sharedVM.sendAddress.observe(viewLifecycleOwner) {
            if (onEditSendInfo)
                vm.enableSure.value = (it.phone != "" && it.addressDetail != "")
        }

        sharedVM.receiveAddress.observe(viewLifecycleOwner) {
            if (!onEditSendInfo)
                vm.enableSure.value = (it.phone != "" && it.addressDetail != "")
        }

        vm.phone.observe(viewLifecycleOwner) {
            //观察输入
            if (checkPhone(it)) {
                vm.enableSure.value = vm.address.value != ""
                if (onEditSendInfo)
                    sharedVM.sendAddress.value?.phone = it
                else
                    sharedVM.receiveAddress.value?.phone = it
            } else {
                vm.enableSure.value = false
            }
        }
        vm.name.observe(viewLifecycleOwner) {
            if (onEditSendInfo)
                sharedVM.sendAddress.value?.name = it
            else
                sharedVM.receiveAddress.value?.name = it
        }
        vm.door.observe(viewLifecycleOwner) {
            if (onEditSendInfo)
                sharedVM.sendAddress.value?.doorNumber = it
            else
                sharedVM.receiveAddress.value?.doorNumber = it
        }

        if (sharedVM.onChooseMapDown.value == true &&
            sharedVM.chooseAddressInfo.value != null &&
            !TextUtils.isEmpty(sharedVM.chooseAddressInfo.value!!["addressName"] as String)
        ) {
            //地址填写成功
            vm.address.value =
                sharedVM.chooseAddressInfo.value!!["addressName"] as String
            if (onEditSendInfo) {
                //寄件人
                sharedVM.sendAddress.value?.addressDetail =
                    sharedVM.chooseAddressInfo.value!!["addressName"] as String//获取的地址信息
                sharedVM.sendAddress.value?.lg =
                    (sharedVM.chooseAddressInfo.value!!["pt"] as LatLng).longitude
                sharedVM.sendAddress.value?.lt =
                    (sharedVM.chooseAddressInfo.value!!["pt"] as LatLng).latitude
            } else {
                //收件人
                sharedVM.receiveAddress.value?.addressDetail =
                    sharedVM.chooseAddressInfo.value!!["addressName"] as String//获取的地址信息
                sharedVM.receiveAddress.value?.lg =
                    (sharedVM.chooseAddressInfo.value!!["pt"] as LatLng).longitude
                sharedVM.receiveAddress.value?.lt =
                    (sharedVM.chooseAddressInfo.value!!["pt"] as LatLng).latitude
            }
        }
        sharedVM.onChooseMapDown.value = false
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
                    if (onEditSendInfo) {
                        sharedVM.onSendSetDown.value = true
                        sharedVM.sendAddress.value = item
                    } else {
                        sharedVM.onReceiveSetDown.value = true
                        sharedVM.receiveAddress.value = item
                    }
                    nav().navigateUp()
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
            if (it.isNotEmpty())
                _binding?.llEmpty?.visibility = View.GONE
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
                    _binding?.llEmpty?.visibility = View.VISIBLE
            }
            .setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    @Throws(PatternSyntaxException::class)
    fun checkPhone(str: String?): Boolean {
        val regExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$"
        val p = Pattern.compile(regExp)
        val m: Matcher = p.matcher(str)
        return m.matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class ClickProxy {
        fun onBackPress() {
            nav().navigateUp()
        }

        fun onChooseAddress() {
            nav().navigate(R.id.action_navigation_home_address_info_to_searchAddressFragment)
        }

        fun onSurePress() {
            if (onEditSendInfo) {
                sharedVM.onSendSetDown.value = true
                sharedVM.sendAddress.value?.aId= dao.insert(sharedVM.sendAddress.value).toInt()
            } else {
                sharedVM.onReceiveSetDown.value = true
                sharedVM.receiveAddress.value?.aId= dao.insert(sharedVM.receiveAddress.value).toInt()
            }
            nav().navigateUp()
        }
    }
}