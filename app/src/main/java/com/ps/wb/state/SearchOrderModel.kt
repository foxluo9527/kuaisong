package com.ps.wb.state

import android.view.View
import androidx.lifecycle.ViewModel
import com.ps.wb.base.bridge.callback.UnPeekLiveData
import com.ps.wb.base.data.Order

class SearchOrderModel : ViewModel() {
    val key = UnPeekLiveData<String>().apply {
        value = ""
    }
    val emptyVisible = UnPeekLiveData<Int>().apply {
        value = View.GONE
    }
    val orders = UnPeekLiveData<Order>()
}