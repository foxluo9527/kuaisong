package com.ps.wb.state

import android.view.View
import androidx.lifecycle.ViewModel
import com.ps.wb.base.bridge.callback.UnPeekLiveData
import com.ps.wb.base.data.Order

class OrdersViewModel : ViewModel() {
    val emptyVisible = UnPeekLiveData<Int>().apply {
        value = View.GONE
    }
    val unLoginVisible = UnPeekLiveData<Int>().apply {
        value = View.GONE
    }
    val orders = UnPeekLiveData<Order>()
}