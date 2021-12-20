package com.ps.wb.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MessageViewModel : ViewModel() {
    val name=MutableLiveData<String>()
}