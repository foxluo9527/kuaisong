package com.ps.wb.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MineViewModel : ViewModel() {
    val name=MutableLiveData<String>().apply {
        value = ""
    }
}