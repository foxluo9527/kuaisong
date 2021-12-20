package com.ps.wb.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResetViewModel : ViewModel() {
    val phone=MutableLiveData<String>().apply {
        value=""
    }
    val pwd=MutableLiveData<String>().apply {
        value=""
    }
    val pwdAgain=MutableLiveData<String>().apply {
        value=""
    }
    val enable=MutableLiveData<Boolean>().apply {
        value=false
    }
}