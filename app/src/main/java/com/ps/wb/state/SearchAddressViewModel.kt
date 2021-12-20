package com.ps.wb.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baidu.mapapi.search.sug.SuggestionSearch

class SearchAddressViewModel : ViewModel() {

    val searchContent = MutableLiveData<String>().apply {
        value = ""
    }
    val mSuggestionSearch = MutableLiveData<SuggestionSearch>().apply {
        value = SuggestionSearch.newInstance()
    }
}