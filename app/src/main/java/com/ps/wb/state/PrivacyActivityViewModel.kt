package com.ps.wb.state

import androidx.lifecycle.ViewModel
import com.ps.wb.base.bridge.callback.UnPeekLiveData

class PrivacyActivityViewModel : ViewModel() {
    val title=UnPeekLiveData<String>()
    val content=UnPeekLiveData<String>()
}