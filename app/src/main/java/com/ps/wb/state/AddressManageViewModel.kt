package com.ps.wb.state

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ps.wb.base.bridge.callback.UnPeekLiveData
import com.ps.wb.base.data.Address

class AddressManageViewModel : ViewModel() {
    val emptyVisible = UnPeekLiveData<Int>().apply {
        value = View.GONE
    }
    var addresses :LiveData<List<Address>>?=null
}