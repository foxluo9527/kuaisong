package com.ps.wb.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ps.wb.base.bridge.callback.UnPeekLiveData
import com.ps.wb.repository.APIRepository
import com.scrb.baselib.net.ExceptionHandle
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel : ViewModel() {
    var stateManager = MutableLiveData<String> () // 绑定布局的显示
    val phone = MutableLiveData<String>().apply {
        value = ""
    }
    val pwd = MutableLiveData<String>().apply {
        value = ""
    }
    val loginResult= UnPeekLiveData<String>().apply {
        value = null
    }
    fun login(){
        viewModelScope.launch {
            try {
                loginResult.value=APIRepository().login(phone.value, pwd.value,stateManager)
            }catch (e:Exception){
                ExceptionHandle.handleException(e)
                loginResult.value=null
            }
        }
    }
}