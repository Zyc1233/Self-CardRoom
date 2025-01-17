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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.cardroom1.AppStyle
import com.example.cardroom1.SettingViewModel



val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF000000), // 主要使用黑色作为主色调
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF202020), // 稍亮的黑色
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = Color(0xFF404040), // 深灰色作为辅助色
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF606060), // 稍亮的深灰色
    onSecondaryContainer = Color(0xFFFFFFFF),
    tertiary = Color(0xFF808080), // 中等灰色作为点缀色
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFA0A0A0), // 稍亮的中等灰色
    onTertiaryContainer = Color(0xFF000000),
    error = Color(0xFFBA1A1A), // 错误提示颜色
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFF121212), // 深灰色背景
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF1E1E1E), // 稍亮的黑色表面
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF303030), // 稍亮的黑色变体
    onSurfaceVariant = Color(0xFFFFFFFF),
    outline = Color(0xFF606060), // 深灰色轮廓
    outlineVariant = Color(0xFF808080), // 中等灰色变体
    scrim = Color(0xFF000000), // 遮罩颜色
    inverseSurface = Color(0xFFFFFFFF), // 反向表面颜色
    inverseOnSurface = Color(0xFF000000), // 反向表面文字颜色
    inversePrimary = Color(0xFF000000), // 反向主色调
    surfaceDim = Color(0xFF202020), // 稍暗的表面
    surfaceBright = Color(0xFF303030), // 稍亮的表面
    surfaceContainerLowest = Color(0xFF121212), // 最暗的表面容器
    surfaceContainerLow = Color(0xFF1E1E1E), // 较暗的表面容器
    surfaceContainer = Color(0xFF202020), // 默认的表面容器
    surfaceContainerHigh = Color(0xFF303030), // 较亮的表面容器
    surfaceContainerHighest = Color(0xFF404040) // 最亮的表面容器
)

val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)


val WarmColorScheme = lightColorScheme(
    primary = Color(0xFFFF9800), // 更鲜明的橙色
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFFFFDBD1), // 橙色容器
    onPrimaryContainer = Color(0xFF3A0B01),
    secondary = Color(0xFFFFC107), // 琥珀色，与主色搭配良好
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFFFFDBD1), // 琥珀色容器
    onSecondaryContainer = Color(0xFF2C150F),
    tertiary = Color(0xFFFF4500), // 红色作为点缀色
    onTertiary = Color(0xFF000000),
    tertiaryContainer = Color(0xFFF5E1A7), // 红色容器
    onTertiaryContainer = Color(0xFF231B00),
    error = Color(0xFFBA1A1A), // 错误提示颜色
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFF2E0), // 暖色背景
    onBackground = Color(0xFF000000),
    surface = Color(0xFFFFECB3), // 温暖的表面颜色
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFFF5DED8), // 暖色变体
    onSurfaceVariant = Color(0xFF53433F),
    outline = Color(0xFF85736E), // 暖色轮廓
    outlineVariant = Color(0xFFD8C2BC), // 暖色变体
    scrim = Color(0xFF000000), // 遮罩颜色
    inverseSurface = Color(0xFF392E2B), // 反向表面颜色
    inverseOnSurface = Color(0xFFFFEDE8), // 反向表面文字颜色
    inversePrimary = Color(0xFFFFB5A0), // 反向主色调
    surfaceDim = Color(0xFFE8D6D2), // 稍暗的表面
    surfaceBright = Color(0xFFFFF8F6), // 稍亮的表面
    surfaceContainerLowest = Color(0xFFFFFFFF), // 最亮的表面容器
    surfaceContainerLow = Color(0xFFFFF1ED), // 较亮的表面容器
    surfaceContainer = Color(0xFFFCEAE5), // 默认的表面容器
    surfaceContainerHigh = Color(0xFFF7E4E0), // 较暗的表面容器
    surfaceContainerHighest = Color(0xFFF1DFDA) // 最暗的表面容器
)

val ColdColorScheme = lightColorScheme(
    primary = Color(0xFF44ABFB), // 更清新的蓝色
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFFE3F2FD), // 浅蓝色容器
    onPrimaryContainer = Color(0xFF000000),
    secondary = Color(0xFF81D4FA), // 浅蓝色，增强冷色感
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFFE3F2FD), // 浅蓝色容器
    onSecondaryContainer = Color(0xFF000000),
    tertiary = Color(0xFFE1F5E2), // 浅绿色作为点缀色
    onTertiary = Color(0xFF000000),
    tertiaryContainer = Color(0xFFE1F5E2), // 浅绿色容器
    onTertiaryContainer = Color(0xFF000000),
    error = Color(0xFFBA1A1A), // 错误提示颜色
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFF0F8FF), // 冷色背景
    onBackground = Color(0xFF000000),
    surface = Color(0xFFE3F2FD), // 冷色表面颜色
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFFE3F2FD), // 冷色变体
    onSurfaceVariant = Color(0xFF000000),
    outline = Color(0xFF85736E), // 冷色轮廓
    outlineVariant = Color(0xFFD8C2BC), // 冷色变体
    scrim = Color(0xFF000000), // 遮罩颜色
    inverseSurface = Color(0xFF392E2B), // 反向表面颜色
    inverseOnSurface = Color(0xFFFFFFFF), // 反向表面文字颜色
    inversePrimary = Color(0xFF44ABFB), // 反向主色调
    surfaceDim = Color(0xFFE8D6D2), // 稍暗的表面
    surfaceBright = Color(0xFFF0F8FF), // 稍亮的表面
    surfaceContainerLowest = Color(0xFFFFFFFF), // 最亮的表面容器
    surfaceContainerLow = Color(0xFFE3F2FD), // 较亮的表面容器
    surfaceContainer = Color(0xFFF0F8FF), // 默认的表面容器
    surfaceContainerHigh = Color(0xFFE3F2FD), // 较暗的表面容器
    surfaceContainerHighest = Color(0xFFE8D6D2) // 最暗的表面容器
)


@Composable
fun CardRoom1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val viewModel: SettingViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val colorScheme = when (viewModel.themeColorScheme.value) {
        WarmColorScheme -> WarmColorScheme
        ColdColorScheme -> ColdColorScheme
        else -> if (darkTheme) DarkColorScheme else LightColorScheme
    }
    val appStyle by viewModel.appStyle.observeAsState(AppStyle("默认", "默认", "默认"))
    val typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = when (appStyle.fontFamily) {
                "宋体" -> FontFamily.Serif
                "楷体" -> FontFamily.SansSerif
                "斜体" -> FontFamily.Default
                else -> FontFamily.Default
            },
            fontSize = when (appStyle.fontSize) {
                "超大" -> 35.sp
                "大" -> 30.sp
                "默认" -> 25.sp
                "小号" -> 20.sp
                "超小" -> 16.sp
                else -> 16.sp
            },
            fontWeight = FontWeight.Normal,
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
