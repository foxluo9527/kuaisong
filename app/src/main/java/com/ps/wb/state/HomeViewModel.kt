package com.ps.wb.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ps.wb.base.data.Address

class HomeViewModel : ViewModel() {
    val sendAddress = MutableLiveData<Address>().apply {
        value= Address("","","","")
    }
    val receiveAddress = MutableLiveData<Address>().apply {
        value= Address("","","","")
    }
    val info=MutableLiveData<String>().apply {
        value=""
    }
    val distance=MutableLiveData<String>().apply {
        value=""
    }
    val money=MutableLiveData<String>().apply {
        value="￥ - - "
    }
    val remark=MutableLiveData<String>().apply {
        value=""
    }
    val time=MutableLiveData<String>().apply {
        value="立即发单"
    }
}