package com.example.cardroom1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingViewModel : ViewModel() {
    private val _appStyle = MutableLiveData(AppStyle("默认", "默认", "默认"))
    val appStyle: LiveData<AppStyle> get() = _appStyle

    fun updateAppStyle(themeColor: String, fontFamily: String, fontSize: String) {
        _appStyle.value = AppStyle(themeColor, fontFamily, fontSize)
    }
}