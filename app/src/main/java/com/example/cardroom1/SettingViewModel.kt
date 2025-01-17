package com.example.cardroom1

import android.app.Application
import android.content.Context
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cardroom1.ui.theme.ColdColorScheme
import com.example.cardroom1.ui.theme.DarkColorScheme
import com.example.cardroom1.ui.theme.LightColorScheme
import com.example.cardroom1.ui.theme.WarmColorScheme

class SettingViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    private val _appStyle = MutableLiveData(
        AppStyle(
            sharedPreferences.getString("themeColor", "默认") ?: "默认",
            sharedPreferences.getString("fontFamily", "默认") ?: "默认",
            sharedPreferences.getString("fontSize", "默认") ?: "默认"
        )
    )
    val appStyle: LiveData<AppStyle> get() = _appStyle

    private val _themeColorScheme = MutableLiveData<ColorScheme>(LightColorScheme)
    val themeColorScheme: LiveData<ColorScheme> get() = _themeColorScheme

    init {
        updateThemeColorScheme(_appStyle.value?.themeColor ?: "默认")
    }

    fun updateAppStyle(themeColor: String, fontFamily: String, fontSize: String) {
        _appStyle.value = AppStyle(themeColor, fontFamily, fontSize)
        sharedPreferences.edit().apply {
            putString("themeColor", themeColor)
            putString("fontFamily", fontFamily)
            putString("fontSize", fontSize)
            apply()
        }
    }

    fun updateThemeColorScheme(themeColor: String) {
        when (themeColor) {
            "深色" -> _themeColorScheme.value = DarkColorScheme
            "浅色" -> _themeColorScheme.value = LightColorScheme
            "暖色" -> _themeColorScheme.value = WarmColorScheme
            "冷色" -> _themeColorScheme.value = ColdColorScheme
            else -> _themeColorScheme.value = LightColorScheme
        }
    }

    @Composable
    fun getFontStyle(fontFamily: String): TextStyle {
        return when (fontFamily) {
            "宋体" -> TextStyle(fontFamily = FontFamily.Cursive)
            "楷体" -> TextStyle(fontFamily = FontFamily.SansSerif)
            "斜体" -> TextStyle(fontStyle = FontStyle.Italic)
            else -> TextStyle() // 默认样式
        }
    }

    @Composable
    fun getFontSize(fontSizeOption: String): TextUnit {
        return when (fontSizeOption) {
            "超大" -> 35.sp
            "大" -> 30.sp
            "默认" -> 25.sp
            "小号" -> 20.sp
            "超小" -> 16.sp
            else -> 25.sp // 默认字体大小
        }
    }
}