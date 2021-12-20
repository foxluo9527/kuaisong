package com.ps.wb.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ps.wb.api.APIClient
import com.ps.wb.api.ApiService

class APIRepository {

    suspend fun login(username: String?, pwd: String?, stateManager: MutableLiveData<String>)
            : String? {
        if (username.isNullOrEmpty() || pwd.isNullOrEmpty()) {
            stateManager.value = "用户名或密码为空"
            Log.d("api", "用户名或密码为空")
            return null
        }
        return APIClient.instance.instanceRetrofit(ApiService::class.java)
            .loginNew(username, pwd)
    }

    suspend fun register(username: String?, pwd: String?)
            : String? {
        return APIClient.instance.instanceRetrofit(ApiService::class.java)
            .register(username, pwd)
    }

    suspend fun resetPassword(username: String?, pwd: String?)
            : String? {
        return APIClient.instance.instanceRetrofit(ApiService::class.java)
            .resetPassword(username, pwd)
    }

    suspend fun logout(phone: String?)
            : String? {
        return APIClient.instance.instanceRetrofit(ApiService::class.java)
            .logout(phone)
    }

    suspend fun webURL()
            : String? {
        return APIClient.instance.instanceRetrofit(ApiService::class.java)
            .webURL()
    }
}