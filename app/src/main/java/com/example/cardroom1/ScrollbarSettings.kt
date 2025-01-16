package com.example.cardroom1

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
data class ScrollbarSettings(
    val enabled: Boolean = true,
    val side: ScrollbarLayoutSide = ScrollbarLayoutSide.Start,
    val alwaysShowScrollbar: Boolean = false,
    val scrollbarPadding: Dp = 8.dp,
    val thumbThickness: Dp = 6.dp,
    val thumbShape: Shape = CircleShape,
    val thumbMinLength: Float = 0.1f,
    val thumbMaxLength: Float = 1.0f,
    val thumbUnselectedColor: Color = Color(0xFF6F9DF8),
    val thumbSelectedColor: Color = Color(0xFF5281CA),
    val selectionMode: ScrollbarSelectionMode = ScrollbarSelectionMode.Thumb,
    val selectionActionable: ScrollbarSelectionActionable = ScrollbarSelectionActionable.Always,
    val hideDelayMillis: Int = 400,
    val hideDisplacement: Dp = 14.dp,
    val hideEasingAnimation: Easing = FastOutSlowInEasing,
    val durationAnimationMillis: Int = 500,
) {
    init {
        require(thumbMinLength <= thumbMaxLength) {
            "thumbMinLength ($thumbMinLength) must be less or equal to thumbMaxLength ($thumbMaxLength)"
        }
    }

    companion object {
        val Default = ScrollbarSettings()
    }
}

enum class ScrollbarLayoutSide {
    Start, End
}

enum class ScrollbarSelectionMode {
    Disabled, Full, Thumb
}

enum class ScrollbarSelectionActionable {
    Always, WhenVisible
}