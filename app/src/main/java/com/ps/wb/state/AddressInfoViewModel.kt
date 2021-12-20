package com.ps.wb.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ps.wb.base.bridge.callback.UnPeekLiveData
import com.ps.wb.base.data.Address

class AddressInfoViewModel: ViewModel() {
    val enableSure=UnPeekLiveData<Boolean>().apply {
        value=false
    }
    val address=UnPeekLiveData<String>().apply {
        value=""
    }
    val phone=UnPeekLiveData<String>().apply {
        value=""
    }
    val door=UnPeekLiveData<String>().apply {
        value=""
    }
    val name=UnPeekLiveData<String>().apply {
        value=""
    }
    var addresses: LiveData<List<Address>>?=null
}