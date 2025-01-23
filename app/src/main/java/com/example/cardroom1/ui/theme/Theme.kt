package com.example.cardroom1.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cardroom1.AppStyle
import com.example.cardroom1.R
import com.example.cardroom1.SettingViewModel



val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF2C2C33), // 深色主题主色调
    onPrimary = Color(0xFFE0E0E0),
    primaryContainer = Color(0xFF373740), // 稍亮的深色
    onPrimaryContainer = Color(0xFFE0E0E0),
    secondary = Color(0xFF50505A), // 较深的灰色作为辅助色
    onSecondary = Color(0xFFE0E0E0),
    secondaryContainer = Color(0xFF60606A), // 稍亮的灰色
    onSecondaryContainer = Color(0xFFE0E0E0),
    tertiary = Color(0xFF70707A), // 中等灰色作为点缀色
    onTertiary = Color(0xFFE0E0E0),
    tertiaryContainer = Color(0xFF80808A), // 稍亮的中等灰色
    onTertiaryContainer = Color(0xFF000000),
    error = Color(0xFFE53935), // 错误提示颜色
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFF121212), // 深灰色背景
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E), // 稍亮的黑色表面
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF303030), // 稍亮的黑色变体
    onSurfaceVariant = Color(0xFFE0E0E0),
    outline = Color(0xFF50505A), // 深灰色轮廓
    outlineVariant = Color(0xFF70707A), // 中等灰色变体
    scrim = Color(0xFF000000), // 遮罩颜色
    inverseSurface = Color(0xFFFFFFFF), // 反向表面颜色
    inverseOnSurface = Color(0xFF000000), // 反向表面文字颜色
    inversePrimary = Color(0xFF2C2C33), // 反向主色调
    surfaceDim = Color(0xFF202020), // 稍暗的表面
    surfaceBright = Color(0xFF303030), // 稍亮的表面
    surfaceContainerLowest = Color(0xFF121212), // 最暗的表面容器
    surfaceContainerLow = Color(0xFF1E1E1E), // 较暗的表面容器
    surfaceContainer = Color(0xFF202020), // 默认的表面容器
    surfaceContainerHigh = Color(0xFF303030), // 较亮的表面容器
    surfaceContainerHighest = Color(0xFF404040) // 最亮的表面容器
)

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4C4C54), // 浅色主题主色调
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFFDADADB), // 稍亮的浅色
    onPrimaryContainer = Color(0xFF000000),
    secondary = Color(0xFF60606A), // 较深的灰色作为辅助色
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFFE0E0E0), // 稍亮的灰色
    onSecondaryContainer = Color(0xFF000000),
    tertiary = Color(0xFF70707A), // 中等灰色作为点缀色
    onTertiary = Color(0xFF000000),
    tertiaryContainer = Color(0xFFF0F0F0), // 稍亮的中等灰色
    onTertiaryContainer = Color(0xFF000000),
    error = Color(0xFFBA1A1A), // 错误提示颜色
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFFFF), // 白色背景
    onBackground = Color(0xFF000000),
    surface = Color(0xFFF0F0F0), // 稍亮的白色表面
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFFE0E0E0), // 稍亮的白色变体
    onSurfaceVariant = Color(0xFF000000),
    outline = Color(0xFF60606A), // 浅灰色轮廓
    outlineVariant = Color(0xFF70707A), // 中等灰色变体
    scrim = Color(0xFF000000), // 遮罩颜色
    inverseSurface = Color(0xFF000000), // 反向表面颜色
    inverseOnSurface = Color(0xFFFFFFFF), // 反向表面文字颜色
    inversePrimary = Color(0xFF4C4C54), // 反向主色调
    surfaceDim = Color(0xFFE0E0E0), // 稍暗的表面
    surfaceBright = Color(0xFFFFFFFF), // 稍亮的表面
    surfaceContainerLowest = Color(0xFFFFFFFF), // 最亮的表面容器
    surfaceContainerLow = Color(0xFFF0F0F0), // 较亮的表面容器
    surfaceContainer = Color(0xFFFFFFFF), // 默认的表面容器
    surfaceContainerHigh = Color(0xFFF0F0F0), // 较暗的表面容器
    surfaceContainerHighest = Color(0xFFE0E0E0) // 最暗的表面容器
)

val WarmColorScheme = lightColorScheme(
    primary = Color(0xFFF39C12), // 暖色系主色调
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFE082), // 暖色容器
    onPrimaryContainer = Color(0xFF000000),
    secondary = Color(0xFFFFB64D), // 较暖的橙色作为辅助色
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFE082), // 较暖的橙色容器
    onSecondaryContainer = Color(0xFF000000),
    tertiary = Color(0xFFFF8C00), // 更暖的橙色作为点缀色
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFE082), // 更暖的橙色容器
    onTertiaryContainer = Color(0xFF000000),
    error = Color(0xFFE53935), // 错误提示颜色
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFF5E0), // 暖色背景
    onBackground = Color(0xFF000000),
    surface = Color(0xFFFFE082), // 温暖的表面颜色
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFFFFE082), // 暖色变体
    onSurfaceVariant = Color(0xFF000000),
    outline = Color(0xFF60606A), // 暖色轮廓
    outlineVariant = Color(0xFF70707A), // 暖色变体
    scrim = Color(0xFF000000), // 遮罩颜色
    inverseSurface = Color(0xFFFFFFFF), // 反向表面颜色
    inverseOnSurface = Color(0xFF000000), // 反向表面文字颜色
    inversePrimary = Color(0xFFF39C12), // 反向主色调
    surfaceDim = Color(0xFFE0E0E0), // 稍暗的表面
    surfaceBright = Color(0xFFFFFFFF), // 稍亮的表面
    surfaceContainerLowest = Color(0xFFFFFFFF), // 最亮的表面容器
    surfaceContainerLow = Color(0xFFFFE082), // 较亮的表面容器
    surfaceContainer = Color(0xFFFFFFFF), // 默认的表面容器
    surfaceContainerHigh = Color(0xFFFFE082), // 较暗的表面容器
    surfaceContainerHighest = Color(0xFFE0E0E0) // 最暗的表面容器
)

val ColdColorScheme = lightColorScheme(
    primary = Color(0xFF3498DB), // 冷色系主色调
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF85C1E9), // 冷色容器
    onPrimaryContainer = Color(0xFF000000),
    secondary = Color(0xFF34495E), // 较冷的蓝色作为辅助色
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF85C1E9), // 较冷的蓝色容器
    onSecondaryContainer = Color(0xFF000000),
    tertiary = Color(0xFF2980B9), // 更冷的蓝色作为点缀色
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF85C1E9), // 更冷的蓝色容器
    onTertiaryContainer = Color(0xFF000000),
    error = Color(0xFFE53935), // 错误提示颜色
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFECF0F1), // 冷色背景
    onBackground = Color(0xFF000000),
    surface = Color(0xFF85C1E9), // 冷色表面颜色
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFF85C1E9), // 冷色变体
    onSurfaceVariant = Color(0xFF000000),
    outline = Color(0xFF34495E), // 冷色轮廓
    outlineVariant = Color(0xFF2980B9), // 冷色变体
    scrim = Color(0xFF000000), // 遮罩颜色
    inverseSurface = Color(0xFFFFFFFF), // 反向表面颜色
    inverseOnSurface = Color(0xFF000000), // 反向表面文字颜色
    inversePrimary = Color(0xFF3498DB), // 反向主色调
    surfaceDim = Color(0xFFE0E0E0), // 稍暗的表面
    surfaceBright = Color(0xFFFFFFFF), // 稍亮的表面
    surfaceContainerLowest = Color(0xFFFFFFFF), // 最亮的表面容器
    surfaceContainerLow = Color(0xFF85C1E9), // 较亮的表面容器
    surfaceContainer = Color(0xFFFFFFFF), // 默认的表面容器
    surfaceContainerHigh = Color(0xFF85C1E9), // 较暗的表面容器
    surfaceContainerHighest = Color(0xFFE0E0E0) // 最暗的表面容器
)


val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("Lobster Two")

val fontFamily = FontFamily(
    Font(googleFont = fontName, fontProvider = provider)
)

@Composable
fun CardRoom1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val viewModel: SettingViewModel = viewModel()
    val colorScheme = when (viewModel.themeColorScheme.value) {
        WarmColorScheme -> WarmColorScheme
        ColdColorScheme -> ColdColorScheme
        else -> if (darkTheme) DarkColorScheme else LightColorScheme
    }
    val appStyle by viewModel.appStyle.observeAsState(AppStyle("默认", "默认", "默认"))

    val typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontStyle = getFontStyle(appStyle.fontFamily).fontStyle,
            fontSize = getFontSize(appStyle.fontSize),
            fontWeight = getFontStyle(appStyle.fontFamily).fontWeight,
            color = getFontStyle(appStyle.fontFamily).color,
            textDecoration = getFontStyle(appStyle.fontFamily).textDecoration,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}

@Composable
fun getFontStyle(fontFamily: String): TextStyle {
    return when (fontFamily) {
        "粗体" -> TextStyle(fontWeight = FontWeight.Bold)
        "斜体" -> TextStyle(fontStyle = FontStyle.Italic)
        "渐变" -> TextStyle(
            fontWeight = FontWeight.W200,
            color = Color(0xFF425AD7), // 渐变颜色
            textDecoration = TextDecoration.None // 渐变效果
        )
        else -> TextStyle(fontWeight = FontWeight.Normal)
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