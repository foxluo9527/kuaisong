package com.ps.wb.state

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ps.wb.base.data.Order
import java.text.SimpleDateFormat
import java.util.*

class DetailViewModel : ViewModel() {
    val order = MutableLiveData<Order>()
    val money = MutableLiveData<String>().apply {
        value = ""
    }
    val sendAddress = MutableLiveData<String>().apply {
        value = ""
    }
    val sendPhone = MutableLiveData<String>().apply {
        value = ""
    }
    val receiveAddress = MutableLiveData<String>().apply {
        value = ""
    }
    val receivePhone = MutableLiveData<String>().apply {
        value = ""
    }
    val type = MutableLiveData<String>().apply {
        value = ""
    }
    val orderTime = MutableLiveData<String>().apply {
        value = ""
    }
    val mark = MutableLiveData<String>().apply {
        value = ""
    }
    val orderNumber = MutableLiveData<String>().apply {
        value = ""
    }
    val cancel = MutableLiveData<String>().apply {
        value = "取消订单"
    }
    val state = MutableLiveData<String>().apply {
        value = ""
    }
    val cancelVisible = MutableLiveData<Int>().apply {
        value = View.VISIBLE
    }

    fun initOrder(order: Order) {
        this.order.value = order
        initData()
    }

    fun initData() {
        val order: Order = this.order.value!!
        orderTime.value = SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(order.orderTime)
        orderNumber.value = order.orderNumber
        type.value = order.type
        mark.value = order.mark
        money.value = "￥ ${order.money}"
        sendAddress.value = order.sendAddress
        receiveAddress.value = order.receiveAddress
        if (order.sendName.isNullOrEmpty())
            sendPhone.value = order.sendPhone
        else
            sendPhone.value = "${order.sendName} ${order.sendPhone}"
        if (order.receiveName.isNullOrEmpty())
            receivePhone.value = order.receivePhone
        else
            receivePhone.value = "${order.receiveName} ${order.receivePhone}"
        if (order.orderState == 0) {
            if (order.bookTime <= Date().time) {
                cancel.value = "取消订单"
                state.value = "待接单"//已到预定时间
            } else {
                cancel.value = "取消预定"
                state.value = "已预定 ${order.bookTimeString}"
            }
            cancelVisible.value = View.VISIBLE
        } else {
            cancelVisible.value = View.GONE
            when (order.orderState) {
                -1 -> state.value = "订单已取消"
                1 -> state.value = "订单待取货"
                2 -> state.value = "配送中"
                3 -> state.value = "订单已完成"
            }
        }
    }
}