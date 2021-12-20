package com.ps.wb.state

import androidx.lifecycle.ViewModel
import com.ps.wb.base.bridge.callback.UnPeekLiveData

class AboutViewModel : ViewModel() {
    val version=UnPeekLiveData<String>().apply {
        value="V 1.0.0"
    }
}