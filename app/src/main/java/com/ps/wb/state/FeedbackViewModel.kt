package com.ps.wb.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedbackViewModel : ViewModel() {
    val pictureNumber=MutableLiveData<String>().apply {
        value = ""
    }
    val contentNumber=MutableLiveData<String>().apply {
        value = "0/50字"
    }
    val content=MutableLiveData<String>().apply {
        value = ""
    }
    val phone = MutableLiveData<String>().apply {
        value = ""
    }
    val name = MutableLiveData<String>().apply {
        value = ""
    }
}