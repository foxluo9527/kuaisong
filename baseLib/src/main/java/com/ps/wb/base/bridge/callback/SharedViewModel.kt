package com.ps.wb.base.bridge.callback

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ps.wb.base.R
import com.ps.wb.base.data.Address
import com.ps.wb.base.data.Order
import com.scrb.baselib.entity.User
import java.util.*

/**
 * SharedViewModel的职责是用于 页面通信的
 */
class SharedViewModel : ViewModel() {
    val showOrder = MutableLiveData<Order>()
    val loginUser = MutableLiveData<User>()

    //是否直接退出程序 BottomNavigationView在后面两个页面返回会退回到默认页面(HomeFragment) 此时应该退出
    val shouldExit = MutableLiveData<Boolean>().apply {
        value = false
    }
    val loginUSerPhone = MutableLiveData<String>().apply {
        value = ""
    }
    val index = MutableLiveData<Int>().apply {
        value = R.id.navigation_home
    }
    val mainNavBottomVisible = MutableLiveData<Int>().apply {
        value = View.VISIBLE
    }
    val addressInfo = MutableLiveData<String>().apply {
        value = ""
    }
    val cityInfo = MutableLiveData<String>().apply {
        value = ""
    }

    val onSendSetDown = MutableLiveData<Boolean>().apply {
        value = false
    }
    val onReceiveSetDown = MutableLiveData<Boolean>().apply {
        value = false
    }
    val onChooseMapDown = MutableLiveData<Boolean>().apply {
        value = false
    }
    val sendAddress = MutableLiveData<Address>().apply {
        value = Address("", "", "", "")
    }
    val receiveAddress = MutableLiveData<Address>().apply {
        value = Address("", "", "", "")
    }
    val chooseAddressInfo = MutableLiveData<HashMap<String, Any>>()
}